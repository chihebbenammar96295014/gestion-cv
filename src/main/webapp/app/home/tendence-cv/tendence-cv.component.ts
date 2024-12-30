import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'jhi-tendence-cv',
  templateUrl: './tendence-cv.component.html',
  standalone: true,
  imports: [RouterLink],
  styleUrls: ['./tendence-cv.component.scss'],
})
export class TendenceCvComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}
}
