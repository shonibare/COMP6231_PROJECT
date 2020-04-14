


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.omg.CORBA.ORB;


import EMSApp.ServerInterfacePOA;
import Entity.Event;
import Entity.LoggerFile;

 class MontrealObj extends ServerInterfacePOA {
	private ORB orb;
	public HashMap<String, HashMap<String, Integer>> eventList;
	public HashMap<String, Integer> conferenceList;
	public HashMap<String, Integer> seminalList;
	public HashMap<String, Integer> tradeshowList;
	public HashMap<String, List<Event>> eventBook;
	public HashMap<String, Integer> count;
	public Event item;
	public LoggerFile log;

	public MontrealObj() {
		log = new LoggerFile();
		eventList = new HashMap<String, HashMap<String, Integer>>();
		conferenceList = new HashMap<String, Integer>();
		seminalList = new HashMap<String, Integer>();
		tradeshowList = new HashMap<String, Integer>();
		count = new HashMap<>();
		eventBook = new HashMap<String, List<Event>>();

	//	conferenceList.put("MTLA120220", 1);
	//	conferenceList.put("MTLE130220", 2);
	//	conferenceList.put("MTLM020220", 3);

	//	seminalList.put("MTLA010220", 3);

	//	tradeshowList.put("MTLE120220", 2);

		eventList.put("CONFERENCE", conferenceList);
		eventList.put("SEMINAL", seminalList);
		eventList.put("TRADESHOW", tradeshowList);

	//	List<Event> event = new ArrayList<Event>();
	//	List<Event> event2 = new ArrayList<Event>();
	//	List<Event> event3 = new ArrayList<Event>();
	//	List<Event> event4 = new ArrayList<Event>();

	//	eventBook.put("MTLC120220", event);
	//	eventBook.put("MTLC130220", event2);
	//	eventBook.put("MTLC020220", event3);
	//	eventBook.put("MTLC010220", event4);

	//	eventBook.get("MTLC120220").add(new Event("MTLC120220", "MTLA120220", "conference".toUpperCase()));
	//	eventBook.get("MTLC120220").add(new Event("MTLC120220", "MTLE120220", "tradeshow".toUpperCase()));
	//	eventBook.get("MTLC130220").add(new Event("MTLC130220", "MTLE130220", "conference".toUpperCase()));
	//	eventBook.get("MTLC020220").add(new Event("MTLC020220", "MTLM020220", "conference".toUpperCase()));
	//	eventBook.get("MTLC010220").add(new Event("MTLC010220", "MTLA010220", "seminal".toUpperCase()));

	}

	
	public void setORB(ORB orb_val) {
		orb = orb_val;
	}
	
	@Override
	public String addEvent(String managerId, String eventId, String eventType, int capacity) {

		if (eventType.equalsIgnoreCase("conference") && eventId.substring(0, 3).equalsIgnoreCase("mtl")) {
			if (eventList.get("CONFERENCE").containsKey(eventId.toUpperCase())) {
				int caps = eventList.get("CONFERENCE").get(eventId.toUpperCase());
				caps += capacity;
				eventList.get("CONFERENCE").put(eventId.toUpperCase(), caps);
				return "true";
			} else {

				eventList.get("CONFERENCE").put(eventId.toUpperCase(), capacity);
				return "true";
			}
		} else if (eventType.equalsIgnoreCase("seminal") && eventId.substring(0, 3).equalsIgnoreCase("mtl")) {
			if (eventList.get("SEMINAL").containsKey(eventId.toUpperCase())) {
				int caps = eventList.get("SEMINAL").get(eventId.toUpperCase());
				caps += capacity;
				eventList.get("SEMINAL").put(eventId.toUpperCase(), caps);
				return "true";
			} else {

				eventList.get("SEMINAL").put(eventId.toUpperCase(), capacity);
				return "true";

			}
		} else if (eventType.equalsIgnoreCase("tradeshow") && eventId.substring(0, 3).equalsIgnoreCase("mtl")) {
			if (eventList.get("TRADESHOW").containsKey(eventId.toUpperCase())) {
				int caps = eventList.get("TRADESHOW").get(eventId.toUpperCase());
				caps += capacity;
				eventList.get("TRADESHOW").put(eventId.toUpperCase(), caps);
				return "true";
			} else {

				eventList.get("TRADESHOW").put(eventId.toUpperCase(), capacity);
				return "true";
			}
		} else {
			return "false";
		}
	}

	@Override
	public String removeEvent(String managerId, String eventId, String eventType) {
		String reply = null;
		if (eventType.equalsIgnoreCase("conference") && eventId.substring(0, 3).equalsIgnoreCase("mtl")) {
			if (eventList.get("CONFERENCE").containsKey(eventId.toUpperCase())) {
				if (eventList.get("CONFERENCE").get(eventId.toUpperCase()) > 1) {
					int caps = eventList.get("CONFERENCE").get(eventId.toUpperCase());
					caps -= 1;
					eventList.get("CONFERENCE").put(eventId.toUpperCase(), caps);
					swapEvent(eventId.toUpperCase(), eventType.toUpperCase());
					return "true";
				} else {
					eventList.get("CONFERENCE").remove(eventId.toUpperCase());
					swapEvent(eventId.toUpperCase(), eventType.toUpperCase());
					return "true";
				}
			} else {
				return "false";
			}
		} else if (eventType.equalsIgnoreCase("seminal") && eventId.substring(0, 3).equalsIgnoreCase("mtl")) {
			if (eventList.get("SEMINAL").containsKey(eventId.toUpperCase())) {
				if (eventList.get("SEMINAL").get(eventId.toUpperCase()) > 1) {
					int caps = eventList.get("SEMINAL").get(eventId.toUpperCase());
					caps -= 1;
					eventList.get("SEMINAL").put(eventId.toUpperCase(), caps);
					swapEvent(eventId.toUpperCase(), eventType.toUpperCase());
					return "true";
				} else {
					eventList.get("SEMINAL").remove(eventId.toUpperCase());
					swapEvent(eventId.toUpperCase(), eventType.toUpperCase());
					return "true";
				}
			} else {
				return "false";
			}
		} else if (eventType.equalsIgnoreCase("tradeshow") && eventId.substring(0, 3).equalsIgnoreCase("mtl")) {
			if (eventList.get("TRADESHOW").containsKey(eventId.toUpperCase())) {
				if (eventList.get("TRADESHOW").get(eventId.toUpperCase()) > 1) {
					int caps = eventList.get("TRADESHOW").get(eventId.toUpperCase());
					caps -= 1;
					eventList.get("TRADESHOW").put(eventId.toUpperCase(), caps);
					swapEvent(eventId.toUpperCase(), eventType.toUpperCase());
					return "true";
				} else {
					eventList.get("TRADESHOW").remove(eventId.toUpperCase());
					swapEvent(eventId.toUpperCase(), eventType.toUpperCase());
					return "true";
				}
			} else {
				return "false";
			}
		}
		return reply;
	}

	@Override
	public String listEventAvailability(String eventType) {
		String list = "";
		String res = eventList.get(eventType.toUpperCase()).toString();
		
		String res2 = MontrealServer.sendRequest(8887, "L-" + eventType);
		list = res+" "+res2;
		//return res.toString()+res2.toString();
		return list.replaceAll("[{\\}\\,]", "").trim();
	}

	@Override
	public String bookEvent(String customerId, String eventId, String eventType) {
		int check = findEvent(customerId, eventId);
		String res = null;
		switch (eventId.substring(0, 3).toUpperCase()) {

		case "MTL": {
			if (eventType.equalsIgnoreCase("conference")) {
				if (conferenceList.containsKey(eventId.toUpperCase())) {
					if (conferenceList.get(eventId.toUpperCase()) >= 1) {
						if (eventBook.containsKey(customerId.toUpperCase()) && check == -1) {
							int caps = conferenceList.get(eventId.toUpperCase());
							caps -= 1;
							conferenceList.put(eventId.toUpperCase(), caps);
							eventBook.get(customerId.toUpperCase()).add(new Event(customerId.toUpperCase(),
									eventId.toUpperCase(), eventType.toUpperCase()));
							return "true";
						} else if (!eventBook.containsKey(customerId.toUpperCase())) {
							int caps = conferenceList.get(eventId.toUpperCase());
							caps -= 1;
							conferenceList.put(eventId.toUpperCase(), caps);
							List<Event> add = new ArrayList<Event>();
							eventBook.put(customerId, add);
							eventBook.get(customerId.toUpperCase()).add(new Event(customerId.toUpperCase(),
									eventId.toUpperCase(), eventType.toUpperCase()));
							return "true";
						} else {
							return "false";
						}
					} else {
						return "false";
					}
				} else {
					return "false";
				}
			} else if (eventType.equalsIgnoreCase("seminal")) {
				if (seminalList.containsKey(eventId.toUpperCase())) {
					if (seminalList.get(eventId.toUpperCase()) >= 1) {
						if (eventBook.containsKey(customerId.toUpperCase()) && check == -1) {
							int caps = seminalList.get(eventId.toUpperCase());
							caps -= 1;
							seminalList.put(eventId.toUpperCase(), caps);
							eventBook.get(customerId.toUpperCase()).add(new Event(customerId.toUpperCase(),
									eventId.toUpperCase(), eventType.toUpperCase()));
							return "true";
						} else if (!eventBook.containsKey(customerId.toUpperCase())) {
							int caps = seminalList.get(eventId.toUpperCase());
							caps -= 1;
							seminalList.put(eventId.toUpperCase(), caps);
							List<Event> add = new ArrayList<Event>();
							eventBook.put(customerId, add);
							eventBook.get(customerId.toUpperCase()).add(new Event(customerId.toUpperCase(),
									eventId.toUpperCase(), eventType.toUpperCase()));
							return "true";
						} else {
							return "false";
						}
					} else {
						return "false";
					}
				} else {
					return "false";
				}
			} else if (eventType.equalsIgnoreCase("tradeshow")) {
				if (tradeshowList.containsKey(eventId.toUpperCase())) {
					if (tradeshowList.get(eventId.toUpperCase()) >= 1) {
						if (eventBook.containsKey(customerId.toUpperCase()) && check == -1) {
							int caps = tradeshowList.get(eventId.toUpperCase());
							caps -= 1;
							tradeshowList.put(eventId.toUpperCase(), caps);
							eventBook.get(customerId.toUpperCase()).add(new Event(customerId.toUpperCase(),
									eventId.toUpperCase(), eventType.toUpperCase()));
							return "true";
						} else if (!eventBook.containsKey(customerId.toUpperCase())) {
							int caps = tradeshowList.get(eventId.toUpperCase());
							caps -= 1;
							tradeshowList.put(eventId, caps);
							List<Event> add = new ArrayList<Event>();
							eventBook.put(customerId, add);
							eventBook.get(customerId.toUpperCase()).add(new Event(customerId.toUpperCase(),
									eventId.toUpperCase(), eventType.toUpperCase()));
							return "true";
						} else {
							return "false";
						}
					} else {
						return "false";
					}
				} else {
					return "false";
				}
			}

			break;
		}

		case "QUE": {
			if (!count.containsKey(customerId.toUpperCase())) {
				System.out.println("Request sent to Quebec Server to book event with ID " + eventId.toUpperCase());
				log.logger.info(
						"Montreal server sent request to Quebec Server to book event with ID " + eventId.toUpperCase());
				res = MontrealServer.sendRequest(8887, customerId + "," + eventId + "," + eventType);
				log.logger.info("Montreal server received this response " + res + " for booking event with ID "
						+ eventId.toUpperCase() + " from Quebec Server");
				if (res.contains("Your"))
					count.put(customerId.toUpperCase(), 1);
			} else if (count.containsKey(customerId.toUpperCase()) && count.get(customerId) < 3) {
				System.out.println("Request sent to Quebec Server to book event with ID " + eventId.toUpperCase());
				log.logger.info(
						"Montreal server sent request to Quebec Server to book event with ID " + eventId.toUpperCase());
				res = MontrealServer.sendRequest(8887, customerId + "," + eventId + "," + eventType);
				log.logger.info("Montreal server received this response " + res + " for booking event with ID "
						+ eventId.toUpperCase() + " from Quebec Server");
				if (res.contains("Your"))
					count.put(customerId.toUpperCase(), count.get(customerId) + 1);
			} else {
				return "false";
			}

			break;
		}

		case "SHE": {
			if (!count.containsKey(customerId.toUpperCase())) {
				System.out.println("Request sent to Shebrooke Server to book event with ID " + eventId.toUpperCase());
				log.logger.info("Montreal server sent request to Shebrooke Server to book event with ID "
						+ eventId.toUpperCase());
				res = MontrealServer.sendRequest(8888, customerId + "," + eventId + "," + eventType);
				log.logger.info("Montreal server received this response " + res + " for booking event with ID "
						+ eventId.toUpperCase() + " from Shebrooke Server");
				if (res.contains("Your"))
					count.put(customerId.toUpperCase(), 1);
			} else if (count.containsKey(customerId.toUpperCase()) && count.get(customerId) < 3) {
				System.out.println("Request sent to Shebrooke Server to book event with ID " + eventId.toUpperCase());
				log.logger.info("Montreal server sent request to Shebrooke Server to book event with ID "
						+ eventId.toUpperCase());
				res = MontrealServer.sendRequest(8888, customerId + "," + eventId + "," + eventType);
				log.logger.info("Montreal server received this response " + res + " for booking event with ID "
						+ eventId.toUpperCase() + " from Shebrooke Server");
				if (res.contains("Your"))
					count.put(customerId.toUpperCase(), count.get(customerId.toUpperCase()) + 1);
			} else {
				return "false";
			}
			break;
		}

		default:
			res = "false";
		}
		return res.trim();
	}

	@Override
	public String cancelEvent(String customerId, String eventId, String evenType) {
		int check = findEvent(customerId, eventId);
		String res = null;
		switch (eventId.substring(0, 3).toUpperCase()) {

		case "MTL": {
			if (evenType.equalsIgnoreCase("conference")) {
				if (eventBook.containsKey(customerId.toUpperCase()) && check != -1) {
					int index = findEvent(customerId, eventId);
					eventBook.get(customerId).remove(index);
					conferenceList.put(eventId.toUpperCase(), conferenceList.get(eventId.toUpperCase()) + 1);
					return "true";
				} else {
					return "false";
				}
			} else if (evenType.equalsIgnoreCase("seminal")) {
				if (eventBook.containsKey(customerId.toUpperCase()) && check != -1) {
					int index = findEvent(customerId, eventId);
					eventBook.get(customerId).remove(index);
					seminalList.put(eventId.toUpperCase(), seminalList.get(eventId.toUpperCase()) + 1);
					res = "false";
				} else {
					return "false";
				}
			} else if (evenType.equalsIgnoreCase("tradeshow")) {
				if (eventBook.containsKey(customerId.toUpperCase()) && check != -1) {
					int index = findEvent(customerId, eventId);
					eventBook.get(customerId).remove(index);
					tradeshowList.put(eventId.toUpperCase(), tradeshowList.get(eventId.toUpperCase()) + 1);
					res = "false";
				} else {
					return "false";
				}
			}
			break;
		}

		case "QUE": {

			System.out.println("Request sent to Quebec Server to cancel event with ID " + eventId.toUpperCase());
			log.logger.info(
					"Montreal server sent request to Quebec Server to cancel event with ID " + eventId.toUpperCase());
			res = MontrealServer.sendRequest(8887, customerId + ":" + eventId + ":" + evenType);
			log.logger.info("Montreal server received this response " + res + " for cancelling event with ID "
					+ eventId.toUpperCase() + " from Quebec Server");
			if (res.contains("cancelled"))
				count.put(customerId.toUpperCase(), count.get(customerId.toUpperCase()) - 1);

			break;
		}

		case "SHE": {

			System.out.println("Request sent to Shebrooke Server to cancel event with ID " + eventId.toUpperCase());
			log.logger.info("Montreal server sent request to Shebrooke Server to cancel event with ID "
					+ eventId.toUpperCase());
			res = MontrealServer.sendRequest(8888, customerId + ":" + eventId + ":" + evenType);
			log.logger.info("Montreal server sent request to Shebrooke Server to cancel event with ID "
					+ eventId.toUpperCase());
			if (res.contains("cancelled"))
				count.put(customerId.toUpperCase(), count.get(customerId) - 1);

			break;
		}
		default:
			return "false";
		}
		return res;
	}

	@Override
	public String getBookingSchedule(String customerId) {
		String mon = "";
		if (eventBook.containsKey(customerId)) {
			for (int i = 0; i < eventBook.get(customerId).size(); i++) {
				mon += eventBook.get(customerId).get(i).getEventId() + " "
						+ eventBook.get(customerId).get(i).getEventType() + "\n";
			}
			
		}else {
			mon = "";
		}
		
		String que = MontrealServer.sendRequest(8887, "V " + customerId);		
		mon = mon + que;
		return mon.replaceAll("[{\\}\\,]", "").trim();
	}

	public int findEvent(String cusid, String eventid) {
		if(eventBook.containsKey(cusid.toUpperCase())) {
		for (int i = 0; i < eventBook.get(cusid).size(); i++) {
			Event id = eventBook.get(cusid).get(i);
			if (id.getEventId().equalsIgnoreCase(eventid)) {
				return i;
			}
		}
		}
		return -1;
	}

	public void swapEvent(String eventid, String type) {
		List<String> book = new ArrayList<String>();
		List<String> cusId = new ArrayList<>(eventBook.keySet());

		for (String cus : cusId) {
			int index = findEvent(cus.toUpperCase(), eventid.toUpperCase());
			if (index != -1) {
				book.add(cus.toUpperCase());
			}
		}
		// delete eventid from its class
		if (!book.isEmpty()) {
			String avai = getAvailableEvent(type.toUpperCase());
			if (eventList.get(type.toUpperCase()).containsKey(eventid.toUpperCase())) {
				if (eventList.get(type.toUpperCase()).get(eventid.toUpperCase()) < book.size()) {
					int size = book.size() - (eventList.get(type.toUpperCase()).get(eventid.toUpperCase()));
					while (size >= 0) {
						SwapDeleteEvent(book.get(size), eventid.toUpperCase(), type.toUpperCase());
						SwapBookEvent(book.get(size), avai.toUpperCase(), type.toUpperCase());
						size--;
					}
				}
			} else {
				SwapDeleteEvent(book.get(0), eventid.toUpperCase(), type.toUpperCase());
				SwapBookEvent(book.get(0), avai.toUpperCase(), type.toUpperCase());
			}

		}
	}

	private String getAvailableEvent(String type) {
		Map.Entry<String, Integer> maxEntry = null;
		for (Map.Entry<String, Integer> entry : eventList.get(type.toUpperCase()).entrySet()) {
			if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
				maxEntry = entry;
			}
		}
		return maxEntry.getKey();
	}

	public void SwapBookEvent(String customerId, String eventId, String eventType) {
		int check = findEvent(customerId.toUpperCase(), eventId.toUpperCase());
		if (eventBook.containsKey(customerId.toUpperCase()) && check == -1) {
			int caps = eventList.get(eventType).get(eventId.toUpperCase());
			caps -= 1;
			eventList.get(eventType).put(eventId.toUpperCase(), caps);
			eventBook.get(customerId.toUpperCase())
					.add(new Event(customerId.toUpperCase(), eventId.toUpperCase(), eventType.toUpperCase()));
			// return "Your event with ID "+eventId.toUpperCase() + " has been booked!";
		} else if (!eventBook.containsKey(customerId.toUpperCase())) {
			int caps = eventList.get(eventType).get(eventId.toUpperCase());
			caps -= 1;
			eventList.get(eventType).put(eventId.toUpperCase(), caps);
			List<Event> add = new ArrayList<Event>();
			eventBook.put(customerId, add);
			eventBook.get(customerId.toUpperCase())
					.add(new Event(customerId.toUpperCase(), eventId.toUpperCase(), eventType.toUpperCase()));
			// return "Your event with ID "+eventId.toUpperCase() + " has been booked!";
		} else {
			// return "You cannot book event with this ID " + eventId.toUpperCase() + " for
			// this event type "+eventType;
		}

	}

	public void SwapDeleteEvent(String customerId, String eventId, String eventType) {
		int check = findEvent(customerId.toUpperCase(), eventId.toUpperCase());
		if (eventBook.containsKey(customerId.toUpperCase()) && check != -1) {
			int index = findEvent(customerId.toUpperCase(), eventId.toUpperCase());
			eventBook.get(customerId.toUpperCase()).remove(index);
			// return "Event with ID " +eventId.toUpperCase() + " has been cancelled";
		}
	}

	@Override
	public String swapEvent(String customerId, String newEventId, String newEventType, String oldEventId,
			String oldEventType) {
		int check = findEvent(customerId, oldEventId);
		int check2 = findEvent(customerId,newEventId);
		String res = "";
		switch (oldEventId.substring(0, 3).toUpperCase()) {

		case "MTL": {
				if (eventBook.containsKey(customerId.toUpperCase()) && check != -1) {
				if(newEventId.substring(0, 3).equalsIgnoreCase("mtl")) {
					if(eventList.get(newEventType.toUpperCase()).containsKey(newEventId.toUpperCase()) && eventList.get(newEventType.toUpperCase()).get(newEventId.toUpperCase())>=1  && check2 == -1) {
					int index = check;
					eventBook.get(customerId).remove(index);
					eventList.get(oldEventType.toUpperCase()).put(oldEventId.toUpperCase(), eventList.get(oldEventType.toUpperCase()).get(oldEventId.toUpperCase()) + 1);
					int caps = eventList.get(newEventType.toUpperCase()).get(newEventId.toUpperCase());
					caps -= 1;
					eventList.get(newEventType.toUpperCase()).put(newEventId.toUpperCase(), caps);
					eventBook.get(customerId.toUpperCase()).add(new Event(customerId.toUpperCase(),
							newEventId.toUpperCase(), newEventType.toUpperCase()));
					return "true";
				}
				else {
					return "false";
				}
				}
				else if(newEventId.substring(0, 3).equalsIgnoreCase("que")) {
					System.out.println("Request sent to Quebec Server to swap event with ID " + oldEventId.toUpperCase());
					log.logger.info("Montreal server sent request to Quebec Server to swap event with ID " + oldEventId.toUpperCase());
					res = MontrealServer.sendRequest(8887, customerId + "," + newEventId + "," + newEventType);
					log.logger.info("Montreal server received this response " + res + " for swapping event with ID "
							+ oldEventId.toUpperCase() + " from Quebec Server");
					if (res.contains("Your")) {
						int index = check;
						eventBook.get(customerId).remove(index);
						//eventList.get(newEventType.toUpperCase()).put(oldEventId.toUpperCase(), eventList.get(newEventType.toUpperCase()).get(oldEventId.toUpperCase()) + 1);	
					}	
				}else if(newEventId.substring(0, 3).equalsIgnoreCase("she")) {
					System.out.println("Request sent to Shebrook Server to swap event with ID " + oldEventId.toUpperCase());
					log.logger.info("Montreal server sent request to Shebrook Server to swap event with ID " + oldEventId.toUpperCase());
					res = MontrealServer.sendRequest(8888, customerId + "," + newEventId + "," + newEventType);
					log.logger.info("Montreal server received this response " + res + " for swapping event with ID "
							+ oldEventId.toUpperCase() + " from Shebrook Server");
					if (res.contains("Your")) {
						int index = check;
						eventBook.get(customerId).remove(index);
						//eventList.get(newEventType.toUpperCase()).put(oldEventId.toUpperCase(), eventList.get(newEventType.toUpperCase()).get(oldEventId.toUpperCase()) + 1);	
					}
				}else {
					return "false";
				}
				}else {
					return "false";
				}
			
			break;
		}
		
		case "QUE":
		{
			System.out.println("Request sent to Quebec Server to swap event with ID " + oldEventId.toUpperCase());
			log.logger.info("Montreal server sent request to Quebec Server to swap event with ID " + oldEventId.toUpperCase());
			res = MontrealServer.sendRequest(8887, "Swap "+customerId + " " + newEventId + " " + newEventType + " " + oldEventId +" "+oldEventType);
			log.logger.info("Montreal server received this response " + res + " for swapping event with ID "
					+ oldEventId.toUpperCase() + " from Quebec Server");
			break;
		}
		
		case "SHE":
		{
			System.out.println("Request sent to Shebrook Server to swap event with ID " + oldEventId.toUpperCase());
			log.logger.info("Montreal server sent request to Shebrook Server to swap event with ID " + oldEventId.toUpperCase());
			res = MontrealServer.sendRequest(8888, "Swap "+customerId + " " + newEventId + " " + newEventType + " " + oldEventId +" "+oldEventType);
			log.logger.info("Montreal server received this response " + res + " for swapping event with ID "
					+ oldEventId.toUpperCase() + " from Shebrook Server");
			break;
		}

		default: return "false";
		}
		return res.trim();
	}

	@Override
	public void shutdown() {
		orb.shutdown(false);
}
}
