package cs3500.animator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Represents motion for a shape with the start time, end time, and a list of final states.
 */
public class ShapeMotion implements IMotion {

  private int start; // Starting time
  private int end; // Ending time
  private List<String> states; // State the shape should be at end

  /**
   * Constructs a command for the shape with the specified start and end time with no state
   * changes.
   *
   * @param start Starting time
   * @param end End time
   * @throws IllegalArgumentException if start or end are negative, or ends before it starts or
   *                                  starts after it ends
   */
  public ShapeMotion(int start, int end) {
    if (start > end || end < 0 || start < 0) {
      throw new IllegalArgumentException("Invalid start and end");
    }
    this.start = start;
    this.end = end;
    states = new ArrayList<>();
  }

  @Override
  public int getStart() {
    return this.start;
  }

  @Override
  public int getEnd() {
    return this.end;
  }

  @Override
  public void setStart(int start) {
    this.start = start;
  }

  @Override
  public void setEnd(int end) {
    this.end = end;
  }

  @Override
  public void addState(String state) throws IllegalArgumentException {
    if (state == null) {
      throw new IllegalArgumentException("Null state");
    }
    Scanner scan = new Scanner(state);
    String command;
    try {
      command = scan.next();
    } catch (NoSuchElementException e) {
      throw new IllegalArgumentException("No command found");
    }
    // Makes sure the command has enough parameters
    switch (command) {
      // Size and move only need 2 parameters
      case "size":
      case "move":
        try {
          scan.nextInt();
          scan.nextInt();
        } catch (NoSuchElementException e) {
          throw new IllegalArgumentException("Not enough parameters given");
        }
        break;
      // Color needs 3 parameters
      case "color":
        try {
          scan.nextInt();
          scan.nextInt();
          scan.nextInt();
        } catch (NoSuchElementException e) {
          throw new IllegalArgumentException("Not enough RGB values given");
        }
        break;
      default:
        throw new IllegalArgumentException("Invalid command");
    }
    states.add(state);
  }

  /**
   * Returns a list of states that the shape should be in at the ending time.
   *
   * @return List of states
   */
  @Override
  public List<String> getStates() {
    List<String> result = new ArrayList<>();
    // Creates a new list to prevent tampering with the list of states
    result.addAll(states);
    return result;
  }
}
