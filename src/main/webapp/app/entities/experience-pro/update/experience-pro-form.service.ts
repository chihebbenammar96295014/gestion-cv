import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IExperiencePro, NewExperiencePro } from '../experience-pro.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IExperiencePro for edit and NewExperienceProFormGroupInput for create.
 */
type ExperienceProFormGroupInput = IExperiencePro | PartialWithRequiredKeyOf<NewExperiencePro>;

type ExperienceProFormDefaults = Pick<NewExperiencePro, 'id'>;

type ExperienceProFormGroupContent = {
  id: FormControl<IExperiencePro['id'] | NewExperiencePro['id']>;
  startDate: FormControl<IExperiencePro['startDate']>;
  endDate: FormControl<IExperiencePro['endDate']>;
  title: FormControl<IExperiencePro['title']>;
  description: FormControl<IExperiencePro['description']>;
  fonction: FormControl<IExperiencePro['fonction']>;
  place: FormControl<IExperiencePro['place']>;
  resume: FormControl<IExperiencePro['resume']>;
};

export type ExperienceProFormGroup = FormGroup<ExperienceProFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ExperienceProFormService {
  createExperienceProFormGroup(experiencePro: ExperienceProFormGroupInput = { id: null }): ExperienceProFormGroup {
    const experienceProRawValue = {
      ...this.getFormDefaults(),
      ...experiencePro,
    };
    return new FormGroup<ExperienceProFormGroupContent>({
      id: new FormControl(
        { value: experienceProRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      startDate: new FormControl(experienceProRawValue.startDate),
      endDate: new FormControl(experienceProRawValue.endDate),
      title: new FormControl(experienceProRawValue.title, {
        validators: [Validators.required],
      }),
      description: new FormControl(experienceProRawValue.description),
      fonction: new FormControl(experienceProRawValue.fonction),
      place: new FormControl(experienceProRawValue.place),
      resume: new FormControl(experienceProRawValue.resume),
    });
  }

  getExperiencePro(form: ExperienceProFormGroup): IExperiencePro | NewExperiencePro {
    return form.getRawValue() as IExperiencePro | NewExperiencePro;
  }

  resetForm(form: ExperienceProFormGroup, experiencePro: ExperienceProFormGroupInput): void {
    const experienceProRawValue = { ...this.getFormDefaults(), ...experiencePro };
    form.reset(
      {
        ...experienceProRawValue,
        id: { value: experienceProRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ExperienceProFormDefaults {
    return {
      id: null,
    };
  }
}
