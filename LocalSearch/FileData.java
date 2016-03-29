package LocalSearch;

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

    public Data get() {
        return data;
    }

    public void out(int grade, HashMap<String, ArrayList<String>> times, File output) {

        String out = "";

        out += "Solution Format: 2\n";
        out += "Objective Function: STUFF\n";
        out += "Class Info: STUFF\n";
        out += "Student Info: STUFF\n";
        out += "Number of students: " + data.studentData.numStudents + "\n";

        for (String time : times.keySet()){
            for (int i = 0; i < times.get(time).size(); i++){
                String studentEmail = times.get(time).get(i);
                out += data.studentData.students.get(studentEmail).email + "\n";
                out += time.replace(",","") + "\n";
                out += data.classData.groups.get(time).email + "\n";
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
