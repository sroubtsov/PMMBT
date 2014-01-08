package protocolmodelingmbt.model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import protocolmodelingmbt.parser.Grammar;

public class Event extends ModelElement {

    private String action;
    private ArrayList<Attribute> attributes;

    public Event(String action) {
        this.action = action;
        this.attributes = new ArrayList();

    }

    public void setAction(String action) {
        this.action = action;
    }

    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(ArrayList<Attribute> attributes) {
        this.attributes = attributes;
    }

    public String getAction() {
        return action;
    }

    void writeEvent(BufferedWriter out) throws IOException {
        out.write(Grammar.EVENT + " " + this.getAction() + "\n");
        out.write(Grammar.ATTRIBUTES + "\n");
        for (int i = 0; i < this.attributes.size(); i++) {
            this.attributes.get(i).writeAttribute(out);
        }
    }

    public void writeEvent() {
        System.out.println(Grammar.EVENT + " " + this.getAction());
        System.out.println(Grammar.ATTRIBUTES);
        for (int i = 0; i < this.attributes.size(); i++) {
            this.attributes.get(i).writeAttribute();
        }

    }
}