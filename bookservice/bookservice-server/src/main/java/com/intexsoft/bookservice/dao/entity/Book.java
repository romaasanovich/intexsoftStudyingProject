package com.intexsoft.bookservice.dao.entity;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.intexsoft.bookservice.dao.entity.aentity.AbstractEntity;
import com.intexsoft.bookservice.utils.converter.localdate.LocalDateDeserializer;
import com.intexsoft.bookservice.utils.converter.localdate.LocalDateSerializer;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "book")
public class Book extends AbstractEntity {
    @Column(name = "name", length = 45)
    private String name;
    @Column(name = "description", length = 1500)
    private String description;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "rate")
    private double rate;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @Column(name = "publishDate")
    private LocalDate publishDate;
    @ManyToOne
    @JoinColumn(name = "publisherId")
    private Publisher publisher;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "book_author",
            joinColumns = {@JoinColumn(name = "bookId")},
            inverseJoinColumns = {@JoinColumn(name = "authorId")})
    private List<Author> authors;

    public Book() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public BigDecimal getPrice() {
        return new BigDecimal(new DecimalFormat("#.##").format(price));
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
