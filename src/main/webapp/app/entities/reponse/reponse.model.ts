import { IFeedbackTest } from 'app/entities/feedback-test/feedback-test.model';

export interface IReponse {
  id: number;
  order?: number | null;
  content?: string | null;
  feedbackTest?: Pick<IFeedbackTest, 'id'> | null;
}

export type NewReponse = Omit<IReponse, 'id'> & { id: null };
