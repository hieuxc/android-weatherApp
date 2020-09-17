package com.example.helloworld;

public class Weather {
    public String DayOfWeek;
    public String Day;
    public String ImageIcon;
    public String Temp;
    public String Status;

    public Weather(String dayOfWeek, String day, String imageIcon, String temp, String status) {
        DayOfWeek = dayOfWeek;
        Day = day;
        ImageIcon = imageIcon;
        Temp = temp;
        Status = status;
    }
}
