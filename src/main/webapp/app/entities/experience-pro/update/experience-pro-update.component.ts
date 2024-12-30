import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ExperienceProFormService, ExperienceProFormGroup } from './experience-pro-form.service';
import { IExperiencePro } from '../experience-pro.model';
import { ExperienceProService } from '../service/experience-pro.service';
import { IResume } from 'app/entities/resume/resume.model';
import { ResumeService } from 'app/entities/resume/service/resume.service';

@Component({
  selector: 'jhi-experience-pro-update',

  templateUrl: './experience-pro-update.component.html',
})
export class ExperienceProUpdateComponent implements OnInit {
  isSaving = false;
  experiencePro: IExperiencePro | null = null;

  resumesSharedCollection: IResume[] = [];

  editForm: ExperienceProFormGroup = this.experienceProFormService.createExperienceProFormGroup();

  constructor(
    protected experienceProService: ExperienceProService,
    protected experienceProFormService: ExperienceProFormService,
    protected resumeService: ResumeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareResume = (o1: IResume | null, o2: IResume | null): boolean => this.resumeService.compareResume(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ experiencePro }) => {
      this.experiencePro = experiencePro;
      if (experiencePro) {
        this.updateForm(experiencePro);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const experiencePro = this.experienceProFormService.getExperiencePro(this.editForm);
    if (experiencePro.id !== null) {
      this.subscribeToSaveResponse(this.experienceProService.update(experiencePro));
    } else {
      this.subscribeToSaveResponse(this.experienceProService.create(experiencePro));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExperiencePro>>): void {
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

  protected updateForm(experiencePro: IExperiencePro): void {
    this.experiencePro = experiencePro;
    this.experienceProFormService.resetForm(this.editForm, experiencePro);

    this.resumesSharedCollection = this.resumeService.addResumeToCollectionIfMissing<IResume>(
      this.resumesSharedCollection,
      experiencePro.resume
    );
  }

  protected loadRelationshipsOptions(): void {
    this.resumeService
      .query()
      .pipe(map((res: HttpResponse<IResume[]>) => res.body ?? []))
      .pipe(map((resumes: IResume[]) => this.resumeService.addResumeToCollectionIfMissing<IResume>(resumes, this.experiencePro?.resume)))
      .subscribe((resumes: IResume[]) => (this.resumesSharedCollection = resumes));
  }
}
