import { IResume, NewResume } from './resume.model';
import { Role } from '../enumerations/role.model';
import dayjs from 'dayjs/esm';

export const sampleWithRequiredData: IResume = {
  id: 99221,
  description: 'Pizza',
};

export const sampleWithPartialData: IResume = {
  id: 43066,
  description: 'Incredible District connecting',
  nom: 'Ali',
  prenom: 'BenAli',
  email: 'chihebbenammar49@gmail.com',
  motDePasse: 'Ali',
  adresse: 'msaken',
  numTelephone: '96295014',
  typeUser: Role.CANDIDAT,
  namecompetence: 'Angular',
  niveaucompetence: 9,
};

export const sampleWithFullData: IResume = {
  id: 43066,
  description: 'Incredible District connecting',
  nom: 'Ali',
  prenom: 'BenAli',
  email: 'chihebbenammar49@gmail.com',
  motDePasse: 'Ali',
  adresse: 'msaken',
  numTelephone: '96295014',
  typeUser: Role.CANDIDAT,
  namecompetence: 'Angular',
  niveaucompetence: 9,
  startDateFormation: dayjs('2024-11-25'),
  endDateFormation: dayjs('2024-12-25'),
  titleFormation: 'Spring boot',
  descriptionFormation: 'amazing',
  placeFormation: 'Sahloul',
  startDateExp: dayjs('2024-07-01'),
  endDateExp: dayjs('2024-09-01'),
  titleExp: 'Stage',
  descriptionExp: 'satge de development',
  fonctionExp: 'website frontend',
  placeExp: 'Satoripop',
};

export const sampleWithNewData: NewResume = {
  id: null,
  description: '',
  nom: '',
  prenom: '',
  email: '',
  motDePasse: '',
  adresse: '',
  numTelephone: '',
  typeUser: Role.CANDIDAT,
  namecompetence: '',
  niveaucompetence: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
