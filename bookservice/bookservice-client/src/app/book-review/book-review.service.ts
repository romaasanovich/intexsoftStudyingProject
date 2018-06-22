import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Review} from '../entity/review/review.model';
import {AppSettings} from '../../../app.settings';
import {ReviewPageModel} from '../entity/review/review.page.model';


@Injectable()
export class BookReviewService {

    constructor(private http: HttpClient) {
    }

    public getBookReviews(id, page, size) {
        const params = new HttpParams()
            .set('page', page)
            .set('size', size);
        return this.http.get<ReviewPageModel>(`${AppSettings.API_URL}/reviews/${id}`, {params});
    }

    public addReview(review: Review) {
        return this.http.post(`${AppSettings.API_URL}/review/`, review);
    }

    public delReview(review: Review) {
        return this.http.delete(`${AppSettings.API_URL}/review/${review.id.toString()}`);
    }
}
