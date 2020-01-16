package com.fh.service.impl;

import com.fh.dao.AreaDao;
import com.fh.dao.StudentDao;
import com.fh.entity.po.AreaBean;
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
    @Resource
    private AreaDao areaDao;
    @Override
    public PageBean<StudentBean> queryStudentList(PageBean<StudentBean> pageBean) {
        long count=studentDao.queryCount();
        List<StudentBean>list=studentDao.queryStudentList(pageBean);
        for (int i = 0; i <list.size() ; i++) {
            /*取出单个对象的地区id串*/
            String areaId = list.get(i).getAddress();
            /*将地区id串按逗号分割成数组*/
            String[] areaIds = areaId.split(",");
            /*定义一个空字符串（用来拼接三级地区）*/
            String a="";
            /*for循环每个地区id*/
            for (int j = 0; j <areaIds.length ; j++) {
                /*取出单个地区id，查询相对应的地区名称*/          /*将string类型的地区id转换为integer类型(方便查询)*/
                AreaBean areaBean=areaDao.queryAreaById(Integer.valueOf(areaIds[j]));
                a+=areaBean.getName()+"->";
            }
            /*将拼接成的地区字符串放到areaStr当中*/
            a=a.substring(0,a.length()-2);
            list.get(i).setAddressStr(a);
        }

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
