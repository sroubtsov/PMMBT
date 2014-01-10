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


        for (String mtrace : model.traces) {//For each model trace
            int modelTraceIndex = model.traces.indexOf(mtrace);
            mtransitions = splitTrace(mtrace);
//            for (int k = 0; k < mtransitions.length; k++) {
//                System.out.println(mtransitions[k]);
//            }
            for (String otrace : objectORbehaviour.getTraces()) {//for each object trace
                otransitions = splitTrace(otrace);
//                for (int k = 0; k < otransitions.length; k++) {
//                    System.out.println(otransitions[k]);
//                }
                int mt = 0;
                int ot = 0;
                do { //do weawing
                    A = ParseModel.getEvent(mtransitions[mt]);
                    B = ParseModel.getEvent(otransitions[ot]);

                    if (A.equals(B)) { //A = B
                        mtransitions[mt] = doBeforeStateConcat(mtransitions[mt], ParseModel.beforeState(otransitions[ot]));
                        mtransitions[mt] = doAfterStateConcat(mtransitions[mt], ParseModel.afterState(otransitions[ot]));
                        System.out.println("A = B");
                        mt++;
                        ot++;
                    } else {//A != B
                        System.out.println("A != B");
                        if (!ParsingUtilities.getDuplicateArrayListElements(model.getEventNames(), objectORbehaviour.getBEEventNames()).contains(A)) {//A is NOT there
                            System.out.println("A is NOT in Es and Ei");
                            if (ParsingUtilities.getDuplicateArrayListElements(model.getEventNames(), objectORbehaviour.getBEEventNames()).contains(B)) { //B is there
                                mtransitions[mt] = doBeforeStateConcat(mtransitions[mt], ParseModel.beforeState(otransitions[ot]));
                                mtransitions[mt] = doAfterStateConcat(mtransitions[mt], ParseModel.beforeState(otransitions[ot]));
                                mt++;
                            }
                        } else {//A IS there
                            System.out.println("A IS  in Es and Ei");
                            if (ParsingUtilities.getDuplicateArrayListElements(model.getEventNames(), objectORbehaviour.getBEEventNames()).contains(B)) { //B is there
                                System.out.println("Deadlock"); //deadlock                          
                                break;
                            } else {//B is NOT there
                                System.out.println("B IS in Es and Ei");
                                mtransitions[mt] = doTrReplace(mtransitions[mt], otransitions[ot]);
                                ot++;
                            }
                        }
                    }
                } while ((mt < mtransitions.length) && (ot < otransitions.length));
                mtrace = glueTrace(mtransitions);

//                System.out.println("glued: " + mtrace);
                if (ot < otransitions.length) {
                    //TConactenate mtrasitions with the rest of otranstions;
                    otrace = glueTrace(otransitions);
                    mtrace = mtrace.replaceAll("|", "") /*it's not the termination yet*/ + glueTrace(otransitions); 
                    System.out.println("with tail: " + mtrace);
                }
                model.traces.set(modelTraceIndex, mtrace);

            }
        }

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

    private static String glueTrace(String[] transitions) {
        String trace = "";
        for (int i = 0; i < transitions.length; i++) {
            trace = trace + transitions[i] + "-->";
        }
        return trace + "|";
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

    private static String doTrReplace(String mtransition, String otransition) {
        String newmtransition = "";        
        //to make sure that the state is notin before states already
        newmtransition = doBeforeStateConcat(mtransition, ParseModel.beforeState(otransition)); 
        //change event
        newmtransition = ParseModel.beforeState(newmtransition) + "*" + ParseModel.getEvent(otransition) + "=" + ParseModel.beforeState(mtransition);
         //to make sure that the state is not in after states already
        newmtransition = doAfterStateConcat(newmtransition,ParseModel.afterState(otransition));
        
        return newmtransition;
    }
}