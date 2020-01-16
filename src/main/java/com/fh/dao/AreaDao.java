package com.fh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.entity.po.AreaBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AreaDao extends BaseMapper<AreaBean> {
    AreaBean queryAreaById(Integer id);

    List<AreaBean> queryAreaListExcel(AreaBean areaBean);
}
