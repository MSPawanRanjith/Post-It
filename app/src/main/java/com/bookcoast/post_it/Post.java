package com.bookcoast.post_it;

/**
 * Created by Anush kumar.v on 11-02-2017.
 */

public class Post {
    private String description,eligibility,title,contact,imgurl;
    public Post(){

    }
    public Post(String description, String eligibility, String title, String contact, String imgurl) {
        this.description = description;
        this.eligibility = eligibility;
        this.title = title;
        this.contact = contact;
        this.imgurl = imgurl;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEligibility() {
        return eligibility;
    }

    public void setEligibility(String eligibility) {
        this.eligibility = eligibility;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
