package ru.ecosystem.dreamjob.app.filter;

import org.springframework.stereotype.Component;
import ru.ecosystem.dreamjob.app.util.DreamJobUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class AuthFilter implements Filter {


    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        var request = (HttpServletRequest) servletRequest;
        var response = (HttpServletResponse) servletResponse;

        String uri = request.getRequestURI();
        if (uri.endsWith("/register/admin") || uri.endsWith("/login")) {
            filterChain.doFilter(request, response);
            return;
        }
        if (Objects.isNull(request.getSession().getAttribute(DreamJobUtils.USER_ID_KEY))) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            filterChain.doFilter(request, response);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
