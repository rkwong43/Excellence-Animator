package cs3500.animator.view;

import cs3500.animator.model.IColoredShape;
import java.util.List;

/**
 * Represents a drawing panel that draws a list of shapes.
 */
public interface IDrawingPanel {

  /**
   * Draws and renders the given list of shapes.
   * @param shapes List of shapes to draw
   */
  void draw(List<IColoredShape> shapes);
}
