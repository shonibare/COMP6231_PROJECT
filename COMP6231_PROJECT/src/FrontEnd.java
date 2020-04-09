import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.omg.CORBA.ORB;
import FrontEndApp.FrontEndInterfacePOA;
class FrontEnd extends FrontEndInterfacePOA {

	private ORB orb;
	private  DatagramSocket client_socket = null;
	public FrontEnd() {
		
	}
		public void setORB(ORB orb_val) {
		orb = orb_val;
	}
	
	
	@Override
	public String addEvent(String managerId, String eventId, String eventType, int capacity) {
		String response = "";
		List res = new ArrayList();
		try {
            client_socket = new DatagramSocket(7777);
            InetAddress IP = InetAddress.getByName("localhost");
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
            client_socket.send(p);
            while (true) {
            byte[] buffer = new byte[1000];
			DatagramPacket datagram = new DatagramPacket(buffer, buffer.length);
			client_socket.receive(datagram);
			String message = (new String(datagram.getData()).trim());
			res.add(message);
			System.out.println("response.. " + message);
            //client_socket.close();
			if(res.size()==3) {
				response = (String) res.get(0);
				res.clear();
			}
            }
 
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		return response;
	}

	@Override
	public String removeEvent(String managerId, String eventId, String eventType) {
		String response = "";
		try {
            client_socket = new DatagramSocket(7777);
            InetAddress IP = InetAddress.getByName("localhost");
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
            client_socket.send(p);
            while (true) {
            byte[] buffer = new byte[1000];
			DatagramPacket datagram = new DatagramPacket(buffer, buffer.length);
			client_socket.receive(datagram);
			String message = (new String(datagram.getData()).trim());
			response = message;
			System.out.println("response.. " + message);
            //client_socket.close();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		return response;
	}

	@Override
	public String listEventAvailability(String eventType) {
		String response = "";
		try {
            client_socket = new DatagramSocket(7777);
            InetAddress IP = InetAddress.getByName("localhost");
            int methodId = 3;
            List data = new ArrayList();
            data.add(methodId);
            data.add(eventType);
           
           
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(baos);
           
            out.writeObject(data);
           
            byte[] bytes = baos.toByteArray();
            DatagramPacket p = new DatagramPacket(bytes, bytes.length, IP, 1234);
            client_socket.send(p);
            while (true) {
            byte[] buffer = new byte[1000];
			DatagramPacket datagram = new DatagramPacket(buffer, buffer.length);
			client_socket.receive(datagram);
			String message = (new String(datagram.getData()).trim());
			response = message;
			System.out.println("response.. " + message);
            //client_socket.close();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		return response;
	}

	@Override
	public String bookEvent(String customerId, String eventId, String eventType) {
		String response = "";
		try {
            client_socket = new DatagramSocket(7777);
            InetAddress IP = InetAddress.getByName("localhost");
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
            client_socket.send(p);
            while (true) {
            byte[] buffer = new byte[1000];
			DatagramPacket datagram = new DatagramPacket(buffer, buffer.length);
			client_socket.receive(datagram);
			String message = (new String(datagram.getData()).trim());
			response = message;
			System.out.println("response.. " + message);
           // client_socket.close();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		return response;
	}

	@Override
	public String cancelEvent(String customerId, String eventId, String evenType) {
		String response = "";
		try {
            client_socket = new DatagramSocket(7777);
            InetAddress IP = InetAddress.getByName("localhost");
            int methodId = 5;
            List data = new ArrayList();
            data.add(methodId);
            data.add(customerId);
            data.add(eventId);
            data.add(evenType);
           
           
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(baos);
           
            out.writeObject(data);
           
            byte[] bytes = baos.toByteArray();
            DatagramPacket p = new DatagramPacket(bytes, bytes.length, IP, 1234);
            client_socket.send(p);
            while (true) {
            byte[] buffer = new byte[1000];
			DatagramPacket datagram = new DatagramPacket(buffer, buffer.length);
			client_socket.receive(datagram);
			String message = (new String(datagram.getData()).trim());
			response = message;
			System.out.println("response.. " + message); 
           // client_socket.close();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		return response;
	}

	@Override
	public String getBookingSchedule(String customerId) {
		String response = "";
		try {
            client_socket = new DatagramSocket(7777);
            InetAddress IP = InetAddress.getByName("localhost");
            int methodId = 6;
            List data = new ArrayList();
            data.add(methodId);
            data.add(customerId);
           
           
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(baos);
           
            out.writeObject(data);
           
            byte[] bytes = baos.toByteArray();
            DatagramPacket p = new DatagramPacket(bytes, bytes.length, IP, 1234);
            client_socket.send(p);
            while (true) {
            byte[] buffer = new byte[1000];
			DatagramPacket datagram = new DatagramPacket(buffer, buffer.length);
			client_socket.receive(datagram);
			String message = (new String(datagram.getData()).trim());
			response = message;
			System.out.println("response.. " + message);
           // client_socket.close();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		return response;
	}

	@Override
	public String swapEvent(String customerId, String newEventId, String newEventType, String oldEventId,String oldEventType) {
		String response = "";
		try {
            client_socket = new DatagramSocket(7777);
            InetAddress IP = InetAddress.getByName("localhost");
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
            client_socket.send(p);
            while (true) {
            byte[] buffer = new byte[1000];
			DatagramPacket datagram = new DatagramPacket(buffer, buffer.length);
			client_socket.receive(datagram);
			String message = (new String(datagram.getData()).trim());
			response = message;
			System.out.println("response.. " + message);
          //  client_socket.close();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		return response;
	}


	public void shutdown() {
		orb.shutdown(false);
		
	}

}
