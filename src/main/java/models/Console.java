package models;

import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.OutputStream;

public class Console extends OutputStream {
    private TextArea output;

    public Console(TextArea ta) {
        output = ta;
    }

    @Override
    public void write(int i) throws IOException {
        output.appendText(String.valueOf((char) i));
    }

    public void write(String s) throws IOException {
        for (char c : s.toCharArray())
            write(c);
    }
    
}
