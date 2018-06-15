import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {BookComponent} from '../book/book.component';
import {PublisherComponent} from '../publisher/publisher.component';
import {AuthorComponent} from '../author/author.component';
import {AddPublisherComponent} from '../add-publisher/add-publisher.component';
import {AddAuthorComponent} from '../add-author/add-author.component';
import {AddBookComponent} from '../add-book/add-book.component';
import {ImportComponent} from '../import/import.component';
import {BookReviewComponent} from '../book-review/book-review.component';
import {AddReviewComponent} from '../add-review/add-review.component';
import {BookDetailsComponent} from '../book-details/book-details.component';


const routes: Routes = [
    {path: 'books', component: BookComponent},
    {path: 'publishers', component: PublisherComponent},
    {path: 'authors', component: AuthorComponent},
    {path: 'publisher', component: AddPublisherComponent},
    {path: 'author', component: AddAuthorComponent},
    {path: 'book', component: AddBookComponent},
    {path: 'import', component: ImportComponent},
    {path: 'reviews', component: BookReviewComponent},
    {path: 'add-review', component: AddReviewComponent},
    {path: 'bookDetail', component: BookDetailsComponent}
];

@NgModule({
    imports: [
        RouterModule.forRoot(routes)
    ],
    exports: [
        RouterModule
    ],
    declarations: []
})
export class AppRoutingModule {
}
