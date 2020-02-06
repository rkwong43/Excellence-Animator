package cs3500.animator.model;

import static cs3500.animator.model.ShapeType.ELLIPSE;

import java.awt.Color;

/**
 * Represents a colored ellipse with x and y coordinates, width, height, and color. All parameters
 * are mutable other than the {@code ShapeType}.
 */
public class ColoredEllipse extends AColoredShape implements IColoredShape {
  private ShapeType shape = ELLIPSE;

  /**
   * Constructs a colored ellipse with the given parameters.
   * @param x The x coordinate
   * @param y The y coordinate
   * @param width The width
   * @param height The height
   * @param color The color
   * @throws IllegalArgumentException if the color is null
   */
  public ColoredEllipse(int x, int y, int width, int height, Color color) {
    super(x, y, width, height, color);
  }

  @Override
  public ShapeType getShapeType() {
    return this.shape;
  }

}
