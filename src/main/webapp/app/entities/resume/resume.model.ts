import { Role } from '../enumerations/role.model';
import dayjs from 'dayjs/esm';

export interface IResume {
  id: number;
  description?: string | null;
  nom?: string | null;
  prenom?: string | null;
  email?: string | null;
  motDePasse?: string | null;
  adresse?: string | null;
  numTelephone?: string | null;
  typeUser?: Role | null;
  namecompetence?: string | null;
  niveaucompetence?: number | null;
  startDateFormation?: dayjs.Dayjs | null;
  endDateFormation?: dayjs.Dayjs | null;
  titleFormation?: string | null;
  descriptionFormation?: string | null;
  placeFormation?: string | null;
  startDateExp?: dayjs.Dayjs | null;
  endDateExp?: dayjs.Dayjs | null;
  titleExp?: string | null;
  descriptionExp?: string | null;
  fonctionExp?: string | null;
  placeExp?: string | null;
}

export type NewResume = Omit<IResume, 'id'> & { id: null };
