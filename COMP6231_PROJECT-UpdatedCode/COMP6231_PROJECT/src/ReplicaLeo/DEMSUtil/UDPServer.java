package DEMSUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public abstract class UDPServer
{
    protected DatagramSocket _aSocket;
    protected byte[] _buffer;
    protected DatagramPacket _request;
    protected DatagramPacket _reply;
    protected int _cityPort;

    public UDPServer(String city)
    {
        if(city.equals("MTL"))
            _cityPort = UDPClient.MTL_UDP_PORT;
        else if(city.equals("QUE"))
            _cityPort = UDPClient.QUE_UDP_PORT;
        else if(city.equals("SHE"))
            _cityPort = UDPClient.SHE_UDP_PORT;
        
        _aSocket = null;
    }

    public void Init()
    {
        WaitForMessage wait = new WaitForMessage();
        wait.start();
    }

    // Override this to handle messages as necessary
    public abstract void HandleMessage();

    private class WaitForMessage extends Thread
    {
        public WaitForMessage()
        {}

        public void run()
        {
            try
            {
                _aSocket = new DatagramSocket(_cityPort);
                _buffer = new byte[2048];

                while(true)
                {
                    _request = new DatagramPacket(_buffer, _buffer.length);
                    _aSocket.receive(_request);
                    HandleMessage();
                }
            }
            catch (SocketException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
				e.printStackTrace();
			}
        }
    }
}