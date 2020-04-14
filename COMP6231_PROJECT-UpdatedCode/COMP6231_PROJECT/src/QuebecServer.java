

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;


import EMSApp.ServerInterface;
import EMSApp.ServerInterfaceHelper;
import Entity.Event;
import Entity.LoggerFile;


public class QuebecServer {
	
	public static void main(String args[]) {
		QuebecObj servant = new QuebecObj();
	

		Runnable task = () -> {
			server(args);
		};
		Runnable task2 = () -> {
			try {
				server_udp(servant);
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception  e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};

		Thread thread = new Thread(task);
		Thread thread2 = new Thread(task2);

		thread.start();
		thread2.start();

	}

	public static void server_udp(QuebecObj servant ){
		LoggerFile log = new LoggerFile();
		System.out.println("QUEBEC UDP SERVER STARTED ON " + 8887);
		log.logger.setLevel(Level.INFO);
		log.logger.info("QUEBEC UDP SERVER STARTED ON " + 8887);
		try {
			while (true) {
				DatagramSocket mySocket = new DatagramSocket(8887);
				DatagramPacket reply;

				// instantiates a datagram socket for receiving the data
				byte[] buffer = new byte[1000];
				DatagramPacket datagram = new DatagramPacket(buffer, buffer.length);
				mySocket.receive(datagram);
				String message = (new String(datagram.getData()).trim());
				System.out.println("message " + message);
				log.logger.setLevel(Level.INFO);
				log.logger.info("Quebec server received request "+message+ " from the server with port "+datagram.getPort());
				
				HashMap<String, HashMap<String, Integer>> eventList = servant.eventList;
				HashMap<String, Integer> conferenceList = servant.conferenceList;
				HashMap<String, Integer> seminalList = servant.seminalList;
				HashMap<String, Integer> tradeshowList = servant.tradeshowList;
				
				String replycon = "";
				if (message.contains(",")) {
					String customerId = message.split(",")[0];
					String eventId = message.split(",")[1];
					String eventType = message.split(",")[2];
					int check = servant.findEvent(customerId, eventId);
					if (eventType.equalsIgnoreCase("conference")) {
						if (conferenceList.containsKey(eventId.toUpperCase())) {
							if (conferenceList.get(eventId.toUpperCase()) >= 1) {
								if (servant.eventBook.containsKey(customerId.toUpperCase()) && check == -1) {
									int caps = conferenceList.get(eventId.toUpperCase());
									caps -= 1;
									conferenceList.put(eventId.toUpperCase(), caps);
									servant.eventBook.get(customerId.toUpperCase()).add(new Event(customerId.toUpperCase(),
											eventId.toUpperCase(), eventType.toUpperCase()));
									replycon = "true";
									log.logger.setLevel(Level.INFO);
									log.logger.info(customerId.toUpperCase()
											+ " successfully booked an event of event ID " + eventId.toUpperCase());
								} else if (!servant.eventBook.containsKey(customerId.toUpperCase())) {
									int caps = conferenceList.get(eventId.toUpperCase());
									caps -= 1;
									conferenceList.put(eventId.toUpperCase(), caps);
									List<Event> add = new ArrayList<Event>();
									servant.eventBook.put(customerId, add);
									servant.eventBook.get(customerId.toUpperCase()).add(new Event(customerId.toUpperCase(),
											eventId.toUpperCase(), eventType.toUpperCase()));
									replycon = "true";
									log.logger.setLevel(Level.INFO);
									log.logger.info(customerId.toUpperCase()
											+ " successfully booked an event of event ID " + eventId.toUpperCase());
								} else {
									replycon = "false";
									log.logger.setLevel(Level.INFO);
									log.logger.info("Event booking by " + customerId.toUpperCase() + " of event ID "
											+ eventId.toUpperCase() + " was unsuccessfull ");
								}
							} else {
								replycon = "false";
								log.logger.setLevel(Level.INFO);
								log.logger.info("Booking of event with ID " + eventId.toUpperCase() + "for customer ID "
										+ customerId.toUpperCase() + " was unsuccessfull ");
							}
						} else {
							replycon = "false";
							log.logger.setLevel(Level.INFO);
							log.logger.info("Event booking of event ID " + eventId + "does not exist, Failed!");
						}
					} else if (eventType.equalsIgnoreCase("seminal")) {
						if (seminalList.containsKey(eventId.toUpperCase())) {
							if (seminalList.get(eventId.toUpperCase()) >= 1) {
								if (servant.eventBook.containsKey(customerId.toUpperCase()) && check == -1) {
									int caps = seminalList.get(eventId.toUpperCase());
									caps -= 1;
									seminalList.put(eventId.toUpperCase(), caps);
									servant.eventBook.get(customerId.toUpperCase()).add(new Event(customerId.toUpperCase(),
											eventId.toUpperCase(), eventType.toUpperCase()));
									replycon = "true";
									log.logger.setLevel(Level.INFO);
									log.logger.info(customerId.toUpperCase()
											+ " successfully booked an event of event ID " + eventId.toUpperCase());
								} else if (!servant.eventBook.containsKey(customerId.toUpperCase())) {
									int caps = servant.seminalList.get(eventId.toUpperCase());
									caps -= 1;
									seminalList.put(eventId.toUpperCase(), caps);
									List<Event> add = new ArrayList<Event>();
									servant.eventBook.put(customerId, add);
									servant.eventBook.get(customerId.toUpperCase()).add(new Event(customerId.toUpperCase(),
											eventId.toUpperCase(), eventType.toUpperCase()));
									replycon = "true";
									log.logger.setLevel(Level.INFO);
									log.logger.info(customerId.toUpperCase()
											+ " successfully booked an event of event ID " + eventId.toUpperCase());

								} else {
									replycon = "false";
									log.logger.setLevel(Level.INFO);
									log.logger.info("Event booking by " + customerId.toUpperCase() + " of event ID "
											+ eventId.toUpperCase() + " was unsuccessfull ");
								}
							} else {
								replycon = "false";
								log.logger.setLevel(Level.INFO);
								log.logger.info("Booking of event with ID " + eventId.toUpperCase() + "for customer ID "
										+ customerId.toUpperCase() + " was unsuccessfull ");
							}
						} else {
							replycon = "false";
							log.logger.setLevel(Level.INFO);
							log.logger.info("Event booking of event ID " + eventId + "does not exist, Failed!");
						}
					} else if (eventType.equalsIgnoreCase("tradeshow")) {
						if (tradeshowList.containsKey(eventId.toUpperCase())) {
							if (tradeshowList.get(eventId.toUpperCase()) >= 1) {
								if (servant.eventBook.containsKey(customerId.toUpperCase()) && check == -1) {
									int caps = tradeshowList.get(eventId.toUpperCase());
									caps -= 1;
									tradeshowList.put(eventId.toUpperCase(), caps);
									servant.eventBook.get(customerId.toUpperCase()).add(new Event(customerId.toUpperCase(),
											eventId.toUpperCase(), eventType.toUpperCase()));
									replycon = "true";
									log.logger.setLevel(Level.INFO);
									log.logger.info(customerId.toUpperCase()
											+ " successfully booked an event of event ID " + eventId.toUpperCase());
								} else if (!servant.eventBook.containsKey(customerId.toUpperCase())) {
									int caps = tradeshowList.get(eventId.toUpperCase());
									caps -= 1;
									tradeshowList.put(eventId, caps);
									List<Event> add = new ArrayList<Event>();
									servant.eventBook.put(customerId, add);
									servant.eventBook.get(customerId.toUpperCase()).add(new Event(customerId.toUpperCase(),
											eventId.toUpperCase(), eventType.toUpperCase()));
									replycon = "true";
									log.logger.setLevel(Level.INFO);
									log.logger.info(customerId.toUpperCase()
											+ " successfully booked an event of event ID " + eventId.toUpperCase());
								} else {
									replycon = "false";
									log.logger.setLevel(Level.INFO);
									log.logger.info("Event booking by " + customerId.toUpperCase() + " of event ID "
											+ eventId.toUpperCase() + " was unsuccessfull ");
								}
							} else {
								replycon = "false";
								log.logger.setLevel(Level.INFO);
								log.logger.info("Booking of event with ID " + eventId.toUpperCase() + "for customer ID "
										+ customerId.toUpperCase() + " was unsuccessfull ");
							}
						} else {
							replycon = "false";
							log.logger.setLevel(Level.INFO);
							log.logger.info("Event booking of event ID " + eventId + "does not exist, Failed!");
						}
					}

					byte[] reply_con = replycon.getBytes();
					reply = new DatagramPacket(reply_con, reply_con.length, datagram.getAddress(), datagram.getPort());
					log.logger.setLevel(Level.INFO);
					log.logger.info("Quebec Server sending back response "+replycon+ "to Server with port "+ reply.getPort());
					mySocket.send(reply);
				}
				if (message.contains(":")) {
					String customerId = message.split(":")[0];
					String eventId = message.split(":")[1];
					String eventType = message.split(":")[2];
					int check = servant.findEvent(customerId, eventId);
					if (eventType.equalsIgnoreCase("conference")) {
						if (servant.eventBook.containsKey(customerId.toUpperCase()) && check != -1) {
							int index = servant.findEvent(customerId, eventId);
							servant.eventBook.get(customerId).remove(index);
							conferenceList.put(eventId.toUpperCase(),
									servant.conferenceList.get(eventId.toUpperCase()) + 1);
							replycon = "true";
							log.logger.setLevel(Level.INFO);
							log.logger.info("Event booked by customer ID " + customerId.toUpperCase()
							+ " was cancelled successfully");
						} else {
							replycon = "false";
							log.logger.setLevel(Level.INFO);
							log.logger.info("Event booking cancellation was not succesfull because the event ID "
									+ eventId.toUpperCase() + " does not exist");
						}
					} else if (eventType.equalsIgnoreCase("seminal")) {
						if (servant.eventBook.containsKey(customerId.toUpperCase()) && check != -1) {
							int index = servant.findEvent(customerId, eventId);
							servant.eventBook.get(customerId).remove(index);
							seminalList.put(eventId.toUpperCase(), servant.seminalList.get(eventId.toUpperCase()) + 1);
							replycon = "true";
							log.logger.setLevel(Level.INFO);
							log.logger.info("Event booked by customer ID " + customerId.toUpperCase()
							+ " was cancelled successfully");
						} else {
							replycon = "false";
							log.logger.setLevel(Level.INFO);
							log.logger.info("Event booking cancellation was not succesfull because the event ID "
									+ eventId.toUpperCase() + " does not exist");
						}
					} else if (eventType.equalsIgnoreCase("tradeshow")) {
						if (servant.eventBook.containsKey(customerId.toUpperCase()) && check != -1) {
							int index = servant.findEvent(customerId, eventId);
							servant.eventBook.get(customerId).remove(index);
							tradeshowList.put(eventId.toUpperCase(),
									servant.tradeshowList.get(eventId.toUpperCase()) + 1);
							replycon = "true";
							log.logger.setLevel(Level.INFO);
							log.logger.info("Event booked by customer ID " + customerId.toUpperCase()
							+ " was cancelled successfully");
						} else {
							replycon = "false";
							log.logger.setLevel(Level.INFO);
							log.logger.info("Event booking cancellation was not succesfull because the event ID "
									+ eventId.toUpperCase() + " does not exist");
						}
					}
					byte[] reply_con = replycon.getBytes();
					reply = new DatagramPacket(reply_con, reply_con.length, datagram.getAddress(), datagram.getPort());
					log.logger.setLevel(Level.INFO);
					log.logger.info("Quebec Server sending back response "+replycon+ "to Server with port "+ reply.getPort());
					mySocket.send(reply);

				}
				if (message.contains("Swap")) {
					String customerId = message.split(" ")[1];
					String newEventId = message.split(" ")[2];
					String newEventType = message.split(" ")[3];
					String oldEventId = message.split(" ")[4];
					String oldEventType = message.split(" ")[5];
					int check = servant.findEvent(customerId, oldEventId);
					int check2 = servant.findEvent(customerId,newEventId);
					if (servant.eventBook.containsKey(customerId.toUpperCase()) && check != -1) {
						if(newEventId.substring(0, 3).equalsIgnoreCase("que")) {
							if(eventList.get(newEventType.toUpperCase()).containsKey(newEventId.toUpperCase()) && eventList.get(newEventType.toUpperCase()).get(newEventId.toUpperCase())>=1  && check2 == -1) {
							int index = check;
							servant.eventBook.get(customerId).remove(index);
							eventList.get(oldEventType.toUpperCase()).put(oldEventId.toUpperCase(), eventList.get(oldEventType.toUpperCase()).get(oldEventId.toUpperCase()) + 1);
							int caps = eventList.get(newEventType.toUpperCase()).get(newEventId.toUpperCase());
							caps -= 1;
							eventList.get(newEventType.toUpperCase()).put(newEventId.toUpperCase(), caps);
							servant.eventBook.get(customerId.toUpperCase()).add(new Event(customerId.toUpperCase(),
									newEventId.toUpperCase(), newEventType.toUpperCase()));
							replycon = "true";
						}
						else {
							replycon = "false";
						}
						}
						else if(newEventId.substring(0, 3).equalsIgnoreCase("mtl")) {
							replycon = sendRequest(8886, customerId + "," + newEventId + "," + newEventType);
							if (replycon.contains("Your")) {
								int index = check;
								servant.eventBook.get(customerId).remove(index);
								//Que.eventList.get(newEventType.toUpperCase()).put(oldEventId.toUpperCase(), Que.eventList.get(newEventType.toUpperCase()).get(oldEventId.toUpperCase()) + 1);	
							}	
						}else if(newEventId.substring(0, 3).equalsIgnoreCase("she")) {
							replycon = sendRequest(8888, customerId + "," + newEventId + "," + newEventType);
							if (replycon.contains("Your")) {
								int index = check;
								servant.eventBook.get(customerId).remove(index);
								//Que.eventList.get(newEventType.toUpperCase()).put(oldEventId.toUpperCase(), Que.eventList.get(newEventType.toUpperCase()).get(oldEventId.toUpperCase()) + 1);	
							}
						}else {
							replycon = "false";
						}
						}else {
							replycon = "false";
						}
					byte[] reply_con = replycon.getBytes();
					reply = new DatagramPacket(reply_con, reply_con.length, datagram.getAddress(), datagram.getPort());
					mySocket.send(reply);
				}
				if (message.contains("L-")) {
					String eventType = message.split("-")[1];
					String s = sendRequest(8888, "L-" + eventType);
					replycon = eventList.get(eventType.toUpperCase()).toString();
					replycon = replycon+" "+s;
					byte[] reply_con = replycon.getBytes();
					reply = new DatagramPacket(reply_con, reply_con.length, datagram.getAddress(), datagram.getPort());
					mySocket.send(reply);
				}
				if (message.contains("X-")) {
					String eventType = message.split("-")[1];
					replycon = eventList.get(eventType.toUpperCase()).toString();
					byte[] reply_con = replycon.getBytes();
					reply = new DatagramPacket(reply_con, reply_con.length, datagram.getAddress(), datagram.getPort());
					mySocket.send(reply);
				}
				if(message.contains("V")) {
					replycon = "";
					String customerId = message.split(" ")[1];
					String s = QuebecServer.sendRequest(8888, "V " + customerId);
					if (servant.eventBook.containsKey(customerId)) {
						for (int i = 0; i < servant.eventBook.get(customerId).size(); i++) {
							replycon += servant.eventBook.get(customerId).get(i).getEventId() + " "
									+ servant.eventBook.get(customerId).get(i).getEventType() + "\n";
					}
					}
					else {
						replycon = "";
					}
					replycon = replycon+s;
					byte[] reply_con = replycon.getBytes();
					reply = new DatagramPacket(reply_con, reply_con.length, datagram.getAddress(), datagram.getPort());
					mySocket.send(reply);
				}	
				if(message.contains("P")) {
					replycon = "";
					String customerId = message.split(" ")[1];
					if (servant.eventBook.containsKey(customerId)) {
						for (int i = 0; i < servant.eventBook.get(customerId).size(); i++) {
							replycon += servant.eventBook.get(customerId).get(i).getEventId() + " "
									+ servant.eventBook.get(customerId).get(i).getEventType() + "\n";
					}
					}
					else {
						replycon = "";
					}
					byte[] reply_con = replycon.getBytes();
					reply = new DatagramPacket(reply_con, reply_con.length, datagram.getAddress(), datagram.getPort());
					mySocket.send(reply);
				}	
				mySocket.close();
			}
		} // end try
		catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public static void server(String[] args) {
		try {
			ORB orb = ORB.init(args, null);
			POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootpoa.the_POAManager().activate();
			QuebecObj queserver = new QuebecObj();
			queserver.setORB(orb);
			org.omg.CORBA.Object ref = rootpoa.servant_to_reference(queserver);
			ServerInterface href = ServerInterfaceHelper.narrow(ref);
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			NameComponent path[] = ncRef.to_name("QUE");
			ncRef.rebind(path, href);
			System.out.println("QUEBEC Server ready and waiting ...");
			for (;;){
				  orb.run();
			      }
		}
		catch(Exception c) {
			System.err.println("ERROR: " + c);
			c.printStackTrace(System.out);
		}
		 System.out.println("QUEBEC Server Exiting ...");
	
	}

	public static String sendRequest(int port, String requests) {
		LoggerFile log = new LoggerFile();
		DatagramSocket aSocket = null;
		String res = "";
		try {
			aSocket = new DatagramSocket();
			InetAddress aHost = InetAddress.getByName("localhost");
			String req_msg = requests;
			byte[] message = new byte[1000];
			message = req_msg.trim().getBytes();
			DatagramPacket request = new DatagramPacket(message, message.length, aHost, port);
			log.logger.setLevel(Level.INFO);
			log.logger.info("Quebec Server sending this request " + message + " to server with port " + port);
			aSocket.send(request);
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			aSocket.receive(reply);
			res = new String(reply.getData());
		} catch (Exception e) {
			System.out.println("Socket: " + e.getMessage());
		}
		return res;
	}

}
