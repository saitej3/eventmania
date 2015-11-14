package com.example.saiteja.eventmania.model;

/**
 * Created by Sai Teja on 11/14/2015.
 */
public class Person {
    String name;

    public Person()
    {

    }

   public Person(String name)
    {
        this.name=name;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name=name;
    }
}
