
public class Event {
    private String eventID;
    private String eventType;
    private String timeSlot;
    private String eventDate;
    private String city;
    public int bookingCapacity;
    
    public Event(String eventType, String eventID, int bookingCapacity) {
        this.eventID = assignID(eventID);
        this.bookingCapacity = bookingCapacity;
        this.eventType = checkEventType(eventType);
    }
    
    public String checkEventType(String eventType) {
        String temp = eventType.substring(0, 1);
        if(temp.equalsIgnoreCase("C"))
            this.eventType = "Conference";
        else if(temp.equalsIgnoreCase("S"))
            this.eventType = "Seminar";
        else this.eventType = "Trade Show";
        
        return this.eventType;    
    }
    
    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getBookingCapacity() {
        return bookingCapacity;
    }

    public void setBookingCapacity(int bookingCapacity) {
        this.bookingCapacity = bookingCapacity;
    }
    
    // to update booking capacity for an event if it's already in the hashmap (database) 
    public void increaseBookingCapacity(int bookingCapacity) {
        this.bookingCapacity += bookingCapacity;
    }
    
    
    // check if the id of an event has the correct format 
    public String assignID(String ID) throws StringIndexOutOfBoundsException {
       try {
          
           this.city = ID.substring(0, 3);
           
           if (!city.equalsIgnoreCase("MTL") && !city.equalsIgnoreCase("SHE") && !city.equalsIgnoreCase("QUE")) {
               System.out.println("City name is not correct!");
              // System.exit(1);
           }
           this.timeSlot = ID.substring(3,4);
           
           if(!timeSlot.equalsIgnoreCase("M") && !timeSlot.equalsIgnoreCase("A") && !timeSlot.equalsIgnoreCase("E")) {
               System.out.println("Time slot is not correct!");
               System.exit(1);
           }
           
           this.eventDate = ID.substring(4, 9);
           if(!eventDate.matches("[0-9]+")) {
               System.out.println("Event date is not correct");
               System.exit(1);
           }
           
           this.eventID = this.city+this.timeSlot+this.eventDate;
           return this.eventID;
        
    } catch (StringIndexOutOfBoundsException e) {
        
        System.out.println("id is incorrect!");
        return "incorrect id!";
    }
  }

}
