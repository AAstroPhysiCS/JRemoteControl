package client;

import javax.sound.sampled.*;
import java.io.*;
import java.net.Socket;

public class AudioCapture {

    private Socket socket;

    private static final int RECORD_TIME = 10000;

    public AudioCapture(Socket socket) {
        this.socket = socket;
        fetchData();
    }

    private void fetchData() {
        try {
            AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
            if (line.getFormat().matches(format)) {
                line.open();
                line.start();

//            System.out.println("Started Audio Capturing...");
                AudioInputStream inputStream = new AudioInputStream(line);
                socket.getOutputStream().write(inputStream.readNBytes((int) (inputStream.getFormat().getFrameSize() * inputStream.getFormat().getFrameRate() * (RECORD_TIME / 1000))));

                Thread.sleep(RECORD_TIME);
//            System.out.println("End Capturing");

                socket.getOutputStream().close();
                line.close();
                inputStream.close();
            }
        } catch (LineUnavailableException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
