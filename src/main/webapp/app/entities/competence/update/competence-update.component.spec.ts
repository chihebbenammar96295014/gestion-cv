import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CompetenceFormService } from './competence-form.service';
import { CompetenceService } from '../service/competence.service';
import { ICompetence } from '../competence.model';
import { IResume } from 'app/entities/resume/resume.model';
import { ResumeService } from 'app/entities/resume/service/resume.service';

import { CompetenceUpdateComponent } from './competence-update.component';

describe('Competence Management Update Component', () => {
  let comp: CompetenceUpdateComponent;
  let fixture: ComponentFixture<CompetenceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let competenceFormService: CompetenceFormService;
  let competenceService: CompetenceService;
  let resumeService: ResumeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CompetenceUpdateComponent],
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
      .overrideTemplate(CompetenceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CompetenceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    competenceFormService = TestBed.inject(CompetenceFormService);
    competenceService = TestBed.inject(CompetenceService);
    resumeService = TestBed.inject(ResumeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Resume query and add missing value', () => {
      const competence: ICompetence = { id: 456 };
      const resume: IResume = { id: 77779 };
      competence.resume = resume;

      const resumeCollection: IResume[] = [{ id: 53494 }];
      jest.spyOn(resumeService, 'query').mockReturnValue(of(new HttpResponse({ body: resumeCollection })));
      const additionalResumes = [resume];
      const expectedCollection: IResume[] = [...additionalResumes, ...resumeCollection];
      jest.spyOn(resumeService, 'addResumeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ competence });
      comp.ngOnInit();

      expect(resumeService.query).toHaveBeenCalled();
      expect(resumeService.addResumeToCollectionIfMissing).toHaveBeenCalledWith(
        resumeCollection,
        ...additionalResumes.map(expect.objectContaining)
      );
      expect(comp.resumesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const competence: ICompetence = { id: 456 };
      const resume: IResume = { id: 82517 };
      competence.resume = resume;

      activatedRoute.data = of({ competence });
      comp.ngOnInit();

      expect(comp.resumesSharedCollection).toContain(resume);
      expect(comp.competence).toEqual(competence);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompetence>>();
      const competence = { id: 123 };
      jest.spyOn(competenceFormService, 'getCompetence').mockReturnValue(competence);
      jest.spyOn(competenceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ competence });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: competence }));
      saveSubject.complete();

      // THEN
      expect(competenceFormService.getCompetence).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(competenceService.update).toHaveBeenCalledWith(expect.objectContaining(competence));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompetence>>();
      const competence = { id: 123 };
      jest.spyOn(competenceFormService, 'getCompetence').mockReturnValue({ id: null });
      jest.spyOn(competenceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ competence: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: competence }));
      saveSubject.complete();

      // THEN
      expect(competenceFormService.getCompetence).toHaveBeenCalled();
      expect(competenceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompetence>>();
      const competence = { id: 123 };
      jest.spyOn(competenceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ competence });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(competenceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareResume', () => {
      it('Should forward to resumeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(resumeService, 'compareResume');
        comp.compareResume(entity, entity2);
        expect(resumeService.compareResume).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
