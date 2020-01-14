package com.fh.controller;

import com.fh.entity.po.StudentBean;
import com.fh.service.StudentService;
import com.fh.util.AliyunOssUtils;
import com.fh.util.ExcelUtils;
import com.fh.util.PageBean;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "http://localhost:8888")
@Controller
@RequestMapping("student")
public class StudentController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private HttpServletRequest request;
    private final static Logger LOGGER = LoggerFactory.getLogger(StudentController.class);
    /*分页查询*/
    @RequestMapping("queryStudentList")
    @ResponseBody
    public PageBean<StudentBean> queryStudentList(PageBean<StudentBean> pageBean){
        pageBean=studentService.queryStudentList(pageBean);

        LOGGER.info("用户试试水执行了登录操作");
        return pageBean;
    }
    /*新增*/
    @RequestMapping("addStudent")
    @ResponseBody
    public Map addStudent(StudentBean studentBean){
        /*处理年龄*/
        int year = studentBean.getBirthday().getYear();
        Date date = new Date();
        int year1 = date.getYear();
        Integer age=year-year1;
        studentBean.setAge(age);
        studentBean.setIsDel(1);
        studentService.addStudent(studentBean);
        Map map=new HashMap();
        map.put("code","200");
        map.put("message","新增成功");
        return map;
    }

    /*回显*/
    @RequestMapping("queryStudentById")
    @ResponseBody
    public StudentBean queryStudentById(Integer id){
        StudentBean studentBean=studentService.queryStudentById(id);
        return studentBean;
    }

    /*修改*/
    @RequestMapping("updateStudentById")
    @ResponseBody
    public Map updateStudentById(StudentBean studentBean){
        /*处理年龄*/
        int year = studentBean.getBirthday().getYear();
        Date date = new Date();
        int year1 = date.getYear();
        Integer age=year-year1;
        studentBean.setAge(age);
        studentService.updateStudentById(studentBean);
        Map map=new HashMap();
        map.put("code","200");
        map.put("message","修改成功");
        return map;
    }

    /*删除*/
    @RequestMapping("deleteStudentById")
    @ResponseBody
    public Map deleteStudentById(Integer id){
        StudentBean studentBean=studentService.queryStudentById(id);
        studentBean.setIsDel(2);
        studentService.updateStudentDelById(studentBean);
        Map map=new HashMap();
        map.put("code",200);
        map.put("message","删除成功");
        return map;
    }

    /*上传图片*/
    @RequestMapping("uploadImg")
    @ResponseBody
    public Map uploadImg(@RequestParam("img") MultipartFile img){
        Map map=new HashMap();
        AliyunOssUtils aliyunOssUtils=new AliyunOssUtils();

        try {
            /*图片重命名*/
            String imgName = aliyunOssUtils.uploadImg2Oss(img);
            /*拼接图片路径*/
            String imageUrl = aliyunOssUtils.getImageUrl(imgName);
            map.put("data",imageUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    };

    // 导出excel
    @RequestMapping("getExcel")
    public void getExcel(HttpServletResponse response, XSSFWorkbook book){
        /*获取要导出的数据*/
        List<StudentBean> list=studentService.queryStudentListExcel();

        /*调用工具类，将要导出的数据传过去*/
        ExcelUtils.execelUtil(list,response);

    }
}
