

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassFile {

    private HashMap<String,String> contents, theGroups, emails;
    private ArrayList<String> professors;

    private final String format = "Class Info Format";
    private final String description = "Description";
    private final String professorCount = "Number of professors";
    private final String groups = "Number of groups";

    public ClassFile(File classFile){
        contents = new HashMap<>();
        professors = new ArrayList<>();
        theGroups = new HashMap<>();
        emails = new HashMap<>();
        parseClass(classFile);
    }

    public String printAll(){
        StringBuilder builder = new StringBuilder();
        builder.append(getFormat());
        builder.append('\n');
        builder.append(getDescription());
        builder.append('\n');
        builder.append(getProfessors());
        builder.append(getNumberOfGroups());
        builder.append('\n');
        builder.append(getAllGroups());
        return builder.toString();
    }

    public String getFormat(){
        return contents.get(format);
    }

    public String getDescription(){
        return contents.get(description);
    }

    public String getProfessors(){
        StringBuilder builder = new StringBuilder();
        for( String s: professors ){
            builder.append(s);
            builder.append('\n');
        }
        return builder.toString();
    }

    public int getNumberOfGroups(){
        return Integer.parseInt(contents.get(groups));
    }

    public String findGroupByName( String name ){
        if( theGroups.containsKey(name) )
            return theGroups.get(name);
        else{
            System.err.println("The group: " + name + " could not be found.");
            return "";
        }
    }

    public String findGroupByEmail( String email ){
        if( emails.containsKey(email)){
            return emails.get(email);
        }
        else{
            System.err.println("The email: " + email + " could not be found.");
            return "";
        }
    }

    public String getAllGroups(){
        return theGroups.toString();
    }

    private void parseClass( File classFile ){
        try{
            Scanner classScanner = new Scanner(classFile);
            while( classScanner.hasNextLine() ){
                String nextLine = classScanner.nextLine();

                if( nextLine.startsWith("#") )
                    continue;

                Scanner lineScan = new Scanner(nextLine).useDelimiter(":");
                String nextToken = lineScan.next();
                switch( nextToken ){
                    case professorCount:
                        parseProfessors(lineScan,classScanner,nextToken);
                        break;
                    case groups:
                        parseGroups(lineScan,classScanner,nextToken);
                        break;
                    default:
                        contents.put(nextToken.trim(), lineScan.next().trim());
                        break;
                }
            }
        } catch (FileNotFoundException e){
            System.err.println("File " + classFile.getName() + " not found");
        }
    }

    private void parseProfessors( Scanner lineScanner, Scanner classScanner, String nextToken ){
        String numberOfProfessors = lineScanner.next().trim();
        contents.put(nextToken.trim(),numberOfProfessors);
        int numProfessors = Integer.parseInt(numberOfProfessors);
        for( int i = 0; i < numProfessors; i++ ) {
            String lectureLine = classScanner.nextLine();
            professors.add(lectureLine.trim());
        }
    }

    private void parseGroups( Scanner lineScanner, Scanner classScanner, String nextToken ){
        String numberOfGroups = lineScanner.next().trim();
        contents.put( nextToken.trim(), numberOfGroups.trim());
        int number = Integer.parseInt(numberOfGroups);
        for( int i = 0; i < number; i++ ){
            String nameLine = classScanner.nextLine();
            Scanner nameLineScan = new Scanner(nameLine).useDelimiter(":");
            nameLineScan.skip("Name");

            String emailLine = classScanner.nextLine();
            Scanner emailLineScan = new Scanner(emailLine).useDelimiter(":");
            emailLineScan.skip("Email");

            String timeLine = classScanner.nextLine();
            String time = extractTimeDay(timeLine).trim();
            emails.put(emailLineScan.next().trim(), time );
            theGroups.put(nameLineScan.next().trim(), time );
        }
    }

    private String extractTimeDay( String line ){

        Pattern dayFormat = Pattern.compile("Mon|Tue|Wed|Thu|Fri");
        Matcher dayMatcher = dayFormat.matcher(line);

        String day = "";
        if( dayMatcher.find() )
            day = dayMatcher.group();

        Pattern timeFormat = Pattern.compile("(((1[0-9]|[1-9])|([2][0-4])):[0-5][0-9])|([1-9])");
        Matcher matcher = timeFormat.matcher(line);

        String time1 = "";
        String time2 = "";

        if( matcher.find() ){
            time1 = matcher.group();
        }
        if( matcher.find() ){
            time2 = matcher.group();
        }
        return day + " " + time1 + " - " + time2;
    }


    public static void main(String args[]){
        ClassFile thisClass = new ClassFile( new File( args[0] ));
        System.out.println( thisClass.printAll() );
        System.out.println( thisClass.findGroupByName("George Patterson"));
        System.out.println( thisClass.findGroupByEmail("jebol@wildcats.unh.edu"));
    }
}
