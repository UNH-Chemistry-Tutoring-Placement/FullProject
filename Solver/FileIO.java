import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * FileIO- This class fills in the appropriate data structures from the 
 * config file.
 * @author Stephen Chambers
 * @author Michaela Tremblay
 *
 */
public class FileIO {
	
	/**
	 * Scanner representing stdin
	 */
	private Scanner in;
	
	/**
	 * A list of times, where each time holds a list 
	 * of groups at that time
	 */
	private ArrayList<Time> groupTimes;
	
	/**
	 * A sorted list of students
	 */
	private PriorityQueue<Student> students;
	
	/**
	 * The class and student descriptions
	 */
	private String classDesc, studentDesc;
	
	/**
	 * Just in case we found invalid students,
	 * they are written to a file named
	 * "badstudents.txt"
	 */
	PrintWriter badStudents;
	
	/**
	 * Constructor
	 */
	public FileIO(){
		in = new Scanner(System.in);
		groupTimes = new ArrayList<Time>();
		students = new PriorityQueue<Student>();
		try {
			badStudents = new PrintWriter("badStudents.txt", "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Fill in appropriate data structures from config file
	 */
	public void getInfo(){
		String newFile = removeComments();
		in = new Scanner(newFile);
		readObjectiveFunction();
		readClassInfoFormat();
		readStudentInfoFormat();

		badStudents.close();
	}
	
	/**
	 * Remove all comments from config file for easier parsing
	 * @return
	 * A string representing the config file without comments
	 */
	private String removeComments(){
		StringBuilder sb = new StringBuilder("");
		while(in.hasNext()){
			String line = in.nextLine();
			if(line.equals("") || line.charAt(0) == '#')
				continue;
			sb.append(line);
			sb.append('\n');
		}
		return sb.toString();
	}
	
	/**
	 * Read the objective function 
	 */
	private void readObjectiveFunction(){
		in.skip("Objective Function Format:");
		int format = in.nextInt();
		in.skip("\nDescription:");		
		String desc = in.nextLine().trim();
		in.skip("min group size:");
		int min = in.nextInt();
		in.skip("\nmax group size:");
		int max = in.nextInt();
		in.skip("\nbelow min penalty:");
		int minPen = in.nextInt();
		in.skip("\nabove max penalty:");
		int maxPen = in.nextInt();
		in.skip("\npossible choice penalty:");
		int posPen = in.nextInt();
		in.skip("\ndiff professor penalty:");
		int diffLec = in.nextInt();
		in.skip("\ngender solo penalty:");
		int genBal = in.nextInt();

		Objective.setFormat(format);
		Objective.setDescription(desc);
		Objective.setMinGroupSize(min);
		Objective.setMaxGroupSize(max);
		Objective.setMinPenalty(minPen);
		Objective.setMaxPenalty(maxPen);
		Objective.setPosPenalty(posPen);
		Objective.setDiffLec(diffLec);
		Objective.setGenBal(genBal);
	}
	
	/**
	 * Read the class info format
	 */
	private void readClassInfoFormat(){
		in.skip("\nClass Info Format:");
		/* Skipping the format for now */
		in.nextInt();
		in.skip("\nDescription:");
		classDesc = in.nextLine().trim();
		
		in.skip("Number of professors:");
		int numLectures = in.nextInt();
		in.skip("\n");
		for(int i = 0; i < numLectures; i++){
			in.skip("Name:");
			in.nextLine().trim();
		}
		
		in.skip("Number of groups:");
		int numGroups = in.nextInt();
		in.skip("\n");
		for(int i = 0; i < numGroups; i++){
			in.skip("Name:");
			String taName = in.nextLine().trim();
			in.skip("Email:");
			String taEmail = in.nextLine().trim();
			in.skip("Time:");
			String time = in.nextLine().trim();
			
			Time t = new Time(time);
			if(!groupTimes.contains(t)){
				groupTimes.add(t);
				t.addGroup(new Group(taName, taEmail, time));
			}
			else{
				for(Time tm : groupTimes){
					if(tm.equals(t))
						tm.addGroup(new Group(taName, taEmail, time));
				}
			}
		}
	}
	
	/**
	 * Read the student info format
	 */
	private void readStudentInfoFormat(){
		in.skip("Student Info Format:");
		/* Skipping the format for now */
		in.nextInt();
		in.skip("\nDescription:");
		studentDesc = in.nextLine().trim();
		
		in.skip("Number of students:");
		int numStudents = in.nextInt();
		in.skip("\n");
		for(int i = 0; i < numStudents; i++){
			in.skip("Name:");
			String name = in.nextLine().trim();
			in.skip("Email:");
			String email = in.nextLine().trim();
			in.skip("Professor:");
			String professor = in.nextLine().trim();
			in.skip("Year:");
			String year = in.nextLine().trim();
			in.skip("Sex:");
			String gender = in.nextLine().trim();

			Student s = new Student(name, email, year, gender, professor);
			
			in.skip("Number of good times:");
			int nGTimes = in.nextInt();
			in.skip("\n");
			for(int j = 0; j < nGTimes; j++){
				String time = in.nextLine();
				for(Time tm : groupTimes){
					if(tm.getTime().equals(time)){
						ArrayList<Group> groups = tm.getGroups();
						s.addGoodGroups(groups);
						for(Group g : groups)
							g.increaseDemand();
					}
				}
			}
			
			in.skip("Number of possible times:");
			int nPTimes = in.nextInt();

			in.skip("\n");
			for(int j = 0; j < nPTimes; j++){
				String time = in.nextLine();
				for(Time tm : groupTimes){
					if(tm.getTime().equals(time)){
						ArrayList<Group> groups = tm.getGroups();
						s.addPossibleGroups(groups);
						for(Group g : groups)
							g.increaseDemand();
					}
				}
			}
			if(s.getPossibleGroups().size() == 0 && s.getGoodGroups().size() == 0){
				badStudents.println(s.getName());
			} else{
				students.add(s);
			}
		}
	}
	
	
	
	/**
	 * Gets the group times
	 * @return the groupTimes
	 */
	public ArrayList<Time> getGroupTimes() {
		return groupTimes;
	}

	/**
	 * Gets the students
	 * @return the students
	 */
	public PriorityQueue<Student> getStudents() {
		return students;
	}

	/**
	 * Print all the students (debug method)
	 */
	public void printStudents(){
		for(Student s : students){
			System.out.println(s);
			printGroupTimes(s);
		}
	}
	
	/**
	 * Gets the class description
	 * @return the classDesc
	 */
	public String getClassDesc() {
		return classDesc;
	}

	/**
	 * Gets the student description
	 * @return the studentDesc
	 */
	public String getStudentDesc() {
		return studentDesc;
	}

	/**
	 * Print the group times of this student (debug method)
	 * @param s
	 * The student to print
	 */
	private void printGroupTimes(Student s){
		System.out.println("   Good Groups:");
		for(Group g : s.getGoodGroups())
			System.out.println("\t" + g.getTime() + " " + g.getTA());
		System.out.println("   Possible Groups:");
		for(Group g : s.getPossibleGroups())
			System.out.println("\t" + g.getTime() + " " + g.getTA());
	}
}