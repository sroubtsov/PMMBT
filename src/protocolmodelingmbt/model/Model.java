package protocolmodelingmbt.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Model {

    private String name;
    public ArrayList<_Object> objects;
    public ArrayList<Behaviour> behaviours;
    public ArrayList<Event> events;
    public ArrayList<Generic> generics;
    public ArrayList<Actor> actors;
    public ArrayList<Callback> callbacks;
    //for test generation
    public ArrayList<String> traces;
    private ArrayList<String> eventNames;

    public ArrayList<String> getEventNames() {
        return eventNames;
    }

    public Model(String name) {
        this.name = name;
        this.behaviours = new ArrayList();
        this.objects = new ArrayList();
        this.events = new ArrayList();
        this.generics = new ArrayList();
        this.actors = new ArrayList();
        this.callbacks = new ArrayList();
        //for test generation
        this.traces = new ArrayList();
        this.eventNames = new ArrayList();
    }

    public void writeModel(String fileName) {
        if (!fileName.isEmpty()) {
            try (BufferedWriter out = new BufferedWriter(new FileWriter(fileName))) {
                out.write("Model: " + this.name + "\n");
                for (Actor actor : this.actors) {
                    actor.writeModelElementName(out);
                    actor.writeActor(out);
                }
                for (_Object object : this.objects) {
                    object.writeModelElementName(out);
                    object.writeObject(out);
                }
                for (Behaviour behaviour : this.behaviours) {
                    behaviour.writeModelElementName(out);
                    behaviour.writeBehaviour(out);
                }
                for (Event event : this.events) {
                    event.writeModelElementName(out);
                    event.writeEvent(out);
                }
                for (Generic generic : this.generics) {
                    generic.writeModelElementName(out);
                    generic.writeGeneric(out);
                }

                for (Callback callback : this.callbacks) {
                    callback.writeModelElementName(out);
                    callback.writeCallback(out);
                }


                out.flush();
                out.close();
            } catch (IOException eio) {
                System.err.println("IOException: " + eio.getMessage());
                System.exit(1);
            }

        } else {
            System.out.println("Model: " + this.name);
            for (Actor actor : this.actors) {
                actor.writeModelElementName();
                actor.writeActor();
            }
            for (_Object object : this.objects) {
                object.writeModelElementName();
                object.writeObject();
            }
            for (Behaviour behaviour : this.behaviours) {
                behaviour.writeModelElementName();
                behaviour.writeBehaviour();
            }
            for (Event event : this.events) {
                event.writeModelElementName();
                event.writeEvent();
            }
            for (Generic generic : this.generics) {
                generic.writeModelElementName();
                generic.writeGeneric();
            }
//            for (Callback callback : this.callbacks) {
//                callback.writeModelElementName();
//                callback.writeCallback();
//            }


        }

    }
}