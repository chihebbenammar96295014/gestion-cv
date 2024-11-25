import dayjs from 'dayjs/esm';

import { IExperiencePro, NewExperiencePro } from './experience-pro.model';

export const sampleWithRequiredData: IExperiencePro = {
  id: 13022,
  title: 'Djibouti firewall backing',
};

export const sampleWithPartialData: IExperiencePro = {
  id: 71376,
  title: 'B2C',
  fonction: 'Plastic Rican',
  place: 'client-server',
};

export const sampleWithFullData: IExperiencePro = {
  id: 66954,
  startDate: dayjs('2024-11-25'),
  endDate: dayjs('2024-11-25'),
  title: 'transmit',
  description: 'bypassing',
  fonction: 'website Avon bluetooth',
  place: 'Antillian',
};

export const sampleWithNewData: NewExperiencePro = {
  title: 'Account Small',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
