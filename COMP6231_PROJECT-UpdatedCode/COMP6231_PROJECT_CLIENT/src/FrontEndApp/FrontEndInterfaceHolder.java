package FrontEndApp;

/**
* FrontEndApp/FrontEndInterfaceHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from C:/Users/Shonibare/eclipse-workspace/COMP6231_PROJECT/src/FrontEndInterface
* Sunday, 12 April 2020 18:57:33 o'clock WAT
*/

public final class FrontEndInterfaceHolder implements org.omg.CORBA.portable.Streamable
{
  public FrontEndApp.FrontEndInterface value = null;

  public FrontEndInterfaceHolder ()
  {
  }

  public FrontEndInterfaceHolder (FrontEndApp.FrontEndInterface initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = FrontEndApp.FrontEndInterfaceHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    FrontEndApp.FrontEndInterfaceHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return FrontEndApp.FrontEndInterfaceHelper.type ();
  }

}
