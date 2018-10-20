package com.example.demo.tools.controller;

import com.example.demo.common.controller.BaseController;
import com.example.demo.common.dto.BaseResponse;
import com.example.demo.tools.service.ExcelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuyf22
 * on 2017/6/20.
 */
@Api(description = "导入导出excel明细")
@Controller
@RequestMapping("excel")
class ExcelController extends BaseController{
    @Resource
    private ExcelService excelService;

    @ApiOperation(value = "导出积分明细到excel", notes = "导出积分明细到excel")
    @RequestMapping(value = "output",method = RequestMethod.GET)
    public void outputExcel(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @RequestParam int type) {
        List<HashMap<String,Object>> list = excelService.getList();
        String fileName = "导出用户信息明细"+ new SimpleDateFormat("yyyyMMdd").format(System.currentTimeMillis());
        String[] heads = {"姓名","性别","月收入"};
        String[] cols = {"name","sex","income"};
        String[] numerics = {"sex","income"};
        excelService.outputExcel(type,httpServletRequest,httpServletResponse,list,fileName,heads,cols,numerics);
    }

    @ApiOperation(value = "导入积分明细", notes = "导入积分明细")
    @RequestMapping(value = "upload",method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse<List<Map<String, Object>>> uploadExcel(@RequestParam MultipartFile multipartFile, @RequestParam int type) {
        String[] mapKey = {"name","sex","income"};
        List<Map<String, Object>> list = excelService.uploadExcel(multipartFile,mapKey,type);
        BaseResponse<List<Map<String, Object>>> baseResponse = new BaseResponse<>();
        if(list.size() > 0){
            baseResponse.setCode(0);
            baseResponse.setMsg("请求数据成功");
            baseResponse.setData(list);
        }else{
            baseResponse.setCode(-3);
            baseResponse.setMsg("请求数据为空");
        }
        return baseResponse;
    }

}
