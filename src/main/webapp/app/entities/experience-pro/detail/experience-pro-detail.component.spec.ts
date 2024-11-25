import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ExperienceProDetailComponent } from './experience-pro-detail.component';

describe('ExperiencePro Management Detail Component', () => {
  let comp: ExperienceProDetailComponent;
  let fixture: ComponentFixture<ExperienceProDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ExperienceProDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ experiencePro: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ExperienceProDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ExperienceProDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load experiencePro on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.experiencePro).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
