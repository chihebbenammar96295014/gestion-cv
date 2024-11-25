import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { AppUserFormService, AppUserFormGroup } from './app-user-form.service';
import { IAppUser } from '../app-user.model';
import { AppUserService } from '../service/app-user.service';
import { IResume } from 'app/entities/resume/resume.model';
import { ResumeService } from 'app/entities/resume/service/resume.service';
import { Role } from 'app/entities/enumerations/role.model';

@Component({
  selector: 'jhi-app-user-update',
  templateUrl: './app-user-update.component.html',
})
export class AppUserUpdateComponent implements OnInit {
  isSaving = false;
  appUser: IAppUser | null = null;
  roleValues = Object.keys(Role);

  resumesCollection: IResume[] = [];

  editForm: AppUserFormGroup = this.appUserFormService.createAppUserFormGroup();

  constructor(
    protected appUserService: AppUserService,
    protected appUserFormService: AppUserFormService,
    protected resumeService: ResumeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareResume = (o1: IResume | null, o2: IResume | null): boolean => this.resumeService.compareResume(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appUser }) => {
      this.appUser = appUser;
      if (appUser) {
        this.updateForm(appUser);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const appUser = this.appUserFormService.getAppUser(this.editForm);
    if (appUser.id !== null) {
      this.subscribeToSaveResponse(this.appUserService.update(appUser));
    } else {
      this.subscribeToSaveResponse(this.appUserService.create(appUser));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAppUser>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(appUser: IAppUser): void {
    this.appUser = appUser;
    this.appUserFormService.resetForm(this.editForm, appUser);

    this.resumesCollection = this.resumeService.addResumeToCollectionIfMissing<IResume>(this.resumesCollection, appUser.resume);
  }

  protected loadRelationshipsOptions(): void {
    this.resumeService
      .query({ filter: 'appuser-is-null' })
      .pipe(map((res: HttpResponse<IResume[]>) => res.body ?? []))
      .pipe(map((resumes: IResume[]) => this.resumeService.addResumeToCollectionIfMissing<IResume>(resumes, this.appUser?.resume)))
      .subscribe((resumes: IResume[]) => (this.resumesCollection = resumes));
  }
}
