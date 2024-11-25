import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ResumeFormService, ResumeFormGroup } from './resume-form.service';
import { IResume } from '../resume.model';
import { ResumeService } from '../service/resume.service';

@Component({
  selector: 'jhi-resume-update',
  templateUrl: './resume-update.component.html',
})
export class ResumeUpdateComponent implements OnInit {
  isSaving = false;
  resume: IResume | null = null;

  editForm: ResumeFormGroup = this.resumeFormService.createResumeFormGroup();

  constructor(
    protected resumeService: ResumeService,
    protected resumeFormService: ResumeFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ resume }) => {
      this.resume = resume;
      if (resume) {
        this.updateForm(resume);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const resume = this.resumeFormService.getResume(this.editForm);
    if (resume.id !== null) {
      this.subscribeToSaveResponse(this.resumeService.update(resume));
    } else {
      this.subscribeToSaveResponse(this.resumeService.create(resume));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IResume>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(resume: IResume): void {
    this.resume = resume;
    this.resumeFormService.resetForm(this.editForm, resume);
  }
}
