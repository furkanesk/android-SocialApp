package com.furkanesk.template.Model;

public class Event {
    private String eventDescription;
    private String eventId;
    private String eventImage;
    private String eventName;
    private String eventVenue;
    private String eventOwner;

    public Event() {
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventImage() {
        return eventImage;
    }

    public void setEventImage(String eventImage) {
        this.eventImage = eventImage;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventVenue() {
        return eventVenue;
    }

    public void setEventVenue(String eventVenue) {
        this.eventVenue = eventVenue;
    }

    public String getEventOwner() {
        return eventOwner;
    }

    public void setEventOwner(String eventOwner) {
        this.eventOwner = eventOwner;
    }

    public Event(String eventDescription, String eventId, String eventImage, String eventName, String eventVenue, String eventOwner) {
        this.eventDescription = eventDescription;
        this.eventId = eventId;
        this.eventImage = eventImage;
        this.eventName = eventName;
        this.eventVenue = eventVenue;
        this.eventOwner = eventOwner;
    }
}
