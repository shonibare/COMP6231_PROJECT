package DEMSBase;


/**
* DEMSBase/DEMSOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from DEMSInterface.idl
* Sunday, March 1, 2020 10:16:59 PM PST
*/

public interface DEMSOperations 
{

  // * Event Manager Functions * //
  int AddEvent (String eventID, DEMSBase.EventType eventType, int eventCapacity, String managerID);
  int RemoveEvent (String eventID, DEMSBase.EventType eventType, String managerID);
  DEMSBase.EventList ListEventAvailability (DEMSBase.EventType eventType, String managerID);

  // * Customer Functions * //
  int BookEvent (String customerID, String eventID, DEMSBase.EventType eventType);
  DEMSBase.EventList GetBookingSchedule (String customerID);
  int CancelEvent (String customerID, String eventID, DEMSBase.EventType eventType);
  int SwapEvent (String customerID, String newEventID, DEMSBase.EventType newEventType, String oldEventID, DEMSBase.EventType oldEventType);

  // * Oneway shutdown * //
  void shutdown ();
} // interface DEMSOperations
