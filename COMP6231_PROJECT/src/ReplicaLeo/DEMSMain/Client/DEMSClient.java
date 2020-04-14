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

public class DEMSClient
{
    static DEMS demsImpl;
    public static void main(String[] args)
    {
        // Create and initialize orb
        ORB orb = ORB.init(args, null);
        String id = "";
        Scanner input = new Scanner(System.in);

        try
        {
            // Get the root naming context
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");

            // Use NamingContextExt
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            // Read server name that connects to
            System.out.print("Input the city acronym: ");
            String city = input.nextLine();
            System.out.println();
            System.out.print("Input your role (M for Manager, C for Customer): ");
            String role = input.nextLine();
            if(role.equals("M") || role.equals("C"))
            {
                System.out.println();
    
                id = city+role+"7589";
                
                // Resolve the object reference in naming
                demsImpl = DEMSHelper.narrow(ncRef.resolve_str(city));
                
                /* Do stuff here */
                int in = -1;
                while(in != 0)
                {
                    if(in == -1)
                    {
                        System.out.println("Welcome to the DEMS Client! Your ID is " + id);
                        System.out.println("Please select one of the operations below " + id);
                        if(role.equals("M"))
                        {
                            System.out.println("(1) Add Event");
                            System.out.println("(2) Remove Event");
                            System.out.println("(3) List Events Availability");
                        }
                        else
                        {
                            System.out.println("(1) Book an event");
                            System.out.println("(2) Cancel an event");
                            System.out.println("(3) Swap event");
                            System.out.println("(4) List booked events");
                        }

                        in = input.nextInt();
                    }
                    else if(in == 1)
                    {
                        if(role.equals("M"))
                        {
                            String eventID = city;
                            EventType eventType = EventType.Conference;
                            int eventCapacity = 0;

                            System.out.println("Adding a new event!");
                            System.out.print("Give the event's time slot(M/A/E): ");
                            eventID = eventID + input.nextLine();
                            System.out.println();
                            System.out.print("Give the event's type(Conference/TradeShow/Seminar): ");
                            eventType = EventUtil.MakeTypeFromString(input.nextLine());
                            System.out.println();
                            System.out.print("Give the event's date(DDMMYY): ");
                            eventID = eventID + input.nextLine();
                            System.out.println();
                            System.out.print("What's the event's capacity: ");
                            eventCapacity = input.nextInt();
                            System.out.println();
                            System.out.println("Adding event with ID: " + eventID);
                            
                            demsImpl.AddEvent(eventID, eventType, eventCapacity, id);
                        }
                        else
                        {
                        }
                    }
                    else if(in == 2)
                    {
                        if(role.equals("M"))
                        {
                            
                        }
                        else
                        {
                        }
                    }
                    else if(in == 3)
                    {
                        if(role.equals("M"))
                        {
                            
                        }
                        else
                        {
                        }
                    }
                    else if(in == 4)
                    {
                        if(role.equals("M"))
                        {
                            System.out.println("Not a valid operation!");
                            in = -1;
                        }
                        else
                        {

                        }
                    }
                    else
                    {
                        System.out.println("Not a valid operation!");
                        in = -1;
                    }
                }
            }
            else
            {
                System.out.println("ERROR: NOT MANAGER NOR CUSTOMER, SIGNING OFF...");
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