package protocolmodelingmbt.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import protocolmodelingmbt.parser.Grammar;
import protocolmodelingmbt.parser.ParseModel;

public class Behaviour extends ModelElement {

    private ArrayList<Attribute> attributes;
    private ArrayList<State> states;
    private ArrayList<Transition> transitions;
    private ArrayList<String> traces;

    public ArrayList<String> getTraces() {
        return traces;
    }

    public Behaviour(ArrayList<Attribute> attributes, ArrayList<State> states, ArrayList<Transition> transitions) {
        this.attributes = attributes;
        this.states = states;
        this.transitions = transitions;
        this.traces = new ArrayList<>();
    }

    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }

    public ArrayList<State> getStates() {
        return states;
    }

    public ArrayList<Transition> getTransitions() {
        return transitions;
    }

    public ArrayList<String> getTransitionStrings() {
        ArrayList<String> transitionStrs = new ArrayList<>();
        for(Transition tr: this.transitions){
            transitionStrs.add(tr.getTransitionStr());
        }
        return transitionStrs;
    }

    public ArrayList<String> getBEEventNames(){
        return ParseModel.allEvents(getTransitionStrings());
    }
    
    public void writeBehaviour(BufferedWriter out) throws IOException {
        out.write(Grammar.ATTRIBUTES + "\n");
        for (int i = 0; i < this.attributes.size(); i++) {
            this.attributes.get(i).writeAttribute(out);
        }
        out.write(Grammar.STATES + "\n");
        for (int i = 0; i < this.states.size(); i++) {
            this.states.get(i).writeState(out);
            out.write("\n");
        }
        out.write(Grammar.TRANSITIONS + "\n");
        for (int i = 0; i < this.transitions.size(); i++) {
            this.transitions.get(i).writeTransition(out);
        }
    }

    public void writeBehaviour() {
        System.out.println(Grammar.ATTRIBUTES);
        for (int i = 0; i < this.attributes.size(); i++) {
            this.attributes.get(i).writeAttribute();
        }
        System.out.println(Grammar.STATES);
        for (int i = 0; i < this.states.size(); i++) {
            this.states.get(i).writeState();
            //           System.out.println();
        }
        System.out.println(Grammar.TRANSITIONS);
        for (int i = 0; i < this.transitions.size(); i++) {
            this.transitions.get(i).writeTransition();
        }
    }
    
    public void writeTraces(){
        System.out.println("Traces for " + this.getModelElementName());
        for (int i = 0; i < this.traces.size(); i++) {
            System.out.println(this.traces.get(i));
        }
        
    } 
}
