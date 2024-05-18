package org.aurora.base.util.poi;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ExcelUtils {

    /**
     * 将文件写入输出流
     */
    public static void write(XSSFWorkbook workbook, ByteArrayOutputStream stream) {
        try (workbook) {
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
        Font titleFont = workbook.createFont();
        titleFont.setFontName("Microsoft YaHei");
        titleFont.setFontHeightInPoints((short) 10);
        titleFont.setBold(true);
        titleStyle.setFont(titleFont);
        return titleStyle;
    }

    /**
     * 内容样式
     */
    public static XSSFCellStyle getRowStyle(XSSFWorkbook workbook) {
        XSSFCellStyle rowStyle = getDefaultStyle(workbook);
        // 设置字体
        Font rowFont = workbook.createFont();
        rowFont.setFontName("Microsoft YaHei");
        rowFont.setFontHeightInPoints((short) 10);
        rowStyle.setFont(rowFont);
        return rowStyle;
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
}
