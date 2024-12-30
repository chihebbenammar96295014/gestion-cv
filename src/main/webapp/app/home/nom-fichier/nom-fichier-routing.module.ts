import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TendenceCvComponent } from '../tendence-cv/tendence-cv.component';
import { HomeComponent } from '../home.component';
import { EntretienComponent } from '../entretien/entretien.component';
import { FormulaireCVComponent } from '../../formulaire-cv/formulaire-cv.component';

const routes: Routes = [
  {
    path: 'HOME',
    component: HomeComponent,
  },
  {
    path: 'abc',
    component: TendenceCvComponent,
  },
  {
    path: 'entretien',
    component: EntretienComponent,
  },
  {
    path: 'formulaire',
    component: FormulaireCVComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class NomFichierRoutingModule {}
