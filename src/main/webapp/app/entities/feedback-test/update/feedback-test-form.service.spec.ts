import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../feedback-test.test-samples';

import { FeedbackTestFormService } from './feedback-test-form.service';

describe('FeedbackTest Form Service', () => {
  let service: FeedbackTestFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FeedbackTestFormService);
  });

  describe('Service methods', () => {
    describe('createFeedbackTestFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFeedbackTestFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            note: expect.any(Object),
            commentaires: expect.any(Object),
            appUser: expect.any(Object),
            assessment: expect.any(Object),
          })
        );
      });

      it('passing IFeedbackTest should create a new form with FormGroup', () => {
        const formGroup = service.createFeedbackTestFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            note: expect.any(Object),
            commentaires: expect.any(Object),
            appUser: expect.any(Object),
            assessment: expect.any(Object),
          })
        );
      });
    });

    describe('getFeedbackTest', () => {
      it('should return NewFeedbackTest for default FeedbackTest initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFeedbackTestFormGroup(sampleWithNewData);

        const feedbackTest = service.getFeedbackTest(formGroup) as any;

        expect(feedbackTest).toMatchObject(sampleWithNewData);
      });

      it('should return NewFeedbackTest for empty FeedbackTest initial value', () => {
        const formGroup = service.createFeedbackTestFormGroup();

        const feedbackTest = service.getFeedbackTest(formGroup) as any;

        expect(feedbackTest).toMatchObject({});
      });

      it('should return IFeedbackTest', () => {
        const formGroup = service.createFeedbackTestFormGroup(sampleWithRequiredData);

        const feedbackTest = service.getFeedbackTest(formGroup) as any;

        expect(feedbackTest).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFeedbackTest should not enable id FormControl', () => {
        const formGroup = service.createFeedbackTestFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFeedbackTest should disable id FormControl', () => {
        const formGroup = service.createFeedbackTestFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
