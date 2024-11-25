import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FeedbackTestComponent } from '../list/feedback-test.component';
import { FeedbackTestDetailComponent } from '../detail/feedback-test-detail.component';
import { FeedbackTestUpdateComponent } from '../update/feedback-test-update.component';
import { FeedbackTestRoutingResolveService } from './feedback-test-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const feedbackTestRoute: Routes = [
  {
    path: '',
    component: FeedbackTestComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FeedbackTestDetailComponent,
    resolve: {
      feedbackTest: FeedbackTestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FeedbackTestUpdateComponent,
    resolve: {
      feedbackTest: FeedbackTestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FeedbackTestUpdateComponent,
    resolve: {
      feedbackTest: FeedbackTestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(feedbackTestRoute)],
  exports: [RouterModule],
})
export class FeedbackTestRoutingModule {}
