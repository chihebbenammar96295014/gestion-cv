import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFeedbackTest, NewFeedbackTest } from '../feedback-test.model';

export type PartialUpdateFeedbackTest = Partial<IFeedbackTest> & Pick<IFeedbackTest, 'id'>;

export type EntityResponseType = HttpResponse<IFeedbackTest>;
export type EntityArrayResponseType = HttpResponse<IFeedbackTest[]>;

@Injectable({ providedIn: 'root' })
export class FeedbackTestService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/feedback-tests');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(feedbackTest: NewFeedbackTest): Observable<EntityResponseType> {
    return this.http.post<IFeedbackTest>(this.resourceUrl, feedbackTest, { observe: 'response' });
  }

  update(feedbackTest: IFeedbackTest): Observable<EntityResponseType> {
    return this.http.put<IFeedbackTest>(`${this.resourceUrl}/${this.getFeedbackTestIdentifier(feedbackTest)}`, feedbackTest, {
      observe: 'response',
    });
  }

  partialUpdate(feedbackTest: PartialUpdateFeedbackTest): Observable<EntityResponseType> {
    return this.http.patch<IFeedbackTest>(`${this.resourceUrl}/${this.getFeedbackTestIdentifier(feedbackTest)}`, feedbackTest, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFeedbackTest>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFeedbackTest[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFeedbackTestIdentifier(feedbackTest: Pick<IFeedbackTest, 'id'>): number {
    return feedbackTest.id;
  }

  compareFeedbackTest(o1: Pick<IFeedbackTest, 'id'> | null, o2: Pick<IFeedbackTest, 'id'> | null): boolean {
    return o1 && o2 ? this.getFeedbackTestIdentifier(o1) === this.getFeedbackTestIdentifier(o2) : o1 === o2;
  }

  addFeedbackTestToCollectionIfMissing<Type extends Pick<IFeedbackTest, 'id'>>(
    feedbackTestCollection: Type[],
    ...feedbackTestsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const feedbackTests: Type[] = feedbackTestsToCheck.filter(isPresent);
    if (feedbackTests.length > 0) {
      const feedbackTestCollectionIdentifiers = feedbackTestCollection.map(
        feedbackTestItem => this.getFeedbackTestIdentifier(feedbackTestItem)!
      );
      const feedbackTestsToAdd = feedbackTests.filter(feedbackTestItem => {
        const feedbackTestIdentifier = this.getFeedbackTestIdentifier(feedbackTestItem);
        if (feedbackTestCollectionIdentifiers.includes(feedbackTestIdentifier)) {
          return false;
        }
        feedbackTestCollectionIdentifiers.push(feedbackTestIdentifier);
        return true;
      });
      return [...feedbackTestsToAdd, ...feedbackTestCollection];
    }
    return feedbackTestCollection;
  }
}
