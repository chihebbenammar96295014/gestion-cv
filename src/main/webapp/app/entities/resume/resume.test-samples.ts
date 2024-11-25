import { IResume, NewResume } from './resume.model';

export const sampleWithRequiredData: IResume = {
  id: 99221,
  description: 'Pizza',
};

export const sampleWithPartialData: IResume = {
  id: 43066,
  description: 'Incredible District connecting',
};

export const sampleWithFullData: IResume = {
  id: 93854,
  description: 'solid',
};

export const sampleWithNewData: NewResume = {
  description: 'Regional',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
