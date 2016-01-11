import java.util.ArrayList;


/**
 * Student- A class representing a student and their currently
 * assigned study group
 * @author Stephen Chambers
 * @author Michaela Tremblay
 *
 */
public class Student implements Comparable<Student>{
	
	/**
	 * Student information
	 */
	private String name, email, year, gender, professor;
	
	/**
	 * Student group information
	 */
	private ArrayList<Group> goodG, possibleG, compGood, uncompGood, compPos, uncompPos;
	
	/**
	 * The current assigned group for the student
	 */
	private Group assignedGroup;
	
	/**
	 * Constructor for the Student class
	 * @param name
	 * The name of the student
	 * @param email
	 * The email of the student
	 * @param year
	 * The year of the student
	 * @param gender
	 * The gender of the student
	 */
	public Student(String name, String email, String year, String gender, String professor){
		this.name = name;
		this.email = email;
		this.year = year;
		this.gender = gender;
		this.professor = professor;
		goodG = new ArrayList<Group>();
		possibleG = new ArrayList<Group>();
		compGood = new ArrayList<Group>();
		uncompGood = new ArrayList<Group>();
		compPos = new ArrayList<Group>();
		uncompPos = new ArrayList<Group>();
	}
	
	/**
	 * Copy constructor
	 * @param s
	 * The student to copy
	 */
	public Student(Student s){
		this(s.getName(), s.getEmail(), s.getYear(), s.getGender(), s.getProfessor());
		assignedGroup = s.getGroupAssignment();
	}
	
	/**
	 * Assign this student to a group.
	 * @param g the group that the student is being assigned to
	 */
	public void assignGroup(Group g){
		assignedGroup = g;
		g.addStudent(this);
		g.decreaseDemand();
	}
	
	/**
	 * Returns true if the student has a group set
	 * @return whether or not the student has a group set
	 */
	public boolean isGroupSet(){
		return assignedGroup != null;
	}
	
	/**
	 * Unset the student from this group
	 * @param g the group that the student is being unset from
	 */
	public void unsetGroup(Group g){
		assignedGroup = null;
		g.removeStudent(this);
		g.increaseDemand();
	}
	
	/**
	 * Get group assignment for this student
	 * @return the group assignment for this student
	 */
	public Group getGroupAssignment(){
		return assignedGroup;
	}
	
	/**
	 * Add a list of good study groups to this student
	 * @param g a list of good study groups to this student
	 */
	public void addGoodGroups(ArrayList<Group> goodGroups){
		 goodG.addAll(goodGroups);
	}
	
	/**
	 * Add a list of possible study groups to this student
	 * @param g a list of possible study groups to this student
	 */
	public void addPossibleGroups(ArrayList<Group> possibleGroups){
		 possibleG.addAll(possibleGroups);
	}
	
	/**
	 * Get a list of this students good groups
	 * @return the list of good groups for this student
	 */
	public ArrayList<Group> getGoodGroups(){
		return goodG;
	}
	
	/**
	 * Get a list of good compatible groups for this student, where
	 * compatible is defined as having the same lecturer as the 
	 * current group
	 * @return the list of good compatible groups for this student
	 */
	public ArrayList<Group> getGoodCompGroups(){
		return compGood;
	}
	
	/**
	 * Get a list of good incompatible groups for this student
	 * @return a list of good incompatible groups for this student
	 */
	public ArrayList<Group> getGoodUncompGroups(){
		return uncompGood;
	}

	/**
	 * Sort the good groups by the value heuristic.
	 * First sort the groups by lecturer into:
	 * GoodComp GoodUncomp
	 * And then sort those layers by the h value as
	 * defined in the GroupComparator
	 */
	public void sortGoodGroups() {
		goodG.sort(new GroupComparator());
		compGood.clear();
		uncompGood.clear();
		for(Group g : goodG){
			if(!g.getProfessor().equals("") && !g.getProfessor().equals(professor)){
				uncompGood.add(g);
			} else{
				compGood.add(g);
			}
		}
	}
	
	/**
	 * Get a list of this students possible groups
	 * @return the possible groups for this student
	 */
	public ArrayList<Group> getPossibleGroups(){
		return possibleG;
	}
	
	/**
	 * Get a list of this students possible compatible groups, 
	 * where compatible is defined as the same as Good Compatible.
	 * @return the possible compatible groups for this student
	 */
	public ArrayList<Group> getPossibleCompGroups(){
		return compPos;
	}
	/**
	 * Get a list of this students possible incompatible groups.
	 * @return a list of this students possible incompatible groups.
	 */
	public ArrayList<Group> getPossibleUncompGroups(){
		return uncompPos;
	}

	/**
	 * Sort the possible groups by the value heuristic.
	 * First sort the groups by lecturer into:
	 * PossilbeComp PossibleUncomp
	 * And then sort those layers by the h value as
	 * defined in the GroupComparator
	 */
	public void sortPossibleGroups() {
		possibleG.sort(new GroupComparator());
		compPos.clear();
		uncompPos.clear();
		for(Group g : possibleG){
			if(!g.getProfessor().equals("") && !g.getProfessor().equals(professor)){
				uncompPos.add(g);
			} else{
				compPos.add(g);
			}
		}
	}
	
	/**
	 * Get this students name
	 * @return the name of the student
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get this students email
	 * @return the email of the student
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Get this students gender
	 * @return the gender of the student
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * Get this students year
	 * @return the year of the student
	 */
	public String getYear() {
		return year;
	}
	
	/**
	 * Get this students professor
	 * @return the professor of the student
	 */
	public String getProfessor() {
		return professor;
	}

	/**
	 * Returns a string representation of this student
	 */
	public String toString(){
		StringBuilder sb = new StringBuilder("Name: ");
		sb.append(name);
		sb.append("\nEmail: ");
		sb.append(email);
		return sb.toString();
	}

	/**
	 * The variable heuristic. When choosing which student to assign a group 
	 * to, we want to chose the most constrained student, or the student with
	 * the least available options. Therefore, we sum up the remaining good 
	 * and possible slots of the student, and use that as the h value. The
	 * smallest number goes to the front of the priority queue.
	 */
	@Override
	public int compareTo(Student other) {
		int maxGSize = Objective.getMaxGroupSize();
		
		int gSum = 0;
		int pSum = 0;
		int oGSum = 0;
		int oPSum = 0;
		for(Group g: this.getGoodGroups()){
			if(maxGSize - g.getStudentCount() > 0){
				gSum += maxGSize - g.getStudentCount();
			}
		}
		for(Group g: other.getGoodGroups()){
			if(maxGSize - g.getStudentCount() > 0){
				oGSum += maxGSize - g.getStudentCount();
			}
		}
		for(Group g: this.getPossibleGroups()){
			if(maxGSize - g.getStudentCount() > 0){
				pSum += maxGSize - g.getStudentCount();
			}
		}
		for(Group g: other.getPossibleGroups()){
			if(maxGSize - g.getStudentCount() > 0){
				oPSum += maxGSize - g.getStudentCount();
			}
		}
		
		if(gSum + pSum != oGSum + oPSum)
			return Integer.compare(gSum + pSum, oGSum + oPSum);
		if(gSum == 0)
			return 1;
		if(oGSum == 0)
			return -1;
		return Integer.compare(gSum, oGSum);
	}
}
