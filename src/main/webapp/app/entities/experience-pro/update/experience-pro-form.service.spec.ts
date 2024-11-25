import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../experience-pro.test-samples';

import { ExperienceProFormService } from './experience-pro-form.service';

describe('ExperiencePro Form Service', () => {
  let service: ExperienceProFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExperienceProFormService);
  });

  describe('Service methods', () => {
    describe('createExperienceProFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createExperienceProFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            title: expect.any(Object),
            description: expect.any(Object),
            fonction: expect.any(Object),
            place: expect.any(Object),
            resume: expect.any(Object),
          })
        );
      });

      it('passing IExperiencePro should create a new form with FormGroup', () => {
        const formGroup = service.createExperienceProFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            title: expect.any(Object),
            description: expect.any(Object),
            fonction: expect.any(Object),
            place: expect.any(Object),
            resume: expect.any(Object),
          })
        );
      });
    });

    describe('getExperiencePro', () => {
      it('should return NewExperiencePro for default ExperiencePro initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createExperienceProFormGroup(sampleWithNewData);

        const experiencePro = service.getExperiencePro(formGroup) as any;

        expect(experiencePro).toMatchObject(sampleWithNewData);
      });

      it('should return NewExperiencePro for empty ExperiencePro initial value', () => {
        const formGroup = service.createExperienceProFormGroup();

        const experiencePro = service.getExperiencePro(formGroup) as any;

        expect(experiencePro).toMatchObject({});
      });

      it('should return IExperiencePro', () => {
        const formGroup = service.createExperienceProFormGroup(sampleWithRequiredData);

        const experiencePro = service.getExperiencePro(formGroup) as any;

        expect(experiencePro).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IExperiencePro should not enable id FormControl', () => {
        const formGroup = service.createExperienceProFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewExperiencePro should disable id FormControl', () => {
        const formGroup = service.createExperienceProFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
