package DEMSBase;

/**
* DEMSBase/EventTypeHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from DEMSInterface.idl
* Sunday, March 1, 2020 10:16:59 PM PST
*/

public final class EventTypeHolder implements org.omg.CORBA.portable.Streamable
{
  public DEMSBase.EventType value = null;

  public EventTypeHolder ()
  {
  }

  public EventTypeHolder (DEMSBase.EventType initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = DEMSBase.EventTypeHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    DEMSBase.EventTypeHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return DEMSBase.EventTypeHelper.type ();
  }

}
