package protocolmodelingmbt.testmaker;

import java.util.ArrayList;
import protocolmodelingmbt.model.Behaviour;
import protocolmodelingmbt.model.Model;
import protocolmodelingmbt.model._Object;
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
        ArrayList<Behaviour> objectsANDbehaviours = new ArrayList<>();
        objectsANDbehaviours.addAll(model.objects);
        objectsANDbehaviours.addAll(model.behaviours);
        for (int i = 0; i < model.objects.size(); i++) {
            if (ParsingUtilities.ifDisjointArrayLists(model.getEventNames(), objectsANDbehaviours.get(i).getBEEventNames())) {
                System.out.println("Disjoint algrithm");
                model.traces.addAll(objectsANDbehaviours.get(i).getTraces());
            } else {
                model.traces.addAll(objectsANDbehaviours.get(i).getTraces());
                System.out.println("Woven algrithm");
                weaveTracesWith(model, objectsANDbehaviours.get(i));
                //model.traces.addAll(objectsANDbehaviours.get(i).getTraces());
            }
            model.getEventNames().addAll(ParsingUtilities.getUniqueArrayListElements(model.getEventNames(), objectsANDbehaviours.get(i).getBEEventNames()));
        }
    }

    private static void weaveTracesWith(Model model, Behaviour objectORbehaviour) {
        int mt = 0;
        String[] otransitions = null;
        String[] mtransitions = null;

        for (String mtrace : model.traces) {
            mtransitions = splitTrace(mtrace);
        }
        int ot = 0;

        for (String otrace : objectORbehaviour.getTraces()) {
            otransitions = splitTrace(otrace);
        }

        do {
            System.out.println(mtransitions[mt] + "@@@@" + otransitions[ot]);
            ot++;
            mt++;
        } while (mt > mtransitions.length);



    }

    private static String[] splitTrace(String trace) {
        String trace2parse = "";
        trace2parse.concat(trace);
        trace2parse = trace2parse.replace("-->", "@");
        return trace2parse.split("@");
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
}