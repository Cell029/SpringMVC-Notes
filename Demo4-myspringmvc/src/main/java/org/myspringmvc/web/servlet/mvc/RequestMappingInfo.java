package org.myspringmvc.web.servlet.mvc;

import java.util.Objects;

// 封装请求映射信息
public class RequestMappingInfo {
    // 请求路径
    private String requestURI;
    // 请求方式（GET或POST）
    private String method;

    public RequestMappingInfo() {
    }

    public RequestMappingInfo(String requestURI, String method) {
        this.requestURI = requestURI;
        this.method = method;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestMappingInfo that = (RequestMappingInfo) o;
        return Objects.equals(requestURI, that.requestURI) && Objects.equals(method, that.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestURI, method);
    }
}
