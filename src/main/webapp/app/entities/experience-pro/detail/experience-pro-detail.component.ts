import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IExperiencePro } from '../experience-pro.model';

@Component({
  selector: 'jhi-experience-pro-detail',
  templateUrl: './experience-pro-detail.component.html',
})
export class ExperienceProDetailComponent implements OnInit {
  experiencePro: IExperiencePro | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ experiencePro }) => {
      this.experiencePro = experiencePro;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
