package DEMSMain.Server;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

import DEMSBase.DEMS;
import DEMSBase.DEMSHelper;
import DEMSMain.DEMSImpl;

public class DEMSServer
{
    public static void main(String args[])
    {
        // Create and initialize ORB
        ORB orb = ORB.init(args, null);
        
        try
        {
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            // create servant and register it
            // TODO: Change to the one found in servers file
            DEMSImpl demsImplMTL = new DEMSImpl("MTL");
            DEMSImpl demsImplQUE = new DEMSImpl("QUE");
            DEMSImpl demsImplSHE = new DEMSImpl("SHE");
            demsImplMTL.setORB(orb);
            demsImplQUE.setORB(orb);
            demsImplSHE.setORB(orb);

            // Get object reference from servant
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(demsImplMTL);
            org.omg.CORBA.Object ref2 = rootpoa.servant_to_reference(demsImplQUE);
            org.omg.CORBA.Object ref3 = rootpoa.servant_to_reference(demsImplSHE);
            DEMS href = DEMSHelper.narrow(ref);
            DEMS href2 = DEMSHelper.narrow(ref2);
            DEMS href3 = DEMSHelper.narrow(ref3);

            // Get root naming context
            // NameService invokes the name service
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");

            // Use NamingContextExt part of the Interroperable Naming Service (INS) specification
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            // Bind the object reference in naming
            NameComponent path[] = ncRef.to_name(demsImplMTL.GetID());
            NameComponent path2[] = ncRef.to_name(demsImplQUE.GetID());
            NameComponent path3[] = ncRef.to_name(demsImplSHE.GetID());
            ncRef.rebind(path, href);
            ncRef.rebind(path2, href2);
            ncRef.rebind(path3, href3);

            System.out.println(demsImplMTL.GetID() + " server ready and waiting...");
            System.out.println(demsImplQUE.GetID() + " server ready and waiting...");
            System.out.println(demsImplSHE.GetID() + " server ready and waiting...");

            // Wait for clients
            orb.run();
        }
        catch (InvalidName e)
        {
            e.printStackTrace();
        }
        catch (AdapterInactive e)
        {
            e.printStackTrace();
        }
        catch (ServantNotActive e)
        {
            e.printStackTrace();
        }
        catch (WrongPolicy e)
        {
            e.printStackTrace();
        }
        catch (org.omg.CosNaming.NamingContextPackage.InvalidName e)
        {
            e.printStackTrace();
        }
        catch (NotFound e)
        {
            e.printStackTrace();
        }
        catch (CannotProceed e)
        {
            e.printStackTrace();
        }
    }
}