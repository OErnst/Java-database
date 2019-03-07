package cz.muni.fi.pb162.hw03;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
/**
 * @author Ondřej Ernst
 */
public class Create extends Command {
    private String tableName;
    private ArrayList<String> columns = new ArrayList<>();

    /**
     * creates Create command
     * @param theLine - the command code
     */
    public Create(ArrayList<String> theLine) {
        super(theLine);
    }

    @Override
    public void execute(String folderName) throws FileNotFoundException, UnsupportedEncodingException {
        File f = new File(folderName + File.separator + tableName + ".csv");
        if(f.exists()) {
            System.err.println("Table " + tableName + " already exists");
            return;
        }

        PrintWriter writer = new PrintWriter(folderName + File.separator + tableName + ".csv", "UTF-8");
        for (String column : columns) {
            writer.print(column + ";");
        }
        writer.print(System.lineSeparator());
        writer.close();
    }

    @Override
    public void parse() {
        int i = -1;
        for (String one : super.getTheLine()) {
            i++;
            if (i == 0) {
                continue;
            }
            if (i == 1) {
                continue;
            }
            if (i == 2) {
                tableName = one;
                continue;
            }
            columns.add(one);
        }



    }

    private boolean firstPart(String part) {
        if (part.trim().length() == 0) {
            return false;
        }
        ArrayList<String> theLine = new ArrayList<>(Arrays.asList(part.trim().split("\\s+")));
        if (theLine.size() != 3 || !theLine.get(0).trim().toLowerCase().equals("create") ||
                !theLine.get(1).trim().toLowerCase().equals("table")) {
            return false;
        }
        for (char x : theLine.get(2).toCharArray()) {
            if (!Character.isLetterOrDigit(x)) {
                return false;
            }
        }
        return true;
    }

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



    @Override
    public boolean checkSyntax(String line) {
        ArrayList<String> parts = new ArrayList<>(2);
        int startingIndex = 0;
        int mode = 0;
        for (int i = 0; i < line.length(); i++) {
            if (mode == 0) {
                if (line.charAt(i) == '(') {
                    parts.add(line.substring(startingIndex, i));
                    startingIndex = i + 1;
                    mode++;
                }
            } else if (mode == 1) {
                if (line.charAt(i) == ')') {
                    parts.add(line.substring(startingIndex, i));
                    startingIndex = i + 1;
                    mode++;
                }
            } else {
                if (!Character.isWhitespace(line.charAt(i))) {
                    return false;
                }
            }
        }
        if (parts.size() != 2) {
            return false;
        }
        return firstPart(parts.get(0)) && secondPart(parts.get(1));
    }
}
