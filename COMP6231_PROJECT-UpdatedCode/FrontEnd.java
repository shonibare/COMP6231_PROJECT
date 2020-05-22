import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.omg.CORBA.ORB;
import FrontEndApp.FrontEndInterfacePOA;
class FrontEnd extends FrontEndInterfacePOA {

	private ORB orb;
	private  DatagramSocket server_socket = null;
	private InetAddress IP = null;
	public FrontEnd() 
	{
		try {
		server_socket = new DatagramSocket(7777);
		IP = InetAddress.getByName("localhost");
		}catch (SocketException e) {
            e.printStackTrace();
        }catch (UnknownHostException e) {
            e.printStackTrace();
        }
	}
	
		public void setORB(ORB orb_val) {
		orb = orb_val;
	}
	
	
		private ArrayList<List> responses_list = new ArrayList();
		 private String response ="";
		 int rm1cont = 0;
		 int rm2cont = 0;
		 int rm3cont = 0;
	    @Override
	    public String addEvent(String eventId, String eventType, int capacity)
	    {
	        boolean res = false;
	       
	        try
	        {
	            int methodId = 1;
	            List data = new ArrayList();
	            data.add(methodId);
	            data.add(eventId);
	            data.add(eventType);
	            data.add(capacity);
	           
	           
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            ObjectOutputStream out = new ObjectOutputStream(baos);
	           
	            out.writeObject(data);
	           
	            byte[] bytes = baos.toByteArray();
	            DatagramPacket p = new DatagramPacket(bytes, bytes.length, IP, 1234);
	            server_socket.send(p);
	           
	           
	                      
	            server_socket.setSoTimeout(5000);
	            int counter = 0 ;
	            int RM1Cont = 0;
	            int RM2Cont = 0;
	            int RM3Cont = 0;
	            while(counter<3)
	            {
	            	byte[] buffer = new byte[1024];
	    			DatagramPacket datagram = new DatagramPacket(buffer, buffer.length);
	    			server_socket.receive(datagram);
	    			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
	    			
	    		
	    			List datatemp = new ArrayList();
	    			datatemp = (List) in.readObject();

	    			System.out.println("response.. " + datatemp);
	                responses_list.add(datatemp);
	                counter += 1;
	                
	                
	                if (counter == 3 && responses_list.size()==3)
		            {
		                res = compareResults(responses_list);
		                if (res == true)
		                {
		                    response =  "Add event successful";
		                }
		                else
		                {
		                    response =  "Add event unsuccessful";
		                }
		               }
		               
	                else
	                {
	                	//declare a counter error here
	                	for (int i = 0 ; i < responses_list.size() ; i ++)
	    	            {
	    	            	if((int) responses_list.get(i).get(1) != 1 && (int) responses_list.get(i).get(1) != 2)
	                        {
	                        	rm3cont++;
	                        	if (rm3cont == 3)
	                        	{
	                        		sendCrash(6);
	                        	}
	                        }
	    	            	else if ((int) responses_list.get(i).get(1) != 2 && (int) responses_list.get(i).get(1) != 3)
	    	            	{
	    	            		rm1cont++;
	    	            		if (rm1cont == 3)
	                        	{
	                        		sendCrash(4);
	                        	}
	    	            	}
	    	            	else if ((int) responses_list.get(i).get(1) != 1 && (int) responses_list.get(i).get(1) != 3)
	    	            	{
	    	            		rm2cont++;
	    	            		if (rm2cont == 3)
	                        	{
	                        		sendCrash(5);
	                        	}
	    	            	}
	    	            }
		            }
	            
	            }
	   
	            responses_list.clear();
	        }catch (IOException e) {
	            e.printStackTrace();
	        } catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	      
	        return response;
	       
	    }
	   
	    private void sendCrash(int crashNum) 
	    {
	    	try {
	            MulticastSocket client_rm_m_socket = new MulticastSocket();
	            InetAddress group = InetAddress.getByName("225.4.5.6");
	            byte[] out_bytes = new byte[1024];
	           
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            ObjectOutputStream out = new ObjectOutputStream(baos);
	           
	            out.writeObject(crashNum);
	           
	            out_bytes = baos.toByteArray();
	           
	            DatagramPacket p_out = new DatagramPacket(out_bytes, out_bytes.length, group, 3456);
	            client_rm_m_socket.send(p_out);
	           
	        } catch (IOException e) {
	           
	            e.printStackTrace();
	        }
			
		}

		private boolean compareResults(ArrayList<List> responses)
	    {
	       
	        int counter_true = 0;
	        int counter_false = 0;
	       
	        for(int i = 0 ; i < responses.size() ; i ++)
	        {
	            if ((boolean) responses.get(i).get(0) == true)
	            {
	                counter_true += 1;              
	            }
	            else
	            {
	                counter_false += 1;
	            }
	           
	        }
	        if (counter_true >= counter_false)
	        {
	            for (int i = 0 ; i < responses.size() ; i ++)
	            {
                    if((boolean) responses.get(i).get(0) == false)
                    {
                    	int rmNum = (int) responses.get(i).get(1);
                        sendError(rmNum);
                    }
	            }
	            return true;
	        }
	        else
	        {
	            for (int i = 0 ; i < responses.size() ; i ++)
	            {
	            	if((boolean) responses.get(i).get(0) == true)
                    {
                    	int rmNum = (int) responses.get(i).get(1);
                        sendError(rmNum);
                    }
	            }
	            return false;  
	        }
	    }
	    
	   
	    private void sendError(int rmNum)
	    {
	    	try {
	            MulticastSocket client_rm_m_socket = new MulticastSocket();
	            InetAddress group = InetAddress.getByName("225.4.5.6");
	            byte[] out_bytes = new byte[1024];
	           
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            ObjectOutputStream out = new ObjectOutputStream(baos);
	           
	            out.writeObject(rmNum);
	           
	            out_bytes = baos.toByteArray();
	           
	            DatagramPacket p_out = new DatagramPacket(out_bytes, out_bytes.length, group, 3456);
	            client_rm_m_socket.send(p_out);
	           
	        } catch (IOException e) {
	           
	            e.printStackTrace();
	        }
	       
	    }

		private void resendAdd(String eventId, String eventType, int capacity)
	    {
	        addEvent(eventId, eventType,capacity);
	    }
	 
	    @Override
	    public String removeEvent(String eventId, String eventType)
	    {
	        boolean res = false;
	        try {
	            int methodId = 2;
	            List data = new ArrayList();
	            data.add(methodId);
	            data.add(eventId);
	            data.add(eventType);
	           
	           
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            ObjectOutputStream out = new ObjectOutputStream(baos);
	           
	            out.writeObject(data);
	           
	            byte[] bytes = baos.toByteArray();
	            DatagramPacket p = new DatagramPacket(bytes, bytes.length, IP, 1234);
	            server_socket.send(p);
	           
	         
	            server_socket.setSoTimeout(5000);
	            int counter = 0 ;
	           
	            while(counter<3)
	            {
	            	byte[] buffer = new byte[1024];
	    			DatagramPacket datagram = new DatagramPacket(buffer, buffer.length);
	    			server_socket.receive(datagram);
	    			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
	    			
	    		
	    			List datatemp = new ArrayList();
	    			datatemp = (List) in.readObject();

	    			System.out.println("response.. " + datatemp);
	                responses_list.add(datatemp);
	                counter += 1;
	           
	            if (counter == 3 && responses_list.size()==3)
	            {
	               
	                res = compareResults(responses_list);
	                if (res == true)
	                {
	                    response =  "Remove event unsuccessful";
	                }
	     
	                else
	                {
	                    response =  "Remove event unsuccessful";
	                }
	            }
	            if(counter == 3 && responses_list.size()<3)
	            {
	            	//declare a counter error here
	            	resendRemove(eventId, eventType);
	            }
	            }
	            responses_list.clear();
	       
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	        
	        return response;
	    }
	    private String compareListResults(List responses_list2)
	    {
	        return (String) responses_list2.get(0);
	    }
	 
	    private void resendRemove(String eventId, String eventType)
	    {
	        removeEvent(eventId,eventType);
	       
	    }
	 
	    @Override
	    public String listEventAvailability(String eventType)
	    {
	        try {
	            
	            int methodId = 3;
	            List data = new ArrayList();
	            data.add(methodId);
	            data.add(eventType);
	           
	           
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            ObjectOutputStream out = new ObjectOutputStream(baos);
	           
	            out.writeObject(data);
	           
	            byte[] bytes = baos.toByteArray();
	            DatagramPacket p = new DatagramPacket(bytes, bytes.length, IP, 1234);
	            server_socket.send(p);
	           
	            server_socket.setSoTimeout(5000);
	            int counter = 0 ;
	           
	            while(counter<3)
	            {
	            	byte[] buffer = new byte[1024];
	    			DatagramPacket datagram = new DatagramPacket(buffer, buffer.length);
	    			server_socket.receive(datagram);
	    			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
	    			
	    		
	    			List datatemp = new ArrayList();
	    			datatemp = (List) in.readObject();

	    			System.out.println("response.. " + datatemp);
	                responses_list.add(datatemp);
	                counter += 1;
	                counter += 1;
	   
	            if (counter == 3 && responses_list.size()==3)
	            {
	               
	                return compareListResults(responses_list);
	            }
	            if(counter == 3 && responses_list.size()<3) 
	            {
	            	//declare a counter error here
	            	resendList(eventType);
	            }
	            }
	            responses_list.clear();
	        }  catch (IOException e) {
	            e.printStackTrace();
	        } catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	        
	        return response;
	    }
	    private void resendList(String eventType)
	    {
	        listEventAvailability(eventType);
	    }
	 
	    
	    @Override
	    public String bookEvent(String customerId, String eventId, String eventType)
	    {
	        boolean res = false;
	        try
	        {
	            
	            int methodId = 4;
	            List data = new ArrayList();
	            data.add(methodId);
	            data.add(customerId);
	            data.add(eventId);
	            data.add(eventType);
	           
	           
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            ObjectOutputStream out = new ObjectOutputStream(baos);
	           
	            out.writeObject(data);
	           
	            byte[] bytes = baos.toByteArray();
	            DatagramPacket p = new DatagramPacket(bytes, bytes.length, IP, 1234);
	            server_socket.send(p);
	                    
	            server_socket.setSoTimeout(5000);
	            int counter = 0 ;
	            
	            while(counter<3)
	            {
	            	byte[] buffer = new byte[1024];
	    			DatagramPacket datagram = new DatagramPacket(buffer, buffer.length);
	    			server_socket.receive(datagram);
	    			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
	    			
	    		
	    			List datatemp = new ArrayList();
	    			datatemp = (List) in.readObject();

	    			System.out.println("response.. " + datatemp);
	                responses_list.add(datatemp);
	                counter += 1;
	            
	            if (counter == 3 && responses_list.size()==3)
	            {
	               
	                res = compareResults(responses_list);
	                if (res == true)
	                {
	                    response = "Book event successful";
	                }
	                else
	                {
	                    response = "Book event unsuccessful";
	                }
	            }
	            if(counter == 3 && responses_list.size()<3)
	            {
	            	//declare a counter error here
	            	resendBook(customerId, eventId, eventType);
	            }
	            }
	            responses_list.clear();
	        }  catch (IOException e) {
	            e.printStackTrace();
	        } catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	        
	        return response;
	    }
	    private void resendBook(String customerId, String eventId, String eventType)
	    {
	       
	        bookEvent(customerId, eventId, eventType);
	    }
	 
	    @Override
	    public String getBookingSchedule(String customerId)
	    {
	        try {
	            
	            int methodId = 6;
	            List data = new ArrayList();
	            data.add(methodId);
	            data.add(customerId);
	           
	           
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            ObjectOutputStream out = new ObjectOutputStream(baos);
	           
	            out.writeObject(data);
	           
	            byte[] bytes = baos.toByteArray();
	            DatagramPacket p = new DatagramPacket(bytes, bytes.length, IP, 1234);
	            server_socket.send(p);
	                    
	            server_socket.setSoTimeout(5000);
	            int counter = 0 ;
	            
	            while(counter<3)
	            {
	            	byte[] buffer = new byte[1024];
	    			DatagramPacket datagram = new DatagramPacket(buffer, buffer.length);
	    			server_socket.receive(datagram);
	    			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
	    			
	    		
	    			List datatemp = new ArrayList();
	    			datatemp = (List) in.readObject();

	    			System.out.println("response.. " + datatemp);
	                responses_list.add(datatemp);
	                counter += 1;
	            
	            if (counter == 3 && responses_list.size()==3)
	            {
	               
	                return compareListResults(responses_list);
	            }
	            if(counter == 3 && responses_list.size()<3)
	            {
	            	//declare a counter error here
	            	resendSchedule(customerId);
	            }
	            }
	            responses_list.clear();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	        
	        return response;
	    }
	    private void resendSchedule(String customerId)
	    {
	        getBookingSchedule(customerId);    
	    }
	 
	    @Override
	    public String cancelEvent(String customerId, String eventId, String eventType)
	    {
	        boolean res = false;
	        try {
	        
	            int methodId = 5;
	            List data = new ArrayList();
	            data.add(methodId);
	            data.add(customerId);
	            data.add(eventId);
	            data.add(eventType);
	           
	           
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            ObjectOutputStream out = new ObjectOutputStream(baos);
	           
	            out.writeObject(data);
	           
	            byte[] bytes = baos.toByteArray();
	            DatagramPacket p = new DatagramPacket(bytes, bytes.length, IP, 1234);
	            server_socket.send(p);
	                     
	            server_socket.setSoTimeout(5000);
	            int counter = 0 ;
	            
	            while(counter<3)
	            {
	            	byte[] buffer = new byte[1024];
	    			DatagramPacket datagram = new DatagramPacket(buffer, buffer.length);
	    			server_socket.receive(datagram);
	    			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
	    			
	    		
	    			List datatemp = new ArrayList();
	    			datatemp = (List) in.readObject();

	    			System.out.println("response.. " + datatemp);
	                responses_list.add(datatemp);
	                counter += 1;
	            
	            if (counter == 3 && responses_list.size()==3)
	            {
	               
	                res = compareResults(responses_list);
	                if (res == true)
	                {
	                    response = "cancel event successful";
	                }
	                else
	                {
	                    response = "cancel event unsuccessful";
	                }
	            }
	            if(counter == 3 && responses_list.size()<3)
	            {
	            	//declare a counter error here
	            	resendcancel(customerId, eventId, eventType);
	            }
	            }
	            responses_list.clear();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	        
	        return response;
	    }
	    private void resendcancel(String customerId, String eventId, String eventType)
	    {
	        cancelEvent(customerId, eventId, eventType);       
	    }
	 
	    @Override
	    public String swapEvent(String customerId, String newEventId, String newEventType, String oldEventId,
	            String oldEventType)
	    {
	        boolean res = false;
	        try {
	            
	            int methodId = 7;
	            List data = new ArrayList();
	            data.add(methodId);
	            data.add(customerId);
	            data.add(newEventId);
	            data.add(newEventType);
	            data.add(oldEventId);
	            data.add(oldEventType);
	           
	           
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            ObjectOutputStream out = new ObjectOutputStream(baos);
	           
	            out.writeObject(data);
	           
	            byte[] bytes = baos.toByteArray();
	            DatagramPacket p = new DatagramPacket(bytes, bytes.length, IP, 1234);
	            server_socket.send(p);
	               
	            server_socket.setSoTimeout(5000);
	            int counter = 0 ;
	            
	            while(counter<3)
	            {
	            	byte[] buffer = new byte[1024];
	    			DatagramPacket datagram = new DatagramPacket(buffer, buffer.length);
	    			server_socket.receive(datagram);
	    			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
	    			
	    		
	    			List datatemp = new ArrayList();
	    			datatemp = (List) in.readObject();

	    			System.out.println("response.. " + datatemp);
	                responses_list.add(datatemp);
	                counter += 1;
	            
	            if (counter == 3 && responses_list.size()==3)
	            {
	               
	                res = compareResults(responses_list);
	                if (res == true)
	                {
	                    response = "swape event successful";
	                }
	                else
	                {
	                    response = "swape event unsuccessful";
	                }
	            }
	            if(counter == 3 && responses_list.size()<3) 
	            {
	            	//declare a counter error here
	            	resendswape(customerId, newEventId, newEventType, oldEventId, oldEventType);	
	            }
	            }
	            responses_list.clear();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        return response;
	    }
	    private void resendswape(String customerId, String newEventId, String newEventType, String oldEventId,
	            String oldEventType)
	    {
	        swapEvent(customerId, newEventId, newEventType, oldEventId,oldEventType);      
	    }
	 
	    @Override
	    public void shutdown() {
	        // TODO Auto-generated method stub
	       
	    }
	 
	 
	 
	}