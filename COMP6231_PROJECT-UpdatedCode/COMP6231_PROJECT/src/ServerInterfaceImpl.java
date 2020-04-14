

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import InterfaceApp.ServerInterface;
import InterfaceApp.ServerInterfaceHelper;
import InterfaceApp.ServerInterfacePOA;

public class ServerInterfaceImpl extends ServerInterfacePOA {
    
    private ORB orb;
    private static HashMap<String, HashMap<String, Event>> eventRecords;
    private static HashMap<String, Event> subEventRecords;
    private String serverName;
    private static HashMap<String, ArrayList<String>> customerToEvent;
    
    
    
   public void setORB(ORB orb_val) {
       orb = orb_val;
   }
   
   
   public ServerInterfaceImpl(String serverName) {
       this.serverName = serverName;
       Logging("CORBA Server "+this.serverName+" has been initiated!");
       eventRecords = new HashMap<String, HashMap<String,Event>>();
       subEventRecords = new HashMap<String, Event>();
       customerToEvent = new HashMap<String, ArrayList<String>>();
   }
   
    
   public String addEvent(String eventType, String eventID, int bookingCapacity) {
        
       eventType = checkEventType(eventType);
       
       if (eventRecords.containsKey(eventType) && subEventRecords.containsKey(eventID)) {
           subEventRecords.get(eventID).increaseBookingCapacity(bookingCapacity);
           
           Logging("\nEvent "+eventID+" is already in the database and booking capacity has been updated and current booking capacity is : "+subEventRecords.get(eventID).bookingCapacity);
           System.out.println("\nEvent "+eventID+" is already in the database and booking capacity has been updated and current booking capacity is : "+subEventRecords.get(eventID).bookingCapacity);
           return "true";
       }
       
       
       else {
           if(eventRecords.containsKey(eventType)){
               
               subEventRecords = eventRecords.get(eventType);
               subEventRecords.put(eventID,new Event(eventType, eventID, bookingCapacity));

               eventRecords.put(eventType , subEventRecords);
               
               
           }else{
               
               subEventRecords = new HashMap<String, Event>();
               subEventRecords.put(eventID, new Event(eventType, eventID, bookingCapacity)); 
               eventRecords.put(eventType, subEventRecords);
              
           }
           
           Logging("\nEvent "+eventID+" has been added.\n Current database for "+this.serverName+" \n "+this.eventRecords);
           System.out.println("\nEvent "+eventID+" has been added.\n Current database for "+this.serverName+" \n "+this.eventRecords);
           return "true";
           
       }
          
    }
   
   
   public String removeEvent(String eventType,String eventID) {
       String success = "false";
       
       if(!this.eventRecords.containsKey(eventType) || !this.eventRecords.get(eventType).containsKey(eventID)) {
           
           System.err.println("\nNo deletion to be performed! Event "+eventID+" is not in database\nCurrent database \n"+this.eventRecords);
           Logging("\nNo deletion to be performed! Event "+eventID+" is not in database\nCurrent database \n"+this.eventRecords);
           success = "false";
       }
       else {
          
           this.eventRecords.get(eventType).remove(eventID);
           Logging("Event with ID "+eventID+" has been removed from server "+this.serverName+".\n Current database \n "+this.eventRecords);
           System.out.println("\nEvent with ID "+eventID+" has been removed from server "+this.serverName+".\n Current database \n "+this.eventRecords);
           success = "true";
           
       }
       
       return success;
   }
   
   
   
   public String bookEvent(String customerID,String eventID,String eventType){
       
       
       
       if(!eventRecords.containsKey(eventType) || !eventRecords.get(eventType).containsKey(eventID)) {
           System.err.println("There is no such event to book!");
           Logging("\nNo event with ID "+eventID+" to book!");
           return "false";
       }
       
       else if(eventRecords.get(eventType).get(eventID).bookingCapacity <= 0) {
           System.err.println("\nNo available seats for this event "+eventID);
           Logging("\nNo available seats for event "+eventID);
           return "false";
       }
       
       
       
       else if( customerToEvent.containsKey(customerID) && customerToEvent.get(customerID).contains(eventID)) {
           System.err.println("Customer with ID "+customerID+" already booked for event "+eventID);
           Logging("\nCustomer with ID "+customerID+ " already booked for the event "+eventID);
           return "false";
       }
       
       else {
           
           if(!customerToEvent.containsKey(customerID)) {
               customerToEvent.put(customerID, new ArrayList<String>());
           }
           customerToEvent.get(customerID).add(eventID);
           customerToEvent.put(customerID,customerToEvent.get(customerID));
           int curentCapacity = eventRecords.get(eventType).get(eventID).bookingCapacity -= 1;
           System.out.println("\nEvent with ID "+eventID+" has been booked by customer with ID "+customerID+" from server "+serverName+ 
                   "\n Current capacity for event "+eventID+" is "+curentCapacity+" \n Current list of customer to event is "+customerToEvent);
           Logging("\nEvent with ID "+eventID+" has been booked by customer with ID "+customerID+" to server "+serverName+ 
                   "\n Current capacity for event "+eventID+" is "+curentCapacity+" \n Current list of customer to event is "+customerToEvent);
         
           return "true";
       }
   }
   
   
   public String cancelEvent(String customerID, String eventID, String eventType) throws NullPointerException {
       try {
       
       if(!customerToEvent.get(customerID).contains(eventID)) {
           System.err.println("\nThere is no such event to cancel!");
           Logging("\nThere is no such event to cancel");
           return "false";
       }
       
       else {
           customerToEvent.get(customerID).remove(eventID);
           eventRecords.get(eventType).get(eventID).bookingCapacity = eventRecords.get(eventType).get(eventID).bookingCapacity + 1;
           Logging("\nEvent "+eventID+" has been canceled by customer"+customerID+" Current list of events for customer "+customerID+ " \n "+customerToEvent.get(customerID));
           System.out.println("Event "+eventID+" has been canceled by customer"+customerID+" Current list of events for customer "+customerID+ " \n "+customerToEvent.get(customerID));
           return "true";
       }
       }catch (NullPointerException e) {
        System.err.println("\nThis is a null pointer exception");
        return "false";
    }
       
       
   }
   
   public String getBookingSchedule(String customerID) {
       
       try {
       String[] bookingSchedule = new String[customerToEvent.get(customerID).size()];
       for (int i = 0 ; i < customerToEvent.get(customerID).size(); i++) {
           bookingSchedule[i] = customerToEvent.get(customerID).get(i);
       }
       return Arrays.toString(bookingSchedule);
       }catch (NullPointerException e) {
        System.out.println("false");
        return "false";
    }
       
   }
   
   
   
   public String swapEvent(String customerID, String newEventID, String newEventType, String oldEventID,String oldEventType) throws NullPointerException {
       try {
       if(customerToEvent.get(customerID).contains(oldEventID)) {
           if(bookEvent(customerID, newEventID, newEventType).equalsIgnoreCase("true")) {
               cancelEvent(customerID, oldEventID, oldEventType);
               System.out.println("\n\nCustomer "+customerID+" has swapped events\n\n");
               return "true";
           }
       }
       
       else {
           System.err.println("Customer "+customerID+" doesn't conatain event "+oldEventID);
           Logging("Customer "+customerID+" doesn't conatain event "+oldEventID);
           return "false";
       }
       }catch (NullPointerException e) {
           
           System.err.println("This is a null pointer exception");
           return "false";
    }
       return "false";
       
   }
   
   

   public String listEventAvailability(String eventType) {
       
       int counter = 0 ;
       for(Event value : eventRecords.get(eventType).values()) { 
           counter += value.bookingCapacity;
       }
       
       
       return Integer.toString(counter);
   }
   
   
   public void Logging(String message) {
       
       Logger logger = Logger.getLogger("logger"+this.serverName);
      //to not print on the console
      logger.setUseParentHandlers(false);
      FileHandler fh;
      try {
          fh = new FileHandler("C:\\Users\\Ahmed\\eclipse-workspace\\CorbaServer\\logs-for-servers\\"+this.serverName+".log",true);
          logger.addHandler(fh);
          SimpleFormatter formatter = new SimpleFormatter();
          fh.setFormatter(formatter);
          logger.info(message);
          fh.close();
          
      } catch (SecurityException e) {
          System.err.println(e.getMessage());
      }catch(IOException e) {
          
          System.err.println(e.getMessage());
      }
      
  }
   
   
   public String checkEventType(String eventType) {
       String temp = eventType.substring(0, 1);
       if(temp.equalsIgnoreCase("C"))
           eventType = "Conference";
       else if(temp.equalsIgnoreCase("S"))
           eventType = "Seminar";
       else eventType = "Trade Show";
       
       return eventType;    
   }

    public static void main(String[] args) {
        try {
            ORB orb = ORB.init(args, null);
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();
            
            ServerInterfaceImpl MTLServer = new ServerInterfaceImpl("MTLServer");
            MTLServer.setORB(orb);
            
            ServerInterfaceImpl QUEServer = new ServerInterfaceImpl("QUEServer");
            QUEServer.setORB(orb);
            
            ServerInterfaceImpl SHEServer = new ServerInterfaceImpl("SHEServer");
            SHEServer.setORB(orb);
            
            
            org.omg.CORBA.Object ref_1 = rootpoa.servant_to_reference(MTLServer);
            ServerInterface href_1 = ServerInterfaceHelper.narrow(ref_1);
            
            org.omg.CORBA.Object ref_2 = rootpoa.servant_to_reference(QUEServer);
            ServerInterface href_2 = ServerInterfaceHelper.narrow(ref_2);
            
            org.omg.CORBA.Object ref_3 = rootpoa.servant_to_reference(SHEServer);
            ServerInterface href_3 = ServerInterfaceHelper.narrow(ref_3);
            
            org.omg.CORBA.Object objRef_1 = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef_1);
            
            
            
            NameComponent path_1[] = ncRef.to_name(MTLServer.serverName); 
            NameComponent path_2[] = ncRef.to_name(QUEServer.serverName);
            NameComponent path_3[] = ncRef.to_name(SHEServer.serverName);
            
            
            ncRef.rebind(path_1, href_1);
            ncRef.rebind(path_2, href_2);
            ncRef.rebind(path_3, href_3);
            
            System.out.println("MTLServer is runing ... ");
            System.out.println("QUEServer is runing ... ");
            System.out.println("SHEServer is runing ... ");
            
            for (;;) {
                orb.run();
            }
           
            
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        
        
        
        

    }


   

}
