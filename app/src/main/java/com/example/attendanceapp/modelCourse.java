package com.example.attendanceapp;

public class modelCourse {

    public modelCourse(String coursename, String place, String teacherName, String startHour, String endHour, String code) {
        Coursename = coursename;
        this.place = place;
        this.teacherName = teacherName;
        this.startHour = startHour;
        this.endHour = endHour;
        this.code = code;
    }

    String Coursename;
    String place;
    String teacherName;

    public String getCoursename() {
        return Coursename;
    }

    public void setCoursename(String coursename) {
        Coursename = coursename;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getEndHour() {
        return endHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    String startHour;
    String endHour;
    String code;

}