package server;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class CameraCapture {

    private InputStream inputStream;
    private Canvas canvas;

    public CameraCapture(Canvas canvas, InputStream inputStream) {
        this.inputStream = inputStream;
        this.canvas = canvas;
        try {
            fetchCam();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fetchCam() throws IOException {
        BufferedImage image = writeToImage(inputStream);
        assert image != null;

        BufferedImage resizedImage = Thumbnails.of(image)
                .size((int) canvas.getWidth() - 150, (int) canvas.getHeight() - 150)
                .asBufferedImage();

        GraphicsContext cx = canvas.getGraphicsContext2D();
        cx.drawImage(SwingFXUtils.toFXImage(resizedImage, null), 0, 0, resizedImage.getWidth(), resizedImage.getHeight());

        inputStream.close();
    }

    private BufferedImage writeToImage(InputStream inputStream) {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            output.write(inputStream.readAllBytes());
            ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
            output.close();
            input.close();
            return ImageIO.read(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
