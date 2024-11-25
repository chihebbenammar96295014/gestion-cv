import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExperiencePro, NewExperiencePro } from '../experience-pro.model';

export type PartialUpdateExperiencePro = Partial<IExperiencePro> & Pick<IExperiencePro, 'id'>;

type RestOf<T extends IExperiencePro | NewExperiencePro> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

export type RestExperiencePro = RestOf<IExperiencePro>;

export type NewRestExperiencePro = RestOf<NewExperiencePro>;

export type PartialUpdateRestExperiencePro = RestOf<PartialUpdateExperiencePro>;

export type EntityResponseType = HttpResponse<IExperiencePro>;
export type EntityArrayResponseType = HttpResponse<IExperiencePro[]>;

@Injectable({ providedIn: 'root' })
export class ExperienceProService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/experience-pros');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(experiencePro: NewExperiencePro): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(experiencePro);
    return this.http
      .post<RestExperiencePro>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(experiencePro: IExperiencePro): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(experiencePro);
    return this.http
      .put<RestExperiencePro>(`${this.resourceUrl}/${this.getExperienceProIdentifier(experiencePro)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(experiencePro: PartialUpdateExperiencePro): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(experiencePro);
    return this.http
      .patch<RestExperiencePro>(`${this.resourceUrl}/${this.getExperienceProIdentifier(experiencePro)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestExperiencePro>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestExperiencePro[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getExperienceProIdentifier(experiencePro: Pick<IExperiencePro, 'id'>): number {
    return experiencePro.id;
  }

  compareExperiencePro(o1: Pick<IExperiencePro, 'id'> | null, o2: Pick<IExperiencePro, 'id'> | null): boolean {
    return o1 && o2 ? this.getExperienceProIdentifier(o1) === this.getExperienceProIdentifier(o2) : o1 === o2;
  }

  addExperienceProToCollectionIfMissing<Type extends Pick<IExperiencePro, 'id'>>(
    experienceProCollection: Type[],
    ...experienceProsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const experiencePros: Type[] = experienceProsToCheck.filter(isPresent);
    if (experiencePros.length > 0) {
      const experienceProCollectionIdentifiers = experienceProCollection.map(
        experienceProItem => this.getExperienceProIdentifier(experienceProItem)!
      );
      const experienceProsToAdd = experiencePros.filter(experienceProItem => {
        const experienceProIdentifier = this.getExperienceProIdentifier(experienceProItem);
        if (experienceProCollectionIdentifiers.includes(experienceProIdentifier)) {
          return false;
        }
        experienceProCollectionIdentifiers.push(experienceProIdentifier);
        return true;
      });
      return [...experienceProsToAdd, ...experienceProCollection];
    }
    return experienceProCollection;
  }

  protected convertDateFromClient<T extends IExperiencePro | NewExperiencePro | PartialUpdateExperiencePro>(experiencePro: T): RestOf<T> {
    return {
      ...experiencePro,
      startDate: experiencePro.startDate?.format(DATE_FORMAT) ?? null,
      endDate: experiencePro.endDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restExperiencePro: RestExperiencePro): IExperiencePro {
    return {
      ...restExperiencePro,
      startDate: restExperiencePro.startDate ? dayjs(restExperiencePro.startDate) : undefined,
      endDate: restExperiencePro.endDate ? dayjs(restExperiencePro.endDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestExperiencePro>): HttpResponse<IExperiencePro> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestExperiencePro[]>): HttpResponse<IExperiencePro[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
