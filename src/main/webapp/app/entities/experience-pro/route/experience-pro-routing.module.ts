import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ExperienceProComponent } from '../list/experience-pro.component';
import { ExperienceProDetailComponent } from '../detail/experience-pro-detail.component';
import { ExperienceProUpdateComponent } from '../update/experience-pro-update.component';
import { ExperienceProRoutingResolveService } from './experience-pro-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const experienceProRoute: Routes = [
  {
    path: '',
    component: ExperienceProComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ExperienceProDetailComponent,
    resolve: {
      experiencePro: ExperienceProRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ExperienceProUpdateComponent,
    resolve: {
      experiencePro: ExperienceProRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ExperienceProUpdateComponent,
    resolve: {
      experiencePro: ExperienceProRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(experienceProRoute)],
  exports: [RouterModule],
})
export class ExperienceProRoutingModule {}
