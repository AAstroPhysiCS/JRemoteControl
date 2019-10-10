package server;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class VideoCapture {

    private Canvas canvas;

    private InputStream inputStream;

    public VideoCapture(Canvas canvas, InputStream inputStream) {
        this.inputStream = inputStream;
        this.canvas = canvas;
        render();
    }

    private void render() {
        try {
            BufferedImage image = writeToImage(inputStream);

            BufferedImage resizedImage = Thumbnails.of(image)
                    .size((int) canvas.getWidth() - 100, (int) canvas.getHeight())
                    .asBufferedImage();

            GraphicsContext cx = canvas.getGraphicsContext2D();
            cx.drawImage(SwingFXUtils.toFXImage(resizedImage, null), 25, 0, resizedImage.getWidth(), resizedImage.getHeight() + 25);

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
