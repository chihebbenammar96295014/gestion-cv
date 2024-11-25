import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFeedbackTest } from '../feedback-test.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../feedback-test.test-samples';

import { FeedbackTestService } from './feedback-test.service';

const requireRestSample: IFeedbackTest = {
  ...sampleWithRequiredData,
};

describe('FeedbackTest Service', () => {
  let service: FeedbackTestService;
  let httpMock: HttpTestingController;
  let expectedResult: IFeedbackTest | IFeedbackTest[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FeedbackTestService);
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

    it('should create a FeedbackTest', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const feedbackTest = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(feedbackTest).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FeedbackTest', () => {
      const feedbackTest = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(feedbackTest).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FeedbackTest', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FeedbackTest', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FeedbackTest', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFeedbackTestToCollectionIfMissing', () => {
      it('should add a FeedbackTest to an empty array', () => {
        const feedbackTest: IFeedbackTest = sampleWithRequiredData;
        expectedResult = service.addFeedbackTestToCollectionIfMissing([], feedbackTest);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(feedbackTest);
      });

      it('should not add a FeedbackTest to an array that contains it', () => {
        const feedbackTest: IFeedbackTest = sampleWithRequiredData;
        const feedbackTestCollection: IFeedbackTest[] = [
          {
            ...feedbackTest,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFeedbackTestToCollectionIfMissing(feedbackTestCollection, feedbackTest);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FeedbackTest to an array that doesn't contain it", () => {
        const feedbackTest: IFeedbackTest = sampleWithRequiredData;
        const feedbackTestCollection: IFeedbackTest[] = [sampleWithPartialData];
        expectedResult = service.addFeedbackTestToCollectionIfMissing(feedbackTestCollection, feedbackTest);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(feedbackTest);
      });

      it('should add only unique FeedbackTest to an array', () => {
        const feedbackTestArray: IFeedbackTest[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const feedbackTestCollection: IFeedbackTest[] = [sampleWithRequiredData];
        expectedResult = service.addFeedbackTestToCollectionIfMissing(feedbackTestCollection, ...feedbackTestArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const feedbackTest: IFeedbackTest = sampleWithRequiredData;
        const feedbackTest2: IFeedbackTest = sampleWithPartialData;
        expectedResult = service.addFeedbackTestToCollectionIfMissing([], feedbackTest, feedbackTest2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(feedbackTest);
        expect(expectedResult).toContain(feedbackTest2);
      });

      it('should accept null and undefined values', () => {
        const feedbackTest: IFeedbackTest = sampleWithRequiredData;
        expectedResult = service.addFeedbackTestToCollectionIfMissing([], null, feedbackTest, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(feedbackTest);
      });

      it('should return initial array if no FeedbackTest is added', () => {
        const feedbackTestCollection: IFeedbackTest[] = [sampleWithRequiredData];
        expectedResult = service.addFeedbackTestToCollectionIfMissing(feedbackTestCollection, undefined, null);
        expect(expectedResult).toEqual(feedbackTestCollection);
      });
    });

    describe('compareFeedbackTest', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFeedbackTest(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFeedbackTest(entity1, entity2);
        const compareResult2 = service.compareFeedbackTest(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFeedbackTest(entity1, entity2);
        const compareResult2 = service.compareFeedbackTest(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFeedbackTest(entity1, entity2);
        const compareResult2 = service.compareFeedbackTest(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
