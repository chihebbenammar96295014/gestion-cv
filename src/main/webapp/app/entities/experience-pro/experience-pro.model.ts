import dayjs from 'dayjs/esm';
import { IResume } from 'app/entities/resume/resume.model';

export interface IExperiencePro {
  id: number;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  title?: string | null;
  description?: string | null;
  fonction?: string | null;
  place?: string | null;
  resume?: Pick<IResume, 'id'> | null;
}

export type NewExperiencePro = Omit<IExperiencePro, 'id'> & { id: null };
