package DEMSMain;

// Java libs imports
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.net.CacheRequest;
import java.net.DatagramPacket;
import java.time.LocalDateTime;
import java.util.HashMap;
import org.omg.CORBA.ORB;

// Local Import
import DEMSUtil.*;
import DEMSBase.DEMSPOA;
import DEMSBase.Event;
import DEMSBase.EventList;
import DEMSBase.EventType;
import DEMSBase.LIST_SIZE;
import DEMSEventRecord.EventRecord;

public class DEMSImpl extends DEMSPOA
{
    /* CONSTANTS */
    public static final String MTL = "MTL";
    public static final String QUE = "QUE";
    public static final String SHE = "SHE";
    public static final String LIST_EVENTS_MESSAGE = "LIST_EVENTS_AVAILABILITY";
    public static final String LIST_EVENTS_REPLY = "LIST_EVENTS_REPLY";
    public static final String BOOKED_EVENTS_MESSAGE = "BOOKED_EVENTS_MESSAGE";
    public static final String BOOKED_EVENTS_REPLY = "BOOKED_EVENTS_REPLY";
    public static final String SWAP_EVENT_MESSAGE = "SWAP_EVENT_MESSAGE";
    public static final String SWAP_EVENT_REPLY = "SWAP_EVENT_REPLY";

    // Private attributes
    private ORB orb; // ORB for CORBA
    private String _ID;
    private EventRecord _record;
    private Logger _logger;
    private String _logFile;
    private DateTimeFormatter _dtf;
    private LocalDateTime _dateTime;
    private UDPHandler _udpServer;
    
    // private int _myPort;

    /* Constructor */
    public DEMSImpl(String city)
    {
        // Initialize EventRecord and Logger
        _record = new EventRecord();
        _logger = Logger.GetInstance();

        // Get correct ID
        FileManager manager = new FileManager();        
        _ID = city;

        // Initialize date formatter
        _dtf = DateTimeFormatter.ofPattern("ddmmyy-HHmm");
        _dateTime = LocalDateTime.now();
        _logFile = _dtf.format(_dateTime)+"-log.txt";
        _dtf = DateTimeFormatter.ofPattern("dd/MM/yyy - HH:mm:ss");

        // Initialize UDP server
        _udpServer = new UDPHandler(_ID);
        _udpServer.Init();
    }

    /* Register Remote Object */
    public void setORB(ORB orb_val)
    {
        orb = orb_val;
    }

    // ******************************************************* //
    // *************** EVENT MANAGER FUNCTIONS *************** //
    // ******************************************************* //
    /* Add a new event or update the capacity */
    @Override
    public int AddEvent (String eventID, EventType eventType, int eventCapacity, String managerID)
    {
        int returnValue = 0;

        // Log entry
        _dateTime = LocalDateTime.now();
        _logger.LogMessage("-- Add an event, requested by " + managerID + " at " + _dtf.format(_dateTime)+"\n", _logFile,
                "Logs/" + _ID + "/");

        // Check if request is sent by a manager
        if (managerID.charAt(3) == 'M')
        {
            // Make event object with parameters
            Event newEvent = new Event(eventID, eventType, eventCapacity, 0);

            // Check if event exists
            Event old = _record.GetEventSubHash(newEvent);
            String message, message2;
            if (old == null)
            {
                // Event doesnt exist, create new entry on HashMap
                _record.AddEventSubHash(newEvent);

                // Create log message
                message = "New event added successfully - ID:  " + newEvent.ID + "; Type: " +
                        EventUtil.MakeStringFromType(newEvent.Type)
                        + "; Capacity: " + newEvent.Capacity+"\n";
                message2 = "Returning 0...\n\n";
            }
            else
            {
                // Event already exists, update it
                old.Capacity = eventCapacity;
                message = "Event updated successfully - ID:  " + newEvent.ID + "; Type: " +
                        EventUtil.MakeStringFromType(newEvent.Type)
                        + "; New Capacity: " + newEvent.Capacity+"\n";
                message2 = "Returning 1...\n\n";
                returnValue = 1;
            }

            // ** Log on server ** //
            // Get current date and time
            _logger.LogMessage(message, _logFile, "Logs/" + _ID + "/");
            _logger.LogMessage(message2, _logFile, "Logs/" + _ID + "/");
            _logger.LogMessage("", _logFile, "Logs/" + _ID + "/");
        }
        else
        {
            // System.out.println("Permission denied! Not an Event Manager");
            // Log that it was unsuccessful
            LogUnsuccessful();
            returnValue = -1;
        }

        return returnValue;
    }

    /* Remove an event */
    @Override
    public int RemoveEvent (String eventID, EventType eventType, String managerID)
    {
        int returnValue = 0;

        // Log entry
        _dateTime = LocalDateTime.now();
        _logger.LogMessage("-- Remove an event, requested by " + managerID + " at " + _dtf.format(_dateTime)+"\n", _logFile,
                "Logs/" + _ID + "/");

        // Check if request is sent by a manager
        if (managerID.charAt(3) == 'M')
        {
            // Make event object with parameters
            Event newEvent = new Event(eventID, eventType, 0, 0);

            // Check if event exists
            Event oldEvent = _record.GetEventSubHash(newEvent);
            String message, message2;
            if (oldEvent == null)
            {
                // Event doesnt exist, no need to do anything
                // Create log message
                message = "No event with ID: " + eventID + " exists in this server."+"\n";
                message2 = "Returning 1...\n\n";
                returnValue = 1;
            }
            else
            {
                // Event exists, remove it
                if (oldEvent.TotalBooked > 0)
                {
                    // Get customer IDs
                    // Book next available event for customer if possible
                }

                // Remove this event
                _record.RemoveEventSubHash(oldEvent);
                message = "Event removed successfully - ID:  " + oldEvent.ID + "; Type: " +
                            EventUtil.MakeStringFromType(oldEvent.Type)+"\n";
                message2 = "Returning 0...\n\n";
            }

            _logger.LogMessage(message, _logFile, "Logs/" + _ID + "/");
            _logger.LogMessage(message2, _logFile, "Logs/" + _ID + "/");
            _logger.LogMessage("", _logFile, "Logs/" + _ID + "/");
        }
        else
        {
            // System.out.println("Permission denied! Not an Event Manager");
            // Log that it was unsuccessful
            LogUnsuccessful();
            returnValue = -1;
        }

        return returnValue;
    }

    /* Get the list of events of given type */
    @Override
    public EventList ListEventAvailability (EventType eventType, String managerID)
    {  
        EventList result = null;

        // Log entry
        _dateTime = LocalDateTime.now();
        _logger.LogMessage("-- List events' availability, requested by " + managerID + " at " + _dtf.format(_dateTime)+"\n", _logFile,
                "Logs/" + _ID + "/");

        // Check if request is sent by a manager
        // if (managerID.charAt(3) == 'M')
        // {

        result = GetLocalEventList(eventType);
        
        /* Request the other servers */
        String msg = LIST_EVENTS_MESSAGE+":"+EventUtil.MakeStringFromType(eventType)+":"+managerID+":";
        String reply;
        String split[];
        UDPClient client1;
        UDPClient client2;

        /* Setup to the correct servers */
        if(_ID.equals(MTL))
        {
            client1 = new UDPClient(QUE);
            client2 = new UDPClient(SHE);
        }
        else if(_ID.equals(SHE))
        {
            client1 = new UDPClient(QUE);
            client2 = new UDPClient(MTL);
        }
        else if(_ID.equals(QUE))
        {
            client1 = new UDPClient(SHE);
            client2 = new UDPClient(MTL);
        }
        else
        {
            System.out.println("ERROR: SERVER ID NOT A CITY.");
            return null;
        }

        /* Send messages */
        client1.SendMessage(msg);
        client2.SendMessage(msg);
        
        /* Wait for replies and add to record */
        client1.WaitForReply();
        reply = client1.GetReply();
        client1.Shutdown();
        split = reply.split(":");
        if(split[0].equals(LIST_EVENTS_REPLY))
        {
            int totalEntries = Integer.parseInt(split[1]);
            int i = 2;
            while(totalEntries > 0)
            {
                if(result.total < LIST_SIZE.value)
                {
                    Event e = new Event(split[i], EventUtil.MakeTypeFromString(split[i+1]),
                                        Integer.parseInt(split[i+2]),
                                        Integer.parseInt(split[i+3]));
                    result.list[result.total] = e;
                    result.total++;

                    i += 4;
                    totalEntries--;
                }
                else
                {
                    System.out.println("Exceeded EventList buffer size.");
                }
            }
        }
        else
        {
            System.out.println(split[0]);
        }

        client2.WaitForReply();
        reply = client2.GetReply();
        client2.Shutdown();
        split = reply.split(":");
        if(split[0].equals(LIST_EVENTS_REPLY))
        {
            int totalEntries = Integer.parseInt(split[1]);
            int i = 2;
            while(totalEntries > 0)
            {
                if(result.total < LIST_SIZE.value)
                {
                    Event e = new Event(split[i], EventUtil.MakeTypeFromString(split[i+1]),
                                        Integer.parseInt(split[i+2]),
                                        Integer.parseInt(split[i+3]));
                    result.list[result.total] = e;
                    result.total++;

                    i += 4;
                    totalEntries--;
                }
                else
                {
                    System.out.println("Exceeded EventList buffer size.");
                }
            }
        }
        else
        {
            System.out.println(split[0]);
        }
        // }
        // else
        // {
        //     // System.out.println("Permission denied! Not an Event Manager");
        //     // Log that it was unsuccessful
        //     LogUnsuccessful();
        //     result = null;
        // }
        
        if(result == null)
        {
            // System.out.println("Permission denied! Not an Event Manager");
            // Log that it was unsuccessful
            LogUnsuccessful();
        }
        
        return result;
    }

    // ******************************************************* //
    // ***************** CUSTOMER FUNCTIONS ****************** //
    // ******************************************************* //
    @Override
    public int BookEvent(String customerID, String eventID, EventType eventType)
    {
        int returnValue = 0;

        // Log entry
        _dateTime = LocalDateTime.now();
        _logger.LogMessage("-- Book an event, requested by " + customerID + " at " + _dtf.format(_dateTime)+"\n", _logFile,
                "Logs/" + _ID + "/");

        // Check if request is sent by a customer
        if (customerID.charAt(3) == 'C')
        {
            // Make event object with parameters
            Event newEvent = new Event(eventID, eventType, 0, 0);

            // Check if event exists
            Event oldEvent = _record.GetEventSubHash(newEvent);
            String message, message2;
            if (oldEvent == null)
            {
                // Event doesn't exist, no need to do anything
                // Create log message
                message = "Sorry! No event with ID: " + eventID + " exists in this server."+"\n";
                message2 = "Returning 1...\n\n";
                returnValue = 1;
            }
            else
            {
                if(oldEvent.TotalBooked == oldEvent.Capacity)
                {
                    message = "Sorry! This event is already booked at maximum capacity - ID: " + eventID + " exists in this server."+"\n";
                    message2 = "Returning 1...\n\n";
                    returnValue = 1;
                }
                else
                {
                    // Book event
                    returnValue = _record.BookEvent(oldEvent, _ID, customerID);
                    if(returnValue == 0)
                    {
                        message = "Event booked successfully - ID:  " + oldEvent.ID + "; Type: " +
                                    EventUtil.MakeStringFromType(oldEvent.Type)+"\n";
                        message2 = "Returning 0...\n\n";
                    }
                    else if(returnValue == 2)
                    {
                        message = "Event already booked by this customer - ID:  " + oldEvent.ID + "; Type: " +
                                    EventUtil.MakeStringFromType(oldEvent.Type)+"\n";
                        message2 = "Returning 0...\n\n";
                    }
                    else if(returnValue == 3)
                    {
                        message = "Event booking capacity reached by this customer - ID:  " + oldEvent.ID + "; Type: " +
                                    EventUtil.MakeStringFromType(oldEvent.Type)+"\n";
                        message2 = "Returning 0...\n\n";
                    }
                    else if(returnValue == -1)
                    {
                        message = "Couldn't book this event, an error occurred - ID:  " + oldEvent.ID + "; Type: " +
                                    EventUtil.MakeStringFromType(oldEvent.Type)+"\n";
                        message2 = "Returning -1...\n\n";
                    }
                    else
                    {
                        message = "THIS SHOULDN'T BE HAPPENING! WHY ARE YOU HERE - ID:  " + oldEvent.ID + "; Type: " +
                                    EventUtil.MakeStringFromType(oldEvent.Type) + "; And result: " + r+"\n";
                        message2 = "Returning -1...\n\n";
                        returnValue = -1;
                    }
                }

            }

            _logger.LogMessage(message, _logFile, "Logs/" + _ID + "/");
            _logger.LogMessage(message2, _logFile, "Logs/" + _ID + "/");
            _logger.LogMessage("", _logFile, "Logs/" + _ID + "/");
        }
        else
        {
            // Log that it was unsuccessful
            LogUnsuccessful();
            returnValue = -1;
        }

        return returnValue;
    }

    @Override
    public EventList GetBookingSchedule(String customerID)
    {
        EventList result;

        // Log entry
        _dateTime = LocalDateTime.now();
        _logger.LogMessage("-- List booked events, requested by " + customerID + " at " + _dtf.format(_dateTime)+"\n", _logFile,
                "Logs/" + _ID + "/");

        // Check if request is sent by a customer
        if (customerID.charAt(3) == 'C')
        {

            result = GetLocalBookedEvents(customerID);
            
            /* Request the other servers */
            String msg = BOOKED_EVENTS_MESSAGE+":"+customerID+":";
            String reply;
            String split[];
            UDPClient client1;
            UDPClient client2;

            /* Setup to the correct servers */
            if(_ID.equals(MTL))
            {
                client1 = new UDPClient(QUE);
                client2 = new UDPClient(SHE);
            }
            else if(_ID.equals(SHE))
            {
                client1 = new UDPClient(QUE);
                client2 = new UDPClient(MTL);
            }
            else if(_ID.equals(QUE))
            {
                client1 = new UDPClient(SHE);
                client2 = new UDPClient(MTL);
            }
            else
            {
                System.out.println("ERROR: SERVER ID NOT A CITY.");
                return null;
            }

            /* Send messages */
            client1.SendMessage(msg);
            client2.SendMessage(msg);
            
            /* Wait for replies and add to record */
            client1.WaitForReply();
            reply = client1.GetReply();
            client1.Shutdown();
            split = reply.split(":");
            if(split[0].equals(BOOKED_EVENTS_REPLY))
            {
                int totalEntries = Integer.parseInt(split[1]);
                int i = 2;
                while(totalEntries > 0)
                {
                    if(result.total < LIST_SIZE.value)
                    {
                        Event e = new Event(split[i], EventUtil.MakeTypeFromString(split[i+1]),
                                            Integer.parseInt(split[i+2]),
                                            Integer.parseInt(split[i+3]));
                        result.list[result.total] = e;
                        result.total++;

                        i += 4;
                        totalEntries--;
                    }
                    else
                    {
                        System.out.println("Exceeded EventList buffer size.");
                    }
                }
            }
            else
            {
                System.out.println(split[0]);
            }

            client2.WaitForReply();
            reply = client2.GetReply();
            client2.Shutdown();
            split = reply.split(":");
            if(split[0].equals(BOOKED_EVENTS_REPLY))
            {
                int totalEntries = Integer.parseInt(split[1]);
                int i = 2;
                while(totalEntries > 0)
                {
                    if(result.total < LIST_SIZE.value)
                    {
                        Event e = new Event(split[i], EventUtil.MakeTypeFromString(split[i+1]),
                                            Integer.parseInt(split[i+2]),
                                            Integer.parseInt(split[i+3]));
                        result.list[result.total] = e;
                        result.total++;

                        i += 4;
                        totalEntries--;
                    }
                    else
                    {
                        System.out.println("Exceeded EventList buffer size.");
                    }
                }
            }
            else
            {
                System.out.println(split[0]);
            }
        }
        else
        {
            // System.out.println("Permission denied! Not an Event Manager");
            // Log that it was unsuccessful
            LogUnsuccessful();
            result = null;
        }

        return result;
    }

    @Override
    public int CancelEvent(String customerID, String eventID, EventType eventType)
    {
        int returnValue = 0;

        // Log entry
        _dateTime = LocalDateTime.now();
        _logger.LogMessage("-- Cancel an event, requested by " + customerID + " at " + _dtf.format(_dateTime)+"\n", _logFile,
                "Logs/" + _ID + "/");

        // Check if request is sent by a customer
        if (customerID.charAt(3) == 'C')
        {
            // Make event object with parameters
            Event newEvent = new Event(eventID, eventType, 0, 0);

            // Check if event exists
            Event oldEvent = _record.GetEventSubHash(newEvent);
            String message, message2;
            if (oldEvent == null)
            {
                message = "Sorry! No event with ID: " + eventID + " exists in this server."+"\n";
                message2 = "Returning 1...\n\n";
                returnValue = 1;
            }
            else
            {
                returnValue = _record.CancelEvent(oldEvent, customerID);
                if(returnValue == 0)
                {
                    message = "Event canceled successfully - ID:  " + oldEvent.ID + "; Type: " +
                                    EventUtil.MakeStringFromType(oldEvent.Type)+"\n";
                    message2 = "Returning 0...\n\n";
                }
                else if(returnValue == 2)
                {
                    message = "Couldn't cancel event, not booked by this customer - ID:  " + oldEvent.ID + "; Type: " +
                                    EventUtil.MakeStringFromType(oldEvent.Type)+"\n";
                    message2 = "Returning 1...\n\n";
                }
                else if(returnValue == -1)
                {
                    message = "Couldn't cancel event, no events booked by this customer in this server - ID:  "
                                    + oldEvent.ID + "; Type: " + EventUtil.MakeStringFromType(oldEvent.Type)+"\n";
                    message2 = "Returning -1...\n\n";
                }
                else
                {
                    message = "WEIRD! YOU SHOULDN'T GET HERE - ID:  " + oldEvent.ID + "; Type: " +
                                    EventUtil.MakeStringFromType(oldEvent.Type)+"\n";
                    message2 = "Returning -2...\n\n";
                }
            }

            _logger.LogMessage(message, _logFile, "Logs/" + _ID + "/");
            _logger.LogMessage(message2, _logFile, "Logs/" + _ID + "/");
            _logger.LogMessage("", _logFile, "Logs/" + _ID + "/");
        }
        else
        {
            // Log that it was unsuccessful
            LogUnsuccessful();
            returnValue = -1;
        }

        return returnValue;
    }

    @Override
    public int SwapEvent (String customerID, String newEventID, EventType newEventType, String oldEventID, EventType oldEventType)
    {
        int returnValue = 0;

        // Log entry
        _dateTime = LocalDateTime.now();
        _logger.LogMessage("-- Swap events, requested by " + customerID + " at " + _dtf.format(_dateTime)+"\n", _logFile,
                "Logs/" + _ID + "/");

        // Check if request is sent by a customer
        if (customerID.charAt(3) == 'C')
        {
            // Make event object with parameters
            Event event = new Event(oldEventID, oldEventType, 0, 0);

            // Check if event exists
            Event oldEvent = _record.GetEventSubHash(event);
            String message, message2;
            if (oldEvent == null)
            {
                // Event doesn't exist, no need to do anything
                // Create log message
                message = "Sorry! No event with ID: " + oldEventID + " exists in this server.\n";
                message2 = "Returning 1...\n\n";
                returnValue = 1;
            }
            else
            {
                if(newEventID.contains(_ID))
                {
                    // Its a local swap
                    int result = BookEvent(customerID, newEventID, newEventType);
                    if(result == 0)
                    {
                        if(CancelEvent(customerID, oldEventID, oldEventType) == 0)
                        {
                            message = "Event swapped successfully - ID:  " + oldEvent.ID + "; Type: " +
                                    EventUtil.MakeStringFromType(oldEventType) + " for ID2: " + newEventID +
                                    "; Type2: " + EventUtil.MakeStringFromType(newEventType)+"\n";
                            message2 = "Returning 0..."+"\n\n";
                        }
                    }
                    else if(result == 1)
                    {
                        message = "Sorry! This event is already booked at maximum capacity - ID: " + newEventID + " exists in this server."+"\n";
                        message2 = "Returning 1...\n\n";
                        returnValue = 1;
                    }
                    else if(result == 2)
                    {
                        message = "Event couldn't be swapped, event already booked by this customer - ID:  " + oldEvent.ID + "; Type: " +
                                    EventUtil.MakeStringFromType(oldEventType) + " for ID2: " + newEventID +
                                    "; Type2: " + EventUtil.MakeStringFromType(newEventType)+"\n";
                        message2 = "Returning 2..."+"\n\n";
                        returnValue = 2;
                    }
                    else if(result == 3)
                    {
                        message = "Event couldn't be swapped, booking limit of 3 already reached by this customer on the other server - ID:  "
                                    + oldEvent.ID + "; Type: " + EventUtil.MakeStringFromType(oldEventType) + " for ID2: " + newEventID +
                                    "; Type2: " + EventUtil.MakeStringFromType(newEventType)+"\n";
                        message2 = "Returning 3..."+"\n\n";
                        returnValue = 3;
                    }
                    else if(result == -1)
                    {
                        message = "Event couldn't be swapped, an error occurred - ID:  " + oldEvent.ID + "; Type: " +
                                    EventUtil.MakeStringFromType(oldEventType) + " for ID2: " + newEventID +
                                    "; Type2: " + EventUtil.MakeStringFromType(newEventType)+"\n";
                        message2 = "Returning -1..."+"\n\n";
                        returnValue = -1;
                    }
                }
                else
                {
                    // Message server in question and check
                    String msg = SWAP_EVENT_MESSAGE+":"+customerID+":"+newEventID+":"
                                    +EventUtil.MakeStringFromType(newEventType)+":";
                    String reply;
                    String split[];
                    UDPClient client;

                    if(newEventID.contains(QUE))
                        client = new UDPClient(QUE);
                    else if(newEventID.contains(MTL))
                        client = new UDPClient(MTL);
                    else if(newEventID.contains(SHE))
                        client = new UDPClient(SHE);
                    else
                    {
                        System.out.println("ERROR: NEW EVENT ID NOT VALID.");
                        return -1;
                    }

                    /* Send messages */
                    client.SendMessage(msg);
                
                    /* Wait for replies and add to record */
                    client.WaitForReply();
                    reply = client.GetReply();
                    client.Shutdown();
                    split = reply.split(":");
                    
                    // If reply is positive, remove event here (only gets positive if the add operation was successful there)
                    if(split[0].equals(SWAP_EVENT_REPLY))
                    {
                        // parse int of reply
                        int r = Integer.parseInt(split[1]);

                        // Finish swapping or log according to reply
                        if(r == 0)
                        {
                            if(CancelEvent(customerID, oldEventID, oldEventType) == 0)
                            {
                                message = "Event swapped successfully - ID:  " + oldEvent.ID + "; Type: " +
                                        EventUtil.MakeStringFromType(oldEventType) + " for ID2: " + newEventID +
                                        "; Type2: " + EventUtil.MakeStringFromType(newEventType)+"\n";
                                message2 = "Returning 0..."+"\n\n";
								returnValue = 0;
                            }
                        }
                        else if(r == 1)
                        {
                            message = "Event couldn't be swapped, event already booked by this customer - ID:  " + oldEvent.ID + "; Type: " +
                                        EventUtil.MakeStringFromType(oldEventType) + " for ID2: " + newEventID +
                                        "; Type2: " + EventUtil.MakeStringFromType(newEventType)+"\n";
                            message2 = "Returning 1..."+"\n\n";
							returnValue = 1;
                        }
                        else if(r == 2)
                        {
                            message = "Event couldn't be swapped, booking limit of 3 already reached by this customer on the other server - ID:  "
                                        + oldEvent.ID + "; Type: " + EventUtil.MakeStringFromType(oldEventType) + " for ID2: " + newEventID +
                                        "; Type2: " + EventUtil.MakeStringFromType(newEventType)+"\n";
                            message2 = "Returning 2..."+"\n\n";
							returnValue = 2;
                        }
                        else if(r == 3)
                        {
                            message = "Event couldn't be swapped, booking limit of 3 already reached by this customer on the other server - ID:  "
                                        + oldEvent.ID + "; Type: " + EventUtil.MakeStringFromType(oldEventType) + " for ID2: " + newEventID +
                                        "; Type2: " + EventUtil.MakeStringFromType(newEventType)+"\n";
                            message2 = "Returning 3..."+"\n\n";
							returnValue = 3;
                        }
                        else if(r == -1)
                        {
                            message = "Event couldn't be swapped, an error occurred - ID:  " + oldEvent.ID + "; Type: " +
                                        EventUtil.MakeStringFromType(oldEventType) + " for ID2: " + newEventID +
                                        "; Type2: " + EventUtil.MakeStringFromType(newEventType)+"\n";
                            message2 = "Returning -1..."+"\n\n";
							returnValue = -1;
                        }

                        _logger.LogMessage(message, _logFile, "Logs/" + _ID + "/");
                        _logger.LogMessage(message2, _logFile, "Logs/" + _ID + "/");
                        _logger.LogMessage("", _logFile, "Logs/" + _ID + "/");
                    }
                    else
                    {
                        System.out.println(split[0]);
                    }
                }
            }
        }
        else
        {
            // Log that it was unsuccessful
            LogUnsuccessful();
            returnValue = -1;
        }

        return returnValue;
    }

    /* Shutdown method */
    @Override
    public void shutdown()
    {
        orb.shutdown(false);
    }

    // ******************************************************* //
    // ******************** GENERAL USE ********************** //
    // ******************************************************* //
    // public int GetPort() { return _myPort; }
    public String GetID() { return _ID; }

    // Log unsuccessful
    private void LogUnsuccessful()
    {
        _logger.LogMessage("Permission denied! Not an Event Manager\n", _logFile, "Logs/" + _ID + "/");
        _logger.LogMessage("Returning error -1...\n\n", _logFile, "Logs/" + _ID + "/");
        _logger.LogMessage("", _logFile, "Logs/" + _ID + "/");
    }

    /* Private Helpers */
    private EventList GetLocalEventList(EventType type)
    {
        EventList result;
        /* Fill with self entries */
        result = _record.FindEventsByType(type);
        return result;
    }

    private EventList GetLocalBookedEvents(String customerID)
    {
        EventList result;
        result = _record.FindAllBookedEvents(customerID);
        return result;
    }


    /* UDP SERVER IMPLEMENTATION */
    private class UDPHandler extends UDPServer
    {
        public UDPHandler(String city)
        {
            super(city);
        }

        private void SendReply(String msg)
        {
            _reply = new DatagramPacket(msg.getBytes(), msg.length(),
                            _request.getAddress(), _request.getPort());
            try
            {
                _aSocket.send(_reply);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void HandleMessage()
        {
            String msg = new String(_request.getData());
            String split[] = msg.split(":");
            
            /* CHECK MESSAGE */
            if(split[0].equals(LIST_EVENTS_MESSAGE))
            {
                System.out.println(_ID + " server got request to list events by " + split[2]);
                
                // Log entry
                _dateTime = LocalDateTime.now();
                _logger.LogMessage("-- List events' availability, requested by " + split[2] + " at " + _dtf.format(_dateTime)+"\n", _logFile,
                    "Logs/" + _ID + "/");

                if(split[2].charAt(3) == 'M')
                {
                    /* Make list as message */
                    EventType type = EventUtil.MakeTypeFromString(split[1]);
                    EventList list = GetLocalEventList(type);
                    if(list.list[0].ID.equals("NONE"))
                    {
                        msg = "NO EVENTS IN " + _ID +" SERVER:";
                    }
                    else
                    {
                        msg = LIST_EVENTS_REPLY+":"+list.total+":";
    
                        for(int i = 0; i < LIST_SIZE.value; ++i)
                        {
                            Event e = list.list[i];
                            if(i < list.total)
                            {
                                if(i == (LIST_SIZE.value-1))
                                {
                                    msg = msg + e.ID + ":" +
                                            EventUtil.MakeStringFromType(e.Type) + ":" +
                                            e.Capacity + ":" + e.TotalBooked;
                                }
                                else
                                {
                                    msg = msg + e.ID + ":" +
                                            EventUtil.MakeStringFromType(e.Type) + ":" +
                                            e.Capacity + ":" + e.TotalBooked + ":";
                                }
    
                            }
                            else
                            {
                                if(i == (LIST_SIZE.value-1))
                                    msg = msg + "NONE";
                                else
                                    msg = msg + "NONE:";
                            }
                        }
                    }
                }
                else
                {
                    msg = "ERROR NOT MANAGER REQUESTED LIST EVENTS!:";
                }

                /* Send reply */
                SendReply(msg);
            }
            else if(split[0].equals(BOOKED_EVENTS_MESSAGE))
            {
                System.out.println(_ID + " server got request to list booked events");
                
                // Log entry
                _dateTime = LocalDateTime.now();
                _logger.LogMessage("-- List booked events, requested by " + split[1] + " at " + _dtf.format(_dateTime)+"\n", _logFile,
                    "Logs/" + _ID + "/");

                if(split[1].charAt(3) == 'C')
                {
                    /* Make list as message */
                    EventList list = GetLocalBookedEvents(split[1]);
                    if(list.list[0].ID.equals("NONE"))
                    {
                        msg = "NO BOOKED EVENTS BY " + split[1] + " IN " + _ID +" SERVER:";
                    }
                    else
                    {
                        msg = BOOKED_EVENTS_REPLY+":"+list.total+":";
    
                        for(int i = 0; i < LIST_SIZE.value; ++i)
                        {
                            Event e = list.list[i];
                            if(i < list.total)
                            {
                                if(i == (LIST_SIZE.value-1))
                                {
                                    msg = msg + e.ID + ":" +
                                            EventUtil.MakeStringFromType(e.Type) + ":" +
                                            e.Capacity + ":" + e.TotalBooked;
                                }
                                else
                                {
                                    msg = msg + e.ID + ":" +
                                            EventUtil.MakeStringFromType(e.Type) + ":" +
                                            e.Capacity + ":" + e.TotalBooked + ":";
                                }
    
                            }
                            else
                            {
                                if(i == (LIST_SIZE.value-1))
                                    msg = msg + "NONE";
                                else
                                    msg = msg + "NONE:";
                            }
                        }
                    }
                }
                else
                {
                    msg = "ERROR NOT CUSTOMER REQUESTED TO LIST BOOKED EVENTS!:";
                }

                /* Send reply */
                SendReply(msg);
            }
            else if(split[0].equals(SWAP_EVENT_MESSAGE))
            {
				// Log entry
                _dateTime = LocalDateTime.now();
                _logger.LogMessage("-- Swap events, requested by " + split[1] + " at " + _dtf.format(_dateTime)+"\n", _logFile,
                    "Logs/" + _ID + "/");
				
                int result = BookEvent(split[1], split[2],
                                EventUtil.MakeTypeFromString(split[3]));
                msg = SWAP_EVENT_REPLY+":"+result+":";

                /* Send reply */
                SendReply(msg);
            }
        }
    }
}
