import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IExperiencePro } from '../experience-pro.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../experience-pro.test-samples';

import { ExperienceProService, RestExperiencePro } from './experience-pro.service';

const requireRestSample: RestExperiencePro = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.format(DATE_FORMAT),
  endDate: sampleWithRequiredData.endDate?.format(DATE_FORMAT),
};

describe('ExperiencePro Service', () => {
  let service: ExperienceProService;
  let httpMock: HttpTestingController;
  let expectedResult: IExperiencePro | IExperiencePro[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ExperienceProService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a ExperiencePro', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const experiencePro = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(experiencePro).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ExperiencePro', () => {
      const experiencePro = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(experiencePro).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ExperiencePro', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ExperiencePro', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ExperiencePro', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addExperienceProToCollectionIfMissing', () => {
      it('should add a ExperiencePro to an empty array', () => {
        const experiencePro: IExperiencePro = sampleWithRequiredData;
        expectedResult = service.addExperienceProToCollectionIfMissing([], experiencePro);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(experiencePro);
      });

      it('should not add a ExperiencePro to an array that contains it', () => {
        const experiencePro: IExperiencePro = sampleWithRequiredData;
        const experienceProCollection: IExperiencePro[] = [
          {
            ...experiencePro,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addExperienceProToCollectionIfMissing(experienceProCollection, experiencePro);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ExperiencePro to an array that doesn't contain it", () => {
        const experiencePro: IExperiencePro = sampleWithRequiredData;
        const experienceProCollection: IExperiencePro[] = [sampleWithPartialData];
        expectedResult = service.addExperienceProToCollectionIfMissing(experienceProCollection, experiencePro);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(experiencePro);
      });

      it('should add only unique ExperiencePro to an array', () => {
        const experienceProArray: IExperiencePro[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const experienceProCollection: IExperiencePro[] = [sampleWithRequiredData];
        expectedResult = service.addExperienceProToCollectionIfMissing(experienceProCollection, ...experienceProArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const experiencePro: IExperiencePro = sampleWithRequiredData;
        const experiencePro2: IExperiencePro = sampleWithPartialData;
        expectedResult = service.addExperienceProToCollectionIfMissing([], experiencePro, experiencePro2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(experiencePro);
        expect(expectedResult).toContain(experiencePro2);
      });

      it('should accept null and undefined values', () => {
        const experiencePro: IExperiencePro = sampleWithRequiredData;
        expectedResult = service.addExperienceProToCollectionIfMissing([], null, experiencePro, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(experiencePro);
      });

      it('should return initial array if no ExperiencePro is added', () => {
        const experienceProCollection: IExperiencePro[] = [sampleWithRequiredData];
        expectedResult = service.addExperienceProToCollectionIfMissing(experienceProCollection, undefined, null);
        expect(expectedResult).toEqual(experienceProCollection);
      });
    });

    describe('compareExperiencePro', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareExperiencePro(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareExperiencePro(entity1, entity2);
        const compareResult2 = service.compareExperiencePro(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareExperiencePro(entity1, entity2);
        const compareResult2 = service.compareExperiencePro(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareExperiencePro(entity1, entity2);
        const compareResult2 = service.compareExperiencePro(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
