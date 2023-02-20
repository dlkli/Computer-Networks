import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

public class TCPClient {
    public static void main(String[] args) {
        String host = "pi.cs.oswego.edu";
        int echoServicePortNumber = 26900;
        Socket echoSocket;
        DataInputStream in = null;
        DataOutputStream out = null;
        try {
            echoSocket = new Socket(host, echoServicePortNumber);
            out = new DataOutputStream(echoSocket.getOutputStream());
            in = new DataInputStream(echoSocket.getInputStream());
            long e1, e2, e3, e4, e5, e6, e7;
            e1 = e2 = e3 = e4 = e5 = e6 = e7 = 0;
            for (int i = 0; i < 100; i++) {
                //8bytes data, 8bytes ack
                ByteBuffer buf = ByteBuffer.allocate(8);
                buf.putLong(1);
                long start = System.nanoTime();
                out.write(buf.array());
                in.read(buf.array());
                long end = System.nanoTime();
                e1 += (end - start);

                //64bytes data, 64bytes ack
                buf = ByteBuffer.allocate(64);
                start = System.nanoTime();
                out.write(buf.array());
                in.read(buf.array());
                end = System.nanoTime();
                e2 += (end - start);

                //256bytes data, 256bytes ack
                buf = ByteBuffer.allocate(256);
                start = System.nanoTime();
                out.write(buf.array());
                in.read(buf.array());
                end = System.nanoTime();
                e3 += (end - start);

                //1024bytes data, 1024bytes ack
                buf = ByteBuffer.allocate(1024);
                start = System.nanoTime();
                out.write(buf.array());
                in.read(buf.array());
                end = System.nanoTime();
                e4 += (end - start);
            }
            //1Mb data, 8bytes ack
            ByteBuffer respond = ByteBuffer.allocate(8);
            ByteBuffer buf;
            long start = System.nanoTime();
            //1024bytes, 1024 times
            for (int j = 0; j < 1024; j++) {
                 buf = ByteBuffer.allocate(1024);
                out.write(buf.array());
                in.read(respond.array());
            }
            long end = System.nanoTime();
            e5 += ((end - start));

            start = System.nanoTime();
            //512bytes, 2048 times
            for (int j = 0; j < 2048; j++) {
                buf = ByteBuffer.allocate(512);
                out.write(buf.array());
                in.read(respond.array());
            }
            end = System.nanoTime();
            e6 += ((end - start));

            start = System.nanoTime();
            //256 bytes, 4096 times
            for (int j = 0; j < 4096; j++) {
                buf = ByteBuffer.allocate(256);
                out.write(buf.array());
                in.read(respond.array());
            }
            end = System.nanoTime();
            e7 += ((end - start));

            try {
                echoSocket.close();
                System.out.println("TCP Communication Closed");
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("TCP Results");
            System.out.println("Average 8 byte time: " + e1 / 100 + " ns");
            System.out.println("Average 64 byte time: " + e2 / 100 + " ns");
            System.out.println("Average 256 byte time: " + e3 / 100 + " ns");
            System.out.println("Average 1024 byte time: " + e4 / 100 + " ns");
            System.out.println("1024 byte throughput: " + 8/(e5/1e+9) + "Mbps");
            System.out.println("512 byte throughput: " + 8/(e6/1e+9) + "Mbps");
            System.out.println("256 byte throughput: " + 8/(e7/1e+9) + "Mbps");
        } catch (IOException ex) {
            System.err.println(("IO failure."));
            ex.printStackTrace();
        }
    }
}