package cz.muni.fi.pb162.hw03;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * @author Ondřej Ernst
 */
public abstract class Command {


    private ArrayList<String> theLine;
    public ArrayList<String> getTheLine() {
        return theLine;
    }
    /**
     * creates class Command
     * @param theLine with code
     */
    public Command(ArrayList<String> theLine) {
        this.theLine = new ArrayList<>(theLine);
    }

    /**
     * executes the command
     * @param folderName name of working folder
     * @throws FileNotFoundException files
     * @throws UnsupportedEncodingException files
     */
    public abstract void execute(String folderName) throws FileNotFoundException, UnsupportedEncodingException;

    /**
     * parses the code so that it can be executed
     */
    public abstract void parse();

    /**
     * checks syntax
     * @param line the command
     * @return true if the syntax is correct, false otherwise
     */
    public abstract boolean checkSyntax(String line);


}
