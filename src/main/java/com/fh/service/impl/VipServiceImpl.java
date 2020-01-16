package com.fh.service.impl;

import com.fh.dao.VipDao;
import com.fh.entity.po.VipBean;
import com.fh.service.VipService;
import com.fh.util.JWT;
import com.fh.util.RedisPools;
import com.fh.util.imgUtil.Rondom;
import com.fh.util.imgUtil.SendSms;
import com.fh.util.response.ResponseServer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
@Transactional
public class VipServiceImpl implements VipService {

      @Resource
    private VipDao vipDao;
    /**
     * 登录
     */
    @Override
    public ResponseServer login(VipBean vipBean) {
        Map map = new HashMap();
        //先查询用户信息做校验
        VipBean vipInfo  = vipDao.queryPhone(vipBean);
        //判断手机号是否注册
        if(vipInfo!=null){
            //不等于代表手机号已注册
            //将用户信息 通过jwt生产密钥
            String sign = JWT.sign(vipInfo, 1000 * 60 * 60 * 24);
            //将token和用户的唯一标识存入redis中
            Jedis jeDis = RedisPools.getredis();
            jeDis.set(vipInfo.getPhone(),sign);
            //设置失效时间
            jeDis.expire(vipInfo.getPhone(),60*30*2);
            //将token返回前台
            String token = vipInfo.getPhone() + "," + sign;
            //将token进行加密
            byte[] encode = Base64.getEncoder().encode(token.getBytes());
            String encodeToken = new String(encode);
            map.put("token",encodeToken);
            //返还jedis
            RedisPools.returnjedis(jeDis);
        }else {
            //如果等于null证明该手机号没有注册
            map.put("code",1);
            map.put("msg","手机号未注册");
            return ResponseServer.error(map);
        }
        return ResponseServer.success(map);
    }

    /**
     * 验证手机号是否注册
     */
    @Override
    public ResponseServer queryPhone(VipBean vipBean) {
        boolean a = false;
        //查询手机号是否注册
        VipBean vipInfo  = vipDao.queryPhone(vipBean);
        if(vipInfo!=null){
            a=false;
        }else if(vipInfo==null){
            a=true;
        }
        return ResponseServer.success(a);
    }

    /**
     * 发送验证码
     */
    @Override
    public ResponseServer faSongYan(String phone) {
        //生成随机验证码
        String random = Rondom.getRandom(5);
        boolean b = SendSms.sendSms(phone, random);
        return ResponseServer.success(b);
    }

    /**
     * 注册
     */
    @Override
    public ResponseServer addPhone(VipBean vipBean) {
        Jedis jeDis = RedisPools.getredis();
        String s = jeDis.get("msg_"+vipBean.getPhone());
        if(s!=null){
            if(s.equals(vipBean.getCode())){//判断前台验证码与后台发送的验证码是否相同
                //相同的话就注册
                vipBean.setCreateDate(new Date());
                vipBean.setIsLock(1);
                vipDao.addVipBean(vipBean);
            }
        }
        return ResponseServer.success();
    }
}
