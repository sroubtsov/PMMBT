package protocolmodelingmbt.testmaker;

import java.util.ArrayList;
import protocolmodelingmbt.model.Behaviour;
import protocolmodelingmbt.model.Model;
import protocolmodelingmbt.model._Object;
import protocolmodelingmbt.parser.ParseModel;
import protocolmodelingmbt.parser.ParsingUtilities;
import protocolmodelingmbt.parser.TransitionNode;

public class OBTraceMaker {

    public static void makeOBtraces(_Object object) {
        //    ArrayList<String> traces = new ArrayList();

        for (int i = 0; i < object.getTransitions().size(); i++) {
            String transition = object.getTransitions().get(i).getTransitionStr();
            String path = "";

            //           System.out.println(" be " + behaviour.getTransitions().get(i).getBeforeState().getState());

            if (object.getTransitions().get(i).getBeforeState().getState().equals("@new")) {
                TransitionNode root = new TransitionNode("@new", transition, object.getTransitionStrings());
                traverse(root, object, path);
                break;
            }
        }
        //  object.getTraces().addAll(traces);
    }

    public static void makeBEtraces(Behaviour behaviour) {
        String transition = "";
        for (int i = 0; i < behaviour.getTransitions().size(); i++) {
            transition = behaviour.getTransitions().get(i).getTransitionStr();
            behaviour.getTraces().add(transition);
        }

    }

    public static void makeModeltraces(Model model) {
        ArrayList<Behaviour> protocolMachines = new ArrayList<>();
        protocolMachines.addAll(model.objects);
        protocolMachines.addAll(model.behaviours);
        for (int i = 0; i < protocolMachines.size(); i++) {
            if (ParsingUtilities.ifDisjointArrayLists(model.getEventNames(), protocolMachines.get(i).getBEEventNames())) {
                System.out.println("Disjoint algrithm for " + protocolMachines.get(i).getModelElementName());
                model.traces.addAll(protocolMachines.get(i).getTraces());
            } else {
                System.out.println("Woven algrithm");
                weaveTracesWith(model, protocolMachines.get(i));
                //               model.traces.addAll(protocolMachines.get(i).getTraces());
            }
            model.getEventNames().addAll(ParsingUtilities.getUniqueArrayListElements(model.getEventNames(), protocolMachines.get(i).getBEEventNames()));
        }
    }

    private static void weaveTracesWith(Model model, Behaviour objectORbehaviour) {
        String[] otransitions = null;
        String[] mtransitions = null;
        String A = null;
        String B = null;
        int mt = 0;
        for (String mtrace : model.traces) {
            mtransitions = splitTrace(mtrace);
            for (int k = 0; k < mtransitions.length; k++) {
                System.out.println(mtransitions[k]);
            }

            int ot = 0;
            for (String otrace : objectORbehaviour.getTraces()) {
                otransitions = splitTrace(otrace);
                for (int k = 0; k < otransitions.length; k++) {
                    System.out.println(otransitions[k]);
                }
                do {
                    A = ParseModel.getEvent(mtransitions[mt]);
                    B = ParseModel.getEvent(otransitions[ot]);

                    if (A.equals(B)) { //A = B
                        doStatesConcat(ParseModel.beforeState(mtransitions[mt]), ParseModel.beforeState(otransitions[ot]));
                        doStatesConcat(ParseModel.afterState(mtransitions[mt]), ParseModel.afterState(otransitions[ot]));

                        System.out.println("A = B");

                        mt++;
                        ot++;
                    } else {//A != B
                        System.out.println("A != B");

                        if (!ParsingUtilities.getDuplicateArrayListElements(model.getEventNames(), objectORbehaviour.getBEEventNames()).contains(A)) {//A is NOT there

                            System.out.println("A is NOT there");

                            if (ParsingUtilities.getDuplicateArrayListElements(model.getEventNames(), objectORbehaviour.getBEEventNames()).contains(B)) { //B is there
                                doStatesConcat(ParseModel.beforeState(mtransitions[mt]), ParseModel.beforeState(otransitions[ot]));
                                doStatesConcat(ParseModel.afterState(mtransitions[mt]), ParseModel.beforeState(otransitions[ot]));
                                mt++;
                            }
                        } else {//A IS there

                            System.out.println("A IS there");

                            if (ParsingUtilities.getDuplicateArrayListElements(model.getEventNames(), objectORbehaviour.getBEEventNames()).contains(B)) { //B is there
                                System.out.println("Deadlock"); //deadlock
                                break;
                            } else {//B is NOT there
                                System.out.println("B IS there");
                                doTrReplace(mtransitions[mt], otransitions[ot]);
                                ot++;
                            }
                        }
                    }
                } while (mt < mtransitions.length);
                if (ot <= otransitions.length) {
                    //TODO Conactenate mtransitions with the rest of otransitions
                }
            }
        }
//TODO Put modified mtransitions back into model.traces
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

    private static void traverse(TransitionNode root, Behaviour behaviour, String path) {

        path += root.getTransitionValues() + "-->";
        for (TransitionNode child : root.getChildren()) {
            traverse(child, behaviour, path);
        }

        // end of current traversal
        if (root.getChildren().isEmpty()) {
            path += "|";
            behaviour.getTraces().add(path);
            //       System.out.println("path " + path);
        }
//        return path;
    }

    private static void doStatesConcat(String state1, String State2) {
        System.out.println("doStatesConcat: Not supported yet.");
    }

    private static void doTrReplace(String transitionBefore, String transitioonAfter) {
        System.out.println("doTrReplace: Not supported yet.");
    }
}