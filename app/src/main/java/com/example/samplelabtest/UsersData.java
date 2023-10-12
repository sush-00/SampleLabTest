package com.example.samplelabtest;

public class UsersData
{
    String email, username, passwd;

    public UsersData()
    {}

    public UsersData(String email, String username, String passwd)
    {
        this.email = email;
        this.username = username;
        this.passwd = passwd;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPasswd()
    {
        return passwd;
    }

    public void setPasswd(String passwd)
    {
        this.passwd = passwd;
    }
}
