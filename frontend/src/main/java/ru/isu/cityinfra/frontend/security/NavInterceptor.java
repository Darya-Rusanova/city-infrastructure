package ru.isu.cityinfra.frontend.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Передает в шаблоны адрес текущей страницы, чтобы подсвечивать активный пункт меню.
 */
public class NavInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            String view = modelAndView.getViewName();
            if (view != null && !view.startsWith("redirect:")) {
                modelAndView.addObject("currentPath", request.getRequestURI());
            }
        }
    }
}
