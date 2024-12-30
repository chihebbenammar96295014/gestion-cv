import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListFormulaireCvComponent } from './list-formulaire-cv.component';

describe('ListFormulaireCvComponent', () => {
  let component: ListFormulaireCvComponent;
  let fixture: ComponentFixture<ListFormulaireCvComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListFormulaireCvComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ListFormulaireCvComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
