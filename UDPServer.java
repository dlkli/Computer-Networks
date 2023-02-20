import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class UDPServer {
    public static void main(String[] args) {
        DatagramSocket udpSocket;
        int portNumber = 26900;
        String host = "pi.cs.oswego.edu";
        InetAddress address;
        try {
            udpSocket = new DatagramSocket(portNumber);
            address = InetAddress.getByName(host);
            ByteBuffer buf = ByteBuffer.allocate(8);
            DatagramPacket udpPacket = new DatagramPacket(buf.array(), buf.array().length, address, portNumber);
            for (int i = 0; i < 100; i++) {
                System.out.println("we tryna receive");
                udpSocket.receive(udpPacket);
                xorCipher(buf);
                if (buf.getLong() != 1) {
                    System.out.println("Invalid message");
                }
                udpPacket = new DatagramPacket(buf.array(), buf.array().length, address, portNumber);
                udpSocket.send(udpPacket);

                System.out.println("PAST FIRST MESSAGE");

                buf = ByteBuffer.allocate(64);
                udpPacket = new DatagramPacket(buf.array(), buf.array().length, address, portNumber);
                udpSocket.receive(udpPacket);
                xorCipher(buf);
                udpPacket = new DatagramPacket(buf.array(), buf.array().length, address, portNumber);
                udpSocket.send(udpPacket);

                buf = ByteBuffer.allocate(256);
                udpPacket = new DatagramPacket(buf.array(), buf.array().length, address, portNumber);
                udpSocket.receive(udpPacket);
                xorCipher(buf);
                udpPacket = new DatagramPacket(buf.array(), buf.array().length, address, portNumber);
                udpSocket.send(udpPacket);

                buf = ByteBuffer.allocate(1024);
                udpPacket = new DatagramPacket(buf.array(), buf.array().length, address, portNumber);
                udpSocket.receive(udpPacket);
                xorCipher(buf);
                udpPacket = new DatagramPacket(buf.array(), buf.array().length, address, portNumber);
                udpSocket.send(udpPacket);
                System.out.println("PAST 1024");
            }

            buf = ByteBuffer.allocate(1024);
            for (int j = 0; j < 1024; j++) {
                udpPacket = new DatagramPacket(buf.array(), buf.array().length);
                udpSocket.receive(udpPacket);
                xorCipher(buf);
                ByteBuffer response = ByteBuffer.allocate(8);
                InetAddress address1 = udpPacket.getAddress();
                xorCipher(response);
                udpPacket = new DatagramPacket(response.array(), response.array().length, address1, portNumber);
                udpSocket.send(udpPacket);
            }

            System.out.println("DONE 1024 1024");

            for (int j = 0; j < 2048; j++) {
                buf = ByteBuffer.allocate(512);
                udpPacket = new DatagramPacket(buf.array(), buf.array().length);
                udpSocket.receive(udpPacket);
                xorCipher(buf);
                ByteBuffer response = ByteBuffer.allocate(8);
                xorCipher(response);
                udpPacket = new DatagramPacket(response.array(), response.array().length, udpPacket.getAddress(), portNumber);
                udpSocket.send(udpPacket);
            }

            System.out.println("DONE 2048 512");

            for (int j = 0; j < 4096; j++) {
                buf = ByteBuffer.allocate(256);
                udpPacket = new DatagramPacket(buf.array(), buf.array().length);
                udpSocket.receive(udpPacket);
                xorCipher(buf);
                ByteBuffer response = ByteBuffer.allocate(8);
                xorCipher(response);
                udpPacket = new DatagramPacket(response.array(), response.array().length, udpPacket.getAddress(), portNumber);
                udpSocket.send(udpPacket);
            }

            System.out.println("DONE 4096 256");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    static void xorCipher (ByteBuffer bbuf) {
        long key = 8;
        bbuf.putLong(0, bbuf.getLong(0) ^ key);
    }
}