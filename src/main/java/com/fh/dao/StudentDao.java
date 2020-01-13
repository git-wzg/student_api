package com.fh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.entity.po.StudentBean;
import com.fh.util.PageBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StudentDao extends BaseMapper<StudentBean> {
    long queryCount();

    List<StudentBean> queryStudentList(PageBean<StudentBean> pageBean);

    StudentBean queryStudentById(Integer id);
}
