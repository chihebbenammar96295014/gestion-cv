import { ICompetence, NewCompetence } from './competence.model';

export const sampleWithRequiredData: ICompetence = {
  id: 53780,
  name: 'SCSI Corporate',
  niveau: 19613,
};

export const sampleWithPartialData: ICompetence = {
  id: 50966,
  name: 'impactful Gourde',
  niveau: 13087,
};

export const sampleWithFullData: ICompetence = {
  id: 28257,
  name: 'morph',
  niveau: 76597,
};

export const sampleWithNewData: NewCompetence = {
  name: 'Plastic',
  niveau: 98329,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
