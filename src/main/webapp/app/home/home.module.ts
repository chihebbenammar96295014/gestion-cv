import { NgModule } from '@angular/core';
import { RouterLinkWithHref, RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { TendenceCvComponent } from './tendence-cv/tendence-cv.component';
import { configurationRoute } from '../admin/configuration/configuration.route';
import { HomecandidatComponent } from './homecandidat/homecandidat.component';

@NgModule({
  declarations: [HomeComponent, HomecandidatComponent],
  imports: [SharedModule, RouterModule.forChild(HOME_ROUTE)],
})
export class HomeModule {}
