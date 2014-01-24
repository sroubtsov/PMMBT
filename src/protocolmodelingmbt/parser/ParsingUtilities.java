package protocolmodelingmbt.parser;

import java.util.ArrayList;
import protocolmodelingmbt.model.Types;

public class ParsingUtilities {

    public static String parseMSname(String prefix, String nameline) {
        if (!nameline.isEmpty()) {
            if (nameline.contains(prefix)) {
                return nameline.substring(nameline.lastIndexOf(prefix) + prefix.length());
            } else {
                return nameline;
            }
        } else {
            return "";
        }

    }
//

    public static String[] parseMSelement(String str2parse, String separator) {
        String[] tokens = str2parse.split(separator);
//        if(tokens.length == 0){
//            tokens = new String[2];
//            tokens[0] = "null";            
//            tokens[1] = "null";
//        };
//        if(tokens.length == 1){
//            String firstelem = tokens[0];
//            tokens = new String[2];
//            tokens[0] = firstelem;            
//            tokens[1] = "null";            
//        };
        return tokens;
    }

    public static String ArrayList2Str(ArrayList<String> list) {
        String liststring = "";
        for (String s : list) {
            liststring += s;
        }
        return liststring;
    }

    public static String StringArray2Str(String[] strarr) {
        String arraystring = "";
        for (int i = 0; i < strarr.length; i++) {
            arraystring.concat(strarr[i]);
        }
        return arraystring;
    }

    public static boolean existsInTypes(String test) {

        for (Types c : Types.values()) {
            if (c.name().equals(test)) {
                return true;
            }
        }
        return false;
    }

    public static Types returnType(String test) {

        for (Types c : Types.values()) {
            if (c.name().equals(test)) {
                return c;
            }
        }
        return Types.REF;
    }

    public static boolean ifDisjointArrayLists(ArrayList<String> hostList/*model*/, ArrayList<String> guestList/*object*/) {
        boolean ifdisjoint = true;
        for (String item : guestList) {
            if (hostList.contains(item)) {
                ifdisjoint = false;
                return ifdisjoint;
            }
        }
        return ifdisjoint;
    }

    public static ArrayList<String> getUniqueArrayListElements(ArrayList<String> hostList/*model*/, ArrayList<String> guestList/*object*/) {
        ArrayList<String> uniques = new ArrayList<>();
        for (String item : guestList) {
            if (!hostList.contains(item)) {
                uniques.add(item);
            }
        }
        return uniques;
    }

    public static ArrayList<String> getDuplicateArrayListElements(ArrayList<String> hostList/*model*/, ArrayList<String> guestList/*object*/) {
        ArrayList<String> duplicates = new ArrayList<>();
        for (String item : guestList) {
            if (hostList.contains(item)) {
                duplicates.add(item);
            }
        }
        return duplicates;
    }

    public static ArrayList<String> getUnionOfArrayListElements(ArrayList<String> hostList/*model*/, ArrayList<String> guestList/*object*/) {
        ArrayList<String> duplicates = new ArrayList<>();
        duplicates.addAll(hostList);
        for (String item : guestList) {
            if (!duplicates.contains(item)) {
                duplicates.add(item);
            }
        }
        
        return duplicates;
    }
}