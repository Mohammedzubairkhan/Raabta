package com.example.mzk.raabta;

/**
 * Created by MZK on 3/27/2018.
 */

public class Messages {
    String Content;
    String username,time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Messages(String content, String username, String time) {

        Content = content;
        this.username = username;
        this.time = time;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Messages() {

    }


}
