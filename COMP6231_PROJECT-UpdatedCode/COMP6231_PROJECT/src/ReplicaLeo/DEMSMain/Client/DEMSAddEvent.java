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

public class DEMSAddEvent
{
    static DEMS demsImpl;
    public static void main(String[] args)
    {
        // Create and initialize orb
        ORB orb = ORB.init(args, null);
        String id = "";
        String server = "";
        String eventID = "";
        EventType eventType = EventType.Conference;
        int eventCapacity = 0;
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

            System.out.print("Event ID: ");
            eventID = input.nextLine();
            System.out.println();

            System.out.print("Event Type: ");
            eventType  = EventUtil.MakeTypeFromString(input.nextLine());
            System.out.println();

            System.out.print("Capacity: ");
            eventCapacity  = input.nextInt();
            System.out.println();

            int result = demsImpl.AddEvent(eventID, eventType, eventCapacity, id);
            if(result == 0)
            {
                System.out.println("New event added successfully!");
            }
            else if(result == 1)
            {
                System.out.println("Event updated successfully!");
            }
            else if(result == -1)
            {
                System.out.println("An error occurred!");
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