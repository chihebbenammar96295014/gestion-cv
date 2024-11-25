import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AppUserFormService } from './app-user-form.service';
import { AppUserService } from '../service/app-user.service';
import { IAppUser } from '../app-user.model';
import { IResume } from 'app/entities/resume/resume.model';
import { ResumeService } from 'app/entities/resume/service/resume.service';

import { AppUserUpdateComponent } from './app-user-update.component';

describe('AppUser Management Update Component', () => {
  let comp: AppUserUpdateComponent;
  let fixture: ComponentFixture<AppUserUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let appUserFormService: AppUserFormService;
  let appUserService: AppUserService;
  let resumeService: ResumeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AppUserUpdateComponent],
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
      .overrideTemplate(AppUserUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AppUserUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    appUserFormService = TestBed.inject(AppUserFormService);
    appUserService = TestBed.inject(AppUserService);
    resumeService = TestBed.inject(ResumeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call resume query and add missing value', () => {
      const appUser: IAppUser = { id: 456 };
      const resume: IResume = { id: 42444 };
      appUser.resume = resume;

      const resumeCollection: IResume[] = [{ id: 45946 }];
      jest.spyOn(resumeService, 'query').mockReturnValue(of(new HttpResponse({ body: resumeCollection })));
      const expectedCollection: IResume[] = [resume, ...resumeCollection];
      jest.spyOn(resumeService, 'addResumeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ appUser });
      comp.ngOnInit();

      expect(resumeService.query).toHaveBeenCalled();
      expect(resumeService.addResumeToCollectionIfMissing).toHaveBeenCalledWith(resumeCollection, resume);
      expect(comp.resumesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const appUser: IAppUser = { id: 456 };
      const resume: IResume = { id: 71645 };
      appUser.resume = resume;

      activatedRoute.data = of({ appUser });
      comp.ngOnInit();

      expect(comp.resumesCollection).toContain(resume);
      expect(comp.appUser).toEqual(appUser);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAppUser>>();
      const appUser = { id: 123 };
      jest.spyOn(appUserFormService, 'getAppUser').mockReturnValue(appUser);
      jest.spyOn(appUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: appUser }));
      saveSubject.complete();

      // THEN
      expect(appUserFormService.getAppUser).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(appUserService.update).toHaveBeenCalledWith(expect.objectContaining(appUser));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAppUser>>();
      const appUser = { id: 123 };
      jest.spyOn(appUserFormService, 'getAppUser').mockReturnValue({ id: null });
      jest.spyOn(appUserService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appUser: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: appUser }));
      saveSubject.complete();

      // THEN
      expect(appUserFormService.getAppUser).toHaveBeenCalled();
      expect(appUserService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAppUser>>();
      const appUser = { id: 123 };
      jest.spyOn(appUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(appUserService.update).toHaveBeenCalled();
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
