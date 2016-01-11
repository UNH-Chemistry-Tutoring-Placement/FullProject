import java.util.ArrayList;


/**
 * Time- Holds a time and a list of study groups at that time
 * @author Stephen Chambers
 * @author Michaela Tremblay
 *
 */
public class Time {
	
	private String time;
	private ArrayList<Group> groups;
	
	/**
	 * Constructor
	 * @param time
	 * A string representing the time
	 */
	public Time(String time){
		this.time = time;
		groups = new ArrayList<Group>();
	}
	
	/**
	 * Add a study group to this time
	 * @param g
	 * The group to add
	 */
	public void addGroup(Group g){
		groups.add(g);
	}
	
	/**
	 * Get a list of groups at this time
	 * @return
	 * A list of groups at this time
	 */
	public ArrayList<Group> getGroups(){
		return groups;
	}
	
	/**
	 * Returns the current time.
	 * @return
	 * A string representing the current time
	 */
	public String getTime(){
		return time;
	}
	
	/**
	 * Returns true if the strings representing the times are equal.
	 */
	@Override
	public boolean equals(Object obj){
	       if (obj == null) {
	           return false;
	       }
	       if (getClass() != obj.getClass()) {
	           return false;
	       }
	       final Time t = (Time) obj;

	       return time.equals(t.getTime());
	}
	
}
