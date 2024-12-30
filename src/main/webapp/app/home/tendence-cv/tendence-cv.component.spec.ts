import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TendenceCvComponent } from './tendence-cv.component';

describe('TendenceCvComponent', () => {
  let component: TendenceCvComponent;
  let fixture: ComponentFixture<TendenceCvComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TendenceCvComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(TendenceCvComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
