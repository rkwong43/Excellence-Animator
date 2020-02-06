package cs3500.animator.model;

import java.awt.Color;

/**
 * Abstract class that represents a shape that is colored. All fields are mutable, which include
 * the x and y coordinates, width, height, and color.
 */
public abstract class AColoredShape implements IColoredShape {
  protected Color color; // Color of the rectangle
  protected int x; // x coordinate
  protected int y; // y coordinate
  protected int width; // width
  protected int height; // height

  /**
   * Constructs a colored shape.
   *
   * @param x The x coordinate
   * @param y The y coordinate
   * @param width The width
   * @param height The height
   * @param color The color
   * @throws IllegalArgumentException if the color is null
   */
  protected AColoredShape(int x, int y, int width, int height, Color color) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    if (color != null) {
      this.color = color;
    } else {
      throw new IllegalArgumentException("Null color given");
    }
  }

  @Override
  public Color getColor() {
    return new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue());
  }

  @Override
  public void setColor(Color color) throws IllegalArgumentException {
    if (color != null) {
      this.color = color;
    } else {
      throw new IllegalArgumentException("Null color");
    }
  }

  @Override
  public int getY() {
    return this.y;
  }

  @Override
  public int getX() {
    return this.x;
  }

  @Override
  public int getHeight() {
    return this.height;
  }

  @Override
  public int getWidth() {
    return this.width;
  }

  @Override
  public void setShape(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  @Override
  public abstract ShapeType getShapeType();
}
