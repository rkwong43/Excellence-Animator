package cs3500.animator.model;

import java.util.Comparator;

/**
 * Sorts the {@code IMotion} based on end time, from greatest to least.
 */
public class MotionEndTimeComparator implements Comparator<IROMotion> {

  @Override
  public int compare(IROMotion that, IROMotion other) {
    Integer start1 = that.getEnd();
    Integer start2 = other.getEnd();
    return start2.compareTo(start1);
  }
}
