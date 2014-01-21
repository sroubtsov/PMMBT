package protocolmodelingmbt.testmaker;

import java.util.ArrayList;
import protocolmodelingmbt.model.Attribute;
import protocolmodelingmbt.model.Behaviour;
import protocolmodelingmbt.model.Event;
import protocolmodelingmbt.model.Model;
import protocolmodelingmbt.model.State;
import protocolmodelingmbt.model.Transition;
import protocolmodelingmbt.model._Object;
import protocolmodelingmbt.parser.Grammar;
import protocolmodelingmbt.parser.ParseModel;
import protocolmodelingmbt.parser.ParsingUtilities;
import protocolmodelingmbt.parser.TransitionNode;

public class OBTraceMaker {

    public static void makeOBtraces(Behaviour object) {
        //    ArrayList<String> traces = new ArrayList();

        for (int i = 0; i < object.getTransitions().size(); i++) {
            String transition = object.getTransitions().get(i).getTransitionStr();

            String path = "";

                      System.out.println(" be " + object.getTransitions().get(i).getBeforeState().getState());
            if (/*object.getTransitions().get(i).getBeforeState().getState().contains("@new")*/stateContainsOnly(object.getTransitions().get(i).getBeforeState().getState(), "@new")) {
                TransitionNode root = new TransitionNode(object.getTransitions().get(i).getBeforeState().getState(), transition, object.getTransitionStrings());
                traverse(root, object, path);
                //break;
            }
        }
        //  object.getTraces().addAll(traces);
    }

    private static boolean stateContainsOnly(String state, String subst) {
        boolean only = true;
        String[] states = state.split("&");
        for (int i = 0; i < states.length; i++) {
            if (!states[i].contains(subst)) {
                only = false;
                return only;
            }
        }
        return only;
    }

    public static void makeBEtraces(Behaviour behaviour) {
        String transition = "";
        for (int i = 0; i < behaviour.getTransitions().size(); i++) {
            transition = /*ParsingUtilities.parseMSname(Grammar.BEHAVIOUR, behaviour.getModelElementName()) + "."
                     + */ behaviour.getTransitions().get(i).getBeforeState().getState() + "*"
                    + behaviour.getTransitions().get(i).getAction().getAction() + "="
                    + /*ParsingUtilities.parseMSname(Grammar.BEHAVIOUR, behaviour.getModelElementName()) + "."
                     + */ behaviour.getTransitions().get(i).getAfterState().getState();

            //getTransitionStr();
            behaviour.getTraces().add(transition);
        }

    }

//    public static void makeModeltraces(Model model) {
//        ArrayList<Behaviour> protocolMachines = new ArrayList<>();
//        protocolMachines.addAll(model.objects);
//        protocolMachines.addAll(model.behaviours);
//        for (int i = 0; i < protocolMachines.size(); i++) {
//            if (ParsingUtilities.ifDisjointArrayLists(model.getEventNames(), protocolMachines.get(i).getBEEventNames())) {
//                System.out.println("Disjoint algorithm for " + protocolMachines.get(i).getModelElementName());
//                model.traces.addAll(protocolMachines.get(i).getTraces());
//            } else {
//                System.out.println("Woven algorithm");
//                weaveTracesWith(model, protocolMachines.get(i));
//                //               model.traces.addAll(protocolMachines.get(i).getTraces());
//            }
//            model.getEventNames().addAll(ParsingUtilities.getUniqueArrayListElements(model.getEventNames(), protocolMachines.get(i).getBEEventNames()));
//        }
//    }
    private static void makeTraceForAllowedEvents(Model model, ArrayList<Behaviour> protocolMachines, ArrayList<String> allowedEventNames) {
        ArrayList<String> nextAllowedEventNames = new ArrayList<>();
        ArrayList<String> firedEventNames = new ArrayList<>();
        nextAllowedEventNames.addAll(allowedEventNames);
        for (String ev : allowedEventNames) {
            boolean allowed = true;
            ArrayList<Behaviour> involvedPMs = new ArrayList<>();
            for (Behaviour o : protocolMachines) {
                if (o.getBEEventNames().contains(ev)) {
                    //            System.out.println(o.getModelElementName() + "  state=" + o.getCurrentState() + " event = " + ev);
                    if (!o.getbeforeStateForEventName(ev).equals(o.getCurrentState())) {
                        allowed = false;//event is NOT allowed
                        //                  System.out.println(allowed);
                        break; //into event firing
                    } else {
                    }
                    involvedPMs.add(o);
                } else {
                    //nothing?
                }
                if (allowed) {
                    firedEventNames.add(ev);
                    nextAllowedEventNames.remove(ev);
                }
            }
            if (allowed) {

                //Put the corresponding objects into corresponding states
                for (Behaviour o : involvedPMs) {
                    o.setAfterStateAsCurrentForEventName(ev);
                    //                System.out.println(o.getModelElementName() + "  state=" + o.getCurrentState());
                }
                //Place the transition into the trace
                for (String fev : firedEventNames) {
                    System.out.print(concatPMBeforeStatesForAllowedEvent(ev, involvedPMs) + "*" + fev + "=" + concatPMAfterStatesForAllowedEvent(ev, involvedPMs) + " --> ");
                }
                if (!nextAllowedEventNames.isEmpty()) {
                    makeTraceForAllowedEvents(model, protocolMachines, nextAllowedEventNames);
                } else {
                    System.out.println("|");
                }

            }
            //Go further

        }




    }

    private static String concatPMBeforeStatesForAllowedEvent(String ev, ArrayList<Behaviour> protocolMachines) {
        String beforeState = "";
        for (Behaviour pm : protocolMachines) {
            beforeState = beforeState + ParsingUtilities.parseMSname(Grammar.OBJECT, pm.getModelElementName()) + "." + pm.getbeforeStateForEventName(ev) + "&";
        }
        return beforeState;
    }

    private static String concatPMAfterStatesForAllowedEvent(String ev, ArrayList<Behaviour> protocolMachines) {
        String afterState = "";
        for (Behaviour pm : protocolMachines) {
            afterState = afterState + ParsingUtilities.parseMSname(Grammar.OBJECT, pm.getModelElementName()) + "." + pm.getafterStateForEventName(ev) + "&";
        }
        return afterState;
    }

    public static Behaviour buildCSPComposition(Behaviour o1, Behaviour o2) {
        Behaviour ocomp;
        ocomp = new Behaviour(new ArrayList<Attribute>(), new ArrayList<State>(), new ArrayList<Transition>());
        ocomp.setModelElementName(o1.getModelElementName() + "&" + o2.getModelElementName());
        System.out.println("=== start ===");
        ArrayList<String> o1o2EIntersection = ParsingUtilities.getDuplicateArrayListElements(o1.getBEEventNames(), o2.getBEEventNames());
        for (int i = 0; i < o1o2EIntersection.size(); i++) {
            System.out.println(" intersection " + i + " " + o1o2EIntersection.get(i));
        }
        for (int i = 0; i < o1.getStates().size(); i++) {
            for (int j = 0; j < o2.getStates().size(); j++) {
                for (int k = 0; k < o1.getStates().size(); k++) {
                    for (int h = 0; h < o2.getStates().size(); h++) {
                        //                    System.out.println(" i =" + i + " j =" + j + " k =" + k + " h =" + h);
                        //                    System.out.println("before: " + o1.getStates().get(i).getState() + o2.getStates().get(j).getState() + " after: " + o1.getStates().get(k).getState() + o2.getStates().get(h).getState());
                        ArrayList<String> o1Eset = getEventsForBeforeAndAfterStates(o1, o1.getStates().get(i), o1.getStates().get(k));
                        ArrayList<String> o2Eset = getEventsForBeforeAndAfterStates(o2, o2.getStates().get(j), o2.getStates().get(h));
                        //System.out.println("o1:");
                        for (String ev : o1Eset) {
                            //                        System.out.println(o1.getStates().get(i).getState() + "*" + ev + "=" + o1.getStates().get(k).getState() + " i =" + i + " j =" + j + " k =" + k + " h =" + h);
                        }
                        //System.out.println("o2:");
                        for (String ev : o2Eset) {
                            //                         System.out.println(o2.getStates().get(j).getState() + "*" + ev + "=" + o2.getStates().get(h).getState() + " i =" + i + " j =" + j + " k =" + k + " h =" + h);
                        }

                        //System.out.println("o1E size = " + o1Eset.size() + " o2E size = " + o2Eset.size());
                        boolean addtransition = false;
                        if (!o1Eset.isEmpty() && !o2Eset.isEmpty()) {


                            for (String ev : ParsingUtilities.getUnionOfArrayListElements(o1Eset, o2Eset)) {
                                Transition tr1 = new Transition(new State(o1.getStates().get(i).getState()),
                                        new Event(ev), new State(o1.getStates().get(k).getState()));

                                Transition tr2 = new Transition(new State(o2.getStates().get(j).getState()),
                                        new Event(ev), new State(o2.getStates().get(h).getState()));


                                if (o1.getTransitions().contains(tr1) && o2.getTransitions().contains(tr2)) {//i.e. both transitions have the same event
                                    addtransition = true;
                                    Transition tr = new Transition(new State(o1.getStates().get(i).getState() + "&"
                                            + o2.getStates().get(j).getState()),
                                            new Event(ev),
                                            new State(o1.getStates().get(k).getState() + "&" + o2.getStates().get(h).getState()));
                                    System.out.println("case 1 " + tr.getTransitionStr());
                                    ocomp.getTransitions().add(tr);

                                } else {
                                    if ((o2.getStates().get(j) != o2.getStates().get(h)) && (o1.getStates().get(i) == o1.getStates().get(k))
                                            || (o1.getStates().get(i) != o1.getStates().get(k)) && (o2.getStates().get(j) == o2.getStates().get(h))) {
                                        addtransition = true;

                                        Transition tr = new Transition(new State(o1.getStates().get(i).getState() + "&"
                                                + o2.getStates().get(j).getState()),
                                                new Event(ev),
                                                new State(o1.getStates().get(k).getState() + "&" + o2.getStates().get(h).getState()));
                                        System.out.println("case 1+ " + tr.getTransitionStr());
                                        ocomp.getTransitions().add(tr);

                                    } else {
                                        //
                                    }
                                }
                            }

                        } else if (!o1Eset.isEmpty() && (o2.getStates().get(j) == o2.getStates().get(h) && o2Eset.isEmpty())) {
                            for (String ev : o1Eset) {
                                if (!o1o2EIntersection.contains(ev)) {
                                    addtransition = true;

                                    Transition tr = new Transition(new State(o1.getStates().get(i).getState() + "&"
                                            + o2.getStates().get(j).getState()),
                                            new Event(ev),
                                            new State(o1.getStates().get(k).getState() + "&" + o2.getStates().get(h).getState()));
                                    System.out.println("case 2 " + tr.getTransitionStr());
                                    ocomp.getTransitions().add(tr);
                                }
                            }
                        } else if (!o2Eset.isEmpty() && (o1.getStates().get(i) == o1.getStates().get(k)) && o1Eset.isEmpty()) {
                            for (String ev : o2Eset) {
                                if (!o1o2EIntersection.contains(ev)) {
                                    addtransition = true;

                                    Transition tr = new Transition(new State(o1.getStates().get(i).getState() + "&"
                                            + o2.getStates().get(j).getState()),
                                            new Event(ev),
                                            new State(o1.getStates().get(k).getState() + "&" + o2.getStates().get(h).getState()));
                                    System.out.println("case 3 " + tr.getTransitionStr());
                                    ocomp.getTransitions().add(tr);
                                }
                            }
                        }//<====
                        if (addtransition) {
                            State begst = new State(o1.getStates().get(i).getState() + "&" + o2.getStates().get(j).getState());
                            if (!ocomp.getstateNames().contains(begst.getState())) {
                                ocomp.getStates().add(begst);
                            }
                            State endst = new State(o1.getStates().get(k).getState() + "&" + o2.getStates().get(h).getState());
                            if (!ocomp.getstateNames().contains(endst.getState())) {
                                ocomp.getStates().add(endst);
                            }
                        }
                    }//for h
                }//for k
            }//for j
        }//for i
        return ocomp;

    }

    private static String[] splitTrace(String trace) {
        if (trace.contains("-->")) {
            trace = trace.replace("-->", ">");
            trace = trace.replace(">|", ""); //Termination is not a transiton

            return trace.split(">");
        } else {
            String[] oneTransactionOnly = new String[1];
            oneTransactionOnly[0] = trace;
            return oneTransactionOnly;
        }

    }

    private static String glueTrace(String[] transitions, int istart) {
        String trace = "";
        for (int i = istart; i < transitions.length; i++) {
            trace = trace + transitions[i] + "-->";
        }
        return trace + "|";
    }

    private static void traverse(TransitionNode root, Behaviour behaviour, String path) {

        path += root.getTransitionValues(behaviour) + "-->";
        for (TransitionNode child : root.getChildren()) {
            traverse(child, behaviour, path);
        }

        // end of current traversal
        if (root.getChildren().isEmpty()) {
            path += "|";
            behaviour.getTraces().add(path);
            //               System.out.println("path " + path);
        }
    }

    private static String doBeforeStateConcat(String mtransition, String state) {
        if (!ParseModel.beforeState(mtransition).contains(state)) {
            return ParseModel.beforeState(mtransition) + "&" + state + "*" + ParseModel.getEvent(mtransition) + "=" + ParseModel.afterState(mtransition);
        } else {
            return mtransition;
        }
    }

    private static String doAfterStateConcat(String mtransition, String state) {
        if (!ParseModel.afterState(mtransition).contains(state)) {
            return ParseModel.beforeState(mtransition) + "*" + ParseModel.getEvent(mtransition) + "=" + ParseModel.afterState(mtransition) + "&" + state;
        } else {
            return mtransition;
        }
    }

//    private static String doTrReplaceAndAdd(String mtransition, String otransition) {
//        String newmtransition = "";
//        String insmttransition = "";
//        //       System.out.print(mtransition);
//        //       System.out.println(" and "+otransition);
//        //before state change
//        newmtransition = doBeforeStateConcat(mtransition, ParseModel.beforeState(otransition));
//        //change event
//        newmtransition = ParseModel.beforeState(newmtransition) + "*" + ParseModel.getEvent(otransition) + "=" + ParseModel.beforeState(mtransition);
//        //afterstate change
//        newmtransition = doAfterStateConcat(newmtransition, ParseModel.afterState(otransition));
//
//        //insert transaction
//        //SR this is bad but for the time being... TODO: Separate funntion for inserting transition?
//        //beforestate change in the initial one
//        insmttransition = doBeforeStateConcat(mtransition, ParseModel.beforeState(otransition));// + "*"
//        //afterstate change in the initial one
//        insmttransition = doAfterStateConcat(insmttransition, ParseModel.beforeState(otransition));
//        //concat the both
//        newmtransition = newmtransition + "-->" + insmttransition;
//        //      System.out.println("become "+newmtransition);
//        return newmtransition;
//    }
    private static ArrayList<State> CartesianProductOfStates(Behaviour be1, Behaviour be2) {
        ArrayList<State> stcartproduct = new ArrayList<>();
        int i = 0;
        for (State st1 : be1.getStates()) {
            for (State st2 : be2.getStates()) {
                stcartproduct.add(new State(st1.getState() + "&" + st2.getState()));
                System.out.println(stcartproduct.get(i++).getState());
            }
        }
        return stcartproduct;
    }

    private static ArrayList<String> intersectionOfEvents(Behaviour be1, Behaviour be2) {
        return ParsingUtilities.getDuplicateArrayListElements(be1.getBEEventNames(), be2.getBEEventNames());
    }

    private static ArrayList<String> unionOfEvents(Behaviour be1, Behaviour be2) {
        return ParsingUtilities.getUnionOfArrayListElements(be1.getBEEventNames(), be2.getBEEventNames());
    }

    private static ArrayList<String> getEventsForBeforeAndAfterStates(Behaviour o, State beginst, State endst) {
        ArrayList<String> events = new ArrayList<>();

        for (Transition tr : o.getTransitions()) {
            if (tr.getBeforeState().getState().equals(beginst.getState()) && tr.getAfterState().getState().equals(endst.getState())) {
                //                        System.out.println("beg " + beginst.getState() + " end " + endst.getState() + " tr " + tr.getTransitionStr());

                events.add(tr.getAction().getAction());
            }
        }
        return events;
    }
}
