package com.example.bisma.calendar_analyzer.models;


public class
EventModel {

    public String AltAddress1;
    public String AltAddress2;
    public String AltGoogleMap;
    public String ListerTitle;
    public String AltPostcode;
    public String AltTown;
    public Boolean AlternativeLocation;
    public String ContactAddress;
    public String ContactName;
    public String ContactPhone;
    public String Description;
    public String EndDate;
    public String ShareUrl;
    public Integer EventDisplayID;
    public Integer EventID;
    public String EventTitle;
    public Integer EventTypeID;
    public String EventURL;
    public Integer ListerID;
    public String StartDate;
    public Integer StatusID;
    public String  Image;


    public String getShareUrl() {
        return ShareUrl;
    }

    public void setShareUrl(String shareUrl) {
        ShareUrl = shareUrl;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getAltAddress1() {
        return AltAddress1;
    }

    public void setAltAddress1(String altAddress1) {
        AltAddress1 = altAddress1;
    }

    public String getAltAddress2() {
        return AltAddress2;
    }

    public void setAltAddress2(String altAddress2) {
        AltAddress2 = altAddress2;
    }

    public Boolean getAlternativeLocation() {
        return AlternativeLocation;
    }

    public void setAlternativeLocation(Boolean alternativeLocation) {
        AlternativeLocation = alternativeLocation;
    }

    public String getAltGoogleMap() {
        return AltGoogleMap;
    }

    public void setAltGoogleMap(String altGoogleMap) {
        AltGoogleMap = altGoogleMap;
    }

    public String getAltPostcode() {
        return AltPostcode;
    }

    public void setAltPostcode(String altPostcode) {
        AltPostcode = altPostcode;
    }

    public String getAltTown() {
        return AltTown;
    }

    public void setAltTown(String altTown) {
        AltTown = altTown;
    }

    public String getContactAddress() {
        return ContactAddress;
    }

    public void setContactAddress(String contactAddress) {
        ContactAddress = contactAddress;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    public String getContactPhone() {
        return ContactPhone;
    }

    public void setContactPhone(String contactPhone) {
        ContactPhone = contactPhone;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public Integer getEventDisplayID() {
        return EventDisplayID;
    }

    public void setEventDisplayID(Integer eventDisplayID) {
        EventDisplayID = eventDisplayID;
    }

    public Integer getEventID() {
        return EventID;
    }

    public void setEventID(Integer eventID) {
        EventID = eventID;
    }

    public String getEventTitle() {
        return EventTitle;
    }

    public void setEventTitle(String eventTitle) {
        EventTitle = eventTitle;
    }

    public Integer getEventTypeID() {
        return EventTypeID;
    }

    public void setEventTypeID(Integer eventTypeID) {
        EventTypeID = eventTypeID;
    }

    public String getEventURL() {
        return EventURL;
    }

    public void setEventURL(String eventURL) {
        EventURL = eventURL;
    }

    public Integer getListerID() {
        return ListerID;
    }

    public void setListerID(Integer listerID) {
        ListerID = listerID;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public Integer getStatusID() {
        return StatusID;
    }

    public void setStatusID(Integer statusID) {
        StatusID = statusID;
    }

    public String getListerTitle() {
        return ListerTitle;
    }

    public void setListerTitle(String listerTitle) {
        ListerTitle = listerTitle;
    }

    @Override
    public String toString() {
        return "EventModel{" +
                "AltAddress1='" + AltAddress1 + '\'' +
                ", AltAddress2='" + AltAddress2 + '\'' +
                ", AltGoogleMap='" + AltGoogleMap + '\'' +
                ", AltPostcode='" + AltPostcode + '\'' +
                ", AltTown='" + AltTown + '\'' +
                ", AlternativeLocation=" + AlternativeLocation +
                ", ContactAddress='" + ContactAddress + '\'' +
                ", ContactName='" + ContactName + '\'' +
                ", ContactPhone='" + ContactPhone + '\'' +
                ", Description='" + Description + '\'' +
                ", EndDate='" + EndDate + '\'' +
                ", EventDisplayID=" + EventDisplayID +
                ", EventID=" + EventID +
                ", EventTitle='" + EventTitle + '\'' +
                ", EventTypeID=" + EventTypeID +
                ", EventURL='" + EventURL + '\'' +
                ", ListerID=" + ListerID +
                ", StartDate='" + StartDate + '\'' +
                ", StatusID=" + StatusID +
                ", Image='" + Image + '\'' +
                '}';
    }
}
