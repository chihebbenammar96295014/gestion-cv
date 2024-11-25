import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFeedbackTest } from '../feedback-test.model';
import { FeedbackTestService } from '../service/feedback-test.service';

@Injectable({ providedIn: 'root' })
export class FeedbackTestRoutingResolveService implements Resolve<IFeedbackTest | null> {
  constructor(protected service: FeedbackTestService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFeedbackTest | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((feedbackTest: HttpResponse<IFeedbackTest>) => {
          if (feedbackTest.body) {
            return of(feedbackTest.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
