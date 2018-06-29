package com.intexsoft.bookservice.service.implementation;

import com.intexsoft.bookservice.dao.entity.Book;
import com.intexsoft.bookservice.dao.entity.Review;
import com.intexsoft.bookservice.dao.repository.ReviewRepository;
import com.intexsoft.bookservice.service.api.BookService;
import com.intexsoft.bookservice.service.api.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BookService bookService;

    @Override
    public List<Review> getAllReview() {
        return reviewRepository.findAll();
    }

    @Override
    public void add(Review review) {
        reviewRepository.save(review);
        Book bookReview = bookService.getBookById(review.getBook().getId());
        bookReview.setRate(getRate(bookReview));
    }

    @Override
    public void delete(Review review) {
        reviewRepository.delete(review);
    }

    @Override
    public Optional<Review> getReviewByID(Integer id) {
        return reviewRepository.findById(id);
    }

    @Override
    public Page<Review> getBookReviews(Integer bookId, Pageable pageable) {
        return reviewRepository.findBookReviews(bookId, pageable);
    }

    @Override
    public Page<Review> getUserReviews(Integer userId, Pageable pageable) {
        return reviewRepository.findUserReviews(userId, pageable);
    }

    @Override
    public List<Review> getAllBookreviews(Integer bookId) {
        return reviewRepository.findAllBookReviews(bookId);
    }

    private Double getRate(Book book) {
        List<Review> reviews = reviewRepository.findAllBookReviews(book.getId());
        Double sumRate = 0.0;
        for (Review review : reviews) {
            sumRate += review.getRate();
        }
        Double rate = (sumRate != 0) ? (sumRate / reviews.size()) : 0.0;
        return Double.valueOf(new DecimalFormat("#.#").format(rate));
    }
}
