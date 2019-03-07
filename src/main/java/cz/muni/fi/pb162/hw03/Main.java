package cz.muni.fi.pb162.hw03;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * @author Ondřej Ernst
 */
public class Main {




    private static StringBuilder openFile(String fileName) {
        StringBuilder wholeFile = new StringBuilder();
        String line;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            while((line = bufferedReader.readLine()) != null) {
                wholeFile.append(line);
            }
            bufferedReader.close();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
        return wholeFile;
    }

    private static boolean switchHandler(String first, String one, ArrayList<String> theLine,
                                        ArrayList<Command> commands) {
        switch (first) {
            case "select":
                Select select = new Select(theLine);
                if (!select.checkSyntax(one)) return false;
                commands.add(select);
                break;
            case "insert":
                Insert insert = new Insert(theLine);
                if (!insert.checkSyntax(one)) return false;
                commands.add(insert);
                break;
            case "create":
                Create create = new Create(theLine);
                if (!create.checkSyntax(one)) return false;
                commands.add(create);
                break;
            case "drop":
                Drop drop = new Drop(theLine);
                if (!drop.checkSyntax(one)) return false;
                commands.add(drop);
                break;
            default:
                return false;
        }
        return true;
    }



    /**
     * @param args Arguments
     * @throws IOException ex
     */
    public static void main(String[] args) throws IOException {
        ArrayList<Command> commands = new ArrayList<>();
        if (args.length != 2) {
            System.out.println("usage: java -jar database.jar <path/to/statements_file.txt> <path/to/tables_folder>");
            return;
        }
        File f = new File(args[0]);
        if(!f.exists() || f.isDirectory()) {
            throw new IOException("file missing");
        }
        String fileName = args[0];
        StringBuilder wholeFile = openFile(fileName);
        ArrayList<String> lines;
        if (wholeFile.toString().trim().charAt(wholeFile.toString().trim().length() - 1) != ';') {
            System.err.println("Syntax error");
            return;
        }
        lines = new ArrayList<>(Arrays.asList(wholeFile.toString().trim().split(";")));
        for (String one : lines) {
            ArrayList<String> theLine = new ArrayList<>(Arrays.asList(one.split(",|\\)|\\(|\\s+")));
            theLine.removeIf(s -> s.length() == 0);
            String first = theLine.get(0).toLowerCase();
            if (!switchHandler(first, one, theLine, commands)) {
                System.err.println("Syntax error");
                return;
            }
        }

        for (Command c : commands) c.parse();
        for (Command c : commands) c.execute(args[1]);
    }
    }

