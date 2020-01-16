package com.fh.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.fh.common.constant.ConstantData;
import com.fh.dao.AreaDao;
import com.fh.entity.po.AreaBean;
import com.fh.service.AreaService;
import com.fh.util.RedisPools;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class AreaServiceImpl implements AreaService {
    @Resource
    private AreaDao areaDao;

    @Override
    public List<AreaBean> queryAllAreaList() {
        List<AreaBean> areaBeanList=new ArrayList<AreaBean>();
                /*获取redis操作对象*/
        Jedis redis = RedisPools.getredis();
//              /*根据地区的key去redis里面查看有没有数据*/
        String area = redis.get(ConstantData.areakey);
        if (area!="" && area!=null){
            /*如果不为空（有数据）就将字符串转为对象返回回去，不用和数据库交互*/
            areaBeanList = JSONArray.parseArray(area, AreaBean.class);
        }else {
            areaBeanList = areaDao.selectList(null);
                redis.set(ConstantData.areakey,JSONArray.toJSONString(areaBeanList));
        }
        /*将redis还回去，释放资源*/
        RedisPools.returnjedis(redis);
        return areaBeanList;
    }

    @Override
    public void addArea(AreaBean areaBean) {
        Jedis getredis = RedisPools.getredis();
         areaDao.insert(areaBean);
        List<AreaBean> areaBeanList = areaDao.selectList(null);
        getredis.set(ConstantData.areakey,JSONArray.toJSONString(areaBeanList));
        RedisPools.returnjedis(getredis);
    }

    @Override
    public AreaBean queryAreaById(Integer id) {
        return areaDao.queryAreaById(id);
    }

    @Override
    public void updateAreaById(AreaBean areaBean) {
        Jedis getredis = RedisPools.getredis();
        areaDao.updateById(areaBean);
        List<AreaBean> areaBeanList = areaDao.selectList(null);
        getredis.set(ConstantData.areakey,JSONArray.toJSONString(areaBeanList));
        RedisPools.returnjedis(getredis);
    }

    @Override
    public void deleteAreaByIds(String[] ids) {
        Jedis getredis = RedisPools.getredis();
        areaDao.deleteBatchIds(Arrays.asList(ids));
        List<AreaBean> areaBeanList = areaDao.selectList(null);
        getredis.set(ConstantData.areakey,JSONArray.toJSONString(areaBeanList));
        RedisPools.returnjedis(getredis);
    }

    @Override
    public List<AreaBean> queryAreaListExcel(String[] ids) {
        if (ids!=null && !ids.equals("")) {
            return areaDao.selectBatchIds(Arrays.asList(ids));
        }else {
            return areaDao.selectList(null);
        }
        }

}
