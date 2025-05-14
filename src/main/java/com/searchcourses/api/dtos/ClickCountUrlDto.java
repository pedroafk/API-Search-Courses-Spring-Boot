package com.searchcourses.api.dtos;

public class ClickCountUrlDto {
    private String title;
    private String date;
    private Integer count;

    public ClickCountUrlDto(String title, String date, Integer count) {
        this.title = title;
        this.date = date;
        this.count = count;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public Integer getCount() {
        return count;
    }
}
