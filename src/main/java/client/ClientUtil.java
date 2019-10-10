package client;

import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;

public class ClientUtil {

    public InetAddress ADDRESS;
    public final int PORT = 8000;

    public Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();

    public ClientUtil() {
        try {
            ADDRESS = InetAddress.getByName("OneTrueGod");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
