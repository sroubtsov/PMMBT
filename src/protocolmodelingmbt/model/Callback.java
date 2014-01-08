package protocolmodelingmbt.model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Callback extends ModelElement {

    private ArrayList<String> callbackcode;

    public Callback(ArrayList<String> callbaclcode) {
        this.callbackcode = callbackcode;
    }

    public ArrayList<String> getCallbaclcode() {
        return callbackcode;
    }

    public void writeCallback(BufferedWriter out) throws IOException {
        for (String line : callbackcode) {
            out.write(line + "\n");
        }
    }

    public void writeCallback() {
        for (String line : callbackcode) {
            System.out.println(line);
        }
    }
}