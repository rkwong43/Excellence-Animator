package cs3500.animator.model;

import java.util.List;

/**
 * Represents a path of motion, with the start time, end time, and any other commands such as
 * changing color, shape, or size.
 */
public interface IMotion {
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
   * Sets the start time.
   *
   * @param start Tick to start at
   * @throws IllegalArgumentException if the given tick is less than 0
   */
  void setStart(int start);

  /**
   * Sets the end time.
   *
   * @param end Tick to end at
   * @throws IllegalArgumentException if the given tick is less than 0
   */
  void setEnd(int end);

  /**
   * Adds a state for the shape to be in at the end time. Will only take in 1 state per method call.
   * States are given in the form of:
   * move x y
   * color r g b
   * size w h
   * Example: "move 200 20" will move the shape to (200, 20) over the duration of the start and end
   * ticks.
   * @param state State to be in
   * @throws IllegalArgumentException if state is null or invalid
   */

  void addState(String state) throws IllegalArgumentException;

  /**
   * Returns a list of states that the shape should be in at the ending time.
   *
   * @return List of states
   */
  List<String> getStates();
}
