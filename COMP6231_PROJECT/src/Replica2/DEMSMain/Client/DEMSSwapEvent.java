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

public class DEMSSwapEvent
{
    static DEMS demsImpl;
    public static void main(String[] args)
    {
        // Create and initialize orb
        ORB orb = ORB.init(args, null);
        String id = "";
        String server = "";
        String oldEventID = "";
        String newEventID = "";
        EventType oldEventType = EventType.Conference;
        EventType newEventType = EventType.Conference;
        Scanner input = new Scanner(System.in);
        

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

            System.out.print("Old Event ID: ");
            oldEventID = input.nextLine();
            System.out.println();

            System.out.print("New Event ID: ");
            newEventID = input.nextLine();
            System.out.println();

            System.out.print("Old Event Type: ");
            oldEventType  = EventUtil.MakeTypeFromString(input.nextLine());
            System.out.println();

            System.out.print("New Event Type: ");
            newEventType  = EventUtil.MakeTypeFromString(input.nextLine());
            System.out.println();

            int result = demsImpl.SwapEvent(id, newEventID, newEventType, oldEventID, oldEventType);
            if(result == 0)
            {
                System.out.println("Event swapped successfully!");
            }
            else if(result == 1)
            {
                System.out.println("Event couldn't be swapped, event already booked by this customer.");
            }
            else if(result == 2)
            {
                System.out.println("Event couldn't be swapped, booking limit of 3 already reached by this customer on the other server.");
            }
            else if(result == -1)
            {
                System.out.println("Event couldn't be swapped, an error occurred, log must be checked.");
            }
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