import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ExperienceProService } from '../service/experience-pro.service';

import { ExperienceProComponent } from './experience-pro.component';

describe('ExperiencePro Management Component', () => {
  let comp: ExperienceProComponent;
  let fixture: ComponentFixture<ExperienceProComponent>;
  let service: ExperienceProService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'experience-pro', component: ExperienceProComponent }]), HttpClientTestingModule],
      declarations: [ExperienceProComponent],
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
      .overrideTemplate(ExperienceProComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ExperienceProComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ExperienceProService);

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
    expect(comp.experiencePros?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to experienceProService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getExperienceProIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getExperienceProIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
