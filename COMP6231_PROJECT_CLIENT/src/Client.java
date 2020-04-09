

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import FrontEndApp.FrontEndInterface;
import FrontEndApp.FrontEndInterfaceHelper;



public class Client {

	public static void main(String[] args) {

		try {
			
			ORB orb = ORB.init(args, null);
		    org.omg.CORBA.Object objRef =   orb.resolve_initial_references("NameService");
		    NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
		    Scanner s = new Scanner(System.in);
			System.out.println("Dashboard Ready...");
			System.out.println("Please enter the CustomerID or ManagerId");
			String id = s.next();
			String id_pos = id.charAt(3) + "".toUpperCase();

			if (id_pos.equalsIgnoreCase("C")) {
				
					FrontEndInterface con = (FrontEndInterface) FrontEndInterfaceHelper.narrow(ncRef.resolve_str("FRTEND"));
					userOperations(con, id);
				}
				
			else if (id_pos.equalsIgnoreCase("M")) {
				
					FrontEndInterface con = (FrontEndInterface) FrontEndInterfaceHelper.narrow(ncRef.resolve_str("FRTEND"));
					managerOperations(con, id);
				}
				
			else {
				System.out.println("Invalid ID");
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		}


	public static void userOperations(FrontEndInterface mon, String userId) throws IOException, InterruptedException {
		BufferedReader scan = new BufferedReader(new InputStreamReader(System.in));

		String choice = "";
		do {

			System.out.println("1...To book an event");
			System.out.println("2...To cancel an event");
			System.out.println("3...To get your list of booked events");
			System.out.println("4...To swap your booked events");
			System.out.println("Enter the option...");
			int input = Integer.parseInt(scan.readLine());

			switch (input) {
			case 1: {

				System.out.println("Select below options User: " + userId);
				System.out.println("Enter Event Type (options--conference,seminal,tradeshow)");
				String eventType = scan.readLine();
				System.out.println("Enter Event ID");
				String eventId = scan.readLine();
				String res = mon.bookEvent(userId, eventId, eventType);
				System.out.println(res);
				break;
			}
			case 2: {
				System.out.println("Select below options User: " + userId);
				System.out.println("Enter Event Type (options--conference,seminal,tradeshow)");
				String eventType = scan.readLine();
				System.out.println("Enter Event ID");
				String eventId = scan.readLine();
				String res = mon.cancelEvent(userId, eventId, eventType);
				System.out.println(res);
				break;
			}
			case 3: {
				System.out.println("The list of your booked events: " + userId);
				String res = mon.getBookingSchedule(userId);
				System.out.println(res);
				break;
			}
			case 4: {
				System.out.println("Select below options User: " + userId);
				System.out.println("Enter old Event Type (options--conference,seminal,tradeshow)");
				String oldEventType = scan.readLine();
				System.out.println("Enter old Event ID");
				String oldEventId = scan.readLine();
				System.out.println("Enter new Event Type (options--conference,seminal,tradeshow)");
				String newEventType = scan.readLine();
				System.out.println("Enter new Event ID");
				String newEventId = scan.readLine();
				String res = mon.swapEvent(userId, newEventId, newEventType,oldEventId,oldEventType);
				System.out.println(res);
				break;
			}
			default:
				System.out.println("Invalid choice...");
			}
			System.out.println("Do you want to contionue(y/n)..");
			choice = scan.readLine();
		} while (choice.equalsIgnoreCase("y"));
	}

	
	private static void managerOperations(FrontEndInterface she, String id)
			throws IOException, InterruptedException {
		BufferedReader scan = new BufferedReader(new InputStreamReader(System.in));
		
		String choice = "";
		do {

			System.out.println("1...Add New Event Record");
			System.out.println("2...Remove Existing Event Record ");
			System.out.println("3...List Event Record List");
			System.out.println("4...To book an event for a customer");
			System.out.println("5...To cancel an event for a customer");
			System.out.println("6...To get list of booked events by a customer");
			System.out.println("7...To swap customer's booked events");
			System.out.println("Enter your option...");
			int option = Integer.parseInt(scan.readLine());
			switch (option) {
			
			case 1: {
				System.out.println("Select below options User: " + id);
				System.out.println("Enter Event Type (options--conference,seminal,tradeshow)");
				String eventType = scan.readLine();
				System.out.println("Enter Event ID");
				String eventId = scan.readLine();
				System.out.println("Enter Event Capacity");
				int capacity = Integer.parseInt(scan.readLine());
				if (capacity > 0) {
					String res = she.addEvent(id.toUpperCase(), eventId.toUpperCase(), eventType, capacity);
					System.out.println(res);
				} else {
					System.out.println("Enter capacity value greater than 0");
				}
				break;
			}
			case 2: {
				System.out.println("Select below options User: " + id);
				System.out.println("Enter Event Type (options--conference,seminal,tradeshow)");
				String eventType = scan.readLine();
				System.out.println("Enter Event ID");
				String eventId = scan.readLine();
				String res = she.removeEvent(id.toUpperCase(), eventId.toUpperCase(), eventType);
				System.out.println(res);
				break;
			}
			case 3: {

				System.out.println("Select below options User: " + id);
				System.out.println("Enter Event Type (options--conference,seminal,tradeshow)");
				String eventType = scan.readLine();
				String eventList = she.listEventAvailability(eventType);
				System.out.println(eventList.toString());

				break;
			}
			case 4: {
				System.out.println("Select below options User: " + id);
				System.out.println("Enter Customer's ID");
				String customerId = scan.readLine();
				System.out.println("Enter Event Type (options--conference,seminal,tradeshow)");
				String eventType = scan.readLine();
				System.out.println("Enter Event ID");
				String eventId = scan.readLine();
				String res = she.bookEvent(customerId, eventId, eventType);
				System.out.println(res);
				break;
			}
			case 5: {
				System.out.println("Select below options User: " + id);
				System.out.println("Enter Customer's ID");
				String customerId = scan.readLine();
				System.out.println("Enter Event Type (options--conference,seminal,tradeshow)");
				String eventType = scan.readLine();
				System.out.println("Enter Event ID");
				String eventId = scan.readLine();
				String res = she.cancelEvent(customerId, eventId, eventType);
				System.out.println(res);
				break;
			}
			case 6: {
				System.out.println("The list of your booked events: " + id);
				System.out.println("Enter Customer's ID");
				String customerId = scan.readLine();
				String res = she.getBookingSchedule(customerId);
				System.out.println(res);
			break;
			}
			case 7: {
				System.out.println("Select below options User: " + id);
				System.out.println("Enter Customer's ID");
				String customerId = scan.readLine();
				System.out.println("Enter old Event Type (options--conference,seminal,tradeshow)");
				String oldEventType = scan.readLine();
				System.out.println("Enter old Event ID");
				String oldEventId = scan.readLine();
				System.out.println("Enter new Event Type (options--conference,seminal,tradeshow)");
				String newEventType = scan.readLine();
				System.out.println("Enter new Event ID");
				String newEventId = scan.readLine();
				String res = she.swapEvent(customerId, newEventId, newEventType,oldEventId,oldEventType);
				System.out.println(res);
				break;
			}
			default:
				System.out.println("Invalid option...");
			}
			System.out.println("Do you want to contionue(y/n)..");
			choice = scan.readLine();
		} while (choice.equalsIgnoreCase("y"));
		}

	}

	
		

	


