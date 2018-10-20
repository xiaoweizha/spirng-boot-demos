package com.example.demo.tools.service.impl;

import com.example.demo.common.util.POIExcelUpload;
import com.example.demo.common.util.PoiExcelExport;
import com.example.demo.tools.service.ExcelService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Override
    public List<HashMap<String,Object>> getList() {
        int size = 100;
        List<HashMap<String,Object>> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            HashMap<String,Object> map = new HashMap<>();
            map.put("name","用户" + i);
            map.put("sex",new Random().nextInt(2));
            map.put("income",i*1000);
            list.add(map);
        }
        return list;
    }

    @Override
    public void outputExcel(int type, HttpServletRequest req, HttpServletResponse resp, List<HashMap<String,Object>> list,
               String fileName, String[] heads, String[] cols, String[] numerics) {

        resp.setContentType("application/vnd.ms-excel");
        String contentDisposition = null;
        try {
            if (req.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
                contentDisposition = "attachment; filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO8859-1")
                        + "\"";// firefox浏览器
            } else {
                contentDisposition = "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"";// IE浏览器
            }
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        resp.setHeader("Content-Disposition", contentDisposition);
        resp.setCharacterEncoding("UTF-8");

        OutputStream out = null;
        try {
            out = resp.getOutputStream();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        PoiExcelExport pee = new PoiExcelExport(fileName, heads, cols, list, numerics, out);
        if(type == 0){
            pee.exportExcel();
        }else{
            pee.exportExcelExtend();
        }
    }

    public List<Map<String, Object>> uploadExcel(MultipartFile multipartFile, String[] mapKey, int type) {
        return new POIExcelUpload().uploadExcel(multipartFile,type,mapKey);
    }

}
