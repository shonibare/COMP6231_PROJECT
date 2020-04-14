package DEMSUtil;

import DEMSBase.Event;
import DEMSBase.EventType;

public class EventUtil
{
    // Static method that makes an ID string based on the parameters
    public static String MakeID(String city, String timeSlot, String date)
    {
        return city+timeSlot+date;
    }
    
    public static EventType MakeTypeFromString(String type)
    {
        if(type.equals("Conference"))
            return EventType.Conference;
        else if(type.equals("Seminar"))
            return EventType.Seminar;
        else if(type.equals("TradeShow"))
            return EventType.TradeShow;
        else
        {
            System.out.println("NOT AN EVENT TYPE! GOT: " + type);
            return null;
        }
    }

    public static String MakeStringFromType(EventType type)
    {
        if(type == EventType.Conference)
            return  "Conference";
        else if(type == EventType.Seminar)
            return  "Seminar";
        else if(type == EventType.TradeShow)
            return  "TradeShow";
        else
        {
            System.out.println("NOT AN EVENT TYPE! GOT: " + type.toString());
            return null;
        }
    }
}
