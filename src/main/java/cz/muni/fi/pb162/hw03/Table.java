package cz.muni.fi.pb162.hw03;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * @author Ondřej Ernst
 */
public class Table {

    private String tableName;
    private ArrayList<String> indexesOfColumns = new ArrayList<>();
    private Map<String, ArrayList<String>> columns = new HashMap<>();

    /**
     * creates class Table
     * @param tableName name
     */
    public Table(String tableName) {
        this.tableName = tableName;
    }


    public Map<String, ArrayList<String>> getColumns() {
        return columns;
    }

    /**
     * creates columns in table
     * @param columns columns
     */
    public void createColumns(ArrayList<String> columns) {
        for (String one : columns) {
            if (one.trim().equals("")) {
                continue;
            }
            this.indexesOfColumns.add(one);
            this.columns.put(one, new ArrayList<>());
        }
    }

    /**
     * adds whole row
     * @param row row
     */
    public void addRow(ArrayList<String> row) {
        int i = 0;
        for (String one : row) {
            columns.get(indexesOfColumns.get(i)).add(one);
            i++;
        }
    }


    public String getTableName() {
        return tableName;
    }

    /**
     * adds one row for specified columns
     * @param row row
     * @param columnsToInsert columns
     */
    public void addRow(ArrayList<String> row, ArrayList<String> columnsToInsert) {
        int newLength = 0;
        for (int i = 0; i < row.size(); i++) {
            columns.get(columnsToInsert.get(i)).add(row.get(i));
            newLength = columns.get(columnsToInsert.get(i)).size();
        }

        for (Map.Entry<String, ArrayList<String>> entry : columns.entrySet()) {
            if (entry.getValue().size() < newLength) {
                entry.getValue().add("\"\"");
            }
        }

    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (String one : indexesOfColumns) {
            result.append(one).append(";");
        }
        result.append(System.lineSeparator());

        int length = columns.get(indexesOfColumns.get(0)).size();
        for (int i = 0; i < length; i++) {
            for (String one : indexesOfColumns) {
                result.append(columns.get(one).get(i)).append(";");
            }
            result.append(System.lineSeparator());
        }
        return result.toString();
    }

}
