import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'jhi-entretien',
  templateUrl: './entretien.component.html',
  styleUrls: ['./entretien.component.scss'],
  imports: [RouterLink],
  standalone: true,
})
export class EntretienComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}
}
