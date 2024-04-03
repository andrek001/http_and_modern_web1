package ru.netology;

import org.apache.hc.core5.http.NameValuePair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request {
    private String method;
    private String path;
    private Map<String, String> headers;

    public List<NameValuePair> getQueryParams() {
        return queryParams;
    }


    public void setQueryParams(List<NameValuePair>queryParams) {
        this.queryParams = queryParams;
    }

    private List<NameValuePair> queryParams;
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
