import { Route, Routes } from '@angular/router';

import { HomeComponent } from './home.component';
import { AppUserComponent } from '../entities/app-user/list/app-user.component';
import { TendenceCvComponent } from './tendence-cv/tendence-cv.component';
import { FormulaireCVComponent } from '../formulaire-cv/formulaire-cv.component';
import { EntretienComponent } from './entretien/entretien.component';
import { HomecandidatComponent } from './homecandidat/homecandidat.component';

export const HOME_ROUTE: Routes = [
  {
    path: '',
    component: HomeComponent,
    data: {
      pageTitle: 'home.title',
    },
  },
  {
    path: 'abc',
    component: TendenceCvComponent,
  },
  {
    path: 'cde',
    component: FormulaireCVComponent,
  },
  {
    path: 'entretien',
    component: EntretienComponent,
  },
  {
    path: 'homecandidat',
    component: HomecandidatComponent,
  },
  {
    path: 'formulaire',
    component: FormulaireCVComponent,
  },
];
