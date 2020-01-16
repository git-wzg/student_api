package com.fh.dao;

import com.fh.entity.po.VipBean;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VipDao  {
    VipBean queryPhone(VipBean vipBean);


    void addVipBean(VipBean vipBean);
}
