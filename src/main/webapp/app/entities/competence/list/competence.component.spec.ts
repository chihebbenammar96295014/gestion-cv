import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CompetenceService } from '../service/competence.service';

import { CompetenceComponent } from './competence.component';

describe('Competence Management Component', () => {
  let comp: CompetenceComponent;
  let fixture: ComponentFixture<CompetenceComponent>;
  let service: CompetenceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'competence', component: CompetenceComponent }]), HttpClientTestingModule],
      declarations: [CompetenceComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(CompetenceComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CompetenceComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CompetenceService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.competences?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to competenceService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getCompetenceIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getCompetenceIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
