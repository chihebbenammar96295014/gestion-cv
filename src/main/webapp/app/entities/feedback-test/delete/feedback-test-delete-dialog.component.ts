import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFeedbackTest } from '../feedback-test.model';
import { FeedbackTestService } from '../service/feedback-test.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './feedback-test-delete-dialog.component.html',
})
export class FeedbackTestDeleteDialogComponent {
  feedbackTest?: IFeedbackTest;

  constructor(protected feedbackTestService: FeedbackTestService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.feedbackTestService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
