package com.bookcoast.post_it;

/**
 * Created by Adi on 10/02/17.
 */

public class Data {

    String title;
    String description;
    String eligibility;
    String contact;
    String imgurl;
    String uniqueID;
    String type;
    String date;
    boolean event;

    public Data()
    {}
    public Data(String title, String description, String eligibility, String contact, String imgurl, boolean event,String uid, String type, String date) {
      /*Blank default constructor essential for Firebase*/
        this.title = title;
        this.description = description;
        this.eligibility = eligibility;
        this.contact = contact;
        this.imgurl = imgurl;
        this.event = event;
        this.uniqueID = uid;
        this.type = type;
        this.date = date;

    }

    public String getTitle()
    {
        return this.title;
    }
    public String getDescription()
    {
        return this.description;
    }
    public String getEligibility()
    {
        return this.eligibility;
    }
    public String getContact()
    {
        return this.contact;
    }
    public String getImgurl()
    {
        return this.imgurl;
    }
    public boolean getEvent()
    {
        return this.event;
    }
    public String getUid()
    {
        return this.uniqueID;
    }
    public String getDate() {return date;}



}
