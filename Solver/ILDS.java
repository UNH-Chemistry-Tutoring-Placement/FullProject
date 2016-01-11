import java.util.ArrayList;
import java.util.PriorityQueue;


/**
 * ILDS- assigns each student to a study group, minimizing
 * the objective function using improved limited discrepancy search
 * @author Stephen Chambers
 * @author Michaela Tremblay
 *
 */
public class ILDS {
	
	/**
	 * The best assignment so far. This is checked at every leaf.
	 */
	private ArrayList<Student> bestAssignments;
	
	/**
	 * The current student assignment
	 */
	private ArrayList<Student> curAssignments;
	
	/**
	 * A sorted list of students. Students are sorted by the 
	 * variable heuristic.
	 */
	private PriorityQueue<Student> students;
	
	/**
	 * A list of times, which hold groups at that time
	 */
	private ArrayList<Time> times;
	
	/**
	 * The number of students
	 */
	private int sSize;
	
	/**
	 * The initial score, calculated by the objective function
	 */
	private int initialScore;
	
	/**
	 * The best score so far. This is the value that is checked 
	 * for the brand-and-bound technique.
	 */
	private int maxScore = Integer.MAX_VALUE;
	
	/**
	 * The timeout for this program. A thread will cut the solver off
	 * after 'timeout' seconds and return the best assignment so far.
	 */
	private boolean timeout = false;
	
	/**
	 * The current discrepancy limit, for use in ILDS
	 */
	private int discLimit = 0;
	
	/**
	 * Constructor for the DepthFirstSearch class
	 * @param students
	 * The list of students that need groups
	 * @param t
	 * An array list of times, which hold possible study groups
	 */
	public ILDS(PriorityQueue<Student> students, ArrayList<Time> t){
		this.students = students;
		times = t;
		sSize = students.size();
		initialScore = 0;
		bestAssignments = new ArrayList<Student>();
		curAssignments = new ArrayList<Student>();
	}

	/**
	 * Assign each student to a study group, minimizing the objective score
	 */
	public void solve(){
		if(sSize > 0){
			while(discLimit < sSize){
				solve(0, initialScore, 0);
				discLimit++;
			}
		}
		else{
			System.err.println("No students provided.");
			System.exit(-1);
		}
	}
	
	/**
	 * The recursive helper function that does most of the work
	 * @param sIndex
	 * The index representing the current student trying to be filled in
	 * @param curScore
	 * The current score of this descent
	 * @param numDiscs
	 * The current number of discrepancies
	 * @return
	 */
	private int solve(int sIndex, int curScore, int numDiscs){
		/**
		 * Timeout has been reached, return the best score so far (min cost).
		 */
		if(timeout){
			return maxScore;
		}
		/**
		 * We are at a leaf! Check if the max score and best
		 * assignment so far need to be updated.
		 */
		if(students.isEmpty()){
			/**
			 * TODO: Most of the costs are updated at the leaf.
			 * THerefore, a lower-bounded cost-to-go would utilize
			 * branch-and-bound much better.
			 */
			curScore += updateMinScore();
			curScore += updateProfScore();
			curScore += updateGenderScore();
			if(curScore <= maxScore){
				maxScore = curScore;
				bestAssignments.clear();
				for(Student s : curAssignments){
					bestAssignments.add(new Student(s));
				}
			}
			
			return curScore;
		}
		
		/**
		 * Get the most constrained student
		 */
		Student cur = students.poll();
		curAssignments.add(cur);
		int choice = 0;
		
		/**
		 * Go through the groups in the following order:
		 *    Good* Possible* Good Possible
		 * Where Good* are compatible groups, or groups
		 * that are in the same group lecture.
		 */
		cur.sortGoodGroups();
		for(Group g : cur.getGoodCompGroups()){
			setGroup(cur, g, choice, curScore, sIndex, numDiscs, true);
			choice++;
		}

		cur.sortPossibleGroups();
		for(Group g : cur.getPossibleCompGroups()){
			setGroup(cur, g, choice, curScore, sIndex, numDiscs, false);
			choice++;
		}
		
		for(Group g : cur.getGoodUncompGroups()){
			setGroup(cur, g, choice, curScore, sIndex, numDiscs, true);
			choice++;
		}
		
		for(Group g : cur.getPossibleUncompGroups()){
			setGroup(cur, g, choice, curScore, sIndex, numDiscs, false);
			choice++;
		}
		/* backtrack and add student back into priority queue */
		curAssignments.remove(cur);
		students.add(cur);
		
		return maxScore;
	}
	
	/**
	 * Helper function that basically does all of the work
	 * @param cur
	 * The current student being set
	 * @param g
	 * The group we are trying to set the student in
	 * @param choice
	 * The current choice
	 * @param curScore
	 * The current score
	 * @param sIndex
	 * The index of this student into the students array
	 * @param numDiscs
	 * The current number of discrepancies
	 * @param good
	 * Whether or not this is a "good" group
	 */
	private void  setGroup(Student cur, Group g, int choice, int curScore, 
			int sIndex, int numDiscs, boolean good){
		boolean setProfessorHere = false;
		boolean incurredProfessorCost = false;
		if(g.getProfessor().equals("")){
			g.setProfessor(cur.getProfessor());
			setProfessorHere = true;
		} else if(!g.getProfessor().equals(cur.getProfessor())){
			incurredProfessorCost = true;
			g.incProfDiscrepancies();
		}
		cur.assignGroup(g);
		int score = calculateScore(curScore, g, good);
		if(score < maxScore){
			if(choice == 0){
				solve(sIndex+1, score, numDiscs );
			}
			else if(numDiscs < discLimit){
				solve(sIndex+1, score, numDiscs + 1);
			}
		}
		
		/* Assigning group g to the student failed. Try another group. */
		if(setProfessorHere){
			g.setProfessor("");
		}
		if(incurredProfessorCost){
			g.decProfDiscrepancies();
		}
		cur.unsetGroup(g);
	}

	/**
	 * Updates the gender score
	 * @return the additional gender score that needs to be added
	 */
	private int updateGenderScore() {
		int score = 0;
		for(Time t : times){
			for(Group g : t.getGroups()){
				if((g.getMales() == 1 && g.getFemales() != 0) 
						|| (g.getFemales() == 1 && g.getMales() != 0)){
					score += Objective.getGenBal();
				}
			}
		}
		return score;
	}

	/**
	 * Updates the professor score
	 * @return the additional professor score that needs to be added
	 */
	private int updateProfScore() {
		int profCost = Objective.getDiffLec();
		int score = 0;
		for(Time t : times){
			for(Group g : t.getGroups()){
				if(g.hasProfDiscrepancy()){
					score += profCost;
				}
			}
		}
		return score;
	}

	/**
	 * Calculate the minimum penalty when a leaf node is reached. 
	 * @return The minimum penalty
	 */
	private int updateMinScore() {
		int min = Objective.getMinGroupSize();
		int minPen = Objective.getMinPenalty();
		int score = 0;
		for(Time t : times){
			for(Group g : t.getGroups()){
				if(g.getStudentCount() < min){
					score += minPen*(min - g.getStudentCount());
				}
			}
		}
		return score;
	}

	/**
	 * Return the solution
	 * @return
	 * A list of assigned students
	 */
	public ArrayList<Student> getSolution(){
		return bestAssignments;
	}
	
	/**
	 * Return the solution cost
	 * @return
	 * The solution cost
	 */
	public int getSolutionCost(){
		return maxScore;
	}


	/**
	 * Calculate the objective score after a group assignment or removal
	 * @param curScore
	 * The current score
	 * @param g
	 * The new group to look at
	 * @param isGood
	 * Boolean representing if the time is a "Good" time
	 * @return
	 */
	private int calculateScore(int curScore, Group g, boolean isGood) {
		int studentCount = g.getStudentCount();
		int dScore = 0;
		if(studentCount > Objective.getMaxGroupSize())
			dScore += Objective.getMaxPenalty();
		
		if(!isGood)
			dScore += Objective.getPosPenalty();
		
		return curScore + dScore;
	}

	/**
	 * A method for the timeout thread.
	 */
	public void end() {
		timeout = true;
	}
}
