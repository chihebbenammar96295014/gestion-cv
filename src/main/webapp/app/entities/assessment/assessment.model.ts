import dayjs from 'dayjs/esm';
import { IQuestion } from 'app/entities/question/question.model';

export interface IAssessment {
  id: number;
  nom?: string | null;
  description?: string | null;
  niveau?: string | null;
  date?: dayjs.Dayjs | null;
  questions?: Pick<IQuestion, 'id'>[] | null;
}

export type NewAssessment = Omit<IAssessment, 'id'> & { id: null };
