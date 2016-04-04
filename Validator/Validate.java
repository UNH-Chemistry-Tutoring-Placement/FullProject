package Validator;
import javafx.util.Pair;

import javax.swing.*;
import java.io.*;
import java.nio.Buffer;
import java.nio.file.Paths;
import java.util.*;

public class Validate {

    private ObjectiveFile objectiveFile;
    private StudentFile studentFile;
    private SolutionFile solutionFile;
    private String fullFile;

    private final String solutionHeader = "Solution Format";
    private final String objHeader = "Objective Function Format";
    private final String studentHeader = "Student Info Format";
    private final String classHeader = "Class Info Format";
    private boolean debug = false;

    /**
     * Init from system.in - cat'ed files
     */
    public Validate( ){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String line;
            while ((line = reader.readLine()) != null) {
                if( !line.startsWith("#"))
                    fullFile += line + "\n";
                line.replaceAll(",","");
            }
            String[] split1 = fullFile.split(classHeader);
            String[] split2 = split1[1].split(studentHeader);
            String[] split3 = split2[1].split(solutionHeader);

            String objectiveFormat = split1[0].replace("null", ""); // good
            String classInfo = classHeader + split2[ 0 ]; // good
            String studentInfo = studentHeader + split3[ 0 ]; // good
            String solution = solutionHeader + split3[ 1 ]; // good

            File objFile = File.createTempFile( "obj", "1", new File("."));
            // File classFile = File.createTempFile("class", "1", new File("."));
            File studFile = File.createTempFile("stud", "1", new File("."));
            File solFile = File.createTempFile("sol", "1", new File("."));

            FileWriter writer = new FileWriter(objFile);
            writer.write(objectiveFormat);
            writer.close();

            // CLASS FILE NOT USED YET
            // writer = new FileWriter(classFile);
            // writer.write(classInfo);
            // writer.close();

            writer = new FileWriter(studFile);
            writer.write(studentInfo);
            writer.close();

            writer = new FileWriter(solFile);
            writer.write(solution);
            writer.close();

            init(objFile.getName(), studFile.getName(), solFile.getName() );

            objFile.delete();
            studFile.delete();
            solFile.delete();

        } catch (IOException e ){
            System.err.println( "IO Exception: " + e.getMessage());
        }
    }

    public Validate( String fullFile ){

        String[] split1 = fullFile.split(classHeader);
        String[] split2 = split1[1].split(studentHeader);
        String[] split3 = split2[1].split(solutionHeader);

        String objectiveFormat = split1[0].replace("null", ""); // good
        String classInfo = classHeader + split2[ 0 ]; // good
        String studentInfo = studentHeader + split3[ 0 ]; // good
        String solution = solutionHeader + split3[ 1 ]; // good

        init_string(objectiveFormat, studentInfo, solution);

    }

    /**
     * Init from 3 different files
     * @param obj - objective file name
     * @param students - student file name
     * @param sol - solution file name
     */
    public Validate( String obj, String students, String sol ){
        init( obj, students, sol);
    }

    private void init( String obj, String students, String sol ){
        File objective = new File( obj );
        File student = new File( students );
        File solution = new File( sol );

        objectiveFile = new ObjectiveFile(objective);
        studentFile = new StudentFile(student);
        solutionFile = new SolutionFile(solution);

        objective.delete();
        student.delete();
        solution.delete();

        validate();
    }

    private void init_string(String obj, String students, String sol){
        objectiveFile = new ObjectiveFile(obj);
        studentFile = new StudentFile(students);
        solutionFile = new SolutionFile(sol);

        validate();
    }

    public void validate(){

        int penalty = 0;
        int aboveMaxPenalty = objectiveFile.getAboveMaxPenalty();
        int belowMinPenalty = objectiveFile.getBelowMinPenalty();
        int minGroupSize = objectiveFile.getMinGroupSize();
        int maxGroupSize = objectiveFile.getMaxGroupSize();
        int possibleChoicePenalty = objectiveFile.getPossibleChoicePenalty();
        int differentProfessorPenalty = objectiveFile.getDiffProfessorPenalty();
        int genderSoloPenalty = objectiveFile.getGenderSoloPenalty();

        HashMap<Pair<String,String>, ArrayList<String>> groups = solutionFile.getGroups();

        Iterator<Pair<String,String>> mapIter = groups.keySet().iterator();
        Pair<String,String> time;
        ArrayList<String> studentsInGroup;

        while( mapIter.hasNext() ){

            boolean difProfApplied = false;
            String professor = "";

            int femalesInGroup = 0;
            int malesInGroup = 0;
            time = mapIter.next();
            studentsInGroup = groups.get(time);

            if( debug )
                System.out.println( "\nGroup: " + time.getKey() + "---" + time.getValue() );

            if( studentsInGroup.size() > maxGroupSize ){
                penalty += (studentsInGroup.size() - maxGroupSize ) * aboveMaxPenalty;
                if( debug )
                    System.out.println( "Applied max group penalty");
            }
            if( studentsInGroup.size() < minGroupSize ) {
                penalty += (minGroupSize - studentsInGroup.size() ) * belowMinPenalty;
                if( debug )
                    System.out.println( "Applied min group penalty");
            }
            for( String studentName: studentsInGroup ){
                String studentSex = studentFile.getStudentSex(studentName);
                if( professor.equals(""))
                    professor = studentFile.getStudentProfessor(studentName);
                if( !difProfApplied && !professor.equals(studentFile.getStudentProfessor(studentName))) {
                    penalty += differentProfessorPenalty;
                    difProfApplied = true;
                    if( debug )
                        System.out.println( "Applied different professor penalty");
                }

                if( studentSex.equals( "Male" ))
                    malesInGroup++;
                if( studentSex.equals( "Female" ))
                    femalesInGroup++;
                if( studentFile.getPossibleTimes(studentName).contains(time.getKey())){
                    penalty += possibleChoicePenalty;
                    if(debug)
                        System.out.println( "Applied possible time penalty");
                }
            }

            if( (malesInGroup == 1 && femalesInGroup > 1)
                    || (malesInGroup > 1 && femalesInGroup == 1) ) {
                if( debug )
                    System.out.println( "Applied gender solo penalty");
                penalty += genderSoloPenalty;
            }
        }
        System.out.println("\nValidator Assigned Penalty: " + penalty );
        System.out.println("Solver assigned Penalty: " + solutionFile.getSolutionCost() + '\n');
    }

    public ArrayList<Pair<String, String>> rearrange(ArrayList<Pair<String,String>> timeToOutput) {
        //TODO: rearrange by the Key of the arrayList
        return timeToOutput;
    }

    public void printRosters(){

        HashMap<Pair<String,String>, ArrayList<String>> groups = solutionFile.getGroups();
        int count = 0;
        ArrayList<Pair<String,String>> times = solutionFile.getTimes();
        ArrayList<Pair<String,String>> output = new ArrayList<>();
        for(int i = 0; i < times.size(); i++) {
            Pair<String, String> taEmailTime = times.get(i);
            String s = "";
            s += "TA email: " + taEmailTime.getValue() + " | Group Time: " + taEmailTime.getKey() + "\n";
            s += "--------------------------------------\n";
            for (String studentEmail : groups.get(taEmailTime)) {
                count++;
                s += String.format("%-28s %-28s %-28s\n", studentFile.getStudentNameByEmail(studentEmail.toLowerCase().trim()), studentEmail, printPreference(studentFile, studentEmail, taEmailTime.getKey()));
                if (debug)
                    s += "Professor: " + studentFile.getStudentProfessor(studentEmail) + "\n";
            }
            s += "count:" + count + "\n";
            output.add(new Pair<>(taEmailTime.getKey(), s));
        }

        output = rearrange(output);

        for(int i = 0; i < output.size(); i++) {
            System.out.println(output.get(i).getValue());
        }
    }

    public void saveToCSV(){
        JFileChooser saveDialog = new JFileChooser();
        saveDialog.showSaveDialog(null);
        File saveFile = saveDialog.getSelectedFile();
        FileWriter fileWriter;
        saveToCSV(saveFile);

    }

    public void saveToCSV( File saveFile ){
        FileWriter fileWriter;
        try{
            fileWriter = new FileWriter(saveFile);
            HashMap<Pair<String,String>, ArrayList<String> > groups = solutionFile.getGroups();
            ArrayList<Pair<String,String>> times = solutionFile.getTimes();
            ArrayList<Pair<String,String>> output = new ArrayList<>();

            for( int i = 0; i < times.size(); i++ ){
                Pair<String,String> taEmailTime = times.get(i);
                fileWriter.write(taEmailTime.getKey() + ", ");

                for( String studentEmail : groups.get(taEmailTime) ){
                    fileWriter.write( studentEmail + ", ");
                }
                fileWriter.write('\n');

            }
            fileWriter.close();
        } catch (IOException e ){
            System.err.println("Could not open filewriter for output file.");
        }
    }


    private String printPreference( StudentFile file, String email, String time ){
        if( file.getPossibleTimes(email).contains(time))
            return "(possible)";
        else if( file.getGoodTimes(email).contains(time))
            return "(good)";
        else
            return "(impossible)";
    }

    public static void main( String[] args ){
        Validate validate = new Validate();
        validate.printRosters();
        validate.saveToCSV();

    }
}
