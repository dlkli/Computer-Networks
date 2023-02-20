import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

public class TCPServer {
    public static void main(String[] args) {
        int portNumber = 26900;
        DataInputStream in;
        DataOutputStream out;
        ServerSocket serverSocket;
        Socket socket;
        try {
            serverSocket = new ServerSocket(portNumber);
            socket = serverSocket.accept();
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            ByteBuffer buf = ByteBuffer.allocate(8);
            for (int i = 0; i < 100; i++) {
                in.read(buf.array());
                xorCipher(buf);
                out.write(buf.array());

                buf = ByteBuffer.allocate(64);
                in.read(buf.array());
                xorCipher(buf);
                out.write(buf.array());

                buf = ByteBuffer.allocate(256);
                in.read(buf.array());
                xorCipher(buf);
                out.write(buf.array());

                buf = ByteBuffer.allocate(1024);
                in.read(buf.array());
                xorCipher(buf);
                out.write(buf.array());

                ByteBuffer response = ByteBuffer.allocate(8);
                for (int j = 0; j < 1024; j++) {
                    in.read(buf.array());
                    xorCipher(response);
                    out.write(response.array());
                }

                buf = ByteBuffer.allocate(512);
                for (int j = 0; j < 2048; j++) {
                    in.read(buf.array());
                    xorCipher(response);
                    out.write(response.array());
                }

                buf = ByteBuffer.allocate(256);
                for (int j = 0; j < 4096; j++) {
                    in.read(buf.array());
                    xorCipher(response);
                    out.write(response.array());
                }
            }
            try {
                in.close();
                System.out.println("TCP Communication Complete.");
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException ex) {
            System.out.println("Exception caught while trying to listen on port " + portNumber + " or listening for connection");
            System.out.println(ex.getMessage());
        }

        System.out.println("Ending TCP Communication...");
    }
    static void xorCipher (ByteBuffer bbuf) {
        long key = 8;
        bbuf.putLong(0, bbuf.getLong(0) ^ key);
    }
}