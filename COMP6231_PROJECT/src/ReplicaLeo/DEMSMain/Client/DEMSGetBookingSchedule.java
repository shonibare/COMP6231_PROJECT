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

public class DEMSGetBookingSchedule
{
    static DEMS demsImpl;
    public static void main(String[] args)
    {
        // Create and initialize orb
        ORB orb = ORB.init(args, null);
        String id = "";
        String server = "";
        EventType eventType = EventType.Conference;
        Scanner input = new Scanner(System.in);
        EventList events;

        try
        {
            // get the root naming context
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");

            // Use NamingContextExt
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            System.out.print("Server: ");
            server = input.nextLine();
            System.out.println();

            System.out.print("User ID: ");
            id = input.nextLine();
            System.out.println();

            // Resolve the object reference in naming
            demsImpl = DEMSHelper.narrow(ncRef.resolve_str(server));

            events = demsImpl.GetBookingSchedule(id);
            System.out.println("Listing " + events.total + " events booked by " + id + " :");
            for(int i = 0; i < events.total; i++)
            {
                System.out.println("ID: " + events.list[i].ID);
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