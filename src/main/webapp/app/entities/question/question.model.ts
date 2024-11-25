import { IAssessment } from 'app/entities/assessment/assessment.model';

export interface IQuestion {
  id: number;
  order?: number | null;
  question?: string | null;
  reponseCorrecte?: string | null;
  assessments?: Pick<IAssessment, 'id'>[] | null;
}

export type NewQuestion = Omit<IQuestion, 'id'> & { id: null };
