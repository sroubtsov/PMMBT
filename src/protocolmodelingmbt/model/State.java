package protocolmodelingmbt.model;

import java.io.BufferedWriter;
import java.io.IOException;

public class State {

    public String state;

    public State(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void writeState(BufferedWriter out) throws IOException {
        out.write(this.getState());
    }

    public void writeState() {
        System.out.println(this.getState());        
    }
}