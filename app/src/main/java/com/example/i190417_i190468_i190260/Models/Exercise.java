package com.example.i190417_i190468_i190260.Models;

public class Exercise {
    String Name, Link, Description, Calories, Time, Image, Timestamp;

    public Exercise() {}

    public Exercise(String Name, String Link, String Description, String Calories, String Time, String Image) {
        this.Name = Name;
        this.Link = Link;
        this.Description = Description;
        this.Calories = Calories;
        this.Time = Time;
        this.Image = Image;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCalories() {
        return Calories;
    }

    public void setCalories(String calories) {
        Calories = calories;
    }
}
