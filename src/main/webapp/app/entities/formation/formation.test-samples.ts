import dayjs from 'dayjs/esm';

import { IFormation, NewFormation } from './formation.model';

export const sampleWithRequiredData: IFormation = {
  id: 7564,
  title: 'Norwegian overriding Uruguayo',
};

export const sampleWithPartialData: IFormation = {
  id: 5308,
  endDate: dayjs('2024-11-25'),
  title: 'capacitor Wisconsin',
  place: 'Sleek',
};

export const sampleWithFullData: IFormation = {
  id: 79654,
  startDate: dayjs('2024-11-25'),
  endDate: dayjs('2024-11-25'),
  title: 'Persevering',
  description: 'ADP RSS',
  place: 'Wooden JSON',
};

export const sampleWithNewData: NewFormation = {
  title: 'Branch Avon',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
