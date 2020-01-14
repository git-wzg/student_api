package com.fh.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fh.common.annotaction.ExcelFild;
import com.fh.common.annotaction.ExcleHeard;
import com.fh.util.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@TableName("t_student")
@ExcleHeard(title = "学生信息")
public class StudentBean {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @Excel(name="学生姓名",value = "name")
    @ExcelFild(name = "姓名")
    private String name;
    @Excel(name="学生年龄",value = "age")
    @ExcelFild(name = "年龄")
    private Integer age;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name="学生生日",value = "birthday")
    @ExcelFild(name ="生日")
    private Date birthday;
    @Excel(name="地址",value = "address")
    @ExcelFild(name = "家庭住址")
    private String address;
    private String imgPath;
    private Integer isDel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }
}
