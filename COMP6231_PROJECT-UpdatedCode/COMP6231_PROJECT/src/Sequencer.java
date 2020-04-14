import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;


public class Sequencer {

	public static void main(String[] args) {

		System.out.println("UDP SERVER STARTED ON " + 1234);
		List input = new ArrayList();
	       
        try {
            DatagramSocket server_socket = new DatagramSocket(1234);
            MulticastSocket client_m_socket = new MulticastSocket();
            InetAddress group = InetAddress.getByName("225.4.5.6");
            byte[] in_bytes = new byte[1024];
            //byte[] out_bytes = new byte[1024];
            int i = 0;
            System.out.println("Sequencer is running...");
           
            while(true) {
               
                DatagramPacket p_in = new DatagramPacket(in_bytes, in_bytes.length);
                server_socket.receive(p_in);
                
                ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(in_bytes));
                input = (List) in.readObject();
                input.add(new String(""+i));
                
                System.out.println("request" + input.toString());
                
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(baos);
               
                out.writeObject(input);
               
                byte[] bytes = baos.toByteArray();
                
                DatagramPacket p_out = new DatagramPacket(bytes, bytes.length, group, 3456);
                client_m_socket.send(p_out);
                
                i++;
               
            }
           
           
           
        } catch (SocketException e) {
            e.printStackTrace();
        }
         catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	

}
