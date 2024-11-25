import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FeedbackTestDetailComponent } from './feedback-test-detail.component';

describe('FeedbackTest Management Detail Component', () => {
  let comp: FeedbackTestDetailComponent;
  let fixture: ComponentFixture<FeedbackTestDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FeedbackTestDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ feedbackTest: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FeedbackTestDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FeedbackTestDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load feedbackTest on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.feedbackTest).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
