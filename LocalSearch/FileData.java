package LocalSearch;

import javafx.beans.binding.StringBinding;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class FileData {

    public static final int NUM_FILES = 4;

    FileParsers parsers;

    Data data;

    public FileData() {
        parsers = new FileParsers();
        data = new Data();
    }

    public void load() {
        String in = "";

        Scanner input = new Scanner(System.in);
        while(input.hasNextLine()) {
            in += input.nextLine() + "\n";
        }

        Parser p = new Parser(in);

        String line;
        for(int i = 0; i < NUM_FILES; i ++) {
            line = p.getLine();

            if(line == null) {
                break;
            } else if(line.contains("Student Info Format")) {
                data.studentData = parsers.studentParser(p);
            } else if(line.contains("Objective Function Format")) {
                data.objectiveData = parsers.objectiveParser(p);
            } else if(line.contains("Class Info Format")) {
                data.classData = parsers.classParser(p);
            } else if(line.contains("Solution Format")) {
                data.assignmentData = parsers.assignmentParser(p);
            }
        }
    }

    public void load(String in){

        Parser p = new Parser(in);

        String line;
        for(int i = 0; i < NUM_FILES; i ++) {
            line = p.getLine();

            if(line == null) {
                break;
            } else if(line.contains("Student Info Format")) {
                data.studentData = parsers.studentParser(p);
            } else if(line.contains("Objective Function Format")) {
                data.objectiveData = parsers.objectiveParser(p);
            } else if(line.contains("Class Info Format")) {
                data.classData = parsers.classParser(p);
            } else if(line.contains("Solution Format")) {
                data.assignmentData = parsers.assignmentParser(p);
            }
        }
    }

    public Data get() {
        return data;
    }

    public void out(int grade, HashMap<FileParsers.Group, ArrayList<FileParsers.Student>> times, File output) {

        String out = "";

        out += "Solution Format: 2\n";
        out += "Objective Function: STUFF\n";
        out += "Class Info: STUFF\n";
        out += "Student Info: STUFF\n";
        out += "Number of students: " + data.studentData.numStudents + "\n";

        for (FileParsers.Group time : times.keySet()){
            for (int i = 0; i < times.get(time).size(); i++){
                String studentEmail = times.get(time).get(i).email;
                out += studentEmail + "\n";
                out += time.time.replace(",","") + "\n";
                out += time.email + "\n";
            }
        }
        out += "Solution cost: " + grade;

        try(PrintWriter writer = new PrintWriter(output, "UTF-8")) {
            writer.print(out);
            writer.close();
        } catch(IOException e) {
            System.err.println("Could not open writer");
        }
    }

    public String out_string(int grade, HashMap<FileParsers.Group, ArrayList<FileParsers.Student>> times){
        StringBuilder builder = new StringBuilder();

        builder.append("Solution Format: 2\n");
        builder.append("Objective Function: STUFF\n");
        builder.append("Class Info: STUFF\n");
        builder.append("Class Info: STUFF\n");
        builder.append("Student Info: STUFF\n");
        builder.append("Number of students: ");
        builder.append(data.studentData.numStudents);
        builder.append("\n");

        for (FileParsers.Group time : times.keySet()){
            for (int i = 0; i < times.get(time).size(); i++){
                String studentEmail = times.get(time).get(i).email;
                builder.append(studentEmail );
                builder.append("\n");
                builder.append(time.time.replace(",",""));
                builder.append("\n");
                builder.append(time.email);
                builder.append("\n");
            }
        }
        builder.append("Solution cost: ");
        builder.append(grade);

        return builder.toString();
    }

    public static class Data {
        public FileParsers.StudentData studentData;
        public FileParsers.ObjectiveData objectiveData;
        public FileParsers.ClassData classData;
        public HashMap<String, String> assignmentData;

        public Data() {
            studentData = new FileParsers.StudentData();
            objectiveData = new FileParsers.ObjectiveData();
            classData = new FileParsers.ClassData();
            assignmentData = new HashMap<>();
        }
    }
}
