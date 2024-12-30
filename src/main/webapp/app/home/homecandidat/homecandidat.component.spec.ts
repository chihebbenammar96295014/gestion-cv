import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HomecandidatComponent } from './homecandidat.component';

describe('HomecandidatComponent', () => {
  let component: HomecandidatComponent;
  let fixture: ComponentFixture<HomecandidatComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [HomecandidatComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(HomecandidatComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
