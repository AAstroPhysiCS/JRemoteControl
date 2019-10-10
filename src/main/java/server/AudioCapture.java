package server;

import javax.sound.sampled.*;
import java.io.*;

public class AudioCapture {

    private InputStream inputStream;

    private static final int RECORD_TIME = 10;  //in seconds
    private static final int BUFFER_LENGTH = 176400 * RECORD_TIME;

    public AudioCapture(InputStream inputStream) {
        this.inputStream = inputStream;
        fetchAudio();
    }

    private void fetchAudio() {
        try {
            AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
            byte[] data = inputStream.readNBytes(BUFFER_LENGTH);

            AudioInputStream audioIn = new AudioInputStream(new ByteArrayInputStream(data), format, data.length);
            DataLine.Info info = new DataLine.Info(Clip.class, format);

            if (audioIn.getFormat().matches(format)) {
                Clip clip = (Clip) AudioSystem.getLine(info);
                clip.open(audioIn);
                clip.start();

                System.out.println("Listen!");

                Thread.sleep(10000);

                clip.stop();
                clip.close();
                audioIn.close();
                inputStream.close();
            }

        } catch (IOException | InterruptedException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
