import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';

import {Author} from '../entity/author.model';


const httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable()
export class AuthorService {

    constructor(private http: HttpClient) {
    }

    public getAuthors() {
        return this.http.get<Author[]>('http://localhost:8080/bookservice/api/authors');
    }

    public addAuthor(author) {
        return this.http.post('http://localhost:8080/bookservice/api/author', author);
    }

    public deleteAuthor(author: Author) {
        return this.http.delete('http://localhost:8080/bookservice/api/author' + '/' + author.id.toString());
    }
}
