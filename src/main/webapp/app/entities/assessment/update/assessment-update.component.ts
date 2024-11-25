import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { AssessmentFormService, AssessmentFormGroup } from './assessment-form.service';
import { IAssessment } from '../assessment.model';
import { AssessmentService } from '../service/assessment.service';
import { IQuestion } from 'app/entities/question/question.model';
import { QuestionService } from 'app/entities/question/service/question.service';

@Component({
  selector: 'jhi-assessment-update',
  templateUrl: './assessment-update.component.html',
})
export class AssessmentUpdateComponent implements OnInit {
  isSaving = false;
  assessment: IAssessment | null = null;

  questionsSharedCollection: IQuestion[] = [];

  editForm: AssessmentFormGroup = this.assessmentFormService.createAssessmentFormGroup();

  constructor(
    protected assessmentService: AssessmentService,
    protected assessmentFormService: AssessmentFormService,
    protected questionService: QuestionService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareQuestion = (o1: IQuestion | null, o2: IQuestion | null): boolean => this.questionService.compareQuestion(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assessment }) => {
      this.assessment = assessment;
      if (assessment) {
        this.updateForm(assessment);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const assessment = this.assessmentFormService.getAssessment(this.editForm);
    if (assessment.id !== null) {
      this.subscribeToSaveResponse(this.assessmentService.update(assessment));
    } else {
      this.subscribeToSaveResponse(this.assessmentService.create(assessment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAssessment>>): void {
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

  protected updateForm(assessment: IAssessment): void {
    this.assessment = assessment;
    this.assessmentFormService.resetForm(this.editForm, assessment);

    this.questionsSharedCollection = this.questionService.addQuestionToCollectionIfMissing<IQuestion>(
      this.questionsSharedCollection,
      ...(assessment.questions ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.questionService
      .query()
      .pipe(map((res: HttpResponse<IQuestion[]>) => res.body ?? []))
      .pipe(
        map((questions: IQuestion[]) =>
          this.questionService.addQuestionToCollectionIfMissing<IQuestion>(questions, ...(this.assessment?.questions ?? []))
        )
      )
      .subscribe((questions: IQuestion[]) => (this.questionsSharedCollection = questions));
  }
}
