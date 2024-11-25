import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAppUser, NewAppUser } from '../app-user.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAppUser for edit and NewAppUserFormGroupInput for create.
 */
type AppUserFormGroupInput = IAppUser | PartialWithRequiredKeyOf<NewAppUser>;

type AppUserFormDefaults = Pick<NewAppUser, 'id'>;

type AppUserFormGroupContent = {
  id: FormControl<IAppUser['id'] | NewAppUser['id']>;
  nom: FormControl<IAppUser['nom']>;
  prenom: FormControl<IAppUser['prenom']>;
  email: FormControl<IAppUser['email']>;
  motDePasse: FormControl<IAppUser['motDePasse']>;
  adresse: FormControl<IAppUser['adresse']>;
  numTelephone: FormControl<IAppUser['numTelephone']>;
  typeUser: FormControl<IAppUser['typeUser']>;
  resume: FormControl<IAppUser['resume']>;
};

export type AppUserFormGroup = FormGroup<AppUserFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AppUserFormService {
  createAppUserFormGroup(appUser: AppUserFormGroupInput = { id: null }): AppUserFormGroup {
    const appUserRawValue = {
      ...this.getFormDefaults(),
      ...appUser,
    };
    return new FormGroup<AppUserFormGroupContent>({
      id: new FormControl(
        { value: appUserRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nom: new FormControl(appUserRawValue.nom, {
        validators: [Validators.required],
      }),
      prenom: new FormControl(appUserRawValue.prenom, {
        validators: [Validators.required],
      }),
      email: new FormControl(appUserRawValue.email, {
        validators: [Validators.required],
      }),
      motDePasse: new FormControl(appUserRawValue.motDePasse, {
        validators: [Validators.required],
      }),
      adresse: new FormControl(appUserRawValue.adresse, {
        validators: [Validators.required],
      }),
      numTelephone: new FormControl(appUserRawValue.numTelephone),
      typeUser: new FormControl(appUserRawValue.typeUser, {
        validators: [Validators.required],
      }),
      resume: new FormControl(appUserRawValue.resume),
    });
  }

  getAppUser(form: AppUserFormGroup): IAppUser | NewAppUser {
    return form.getRawValue() as IAppUser | NewAppUser;
  }

  resetForm(form: AppUserFormGroup, appUser: AppUserFormGroupInput): void {
    const appUserRawValue = { ...this.getFormDefaults(), ...appUser };
    form.reset(
      {
        ...appUserRawValue,
        id: { value: appUserRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AppUserFormDefaults {
    return {
      id: null,
    };
  }
}
