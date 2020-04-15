package DEMSBase;


/**
* DEMSBase/DEMSHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from DEMSInterface.idl
* Sunday, March 1, 2020 10:16:59 PM PST
*/

abstract public class DEMSHelper
{
  private static String  _id = "IDL:DEMSBase/DEMS:1.0";

  public static void insert (org.omg.CORBA.Any a, DEMSBase.DEMS that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static DEMSBase.DEMS extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (DEMSBase.DEMSHelper.id (), "DEMS");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static DEMSBase.DEMS read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_DEMSStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, DEMSBase.DEMS value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static DEMSBase.DEMS narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof DEMSBase.DEMS)
      return (DEMSBase.DEMS)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      DEMSBase._DEMSStub stub = new DEMSBase._DEMSStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static DEMSBase.DEMS unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof DEMSBase.DEMS)
      return (DEMSBase.DEMS)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      DEMSBase._DEMSStub stub = new DEMSBase._DEMSStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}
