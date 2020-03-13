package com.shridhar.auth.filter;

import static com.shridhar.auth.constants.Constants.DEBUG;
import static com.shridhar.auth.constants.Constants.X_DEBUG;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class HeaderInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeaderInterceptor.class);

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) {

        String debug = request.getHeader(X_DEBUG);

        if (debug != null && debug.equals("true")) {
            MDC.put(DEBUG, "true");
            LOGGER.debug("Debug header found");
        }

        return true;
    }

    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView) {
        MDC.remove(DEBUG);
    }
}
