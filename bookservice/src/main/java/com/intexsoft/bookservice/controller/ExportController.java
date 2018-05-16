package com.intexsoft.bookservice.controller;

import com.intexsoft.bookservice.api.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ExportController {

    @Autowired
    private ExportService exportService;

    @GetMapping(path = "/export")
    public String exportBooks() {
        return exportService.exportToDb();
    }
}
