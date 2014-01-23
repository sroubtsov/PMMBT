package protocolmodelingmbt.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import protocolmodelingmbt.parser.Grammar;
import protocolmodelingmbt.parser.ParseModel;
import protocolmodelingmbt.testmaker.OBTraceMaker;

public class Behaviour extends ModelElement {

    private ArrayList<Attribute> attributes;
    private ArrayList<State> states;
    private ArrayList<Transition> transitions;
    private ArrayList<String> traces;
    private State currentState;

    public ArrayList<String> getTraces() {
        return traces;
    }

    public Behaviour(ArrayList<Attribute> attributes, ArrayList<State> states, ArrayList<Transition> transitions) {
        this.attributes = attributes;
        this.states = states;
        this.transitions = transitions;
        this.traces = new ArrayList<>();
        this.currentState = new State("@new");
    }

    public void setCurrentState(String currentState) {
        this.currentState.setState(currentState);
    }

    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }

    public String getCurrentState() {
        return currentState.getState();
    }

    public ArrayList<State> getStates() {
        return states;
    }

    public ArrayList<Transition> getTransitions() {
        return transitions;
    }

    public ArrayList<String> getTransitionStrings() {
        ArrayList<String> transitionStrs = new ArrayList<>();
        for (Transition tr : this.transitions) {
            transitionStrs.add(tr.getTransitionStr());
        }
        return transitionStrs;
    }

    public ArrayList<String> getstateNames() {
        ArrayList<String> stateStrs = new ArrayList<>();
        for (State st : this.states) {
            stateStrs.add(st.getState());
        }
        return stateStrs;

    }

    public ArrayList<String> getBEEventNames() {
        return ParseModel.allEvents(getTransitionStrings());
    }

    public String getbeforeStateForEventName(String eventname) {
        String beforestate = "";
        for (Transition tr : this.transitions) {
            if (tr.getAction().getAction().equals(eventname)) {
                beforestate = tr.getBeforeState().getState();
                break;
            }
        }
        return beforestate;
    }

    public String getafterStateForEventName(String eventname) {
        String afterstate = "";
        for (Transition tr : this.transitions) {
            if (tr.getAction().getAction().equals(eventname)) {
                afterstate = tr.getAfterState().getState();
                break;
            }
        }
        return afterstate;
    }

    public void setAfterStateAsCurrentForEventName(String eventname) {
        for (Transition tr : this.transitions) {
            if (tr.getAction().getAction().equals(eventname)) {
                this.currentState.setState(tr.getAfterState().getState());
                break;
            }
        }
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

    public void writeTraces(int mode) {
        System.out.println("Traces for " + this.getModelElementName());
        switch (mode) {
            case 0:
                for (int i = 0; i < this.traces.size(); i++) {
                    System.out.println(traces.get(i));
                }

                break;
            case 1:
            case 2:
                for (int i = 0; i < this.traces.size(); i++) {
                    String[] tracesstr = OBTraceMaker.splitTrace(this.traces.get(i));

                    for (int j = 0; j < tracesstr.length - 1; j++) {
                        if (mode == 2) {
                            System.out.print(OBTraceMaker.hideStatesInTransition(tracesstr[j]) + "-->");
                        } else {
                            System.out.println(tracesstr[j] + "-->");
                        }
                    }
                    System.out.println(tracesstr[tracesstr.length - 1]);
                }
                break;
        }

    }
}
