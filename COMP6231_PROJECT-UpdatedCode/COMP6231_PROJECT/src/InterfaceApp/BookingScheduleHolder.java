package InterfaceApp;


/**
* InterfaceApp/BookingScheduleHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Interface.idl
* Friday, April 10, 2020 8:48:58 AM PDT
*/

public final class BookingScheduleHolder implements org.omg.CORBA.portable.Streamable
{
  public String value[] = null;

  public BookingScheduleHolder ()
  {
  }

  public BookingScheduleHolder (String[] initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = InterfaceApp.BookingScheduleHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    InterfaceApp.BookingScheduleHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return InterfaceApp.BookingScheduleHelper.type ();
  }

}
