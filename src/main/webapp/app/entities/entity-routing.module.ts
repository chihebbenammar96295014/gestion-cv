import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'app-user',
        data: { pageTitle: 'candidatApp.appUser.home.title' },
        loadChildren: () => import('./app-user/app-user.module').then(m => m.AppUserModule),
      },
      {
        path: 'resume',
        data: { pageTitle: 'candidatApp.resume.home.title' },
        loadChildren: () => import('./resume/resume.module').then(m => m.ResumeModule),
      },
      {
        path: 'competence',
        data: { pageTitle: 'candidatApp.competence.home.title' },
        loadChildren: () => import('./competence/competence.module').then(m => m.CompetenceModule),
      },
      {
        path: 'formation',
        data: { pageTitle: 'candidatApp.formation.home.title' },
        loadChildren: () => import('./formation/formation.module').then(m => m.FormationModule),
      },
      {
        path: 'experience-pro',
        data: { pageTitle: 'candidatApp.experiencePro.home.title' },
        loadChildren: () => import('./experience-pro/experience-pro.module').then(m => m.ExperienceProModule),
      },
      {
        path: 'assessment',
        data: { pageTitle: 'candidatApp.assessment.home.title' },
        loadChildren: () => import('./assessment/assessment.module').then(m => m.AssessmentModule),
      },
      {
        path: 'question',
        data: { pageTitle: 'candidatApp.question.home.title' },
        loadChildren: () => import('./question/question.module').then(m => m.QuestionModule),
      },
      {
        path: 'reponse',
        data: { pageTitle: 'candidatApp.reponse.home.title' },
        loadChildren: () => import('./reponse/reponse.module').then(m => m.ReponseModule),
      },
      {
        path: 'feedback-test',
        data: { pageTitle: 'candidatApp.feedbackTest.home.title' },
        loadChildren: () => import('./feedback-test/feedback-test.module').then(m => m.FeedbackTestModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
