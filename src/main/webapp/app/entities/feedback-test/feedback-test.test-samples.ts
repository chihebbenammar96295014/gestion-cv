import { IFeedbackTest, NewFeedbackTest } from './feedback-test.model';

export const sampleWithRequiredData: IFeedbackTest = {
  id: 14688,
};

export const sampleWithPartialData: IFeedbackTest = {
  id: 62412,
  note: 78997,
  commentaires: 'interface Avon orange',
};

export const sampleWithFullData: IFeedbackTest = {
  id: 91942,
  note: 7220,
  commentaires: 'Tactics mobile',
};

export const sampleWithNewData: NewFeedbackTest = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
