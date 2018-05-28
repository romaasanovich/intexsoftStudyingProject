package com.intexsoft.bookservice.importer.importer;

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
import com.intexsoft.bookservice.utill.Converter;
import com.intexsoft.bookservice.utill.Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImporterXmlImpl implements Importer {

    private final Logger logger = LoggerFactory.getLogger("log");
    @Autowired
    PublisherService publisherService;
    @Autowired
    AuthorService authorService;
    @Autowired
    BookService bookService;
    @Value("${xmlImport}")
    private String xmlPath;

    @Override
    public TypeImport getType() {
        return TypeImport.xml;
    }

    @Transactional
    @Override
    public void importToDb() {
        try {
            Reader reader = new Reader();
            Converter converter = new Converter();
            ImportEntityRepository entityRep = converter.fromXmlToEntityRep(reader.getFile(xmlPath));
            List<ImportBook> books = entityRep.getBooks();
            List<ImportAuthor> authors = entityRep.getAuthors();
            List<ImportPublisher> publishers = entityRep.getPublishers();
            logger.info("Xml is parse!!!");
            importPublishersToDB(publishers);
            logger.info("Publishers are import");
            importAuthorsToDB(authors);
            logger.info("Authors are import");
            importBooksToDB(books);
            logger.info("Books are import");
        } catch (NullPointerException ex) {
            logger.error("File not found: ", ex);
        } catch (IOException ex1) {
            logger.error("File not found: ", ex1);
        } catch (JAXBException er) {
            logger.error("Wrong XML structure: ", er);
        }
    }

    private void importPublishersToDB(List<ImportPublisher> publishers) {
        for (ImportPublisher importPublisher : publishers) {
            Publisher publisher = publisherService.getByUUID(importPublisher.getUuid());
            if (publisher == null) {
                publisher = new Publisher();
            }
            publisher.setName(importPublisher.getName());
            publisher.setUuid(importPublisher.getUuid());
            publisherService.add(publisher);
        }
    }

    private void importAuthorsToDB(List<ImportAuthor> authors) {
        for (ImportAuthor importAuthor : authors) {
            Author author = authorService.getByUUID(importAuthor.getUuid());
            if (author == null) {
                author = new Author();
            }
            author.setName(importAuthor.getName());
            author.setBio(importAuthor.getBio());
            author.setBirthDay(importAuthor.getBirthDay());
            author.setUuid(importAuthor.getUuid());
            authorService.add(author);
        }
    }


    private void importBooksToDB(List<ImportBook> books) {
        for (ImportBook importBook : books) {
            String uuid = importBook.getUuid();
            Book book = bookService.getByUUID(uuid);
            if (book == null) {
                book = new Book();
            }
            book.setName(importBook.getName());
            book.setUuid(importBook.getUuid());
            book.setDescription(importBook.getDescription());
            book.setPrice(importBook.getPrice());
            book.setPublishDate(importBook.getPublishDate());
            book.setPublisher(publisherService.getByUUID(importBook.getPublisherUUID()));
            book.setAuthors(getAuthors(importBook.getAuthorsUUID()));
            if (book.getAuthors().get(0) == null || book.getPublisher() == null) {
                break;
            }
            bookService.add(book);
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

