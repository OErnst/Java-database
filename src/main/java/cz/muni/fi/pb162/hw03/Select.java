package cz.muni.fi.pb162.hw03;

import java.util.ArrayList;
import java.util.Arrays;
/**
 * @author Ondřej Ernst
 */
public class Select extends Command {

    private ArrayList<String> columns = new ArrayList<>();
    private String tableName = new String("");

    /**
     * creates Select command
     * @param theLine - the command code
     */
    public Select(ArrayList<String> theLine) {
        super(theLine);
    }

    @Override
    public void execute(String folderName) {
        MyDatabase md = new MyDatabase(folderName);
        Table table = md.loadTable(tableName);
        if (table == null) {
            System.err.println("Table " + tableName + " is missing");
            return;
        }
        int length = table.getColumns().get(columns.get(0)).size();
        for (int i = 0; i < length; i++) {
            int j = 0;
            for (String one : columns) {

                System.out.print(table.getColumns().get(one).get(i).trim().substring(1,
                        table.getColumns().get(one).get(i).trim().length() - 1));

                if (j + 1 != columns.size()) {
                    System.out.print(";");
                }
                j++;
            }
            System.out.println();
        }

    }

    @Override
    public void parse() {

        int i = 0;
        boolean wasFrom = false;
        for (String one : super.getTheLine()) {
            String oneLower = one.toLowerCase();
            if (i == 0) {
                i++;
                continue;
            }
            if (oneLower.equals("from") && !wasFrom) {
                wasFrom = true;
                continue;

            }
            if (!wasFrom) {
                columns.add(one);
                continue;
            }
            tableName = one;
            i++;
        }
    }
    @Override
    public boolean checkSyntax(String line) {
        ArrayList<String> theLine = new ArrayList<>(Arrays.asList(line.trim().split(",")));
        if (theLine.size() < 1) {
            return false;
        }
        String first = theLine.get(0);
        String last = theLine.get(theLine.size() - 1);
        ArrayList<String> firstPart = new ArrayList<>(Arrays.asList(first.trim().split("\\s+")));
        ArrayList<String> lastPart = new ArrayList<>(Arrays.asList(last.trim().split("\\s+")));
        if (!selectFail(theLine, firstPart, lastPart)) {
            return false;
        }
        if (!firstPart.get(0).trim().toLowerCase().equals("select")) {
            return false;
        }
        theLine.set(0, firstPart.get(1));
        if (theLine.size() != 1) {
            theLine.set(theLine.size() - 1, lastPart.get(0));
        }
        for (String one : theLine) {
            for (char x : one.trim().toCharArray()) {
                if (!Character.isLetterOrDigit(x)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean selectFail(ArrayList<String> theLine, ArrayList<String> firstPart, ArrayList<String> lastPart) {
        if (theLine.size() == 1) {
            if (firstPart.size() != 4) {
                return false;
            }
            if (!lastPart.get(2).trim().toLowerCase().equals("from")) {
                return false;
            }
        } else {
            if (firstPart.size() != 2) {
                return false;
            }
            if (lastPart.size() != 3) {
                return false;
            }
            if (!lastPart.get(1).trim().toLowerCase().equals("from")) {
                return false;
            }
        }
        return true;
    }


}
