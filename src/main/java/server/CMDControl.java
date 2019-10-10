package server;

import java.io.IOException;
import java.io.OutputStream;

public class CMDControl {

    private OutputStream outputStream;
    private String text;

    public CMDControl(OutputStream stream, String text){
        this.outputStream = stream;
        if(text != null){
            this.text = text;
            fetchData();
        }
    }

    private void fetchData(){
        byte[] textInBytes = text.getBytes();
        try {
            outputStream.write(textInBytes);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
