package com.example.demo.common.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 上传excel数据到数据库工具类
 */
public class POIExcelUpload {
    // 导入数据库的操作
    public List<Map<String, Object>> uploadExcel(MultipartFile multipartFile, int type, String[] mapKey) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            FileInputStream fileInputStream = (FileInputStream)multipartFile.getInputStream();
            // 创建电子表
            Workbook workbook = this.createWorkBook(fileInputStream, type);
            Sheet sheet = workbook.getSheetAt(0);// excel表的第0个sheet表的数据
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                // 循环一行一行读取数据 ，因此可以将获取到的数据存入到一个map集合中，然后循环插入
                Row row = sheet.getRow(i);
                Map<String, Object> map = new HashMap<>();
                for (int j = 0; j < mapKey.length; j++) {
                    String cellCol = null;
                    if (row.getCell(j) != null) {
                        row.getCell(j).setCellType(Cell.CELL_TYPE_STRING);
                        cellCol = row.getCell(j).getStringCellValue();
                    }
                    map.put(mapKey[j],cellCol);
                }
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 创建电子表格
    private Workbook createWorkBook(FileInputStream fileInputStream, int type) throws Exception {
        if (type == 0) {
            return new HSSFWorkbook(fileInputStream);
        }else{
            return new XSSFWorkbook(fileInputStream);
        }
    }
}
