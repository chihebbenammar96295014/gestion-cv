import dayjs from 'dayjs/esm';

import { IAssessment, NewAssessment } from './assessment.model';

export const sampleWithRequiredData: IAssessment = {
  id: 82522,
  nom: 'visualize',
};

export const sampleWithPartialData: IAssessment = {
  id: 94174,
  nom: 'Ukraine viral York',
  niveau: 'Minnesota Outlying HDD',
};

export const sampleWithFullData: IAssessment = {
  id: 55380,
  nom: 'virtual orange',
  description: 'Towels hybrid Keyboard',
  niveau: 'generate deposit',
  date: dayjs('2024-11-25T09:01'),
};

export const sampleWithNewData: NewAssessment = {
  nom: 'Buckinghamshire visionary',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
