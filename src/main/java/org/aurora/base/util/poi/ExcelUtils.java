package org.aurora.base.util.poi;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ExcelUtils {

    public static XSSFWorkbook create(MultipartFile excelFile) {
        try {
            return new XSSFWorkbook(excelFile.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getCellValue(Cell cell) {
        return switch (cell.getCellType()) {
            case STRING -> cell.getRichStringCellValue().getString();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getDateCellValue();
                }
                yield cell.getNumericCellValue();
            }
            case BOOLEAN -> cell.getBooleanCellValue();
            case FORMULA -> cell.getCellFormula();
            // case BLANK -> null;
            default -> null;
        };
    }

    /**
     * 将文件写入输出流
     */
    public static void write(XSSFWorkbook workbook, ByteArrayOutputStream stream) {
        try {
            workbook.write(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 标题行样式
     */
    public static XSSFCellStyle getTitleStyle(XSSFWorkbook workbook) {
        XSSFCellStyle titleStyle = getDefaultStyle(workbook);
        // 设置背景色
        titleStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // 设置字体
        Font titleFont = getDefaultFont(workbook);
        titleFont.setBold(true);
        titleStyle.setFont(titleFont);
        return titleStyle;
    }

    /**
     * 内容样式 (字符串)
     */
    public static XSSFCellStyle getStringStyle(XSSFWorkbook workbook) {
        XSSFCellStyle style = getDefaultStyle(workbook);
        style.setFont(getDefaultFont(workbook));
        return style;
    }

    /**
     * 内容样式 (日期时间)
     */
    public static XSSFCellStyle getDateTimeStyle(XSSFWorkbook workbook) {
        XSSFCellStyle style = getDefaultStyle(workbook);
        XSSFCreationHelper createHelper = workbook.getCreationHelper();
        style.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));
        style.setFont(getDefaultFont(workbook));
        return style;
    }

    /**
     * 默认样式
     */
    public static XSSFCellStyle getDefaultStyle(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        // 水平对齐方式
        style.setAlignment(HorizontalAlignment.CENTER);
        // 垂直对齐方式
        style.setVerticalAlignment(VerticalAlignment.BOTTOM);
        // 边框的样式
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        return style;
    }

    /**
     * 默认字体
     */
    public static Font getDefaultFont(XSSFWorkbook workbook) {
        Font font = workbook.createFont();
        font.setFontName("Microsoft YaHei");
        font.setFontHeightInPoints((short) 10);
        return font;
    }
}
