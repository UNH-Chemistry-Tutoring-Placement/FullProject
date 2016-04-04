import FileIO.StudentIO;
import GUI.Frame;
import LocalSearch.LocalSearch;
import Validator.Validate;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TutoringPlacementApplication {

    public TutoringPlacementApplication(){
        Frame f = new Frame("Team Scheduling Tool");
    }

    private String readSupportingFiles(){
        File objFile = new File("./Files/objFile");
        File classFile = new File("./Files/classFile");


        String obj = readFile(objFile);
        String clas = readFile(classFile);

        StringBuilder builder = new StringBuilder();
        builder.append(obj);
        builder.append(clas);


        return builder.toString();
    }

    private String readFile( File file ){

        StringBuilder builder = new StringBuilder();
        try{
            Scanner objScanner = new Scanner( file );
            while( objScanner.hasNextLine() ){
                builder.append(objScanner.nextLine());
                builder.append("\n");
            }
        }catch(FileNotFoundException e){
            System.err.println("File not found: " + file.getPath() );
        }
        return builder.toString();

    }

    public static void main( String[] args ){
        new TutoringPlacementApplication();
    }
}
