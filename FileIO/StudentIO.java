import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentIO {

    //++++++++++++++++++++ Instance Variables +++++++++++++++++++++++++++

    private final int version = 3;
    private final String description = "A bunch of students";
    private String[] _fileNames;
    private HashMap<String, ArrayList<Student>> allLectures;
    private int numberOfStudents = 0;
    private SanityChecker sanityChecker;
    private String logFileName = "problem_students.txt";
    boolean googleForm = true; // use blackboard if false
    private ArrayList<String> groupTimes;
    private ArrayList<String> professors;

    //+++++++++++++++++++++++++ Constructors ++++++++++++++++++++++++++
    public StudentIO(String[] fileNames){

        professors = new ArrayList<>();

        _fileNames = fileNames;
        allLectures = new HashMap<>();
        sanityChecker = new SanityChecker(logFileName);

        if( fileNames[0].startsWith("-") ){
            if( fileNames[0].substring(1).equals("bb")){
                googleForm = false;
            }
        }

        parseAll(_fileNames);
        produceStudentFile("students");
        sanityChecker.close();
    }

    //++++++++++++++++++++++++++++++ METHODS ++++++++++++++++++++++++++++
    //=============================== parseAll =================================
    private void parseAll(String[] files){
        for( String fileName : files ) {
            if( !googleForm )
                allLectures.put(fileName, parseFromBBCSV(new File(fileName)));
            else
                allLectures.put( fileName, parseGoogleForm( new File(fileName )));
        }

        if( googleForm )
            makeClassTemplateFile();
    }
    //==============================parseGoogleForm=================================
    private ArrayList<Student> parseGoogleForm( File file ){
        ArrayList<Student> students = new ArrayList<>();

        BufferedReader fileScanner;
        try{
            groupTimes = new ArrayList<>();
            fileScanner = new BufferedReader( new InputStreamReader( new FileInputStream(file), "utf-8"));

            String line = fileScanner.readLine();

            String[] fields = line.split(",");
            for( int i = 7; i < fields.length - 2; i++ ){
                groupTimes.add( fields[i] );
            }
            Student s;
            System.out.println( "Before new lines read ");
            while( (line = fileScanner.readLine()) != null ){

                s = parseGoogleLine(line);

                students.add( s );
            }
            numberOfStudents = students.size();
            return students;
        } catch( FileNotFoundException e){
            System.err.println( "File not found " + e.getMessage());
            System.exit(1);

        } catch( UnsupportedEncodingException io ){
            System.err.println( "File encoding not supported " + io.getMessage() );
            System.exit(1);
        } catch (IOException e){
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return students;
    }

    private Student parseGoogleLine( String line ){

        String[] fields = line.split(",");
        String firstName = fields[1];
        String lastName = fields[2];
        String email = fields[3];
        String gender = fields[4];
        int year = resolveYear(fields[5]);
        String professor = fields[6];

        if( !professors.contains(professor) )
            professors.add( professor );

        ArrayList<String> goodTimes = new ArrayList<>();
        ArrayList<String> possibleTimes = new ArrayList<>();


        for( int i = 0; i < groupTimes.size(); i++ ){
            if( fields[ i + 7 ].equals("Good") ){
                goodTimes.add( groupTimes.get(i) );
            }
            if( fields[i + 7].equals("Possible")){
                possibleTimes.add(groupTimes.get(i));
            }
        }
        String comments = "";
        for( int i = 7 + groupTimes.size(); i < fields.length; i++ ) {
            comments += fields[i];
        }
        Student student = new Student( firstName + " " + lastName, email, professor,gender, year, goodTimes, possibleTimes );
        student.setComment(comments);

        if( sanityChecker.addToRoster(student) ){
            return student;
        }
        else{
            System.out.println( student.getName() + " not added to roster. See " + logFileName );
            return null;
        }
    }

    //=============================== parseFromCSV =================================
    private ArrayList<Student> parseFromBBCSV( File file ){

        BufferedReader fileScanner;
        try {
            fileScanner = new BufferedReader( new InputStreamReader(new FileInputStream(file), "Unicode"));
            ArrayList<Student> students = new ArrayList<>();

            // skip first line, it's junk
            String line = fileScanner.readLine();
            while( (line = fileScanner.readLine()) != null){
                Student s = parseBBLine(line, file.getName(),"\",\"");
                if( s != null )
                    students.add(s);
            }

            return students;

        } catch (FileNotFoundException e){
            System.err.println( "File not found " + e.getMessage());
            System.exit(1);
        } catch( UnsupportedEncodingException io ){
            System.err.println( "File encoding not supported " + io.getMessage() );
            System.exit(1);
        } catch (IOException e ){
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return null;
    }

    //=============================== parseLine =================================
    private Student parseBBLine( String line, String fileName, String delimiter ){

        String[] fields = line.split( delimiter );
        String name = fields[5] + " " + fields[2];
        String email = parseEmail(fields[8]);
        int year = resolveYear(fields[11].trim());

        ArrayList<String> goodTimes = new ArrayList<>();
        ArrayList<String> possibleTimes = new ArrayList<>();

        for( int i = 17; i < 93; i+=3 ){
            if( fields[i].equals("first choice") ){
                String choice =  fields[ i - 1 ];
                goodTimes.add( parseDayTime(choice) );
            }
            if( fields[i].equals("second choice")) {
                String choice =  fields[ i - 1 ];
                possibleTimes.add( parseDayTime(choice) );
            }
        }
        numberOfStudents++;

        Student thisStudent = new Student(name, email, fileName.replaceAll(".csv", ""),"n/a", year, goodTimes, possibleTimes);
        sanityChecker.checkStudent( thisStudent );
        if( sanityChecker.addToRoster(thisStudent) )
            return thisStudent;
        else{
            System.out.println( thisStudent.getName() + " not added to roster. See " + logFileName );
            return null;
        }


    }



    public int resolveYear( String year ){
        switch (year){
            case "First-year":
                return 1;
            case "Freshman":
                return 1;
            case "Sophomore":
                return 2;
            case "Junior":
                return 3;
            case "Senior":
                return 4;
            case "Grad or Special Status":
                return 5;
            case "Graduate/Other":
                return 5;
            default:
                return 0;
        }
    }

    //=========================== parseEmail() =================================
    private String parseEmail( String email ){
        Pattern wildcatEmailRegex = Pattern.compile("([a-zA-Z]{1,3}([0-9]+)?)");
        Matcher emailMatcher = wildcatEmailRegex.matcher(email);

        if( emailMatcher.find() && !email.contains("@") ) {
            email = emailMatcher.group();
            email += "@wildcats.unh.edu";
        }

        if( email.contains("<a href=\"\"mailto:")){
            int startIndex = email.indexOf( ">" ) + 1;
            email = email.substring(startIndex);
            int endIndex = email.indexOf("<");
            email = email.substring(0, endIndex);
        }

        if( email.contains("<br />")){
            email = email.replaceAll("<br />", "");
        }

        return email.trim();
    }

    //=========================== parseDayTime =======================================
    private String parseDayTime( String input ){

        Pattern timeFormat = Pattern.compile("((1[012]|[1-9]):[0-5][0-9])|([1-9])");
        Matcher matcher = timeFormat.matcher(input);
        String time1= "";
        String time2 ="";
        if( matcher.find() )
            time1 = matcher.group();
        if( matcher.find() )
            time2 = matcher.group();

        if( time1.length() == 1 )
            time1 = time1 + ":00";
        if( time2.length() == 1 )
            time2 = time2 + ":00";

        Pattern dayPattern = Pattern.compile("Mon|Tue|Wed|Thu|Fri");
        Matcher dayMatcher = dayPattern.matcher(input);
        String day = "";
        if( dayMatcher.find() )
            day = dayMatcher.group();
        return day + ": " + time1 + " - " + time2;
    }

    //=============================== produceStudentFile =================================
    private void produceStudentFile( String name ){

        try {
            File newFile = new File(name);
            if ( newFile.createNewFile()) {
                BufferedWriter fileOut = new BufferedWriter( new FileWriter( newFile ) );
                fileOut.append("Student Info Format: " + version + '\n');
                fileOut.append("Description: " + description + '\n');
                fileOut.append("Number of students: " + numberOfStudents + "\n");
                for( String s: _fileNames ){
                    printToFile( allLectures.get(s), fileOut );
                }
                fileOut.close();
                System.out.println("Student file written to " + name);
            }
            else
                overwrite(newFile);

        } catch (IOException io ){
            System.out.println( "StudentIO Exception " + io.getMessage() );
        }
    }

    //=============================== printToFile =================================
    private void printToFile( ArrayList<Student> students, Writer out ){
        try {
            for (Student student : students) {
                out.append(student.print());
                //out.append('\n');
            }
        } catch (IOException io ){
            System.err.println( "IOException caught: " + io.getMessage() );
        }
    }


    //=============================== overwrite =================================
    private void overwrite( File file ){
        System.out.println("File " + file.getName() + " already exists: Overwrite (y/n) or Enter a new name.");
        Scanner stdIn = new Scanner( System.in );
        boolean properResponse = false;
        while( !properResponse ) {
            if (stdIn.hasNextLine()) {
                String response = stdIn.nextLine();
                if( response.length() == 1 ) {
                    switch (response.charAt(0)) {
                        case 'y':
                            deleteFile(file);
                            produceStudentFile(file.getName());
                            properResponse = true;
                            break;
                        case 'n':
                            System.out.println("File not overwritten. No changes made.");
                            properResponse = true;
                            break;
                        default:
                            properResponse = false;
                            System.out.println("Please type 'y' or 'n'.");
                            break;
                    }
                }
                else{
                    produceStudentFile(response);
                    properResponse = true;
                }
            }
        }
    }

    public void makeClassTemplateFile(){
        File classFile = new File( "classFile" );
        try {
            FileWriter fileWriter = new FileWriter(classFile);
            fileWriter.write("# Auto generated template \n");
            fileWriter.write("Class Info Format: 2\n");
            fileWriter.write("Description: (add description here)\n");
            fileWriter.write("Number of professors: " + professors.size() + "\n");
            for( int i = 0; i < professors.size(); i++ ){
                fileWriter.write("Name: " + professors.get(i) + "\n" );
            }
            fileWriter.write( "Number of groups: " + groupTimes.size() + "\n" );
            for( int i = 0; i < groupTimes.size(); i++ ){
                fileWriter.write("Name: \n");
                fileWriter.write("Email: \n");
                fileWriter.write("Time: " + groupTimes.get(i) + "\n" );
            }
            fileWriter.close();
        } catch( IOException e ){
            System.err.println( "IO Exception when making class template: " + e.getMessage() );
        }

    }

    //=============================== deleteFile =================================
    private void deleteFile( File file ){
        if( file.delete() )
            System.out.println( file.getName() + " deleted successfully.");
        else
            System.out.println( file.getName() + " couldn't be deleted");
    }

    //===============================main=================================
    public static void main( String[] args ){
        new StudentIO(args);
    }
}
