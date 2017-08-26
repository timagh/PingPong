import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class Client implements Runnable {

    private DatagramSocket socket;
    private InetAddress address;
    public DatagramPacket packet;
    public byte[] buffer;
    public byte[] buffer2;

    private static Thread thread;
    private static boolean running = false;


    public synchronized void start() {
        try {
            address = InetAddress.getByName("localhost");  // getByName()
            socket = new DatagramSocket();
        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        }

        running = true;
        thread = new Thread(this, "Client");
        thread.start();


    }


    public void sendPacket() {

        buffer2 = new byte[1024];
        String returnString = Integer.toString(NetInput.sMoveDir);
        buffer2 = returnString.getBytes();

        DatagramPacket packet = new DatagramPacket(buffer2, buffer2.length, address, 7777);

        try {
            socket.send(packet);
            //System.out.println("Client has send packet");
        } catch (IOException e) {
            System.out.println("Client Can't sent a packet : " + e.getMessage());
        }


    }

    public void receivePacket() {


        buffer = new byte[1024];
        packet = new DatagramPacket(buffer, buffer.length);

        try {
            socket.receive(packet);
           // System.out.println("Client has  received packet");
        } catch (IOException e) {
            System.out.println("Client Can't receive a packet : " + e.getMessage());
        }
        String string = new String(buffer, 0, packet.getLength());
        NetInput.rMoveDir = Integer.parseInt(string);


    }


    @Override
    public void run() {

        long lastTime = System.nanoTime();
        final double ns = 1_000_000_000.0 / 60.0; // частота вызова update()
        double delta = 0;

        while (running) {
            long currTime = System.nanoTime();
            delta += (currTime - lastTime) / ns;

            lastTime = currTime;

            while (delta >= 1) {
                sendPacket();
                receivePacket();
            }


        }
    }


}
