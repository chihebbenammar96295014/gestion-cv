import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ReponseFormService, ReponseFormGroup } from './reponse-form.service';
import { IReponse } from '../reponse.model';
import { ReponseService } from '../service/reponse.service';
import { IFeedbackTest } from 'app/entities/feedback-test/feedback-test.model';
import { FeedbackTestService } from 'app/entities/feedback-test/service/feedback-test.service';

@Component({
  selector: 'jhi-reponse-update',
  templateUrl: './reponse-update.component.html',
})
export class ReponseUpdateComponent implements OnInit {
  isSaving = false;
  reponse: IReponse | null = null;

  feedbackTestsSharedCollection: IFeedbackTest[] = [];

  editForm: ReponseFormGroup = this.reponseFormService.createReponseFormGroup();

  constructor(
    protected reponseService: ReponseService,
    protected reponseFormService: ReponseFormService,
    protected feedbackTestService: FeedbackTestService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareFeedbackTest = (o1: IFeedbackTest | null, o2: IFeedbackTest | null): boolean =>
    this.feedbackTestService.compareFeedbackTest(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reponse }) => {
      this.reponse = reponse;
      if (reponse) {
        this.updateForm(reponse);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reponse = this.reponseFormService.getReponse(this.editForm);
    if (reponse.id !== null) {
      this.subscribeToSaveResponse(this.reponseService.update(reponse));
    } else {
      this.subscribeToSaveResponse(this.reponseService.create(reponse));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReponse>>): void {
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

  protected updateForm(reponse: IReponse): void {
    this.reponse = reponse;
    this.reponseFormService.resetForm(this.editForm, reponse);

    this.feedbackTestsSharedCollection = this.feedbackTestService.addFeedbackTestToCollectionIfMissing<IFeedbackTest>(
      this.feedbackTestsSharedCollection,
      reponse.feedbackTest
    );
  }

  protected loadRelationshipsOptions(): void {
    this.feedbackTestService
      .query()
      .pipe(map((res: HttpResponse<IFeedbackTest[]>) => res.body ?? []))
      .pipe(
        map((feedbackTests: IFeedbackTest[]) =>
          this.feedbackTestService.addFeedbackTestToCollectionIfMissing<IFeedbackTest>(feedbackTests, this.reponse?.feedbackTest)
        )
      )
      .subscribe((feedbackTests: IFeedbackTest[]) => (this.feedbackTestsSharedCollection = feedbackTests));
  }
}
