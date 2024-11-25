import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { FeedbackTestFormService, FeedbackTestFormGroup } from './feedback-test-form.service';
import { IFeedbackTest } from '../feedback-test.model';
import { FeedbackTestService } from '../service/feedback-test.service';
import { IAppUser } from 'app/entities/app-user/app-user.model';
import { AppUserService } from 'app/entities/app-user/service/app-user.service';
import { IAssessment } from 'app/entities/assessment/assessment.model';
import { AssessmentService } from 'app/entities/assessment/service/assessment.service';

@Component({
  selector: 'jhi-feedback-test-update',
  templateUrl: './feedback-test-update.component.html',
})
export class FeedbackTestUpdateComponent implements OnInit {
  isSaving = false;
  feedbackTest: IFeedbackTest | null = null;

  appUsersSharedCollection: IAppUser[] = [];
  assessmentsSharedCollection: IAssessment[] = [];

  editForm: FeedbackTestFormGroup = this.feedbackTestFormService.createFeedbackTestFormGroup();

  constructor(
    protected feedbackTestService: FeedbackTestService,
    protected feedbackTestFormService: FeedbackTestFormService,
    protected appUserService: AppUserService,
    protected assessmentService: AssessmentService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareAppUser = (o1: IAppUser | null, o2: IAppUser | null): boolean => this.appUserService.compareAppUser(o1, o2);

  compareAssessment = (o1: IAssessment | null, o2: IAssessment | null): boolean => this.assessmentService.compareAssessment(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ feedbackTest }) => {
      this.feedbackTest = feedbackTest;
      if (feedbackTest) {
        this.updateForm(feedbackTest);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const feedbackTest = this.feedbackTestFormService.getFeedbackTest(this.editForm);
    if (feedbackTest.id !== null) {
      this.subscribeToSaveResponse(this.feedbackTestService.update(feedbackTest));
    } else {
      this.subscribeToSaveResponse(this.feedbackTestService.create(feedbackTest));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFeedbackTest>>): void {
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

  protected updateForm(feedbackTest: IFeedbackTest): void {
    this.feedbackTest = feedbackTest;
    this.feedbackTestFormService.resetForm(this.editForm, feedbackTest);

    this.appUsersSharedCollection = this.appUserService.addAppUserToCollectionIfMissing<IAppUser>(
      this.appUsersSharedCollection,
      feedbackTest.appUser
    );
    this.assessmentsSharedCollection = this.assessmentService.addAssessmentToCollectionIfMissing<IAssessment>(
      this.assessmentsSharedCollection,
      feedbackTest.assessment
    );
  }

  protected loadRelationshipsOptions(): void {
    this.appUserService
      .query()
      .pipe(map((res: HttpResponse<IAppUser[]>) => res.body ?? []))
      .pipe(
        map((appUsers: IAppUser[]) => this.appUserService.addAppUserToCollectionIfMissing<IAppUser>(appUsers, this.feedbackTest?.appUser))
      )
      .subscribe((appUsers: IAppUser[]) => (this.appUsersSharedCollection = appUsers));

    this.assessmentService
      .query()
      .pipe(map((res: HttpResponse<IAssessment[]>) => res.body ?? []))
      .pipe(
        map((assessments: IAssessment[]) =>
          this.assessmentService.addAssessmentToCollectionIfMissing<IAssessment>(assessments, this.feedbackTest?.assessment)
        )
      )
      .subscribe((assessments: IAssessment[]) => (this.assessmentsSharedCollection = assessments));
  }
}
