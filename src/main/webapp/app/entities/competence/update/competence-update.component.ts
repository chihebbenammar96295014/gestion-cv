import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CompetenceFormService, CompetenceFormGroup } from './competence-form.service';
import { ICompetence } from '../competence.model';
import { CompetenceService } from '../service/competence.service';
import { IResume } from 'app/entities/resume/resume.model';
import { ResumeService } from 'app/entities/resume/service/resume.service';

@Component({
  selector: 'jhi-competence-update',
  templateUrl: './competence-update.component.html',
})
export class CompetenceUpdateComponent implements OnInit {
  isSaving = false;
  competence: ICompetence | null = null;

  resumesSharedCollection: IResume[] = [];

  editForm: CompetenceFormGroup = this.competenceFormService.createCompetenceFormGroup();

  constructor(
    protected competenceService: CompetenceService,
    protected competenceFormService: CompetenceFormService,
    protected resumeService: ResumeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareResume = (o1: IResume | null, o2: IResume | null): boolean => this.resumeService.compareResume(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ competence }) => {
      this.competence = competence;
      if (competence) {
        this.updateForm(competence);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const competence = this.competenceFormService.getCompetence(this.editForm);
    if (competence.id !== null) {
      this.subscribeToSaveResponse(this.competenceService.update(competence));
    } else {
      this.subscribeToSaveResponse(this.competenceService.create(competence));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompetence>>): void {
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

  protected updateForm(competence: ICompetence): void {
    this.competence = competence;
    this.competenceFormService.resetForm(this.editForm, competence);

    this.resumesSharedCollection = this.resumeService.addResumeToCollectionIfMissing<IResume>(
      this.resumesSharedCollection,
      competence.resume
    );
  }

  protected loadRelationshipsOptions(): void {
    this.resumeService
      .query()
      .pipe(map((res: HttpResponse<IResume[]>) => res.body ?? []))
      .pipe(map((resumes: IResume[]) => this.resumeService.addResumeToCollectionIfMissing<IResume>(resumes, this.competence?.resume)))
      .subscribe((resumes: IResume[]) => (this.resumesSharedCollection = resumes));
  }
}
