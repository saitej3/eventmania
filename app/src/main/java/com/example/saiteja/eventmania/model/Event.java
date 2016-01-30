package com.example.saiteja.eventmania.model;

/**
 * Created by Sai Teja on 10/22/2015.
 */
public class Event {

    int id;
    String eventName;
    String eventTime;
    String eventVenue;
    String imagePath;
    String type;
    String subtype,cname1,cname2,cno1,cno2,club;
    String content;
    String description;
    int going;

    public Event()
    {

    }

//    public Event(int id, String eventName, String eventTime, String eventVenue, String imagePath)
//    {
//        this.id=id;
//        this.eventName=eventName;
//        this.eventTime=eventTime;
//        this.eventVenue =eventVenue;
//        this.imagePath=imagePath;
//    }

    public int getId()
    {
        return id;
    }

    public void setId(int num)
    {
        this.id=num;
    }

    public String getEventName()
    {
        return eventName;
    }

    public void setEventName(String name)
    {
        this.eventName=name;
    }
    public String getEventTime()
    {
        return eventTime;
    }

    public void setEventTime(String time)
    {
        this.eventTime=time;
    }
    public String getEventVenue()
    {
        return eventVenue;
    }

    public void setEventVenue(String name)
    {
        this.eventVenue =name;
    }

    public String getImagePath(){return imagePath;}

    public void setImagePath(String name)
    {
        this.imagePath=name;
    }

    public String getType(){return type;}

    public void setType(String name)
    {
        this.type=name;
    }

    public String getSubtype(){return subtype;}

    public void setSubtype(String name)
    {
        this.subtype=name;
    }

    public String getCname1(){return cname1;}

    public void setCname1(String name)
    {
        this.cname1=name;
    }

    public String getCname2(){return cname2;}

    public void setCname2(String name)
    {
        this.cname2=name;
    }

    public String getCno1(){return cno1;}

    public void setCno1(String name)
    {
        this.cno1=name;
    }

    public String getCno2(){return cno2;}

    public void setCno2(String name)
    {
        this.cno2=name;
    }

    public String getClub(){return club;}

    public void setClub(String name)
    {
        this.club=name;
    }

    public String getContent(){return content;}

    public void setContent(String name)
    {
        this.content=name;
    }

    public String getDescription(){return description;}

    public void setDescription(String name)
    {
        this.description=name;
    }

    public int getGoing()
    {
        return going;
    }

    public void setGoing(int num)
    {
        this.going=num;
    }

}
