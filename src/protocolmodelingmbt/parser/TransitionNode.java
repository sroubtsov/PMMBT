package protocolmodelingmbt.parser;

import protocolmodelingmbt.parser.ParseModel;
import protocolmodelingmbt.model.State;
import protocolmodelingmbt.model.Event;
import java.util.ArrayList;
import protocolmodelingmbt.model.Behaviour;

public class TransitionNode {

    private State beforeState;
    public Event action;
    private State afterState;
    public ArrayList<TransitionNode> possibleTransitions = new ArrayList<>();

    public TransitionNode(String currentState, String transition, ArrayList<String> transitions) {
        this.beforeState = new State(currentState);        
        this.action = new Event(ParseModel.getEvent(transition));
        this.afterState = new State(ParseModel.afterState(transition));
        ArrayList<String> nextTransitions = new ArrayList<>();

        nextTransitions.addAll(transitions);
        nextTransitions.remove(transition);

        int i = 0;
        while (i < nextTransitions.size()) {
            String nextTransition = nextTransitions.get(i);
            if (ParseModel.beforeState(nextTransition).equals(this.afterState.getState()) || ParseModel.beforeState(nextTransition).equals("@any")) {
                possibleTransitions.add(new TransitionNode(this.afterState.getState(), nextTransition, nextTransitions));
            }
            i++;
        }
    }

//    public void traverseTransitionNode() {
//
//        // print, increment counter, whatever
//        //System.out.println(tree.toString());
//        System.out.println(this.action);
//        // traverse children
//        int childCount = this.getChildCount();
//        if (childCount == 0) {
//            System.out.println("----");
//        } else {
//            for (int i = 0; i < childCount; i++) {
//                this.possibleTransitions.get(i).traverseTransitionNode();
//            }
//        }
//    }

    private int getChildCount() {
        return this.possibleTransitions.size();
    }
    
    public ArrayList<TransitionNode> getChildren(){
        return this.possibleTransitions;
    }
    
    
    public String getTransitionValues(Behaviour behaviour){
        return this.beforeState.getState() + "*" + this.action.getAction() + "=" + this.afterState.getState();
    }
}