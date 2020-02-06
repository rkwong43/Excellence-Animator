package cs3500.animator.model;

import java.util.List;

/**
 * Represents a read-only motion for a shape. Only has the capability to return the start time,
 * end time, and list of states.
 */
public interface IROMotion {
  /**
   * Returns the start time.
   *
   * @return integer of the start time
   */
  int getStart();

  /**
   * Returns the end time.
   *
   * @return integer of the end time.
   */
  int getEnd();

  /**
   * Returns a list of states that the shape should be in at the ending time.
   *
   * @return List of states
   */
  List<String> getStates();
}
