package com.openbanking.comon;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BaseExelServiceImpl implements BaseExcelService {
    @PersistenceContext
    private EntityManager entityManager;

    public <T> ResponseEntity<InputStreamResource> exportToExcel(List<T> data, Class<T> clazz , InputStream inputStream) {
        try (Workbook workbook = (inputStream != null ? new XSSFWorkbook(inputStream) : new XSSFWorkbook())) {

            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                sheet = workbook.createSheet("Sheet1");
            }

            int dataStartRow = 1; // Ví dụ: bắt đầu từ hàng 1
            int currentLastRow = sheet.getLastRowNum();
            int dataRowsCount = data.size();

            // Di chuyển các hàng của template xuống dưới để tạo không gian cho dữ liệu mới
            moveTemplateRowsDown(sheet, dataStartRow, dataRowsCount);

            // Thêm dữ liệu vào sheet từ hàng bắt đầu
            addDataToSheet(sheet, data, clazz, dataStartRow);

            // Điều chỉnh vùng merge nếu cần
            adjustMergedRegions(sheet, dataStartRow, dataRowsCount);

            createHeader(sheet, clazz);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            ByteArrayInputStream bis = new ByteArrayInputStream(outputStream.toByteArray());

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=data.xlsx");
            headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(bis));

        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private void moveTemplateRowsDown(Sheet sheet, int startRow, int dataRowsCount) {
        // Di chuyển các hàng của template xuống dưới để tạo không gian cho dữ liệu mới
        int lastRowNum = sheet.getLastRowNum();
        for (int i = lastRowNum; i >= startRow; i--) {
            Row oldRow = sheet.getRow(i);
            if (oldRow != null) {
                Row newRow = sheet.createRow(i + dataRowsCount);
                copyRow(oldRow, newRow);
            }
        }
    }

    private void createHeader(Sheet sheet, Class<?> clazz) {
        // Tạo hàng tiêu đề dựa trên lớp đối tượng
        Row headerRow = sheet.getRow(0); // Giả sử hàng 0 là hàng tiêu đề
        if (headerRow == null) {
            headerRow = sheet.createRow(0);
        }

        // Tạo tiêu đề cho cột STT
        headerRow.createCell(0).setCellValue("STT");

        // Tạo tiêu đề cho các cột dữ liệu
        Field[] fields = clazz.getDeclaredFields();
        int headerCellNum = 1; // Bắt đầu từ cột 1 vì cột 0 đã được sử dụng cho STT
        for (Field field : fields) {
            headerRow.createCell(headerCellNum++).setCellValue(field.getName());
        }
    }

    private void adjustMergedRegions(Sheet sheet, int dataStartRow, int numberOfRowsToMove) {
        int numMergedRegions = sheet.getNumMergedRegions();
        for (int i = 0; i < numMergedRegions; i++) {
            CellRangeAddress oldRange = sheet.getMergedRegion(i);
            if (oldRange.getFirstRow() >= dataStartRow) {
                // Di chuyển các vùng merge xuống
                CellRangeAddress newRange = new CellRangeAddress(
                        oldRange.getFirstRow() + numberOfRowsToMove,
                        oldRange.getLastRow() + numberOfRowsToMove,
                        oldRange.getFirstColumn(),
                        oldRange.getLastColumn()
                );
                sheet.addMergedRegion(newRange);
                sheet.removeMergedRegion(i);
                numMergedRegions--; // Cập nhật số lượng vùng merge
                i--; // Xem xét lại chỉ mục vì vùng merge đã bị xóa
            }
        }
    }

    private void addDataToSheet(Sheet sheet, List<?> data, Class<?> clazz, int startRow) throws IllegalAccessException {
        // Thêm dữ liệu vào sheet từ hàng bắt đầu
        int rowNum = startRow;
        Field[] fields = clazz.getDeclaredFields();

        // Duyệt qua dữ liệu
        for (Object item : data) {
            Row row = sheet.createRow(rowNum++);
            int cellNum = 0;

            // Thêm số thứ tự ở cột đầu tiên
            Cell cellSTT = row.createCell(cellNum++);
            cellSTT.setCellValue(rowNum - startRow); // STT bắt đầu từ 1

            // Thêm dữ liệu của đối tượng
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(item);
                Cell cell = row.createCell(cellNum++);

                if (value instanceof Number) {
                    // Nếu giá trị là số, đặt giá trị ô là số
                    cell.setCellValue(((Number) value).doubleValue());
                } else if (value instanceof Boolean) {
                    // Nếu giá trị là boolean, đặt giá trị ô là boolean
                    cell.setCellValue((Boolean) value);
                } else if (value instanceof Date) {
                    // Nếu giá trị là ngày, định dạng ô dưới dạng ngày
                    cell.setCellValue((Date) value);
                    // Bạn có thể thêm mã để định dạng ngày ở đây nếu cần
                } else {
                    // Nếu không phải số, boolean, hoặc ngày, đặt giá trị ô là chuỗi
                    cell.setCellValue(value != null ? value.toString() : "");
                }
            }
        }
    }

    private void copyRow(Row oldRow, Row newRow) {
        // Sao chép dữ liệu và định dạng từ hàng cũ sang hàng mới
        for (int i = 0; i < oldRow.getLastCellNum(); i++) {
            Cell oldCell = oldRow.getCell(i);
            if (oldCell != null) {
                Cell newCell = newRow.createCell(i);
                newCell.setCellValue(oldCell.toString());
                newCell.setCellStyle(oldCell.getCellStyle());
            }
        }
    }


    @Override
    @Transactional
    public <T> ResponseEntity<String> importFromExcel(MultipartFile file, Class<T> clazz) throws Exception {
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            List<T> entities = new ArrayList<>();
            Field[] fields = clazz.getDeclaredFields();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                T instance = clazz.getDeclaredConstructor().newInstance();
                for (int j = 0; j < fields.length; j++) {
                    Field field = fields[j];
                    field.setAccessible(true);
                    Cell cell = row.getCell(j);

                    if (cell == null) continue;

                    String cellValue = "";
                    switch (cell.getCellType()) {
                        case STRING:
                            cellValue = cell.getStringCellValue();
                            break;
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                cellValue = cell.getDateCellValue().toString();
                            }
                            else {
                                cellValue = String.valueOf(cell.getNumericCellValue());
                            }
                            break;
                        case BOOLEAN:
                            cellValue = String.valueOf(cell.getBooleanCellValue());
                            break;
                        case FORMULA:
                            cellValue = cell.getCellFormula();
                            break;
                        default:
                            cellValue = "";
                    }

                    if (field.getType() == String.class) {
                        field.set(instance, cellValue);
                    } else if (field.getType() == Integer.class || field.getType() == int.class) {
                        try {
                            field.set(instance, Integer.parseInt(cellValue));
                        } catch (NumberFormatException e) {
                            field.set(instance, 0); //
                        }
                    } else if (field.getType() == Double.class || field.getType() == double.class) {
                        try {
                            field.set(instance, Double.parseDouble(cellValue));
                        } catch (NumberFormatException e) {
                            field.set(instance, 0.0);
                        }
                    } else if (field.getType() == Boolean.class || field.getType() == boolean.class) {
                        field.set(instance, Boolean.parseBoolean(cellValue));
                    }
                }
                entities.add(instance);
            }

            for (T entity : entities) {
                entityManager.persist(entity);
            }

            return ResponseEntity.ok("Data imported successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to import data: " + e.getMessage());
        }
    }
}
