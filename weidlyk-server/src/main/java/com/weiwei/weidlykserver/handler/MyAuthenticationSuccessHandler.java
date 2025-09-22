package com.weiwei.weidlykserver.handler;


import com.weiwei.weidlykserver.entity.User;
import com.weiwei.weidlykserver.result.Result;
import com.weiwei.weidlykserver.util.JSONUtils;
//import com.weiwei.weidlykserver.util.JWTUtils;
import com.weiwei.weidlykserver.util.ResponseUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

//    @Resource
//    private RedisService redisService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //登录成功，执行该方法，在该方法中返回json给前端，就行了
        User user = (User) authentication.getPrincipal();

//        //1、生成jwt
//        //把tUser对象转成json作为负载数据放入jwt
//        String userJSON = JSONUtils.toJSON(User);
//        String jwt = JWTUtils.createJWT(userJSON);
//
//        //2、写入redis
//        redisService.setValue(Constants.REDIS_JWT_KEY + User.getId(), jwt);

//        //3、设置jwt的过期时间(如果选择了记住我，过期时间是7天，否则是30分钟)
//        String rememberMe = request.getParameter("rememberMe");
//        if (Boolean.parseBoolean(rememberMe)) {
//            redisService.expire(Constants.REDIS_JWT_KEY + User.getId(), Constants.EXPIRE_TIME, TimeUnit.SECONDS);
//        } else {
//            redisService.expire(Constants.REDIS_JWT_KEY + User.getId(), Constants.DEFAULT_EXPIRE_TIME, TimeUnit.SECONDS);
//        }

        //登录成功的统一结果
        Result<User> result = Result.ok(user);

        //把R对象转成json
        String resultJSON = JSONUtils.toJSON(result);

        //把R以json返回给前端
        ResponseUtils.write(response, resultJSON);
    }
}
