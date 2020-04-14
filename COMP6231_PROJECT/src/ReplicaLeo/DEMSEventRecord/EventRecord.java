package DEMSEventRecord;

import java.util.HashMap;

import DEMSBase.Event;
import DEMSBase.EventList;
import DEMSBase.EventType;
import DEMSBase.LIST_SIZE;

public class EventRecord
{
    public static final int MAX_EVENTS_BOOKED = 2048;

    private HashMap<EventType, HashMap<String, Event>> _eventRecord;
    private HashMap<String, String[]> _bookedIDs;

    public EventRecord()
    {
        _eventRecord = new HashMap<EventType, HashMap<String, Event>>();
        _eventRecord.put(EventType.Conference, new HashMap<String, Event>());
        _eventRecord.put(EventType.Seminar, new HashMap<String, Event>());
        _eventRecord.put(EventType.TradeShow, new HashMap<String, Event>());

        _bookedIDs = new HashMap<String, String[]>();
    }

    // Check if these 3 are necessary
    // Add member main Hash method
    // Remove member main Hash method
    // Look for member main  method
    public EventList FindEventsByType(EventType type)
    {
        GetEventsOfType getEvents = new GetEventsOfType(type);
        getEvents.run();
        return getEvents._result;
    }

    public Event FindEventByID(String eventID)
    {
        Event e;
        HashMap<String, Event> hash = _eventRecord.get(EventType.Conference);
        e = hash.get(eventID);
        if(e != null)
            return e;

        hash = _eventRecord.get(EventType.Seminar);
        e = hash.get(eventID);
        if(e != null)
            return e;

        hash = _eventRecord.get(EventType.TradeShow);
        e = hash.get(eventID);
        if(e != null)
            return e;

        return null;
    }

    public EventList FindAllBookedEvents(String customerID)
    {
        GetBookedEvents booked = new GetBookedEvents(customerID);
        booked.run();
        return booked._result;
    }

    // Print entire subhash keys
    public void PrintSubhash()
    {
        HashMap<String, Event> hash = _eventRecord.get(EventType.Conference);
        for (String i : hash.keySet())
        {
            System.out.println("ID: " + i + "; Capacity: " + hash.get(i).Capacity);
        }

        hash = _eventRecord.get(EventType.TradeShow);
        for (String i : hash.keySet())
        {
            System.out.println("ID: " + i + "; Capacity: " + hash.get(i).Capacity);
        }

        hash = _eventRecord.get(EventType.Seminar);
        for (String i : hash.keySet())
        {
            System.out.println("ID: " + i + "; Capacity: " + hash.get(i).Capacity);
        }
    }

    // Read subhash from file (?)
    
    // Add member subHash Method
    public void AddEventSubHash(Event event)
    {
        AddEvent add = new AddEvent(event);
        add.start();
    }

    // Remove member subHash method
    public void RemoveEventSubHash(Event event)
    {
        RemoveEvent remove = new RemoveEvent(event);
        remove.start();
    }

    // Look for member subHash Method
    public Event GetEventSubHash(Event event)
    {
        GetEvent get = new GetEvent(event);
        get.run();
        return get.FoundEvent;
    }

    public int BookEvent(Event event, String city, String id)
    {
        BookAnEvent book = new BookAnEvent(event, city, id);
        book.run();
        return book._booked;
    }

    public int CancelEvent(Event event, String id)
    {
        String[] list = _bookedIDs.get(id);
        if(list != null)
        {
            boolean found = false;
            int i;
            for(i = 0; (i < MAX_EVENTS_BOOKED) && !found; i++)
            {
                if(list[i].equals(event.ID))
                {
                    found = true;
                }
            }
    
            if(found)
            {
                UnbookAnEvent unbook = new UnbookAnEvent(event, id, i-1);
                unbook.start();
                return 0;
            }
            else
            {
                return 2;
            }
        }
        else
        {
            return -1;
        }
    }


    /*
        THREAD CLASSES TO RUN CONCURRENT OPERATIONS
    */

    private class UnbookAnEvent extends Thread
    {
        private Event _event;
        private String _id;
        private int _pos;

        public UnbookAnEvent(Event e, String id, int pos)
        {
            _event = e;
            _id = id;
            _pos = pos;
        }

        public void run()
        {
            try
            {
                synchronized(this)
                {
                    _event.TotalBooked--;
					String[] list = _bookedIDs.remove(_id);
                    list[_pos] = "";
                    _bookedIDs.put(_id, list);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private class BookAnEvent extends Thread
    {
        private Event _event;
        private String _id;
        private String _city;
        public int _booked;

        public BookAnEvent(Event e, String city, String id)
        {
            _event = e;
            _id = id;
            _city = city;
            _booked = -1;
        }

        public void run()
        {
            try
            {
                /* Search if event is already booked by this customer */
                if(_event != null)
                {
                    String[] events = _bookedIDs.get(_id);
                    if(events != null)
                    {
                        int i;
                        /* Check if event not already booked */
                        for(i = 0; (i < MAX_EVENTS_BOOKED) && !(_booked == 2); i++)
                        {
                            if(events[i].equals(_event.ID))
                            {
                                _booked = 2;
                            }
                        }

                        /* Find first empty spot */
                        i = 0;
                        boolean found = false;
                        while(!found)
                        {
                            if((i < MAX_EVENTS_BOOKED) && !events[i].equals(""))
                            {
                                found = true;
                            }
                            i++;
                        }

                        if(i < MAX_EVENTS_BOOKED && _booked != 1)
                        {
                            /* Add to booked HashMap */
                            if(_id.contains(_city) || (i < 3))
                            {
                                events[i] = _event.ID;
                                _booked = 0;
                            }
                            else
                            {
                                _booked = 2;
                            }
                        }
                    }
                    else
                    {
                        /* Create new entry for this customer */
                        events = new String[MAX_EVENTS_BOOKED];
                        for(int i = 0; i < MAX_EVENTS_BOOKED; i++)
                            events[i] = "";
                        events[0] = _event.ID;
                        _booked = 0;
                    }

                    if(_booked == 0)
                    {
                        synchronized(this)
                        {
                            _bookedIDs.put(_id, events);
                            _event.TotalBooked++;
                        }
                    }
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private class AddEvent extends Thread
    {
        private Event _newEvent;
        
        public AddEvent(Event event)
        {
            _newEvent = event;
        }

        public void run()
        {
            try
            {
                synchronized(this)
                {
                   _eventRecord.get(_newEvent.Type).put(_newEvent.ID,_newEvent);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }

    private class RemoveEvent extends Thread
    {
        private Event _eventToRemove;
        
        public RemoveEvent(Event event)
        {
            _eventToRemove = event;
        }

        public void run()
        {
            try
            {
                synchronized(this)
                {
                    _eventRecord.get(_eventToRemove.Type).remove(_eventToRemove.ID);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private class GetEvent extends Thread
    {
        private Event _eventToSearch;
        public Event FoundEvent;
        
        public GetEvent(Event event)
        {
            _eventToSearch = event;
        }

        public void run()
        {
            try
            {
                FoundEvent = _eventRecord.get(_eventToSearch.Type).get(_eventToSearch.ID);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private class GetEventsOfType extends Thread
    {
        private EventType _type;
        public EventList _result;

        public GetEventsOfType(EventType type)
        {
            _type = type;
        }

        public void run()
        {
            try
            {
                // MUST INITIALIZE ENTIRE VECTOR OTHERWISE CORBA FAILS
                _result = new EventList();
                _result.list = new Event[LIST_SIZE.value];
                for(int i = 0; i < LIST_SIZE.value; ++i)
                {
                    _result.list[i] = new Event();
                    _result.list[i].ID = "NONE";
                    _result.list[i].Type = EventType.Conference;
                }

                // Now search the HashMap and fill the events properly
                _result.total = 0;
                HashMap<String, Event> hash = _eventRecord.get(_type);
                for (String i : hash.keySet())
                {
                    _result.list[_result.total] = hash.get(i);
                    _result.total++;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private class GetBookedEvents extends Thread
    {
        private String _id;
        public EventList _result;

        public GetBookedEvents(String id)
        {
            _id = id;
        }

        public void run()
        {
            try
            {
                // MUST INITIALIZE ENTIRE VECTOR OTHERWISE CORBA FAILS
                _result = new EventList();
                _result.list = new Event[LIST_SIZE.value];
                for(int i = 0; i < LIST_SIZE.value; ++i)
                {
                    _result.list[i] = new Event();
                    _result.list[i].ID = "NONE";
                    _result.list[i].Type = EventType.Conference;
                }

                // Get event IDs for this customer
                String[] events = _bookedIDs.get(_id);
                if(events != null)
                {
                    // Find event objects of the IDs and add to list
                    for(int i = 0; (i < MAX_EVENTS_BOOKED) && !events[i].equals(""); ++i)
                    {
                        Event e = FindEventByID(events[i]);
                        if(e != null)
                        {
                            _result.list[i] = e;
                            _result.total++;
                        }
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
