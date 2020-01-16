package com.fh.controller;

import com.fh.common.annotaction.IsNoLogin;
import com.fh.entity.po.AreaBean;
import com.fh.service.AreaService;
import com.fh.util.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Controller
@RequestMapping("area")
@CrossOrigin(origins = "http://localhost:8888")
public class AreaController {
    @Autowired
    private AreaService areaService;
    /*查询所有数据*/
    @RequestMapping("queryAllAreaList")
    @ResponseBody
    @IsNoLogin
    public List<AreaBean> queryAllAreaList(){
        List<AreaBean>list=areaService.queryAllAreaList();
        return list;
    }
    /*新增地区*/
    @RequestMapping("addArea")
    @ResponseBody
    @IsNoLogin
    public Map addArea(AreaBean areaBean){
        areaService.addArea(areaBean);
        Map map=new HashMap();
        map.put("code","200");
        map.put("message","新增成功");
        return map;
    }
    /*回显地区*/
    @RequestMapping("queryAreaById")
    @ResponseBody
    @IsNoLogin
    public AreaBean queryAreaById(Integer id){
            AreaBean areaBean=areaService.queryAreaById(id);
            return areaBean;
    }

    /*修改地区*/
    @RequestMapping("updateAreaById")
    @ResponseBody
    @IsNoLogin
    public Map updateAreaById(AreaBean areaBean){
        areaService.updateAreaById(areaBean);
        Map map=new HashMap();
        map.put("code","200");
        map.put("message","修改成功");
        return map;
    }
    /*删除地区*/
    @RequestMapping("deleteAreaByIds")
    @ResponseBody
    @IsNoLogin
    public Map deleteAreaByIds(String[] ids){
        areaService.deleteAreaByIds(ids);
        Map map=new HashMap();
        map.put("code","200");
        map.put("message","删除成功");
        return map;
    }
        /*导出Excel*/
    @RequestMapping("getExcel")
    @IsNoLogin
    public void getExcel(HttpServletResponse response,String[] ids){
        /*获取数据*/
        List<AreaBean>list=areaService.queryAreaListExcel(ids);
        ExcelUtils.execelUtil(list,response);
    }

}
