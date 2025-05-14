package com.searchcourses.api.dtos;

public class ClickCountUrlDto {
    private String title;
    private String date;
    private String count;

    public ClickCountUrlDto(String title, String date, String count) {
        this.title = title;
        this.date = date;
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getCount() {
        return count;
    }
}
