package DEMSUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPClient
{
    public static final int MTL_UDP_PORT = 6789;
    public static final int QUE_UDP_PORT = 6790;
    public static final int SHE_UDP_PORT = 6791;


    private int _cityPort;
    private DatagramSocket _aSocket;
    private DatagramPacket _request;
    private DatagramPacket _reply;
    private String _msg;
    private byte[] _replyBuffer;
    private InetAddress _aHost;

    public UDPClient(String city)
    {
        if(city.equals("MTL"))
            _cityPort = MTL_UDP_PORT;
        else if(city.equals("QUE"))
            _cityPort = QUE_UDP_PORT;
        else if(city.equals("SHE"))
            _cityPort = SHE_UDP_PORT;

        Initialize();
    }

    public void SendMessage(String message)
    {
        // Thread the message being sent
        _msg = message;
        SendingMessage sending = new SendingMessage();
        sending.start();

        // Thread the reply
        // ReceivingReply receiving = new ReceivingReply();
        // receiving.start();
    }

    public String GetReply()
    {
        return new String(_reply.getData());
    }

    private void Initialize()
    {
        try
        {
			_aSocket = new DatagramSocket();
            _aHost = InetAddress.getByName("localHost");
        }
        catch (SocketException e)
        {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        catch (UnknownHostException e)
        {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void Shutdown()
    {
        _aSocket.close();
    }

    public void WaitForReply()
    {
        _replyBuffer = new byte[2048];
        _reply = new DatagramPacket(_replyBuffer, _replyBuffer.length);
        
        try
        {
            _aSocket.receive(_reply);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            _aSocket.close();
        }
    }

    private class SendingMessage extends Thread
    {
        public SendingMessage()
        {}

        public void run()
        {
            try
            {
                _request = new DatagramPacket(_msg.getBytes(), _msg.length(), _aHost, _cityPort);
                _aSocket.send(_request);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}