package LocalSearch;

import java.util.*;

public class LocalSearch {
    private int grade = 0;
    private int secondsToRun;
    private FileData fileData;
    public LocalSearch( int timeToRun, String outFileName ){

        fileData = new FileData();
        secondsToRun = timeToRun;
        fileData.load();
        HashMap<FileParsers.Group, ArrayList<FileParsers.Student>> assignments = regularSwap();



        //fileData.out(grade, assignments, args[1]);
    }

    private HashMap<FileParsers.Group, ArrayList<FileParsers.Student>> regularSwap(){

        HashMap<FileParsers.Group, ArrayList<FileParsers.Student>>  results = new HashMap<>();

        long timeStart = new Date().getTime();

        while( new Date().getTime() - timeStart < secondsToRun){
            HashMap<FileParsers.Group, ArrayList<FileParsers.Student>>   curAssignment = randomAssignment();

            HashMap<FileParsers.Group, ArrayList<FileParsers.Student>>  result = swap(curAssignment);


        }

        return results;
    }

    private HashMap<FileParsers.Group, ArrayList<FileParsers.Student>>   randomAssignment( ){

        HashMap<FileParsers.Group, ArrayList<FileParsers.Student>>  assignment = new HashMap<>();
        ArrayList<FileParsers.Group> times = new ArrayList<>();
        Iterator<ArrayList<FileParsers.Group>> groupIter = fileData.get().classData.groups.values().iterator();

        while( groupIter.hasNext() ){
            ArrayList<FileParsers.Group> groups= groupIter.next();
            for(FileParsers.Group group : groups) {
                times.add(group);
                assignment.put(group, new ArrayList<>());
            }
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

            if( acutalPossibleTimes.size() == 0 ){
                ArrayList<String> goodTimes = stud.goodTimes;
                ArrayList<String> actualGoodTimes = new ArrayList<>();

                for( String s : goodTimes ){
                    if( fileData.get().classData.groupsByTime.containsKey(s) ){
                        actualGoodTimes.add(s);
                    }
                }

                if( actualGoodTimes.size() == 0 ){
                    Random rand = new Random();
                    int randInt = rand.nextInt(times.size());
                    assignment.get(times.get(randInt)).add(stud);
                }else{
                    Random rand = new Random();
                    int randInt = rand.nextInt(actualGoodTimes.size());
                    int randTime = rand.nextInt(fileData.get().classData.groupsByTime.get(actualGoodTimes.get(randInt)).size());
                    FileParsers.Group group = fileData.get().classData.groupsByTime.get(actualGoodTimes.get(randInt)).get(randTime);
                    assignment.get(group).add(stud);
                }
            }
            else{
                Random rand = new Random();
                int ran = rand.nextInt(acutalPossibleTimes.size());
                int timeRan = rand.nextInt(fileData.get().classData.groupsByTime.get(acutalPossibleTimes.get(ran)).size());
                FileParsers.Group group = fileData.get().classData.groupsByTime.get(acutalPossibleTimes.get(ran)).get(timeRan);

                assignment.get(group).add(stud);
            }
        }

        return assignment;
    }

    private HashMap<FileParsers.Group, ArrayList<FileParsers.Student>> swap( HashMap<FileParsers.Group, ArrayList<FileParsers.Student>>  curAssignment ){

        HashMap<FileParsers.Group, ArrayList<FileParsers.Student>>  result = new HashMap<>();

        return result;
    }

    //main method to run everything
    public static void main(String[] args) {

    }
}