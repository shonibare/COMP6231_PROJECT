package DEMSMain.Client;

import java.util.Scanner;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import DEMSBase.DEMS;
import DEMSBase.DEMSHelper;
import DEMSBase.Event;
import DEMSBase.EventType;
import DEMSBase.EventList;
import DEMSUtil.EventUtil;

public class TATestCase
{
    static DEMS demsImpl;
    public static void main(String[] args)
    {
        // Create and initialize orb
        ORB orb = ORB.init(args, null);
        String id = "";
        String name = "";

        try
        {
            // Get the root naming context
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");

            // Use NamingContextExt
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            // Login as SHEM3456
            name = "SHE";
            id = "SHEM3456";
            demsImpl = DEMSHelper.narrow(ncRef.resolve_str(name));
            demsImpl.AddEvent("SHEE150620", EventType.Conference, 2, id);
            demsImpl.AddEvent("SHEE160620", EventType.Seminar, 1, id);

            // Login as MTLM9000
            name = "MTL";
            id = "MTLM9000";
            demsImpl = DEMSHelper.narrow(ncRef.resolve_str(name));
            demsImpl.AddEvent("MTLA160620", EventType.Conference, 2, id);
            demsImpl.AddEvent("MTLA150620", EventType.TradeShow, 1, id);
            demsImpl.AddEvent("MTLA170620", EventType.TradeShow, 1, id);

            // Login as QUEM9000
            name = "QUE";
            id = "QUEM9000";
            demsImpl = DEMSHelper.narrow(ncRef.resolve_str(name));
            demsImpl.AddEvent("QUEA150620", EventType.Conference, 1, id);
            demsImpl.AddEvent("QUEA160620", EventType.Seminar, 1, id);

            // Login as QUEC1234
            id = "QUEC1234";
            name = "SHE";
            demsImpl = DEMSHelper.narrow(ncRef.resolve_str(name));
            demsImpl.BookEvent(id, "SHEE150620", EventType.Conference);
            demsImpl.BookEvent(id, "SHEE160620", EventType.Seminar);
            name = "MTL";
            demsImpl = DEMSHelper.narrow(ncRef.resolve_str(name));
            demsImpl.BookEvent(id, "MTLA160620", EventType.Conference);
            name = "QUE";
            demsImpl = DEMSHelper.narrow(ncRef.resolve_str(name));
            demsImpl.BookEvent(id, "QUEA150620", EventType.Conference);

            // Login as SHEC1234
            name = "MTL";
            id = "SHEC1234";
            demsImpl = DEMSHelper.narrow(ncRef.resolve_str(name));
            demsImpl.BookEvent(id, "MTLA170620", EventType.TradeShow);

            // Login as QUEM6785
            name = "QUE";
            id = "QUEM6785";
            EventList events = demsImpl.ListEventAvailability(EventType.Conference, id);
            System.out.println("CONFERENCE EVENTS: " + events.total);
            for(int i = 0; i < events.total; i++)
            {
                int b = (events.list[i].Capacity - events.list[i].TotalBooked);
                System.out.println("ID: " + events.list[i].ID + " " + b);
            }
            System.out.println();

            events = demsImpl.ListEventAvailability(EventType.Seminar, id);
            System.out.println("SEMINAR EVENTS: " + events.total);
            for(int i = 0; i < events.total; i++)
            {
                int b = (events.list[i].Capacity - events.list[i].TotalBooked);
                System.out.println("ID: " + events.list[i].ID + " " + b);
            }
            System.out.println();

            events = demsImpl.ListEventAvailability(EventType.TradeShow, id);
            System.out.println("TRADE SHOW EVENTS: " + events.total);
            for(int i = 0; i < events.total; i++)
            {
                int b = (events.list[i].Capacity - events.list[i].TotalBooked);
                System.out.println("ID: " + events.list[i].ID + " " + b);
            }
            System.out.println();
        }
        catch(InvalidName e)
        {
            e.printStackTrace();
        }
        catch(NotFound e)
        {
            e.printStackTrace();
        }
        catch (CannotProceed e)
        {
            e.printStackTrace();
        }
        catch (org.omg.CORBA.ORBPackage.InvalidName e)
        {
            e.printStackTrace();
        }
    }
}