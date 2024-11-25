import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFormation, NewFormation } from '../formation.model';

export type PartialUpdateFormation = Partial<IFormation> & Pick<IFormation, 'id'>;

type RestOf<T extends IFormation | NewFormation> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

export type RestFormation = RestOf<IFormation>;

export type NewRestFormation = RestOf<NewFormation>;

export type PartialUpdateRestFormation = RestOf<PartialUpdateFormation>;

export type EntityResponseType = HttpResponse<IFormation>;
export type EntityArrayResponseType = HttpResponse<IFormation[]>;

@Injectable({ providedIn: 'root' })
export class FormationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/formations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(formation: NewFormation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(formation);
    return this.http
      .post<RestFormation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(formation: IFormation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(formation);
    return this.http
      .put<RestFormation>(`${this.resourceUrl}/${this.getFormationIdentifier(formation)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(formation: PartialUpdateFormation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(formation);
    return this.http
      .patch<RestFormation>(`${this.resourceUrl}/${this.getFormationIdentifier(formation)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFormation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFormation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFormationIdentifier(formation: Pick<IFormation, 'id'>): number {
    return formation.id;
  }

  compareFormation(o1: Pick<IFormation, 'id'> | null, o2: Pick<IFormation, 'id'> | null): boolean {
    return o1 && o2 ? this.getFormationIdentifier(o1) === this.getFormationIdentifier(o2) : o1 === o2;
  }

  addFormationToCollectionIfMissing<Type extends Pick<IFormation, 'id'>>(
    formationCollection: Type[],
    ...formationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const formations: Type[] = formationsToCheck.filter(isPresent);
    if (formations.length > 0) {
      const formationCollectionIdentifiers = formationCollection.map(formationItem => this.getFormationIdentifier(formationItem)!);
      const formationsToAdd = formations.filter(formationItem => {
        const formationIdentifier = this.getFormationIdentifier(formationItem);
        if (formationCollectionIdentifiers.includes(formationIdentifier)) {
          return false;
        }
        formationCollectionIdentifiers.push(formationIdentifier);
        return true;
      });
      return [...formationsToAdd, ...formationCollection];
    }
    return formationCollection;
  }

  protected convertDateFromClient<T extends IFormation | NewFormation | PartialUpdateFormation>(formation: T): RestOf<T> {
    return {
      ...formation,
      startDate: formation.startDate?.format(DATE_FORMAT) ?? null,
      endDate: formation.endDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restFormation: RestFormation): IFormation {
    return {
      ...restFormation,
      startDate: restFormation.startDate ? dayjs(restFormation.startDate) : undefined,
      endDate: restFormation.endDate ? dayjs(restFormation.endDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFormation>): HttpResponse<IFormation> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFormation[]>): HttpResponse<IFormation[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
