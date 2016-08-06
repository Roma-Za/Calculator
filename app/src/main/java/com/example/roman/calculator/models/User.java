package com.example.roman.calculator.models;

public class User {
    private int user_id = 0;
    private String fb_id = "";
    private String first_name = "";
    private String last_name = "";
    private String login = "";
    private String password = "";
    private String email = "";
    private String phone = "";

    public void setFb_id(String fb_id){
        this.fb_id = fb_id;
    }
    public void setFirst_name(String fn){
        this.first_name = fn;
    }
    public void setLast_name(String ln){
        this.last_name = ln;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getFirst_name(){return first_name;}
    public String getLast_name(){return last_name;}
}
