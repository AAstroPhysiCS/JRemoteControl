package client;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;

public class CameraCapture extends ClientUtil {

    private VideoCapture capture;
    private Mat c_Mat;

    private Socket socket;

    public CameraCapture(Socket socket, VideoCapture capture, Mat c_Mat) {
        this.socket = socket;
        this.capture = capture;
        this.c_Mat = c_Mat;
        sendCam();
    }

    private void sendCam() {
        capture.read(c_Mat);
        BufferedImage image = convertToBufferedImage(c_Mat);
        ByteArrayOutputStream outputArray = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", outputArray);
            socket.getOutputStream().write(outputArray.toByteArray());
            socket.getOutputStream().flush();
            socket.getOutputStream().close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage convertToBufferedImage(Mat mat) {
        BufferedImage image = new BufferedImage(mat.width(), mat.height(), BufferedImage.TYPE_3BYTE_BGR);
        DataBuffer buffer = image.getRaster().getDataBuffer();
        DataBufferByte bufferByte = (DataBufferByte) buffer;
        mat.get(0, 0, bufferByte.getData());
        return image;
    }
}
