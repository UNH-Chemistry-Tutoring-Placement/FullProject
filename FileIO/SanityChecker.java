import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class SanityChecker {

    private File logFile;
    private FileWriter fileWriter;
    private HashMap<Student, ArrayList<String>> errors;

    private String emailError = "Incorrect email format ->";
    private String yearError = "Incorrect year format ->";
    private String goodTimeWarning = "No good times were selected";
    private String possibleTimeWarning = "No possible times selected";
    private String noTimesError = "No good or possible times selected";


    public SanityChecker( String fileName ){
        logFile = new File(fileName);
        errors = new HashMap<>();
        openFileWriter();
    }

    public boolean checkStudent( Student s ){
        boolean goodEmail = checkEmail(s);
        boolean goodYear = checkYear(s);

        return goodEmail && goodYear;
    }

    public boolean addToRoster( Student s ){
        boolean goodPossibles = checkPossibleTimes(s);
        boolean goodGoods = checkGoodTimes(s);

        if( !goodGoods && !goodPossibles )
            printToLogFile(s, 5);

        return goodGoods || goodPossibles;
    }

    private boolean openFileWriter(){
        try {
            fileWriter = new FileWriter(logFile);
            return true;
        } catch( IOException e ){
            System.err.println( "SanityChecker: There was an error opening the file writer.\n"
                    + e.getMessage());
            return false;
        }
    }

    private boolean checkEmail( Student student ){
        if(student.getEmail().contains("@"))
            return true;

        printToLogFile(student, 1);
        return false;
    }

    private boolean checkPossibleTimes( Student student ){
        if( student.getPossibleTimes().size() == 0){
            printToLogFile(student,4);
            return false;
        }
        return true;
    }
    private boolean checkGoodTimes( Student student ){
        if( student.getGoodTimes().size() == 0){
            printToLogFile(student,3);
            return false;
        }
        return true;
    }

    private boolean checkYear( Student student ){
        if( student.getYear() <= 5 && student.getYear() > 0)
            return true;
        printToLogFile(student, 2);
        return false;
    }


    /**
     * Error code explanation:
     * 1 = email error
     * 2 = year error
     * 3 = good time error
     * 4 = possible time error
     * 5 = no times error
     * @param errorCode
     */
    private void printToLogFile( Student s, int errorCode ) {


        if( !errors.containsKey(s)){
            errors.put( s , new ArrayList<String>() );
        }

        switch (errorCode) {
            case 1:
                errors.get(s).add( "Not added to roster for: " + emailError
                        + " " + s.getEmail() + '\n');
                break;
            case 2:
                errors.get(s).add( "Not added to roster for: " + yearError
                        + " " + s.getYear() + '\n');
                break;
            case 3:
                errors.get(s).add( "Warning: " + goodTimeWarning + '\n');
                break;
            case 4:
                errors.get(s).add( "Warning: " + possibleTimeWarning + "\n");
                break;
            case 5:
                errors.get(s).add("Not added to roster for: " + noTimesError + '\n');
                break;
            default:
                System.err.println( "SanityChecker: invalid error code " + errorCode );
                break;
        }

    }
    public void close(){
        try {
            Iterator<Student> iterator = errors.keySet().iterator();
            while( iterator.hasNext() ){
                Student key = iterator.next();
                ArrayList<String> contents = errors.get(key);
                fileWriter.write(key.getName() + " (" + key.getEmail() +")" + ":\n");
                for (String value: contents){
                    fileWriter.write(value );
                }
                fileWriter.write('\n');
                fileWriter.write( "Comments: " + key.getComments() );
            }
            fileWriter.close();
        } catch( IOException e){
            System.err.println( "SanityChecker: Could not close file writer. " + e.getMessage() );
        }
    }

}
