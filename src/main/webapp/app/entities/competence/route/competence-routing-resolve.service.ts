import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICompetence } from '../competence.model';
import { CompetenceService } from '../service/competence.service';

@Injectable({ providedIn: 'root' })
export class CompetenceRoutingResolveService implements Resolve<ICompetence | null> {
  constructor(protected service: CompetenceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICompetence | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((competence: HttpResponse<ICompetence>) => {
          if (competence.body) {
            return of(competence.body);
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
