package DEMSBase;


/**
* DEMSBase/_DEMSStub.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from DEMSInterface.idl
* Sunday, March 1, 2020 10:16:59 PM PST
*/

public class _DEMSStub extends org.omg.CORBA.portable.ObjectImpl implements DEMSBase.DEMS
{


  // * Event Manager Functions * //
  public int AddEvent (String eventID, DEMSBase.EventType eventType, int eventCapacity, String managerID)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("AddEvent", true);
                $out.write_string (eventID);
                DEMSBase.EventTypeHelper.write ($out, eventType);
                $out.write_long (eventCapacity);
                $out.write_string (managerID);
                $in = _invoke ($out);
                int $result = $in.read_long ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return AddEvent (eventID, eventType, eventCapacity, managerID        );
            } finally {
                _releaseReply ($in);
            }
  } // AddEvent

  public int RemoveEvent (String eventID, DEMSBase.EventType eventType, String managerID)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("RemoveEvent", true);
                $out.write_string (eventID);
                DEMSBase.EventTypeHelper.write ($out, eventType);
                $out.write_string (managerID);
                $in = _invoke ($out);
                int $result = $in.read_long ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return RemoveEvent (eventID, eventType, managerID        );
            } finally {
                _releaseReply ($in);
            }
  } // RemoveEvent

  public DEMSBase.EventList ListEventAvailability (DEMSBase.EventType eventType, String managerID)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("ListEventAvailability", true);
                DEMSBase.EventTypeHelper.write ($out, eventType);
                $out.write_string (managerID);
                $in = _invoke ($out);
                DEMSBase.EventList $result = DEMSBase.EventListHelper.read ($in);
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return ListEventAvailability (eventType, managerID        );
            } finally {
                _releaseReply ($in);
            }
  } // ListEventAvailability


  // * Customer Functions * //
  public int BookEvent (String customerID, String eventID, DEMSBase.EventType eventType)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("BookEvent", true);
                $out.write_string (customerID);
                $out.write_string (eventID);
                DEMSBase.EventTypeHelper.write ($out, eventType);
                $in = _invoke ($out);
                int $result = $in.read_long ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return BookEvent (customerID, eventID, eventType        );
            } finally {
                _releaseReply ($in);
            }
  } // BookEvent

  public DEMSBase.EventList GetBookingSchedule (String customerID)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("GetBookingSchedule", true);
                $out.write_string (customerID);
                $in = _invoke ($out);
                DEMSBase.EventList $result = DEMSBase.EventListHelper.read ($in);
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return GetBookingSchedule (customerID        );
            } finally {
                _releaseReply ($in);
            }
  } // GetBookingSchedule

  public int CancelEvent (String customerID, String eventID, DEMSBase.EventType eventType)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("CancelEvent", true);
                $out.write_string (customerID);
                $out.write_string (eventID);
                DEMSBase.EventTypeHelper.write ($out, eventType);
                $in = _invoke ($out);
                int $result = $in.read_long ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return CancelEvent (customerID, eventID, eventType        );
            } finally {
                _releaseReply ($in);
            }
  } // CancelEvent

  public int SwapEvent (String customerID, String newEventID, DEMSBase.EventType newEventType, String oldEventID, DEMSBase.EventType oldEventType)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("SwapEvent", true);
                $out.write_string (customerID);
                $out.write_string (newEventID);
                DEMSBase.EventTypeHelper.write ($out, newEventType);
                $out.write_string (oldEventID);
                DEMSBase.EventTypeHelper.write ($out, oldEventType);
                $in = _invoke ($out);
                int $result = $in.read_long ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return SwapEvent (customerID, newEventID, newEventType, oldEventID, oldEventType        );
            } finally {
                _releaseReply ($in);
            }
  } // SwapEvent


  // * Oneway shutdown * //
  public void shutdown ()
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("shutdown", false);
                $in = _invoke ($out);
                return;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                shutdown (        );
            } finally {
                _releaseReply ($in);
            }
  } // shutdown

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:DEMSBase/DEMS:1.0"};

  public String[] _ids ()
  {
    return (String[])__ids.clone ();
  }

  private void readObject (java.io.ObjectInputStream s) throws java.io.IOException
  {
     String str = s.readUTF ();
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     org.omg.CORBA.Object obj = orb.string_to_object (str);
     org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate ();
     _set_delegate (delegate);
   } finally {
     orb.destroy() ;
   }
  }

  private void writeObject (java.io.ObjectOutputStream s) throws java.io.IOException
  {
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     String str = orb.object_to_string (this);
     s.writeUTF (str);
   } finally {
     orb.destroy() ;
   }
  }
} // class _DEMSStub