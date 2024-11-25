import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AssessmentFormService } from './assessment-form.service';
import { AssessmentService } from '../service/assessment.service';
import { IAssessment } from '../assessment.model';
import { IQuestion } from 'app/entities/question/question.model';
import { QuestionService } from 'app/entities/question/service/question.service';

import { AssessmentUpdateComponent } from './assessment-update.component';

describe('Assessment Management Update Component', () => {
  let comp: AssessmentUpdateComponent;
  let fixture: ComponentFixture<AssessmentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let assessmentFormService: AssessmentFormService;
  let assessmentService: AssessmentService;
  let questionService: QuestionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AssessmentUpdateComponent],
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
      .overrideTemplate(AssessmentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AssessmentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    assessmentFormService = TestBed.inject(AssessmentFormService);
    assessmentService = TestBed.inject(AssessmentService);
    questionService = TestBed.inject(QuestionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Question query and add missing value', () => {
      const assessment: IAssessment = { id: 456 };
      const questions: IQuestion[] = [{ id: 38169 }];
      assessment.questions = questions;

      const questionCollection: IQuestion[] = [{ id: 35499 }];
      jest.spyOn(questionService, 'query').mockReturnValue(of(new HttpResponse({ body: questionCollection })));
      const additionalQuestions = [...questions];
      const expectedCollection: IQuestion[] = [...additionalQuestions, ...questionCollection];
      jest.spyOn(questionService, 'addQuestionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assessment });
      comp.ngOnInit();

      expect(questionService.query).toHaveBeenCalled();
      expect(questionService.addQuestionToCollectionIfMissing).toHaveBeenCalledWith(
        questionCollection,
        ...additionalQuestions.map(expect.objectContaining)
      );
      expect(comp.questionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const assessment: IAssessment = { id: 456 };
      const question: IQuestion = { id: 22530 };
      assessment.questions = [question];

      activatedRoute.data = of({ assessment });
      comp.ngOnInit();

      expect(comp.questionsSharedCollection).toContain(question);
      expect(comp.assessment).toEqual(assessment);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssessment>>();
      const assessment = { id: 123 };
      jest.spyOn(assessmentFormService, 'getAssessment').mockReturnValue(assessment);
      jest.spyOn(assessmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assessment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assessment }));
      saveSubject.complete();

      // THEN
      expect(assessmentFormService.getAssessment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(assessmentService.update).toHaveBeenCalledWith(expect.objectContaining(assessment));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssessment>>();
      const assessment = { id: 123 };
      jest.spyOn(assessmentFormService, 'getAssessment').mockReturnValue({ id: null });
      jest.spyOn(assessmentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assessment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assessment }));
      saveSubject.complete();

      // THEN
      expect(assessmentFormService.getAssessment).toHaveBeenCalled();
      expect(assessmentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssessment>>();
      const assessment = { id: 123 };
      jest.spyOn(assessmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assessment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(assessmentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareQuestion', () => {
      it('Should forward to questionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(questionService, 'compareQuestion');
        comp.compareQuestion(entity, entity2);
        expect(questionService.compareQuestion).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
