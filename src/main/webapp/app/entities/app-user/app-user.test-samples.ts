import { Role } from 'app/entities/enumerations/role.model';

import { IAppUser, NewAppUser } from './app-user.model';

export const sampleWithRequiredData: IAppUser = {
  id: 1883,
  nom: 'compressing Latvian Account',
  prenom: 'Outdoors Health Gloves',
  email: 'Justina_Mueller45@gmail.com',
  motDePasse: 'Books',
  adresse: 'initiatives Mountains Handmade',
  typeUser: Role['ADMIN'],
};

export const sampleWithPartialData: IAppUser = {
  id: 61882,
  nom: 'scalable',
  prenom: 'Georgia overriding',
  email: 'Marilou71@yahoo.com',
  motDePasse: 'Streamlined',
  adresse: 'pixel',
  typeUser: Role['CANDIDAT'],
};

export const sampleWithFullData: IAppUser = {
  id: 45124,
  nom: 'hack foreground Electronics',
  prenom: 'invoice',
  email: 'Kenyatta35@hotmail.com',
  motDePasse: 'Planner',
  adresse: 'azure',
  numTelephone: 'Awesome',
  typeUser: Role['CANDIDAT'],
};

export const sampleWithNewData: NewAppUser = {
  nom: 'user-centric Refined Islands,',
  prenom: 'Ville Account',
  email: 'Graciela.Moore56@hotmail.com',
  motDePasse: 'haptic',
  adresse: 'Licensed copy Cotton',
  typeUser: Role['ADMIN'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
