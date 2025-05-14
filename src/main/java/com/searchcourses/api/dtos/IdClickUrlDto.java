package com.searchcourses.api.dtos;

public class IdClickUrlDto {
    private String url;
    private String code;
    private String title;
    private Integer count;

    public IdClickUrlDto(String url, String code, String title, Integer count) {
        this.url = url;
        this.code = code;
        this.title = title;
        this.count = count;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public Integer getCount() {
        return count;
    }
}
