package DEMSBase;

/**
* DEMSBase/EventListHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from DEMSInterface.idl
* Sunday, March 1, 2020 10:16:59 PM PST
*/

public final class EventListHolder implements org.omg.CORBA.portable.Streamable
{
  public DEMSBase.EventList value = null;

  public EventListHolder ()
  {
  }

  public EventListHolder (DEMSBase.EventList initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = DEMSBase.EventListHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    DEMSBase.EventListHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return DEMSBase.EventListHelper.type ();
  }

}
