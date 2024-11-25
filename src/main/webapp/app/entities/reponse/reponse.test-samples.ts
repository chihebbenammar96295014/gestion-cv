import { IReponse, NewReponse } from './reponse.model';

export const sampleWithRequiredData: IReponse = {
  id: 6123,
  order: 84623,
  content: 'National Steel',
};

export const sampleWithPartialData: IReponse = {
  id: 86137,
  order: 81517,
  content: 'Vermont',
};

export const sampleWithFullData: IReponse = {
  id: 14295,
  order: 62761,
  content: 'Marketing',
};

export const sampleWithNewData: NewReponse = {
  order: 33783,
  content: 'channels streamline bandwidth',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
