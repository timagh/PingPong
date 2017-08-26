import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by User on 19.08.2017.
 */

public class Server implements Runnable {

    private DatagramSocket socket;
    public DatagramPacket packet;
    public byte[] buffer;
    public byte[] buffer2;

    private Thread thread;
    private boolean running = false;


    public synchronized void start() {
        try {
            socket = new DatagramSocket(7777);
        } catch (SocketException e) {
            System.out.println("SocketException: " + e.getMessage());
        }

        running = true;
        thread = new Thread(this, "Server");
        thread.start();
        System.out.println("server started");
    }


    public void receivePacket() {

        buffer = new byte[1024];
        packet = new DatagramPacket(buffer, buffer.length);
        try {
            socket.receive(packet);
            //System.out.println("Server has  received packet");
        } catch (IOException e) {
            System.out.println("Server Can't receive a packet : " + e.getMessage());
        }
        String string = new String(buffer, 0, packet.getLength());
        NetInput.rMoveDir = Integer.parseInt(string);


    }

    public void sendPacket() {

        buffer2 = new byte[1024];
        String returnString = Integer.toString(NetInput.sMoveDir);
        System.out.println(NetInput.sMoveDir);
        buffer2 = returnString.getBytes();
        if (buffer.equals(null)) {
            System.out.println("Sending no data");
        }
        InetAddress address = packet.getAddress();
        int port = packet.getPort();

        packet = new DatagramPacket(buffer2, buffer2.length, address, port);
        try {
            socket.send(packet);
           // System.out.println("Server has sent packet");
        } catch (IOException e) {
            System.out.println("Server Can't send a packet : " + e.getMessage());
        }


    }


    @Override
    public void run() {

        long lastTime = System.nanoTime();
        final double ns = 1_000_000_000.0 / 30.0; // частота вызова update()
        double delta = 0;

        while (running) {
            long currTime = System.nanoTime();
            delta += (currTime - lastTime) / ns;
            lastTime = currTime;


            while (delta >= 1) {
                receivePacket();
                sendPacket();
            }


        }
    }


}


