package com.example.demo.common.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 导出excel工具类
 */
public class PoiExcelExport {
    // excel导出名称
    private String fileName;
    // excel 表头
    private String[] heads;
    // excel 列
    private String[] cols;
    // 设置数值型的列
    private String[] numerics;
    //list集合
    private List<HashMap<String,Object>> list;
    // 输出流
    private OutputStream out;
    // 构造函数 全是字符串类型
    public PoiExcelExport(String fileName, String[] heads, String[] cols, List<HashMap<String,Object>> list, OutputStream out) {
        this.fileName = fileName;
        this.heads = heads;
        this.cols = cols;
        this.list = list;
        this.out = out;
    }

    // 构造函数 带数字类型
    public PoiExcelExport(String fileName, String[] heads, String[] cols, List<HashMap<String,Object>> list, String[] numerics, OutputStream out) {
        this.fileName = fileName;
        this.heads = heads;
        this.cols = cols;
        this.list = list;
        this.numerics = numerics;
        this.out = out;
    }

    public void exportExcelExtend() {
        XSSFWorkbook xssfworkbook = new XSSFWorkbook();
        for (int i = 0; i <= (list.size() / 65535); i++) {
            XSSFSheet xssfsheet = xssfworkbook.createSheet(); // 工作表
            // 工作表名称
            xssfworkbook.setSheetName(i, fileName.replace(".xlsx", "") + "(第" + (i + 1) + "页)");
            int beginRows = 65535 * i;
            int endRows = (list.size() > 65535 * (i + 1)) ? 65535 * (i + 1) - 1 : list.size() - 1;
            XSSFRow xssfrowHead = xssfsheet.createRow(0);
            // 输出excel 表头
            if (heads != null && heads.length > 0) {
                for (int h = 0; h < heads.length; h++) {
                    XSSFCell xssfcell = xssfrowHead.createCell(h, Cell.CELL_TYPE_STRING);
                    xssfcell.setCellValue(heads[h]);
                }
            }
            // 输出excel 数据
            // 要设置数值型 列表
            XSSFCellStyle cellstyle = xssfworkbook.createCellStyle();
            cellstyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("##0"));
            // 是否是数值型
            boolean isNum;
            for (int curRow = beginRows; curRow <= endRows; curRow++) {
                // 获取数据
                Map<String,Object> hm = list.get(curRow);
                // 创建excel行 表头1行 导致数据行数 延后一行
                XSSFRow xssfrow = xssfsheet.createRow(curRow % 65535 + 1);
                // 读取数据值
                for (int k = 0; k < cols.length; k++) {
                    XSSFCell xssfcell = xssfrow.createCell(k);
                    // hssfcell.setCellValue(hm.get(cols[k])==null?"":hm.get(cols[k]).toString());
                    isNum = false;
                    if(numerics != null){
                        for (int z = 0; z < numerics.length; z++) {
                            if (numerics[z].equals(cols[k])) {
                                isNum = true;
                                break;
                            }
                        }
                    }

                    if(hm.get(cols[k]) != null && StringUtils.hasText(hm.get(cols[k]).toString())){
                        if(isNum){
                            xssfcell.setCellStyle(cellstyle);
                            xssfcell.setCellValue(Double.parseDouble(hm.get(cols[k]).toString().replace(",", "")));
                        }else{
                            xssfcell.setCellValue(hm.get(cols[k]).toString());
                        }
                    }
                }
            }
        }
        // excel生成完毕
        this.workbookWrite(xssfworkbook);
    }

    public void exportExcel() {
        HSSFWorkbook hssfworkbook = new HSSFWorkbook(); // 创建一个excel对象
        for (int i = 0; i <= (list.size() / 65535); i++) {
            HSSFSheet hssfsheet = hssfworkbook.createSheet(); // 工作表
            // 工作表名称
            hssfworkbook.setSheetName(i, fileName.replace(".xls", "") + "(第" + (i + 1) + "页)");
            int beginRows = 65535 * i;
            int endRows = (list.size() > 65535 * (i + 1)) ? 65535 * (i + 1) - 1 : list.size() - 1;
            HSSFRow hssfrowHead = hssfsheet.createRow(0);
            // 输出excel 表头
            if (heads != null && heads.length > 0) {
                for (int h = 0; h < heads.length; h++) {
                    HSSFCell hssfcell = hssfrowHead.createCell(h, Cell.CELL_TYPE_STRING);
                    hssfcell.setCellValue(heads[h]);
                }
            }
            // 要设置数值型 列表
            HSSFCellStyle cellstyle = hssfworkbook.createCellStyle();
            cellstyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("##0"));
            // 是否是数值型
            boolean isNum;
            // 输出excel 数据
            for (int curRow = beginRows; curRow <= endRows; curRow++) {
                // 获取数据
                Map<String,Object> hm = list.get(curRow);
                // 创建excel行 表头1行 导致数据行数 延后一行
                HSSFRow hssfrow = hssfsheet.createRow(curRow % 65535 + 1);
                // 读取数据值
                for (int k = 0; k < cols.length; k++) {
                    HSSFCell hssfcell = hssfrow.createCell(k);
                    // hssfcell.setCellValue(hm.get(cols[k])==null?"":hm.get(cols[k]).toString());
                    isNum = false;
                    if(numerics != null){
                        for (int z = 0; z < numerics.length; z++) {
                            if (numerics[z].equals(cols[k])) {
                                isNum = true;
                                break;
                            }
                        }
                    }
                    boolean bool = hm.get(cols[k]) != null && StringUtils.hasText(hm.get(cols[k]).toString());
                    if(bool){
                        if(isNum){
                            hssfcell.setCellStyle(cellstyle);
                            hssfcell.setCellValue(Double.parseDouble(hm.get(cols[k]).toString().replace(",", "")));
                        }else{
                            hssfcell.setCellValue(hm.get(cols[k]).toString());
                        }
                    }
                }
            }
        }
        this.workbookWrite(hssfworkbook);
    }

    private void workbookWrite(Workbook workbook){
        // excel生成完毕
        try {
            workbook.write(out);
            out.flush();
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
