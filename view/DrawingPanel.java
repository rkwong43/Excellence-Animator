package cs3500.animator.view;

import cs3500.animator.model.IColoredShape;
import cs3500.animator.model.ShapeType;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import javax.swing.JPanel;

/**
 * Represents a drawing panel that draws shapes.
 */
public class DrawingPanel extends JPanel implements IDrawingPanel {

  private List<IColoredShape> shapes;

  /**
   * Constructs a default drawing panel.
   */
  public DrawingPanel() {
    super();
    setBackground(Color.white);
    setVisible(true);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D graphics2D = (Graphics2D) g;
    // Only draws the shapes if they are not null
    if (shapes != null) {
      for (IColoredShape shape : shapes) {
        if (shape != null) {
          graphics2D.setPaint(shape.getColor());
          ShapeType type = shape.getShapeType();
          switch (type) {
            case ELLIPSE:
              graphics2D.fillOval(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
              break;
            case RECTANGLE:
              graphics2D.fillRect(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
              break;
            default:
              throw new IllegalStateException("Invalid shape type");
          }
        }
      }
    }
  }

  @Override
  public void draw(List<IColoredShape> shapes) {
    this.shapes = shapes;
    repaint();
  }
}
