package cs3500.animator.model;

import java.util.Comparator;

/**
 * Sorts the {@code IMotion} based on start time.
 */
public class MotionStartTimeComparator implements Comparator<IMotion> {

  @Override
  public int compare(IMotion that, IMotion other) {
    Integer start1 = that.getStart();
    Integer start2 = other.getStart();
    return start1.compareTo(start2);
  }
}
