package protocolmodelingmbt.model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import protocolmodelingmbt.parser.Grammar;

public class Generic extends ModelElement {
    private String name;
    private ArrayList<String> matches;

    public Generic(String name, ArrayList<String> matches) {
        this.name = name;
        this.matches = matches;
    }

    public ArrayList<String> getMatches() {
        return matches;
    }

    public void writeGeneric(BufferedWriter out) throws IOException {
        out.write(Grammar.GENERIC + " " + this.name + "\n");
        out.write(Grammar.MATCHES + "\n");
        for (int i = 0; i < this.matches.size(); i++) {
            out.write(this.matches.get(i) + "\n");
        }
    }

    public void writeGeneric() {
        System.out.println(Grammar.GENERIC + " " + this.name);
        System.out.println(Grammar.MATCHES);
        for (int i = 0; i < this.matches.size(); i++) {
            System.out.println(this.matches.get(i));
        }
    }
}