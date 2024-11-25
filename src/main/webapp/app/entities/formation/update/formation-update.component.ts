import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { FormationFormService, FormationFormGroup } from './formation-form.service';
import { IFormation } from '../formation.model';
import { FormationService } from '../service/formation.service';
import { IResume } from 'app/entities/resume/resume.model';
import { ResumeService } from 'app/entities/resume/service/resume.service';

@Component({
  selector: 'jhi-formation-update',
  templateUrl: './formation-update.component.html',
})
export class FormationUpdateComponent implements OnInit {
  isSaving = false;
  formation: IFormation | null = null;

  resumesSharedCollection: IResume[] = [];

  editForm: FormationFormGroup = this.formationFormService.createFormationFormGroup();

  constructor(
    protected formationService: FormationService,
    protected formationFormService: FormationFormService,
    protected resumeService: ResumeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareResume = (o1: IResume | null, o2: IResume | null): boolean => this.resumeService.compareResume(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ formation }) => {
      this.formation = formation;
      if (formation) {
        this.updateForm(formation);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const formation = this.formationFormService.getFormation(this.editForm);
    if (formation.id !== null) {
      this.subscribeToSaveResponse(this.formationService.update(formation));
    } else {
      this.subscribeToSaveResponse(this.formationService.create(formation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFormation>>): void {
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

  protected updateForm(formation: IFormation): void {
    this.formation = formation;
    this.formationFormService.resetForm(this.editForm, formation);

    this.resumesSharedCollection = this.resumeService.addResumeToCollectionIfMissing<IResume>(
      this.resumesSharedCollection,
      formation.resume
    );
  }

  protected loadRelationshipsOptions(): void {
    this.resumeService
      .query()
      .pipe(map((res: HttpResponse<IResume[]>) => res.body ?? []))
      .pipe(map((resumes: IResume[]) => this.resumeService.addResumeToCollectionIfMissing<IResume>(resumes, this.formation?.resume)))
      .subscribe((resumes: IResume[]) => (this.resumesSharedCollection = resumes));
  }
}
