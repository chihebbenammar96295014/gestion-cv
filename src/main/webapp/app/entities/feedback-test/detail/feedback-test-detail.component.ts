import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFeedbackTest } from '../feedback-test.model';

@Component({
  selector: 'jhi-feedback-test-detail',
  templateUrl: './feedback-test-detail.component.html',
})
export class FeedbackTestDetailComponent implements OnInit {
  feedbackTest: IFeedbackTest | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ feedbackTest }) => {
      this.feedbackTest = feedbackTest;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
