package protocolmodelingmbt.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import protocolmodelingmbt.parser.Grammar;
import protocolmodelingmbt.parser.ParsingUtilities;

public class _Object extends Behaviour {

    private String name;
    private ArrayList<String> includes;
    private ArrayList<String> callbacks;

    public _Object(String name) {
        super(new ArrayList<Attribute>(), new ArrayList<State>(), new ArrayList<Transition>());
        this.name = name;
        this.includes = new ArrayList<String>();
        this.callbacks = new ArrayList<String>();
        
    }

    public _Object(String name, ArrayList<Attribute> attributes, ArrayList<State> states, ArrayList<Transition> transitions) {
        super(attributes, states, transitions);
        this.name = name;
    }

    public _Object(String name, ArrayList<String> includes, ArrayList<Attribute> attributes, ArrayList<State> states, ArrayList<Transition> transitions) {
        super(attributes, states, transitions);
        this.name = name;
        this.includes = includes;
    }

    public ArrayList<String> getIncludes() {
        return includes;
    }

    public String getName() {
        return name;
    }

    public void writeObject(BufferedWriter out) throws IOException {
        out.write(Grammar.NAME + ":" + this.name + "\n");
                out.write(Grammar.INCLUDES + "\n");
        for (int i = 0; i < this.includes.size(); i++) {
            out.write(this.includes.get(i) + "\n");
        }

        super.writeBehaviour(out);
    }

    public void writeObject() {
        System.out.println(Grammar.NAME + ":" + this.name);
        System.out.println(Grammar.INCLUDES);
        for (int i = 0; i < this.includes.size(); i++) {
            System.out.println(this.includes.get(i));
        }

        super.writeBehaviour();
    }
}