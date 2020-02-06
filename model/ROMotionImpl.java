package cs3500.animator.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a read-only motion. Has the ability to return the start, end, and list of states.
 */
public class ROMotionImpl implements IROMotion {

  private final IMotion motion;

  /**
   * Constructs a read-only motion with the given motion.
   *
   * @param motion The motion to grab information from
   * @throws IllegalArgumentException if the given motion is null
   */
  public ROMotionImpl(IMotion motion) {
    this.motion = motion;
  }


  @Override
  public int getStart() {
    return motion.getStart();
  }

  @Override
  public int getEnd() {
    return motion.getEnd();
  }

  @Override
  public List<String> getStates() {
    List<String> result = new ArrayList<>();
    result.addAll(motion.getStates());
    return result;
  }
}
