import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ResumeComponent } from './list/resume.component';
import { ResumeDetailComponent } from './detail/resume-detail.component';
import { ResumeUpdateComponent } from './update/resume-update.component';
import { ResumeDeleteDialogComponent } from './delete/resume-delete-dialog.component';
import { ResumeRoutingModule } from './route/resume-routing.module';
import { FormationModule } from '../formation/formation.module';
import { AppUserModule } from '../app-user/app-user.module';
import { CompetenceModule } from '../competence/competence.module';
import { ExperienceProModule } from '../experience-pro/experience-pro.module';

@NgModule({
  imports: [SharedModule, ResumeRoutingModule, FormationModule, AppUserModule, CompetenceModule, ExperienceProModule],
  declarations: [ResumeComponent, ResumeDetailComponent, ResumeUpdateComponent, ResumeDeleteDialogComponent],
})
export class ResumeModule {}
