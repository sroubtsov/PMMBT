/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package protocolmodelingmbt;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author S. Roubtsov
 */
public class WriteTestScripts {

    public static void writeTestScripts(String scriptFileName, ArrayList transitions) {
        if (!scriptFileName.isEmpty()) {
            try (BufferedWriter out = new BufferedWriter(new FileWriter(scriptFileName))) {
                for (int i = 0; i < transitions.size(); i++) {
                    out.write(transitions.get(i) + "\n");
                }
            } catch (IOException eio) {
                System.err.println("IOException: " + eio.getMessage());
                System.exit(1);
            }

        } else {
            for (int i = 0; i < transitions.size(); i++) {
                System.out.println(transitions.get(i));
            }

        }
    }
}
