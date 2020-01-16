package com.fh.service;

import com.fh.entity.po.VipBean;
import com.fh.util.response.ResponseServer;

public interface VipService {
    /**
     * 登录
     */
    ResponseServer login(VipBean vipBean);

    /**
     * 验证手机号是否注册
     */
    ResponseServer queryPhone(VipBean vipBean);

    /**
     * 发送验证码
     */
    ResponseServer faSongYan(String phone);

    /**
     * 注册
     */
    ResponseServer addPhone(VipBean vipBean);
}
