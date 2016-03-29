package LocalSearch;

import java.util.ArrayList;
import java.util.HashMap;

public class FileParsers {

    public FileParsers() {

    }

    public ClassData classParser(Parser p) {
        String name, email, time;

        ClassData data = new ClassData();

        data.description = p.getSecondPart();
        data.numberOfProfessors = p.getSecondPartAsNumber();

        for(int i = 0; i < data.numberOfProfessors; i++) {
            data.professors.add(p.getSecondPart());
        }

        data.numberOfGroups = p.getSecondPartAsNumber();
        for(int i = 0; i < data.numberOfGroups; i++) {
            name = p.getSecondPart();
            email = p.getSecondPart();
            time = p.getSecondPart();

            Group g = new Group(name, email, time);

            data.groups.put(g.time + "," + g.name, g);
            data.groupsByTime.put(g.time, g);
        }

        return data;
    }

    public HashMap<String, String> assignmentParser(Parser p) {
        String email, time;

        int numberOfStudents = p.getSecondPartAsNumber();

        HashMap<String, String> data = new HashMap<>();

        for(int i = 0; i < numberOfStudents; i++) {
            email = p.getLine();
            time = p.getLine();
            p.getLine(); //ignore TA for now

            data.put(time, email);
        }
        p.getLine(); //solution cost
        return data;
    }

    public StudentData studentParser(Parser p) {
        StudentData data = new StudentData();

        data.description = p.getSecondPart();
        data.numStudents = p.getSecondPartAsNumber();

        for(int i = 0; i < data.numStudents; i++) {
            Student student = new Student();

            student.name = p.getSecondPart();
            student.email = p.getSecondPart();
            student.professor = p.getSecondPart();
            student.year = p.getSecondPartAsNumber();
            student.sex = p.getSecondPart();
            student.numGoodTimes = p.getSecondPartAsNumber();
            for(int j = 0; j < student.numGoodTimes; j++) {
                student.goodTimes.add(p.getLine());
            }
            student.numPossibleTimes = p.getSecondPartAsNumber();
            for(int j = 0; j < student.numPossibleTimes; j++) {
                student.possibleTimes.add(p.getLine());
            }
            data.students.put(student.email, student);
        }


        return data;
    }

    public ObjectiveData objectiveParser(Parser p) {
        ObjectiveData data = new ObjectiveData();

        data.description = p.getSecondPart();
        data.minGroupSize = p.getSecondPartAsNumber();
        data.maxGroupSize = p.getSecondPartAsNumber();
        data.belowMinPenalty = p.getSecondPartAsNumber();
        data.aboveMaxPenalty = p.getSecondPartAsNumber();
        data.possibleChoicePenalty = p.getSecondPartAsNumber();
        data.diffProfessorPenalty = p.getSecondPartAsNumber();
        data.genderSoloPenalty = p.getSecondPartAsNumber();

        return data;
    }

    //data for the class
    public static class ClassData {
        public String description;
        public ArrayList<String> professors;
        public int numberOfProfessors;
        public int numberOfGroups;
        public HashMap<String, Group> groups;
        public HashMap<String, Group> groupsByTime;

        public ClassData() {
            description = "";
            professors = new ArrayList<>();
            numberOfProfessors = 0;
            numberOfGroups = 0;
            groups = new HashMap<>();
            groupsByTime = new HashMap<>();
        }
    }

    //data for a single group
    public class Group {
        public String name;
        public String email;
        public String time;

        public Group(String name, String email, String time) {
            this.name = name;
            this.email = email;
            this.time = time;
        }
    }

    //data for the students
    public static class StudentData {
        public String description;
        public int numStudents;
        public HashMap<String, Student> students;

        public StudentData() {
            description = "";
            numStudents = 0;
            students = new HashMap<>();
        }
    }

    //data for a single student
    public class Student {
        public String name;
        public String email;
        public String professor;
        public int year;
        public String sex;
        public int numGoodTimes;
        public ArrayList<String> goodTimes;
        public int numPossibleTimes;
        public ArrayList<String> possibleTimes;

        public Student() {
            name = "";
            email = "";
            professor = "";
            year = 0;
            sex = "";
            numGoodTimes = 0;
            goodTimes = new ArrayList<>();
            numPossibleTimes = 0;
            possibleTimes = new ArrayList<>();
        }
    }

    //data for the objective function
    public static class ObjectiveData {
        public String description;
        public int minGroupSize;
        public int maxGroupSize;
        public int belowMinPenalty;
        public int aboveMaxPenalty;
        public int possibleChoicePenalty;
        public int diffProfessorPenalty;
        public int genderSoloPenalty;

        public ObjectiveData() {
            description = "";
            minGroupSize = 0;
            maxGroupSize = 0;
            belowMinPenalty = 0;
            aboveMaxPenalty = 0;
            possibleChoicePenalty = 0;
            diffProfessorPenalty = 0;
            genderSoloPenalty = 0;
        }
    }

}
