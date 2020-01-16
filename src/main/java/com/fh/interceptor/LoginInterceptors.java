package com.fh.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.fh.common.annotaction.IsNoLogin;
import com.fh.entity.po.VipBean;
import com.fh.util.JWT;
import com.fh.util.RedisPools;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Base64;

@Component//把当前类交给IOC
public class LoginInterceptors implements HandlerInterceptor{
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //因为客户端把token放到header里面 所以会发送两次请求， 第一次确定是否接受你的请求，options
        String method = request.getMethod();
        //判断是否为options请求
        if(method.equalsIgnoreCase("options")){
            //如果是的话允许改头信息
            //是的话  允许向hearder 放变量 token
            response.addHeader("Access-Control-Allow-Headers", "token");
            String origin = request.getHeader("Origin");//获取请求的域名信息
            //设置允许跨域
            response.setHeader("Access-Control-Allow-Origin",origin);
            return false;//下次过来就是真正的请求
        }
        //header controller层的请求代理方法的对象
        HandlerMethod hm=(HandlerMethod) handler;
        //获取要访问的方法
        Method hmMethod = hm.getMethod();
        boolean annotationPresent = hmMethod.isAnnotationPresent(IsNoLogin.class);
        if(annotationPresent==true){//有注解需要登录
            //带了token过来，通过了第一次请求
            String token = request.getHeader("token");
            //解密
            byte[] decode = Base64.getDecoder().decode(token);
            String  tokeStr = new String(decode);
            String[] split = tokeStr.split(",");
            if(split.length!=2){
                //token
                throw new Exception("非法请求");
            }

            String phone = split[0];
            String jwtToken = split[1];

            //从redis中取出token
            Jedis jeDis = RedisPools.getredis();
            String redToken = jeDis.get(phone);
            if(redToken!=null){ //验证是否超时
                if(jwtToken.equals(redToken)){//验证是否最新
                    //token正确，将用户信息取出来放入request中，方便以后代码使用
                    VipBean vipBean = JWT.unsign(jwtToken, VipBean.class);
                    request.setAttribute("vipBean",vipBean);
                    //放入redis里让其他项目使用
                    jeDis.set("vipBean", JSONObject.toJSONString(vipBean));
                    //重新设置session时间
                    jeDis.expire(vipBean.getPhone(),60*30*2);
                    RedisPools.returnjedis(jeDis);
                    return true;
                }
            }else {
                throw new Exception("登录超时");
            }

        }
        //没有注解直接放行
        return true;
    }
}
