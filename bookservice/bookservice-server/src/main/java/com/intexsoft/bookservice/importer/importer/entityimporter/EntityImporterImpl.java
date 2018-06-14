package com.intexsoft.bookservice.importer.importer.entityimporter;

import com.intexsoft.bookservice.dao.entity.Author;
import com.intexsoft.bookservice.dao.entity.Book;
import com.intexsoft.bookservice.dao.entity.Publisher;
import com.intexsoft.bookservice.importer.entity.ImportAuthor;
import com.intexsoft.bookservice.importer.entity.ImportBook;
import com.intexsoft.bookservice.importer.entity.ImportPublisher;
import com.intexsoft.bookservice.importer.entity.repository.ImportEntityRepository;
import com.intexsoft.bookservice.service.api.AuthorService;
import com.intexsoft.bookservice.service.api.BookService;
import com.intexsoft.bookservice.service.api.PublisherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EntityImporterImpl implements EntityImporter {

    private static final Logger logger = LoggerFactory.getLogger("log");

    @Value("${import.images.path}")
    private String importImagesPath;

    @Autowired
    private PublisherService publisherService;

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private ImageWorker imageWorker;

    @Override
    public void importEntities(ImportEntityRepository entityRepository) throws Exception {
        List<ImportBook> books = entityRepository.getBooks();
        List<ImportAuthor> authors = entityRepository.getAuthors();
        List<ImportPublisher> publishers = entityRepository.getPublishers();
        importAuthorsToDB(authors);
        importPublishersToDB(publishers);
        importBooksToDB(books);
    }

    private void importPublishersToDB(List<ImportPublisher> publishers) {
        for (ImportPublisher importPublisher : publishers) {
            Publisher publisher = publisherService.getByUUID(importPublisher.getUuid());
            if (publisher == null) {
                publisher = new Publisher();
                publisher.setUuid(importPublisher.getUuid());
            }
            publisher.setName(importPublisher.getName());
            publisherService.add(publisher);
        }
    }

    private void importAuthorsToDB(List<ImportAuthor> authors) {
        for (ImportAuthor importAuthor : authors) {
            Author author = authorService.getByUUID(importAuthor.getUuid());
            if (author == null) {
                author = new Author();
                author.setUuid(importAuthor.getUuid());
            }
            author.setName(importAuthor.getName());
            author.setBio(importAuthor.getBio());
            author.setBirthDay(importAuthor.getBirthDay());
            authorService.add(author);
        }
    }


    private void importBooksToDB(List<ImportBook> books) throws Exception {
        try {
            imageWorker.unzipImages();
            for (ImportBook importBook : books) {
                String uuid = importBook.getUuid();
                Book book = bookService.getByUUID(uuid);
                if (book == null) {
                    book = new Book();
                    book.setUuid(importBook.getUuid());
                }
                book.setName(importBook.getName());
                book.setDescription(importBook.getDescription());
                book.setPrice(importBook.getPrice());
                book.setPublishDate(importBook.getPublishDate());
                book.setPublisher(publisherService.getByUUID(importBook.getPublisherUUID()));
                book.setAuthors(getAuthors(importBook.getAuthorsUUID()));
                if (book.getPublisher() == null) {
                    break;
                }
                for (Author author : book.getAuthors()) {
                    if (author == null) {
                        break;
                    }
                }
                imageWorker.processCover(book, importBook.getCoverPath());
                imageWorker.processImages(book, importBook.getPagesPath());
                bookService.add(book);
            }
        } finally {
            imageWorker.deleteFolder(importImagesPath);
        }

    }

    private List<Author> getAuthors(List<String> uuids) {
        List<Author> authors = new ArrayList<>();
        for (String uuid : uuids) {
            authors.add(authorService.getByUUID(uuid));
        }
        return authors;
    }


}
