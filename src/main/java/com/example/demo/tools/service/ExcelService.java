package com.example.demo.tools.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 导入导出excel
 */
public interface ExcelService {
    /**
     * 添加测试数据，用来写入excel文件
     *
     * @return list
     */
    List<HashMap<String,Object>> getList();

    /**
     * 根据list集合，导出excel文件
     *
     * @param type 0对应.xls文件，1对应.xlsx文件
     * @param req 请求
     * @param resp 响应
     * @param list 可以是数据库查询处理啊的List<HashMap<String,Object>>
     * @param fileName 导出excel的文件名称
     * @param heads excel的第一行--表头名称
     * @param cols 字段名称--hashmap的key
     * @param numerics 数字类型的字段的名称
     */
    void outputExcel(int type, HttpServletRequest req, HttpServletResponse resp, List<HashMap<String,Object>> list,
                     String fileName, String[] heads, String[] cols, String[] numerics);

    /**
     * 读取excel文件，放入list集合，可用于导入到数据库的操作
     *
     * @param multipartFile excel 文件
     * @param mapKey hashmap的key值数组
     * @param type 0对应.xls文件，1对应.xlsx文件
     * @return 列表，可用于存入数据库
     */
    List<Map<String, Object>> uploadExcel(MultipartFile multipartFile, String[] mapKey, int type);
}
