package cs3500.animator.model;

import java.util.List;

/**
 * Represents what an animation model can do. It will be abLe to accept new shapes, add motion to
 * the shapes, and return a description of all the shapes and their motions.
 */
public interface IAnimatorModel {

  /**
   * Adds a shape of the specified type with the specified ID to the animator.
   *
   * @param shape Type of shape
   * @param id  ID of shape
   * @param x X coordinate at start
   * @param y Y coordinate at start
   * @param width Width of shape
   * @param height Height of shape
   * @param r Red value of the shape's color
   * @param g Green value of the shape's color
   * @param b Blue value of the shape's color
   * @throws IllegalArgumentException if the shape type or ID is null, width and height below zero,
   *        the RGB values not within the range of [0, 255], or if the shape type is not supported.
   */
  void addShape(ShapeType shape, String id, int x, int y, int width, int height, int r, int g,
      int b) throws IllegalArgumentException;

  /**
   * Adds a path of motion to the shape with the corresponding ID.
   *
   * @param motion Motion path to add to the shape
   * @param id ID of shape to add the motion to
   * @throws IllegalArgumentException if parameters are null or the ID does not exist or if motion
   *                                   overlaps with existing ones.
   */
  void addMotion(IMotion motion, String id) throws IllegalArgumentException;

  /**
   * Creates a textual description of all the shapes and their corresponding motion path according
   * to time.
   *
   * @return String of all the shapes and their motions
   */
  String outputDescription();

  /**
   * Removes the shape with the specified ID. Will also remove its associated motions if any.
   *
   * @param id ID of the shape to be removed
   * @throws IllegalArgumentException if the ID does not exist
   */
  void removeShape(String id) throws IllegalArgumentException;

  /**
   * Returns a list of Shapes at the specified tick.
   *
   * @param tick The time to get all the states of the shapes
   * @return List of all the Shapes at the specified tick
   * @throws IllegalArgumentException if tick is negative
   */
  List<IColoredShape> getShapesAtTick(int tick) throws IllegalArgumentException;

  /**
   * Returns a list of motions for the specified shape. Motions are read-only.
   *
   * @param id The shape ID to retrieve the motions for
   * @return List of all the motions for the shape
   * @throws IllegalArgumentException if the given ID is null or does not exist
   */
  List<IROMotion> getMotionsForShape(String id) throws IllegalArgumentException;

  /**
   * Sets the bounds of the canvas the model will play in.
   *
   * @param bounds The array of integers the bounds will be, in the order of x, y, width, height
   * @throws IllegalArgumentException if the given array is null
   */
  void setBounds(int[] bounds) throws IllegalArgumentException;

  /**
   * Returns the bounds of the canvas the model will play in.
   *
   * @return Array of integers representing bounds in the order of x, y, width, height
   * @throws IllegalStateException if the bounds have not been initialized yet
   */
  int[] getBounds() throws IllegalStateException;

  /**
   * Returns the greatest end tick represented in the motions. If none exist, returns 0.
   *
   * @return Integer of the last tick in shape motions.
   */
  int getFinalTick();

  /**
   * Returns a list of all the IDs representing shapes within the model.
   *
   * @return List of Strings representing IDs inside the model.
   */
  List<String> getNames();

  /**
   * Resets the shapes to their original states.
   *
   * @throws UnsupportedOperationException if the model has a shape that is not supported
   */
  void resetShapes();

  /**
   * Returns a deep copy of the shape of the specified ID.
   *
   * @param id the ID of the shape
   * @throws IllegalArgumentException if the ID doesn't exist or is null
   */
  IColoredShape getShape(String id);

  /**
   * Adds a keyframe into the animation for the specified shape.
   *
   * @param id The ID to add the keyframe onto
   * @param state The kind of change to process
   * @param tick The tick to add it at
   * @param params Other parameters, which may be x, y, width, height, or color RGB values
   * @throws IllegalArgumentException if the width or height are less than 0, RGB values are invalid
   *                                  or the tick is less than 0, or if the ID doesn't exist
   */
  void addKeyframe(String id, String state, int tick, int ... params);

  /**
   * Removes the keyframe from the animation for the shape at the given time.
   *
   * @param id The ID to remove the keyframe from
   * @param tick The tick to remove the keyframe from
   * @throws IllegalArgumentException if the ID doesn't exist or if the tick does not align with a
   *                                  motion
   */
  void removeKeyframe(String id, int tick);
}
