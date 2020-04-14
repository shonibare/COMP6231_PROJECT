package InterfaceApp;


/**
* InterfaceApp/ServerInterfacePOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Interface.idl
* Monday, April 13, 2020 10:18:36 AM PDT
*/

public abstract class ServerInterfacePOA extends org.omg.PortableServer.Servant
 implements InterfaceApp.ServerInterfaceOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("addEvent", new java.lang.Integer (0));
    _methods.put ("removeEvent", new java.lang.Integer (1));
    _methods.put ("bookEvent", new java.lang.Integer (2));
    _methods.put ("cancelEvent", new java.lang.Integer (3));
    _methods.put ("getBookingSchedule", new java.lang.Integer (4));
    _methods.put ("swapEvent", new java.lang.Integer (5));
    _methods.put ("listEventAvailability", new java.lang.Integer (6));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // InterfaceApp/ServerInterface/addEvent
       {
         String eventType = in.read_string ();
         String eventID = in.read_string ();
         int bookingCapacity = in.read_long ();
         String $result = null;
         $result = this.addEvent (eventType, eventID, bookingCapacity);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 1:  // InterfaceApp/ServerInterface/removeEvent
       {
         String eventType = in.read_string ();
         String eventID = in.read_string ();
         String $result = null;
         $result = this.removeEvent (eventType, eventID);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 2:  // InterfaceApp/ServerInterface/bookEvent
       {
         String customerID = in.read_string ();
         String eventID = in.read_string ();
         String eventType = in.read_string ();
         String $result = null;
         $result = this.bookEvent (customerID, eventID, eventType);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 3:  // InterfaceApp/ServerInterface/cancelEvent
       {
         String customerID = in.read_string ();
         String eventID = in.read_string ();
         String eventType = in.read_string ();
         String $result = null;
         $result = this.cancelEvent (customerID, eventID, eventType);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 4:  // InterfaceApp/ServerInterface/getBookingSchedule
       {
         String customerID = in.read_string ();
         String $result = null;
         $result = this.getBookingSchedule (customerID);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 5:  // InterfaceApp/ServerInterface/swapEvent
       {
         String customerID = in.read_string ();
         String newEventID = in.read_string ();
         String newEventType = in.read_string ();
         String oldEventID = in.read_string ();
         String oldEventType = in.read_string ();
         String $result = null;
         $result = this.swapEvent (customerID, newEventID, newEventType, oldEventID, oldEventType);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 6:  // InterfaceApp/ServerInterface/listEventAvailability
       {
         String eventType = in.read_string ();
         String $result = null;
         $result = this.listEventAvailability (eventType);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:InterfaceApp/ServerInterface:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public ServerInterface _this() 
  {
    return ServerInterfaceHelper.narrow(
    super._this_object());
  }

  public ServerInterface _this(org.omg.CORBA.ORB orb) 
  {
    return ServerInterfaceHelper.narrow(
    super._this_object(orb));
  }


} // class ServerInterfacePOA