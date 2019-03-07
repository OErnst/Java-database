package cz.muni.fi.pb162.hw03;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Ondřej Ernst
 */
public class MyDatabase {
    private String folderPath;

    /**
     * creates database to work with files
     * @param folderPath of the files
     */
    public MyDatabase(String folderPath) {
        this.folderPath = folderPath;
    }

    /**
     * loads the table
     * @param tableName name
     * @return table or null if couldnt be opened
     */
    public Table loadTable(String tableName) {
        String line;
        ArrayList<String> lines = new ArrayList<>();
        File f = new File(folderPath + File.separator + tableName + ".csv");
        if(!f.exists() || f.isDirectory()) {
            return null;
        }
        try {
            BufferedReader bufferedReader =
                    new BufferedReader(new FileReader(folderPath + File.separator + tableName + ".csv"));
            while((line = bufferedReader.readLine()) != null) {
                if (line.trim().equals("")) {
                    continue;
                }
                lines.add(line);
            }
            bufferedReader.close();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
        Table table = new Table(tableName);
        table.createColumns(new ArrayList<>(Arrays.asList(lines.get(0).split(";"))));
        for (int i = 1; i < lines.size(); i++) {
            table.addRow(new ArrayList<>(Arrays.asList(lines.get(i).split(";"))));
        }
        return table;
    }

    /**
     * saves the table
     * @param table to be saved
     * @throws FileNotFoundException ex
     * @throws UnsupportedEncodingException ex
     */
    public void save(Table table) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer =
                new PrintWriter(folderPath + File.separator + table.getTableName() + ".csv", "UTF-8");
        writer.print(table);
        writer.close();
    }







}
