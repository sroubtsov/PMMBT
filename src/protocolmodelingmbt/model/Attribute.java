package protocolmodelingmbt.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Attribute {

    private String name;
    private Types type;

    public Attribute(String name, Types type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Types getType() {
        return type;
    }

    public void writeAttribute(BufferedWriter out) throws IOException {
        out.write(this.getName() + ":" + this.getType() + "\n");
    }

    public void writeAttribute() {
         System.out.println(this.getName() + ":" + this.getType());
    }
}