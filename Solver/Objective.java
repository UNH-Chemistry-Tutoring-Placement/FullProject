/**
 * Objective- A class holding information related to the objective function
 * @author Stephen Chambers
 * @author Michaela Tremblay
 *
 */
public class Objective {
	
	/**
	 * Variable for each cost in the objective function
	 */
	private static int format, minGroupSize, maxGroupSize;
	private static int minPenalty, maxPenalty, posPenalty;
	private static int diffLec, genBal;
	private static String description;
	
	/**
	 * Gets the objective format represented by an integer
	 * @return an integer representing the objective format
	 */
	public static int getFormat() {
		return format;
	}

	/**
	 * Sets the objective format
	 * @param the new objective format
	 */
	public static void setFormat(int format) {
		Objective.format = format;
	}

	/**
	 * Set the minimum group size
	 * @param minGroupSize the minGroupSize to set
	 */
	public static void setMinGroupSize(int minGroupSize) {
		Objective.minGroupSize = minGroupSize;
	}

	/**
	 * Gets the maximum group size
	 * @param maxGroupSize the maxGroupSize to set
	 */
	public static void setMaxGroupSize(int maxGroupSize) {
		Objective.maxGroupSize = maxGroupSize;
	}

	/**
	 * Gets the minimum group size penalty
	 * @param minPenalty the minPenalty to set
	 */
	public static void setMinPenalty(int minPenalty) {
		Objective.minPenalty = minPenalty;
	}

	/**
	 * Sets the maximum group size penalty
	 * @param maxPenalty the maxPenalty to set
	 */
	public static void setMaxPenalty(int maxPenalty) {
		Objective.maxPenalty = maxPenalty;
	}

	/**
	 * Sets the penalty for if a student is 
	 * placed in a group that is possible for them
	 * but not prefered.
	 * @param posPenalty the posPenalty to set
	 */
	public static void setPosPenalty(int posPenalty) {
		Objective.posPenalty = posPenalty;
	}

	/**
	 * Sets the penalty for a student being
	 * in a different lecture than the 
	 * current group
	 * @param diffLec the diffLec to set
	 */
	public static void setDiffLec(int diffLec) {
		Objective.diffLec = diffLec;
	}

	/**
	 * Sets the solo gender penalty. This
	 * penalty is incurred if one male is
	 * in a group of all females or vice
	 * versa
	 * @param genBal the genBal to set
	 */
	public static void setGenBal(int genBal) {
		Objective.genBal = genBal;
	}

	/**
	 * Set the description of the obejctive function
	 * @param description the description to set
	 */
	public static void setDescription(String description) {
		Objective.description = description;
	}

	/**
	 * Get the minimum group size
	 * @return the minGroupSize
	 */
	public static int getMinGroupSize() {
		return minGroupSize;
	}

	/**
	 * Get the maximum group size
	 * @return the maxGroupSize
	 */
	public static int getMaxGroupSize() {
		return maxGroupSize;
	}

	/**
	 * Get the minimum group size penalty
	 * @return the minPenalty
	 */
	public static int getMinPenalty() {
		return minPenalty;
	}

	/**
	 * Get the maximum group size penalty
	 * @return the maxPenalty
	 */
	public static int getMaxPenalty() {
		return maxPenalty;
	}

	/**
	 * Get the penalty for being a 'possible' time
	 * @return the posPenalty
	 */
	public static int getPosPenalty() {
		return posPenalty;
	}

	/**
	 * Get the description of the objective function
	 * @return the description
	 */
	public static String getDescription() {
		return description;
	}
	
	/**
	 * Get the different lecture penalty
	 * @return the different lecture penalty
	 */
	public static int getDiffLec(){
		return diffLec;
	}
	
	/**
	 * Get the gender balance penalty
	 * @return the gender balance penalty
	 */
	public static int getGenBal(){
		return genBal;
	}	
}
