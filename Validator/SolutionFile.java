

import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class SolutionFile {

    private final String versionKey = "Solution Format";
    private final String functionKey = "Objective Function";
    private final String classInfoKey = "Class Info";
    private final String studentInfoKey = "Student Info";
    private final String studentNumKey = "Number of students";
    private final String solutionCostKey = "Solution cost";


    private double version;
    private String functionDescription;
    private String classInfo;
    private String studentInfo;
    private int numberOfStudents;
    private HashMap<String,String> studentTimes;
    private HashMap<Pair<String,String>, ArrayList<String>> groups;
    private ArrayList<String> students;
    private int solutionCost;

    public SolutionFile( File solutionFile ){
        studentTimes = new HashMap<>();
        students = new ArrayList<>();
        groups = new HashMap<>();
        parseAll( solutionFile );
    }

    private void parseAll( File file ){

        try{
            Scanner solutionScanner = new Scanner( file );
            while( solutionScanner.hasNextLine() ){
                String nextLine = solutionScanner.nextLine();

                if( nextLine.startsWith("#") )
                    continue;

                Scanner lineScanner = new Scanner(nextLine).useDelimiter(":");
                String key = lineScanner.next();
                switch (key){
                    case versionKey:
                        version = Double.parseDouble(lineScanner.next().trim());
                        break;
                    case functionKey:
                        functionDescription = lineScanner.next().trim();
                        break;
                    case classInfoKey:
                        classInfo = lineScanner.next().trim();
                        break;
                    case studentInfoKey:
                        studentInfo = lineScanner.next().trim();
                        break;
                    case studentNumKey:
                        numberOfStudents = Integer.parseInt(lineScanner.next().trim());
                        parseStudents(solutionScanner);
                        break;
                    case solutionCostKey:
                        solutionCost = Integer.parseInt(lineScanner.next().trim());
                        break;
                }
            }
        }catch( FileNotFoundException e){
            System.err.println( "File " + file.getName() + " not found." );
        }
    }

    private void parseStudents( Scanner lineScanner ){

        for( int i = 0; i < numberOfStudents; i++ ){

            String studentName = lineScanner.nextLine().trim();
            String studentPlacement = lineScanner.nextLine().trim();
            String taEmail = lineScanner.nextLine().trim();
            Pair<String, String> taEmailPair = new Pair<>(studentPlacement, taEmail);

            if( !groups.containsKey(taEmailPair) ){
                groups.put( taEmailPair, new ArrayList<>());
            }
            groups.get( taEmailPair ).add(studentName);
            studentTimes.put(studentName, studentPlacement);
            students.add( studentName );
        }
    }

    public ArrayList<String> getStudentsInGroup( String time, String taEmail ){
        Pair<String, String> emailPair = new Pair<>(time, taEmail);
        if( groups.containsKey(emailPair))
            return groups.get(emailPair);
        System.err.println( "Could not find pair: " + emailPair );
        return null;
    }

    public String getAllStudents(){
        String result = "";
        for( String name: students ){
            result += name + ": " + studentTimes.get( name ) + '\n';
        }
        return result;
    }
    public String getTimeForStudentByName( String name ){

        if( !studentTimes.containsKey(name)){
            System.err.println( "Student " + name + " not found.");
            return "";
        }

        return studentTimes.get(name);
    }

    public int getSolutionCost(){
        return solutionCost;
    }

    /**
    public String getTimeForStudentByEmail( String email ){

        if( !studentEmails.containsKey(email)){
            System.err.println( "Email " + email + " not found.");
            return "";
        }
        String name = studentEmails.get( email );

        if( !studentTimes.containsKey(name)){
            System.err.println( "Could not find " + name + " with email " + email);
            return "";
        }
        return studentTimes.get( name );
    }
     */

    public double getVersion(){
        return version;
    }

    public String getFunctionDescription(){
        return functionDescription;
    }

    public String getClassInfo(){
        return classInfo;
    }

    public String getStudentInfo(){
        return studentInfo;
    }

    public int getNumberOfStudents(){
        return numberOfStudents;
    }

    public ArrayList<String> getStudentList(){
        return students;
    }

    public HashMap<Pair<String,String>, ArrayList<String>> getGroups(){
        return groups;
    }

    public static void main( String[] args ){
        SolutionFile file = new SolutionFile( new File( args[0]) );
        System.out.println( file.getAllStudents() );
        //System.out.println( file.getTimeForStudentByEmail("jfoo@gmail.com"));
        System.out.println( file.getStudentsInGroup("Fri 14:00-15:00", "shmar@wildcats.unh.edu"));
    }


}
