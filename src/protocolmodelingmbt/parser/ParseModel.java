/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package protocolmodelingmbt.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import protocolmodelingmbt.model.Attribute;
import protocolmodelingmbt.model.Behaviour;
import protocolmodelingmbt.model.Callback;
import protocolmodelingmbt.model.Event;
import protocolmodelingmbt.model.Generic;
import protocolmodelingmbt.model.Model;
import protocolmodelingmbt.model.State;
import protocolmodelingmbt.model.Transition;
import protocolmodelingmbt.model._Object;

/**
 *
 * @author S. Roubtsov
 */
public class ParseModel {

    public Model model;

    private boolean existsInTypes(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static enum PARSESTATE {

        model, object, behaviour, event, generic, callback, actor, attribute, state, transition, include, match
    }
    private static PARSESTATE parsestate;

    private static ArrayList<String> readModel(String filename) {
        ArrayList<String> model;
        model = new ArrayList();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
//                line = line.replaceAll(",", ""); //SR TODO Comma is needed. To be removed later where necessary
                line = line.replaceAll(" ", ""); //remove spaces
                line = line.replaceAll("\t", "");//and tabs

                if (!line.equals("") && !line.startsWith("#")) {//remove empty lines and comments
//                    System.out.println("===" +  line);
                    model.add(line);
                }
            }
        } catch (IOException eio) {
            System.err.println("IOException: " + eio.getMessage());
            System.exit(1);
        }
        return model;
    }

    public static String beforeState(String transition) {

        String beforestate = transition.substring(0, transition.indexOf("*"));
        return beforestate;
    }

    public static String afterState(String transition) {
        String afterstate = transition.substring(transition.indexOf("=") + 1);
        return afterstate;
    }

    public static ArrayList<String> allEvents(ArrayList<String> transitions) {
        ArrayList<String> allevents = new ArrayList<String>();
        for (int i = 0; i < transitions.size(); i++) {
            allevents.add(transitions.get(i).substring(transitions.get(i).indexOf("*") + 1, transitions.get(i).indexOf("=")));
        }
        return allevents;
    }

    public static String getEvent(String transition) {
        return transition.substring(transition.indexOf("*") + 1, transition.indexOf("="));
    }

    public Model parseModel(String filename) {
        String line;
        ArrayList<String> modellines = readModel(filename);
//System.out.println(" size =" +modellines.size());

//Contain the lines of current model element
        ArrayList<String> element = new ArrayList();
//Main parsing'loop        
        int i;
//First line should contain model name            
        if (modellines.get(0).startsWith(Grammar.MODEL)) {
            createModel(ParsingUtilities.parseMSname(Grammar.MODEL, modellines.get(0)));
        } else {
            // if it doesn't
            createModel("NONAME");
        }
        parsestate = PARSESTATE.model;
        for (i = 0; i < modellines.size(); i++) {
//finds out what model element it is            
            if (modellines.get(i).startsWith(Grammar.BEHAVIOUR)
                    || modellines.get(i).startsWith(Grammar.OBJECT)
                    || modellines.get(i).startsWith(Grammar.EVENT)
                    || modellines.get(i).startsWith(Grammar.GENERIC)
                    || modellines.get(i).startsWith(Grammar.ACTOR)
                    || modellines.get(i).startsWith(Grammar.CALLBACK)) {

                if (modellines.get(i).startsWith(Grammar.EVENT)) {
                    createModelElement(parsestate, element); //For the previous state!
                    element = new ArrayList();
//                    System.out.println("------  event --------");
                    parsestate = PARSESTATE.event;
                } else if (modellines.get(i).startsWith(Grammar.GENERIC)) {
                    createModelElement(parsestate, element);
                    element = new ArrayList();
 //                   System.out.println("------ generic --------");
                    parsestate = PARSESTATE.generic;
                } else if (modellines.get(i).startsWith(Grammar.BEHAVIOUR)) {
                    createModelElement(parsestate, element);
                    element = new ArrayList();
//                    System.out.println("-------- behaviour ------");
                    parsestate = PARSESTATE.behaviour;
                } else if (modellines.get(i).startsWith(Grammar.OBJECT)) {
                    createModelElement(parsestate, element);
                    element = new ArrayList();
 //                   System.out.println("------- object -------");
                    parsestate = PARSESTATE.object;
                } else if (modellines.get(i).startsWith(Grammar.ACTOR)) {
                    //TODO
                    createModelElement(parsestate, element);
                    element = new ArrayList();
//                    System.out.println("------- actor -------");
                    parsestate = PARSESTATE.actor;
                } else if (modellines.get(i).startsWith(Grammar.CALLBACK)) {
                    createModelElement(parsestate, element);
                    //TODO
//                    element = new ArrayList();
                    System.out.println("------- callback -------");
                    parsestate = PARSESTATE.callback;
                }
            }

//parse model element at the current parsing state
            if (parsestate == PARSESTATE.actor) {
                //TODO
                element.add(modellines.get(i));
                // System.out.println(modellines.get(i));
            } else if (parsestate == PARSESTATE.generic) {
                element.add(modellines.get(i));
                //System.out.println(modellines.get(i));
            } else if (parsestate == PARSESTATE.event) {
                element.add(modellines.get(i));
                //System.out.println(modellines.get(i));
            } else if (parsestate == PARSESTATE.behaviour) {
                element.add(modellines.get(i));
                //System.out.println(modellines.get(i));
            } else if (parsestate == PARSESTATE.object) {
                element.add(modellines.get(i));
                //System.out.println(modellines.get(i));
            } else if (parsestate == PARSESTATE.callback) {
                //TODO
                element.add(modellines.get(i));
                //System.out.println(modellines.get(i));
            }
        }
        createModelElement(parsestate, element); //For the last one! 
        return this.model;
    }

    private void createModel(String name) {
        this.model = new Model(name);
    }

    private void createEvent(ArrayList<String> ev) {
        String currentline = "";
        Event event = new Event(ParsingUtilities.parseMSname(Grammar.EVENT, ev.get(0)));
        event.setModelElementName(ev.get(0));

        parsestate = PARSESTATE.event;
//add attributes
        for (int i = 0; i < ev.size(); i++) {
            if (ev.get(i).startsWith(Grammar.ATTRIBUTES)) {
                parsestate = PARSESTATE.attribute;
            }
            if ((parsestate == PARSESTATE.attribute)) {
                currentline = ParsingUtilities.parseMSname(Grammar.ATTRIBUTES, ev.get(i));
                if (!currentline.isEmpty()) {
                    String[] attributes = ParsingUtilities.parseMSelement(currentline, ",");
                    for (int j = 0; j < attributes.length; j++) {
                        String[] tokens = ParsingUtilities.parseMSelement(attributes[j], ":");
                        event.getAttributes().add(new Attribute(tokens[0], ParsingUtilities.returnType(tokens[1])));
                    }
                }
            }
        }
        this.model.events.add(event);
//        System.out.println("... creating event " + event.getAction());
//        event.writeEvent();
    }

    private void createOB(ArrayList<String> ob) {
        //    _Object object = new _Object();
        String currentline = "";
        _Object object = new _Object(ParsingUtilities.parseMSname(Grammar.OBJECT, ob.get(0)));
        object.setModelElementName(ob.get(0));

        parsestate = PARSESTATE.object;
//parse state change
        for (int i = 0; i < ob.size(); i++) {
            if (ob.get(i).startsWith(Grammar.ATTRIBUTES)) {
                parsestate = PARSESTATE.attribute;
            } else if (ob.get(i).startsWith(Grammar.STATES)) {
                parsestate = PARSESTATE.state;
            } else if (ob.get(i).startsWith(Grammar.INCLUDES)) {
                parsestate = PARSESTATE.include;
            } else if (ob.get(i).startsWith(Grammar.TRANSITIONS)) {
                parsestate = PARSESTATE.transition;
            }
//parse at a current state            
            if ((parsestate == PARSESTATE.attribute)) {
                currentline = ParsingUtilities.parseMSname(Grammar.ATTRIBUTES, ob.get(i));
                if (!currentline.isEmpty()) {
                    String[] attributes = ParsingUtilities.parseMSelement(currentline, ",");
                    for (int j = 0; j < attributes.length; j++) {
                        String[] tokens = ParsingUtilities.parseMSelement(attributes[j], ":");
                        object.getAttributes().add(new Attribute(tokens[0], ParsingUtilities.returnType(tokens[1])));
                    }
                }
            } else if (parsestate == PARSESTATE.state) {
                currentline = ParsingUtilities.parseMSname(Grammar.STATES, ob.get(i));
                if (!currentline.isEmpty()) {
                    String[] states = ParsingUtilities.parseMSelement(currentline, ",");
                    object.getStates().add(new State(object.getName() + "."  + "@new"));
                    for (int j = 0; j < states.length; j++) {
                        System.out.println("state  " + j + " " + object.getName() + "."  + states[j]);
                        object.getStates().add(new State(object.getName() + "."  + states[j]));
                    }
                }
            } else if (parsestate == PARSESTATE.include) {
                currentline = ParsingUtilities.parseMSname(Grammar.INCLUDES, ob.get(i));
                if (!currentline.isEmpty()) {
                    String[] includes = ParsingUtilities.parseMSelement(currentline, ",");
                    for (int j = 0; j < includes.length; j++) {
//                        System.out.println("state  " + j + " " + states[j]);
                        object.getIncludes().add(new String(includes[j]));
                    }
                }

            } else if (parsestate == PARSESTATE.transition) {
                currentline = ParsingUtilities.parseMSname(Grammar.TRANSITIONS, ob.get(i));
                if (!currentline.isEmpty()) {
                    String[] transitions = ParsingUtilities.parseMSelement(currentline, ",");
                    for (int j = 0; j < transitions.length; j++) {
                        String[] tokens = ParsingUtilities.parseMSelement(transitions[j], "[*=]");
                        object.getTransitions().add(new Transition(new State(tokens[0]), new Event(tokens[1]), new State(tokens[2])));
                    }

                }
            }
        }
        this.model.objects.add(object);
//        System.out.println("... creating object " + object.getName());
//        object.writeObject();
    }

    private void createBE(ArrayList<String> be) {
        //    _Object object = new _Object();
        String currentline = "";
        Behaviour behaviour = new Behaviour(new ArrayList<Attribute>(), new ArrayList<State>(), new ArrayList<Transition>());
        behaviour.setModelElementName(be.get(0));

        parsestate = PARSESTATE.behaviour;
//parse state change
        for (int i = 0; i < be.size(); i++) {
            if (be.get(i).startsWith(Grammar.ATTRIBUTES)) {
                parsestate = PARSESTATE.attribute;
            } else if (be.get(i).startsWith(Grammar.STATES)) {
                parsestate = PARSESTATE.state;
            } else if (be.get(i).startsWith(Grammar.TRANSITIONS)) {
                parsestate = PARSESTATE.transition;
            }
//parse at a current state            
            if ((parsestate == PARSESTATE.attribute)) {
                currentline = ParsingUtilities.parseMSname(Grammar.ATTRIBUTES, be.get(i));
                if (!currentline.isEmpty()) {
                    String[] attributes = ParsingUtilities.parseMSelement(currentline, ",");
                    for (int j = 0; j < attributes.length; j++) {
                        String[] tokens = ParsingUtilities.parseMSelement(attributes[j], ":");
                        behaviour.getAttributes().add(new Attribute(tokens[0], ParsingUtilities.returnType(tokens[1])));
                    }
                }
            } else if (parsestate == PARSESTATE.state) {
                currentline = ParsingUtilities.parseMSname(Grammar.STATES, be.get(i));
                if (!currentline.isEmpty()) {
                    String[] states = ParsingUtilities.parseMSelement(currentline, ",");
                    for (int j = 0; j < states.length; j++) {
                        //                      System.out.println("state  " + j + " " + states[j]);
                        behaviour.getStates().add(new State(ParsingUtilities.parseMSname(Grammar.BEHAVIOUR, behaviour.getModelElementName()) + "." + states[j]));
                    }
                }

            } else if (parsestate == PARSESTATE.transition) {
                currentline = ParsingUtilities.parseMSname(Grammar.TRANSITIONS, be.get(i));
                if (!currentline.isEmpty()) {
                    String[] transitions = ParsingUtilities.parseMSelement(currentline, ",");
                    for (int j = 0; j < transitions.length; j++) {
                        String[] tokens = ParsingUtilities.parseMSelement(transitions[j], "[*=]");
                        behaviour.getTransitions().add(new Transition(new State(tokens[0]), new Event(tokens[1]), new State(tokens[2])));
                    }

                }
            }
        }
        this.model.behaviours.add(behaviour);
  //      System.out.println("... creating behaviour " + behaviour.getModelElementName());
 //       behaviour.writeBehaviour();
    }

    private void createGeneric(ArrayList<String> ge) {
        String currentline;
        Generic generic = new Generic(ParsingUtilities.parseMSname(Grammar.GENERIC, ge.get(0)), new ArrayList<String>());
        generic.setModelElementName(ge.get(0));

        parsestate = PARSESTATE.generic;
//add matches
        for (int i = 0; i < ge.size(); i++) {
            if (ge.get(i).startsWith(Grammar.MATCHES)) {
                parsestate = PARSESTATE.match;
            }
            if (parsestate == PARSESTATE.match) {
                currentline = ParsingUtilities.parseMSname(Grammar.MATCHES, ge.get(i));
                if (!currentline.isEmpty()) {
                    String[] matches = ParsingUtilities.parseMSelement(currentline, ",");
                    for (int j = 0; j < matches.length; j++) {
                        generic.getMatches().add(matches[j]);
                    }
                }
            }
        }
        this.model.generics.add(generic);
 //       System.out.println("... creating generic ");
//        generic.writeGeneric();
    }

    private void createCB(ArrayList<String> cb) {
        String currentline;
        Callback callback = new Callback(cb); //SR for the time being. TODO: 1 - remove CALLBACK line; 
        //2 - parse public class ... for the name of the corresponding object; 
        //3 - store it to the object as well
        this.model.callbacks.add(callback);
//        System.out.println("... creating callback ");
//        callback.writeCallback();


    }

    private void createModelElement(PARSESTATE parsestate, ArrayList<String> element) {
        if (parsestate == PARSESTATE.actor) {
            //TODO
        } else if (parsestate == PARSESTATE.generic) {
            createGeneric(element);
        } else if (parsestate == PARSESTATE.event) {
            createEvent(element);
        } else if (parsestate == PARSESTATE.behaviour) {
            createBE(element);
        } else if (parsestate == PARSESTATE.object) {
            createOB(element);
        } else if (parsestate == PARSESTATE.callback) {
            createCB(element);
        }

    }
}
