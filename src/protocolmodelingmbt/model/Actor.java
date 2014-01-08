package protocolmodelingmbt.model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import protocolmodelingmbt.parser.Grammar;

public class Actor extends ModelElement {

    private String name;
    private ArrayList<String> behaviours;
    private ArrayList<String> events;

    public Actor(String name, ArrayList<String> behaviours, ArrayList<String> events) {
        this.name = name;
        this.behaviours = behaviours;
        this.events = events;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getBehaviours() {
        return behaviours;
    }

    public ArrayList<String> getEvents() {
        return events;
    }

    public void writeActor(BufferedWriter out) throws IOException {
        out.write(Grammar.BEHAVIOURS + "\n");
        for (int i = 0; i < this.behaviours.size(); i++) {
            out.write(this.behaviours.get(i) + "\n");      
        }
        out.write(Grammar.EVENTS + " ");
        for (int i = 0; i < this.events.size(); i++) {
            out.write(this.events.get(i) + "\n");
        }
    }
    
        public void writeActor(){
        System.out.println(Grammar.BEHAVIOURS);
        for (int i = 0; i < this.behaviours.size(); i++) {
            System.out.print(this.behaviours.get(i) + ", ");    //TODO parse to remove BEHAVIOUR        
        }
        System.out.println(Grammar.EVENTS + " ");
        for (int i = 0; i < this.events.size(); i++) {
            System.out.print(this.events.get(i));
        }
    }
}