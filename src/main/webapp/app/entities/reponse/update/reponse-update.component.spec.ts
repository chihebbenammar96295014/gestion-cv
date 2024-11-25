import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ReponseFormService } from './reponse-form.service';
import { ReponseService } from '../service/reponse.service';
import { IReponse } from '../reponse.model';
import { IFeedbackTest } from 'app/entities/feedback-test/feedback-test.model';
import { FeedbackTestService } from 'app/entities/feedback-test/service/feedback-test.service';

import { ReponseUpdateComponent } from './reponse-update.component';

describe('Reponse Management Update Component', () => {
  let comp: ReponseUpdateComponent;
  let fixture: ComponentFixture<ReponseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reponseFormService: ReponseFormService;
  let reponseService: ReponseService;
  let feedbackTestService: FeedbackTestService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ReponseUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ReponseUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReponseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reponseFormService = TestBed.inject(ReponseFormService);
    reponseService = TestBed.inject(ReponseService);
    feedbackTestService = TestBed.inject(FeedbackTestService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call FeedbackTest query and add missing value', () => {
      const reponse: IReponse = { id: 456 };
      const feedbackTest: IFeedbackTest = { id: 51926 };
      reponse.feedbackTest = feedbackTest;

      const feedbackTestCollection: IFeedbackTest[] = [{ id: 99355 }];
      jest.spyOn(feedbackTestService, 'query').mockReturnValue(of(new HttpResponse({ body: feedbackTestCollection })));
      const additionalFeedbackTests = [feedbackTest];
      const expectedCollection: IFeedbackTest[] = [...additionalFeedbackTests, ...feedbackTestCollection];
      jest.spyOn(feedbackTestService, 'addFeedbackTestToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reponse });
      comp.ngOnInit();

      expect(feedbackTestService.query).toHaveBeenCalled();
      expect(feedbackTestService.addFeedbackTestToCollectionIfMissing).toHaveBeenCalledWith(
        feedbackTestCollection,
        ...additionalFeedbackTests.map(expect.objectContaining)
      );
      expect(comp.feedbackTestsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const reponse: IReponse = { id: 456 };
      const feedbackTest: IFeedbackTest = { id: 48048 };
      reponse.feedbackTest = feedbackTest;

      activatedRoute.data = of({ reponse });
      comp.ngOnInit();

      expect(comp.feedbackTestsSharedCollection).toContain(feedbackTest);
      expect(comp.reponse).toEqual(reponse);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReponse>>();
      const reponse = { id: 123 };
      jest.spyOn(reponseFormService, 'getReponse').mockReturnValue(reponse);
      jest.spyOn(reponseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reponse });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reponse }));
      saveSubject.complete();

      // THEN
      expect(reponseFormService.getReponse).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(reponseService.update).toHaveBeenCalledWith(expect.objectContaining(reponse));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReponse>>();
      const reponse = { id: 123 };
      jest.spyOn(reponseFormService, 'getReponse').mockReturnValue({ id: null });
      jest.spyOn(reponseService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reponse: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reponse }));
      saveSubject.complete();

      // THEN
      expect(reponseFormService.getReponse).toHaveBeenCalled();
      expect(reponseService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReponse>>();
      const reponse = { id: 123 };
      jest.spyOn(reponseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reponse });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reponseService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFeedbackTest', () => {
      it('Should forward to feedbackTestService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(feedbackTestService, 'compareFeedbackTest');
        comp.compareFeedbackTest(entity, entity2);
        expect(feedbackTestService.compareFeedbackTest).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
