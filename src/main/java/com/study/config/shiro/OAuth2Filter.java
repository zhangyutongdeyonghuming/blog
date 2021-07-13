package com.study.config.shiro;

import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSON;
import com.study.utils.Result;
import lombok.SneakyThrows;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhangyutong
 * Create by 2021/7/13 21:41
 */
public class OAuth2Filter extends AuthenticatingFilter {

    /**
     * 从请求头获取token, 如果没有则从参数列表获取token
     *
     * @param httpServletRequest {@link HttpServletRequest}
     * @return token
     */
    public static String getToken(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            token = httpServletRequest.getParameter("token");
        }
        return token;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest,
                                     ServletResponse servletResponse) throws Exception {
        // 获取token
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = getToken(request);
        if (StringUtils.isEmpty(token)) {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
            // 如果没有token则返回401
            response.getWriter().write(JSON.toJSONString(Result.newFailure(HttpStatus.HTTP_UNAUTHORIZED, "invalid token")));
            return false;
        }
        return false;
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        //从url中拿到auth code
        String token = getToken(httpRequest);
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        //用auth code创建auth2Token
        return new OAuth2Token(token);
    }

    @SneakyThrows
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.getWriter().write(JSON.toJSONString(Result.newFailure(HttpStatus.HTTP_UNAUTHORIZED, e.getMessage())));
        return false;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return ((HttpServletRequest) request).getMethod().equals(RequestMethod.OPTIONS.name());
    }
}
