package com.fh.controller;

import com.fh.entity.po.VipBean;
import com.fh.service.VipService;
import com.fh.util.response.ResponseServer;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("login")
@CrossOrigin(origins = "http://localhost:8888")
public class VipController {

    @Resource
    private VipService vipService;
    /**
     * 登录
     */
    @RequestMapping("login")
    public ResponseServer login(VipBean vipBean){
        ResponseServer responseServer = vipService.login(vipBean);
        return  responseServer;
    }


    /**
     * 验证
     */
    @RequestMapping("queryLogin")
    public ResponseServer queryLogin(HttpServletRequest request){
        VipBean vipBean = (VipBean) request.getAttribute("vipBean");
        return ResponseServer.success(vipBean);
    }


    /**
     * 验证手机号是否已经注册
     */
    @RequestMapping("queryPhone")
    public ResponseServer queryPhone (VipBean vipBean){
        ResponseServer responseServer = vipService.queryPhone(vipBean);
        return responseServer;
    }

    /**
     * 发送验证码
     */
    @RequestMapping("faSongYan")
    public ResponseServer faSongYan(String phone){
        ResponseServer responseServer = vipService.faSongYan(phone);
        return responseServer;
    }


    /**
     * 注册
     */
    @RequestMapping("addPhone")
    public ResponseServer addPhone(VipBean vipBean){
        ResponseServer responseServer = vipService.addPhone(vipBean);
        return responseServer;
    }
}
