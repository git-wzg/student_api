package com.fh.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fh.common.annotaction.ExcelFild;
import com.fh.common.annotaction.ExcleHeard;

@TableName("area_data")
@ExcleHeard(title = "地区信息")
public class AreaBean {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @ExcelFild(name = "地区名")
    @TableField("name")
    private String name;
    @TableField("pid")
    private Integer pid;

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

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }
}
