import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FeedbackTestComponent } from './list/feedback-test.component';
import { FeedbackTestDetailComponent } from './detail/feedback-test-detail.component';
import { FeedbackTestUpdateComponent } from './update/feedback-test-update.component';
import { FeedbackTestDeleteDialogComponent } from './delete/feedback-test-delete-dialog.component';
import { FeedbackTestRoutingModule } from './route/feedback-test-routing.module';

@NgModule({
  imports: [SharedModule, FeedbackTestRoutingModule],
  declarations: [FeedbackTestComponent, FeedbackTestDetailComponent, FeedbackTestUpdateComponent, FeedbackTestDeleteDialogComponent],
})
export class FeedbackTestModule {}
