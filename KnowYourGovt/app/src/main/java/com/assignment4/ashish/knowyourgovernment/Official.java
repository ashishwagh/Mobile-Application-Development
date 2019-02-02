package com.assignment4.ashish.knowyourgovernment;

import java.io.Serializable;

public class Official implements Serializable {

    private String name;
    private String office;
    private String party;
    private int indice;
    private String address;
    private String phone;
    private String website;
    private String email;
    private String photoURL;
    private String youtubeId;
    private String googleplusId;
    private String facebookId;
    private String twitterId;

    public Official(String name, String office, String party, String address, String phone, String website, String email) {
        this.name = name;
        this.office = office;
        this.party = party;
        this.address = address;
        this.phone = phone;
        this.website = website;
        this.email = email;
    }

    public Official(String office, int indice) {
        this.office = office;
        this.indice = indice;
    }

    public Official() {
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getYoutubeId() {
        return youtubeId;
    }

    public void setYoutubeId(String youtubeId) {
        this.youtubeId = youtubeId;
    }

    public String getGoogleplusId() {
        return googleplusId;
    }

    public void setGoogleplusId(String googleplusId) {
        this.googleplusId = googleplusId;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getTwitterId() {
        return twitterId;
    }

    public void setTwitterId(String twitterId) {
        this.twitterId = twitterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }
}
