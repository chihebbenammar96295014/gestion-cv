import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ExperienceProFormService } from './experience-pro-form.service';
import { ExperienceProService } from '../service/experience-pro.service';
import { IExperiencePro } from '../experience-pro.model';
import { IResume } from 'app/entities/resume/resume.model';
import { ResumeService } from 'app/entities/resume/service/resume.service';

import { ExperienceProUpdateComponent } from './experience-pro-update.component';

describe('ExperiencePro Management Update Component', () => {
  let comp: ExperienceProUpdateComponent;
  let fixture: ComponentFixture<ExperienceProUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let experienceProFormService: ExperienceProFormService;
  let experienceProService: ExperienceProService;
  let resumeService: ResumeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ExperienceProUpdateComponent],
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
      .overrideTemplate(ExperienceProUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ExperienceProUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    experienceProFormService = TestBed.inject(ExperienceProFormService);
    experienceProService = TestBed.inject(ExperienceProService);
    resumeService = TestBed.inject(ResumeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Resume query and add missing value', () => {
      const experiencePro: IExperiencePro = { id: 456 };
      const resume: IResume = { id: 81971 };
      experiencePro.resume = resume;

      const resumeCollection: IResume[] = [{ id: 75016 }];
      jest.spyOn(resumeService, 'query').mockReturnValue(of(new HttpResponse({ body: resumeCollection })));
      const additionalResumes = [resume];
      const expectedCollection: IResume[] = [...additionalResumes, ...resumeCollection];
      jest.spyOn(resumeService, 'addResumeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ experiencePro });
      comp.ngOnInit();

      expect(resumeService.query).toHaveBeenCalled();
      expect(resumeService.addResumeToCollectionIfMissing).toHaveBeenCalledWith(
        resumeCollection,
        ...additionalResumes.map(expect.objectContaining)
      );
      expect(comp.resumesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const experiencePro: IExperiencePro = { id: 456 };
      const resume: IResume = { id: 91547 };
      experiencePro.resume = resume;

      activatedRoute.data = of({ experiencePro });
      comp.ngOnInit();

      expect(comp.resumesSharedCollection).toContain(resume);
      expect(comp.experiencePro).toEqual(experiencePro);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExperiencePro>>();
      const experiencePro = { id: 123 };
      jest.spyOn(experienceProFormService, 'getExperiencePro').mockReturnValue(experiencePro);
      jest.spyOn(experienceProService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ experiencePro });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: experiencePro }));
      saveSubject.complete();

      // THEN
      expect(experienceProFormService.getExperiencePro).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(experienceProService.update).toHaveBeenCalledWith(expect.objectContaining(experiencePro));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExperiencePro>>();
      const experiencePro = { id: 123 };
      jest.spyOn(experienceProFormService, 'getExperiencePro').mockReturnValue({ id: null });
      jest.spyOn(experienceProService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ experiencePro: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: experiencePro }));
      saveSubject.complete();

      // THEN
      expect(experienceProFormService.getExperiencePro).toHaveBeenCalled();
      expect(experienceProService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExperiencePro>>();
      const experiencePro = { id: 123 };
      jest.spyOn(experienceProService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ experiencePro });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(experienceProService.update).toHaveBeenCalled();
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
