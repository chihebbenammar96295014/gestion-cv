import { IQuestion, NewQuestion } from './question.model';

export const sampleWithRequiredData: IQuestion = {
  id: 47363,
  order: 92408,
  question: 'next-generation Concrete',
  reponseCorrecte: 'North Buckinghamshire Investment',
};

export const sampleWithPartialData: IQuestion = {
  id: 67350,
  order: 64791,
  question: 'Minnesota Motorway Japan',
  reponseCorrecte: 'Lats Namibia',
};

export const sampleWithFullData: IQuestion = {
  id: 93930,
  order: 58974,
  question: 'parse Franc',
  reponseCorrecte: 'Account portals',
};

export const sampleWithNewData: NewQuestion = {
  order: 61445,
  question: 'Oklahoma Account Beauty',
  reponseCorrecte: 'parse stable',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
