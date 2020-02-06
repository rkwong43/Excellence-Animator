package cs3500.animator.model;

import java.util.List;

/**
 * Represents a read-only {@code IAnimatorModel}. Only has the ability to return the shapes at a
 * particular tick.
 */
public interface IROAnimatorModel {

  /**
   * Returns a copy of the {@code IColoredShape}s at the specified tick.
   *
   * @param tick The tick to retrieve the shapes at
   * @return List of shapes at the particular tick.
   * @throws IllegalArgumentException if the given tick is less than 0
   */
  List<IColoredShape> getShapesAt(int tick) throws IllegalArgumentException;

  /**
   * Returns a String of the description of the shapes and their movements.
   *
   * @return String of the output from {@code outputDescription()} in {@code IAnimatorModel}
   */
  String getDescription();

  /**
   * Returns a list of read-only motions for the specified ID.
   *
   * @param id The ID of the specified shape
   * @return List of read-only motions for the shape
   * @throws IllegalArgumentException if the ID is null or nonexistent
   */
  List<IROMotion> getMotionsFor(String id) throws IllegalArgumentException;

  /**
   * Returns the bounds of the model to operate in, in the order of x, y, width, height.
   *
   * @return Integer array of the bounds, in order of x, y, width, height
   * @throws IllegalStateException if the bounds have not been declared
   */
  int[] getBounds() throws IllegalStateException;

  /**
   * Returns the final tick of the last motion in the model.
   *
   * @return integer of the final tick
   */
  int getFinalTick();

  /**
   * Returns a list of IDs for all the shapes in the model.
   *
   * @return List of Strings representing IDs
   */
  List<String> getNames();

  /**
   * Loops and resets the shapes to their original states.
   *
   * @return {@code this}, the current read-only model with the reset shapes
   */
  IROAnimatorModel loop();

  /**
   * Returns the shape with the specified ID.
   *
   * @param id The ID of the shape to return
   * @throws IllegalArgumentException if the ID doesn't exit or is null
   */
  IColoredShape getShape(String id);
}
