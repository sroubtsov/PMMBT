/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package protocolmodelingmbt;

import protocolmodelingmbt.parser.ParseModel;
import java.util.ArrayList;
import protocolmodelingmbt.model.Behaviour;
import protocolmodelingmbt.model.Model;
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
        
        for(_Object ob: model.objects){
            OBTraceMaker.makeOBtraces(ob);
//            System.out.println("Events of " + ob.getName());
//            for (String ev: ob.getBEEventNames()){
//              System.out.println(ev);
//            };
//            ob.writeTraces();
        } 
        
        for(Behaviour be: model.behaviours){
            OBTraceMaker.makeBEtraces(be);
//            System.out.println("Events of " + be.getModelElementName());
//            for (String ev: be.getBEEventNames()){
//              System.out.println(ev);
//            };
//            
//            be.writeTraces();
        } 
        OBTraceMaker.makeModeltraces(model);
        for(String trace: model.traces){
            System.out.println(trace);
        }
        
        if ((args.length > 1)) {
           // WriteTestScripts.writeTestScripts(args[1],traces);
        } else {
           // WriteTestScripts.writeTestScripts("",traces);
        }
    }
}
