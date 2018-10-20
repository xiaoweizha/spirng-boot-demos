package com.example.demo.config.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class XssFilter implements Filter {
    FilterConfig fc = null;
    public void destroy() {
        fc = null;
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain fc) throws IOException, ServletException {
        fc.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest)req), resp);
    }

    public void init(FilterConfig fc) {
        this.fc = fc;
    }
}
