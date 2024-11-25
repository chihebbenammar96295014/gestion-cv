import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAssessment, NewAssessment } from '../assessment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAssessment for edit and NewAssessmentFormGroupInput for create.
 */
type AssessmentFormGroupInput = IAssessment | PartialWithRequiredKeyOf<NewAssessment>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAssessment | NewAssessment> = Omit<T, 'date'> & {
  date?: string | null;
};

type AssessmentFormRawValue = FormValueOf<IAssessment>;

type NewAssessmentFormRawValue = FormValueOf<NewAssessment>;

type AssessmentFormDefaults = Pick<NewAssessment, 'id' | 'date' | 'questions'>;

type AssessmentFormGroupContent = {
  id: FormControl<AssessmentFormRawValue['id'] | NewAssessment['id']>;
  nom: FormControl<AssessmentFormRawValue['nom']>;
  description: FormControl<AssessmentFormRawValue['description']>;
  niveau: FormControl<AssessmentFormRawValue['niveau']>;
  date: FormControl<AssessmentFormRawValue['date']>;
  questions: FormControl<AssessmentFormRawValue['questions']>;
};

export type AssessmentFormGroup = FormGroup<AssessmentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AssessmentFormService {
  createAssessmentFormGroup(assessment: AssessmentFormGroupInput = { id: null }): AssessmentFormGroup {
    const assessmentRawValue = this.convertAssessmentToAssessmentRawValue({
      ...this.getFormDefaults(),
      ...assessment,
    });
    return new FormGroup<AssessmentFormGroupContent>({
      id: new FormControl(
        { value: assessmentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nom: new FormControl(assessmentRawValue.nom, {
        validators: [Validators.required],
      }),
      description: new FormControl(assessmentRawValue.description),
      niveau: new FormControl(assessmentRawValue.niveau),
      date: new FormControl(assessmentRawValue.date),
      questions: new FormControl(assessmentRawValue.questions ?? []),
    });
  }

  getAssessment(form: AssessmentFormGroup): IAssessment | NewAssessment {
    return this.convertAssessmentRawValueToAssessment(form.getRawValue() as AssessmentFormRawValue | NewAssessmentFormRawValue);
  }

  resetForm(form: AssessmentFormGroup, assessment: AssessmentFormGroupInput): void {
    const assessmentRawValue = this.convertAssessmentToAssessmentRawValue({ ...this.getFormDefaults(), ...assessment });
    form.reset(
      {
        ...assessmentRawValue,
        id: { value: assessmentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AssessmentFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      date: currentTime,
      questions: [],
    };
  }

  private convertAssessmentRawValueToAssessment(
    rawAssessment: AssessmentFormRawValue | NewAssessmentFormRawValue
  ): IAssessment | NewAssessment {
    return {
      ...rawAssessment,
      date: dayjs(rawAssessment.date, DATE_TIME_FORMAT),
    };
  }

  private convertAssessmentToAssessmentRawValue(
    assessment: IAssessment | (Partial<NewAssessment> & AssessmentFormDefaults)
  ): AssessmentFormRawValue | PartialWithRequiredKeyOf<NewAssessmentFormRawValue> {
    return {
      ...assessment,
      date: assessment.date ? assessment.date.format(DATE_TIME_FORMAT) : undefined,
      questions: assessment.questions ?? [],
    };
  }
}
