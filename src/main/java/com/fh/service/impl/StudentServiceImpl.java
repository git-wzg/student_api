package com.fh.service.impl;

import com.fh.dao.StudentDao;
import com.fh.entity.po.StudentBean;
import com.fh.service.StudentService;
import com.fh.util.PageBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {
        @Resource
    private StudentDao studentDao;

    @Override
    public PageBean<StudentBean> queryStudentList(PageBean<StudentBean> pageBean) {
        long count=studentDao.queryCount();
        List<StudentBean>list=studentDao.queryStudentList(pageBean);
        pageBean.setData(list);
        pageBean.setRecordsFiltered(count);
        pageBean.setRecordsTotal(count);
        return pageBean;
    }

    @Override
    public void addStudent(StudentBean studentBean) {
        studentDao.insert(studentBean);
    }

    @Override
    public StudentBean queryStudentById(Integer id) {
        return studentDao.queryStudentById(id);
    }

    @Override
    public void updateStudentById(StudentBean studentBean) {
        studentDao.updateById(studentBean);
    }

    @Override
    public void updateStudentDelById(StudentBean studentBean) {
        studentDao.updateById(studentBean);
    }

    @Override
    public List<StudentBean> queryStudentListExcel() {
        return studentDao.selectList(null);
    }

}
