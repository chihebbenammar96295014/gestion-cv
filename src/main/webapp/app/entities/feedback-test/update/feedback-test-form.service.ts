import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFeedbackTest, NewFeedbackTest } from '../feedback-test.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFeedbackTest for edit and NewFeedbackTestFormGroupInput for create.
 */
type FeedbackTestFormGroupInput = IFeedbackTest | PartialWithRequiredKeyOf<NewFeedbackTest>;

type FeedbackTestFormDefaults = Pick<NewFeedbackTest, 'id'>;

type FeedbackTestFormGroupContent = {
  id: FormControl<IFeedbackTest['id'] | NewFeedbackTest['id']>;
  note: FormControl<IFeedbackTest['note']>;
  commentaires: FormControl<IFeedbackTest['commentaires']>;
  appUser: FormControl<IFeedbackTest['appUser']>;
  assessment: FormControl<IFeedbackTest['assessment']>;
};

export type FeedbackTestFormGroup = FormGroup<FeedbackTestFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FeedbackTestFormService {
  createFeedbackTestFormGroup(feedbackTest: FeedbackTestFormGroupInput = { id: null }): FeedbackTestFormGroup {
    const feedbackTestRawValue = {
      ...this.getFormDefaults(),
      ...feedbackTest,
    };
    return new FormGroup<FeedbackTestFormGroupContent>({
      id: new FormControl(
        { value: feedbackTestRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      note: new FormControl(feedbackTestRawValue.note),
      commentaires: new FormControl(feedbackTestRawValue.commentaires),
      appUser: new FormControl(feedbackTestRawValue.appUser),
      assessment: new FormControl(feedbackTestRawValue.assessment),
    });
  }

  getFeedbackTest(form: FeedbackTestFormGroup): IFeedbackTest | NewFeedbackTest {
    return form.getRawValue() as IFeedbackTest | NewFeedbackTest;
  }

  resetForm(form: FeedbackTestFormGroup, feedbackTest: FeedbackTestFormGroupInput): void {
    const feedbackTestRawValue = { ...this.getFormDefaults(), ...feedbackTest };
    form.reset(
      {
        ...feedbackTestRawValue,
        id: { value: feedbackTestRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FeedbackTestFormDefaults {
    return {
      id: null,
    };
  }
}
