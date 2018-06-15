package com.intexsoft.bookservice.dao.repository;

import com.intexsoft.bookservice.dao.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Query(value = "SELECT * FROM review where review.bookId = ?1", nativeQuery = true)
    List<Review> findBookReview(Integer id);

    @Query(value = "SELECT * FROM review where review.bookId = ?1", nativeQuery = true)
    List<Review> findUserReview(Integer id);


}
