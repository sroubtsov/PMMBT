/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package protocolmodelingmbt;

import protocolmodelingmbt.parser.ParseModel;
import java.util.ArrayList;
import protocolmodelingmbt.model.Behaviour;
import protocolmodelingmbt.model.Model;
import protocolmodelingmbt.model.State;
import protocolmodelingmbt.model.Transition;
import protocolmodelingmbt.model._Object;
import protocolmodelingmbt.testmaker.OBTraceMaker;

/**
 *
 * @author sroubtso
 */
public class ProtocolModelingMBT {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        ArrayList<String> traces;
        if (args.length == 0) {
            System.out.println("Model file is not specified");
            System.exit(1);
        }
        ParseModel parsemodel = new ParseModel();
        Model model = parsemodel.parseModel(args[0]);

        //       model.writeModel("");
//        model.writeModel(args[1]);

        for (_Object ob : model.objects) {
            OBTraceMaker.makeOBtraces(ob);
//            System.out.println("Events of " + ob.getName());
//            for (String ev: ob.getBEEventNames()){
//              System.out.println(ev);
//            };
            ob.writeBehaviour();
            ob.writeTraces(0);
        }

        for (Behaviour be : model.behaviours) {
            OBTraceMaker.makeBEtraces(be);
//            System.out.println("Events of " + be.getModelElementName());
//            for (String ev: be.getBEEventNames()){
//              System.out.println(ev);
//            };
            be.writeBehaviour();
            be.writeTraces(0);
        }
        //OBTraceMaker.makeModeltraces(model);
        // NEW algorithm 
        //OBTraceMaker.weaveTracesFromObjects(model);
        ArrayList<Behaviour> pms = new ArrayList<>();
        pms.addAll(model.objects);
        pms.addAll(model.behaviours);

        Behaviour ocomp = pms.get(0);
        for (int i = 1; i < pms.size(); i++) {
            ocomp = OBTraceMaker.buildCSPComposition_a2(ocomp, pms.get(i));
            System.out.println("i=" + i);

        };

//        for (Transition tr : ocomp.getTransitions()) {
//            System.out.println(tr.getTransitionStr());
//        }
//        for (State  st: ocomp.getStates()){
//            System.out.println(st.getState());
//        }
        OBTraceMaker.makeOBtraces(ocomp);
        ocomp.writeTraces(0);
//        for(String trace: model.traces){
//            System.out.println(trace);
//        }

        if ((args.length > 1)) {
            // WriteTestScripts.writeTestScripts(args[1],traces);
        } else {
            // WriteTestScripts.writeTestScripts("",traces);
        }
    }
}
