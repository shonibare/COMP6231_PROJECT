package Entity;

public class Event {
	String eventId;
	String eventType;
	String customerId;
	
	public Event() {
		
	}
	public String getEventId() {
		return eventId;
	}


	public void setEventId(String eventId) {
		this.eventId = eventId;
	}


	public String getEventType() {
		return eventType;
	}


	public void setEventType(String eventType) {
		this.eventType = eventType;
	}


	public String getCustomerId() {
		return customerId;
	}


	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}


	public Event(String customerId, String eventId, String eventType) {
		super();
		this.eventId = eventId;
		this.eventType = eventType;
		this.customerId = customerId;
	}
	
	
}
