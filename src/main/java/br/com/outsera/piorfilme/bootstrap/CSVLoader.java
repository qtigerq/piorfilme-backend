package br.com.outsera.piorfilme.bootstrap;

import br.com.outsera.piorfilme.service.CSVImportService;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class CSVLoader {

    @Autowired
    private CSVImportService csvImportService;

    @Value("classpath:Movielist.csv")
    private Resource csvFile;

    @PostConstruct
    public void loadCSVOnInit() {
        this.csvImportService.CSVImport(csvFile);
    }
}