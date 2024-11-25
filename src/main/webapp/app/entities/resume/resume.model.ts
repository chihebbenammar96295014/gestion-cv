export interface IResume {
  id: number;
  description?: string | null;
}

export type NewResume = Omit<IResume, 'id'> & { id: null };
