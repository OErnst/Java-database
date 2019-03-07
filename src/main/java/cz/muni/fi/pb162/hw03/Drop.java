package cz.muni.fi.pb162.hw03;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
/**
 * @author Ondřej Ernst
 */
public class Drop extends Command {
    private String tableName;

    /**
     * creates Drop command
     * @param theLine - the command code
     */
    public Drop(ArrayList<String> theLine) {
        super(theLine);
    }

    @Override
    public void execute(String folderName) {
        File file = new File(folderName + File.separator + tableName + ".csv");
        if(!file.delete()) {
            System.err.println("Table " + tableName + " is missing");
        }
    }

    @Override
    public void parse() {
        tableName = super.getTheLine().get(2);
    }

    @Override
    public boolean checkSyntax(String line) {
        ArrayList<String> theLine = new ArrayList<>(Arrays.asList(line.trim().split("\\s+")));
        boolean first = theLine.size() > 2 && theLine.size() == 3 && theLine.get(0).trim().toLowerCase().equals("drop")
                && theLine.get(1).trim().toLowerCase().equals("table");
        if (!first) {
            return false;
        }
        boolean second = true;
        for (char x : theLine.get(2).toCharArray()) {
            if (!Character.isLetterOrDigit(x)) {
                second = false;
            }
        }
        return first && second;
    }

}
