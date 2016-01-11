
/**
 * Group- This class represents a study group.
 * @author Stephen Chambers
 * @author Michaela Tremblay
 *
 */
public class Group {
	/**
	 * The TA information
	 */
	private String taName, taEmail;
	
	/**
	 * The time the group is at
	 */
	private String time;
	
	/**
	 * The groups current professor
	 */
	private String professor;
	
	/**
	 * How many students are in the group
	 */
	private int studentCount = 0;
	
	/**
	 * How many students want to get in to the group
	 */
	private int demand = 0;
	
	/**
	 * The number of males in the group
	 */
	private int males = 0;
	
	/**
	 * The number of females in the group
	 */
	private int females = 0;
	
	/**
	 * The current number of professor discrepancies.
	 * For example, if this group was set to professor A,
	 * and two students in this group were in professor B,
	 * and one was in professor C, profCost would be set to 3.
	 */
	private int profDiscrepancies = 0;
	
	/**
	 * Constructor for the Group class
	 * @param taName
	 * The name of the TA responsible for teaching this study group
	 * @param time
	 * The time of the study group
	 */
	public Group(String taName, String taEmail, String time){
		this.taName = taName;
		this.taEmail = taEmail;
		this.time = time;
		this.professor = "";
	}
	
	/**
	 * Returns if two objects are equal or not
	 * @param obj
	 * The object to compare against
	 */
	@Override
	public boolean equals(Object obj){
	       if (obj == null) {
	           return false;
	       }
	       if (getClass() != obj.getClass()) {
	           return false;
	       }
	       final Group rec = (Group) obj;
	       
	       if(taName.equals(rec.getTA()) && time.equals(rec.getTime()))
	    	   return true;

	       return false;
	}
	

	/**
	 * Get the time
	 * @return
	 * The time of this study group
	 */
	public String getTime() {
		return time;
	}

	/**
	 * Get the TA
	 * @return
	 * The TA of this study group
	 */
	public String getTA() {
		return taName;
	}
	
	/**
	 * Get the TA Email
	 * @return
	 * The TA of this study group's email
	 */
	public String getTAEmail() {
		return taEmail;
	}
	
	/**
	 * Add a student to this study group
	 * @param s
	 * The student to add
	 */
	public void addStudent(Student s){
		if(s.getGender().equals("Male")){
			males++;
		}
		else if(s.getGender().equals("Female")){
			females++;
		}
		studentCount++;
	}
	
	/**
	 * Remove a student from this study group
	 * @param s 
	 * The student to remove
	 */
	public void removeStudent(Student s){
		if(s.getGender().equals("Male")){
			males--;
		}
		else if(s.getGender().equals("Female")){
			females--;
		}
		studentCount--;
	}
	
	/**
	 * Get the current count of students in this study group
	 * @return
	 */
	public int getStudentCount(){
		return studentCount;
	}
	
	/**
	 * toString
	 * @return
	 * The string representation of this group
	 */
	public String toString(){
		StringBuilder sb = new StringBuilder(time);
		sb.append(" with ");
		sb.append(taName);
		return sb.toString();
	}

	/**
	 * Increase the demand of this group
	 */
	public void increaseDemand() {
		demand++;
	}

	/**
	 * Decrease the demand of this group
	 */
	public void decreaseDemand() {
		demand--;
	}
	
	/**
	 * Get the demand of the group
	 * @return the demand of the group
	 */
	public int getDemand(){
		return demand;
	}
	
	/**
	 * Get the number of males in this group
	 * @return the number of males in this group
	 */
	public int getMales(){
		return males;
	}
	
	/**
	 * Get the number of females in this group
	 * @return the number of females in this group
	 */
	public int getFemales(){
		return females;
	}

	/**
	 * Set the professor for this group
	 * @param prof
	 * The new professor
	 */
	public void setProfessor(String prof) {
		professor = prof;
	}

	/**
	 * Get the professor for this group
	 * @return the professor for this group
	 */
	public String getProfessor() {
		return professor;
	}

	/**
	 * Increase the number of professor discrepancies
	 */
	public void incProfDiscrepancies() {
		profDiscrepancies++;
	}

	/**
	 * Decrease the number of professor discrepancies
	 */
	public void decProfDiscrepancies() {
		profDiscrepancies--;
	}
	
	/**
	 * Check if this group has professor discrepancies
	 * @return whether or not this group has professor discrepancies
	 */
	public boolean hasProfDiscrepancy(){
		return profDiscrepancies != 0;
	}
}
