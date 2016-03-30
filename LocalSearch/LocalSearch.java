package LocalSearch;

import java.io.File;
import java.lang.reflect.Array;
import java.util.*;

public class LocalSearch {
    private int finalGrade = 0;
    private HashMap<FileParsers.Group, ArrayList<FileParsers.Student>> bestSolution;
    private int secondsToRun;
    private FileData fileData;
    public LocalSearch( int timeToRun, String outFileName ){

        fileData = new FileData();
        secondsToRun = timeToRun * 1000;
        fileData.load();
        regularSwap();

        fileData.out(finalGrade, bestSolution, new File(outFileName));
    }

    private void regularSwap(){

        HashMap<FileParsers.Group, ArrayList<FileParsers.Student>>  results = new HashMap<>();

        long timeStart = new Date().getTime();

        while( new Date().getTime() - timeStart < secondsToRun ){
            System.out.println( "Running: " + (new Date().getTime() - timeStart) );
            HashMap<FileParsers.Group, ArrayList<FileParsers.Student>>   curAssignment = randomAssignment();

            HashMap<FileParsers.Group, ArrayList<FileParsers.Student>>  result = swap(curAssignment);

            int grade = gradeTotal(result);
            if( grade > finalGrade ){
                bestSolution = result;
                finalGrade = grade;
            }
        }
        System.out.println("finished");
    }

    public int getGrade(){
        return finalGrade;
    }

    public HashMap<FileParsers.Group, ArrayList<FileParsers.Student>> getSolution(){
        return bestSolution;
    }

    private HashMap<FileParsers.Group, ArrayList<FileParsers.Student>> randomAssignment( ){

        HashMap<FileParsers.Group, ArrayList<FileParsers.Student>>  assignment = new HashMap<>();
        ArrayList<FileParsers.Group> times = new ArrayList<>();
        Iterator<FileParsers.Group> groupIter = fileData.get().classData.groups.values().iterator();

        while( groupIter.hasNext() ){
            FileParsers.Group group = groupIter.next();

            times.add(group);
            assignment.put(group, new ArrayList<>());

        }


        Iterator<FileParsers.Student> studentIterator = fileData.get().studentData.students.values().iterator();
        while( studentIterator.hasNext() ){
            FileParsers.Student stud = studentIterator.next();
            ArrayList<String> possibleTimes = stud.possibleTimes;
            ArrayList<String> acutalPossibleTimes = new ArrayList<>();

            for( String s : possibleTimes ){
                if( fileData.get().classData.groupsByTime.containsKey(s) )
                    acutalPossibleTimes.add(s);
            }

            //System.out.println("Actualpossibles: " + acutalPossibleTimes );

            if( acutalPossibleTimes.size() == 0 ){
                ArrayList<String> goodTimes = stud.goodTimes;
                ArrayList<String> actualGoodTimes = new ArrayList<>();

                for( String s : goodTimes ){
                    if( fileData.get().classData.groupsByTime.containsKey(s) ){
                        actualGoodTimes.add(s);
                    }
                }

                if( actualGoodTimes.size() == 0 ){

                    int randInt = randNumber(0,times.size());
                    assignment.get(times.get(randInt)).add(stud);
                }else{

                    int randInt = randNumber(0,actualGoodTimes.size());
                    //int randTime = randNumber(0,fileData.get().classData.groupsByTime.get(actualGoodTimes.get(randInt)).size());
                    FileParsers.Group group = fileData.get().classData.groupsByTime.get(actualGoodTimes.get(randInt));
                    assignment.get(group).add(stud);
                }
            }
            else{

                int ran = randNumber(0,acutalPossibleTimes.size());

                //int timeRan = randNumber(0,fileData.data.classData.groupsByTime.get(acutalPossibleTimes.get(ran)).size());
               // System.out.println( "timeRan: " + timeRan );
               // System.out.println("here: " + fileData.get().classData.groupsByTime.get(acutalPossibleTimes.get(ran)) );
                FileParsers.Group group = fileData.get().classData.groupsByTime.get(acutalPossibleTimes.get(ran));

                assignment.get(group).add(stud);
            }
        }

        return assignment;
    }

    private void doubleSwap(HashMap<FileParsers.Group, ArrayList<FileParsers.Student>> curAssignment){
        int zeroCounter = 0;
        boolean swapped;
        do{
            System.out.println("looping 2");
            swapped = false;
            Set<FileParsers.Group> groups = curAssignment.keySet();
            //System.out.println("double swap");
            for( FileParsers.Group time : groups ){
                ArrayList<FileParsers.Student> students = curAssignment.get(time);
                for( int i = 0; i < students.size(); i++ ){
                    FileParsers.Student student = students.get(i);
                    for( FileParsers.Group time2 : groups ){

                        boolean studentAlreadySwapped = false;

                        if( time == time2 )
                            continue;

                        String justTime2= time2.time;

                        if( fileData.get().studentData.students.get(student.email).goodTimes.contains(justTime2) ||
                                fileData.get().studentData.students.get(student.email).possibleTimes.contains(justTime2)){
                            ArrayList<FileParsers.Student> students2 = curAssignment.get(time2);
                            for( int j = 0; j < students2.size(); j++){
                                FileParsers.Student student2 = students2.get(j);
                                String justTime1 = time.time;

                                if( fileData.get().studentData.students.get(student2.email).goodTimes.contains(justTime1) ||
                                        fileData.get().studentData.students.get(student2.email).possibleTimes.contains(justTime1) ){
                                    int gradeTime1Before = grade( time, curAssignment.get(time) );
                                    int gradeTime1After = gradeSwap(time, curAssignment.get(time), student2, student);
                                    int diff1 = gradeTime1Before - gradeTime1After;

                                    int gradeTime2Before = grade(time2, curAssignment.get(time2));
                                    int gradeTime2After = gradeSwap(time2, curAssignment.get(time2), student,student2);
                                    int diff2 = gradeTime2Before - gradeTime2After;

                                    if( diff1 + diff2 >= 0 ){
                                        int totalDiff = diff1 + diff2;

                                        if( totalDiff == 0 ){
                                            if( zeroCounter >= 1000 ){
                                                return;
                                            }else{
                                                zeroCounter++;
                                            }
                                        }
                                        else{
                                            zeroCounter = 0;
                                        }

                                        int studIndex = curAssignment.get(time).indexOf(student);
                                        curAssignment.get(time).remove(studIndex);

                                        int stud2Index = curAssignment.get(time2).indexOf(student2);
                                        curAssignment.get(time2).remove(stud2Index);

                                        curAssignment.get(time).add(studIndex,student2);
                                        curAssignment.get(time2).add(stud2Index,student);

                                        swapped = true;
                                        studentAlreadySwapped = true;

                                        break;

                                    }

                                }
                            }

                        }
                        if( studentAlreadySwapped )
                            break;
                    }
                }
            }

        } while(swapped);
    }

    private void singleSwap(HashMap<FileParsers.Group, ArrayList<FileParsers.Student>> curAssignment){
        int zeroCounter = 0;
        boolean swapped = false;
        do{
            System.out.println("looping 1");
            swapped = false;
            Set<FileParsers.Group> groups = curAssignment.keySet();
            for( FileParsers.Group time : groups ){
                ArrayList<FileParsers.Student> students = curAssignment.get(time);
                for( int i = 0; i < students.size(); i++ ){
                    FileParsers.Student student = students.get(i);

                    int beforePenalty1 = grade( time, curAssignment.get(time) );
                    int afterPenalty1 = gradeWithout( time, curAssignment.get(time), student);

                    int diff1 = beforePenalty1 - afterPenalty1;

                    ArrayList<String> _allTimes = new ArrayList<>();
                    _allTimes.addAll(fileData.get().studentData.students.get(student.email).possibleTimes);
                    _allTimes.addAll(fileData.get().studentData.students.get(student.email).goodTimes);
                    ArrayList<FileParsers.Group> allTimes = new ArrayList<>();

                    for( String _time: _allTimes ){
                        if( fileData.get().classData.groupsByTime.containsKey(_time) ){
                            //for( FileParsers.Group __group : fileData.get().classData.groupsByTime.get(_time) ){
                                allTimes.add(fileData.get().classData.groupsByTime.get(_time));
                            //}
                        }
                    }

                    for( FileParsers.Group time2 : allTimes ){
                        if( time2 == time )
                            continue;
                        if( !curAssignment.containsKey(time2) )
                            continue;

                        int beforePenalty2 = grade(time2, curAssignment.get(time2));
                        int afterPenalty2 = gradeWith(time2, curAssignment.get(time2), student);

                        int diff2 = beforePenalty2 - afterPenalty2;

                        int totalDiff = diff2 + diff1;



                        if( totalDiff >= 0 ){
                            if( zeroCounter >= 200 ){
                                return;
                            }else if(totalDiff == 0){
                                zeroCounter++;
                            }else{
                                zeroCounter = 0;
                            }
                            swapped = true;



                            curAssignment.get(time).remove(curAssignment.get(time).indexOf(student));
                            curAssignment.get(time2).add(student);

                            break;
                        }
                    }
                }
            }

        }while(swapped);
    }

    private int grade(FileParsers.Group timeSlot, ArrayList<FileParsers.Student> studentInTimeSlot ){
        int belowMin = fileData.get().objectiveData.belowMinPenalty;
        int aboveMax = fileData.get().objectiveData.aboveMaxPenalty;
        int possChoice = fileData.get().objectiveData.possibleChoicePenalty;
        int genderSolo = fileData.get().objectiveData.genderSoloPenalty;
        int professorPenalty = fileData.get().objectiveData.diffProfessorPenalty;
        int impossChoice = 10;

        int grade = 0;
        int male = 0;
        int female = 0;
        String sharedProfessor = "";
        boolean profShared = false;
        String justTime = timeSlot.time;

        if( studentInTimeSlot.size() < fileData.get().objectiveData.minGroupSize ){
            grade = grade + (belowMin * (fileData.get().objectiveData.minGroupSize - studentInTimeSlot.size()) );
        }

        if( studentInTimeSlot.size() > fileData.get().objectiveData.maxGroupSize ){
            grade = grade + (aboveMax * (studentInTimeSlot.size() - fileData.get().objectiveData.maxGroupSize));
        }

        for( int i = 0; i < studentInTimeSlot.size(); i++ ){
            //System.out.println( "Student: "+ studentInTimeSlot.get(i).email );
            //System.out.println( "Students list: " + fileData.get().studentData.students );
            FileParsers.Student stud = fileData.get().studentData.students.get(studentInTimeSlot.get(i).email);

            if( stud.sex.toLowerCase().charAt(0) == 'm' ){
                male++;
            }
            if( stud.sex.toLowerCase().charAt(0) == 'f' ){
                female++;
            }

            if( i == 0 )
                sharedProfessor = stud.professor;
            else if( sharedProfessor.compareTo(stud.professor) == 0 )
                profShared = true;

            if( stud.possibleTimes.contains(justTime)){
                grade = grade + possChoice;
            }
            else if(!stud.goodTimes.contains(justTime)){
                grade += impossChoice;
            }
        }

        if( male == 1 && female > 1 || female == 1 && male > 1 ){
            grade += genderSolo;
        }
        if( !profShared )
            grade += professorPenalty;

        return grade;
    }

    private int gradeSwap( FileParsers.Group timeSlot, ArrayList<FileParsers.Student> studentsInTimeSlot, FileParsers.Student studentToAdd, FileParsers.Student studentToRemove ){
        int studentIndex = studentsInTimeSlot.indexOf(studentToRemove);

        studentsInTimeSlot.remove(studentIndex);

        studentsInTimeSlot.add(studentToAdd);

        int grade = grade(timeSlot, studentsInTimeSlot);

        studentsInTimeSlot.remove(studentsInTimeSlot.size() - 1);

        studentsInTimeSlot.add(studentIndex,studentToRemove);

        return grade;
    }

    private int gradeWith(FileParsers.Group timeSlot, ArrayList<FileParsers.Student> studentsInTimeSlot, FileParsers.Student extraStudent ){
        studentsInTimeSlot.add(extraStudent);

        int grade = grade(timeSlot,studentsInTimeSlot);

        studentsInTimeSlot.remove(studentsInTimeSlot.size()-1);

        return grade;
    }

    private int gradeWithout(FileParsers.Group timeSlot, ArrayList<FileParsers.Student> studentsInTimeSlot, FileParsers.Student removeStudent ) {
        int studentIndex = studentsInTimeSlot.indexOf(removeStudent);
        studentsInTimeSlot.remove(studentIndex);
        int grade = grade(timeSlot, studentsInTimeSlot);

        studentsInTimeSlot.add(studentIndex, removeStudent);

        return grade;
    }

    private int gradeTotal(HashMap<FileParsers.Group, ArrayList<FileParsers.Student>> times ){
        int total = 0;
        Set<FileParsers.Group> groups = times.keySet();
        for( FileParsers.Group g : groups ){
            total += grade( g, times.get(g) );
        }
        return total;
    }


    private HashMap<FileParsers.Group, ArrayList<FileParsers.Student>> swap( HashMap<FileParsers.Group, ArrayList<FileParsers.Student>>  curAssignment ){

        singleSwap(curAssignment);
        doubleSwap(curAssignment);

        return curAssignment;
    }

    private int randNumber(int min, int max){
        return (int)Math.floor(Math.random() * (max-min) + min);
    }

    //main method to run everything
    public static void main(String[] args) {
        LocalSearch localSearch = new LocalSearch(Integer.parseInt(args[0]), args[1]);
        System.exit(0);
    }
}