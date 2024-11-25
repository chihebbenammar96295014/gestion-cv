import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IExperiencePro } from '../experience-pro.model';
import { ExperienceProService } from '../service/experience-pro.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './experience-pro-delete-dialog.component.html',
})
export class ExperienceProDeleteDialogComponent {
  experiencePro?: IExperiencePro;

  constructor(protected experienceProService: ExperienceProService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.experienceProService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
