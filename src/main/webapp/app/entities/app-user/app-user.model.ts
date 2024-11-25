import { IResume } from 'app/entities/resume/resume.model';
import { Role } from 'app/entities/enumerations/role.model';

export interface IAppUser {
  id: number;
  nom?: string | null;
  prenom?: string | null;
  email?: string | null;
  motDePasse?: string | null;
  adresse?: string | null;
  numTelephone?: string | null;
  typeUser?: Role | null;
  resume?: Pick<IResume, 'id'> | null;
}

export type NewAppUser = Omit<IAppUser, 'id'> & { id: null };
