package com.upao.induct3d.backend.domain.response;


public class ApiResponse<T> {
    private T data;
    private Object meta;

    public ApiResponse() {}

    public ApiResponse(T data) {
        this.data = data;
    }

    public ApiResponse(T data, Object meta) {
        this.data = data;
        this.meta = meta;
    }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public Object getMeta() { return meta; }
    public void setMeta(Object meta) { this.meta = meta; }
}

