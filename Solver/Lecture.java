/**
 * Lecture- Doesn't really do anything right now, but a 
 * representation will be important in further objective functions
 * @author Stephen, Michaela
 *
 */
public class Lecture {
	
	private String lec;
	
	/**
	 * Constructor
	 * @param lec
	 * The lecture time
	 */
	public Lecture(String lec){
		this.lec = lec;
	}

	/**
	 * @param lec the lec to set
	 */
	public void setLec(String lec) {
		this.lec = lec;
	}


	/**
	 * @return the lecture time
	 */
	public String getLec() {
		return lec;
	}
}
