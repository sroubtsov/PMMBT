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
    
    public static void makeModeltraces(Model model){
        for (int i = 0; i < model.objects.size(); i++){
            if(ParsingUtilities.ifDisjointArrayLists(model.getEventNames(),model.objects.get(i).getBEEventNames())){              
                System.out.println("Disjoint algrithm");
                model.getEventNames().addAll(ParsingUtilities.getUniqueArrayListElements(model.getEventNames(),model.objects.get(i).getBEEventNames()));
            } else {
                System.out.println("Woven algrithm");
            }
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
}