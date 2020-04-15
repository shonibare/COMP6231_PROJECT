import java.io.ByteArrayInputStream;
import InterfaceApp.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import DEMSBase.DEMS;
import DEMSBase.DEMSHelper;
import DEMSBase.EventList;

public class ReplicaManager4 {
	
	private static NamingContextExt ncRef;
	private static Map<String,String> storage ;
	
	public static void main(String[] args) {
		storage = new HashMap<String,String>();
		
		new Thread() {
			@Override
			public void run() {
			
		    try {
		    	ORB orb = ORB.init(args, null);
			    org.omg.CORBA.Object objRef =   orb.resolve_initial_references("NameService");
			    ncRef = NamingContextExtHelper.narrow(objRef);

	            System.out.println("This is replica manager");
	            InetAddress group = InetAddress.getByName("225.4.5.6");
	            InetAddress rm_group = InetAddress.getByName("225.5.6.7");
	            MulticastSocket server_m_socket = new MulticastSocket(3456);
	            MulticastSocket client_rm_m_socket = new MulticastSocket();
	       //     MulticastSocket server_rm_m_socket = new MulticastSocket(4567);
	            server_m_socket.joinGroup(group);
	      //      server_rm_m_socket.joinGroup(rm_group);
	            
	           
	            byte[] in_bytes = new byte[1024];
	          //  byte[] bytes = new byte [1024];
	            
	            while(true) {
	                System.out.println("Hello");
	               
	                DatagramPacket p_in = new DatagramPacket(in_bytes, in_bytes.length);
	                server_m_socket.receive(p_in);
	               
	               
	               
	            //    DatagramPacket p_rm_in = new DatagramPacket(bytes, bytes.length);
	              //  server_rm_m_socket.receive(p_rm_in);
	           
	                ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(in_bytes));
	                List data = new ArrayList();
	                data = (List) in.readObject();
	               
	                System.out.println("Request from Sequencer " +data.toString());
	                
	                String seq_no = (String)data.get(data.size()-1);
	                storage.put(seq_no, ""); 
	               
	                
	                
	                DatagramPacket p_out = new DatagramPacket(p_in.getData(), p_in.getLength(), rm_group, 4567);
	                client_rm_m_socket.send(p_out);
	                
	              /**  ObjectInputStream in2 = new ObjectInputStream(new ByteArrayInputStream(bytes));
	                List data2 = new ArrayList();
	                data2 = (List) in2.readObject();
	               
	                seq_no = (String)data2.get(data2.size()-1);
	                if(!storage.containsKey(seq_no)) {
	                	storage.put(seq_no, "");
	                	String result = processsRequest(data2);
	                }
	                
	                */
	                
	                String result = processsRequest(data);
	    		    System.out.println("result " +result);
	    		    InetAddress aHost = InetAddress.getByName("localhost");
	    			byte[] message = new byte[1000];
	    			message = result.trim().getBytes();
	    			System.out.println("reply " + message);
	    			DatagramPacket request = new DatagramPacket(message, message.length, aHost, 7777);
	    			server_m_socket.send(request);
	                
	    			System.out.println("request no "+ storage.toString());
	                System.out.println("data 1 : "+data.toString());
	               // System.out.println("data 2 : "+data2.toString());
	                
	            }
	            
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (ClassNotFoundException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }catch (Exception ex) {
				ex.printStackTrace();
			}

			}
		
		}.start();

	
new Thread() {
	@Override
	public void run() {
	try {
		MulticastSocket server_rm_m_socket = new MulticastSocket(4567);
		InetAddress rm_group = InetAddress.getByName("225.5.6.7");
		server_rm_m_socket.joinGroup(rm_group);
    
     while(true) {
        
    	 byte[] bytes = new byte [1024];
    	 DatagramPacket p_rm_in = new DatagramPacket(bytes, bytes.length);
         server_rm_m_socket.receive(p_rm_in);
         
         ObjectInputStream in2 = new ObjectInputStream(new ByteArrayInputStream(bytes));
         List data2 = new ArrayList();
         data2 = (List) in2.readObject();
        
         System.out.println("Request from RMs " +data2.toString());
         
         String seq_no = (String)data2.get(data2.size()-1);
         if(!storage.containsKey(seq_no)) {
         	storage.put(seq_no, "");
         	String result = processsRequest(data2);
         }
     
     }
    
    
 } catch (SocketException e) {
     e.printStackTrace();
 }
  catch (IOException e) {
     // TODO Auto-generated catch block
     e.printStackTrace();
 } catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
	
}.start();


}

	private static String processsRequest(List request) {
		
		String result="";
		
		int no =  (int) request.get(0);
		try {
		if (no==1) {
			String managerId = "";
			String eventId = (String) request.get(1);
			String eventType = (String) request.get(2);
			int capacity = (int) request.get(3);
			String seq = (String) request.get(4);
			String func = eventId.substring(0, 3).toUpperCase();
			switch(func) {
			case "MTL":
			{
			    DEMS server = (DEMS)DEMSHelper.narrow(ncRef.resolve_str("MTL"));
                
                result = server.addEvent(eventType, eventId, capacity, managerId);
				storage.put(seq, result);
				break;
			}
			case "QUE":
			{
			//	
				DEMS server = (DEMS)DEMSHelper.narrow(ncRef.resolve_str("QUE"));
				
				result = server.addEvent(eventType, eventId, capacity, managerId);
				storage.put(seq, result);
				break;
			}
			case "SHE":
			{
			    DEMS server = (DEMS)DEMSHelper.narrow(ncRef.resolve_str("SHE"));
                
                result = server.addEvent(eventType, eventId, capacity, managerId);
				storage.put(seq, result);
				break;
			}
			default: result = "Error";
			}
			
		}else if(no==2){ 
			String managerId = "";
			String eventId = (String) request.get(1);
			String eventType = (String) request.get(2);
			String seq = (String) request.get(3);
			String func = eventId.substring(0, 3).toUpperCase();
			switch(func) {
			case "MTL":
			{
				DEMS server = DEMSHelper.narrow(ncRef.resolve_str("MTL"));
				result = server.removeEvent(eventId, eventType, managerId);
				storage.put(seq, result);
				break;
			}
			case "QUE":
			{
				DEMS server = DEMSHelper.narrow(ncRef.resolve_str("QUE"));
				result = server.removeEvent(eventId, eventType, managerId);
				storage.put(seq, result);
				break;
			}
			case "SHE":
			{
				DEMS server = DEMSHelper.narrow(ncRef.resolve_str("SHE"));
				result = server.removeEvent(eventId, eventType, managerId);
				storage.put(seq, result);
				break;
			}
			default: result = "Error";
			}
		}else if(no==3){ 
			String eventType = (String) request.get(1);
			String seq = (String) request.get(2);
				DEMS server = DEMSHelper.narrow(ncRef.resolve_str("SHE"));
				EventList list = server.listEventAvailability(eventType, managerId);

				for(int i = 0; i < list.total; i++)
				{
					result = result + " " + list.list[i].ID + " " + list.list[i].Capacity;
				}

				storage.put(seq, result);
				
		}else if(no==4){
			String customerId = (String) request.get(1);
			String eventId = (String) request.get(2);
			String eventType = (String) request.get(3);
			String seq = (String) request.get(4);
			String func = customerId.substring(0, 3).toUpperCase();
			switch(func) {
			case "MTL":
			{
				DEMS server = DEMSHelper.narrow(ncRef.resolve_str("MTL"));
				result = server.bookEvent(customerId, eventId, eventType);
				storage.put(seq, result);
				break;
			}
			case "QUE":
			{
				DEMS server = DEMSHelper.narrow(ncRef.resolve_str("QUE"));
				result = server.bookEvent(customerId, eventId, eventType);
				storage.put(seq, result);
				break;
			}
			case "SHE":
			{
				DEMS server = DEMSHelper.narrow(ncRef.resolve_str("SHE"));
				result = server.bookEvent(customerId, eventId, eventType);
				storage.put(seq, result);
				break;
			}
			default: result = "Invalid result";
			}
		}else if(no==5){ 
			String customerId = (String) request.get(1);
			String eventId = (String) request.get(2);
			String eventType = (String) request.get(3);
			String seq = (String) request.get(4);
			String func = customerId.substring(0, 3).toUpperCase();
			switch(func) {
			case "MTL":
			{
				DEMS server = DEMSHelper.narrow(ncRef.resolve_str("MTL"));
				result = server.cancelEvent(customerId, eventId, eventType);
				storage.put(seq, result);
				break;
			}
			case "QUE":
			{
				DEMS server = DEMSHelper.narrow(ncRef.resolve_str("QUE"));
				result = server.cancelEvent(customerId, eventId, eventType);
				storage.put(seq, result);
				break;
			}
			case "SHE":
			{
				DEMS server = DEMSHelper.narrow(ncRef.resolve_str("SHE"));
				result = server.cancelEvent(customerId, eventId, eventType);
				storage.put(seq, result);
				break;
			}
			default: result = "Invalid result";
			}
		}else if(no==6){ 
			String customerId = (String) request.get(1);
			String seq = (String) request.get(2);
			String func = customerId.substring(0, 3).toUpperCase();
			switch(func) {
			case "MTL":
			{
				DEMS server = DEMSHelper.narrow(ncRef.resolve_str("MTL"));
				EventList list = server.getBookingSchedule(customerId);

				for(int i = 0; i < list.total; i++)
				{
					result = result + " " + list.list[i].ID;
				}

				storage.put(seq, result);
				break;
			}
			case "QUE":
			{
				DEMS server = DEMSHelper.narrow(ncRef.resolve_str("QUE"));
				EventList list = server.getBookingSchedule(customerId);

				for(int i = 0; i < list.total; i++)
				{
					result = result + " " + list.list[i].ID;
				}

				storage.put(seq, result);
				break;
			}
			case "SHE":
			{
				DEMS server = DEMSHelper.narrow(ncRef.resolve_str("SHE"));
				EventList list = server.getBookingSchedule(customerId);

				for(int i = 0; i < list.total; i++)
				{
					result = result + " " + list.list[i].ID;
				}

				storage.put(seq, result);
				break;
			}
			default: result = "Invalid result";
			}
		}else if(no==7){ 
			String customerId = (String) request.get(1);
			String newEventId = (String) request.get(2);
			String newEventType = (String) request.get(3);
			String oldEventId = (String) request.get(4);
			String oldEventType = (String) request.get(5);
			String seq = (String) request.get(6);
			String func = customerId.substring(0, 3).toUpperCase();
			switch(func) { 
			case "MTL":
			{
				DEMS server = DEMSHelper.narrow(ncRef.resolve_str("MTL"));
				result = server.swapEvent(customerId, newEventId, newEventType,oldEventId,oldEventType);
				storage.put(seq, result);
				break;
			}
			case "QUE":
			{
				DEMS server = DEMSHelper.narrow(ncRef.resolve_str("QUE"));
				result = server.swapEvent(customerId, newEventId, newEventType,oldEventId,oldEventType);
				storage.put(seq, result);
				break;
			}
			case "SHE":
			{
				DEMS server = DEMSHelper.narrow(ncRef.resolve_str("SHE"));
				result = server.swapEvent(customerId, newEventId, newEventType,oldEventId,oldEventType);
				storage.put(seq, result);
				break;
			}
			default: result = "Invalid result";
			}
		}
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return result;
	}

	}
