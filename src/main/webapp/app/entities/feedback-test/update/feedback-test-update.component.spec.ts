import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FeedbackTestFormService } from './feedback-test-form.service';
import { FeedbackTestService } from '../service/feedback-test.service';
import { IFeedbackTest } from '../feedback-test.model';
import { IAppUser } from 'app/entities/app-user/app-user.model';
import { AppUserService } from 'app/entities/app-user/service/app-user.service';
import { IAssessment } from 'app/entities/assessment/assessment.model';
import { AssessmentService } from 'app/entities/assessment/service/assessment.service';

import { FeedbackTestUpdateComponent } from './feedback-test-update.component';

describe('FeedbackTest Management Update Component', () => {
  let comp: FeedbackTestUpdateComponent;
  let fixture: ComponentFixture<FeedbackTestUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let feedbackTestFormService: FeedbackTestFormService;
  let feedbackTestService: FeedbackTestService;
  let appUserService: AppUserService;
  let assessmentService: AssessmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FeedbackTestUpdateComponent],
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
      .overrideTemplate(FeedbackTestUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FeedbackTestUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    feedbackTestFormService = TestBed.inject(FeedbackTestFormService);
    feedbackTestService = TestBed.inject(FeedbackTestService);
    appUserService = TestBed.inject(AppUserService);
    assessmentService = TestBed.inject(AssessmentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call AppUser query and add missing value', () => {
      const feedbackTest: IFeedbackTest = { id: 456 };
      const appUser: IAppUser = { id: 4026 };
      feedbackTest.appUser = appUser;

      const appUserCollection: IAppUser[] = [{ id: 87497 }];
      jest.spyOn(appUserService, 'query').mockReturnValue(of(new HttpResponse({ body: appUserCollection })));
      const additionalAppUsers = [appUser];
      const expectedCollection: IAppUser[] = [...additionalAppUsers, ...appUserCollection];
      jest.spyOn(appUserService, 'addAppUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ feedbackTest });
      comp.ngOnInit();

      expect(appUserService.query).toHaveBeenCalled();
      expect(appUserService.addAppUserToCollectionIfMissing).toHaveBeenCalledWith(
        appUserCollection,
        ...additionalAppUsers.map(expect.objectContaining)
      );
      expect(comp.appUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Assessment query and add missing value', () => {
      const feedbackTest: IFeedbackTest = { id: 456 };
      const assessment: IAssessment = { id: 18450 };
      feedbackTest.assessment = assessment;

      const assessmentCollection: IAssessment[] = [{ id: 66908 }];
      jest.spyOn(assessmentService, 'query').mockReturnValue(of(new HttpResponse({ body: assessmentCollection })));
      const additionalAssessments = [assessment];
      const expectedCollection: IAssessment[] = [...additionalAssessments, ...assessmentCollection];
      jest.spyOn(assessmentService, 'addAssessmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ feedbackTest });
      comp.ngOnInit();

      expect(assessmentService.query).toHaveBeenCalled();
      expect(assessmentService.addAssessmentToCollectionIfMissing).toHaveBeenCalledWith(
        assessmentCollection,
        ...additionalAssessments.map(expect.objectContaining)
      );
      expect(comp.assessmentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const feedbackTest: IFeedbackTest = { id: 456 };
      const appUser: IAppUser = { id: 81257 };
      feedbackTest.appUser = appUser;
      const assessment: IAssessment = { id: 3395 };
      feedbackTest.assessment = assessment;

      activatedRoute.data = of({ feedbackTest });
      comp.ngOnInit();

      expect(comp.appUsersSharedCollection).toContain(appUser);
      expect(comp.assessmentsSharedCollection).toContain(assessment);
      expect(comp.feedbackTest).toEqual(feedbackTest);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedbackTest>>();
      const feedbackTest = { id: 123 };
      jest.spyOn(feedbackTestFormService, 'getFeedbackTest').mockReturnValue(feedbackTest);
      jest.spyOn(feedbackTestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedbackTest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: feedbackTest }));
      saveSubject.complete();

      // THEN
      expect(feedbackTestFormService.getFeedbackTest).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(feedbackTestService.update).toHaveBeenCalledWith(expect.objectContaining(feedbackTest));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedbackTest>>();
      const feedbackTest = { id: 123 };
      jest.spyOn(feedbackTestFormService, 'getFeedbackTest').mockReturnValue({ id: null });
      jest.spyOn(feedbackTestService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedbackTest: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: feedbackTest }));
      saveSubject.complete();

      // THEN
      expect(feedbackTestFormService.getFeedbackTest).toHaveBeenCalled();
      expect(feedbackTestService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedbackTest>>();
      const feedbackTest = { id: 123 };
      jest.spyOn(feedbackTestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedbackTest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(feedbackTestService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAppUser', () => {
      it('Should forward to appUserService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(appUserService, 'compareAppUser');
        comp.compareAppUser(entity, entity2);
        expect(appUserService.compareAppUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareAssessment', () => {
      it('Should forward to assessmentService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(assessmentService, 'compareAssessment');
        comp.compareAssessment(entity, entity2);
        expect(assessmentService.compareAssessment).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
