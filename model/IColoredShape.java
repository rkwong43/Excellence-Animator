package cs3500.animator.model;

import java.awt.Color;

/**
 * Represents a shape that is colored.
 */
public interface IColoredShape {
  /**
   * Returns the color of the shape.
   *
   * @return the color of the shape
   */
  Color getColor();

  /**
   * Sets the color of the shape.
   *
   * @param color The new color
   * @throws IllegalArgumentException if color is null
   */
  void setColor(Color color) throws IllegalArgumentException;

  /**
   * Gets the Y coordinate of the shape.
   *
   * @return the y coordinate
   */
  int getY();

  /**
   * Gets the X coordinate of the shape.
   *
   * @return the x coordinate
   */
  int getX();

  /**
   * Gets the height of the shape.
   *
   * @return the height
   */
  int getHeight();

  /**
   * Gets the width of the shape.
   *
   * @return the width
   */
  int getWidth();

  /**
   * Sets the x, y, width and height of the shape to the given.
   *
   * @param x The new x coordinate
   * @param y The new y coordinate
   * @param width The new width
   * @param height The new height
   */
  void setShape(int x, int y, int width, int height);

  /**
   * Returns the {@code ShapeType} the shape is for processing in the view.
   *
   * @return the {@code ShapeType} that the class represents.
   */
  ShapeType getShapeType();
}
