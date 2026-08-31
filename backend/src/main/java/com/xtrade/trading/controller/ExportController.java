package com.xtrade.trading.controller;

import com.xtrade.trading.service.ExportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Endpoints REST pour l'export de l'historique des transactions
 * vers un fichier CSV ou Excel.
 */
@RestController
@RequestMapping("/api/export")
public class ExportController {

    private final ExportService exportService;

    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }

    @GetMapping("/csv")
    public void exportCsv(HttpServletResponse response,
                          @RequestParam(required = false) Long traderId) throws IOException {
        exportService.exportCsv(response, traderId);
    }

    @GetMapping("/excel")
    public void exportExcel(HttpServletResponse response,
                            @RequestParam(required = false) Long traderId) throws IOException {
        exportService.exportExcel(response, traderId);
    }
}
