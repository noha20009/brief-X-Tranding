package com.xtrade.trading.service;

import com.xtrade.trading.model.Transaction;
import com.xtrade.trading.repository.TransactionRepository;
import com.opencsv.CSVWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Export de l'historique des transactions vers un fichier CSV ou Excel,
 * déclenché depuis l'API (et le menu du frontend).
 */
@Service
public class ExportService {

    private final TransactionRepository transactionRepository;

    public ExportService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    private static final String[] HEADERS = {
            "ID", "Type", "Trader", "Code actif", "Nom actif", "Quantité", "Prix unitaire", "Montant total", "Date"
    };

    public void exportCsv(HttpServletResponse response, Long traderId) throws IOException {
        List<Transaction> txs = resolveTransactions(traderId);
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"transactions.csv\"");

        CSVWriter writer = new CSVWriter(new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8),
                CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, "\n");
        writer.writeNext(HEADERS);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Transaction t : txs) {
            writer.writeNext(new String[]{
                    String.valueOf(t.getId()),
                    t.getType().name(),
                    t.getTrader().getNom(),
                    t.getAsset().getCode(),
                    t.getAsset().getNom(),
                    String.valueOf(t.getQuantite()),
                    String.format("%.2f", t.getPrixUnitaire()),
                    String.format("%.2f", t.getMontantTotal()),
                    sdf.format(java.sql.Timestamp.valueOf(t.getDate()))
            });
        }
        writer.flush();
        writer.close();
    }

    public void exportExcel(HttpServletResponse response, Long traderId) throws IOException {
        List<Transaction> txs = resolveTransactions(traderId);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"transactions.xlsx\"");

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Transactions");

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < HEADERS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(HEADERS[i]);
                cell.setCellStyle(headerStyle);
                sheet.autoSizeColumn(i);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int rowIdx = 1;
            for (Transaction t : txs) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(t.getId());
                row.createCell(1).setCellValue(t.getType().name());
                row.createCell(2).setCellValue(t.getTrader().getNom());
                row.createCell(3).setCellValue(t.getAsset().getCode());
                row.createCell(4).setCellValue(t.getAsset().getNom());
                row.createCell(5).setCellValue(t.getQuantite());
                row.createCell(6).setCellValue(t.getPrixUnitaire());
                row.createCell(7).setCellValue(t.getMontantTotal());
                row.createCell(8).setCellValue(sdf.format(java.sql.Timestamp.valueOf(t.getDate())));
            }

            workbook.write(response.getOutputStream());
        }
    }

    private List<Transaction> resolveTransactions(Long traderId) {
        if (traderId != null) {
            return transactionRepository.findByTraderIdOrderByDateAsc(traderId);
        }
        return transactionRepository.findAllByOrderByDateAsc();
    }
}
