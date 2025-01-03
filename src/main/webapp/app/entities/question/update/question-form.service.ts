import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IQuestion, NewQuestion } from '../question.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IQuestion for edit and NewQuestionFormGroupInput for create.
 */
type QuestionFormGroupInput = IQuestion | PartialWithRequiredKeyOf<NewQuestion>;

type QuestionFormDefaults = Pick<NewQuestion, 'id' | 'assessments'>;

type QuestionFormGroupContent = {
  id: FormControl<IQuestion['id'] | NewQuestion['id']>;
  order: FormControl<IQuestion['order']>;
  question: FormControl<IQuestion['question']>;
  reponseCorrecte: FormControl<IQuestion['reponseCorrecte']>;
  assessments: FormControl<IQuestion['assessments']>;
};

export type QuestionFormGroup = FormGroup<QuestionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class QuestionFormService {
  createQuestionFormGroup(question: QuestionFormGroupInput = { id: null }): QuestionFormGroup {
    const questionRawValue = {
      ...this.getFormDefaults(),
      ...question,
    };
    return new FormGroup<QuestionFormGroupContent>({
      id: new FormControl(
        { value: questionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      order: new FormControl(questionRawValue.order, {
        validators: [Validators.required],
      }),
      question: new FormControl(questionRawValue.question, {
        validators: [Validators.required],
      }),
      reponseCorrecte: new FormControl(questionRawValue.reponseCorrecte, {
        validators: [Validators.required],
      }),
      assessments: new FormControl(questionRawValue.assessments ?? []),
    });
  }

  getQuestion(form: QuestionFormGroup): IQuestion | NewQuestion {
    return form.getRawValue() as IQuestion | NewQuestion;
  }

  resetForm(form: QuestionFormGroup, question: QuestionFormGroupInput): void {
    const questionRawValue = { ...this.getFormDefaults(), ...question };
    form.reset(
      {
        ...questionRawValue,
        id: { value: questionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): QuestionFormDefaults {
    return {
      id: null,
      assessments: [],
    };
  }
}
