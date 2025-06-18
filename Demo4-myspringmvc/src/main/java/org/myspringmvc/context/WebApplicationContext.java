package org.myspringmvc.context;

import jakarta.servlet.ServletContext;

public class WebApplicationContext extends ApplicationContext{
    private ServletContext servletContext;

    public WebApplicationContext(String xmlPath, ServletContext servletContext) throws Exception {
        super(xmlPath);
        this.servletContext = servletContext;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }
}
