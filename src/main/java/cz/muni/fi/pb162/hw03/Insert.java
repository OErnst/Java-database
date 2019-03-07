package cz.muni.fi.pb162.hw03;


import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
/**
 * @author Ondřej Ernst
 */
public class Insert extends Command {
    private String tableName;
    private ArrayList<String> columns = new ArrayList<>();
    private ArrayList<String> values = new ArrayList<>();
    private String word = "";
    private boolean mustBeUvoz = true;
    private boolean mustBeComma = false;

    /**
     * creates Insert command
     * @param theLine - the command code
     */
    public Insert(ArrayList<String> theLine) {
        super(theLine);
    }

    @Override
    public void execute(String folderName) throws FileNotFoundException, UnsupportedEncodingException {
        MyDatabase md = new MyDatabase(folderName);
        Table table = md.loadTable(tableName);
        if (table == null) {
            System.err.println("Table " + tableName + " is missing");
            return;
        }
        table.addRow(values, columns);
        md.save(table);


    }

    @Override
    public void parse() {

        tableName = super.getTheLine().get(2);
        boolean wasValues = false;
        int i = -1;
        for (String one : super.getTheLine()) {
            i++;
            if (i < 3) {
                continue;
            }
            if (one.trim().toLowerCase().equals("values")) {
                wasValues = true;
                continue;
            }
            if (!wasValues) {
                columns.add(one);
            }
        }

    }

    /**
     *
     * @param part one of the code
     * @return true if correct
     */
    private boolean firstPart(String part) {
        if (part.trim().length() == 0) {
            return false;
        }
        ArrayList<String> theLine = new ArrayList<>(Arrays.asList(part.trim().split("\\s+")));
        if (theLine.size() < 3) {
            return false;
        }
        boolean first = theLine.size() == 3 && theLine.get(0).toLowerCase().equals("insert") &&
                theLine.get(1).toLowerCase().equals("into");
        boolean second = true;
        for (char x : theLine.get(2).toCharArray()) {
            if (!Character.isLetterOrDigit(x)) {
                second = false;
            }
        }
        //tableName = theLine.get(2);
        return first && second;
    }

    /**
     *
     * @param part with columns
     * @return true if correct
     */
    private boolean secondPart(String part) {
        if (part.trim().length() == 0) {
            return false;
        }
        part = part.trim();
        if(part.charAt(0) == ',' || part.charAt(part.length() - 1) == ',') {
            return false;
        }
        ArrayList<String> theLine = new ArrayList<>(Arrays.asList(part.trim().split(",")));
        for (String one : theLine) {
            if (one.trim().equals("")) {
                return false;
            }
            for (char x : one.trim().toCharArray()) {
                if (!Character.isLetterOrDigit(x)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     *
     * @param part with VALUES word
     * @return true if correct
     */
    private boolean thirdPart(String part) {
        return part.trim().toLowerCase().equals("values");
    }

    /**
     *
     * @param part with values
     * @return true if correct
     */
    private boolean fourthPart(String part) {
        if (part.trim().length() == 0) return false;
        part = part.trim();
        if(part.charAt(0) == ',' || part.charAt(part.length() - 1) == ',') return false;
        ArrayList<String> theLine = new ArrayList<>();

        if (!syntaxFail(part, theLine)) {
            return false;
        }

        values = new ArrayList<>(theLine);
        return true;
    }


    private boolean syntaxFailPart(char x, ArrayList<String> theLine) {
        if (Character.isWhitespace(x)) {
            if (!mustBeComma && !mustBeUvoz) word += x;
            return true;
        }
        if (mustBeComma) {
            if (x == ',') {
                mustBeComma = false;
                mustBeUvoz = true;
                return true;
            } else return false;
        }
        if (mustBeUvoz) {
            if (x == '\"') {
                word += x;
                mustBeUvoz = false;
                return true;
            } else return false;
        } else {
            if (x == '\"') {
                theLine.add(word + x);
                word = "";
                mustBeComma = true;
                return true;
            }
        }
        word += x;
        return true;
    }


    private boolean syntaxFail(String part, ArrayList<String> theLine) {
        for (char x : part.trim().toCharArray()) {
            if (!syntaxFailPart(x, theLine)) {
                return false;
            }
        }
        return word.equals("");
    }
    @Override
    public boolean checkSyntax(String line) {

        ArrayList<String> parts = new ArrayList<>(4);
        int startingIndex = 0;
        int mode = 0;
        boolean inValue = false;
        String cases = "()(";
        for (int i = 0; i < line.length(); i++) {
            if (mode < 3) {
                if (line.charAt(i) == cases.charAt(mode)) {
                    parts.add(line.substring(startingIndex, i));
                    startingIndex = i + 1;
                    mode++;
                }
            } else if (mode == 3){
                if (line.charAt(i) == '\"') {
                    inValue = !inValue;
                }
                if (line.charAt(i) == ')' && !inValue) {
                    parts.add(line.substring(startingIndex, i));
                    startingIndex = i + 1;
                    mode++;
                }
            } else {
                if (!Character.isWhitespace(line.charAt(i))) return false;
            }

        }

        if (parts.size() != 4) return false;


        return firstPart(parts.get(0)) && secondPart(parts.get(1)) && thirdPart(parts.get(2)) &&
                fourthPart(parts.get(3));
    }
}
