package com.example.myapplication;

import java.io.Serializable;

public class ContactItem implements Serializable
{
    private String user_phNumber, user_Name;
    private long photo_id = 0 , person_id = 0 ;
    private int id;

    public ContactItem(String name, String phone){
        user_Name = name;
        user_phNumber = phone;
        //Photo = photo;
    }

    public long getPhoto_id(){
        return photo_id;
    }

    public long getPerson_id(){
        return person_id;
    }

    public String getUser_phNumber(){
        return user_phNumber;
    }

    public String getUser_Name(){
        return user_Name;
    }

    public void setUser_phNumber(String phone){
        this.user_phNumber = phone;
    }

    public void setUser_Name(String name){
        this.user_Name = name;
    }

    @Override
    public String toString(){
        return this.user_phNumber;
    }

    public String getPhNumberChanged(){
        return user_phNumber.replace("-","");
    }

}
