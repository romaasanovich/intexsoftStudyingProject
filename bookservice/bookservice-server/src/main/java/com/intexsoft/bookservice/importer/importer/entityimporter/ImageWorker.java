package com.intexsoft.bookservice.importer.importer.entityimporter;

import com.intexsoft.bookservice.dao.entity.Book;
import com.intexsoft.bookservice.dao.entity.BookImage;
import com.intexsoft.bookservice.dao.entity.ImageType;
import com.intexsoft.bookservice.service.api.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Component
public class ImageWorker {

    private static final Logger logger = LoggerFactory.getLogger("log");

    @Autowired
    private ImageService imageService;

    @Value("${import.images.zip}")
    private String zipPath;

    @Value("${import.images.path}")
    private String tempImagesPath;

    @Value("${book.images.path}")
    private String imagesPath;


    public void unzipImages() throws IOException {
        try (ZipFile file = new ZipFile(zipPath)) {
            Enumeration<? extends ZipEntry> entries = file.entries();
            String uncompressedDirectory = tempImagesPath;
            Files.createDirectory(Paths.get(uncompressedDirectory));
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.isDirectory()) {
                    Files.createDirectories(Paths.get(uncompressedDirectory + entry.getName()));
                } else {
                    InputStream is = file.getInputStream(entry);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    String uncompressedFileName = uncompressedDirectory + entry.getName();
                    Path uncompressedFilePath = Paths.get(uncompressedFileName);
                    Files.createFile(uncompressedFilePath);
                    FileOutputStream fileOutput = new FileOutputStream(uncompressedFileName);
                    while (bis.available() > 0) {
                        fileOutput.write(bis.read());
                    }
                    fileOutput.close();
                }
            }
        } catch (IOException e) {
            logger.error("Archive not found:", e);
            throw e;
        }
    }

    public boolean deleteFolder() throws IOException {
        try {
            return FileSystemUtils.deleteRecursively(Paths.get(tempImagesPath));
        } catch (IOException e) {
            logger.error("IO Error: ", e);
            throw e;
        }
    }

    private void deletePages(List<BookImage> bookPages) {
        for (BookImage bookPage : bookPages) {
            try {
                Files.deleteIfExists(imageService.generateImagePath(bookPage));
            } catch (IOException e) {
                logger.error("IO Error: ", e);
            }
        }
    }


    public void processCover(Book book, String importCoverPath) {
        if (importCoverPath != null) {
            BookImage cover = imageService.getBookCover(book);
            if (cover != null) {
                imageService.updateImage(importCoverPath, cover);
            } else {
                imageService.addImage(book, importCoverPath, ImageType.COVER);
            }
        }
    }

    public void processImages(Book book, List<String> importPagesPath) {
        if (!importPagesPath.isEmpty() && importPagesPath != null) {
            try {
                List<BookImage> bookPages = imageService.getBookPages(book);
                if (!bookPages.isEmpty() && bookPages != null) {
                    deletePages(bookPages);
                    for (String importPagePath : importPagesPath) {
                        imageService.addImage(book, importPagePath, ImageType.PAGE);
                    }
                }
            } catch (Exception ex) {
                logger.error("Error with files or file path:", ex);
            }
        }
    }


}