import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-homecandidat',
  templateUrl: './homecandidat.component.html',
  styleUrls: ['./homecandidat.component.scss'],
})
export class HomecandidatComponent implements OnInit {
  constructor(private router: Router) {}

  ngOnInit(): void {}

  navigateToForm() {
    this.router.navigate(['/formulaire']);
  }
}
