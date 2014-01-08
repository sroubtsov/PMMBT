package protocolmodelingmbt.model; 

import java.io.BufferedWriter;
import java.io.IOException;

public class Transition{
    private State beforeState;
    private Event action;

    public State getBeforeState() {
        return beforeState;
    }

    public Event getAction() {
        return action;
    }

    public State getAfterState() {
        return afterState;
    }
    private State afterState;

    public Transition(State beforeState, Event action, State afterState) {
        this.beforeState = beforeState;
        this.action = action;
        this.afterState = afterState;
    }
    
    public String getTransitionStr(){
        return this.getBeforeState().getState() + "*" + this.getAction().getAction() + "=" + this.getAfterState().getState();
    }
    
    public void writeTransition(){
        System.out.println(this.getBeforeState().getState() + "*" + this.getAction().getAction() + "=" + this.getAfterState().getState());
    }
    public void writeTransition(BufferedWriter out) throws IOException{
        out.write(this.getBeforeState().getState() + "*" + this.getAction().getAction() + "=" + this.getAfterState().getState() + "\n");
    }



}