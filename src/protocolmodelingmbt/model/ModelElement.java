package protocolmodelingmbt.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public abstract class ModelElement {

    private String modelElementName;

    public String getModelElementName() {
        return modelElementName;
    }

    public void setModelElementName(String modelElementName) {
        this.modelElementName = modelElementName;
    }

    public void writeModelElementName(BufferedWriter out) throws IOException {
        out.write(this.getModelElementName() + "\n");
    }

    public void writeModelElementName() {
        System.out.println(this.getModelElementName());
    }
}
