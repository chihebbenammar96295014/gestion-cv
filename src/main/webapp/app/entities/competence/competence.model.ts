import { IResume } from 'app/entities/resume/resume.model';

export interface ICompetence {
  id: number;
  name?: string | null;
  niveau?: number | null;
  resume?: Pick<IResume, 'id'> | null;
}

export type NewCompetence = Omit<ICompetence, 'id'> & { id: null };
