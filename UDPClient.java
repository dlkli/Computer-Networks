import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

public class UDPClient {
    public static void main(String [] args) {
        DatagramSocket udpSocket;
        InetAddress address;
        String host = "pi.cs.oswego.edu";
        int port = 26900;
        try {
            udpSocket = new DatagramSocket(port);
            address = InetAddress.getByName(host);
            ByteBuffer buf;
            long e1, e2, e3, e4, e5, e6, e7;
            e1 = e2 = e3 = e4 = e5 = e6 = e7 = 0;
            for (int i = 0; i < 100; i++) {
                //8bytes data, 8bytes ack
                buf = ByteBuffer.allocate(8);
                buf.putLong(1);
                xorCipher(buf);
                DatagramPacket packet = new DatagramPacket(buf.array(), buf.array().length, address, port);
                long start = System.nanoTime();
                udpSocket.send(packet);
                packet = new DatagramPacket(buf.array(), buf.array().length);
                udpSocket.receive(packet);
                long end = System.nanoTime();
                e1 += (end - start);

                //64bytes data, 64bytes ack
                buf = ByteBuffer.allocate(64);
                xorCipher(buf);
                packet = new DatagramPacket(buf.array(), buf.array().length, address, port);
                start = System.nanoTime();
                udpSocket.send(packet);
                packet = new DatagramPacket(buf.array(), buf.array().length);
                udpSocket.receive(packet);
                end = System.nanoTime();
                e2 += (end - start);

                //256bytes data, 256bytes ack
                buf = ByteBuffer.allocate(256);
                xorCipher(buf);
                packet = new DatagramPacket(buf.array(), buf.array().length, address, port);
                start = System.nanoTime();
                udpSocket.send(packet);
                packet = new DatagramPacket(buf.array(), buf.array().length);
                udpSocket.receive(packet);
                end = System.nanoTime();
                e3 += (end - start);

                //1024bytes data, 1024bytes ack
                buf = ByteBuffer.allocate(1024);
                xorCipher(buf);
                packet = new DatagramPacket(buf.array(), buf.array().length, address, port);
                start = System.nanoTime();
                udpSocket.send(packet);
                packet = new DatagramPacket(buf.array(), buf.array().length);
                udpSocket.receive(packet);
                end = System.nanoTime();
                e4 += (end - start);
            }
            //1Mb data, 8bytes ack
            buf = ByteBuffer.allocate(1024);
            long start = System.nanoTime();
            //1024bytes, 1024 times
            for (int j = 0; j < 1024;) {
                xorCipher(buf);
                DatagramPacket packet = new DatagramPacket(buf.array(), buf.array().length, address, port);
                udpSocket.send(packet);
                ByteBuffer respond = ByteBuffer.allocate(8);
                packet = new DatagramPacket(respond.array(), respond.array().length);
                udpSocket.receive(packet);
                xorCipher(respond);
                j++;
            }
            long end = System.nanoTime();
            e5 += (end - start);

            buf = ByteBuffer.allocate(512);
            start = System.nanoTime();
            //512bytes, 2048 times
            for (int j = 0; j < 2048; j++) {
                xorCipher(buf);
                DatagramPacket packet = new DatagramPacket(buf.array(), buf.array().length, address, port);
                udpSocket.send(packet);
                ByteBuffer respond = ByteBuffer.allocate(8);
                packet = new DatagramPacket(respond.array(), respond.array().length);
                udpSocket.receive(packet);
                xorCipher(respond);
            }
            end = System.nanoTime();
            e6 += (end - start);

            buf = ByteBuffer.allocate(256);
            start = System.nanoTime();
            //256bytes, 4096 times
            for (int j = 0; j < 4096; j++) {
                xorCipher(buf);
                DatagramPacket packet = new DatagramPacket(buf.array(), buf.array().length, address, port);
                udpSocket.send(packet);
                ByteBuffer respond = ByteBuffer.allocate(8);
                packet = new DatagramPacket(respond.array(), respond.array().length);
                udpSocket.receive(packet);
                xorCipher(respond);
            }
            end = System.nanoTime();
            e7 += (end - start);

            System.out.println("UDP Results:");
            System.out.println("Average 8 byte time: " + e1 / 100 + " ns");
            System.out.println("Average 64 byte time: " + e2 / 100 + " ns");
            System.out.println("Average 256 byte time: " + e3 / 100 + " ns");
            System.out.println("Average 1024 byte time: " + e4 / 100 + " ns");
            System.out.println("1024 byte throughput: " + 8/(e5/1e+9) + " Mbps");
            System.out.println("512 byte throughput: " + 8/(e6/1e+9) + " Mbps");
            System.out.println("256 byte throughput: " + 8/(e7/1e+9) + " Mbps");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void xorCipher (ByteBuffer bbuf) {
        long key = 8;
        bbuf.putLong(0, bbuf.getLong(0) ^ key);
    }
}