package com.fh.service;

import com.fh.entity.po.AreaBean;

import java.util.List;

public interface AreaService {
    List<AreaBean> queryAllAreaList();

    void addArea(AreaBean areaBean);

    AreaBean queryAreaById(Integer id);

    void updateAreaById(AreaBean areaBean);

    void deleteAreaByIds(String[] ids);

    List<AreaBean> queryAreaListExcel(String[] ids);
}
