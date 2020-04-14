package DEMSMain.Client;

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

public class DEMSSwapEventTestMTL
{
    static DEMS demsImpl;
    public static void main(String[] args)
    {
        // Create and initialize orb
        ORB orb = ORB.init(args, null);
        String id = "MTLM0001";
        String customerID = "MTLC0004";

        try
        {
            // get the root naming context
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");

            // Use NamingContextExt
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            // Resolve the object reference in naming
            String name = "MTL";
            demsImpl = DEMSHelper.narrow(ncRef.resolve_str(name));

            /* Do stuff here */
            String eventID = EventUtil.MakeID("MTL", "A", "290220");
            demsImpl.AddEvent(eventID, EventType.Seminar, 5, id);
            eventID = EventUtil.MakeID("MTL", "A", "290220");
            demsImpl.AddEvent(eventID, EventType.Seminar, 6, id);
            eventID = EventUtil.MakeID("MTL", "E", "290220");
            demsImpl.AddEvent(eventID, EventType.Seminar, 10, id);
            eventID = EventUtil.MakeID("MTL", "M", "290220");
            demsImpl.AddEvent(eventID, EventType.Seminar, 1, id);

            // List
            EventList events = demsImpl.ListEventAvailability(EventType.Seminar, id);
            System.out.println("TOTAL SEMINAR EVENTS: " + events.total);
            for(int i = 0; i < events.total; i++)
            {
                System.out.println(events.list[i].ID);
            }
            System.out.println();
            System.out.println("CUSTOMER START");

            eventID = EventUtil.MakeID("MTL", "A", "290220");
            if(demsImpl.BookEvent(customerID, eventID, EventType.Seminar) == 0)
            {
                System.out.println("Booked successfully");
            }

            events = demsImpl.GetBookingSchedule(customerID);
            System.out.println("TOTAL EVENTS BOOKED: " + events.total);
            for(int i = 0; i < events.total; i++)
            {
                System.out.println(events.list[i].ID);
            }

            /*if(demsImpl.CancelEvent(customerID, eventID, EventType.Seminar) == 0)
            {
                System.out.println("Canceled successfully");
            }*/
			
			String newEventID = EventUtil.MakeID("QUE", "E", "010320");
			eventID = EventUtil.MakeID("MTL", "A", "290220");
			if(demsImpl.SwapEvent(customerID, newEventID, EventType.Seminar, eventID, EventType.Seminar) == 0)
			{
				System.out.println("Swapped event successfully!");
			}
			else
			{
				System.out.println("Check log!");
			}
			
			events = demsImpl.GetBookingSchedule(customerID);
            System.out.println("TOTAL EVENTS BOOKED: " + events.total);
            for(int i = 0; i < events.total; i++)
            {
                System.out.println(events.list[i].ID);
            }

            /* events = demsImpl.ListEventAvailability(EventType.Conference, "MTLM0001");
            System.out.println("TOTAL CONFERENCE EVENTS: " + events.total);
            for(int i = 0; i < events.total; i++)
            {
                System.out.println(events.list[i].ID);
            }
            System.out.println(); */

            // Delete one seminar event
            /* id = EventUtil.MakeID("MTL", "E", "290220");
            demsImpl.RemoveEvent(id, EventType.Seminar, "MTLM0001");

            events = demsImpl.ListEventAvailability(EventType.Seminar, "MTLM0001");
            System.out.println("TOTAL SEMINAR EVENTS: " + events.total);
            for(int i = 0; i < events.total; i++)
            {
                System.out.println(events.list[i].ID);
            } */
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
