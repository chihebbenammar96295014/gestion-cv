import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ExperienceProComponent } from './list/experience-pro.component';
import { ExperienceProDetailComponent } from './detail/experience-pro-detail.component';
import { ExperienceProUpdateComponent } from './update/experience-pro-update.component';
import { ExperienceProDeleteDialogComponent } from './delete/experience-pro-delete-dialog.component';
import { ExperienceProRoutingModule } from './route/experience-pro-routing.module';

@NgModule({
  imports: [SharedModule, ExperienceProRoutingModule],
  declarations: [ExperienceProComponent, ExperienceProDetailComponent, ExperienceProUpdateComponent, ExperienceProDeleteDialogComponent],
  exports: [ExperienceProUpdateComponent],
})
export class ExperienceProModule {}
