package com.furkanesk.template.Model;

public class Kullanici {
    private String id;
    private String username;
    private String name;
    private String resimurl;

    public Kullanici() {
    }

    public Kullanici(String id, String username, String name, String resimurl) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.resimurl = resimurl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResimurl() {
        return resimurl;
    }

    public void setResimurl(String resimurl) {
        this.resimurl = resimurl;
    }
}
