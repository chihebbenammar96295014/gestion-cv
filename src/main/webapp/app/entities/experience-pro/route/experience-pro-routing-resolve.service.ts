import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IExperiencePro } from '../experience-pro.model';
import { ExperienceProService } from '../service/experience-pro.service';

@Injectable({ providedIn: 'root' })
export class ExperienceProRoutingResolveService implements Resolve<IExperiencePro | null> {
  constructor(protected service: ExperienceProService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IExperiencePro | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((experiencePro: HttpResponse<IExperiencePro>) => {
          if (experiencePro.body) {
            return of(experiencePro.body);
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
