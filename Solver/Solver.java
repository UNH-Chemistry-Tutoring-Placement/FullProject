import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class is responsible for solving the algorithm.
 * @author Stephen Chambers
 * @author Michaela Tremblay
 *
 */
public class Solver {
	
	/**
	 * A sorted list of students
	 */
	private PriorityQueue<Student> students;
	
	/**
	 * The current solution cost
	 */
	private int solutionCost;
	
	/**
	 * The FileIO object that reads the input file
	 * and stores information in data structures.
	 */
	private FileIO fileIO;
	
	/**
	 * The timeout for the program
	 */
	private int timeout = 0;
	
	/**
	 * Whether or not this program is being tested locally
	 */
	private static boolean localTesting = true;
	
	/**
	 * Constructor
	 */
	public Solver(){
		getInfo();
	}
	
	/**
	 * Constructor
	 */
	public Solver(int timeout){
	    this.timeout = timeout;
		getInfo();
	}
	
	/**
	 * A method that spawns an ILDS object and calls its solve method. 
	 * If other algorithms were used, this would be a good place to
	 * call their solve methods.
	 */
	public void solve(){
		/* Create a depth first search instance */
		ILDS dfs = new ILDS( 
				students, 
				fileIO.getGroupTimes());
		
		if(timeout != 0){
			Thread thread = new Thread(){
				public void run(){
					Timer timer = new Timer();
					timer.schedule(new TimerTask() {
						  @Override
						  public void run() {
							  dfs.end();
							  solutionCost = dfs.getSolutionCost();
						  }
						}, timeout);
				}
		    };
		    thread.start();
		}
	    
		/* Solve and print the solution */
		dfs.solve();
		printStudentSolution(dfs.getSolution());
	}
	
	/**
	 * Parse input and store results into FileInfo
	 */
	private void getInfo(){
		fileIO = new FileIO();
		fileIO.getInfo();
		students = fileIO.getStudents();
	}
	
	/**
	 * Print the student solution
	 * @param studs
	 * The studly students assigned to their study groups
	 */
	private void printStudentSolution(ArrayList<Student> studs) {
		System.out.println("Solution Format: 3");
		System.out.println("Objective Function: " + Objective.getDescription());
		System.out.println("Class Info: " + fileIO.getClassDesc());
		System.out.println("Student Info:  " + fileIO.getStudentDesc());
		System.out.println("Number of students: " + studs.size());

		for(Student s : studs){
			System.out.println(s.getEmail());
			System.out.println(s.getGroupAssignment().getTime());
			System.out.println(s.getGroupAssignment().getTAEmail());
		}
		System.out.println("Solution cost: " + solutionCost);
	}

	/**
	 * The main method. Sets stdin to a file if local testing
	 * flag is set
	 * @param args
	 */
	public static void main(String[] args){
		
		/* Populate initial world state */
		//String filename = "test.txt";
		//String filename = "bigTest";
		//String filename = "real-students.txt";
		String filename = "remove_students";
        //String filename = "add_times";
		
		if(localTesting){
			try {
				System.setIn(new FileInputStream(filename));
			} catch (FileNotFoundException e) {
				
			}
		}
		Solver s = null;
		if(args.length != 0){
			s = new Solver(Integer.parseInt(args[0]) * 1000);
		} else{
			s = new Solver();
		}
		s.solve();
	}

}
