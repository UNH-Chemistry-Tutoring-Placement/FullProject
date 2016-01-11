import java.util.Comparator;

/**
 * GroupCompartor.java- How the groups get ordered (the value heuristic).
 * @author Stephen Chambers
 * @author Michaela Tremblay
 *
 */
public class GroupComparator implements Comparator<Group> {

	/**
	 * Compute hval for each group and compare the two.
	 * hval = demand / (remainingSlots in the group)
	 */
	@Override
	public int compare(Group o1, Group o2) {
		int maxGSize = Objective.getMaxGroupSize();
		
		if(o1.getStudentCount() == 0)
			return -1;
		if(o2.getStudentCount() == 0)
			return 1;
		
		int remSlots1 = maxGSize - o1.getStudentCount();
		int remSlots2 = maxGSize - o2.getStudentCount();
		int val1 = (remSlots1 > 0) ? o1.getDemand()/remSlots1 : 0;
		int val2 = (remSlots2 > 0) ? o2.getDemand()/remSlots2 : 0;
		return Integer.compare(val2, val1);
	}
}
