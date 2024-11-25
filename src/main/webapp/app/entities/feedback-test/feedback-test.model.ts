import { IAppUser } from 'app/entities/app-user/app-user.model';
import { IAssessment } from 'app/entities/assessment/assessment.model';

export interface IFeedbackTest {
  id: number;
  note?: number | null;
  commentaires?: string | null;
  appUser?: Pick<IAppUser, 'id'> | null;
  assessment?: Pick<IAssessment, 'id'> | null;
}

export type NewFeedbackTest = Omit<IFeedbackTest, 'id'> & { id: null };
