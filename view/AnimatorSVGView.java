package cs3500.animator.view;

import cs3500.animator.model.IColoredShape;
import cs3500.animator.model.IROAnimatorModel;
import cs3500.animator.model.IROMotion;
import cs3500.animator.model.ShapeType;
import java.awt.Color;
import java.util.List;
import java.util.Scanner;

/**
 * Represents an animation in a SVG style text format.
 */
public class AnimatorSVGView extends AAnimatorView implements IAnimatorView {
  private boolean finished = false;

  /**
   * Constructs an animator that is SVG-friendly.
   *
   * @param ap {@code Appendable} to represent output
   * @throws IllegalArgumentException if the given parameter is null
   */
  public AnimatorSVGView(Appendable ap) {
    super(ap);
  }

  /**
   * Renders the animation in a SVG style text format.
   *
   * @param model The model to grab shapes from
   * @param tick The tick at which the shapes should be grabbed from
   * @throws IllegalArgumentException if the model is null or the ticks are negative
   */
  @Override
  public void render(IROAnimatorModel model, int tick) throws IllegalArgumentException {
    if (!finished) {
      write(model, tick);
    }
  }

  // Writes down the shapes, canvas, and motions in SVG style
  private void write(IROAnimatorModel model, int tick) {
    // Header, default size of 1500 x 1500
    int[] bounds = model.getBounds();
    append("<svg width=\"" + bounds[2] + "\" height=\"" + bounds[3] + "\" version=\"1.1\"\n\t\t"
        + "xmlns=\"http://www.w3.org/2000/svg\">\n\n");
    List<IColoredShape> shapes = model.getShapesAt(tick);
    List<String> ids = model.getNames();
    int count = 0;
    for (IColoredShape shape : shapes) {
      String shapeType;
      switch (shape.getShapeType()) {
        case RECTANGLE:
          shapeType = "rect";
          break;
        case ELLIPSE:
          shapeType = "ellipse";
          break;
        default:
          throw new IllegalArgumentException(
              "Update the switch in render() to support new shape types");
      }
      append("<" + shapeType + " id=\"");
      String id = ids.get(count++);
      int width = shape.getWidth();
      int height = shape.getHeight();
      if (shapeType.equals("ellipse")) {
        // Updates to radius
        width /= 2;
        height /= 2;
      }
      Color color = shape.getColor();
      append(id + "\" x=\"" + shape.getX() + "\" y=\"" + shape.getY() + "\" width=\"" + width
          + "\" height=\""
          + height + "\" fill=\"rgb(" + color.getRed() + "," + color.getGreen() + ","
          + color.getBlue()
          + ")\" visibility=\"visible\" >\n");
      List<IROMotion> motions = model.getMotionsFor(id);
      for (IROMotion motion : motions) {
        List<String> states = motion.getStates();
        processStates(motion, states, shape);
      }
      append("\n</" + shapeType + ">\n\n");
    }
    append("</svg>");
    finished = true;
  }

  @Override
  public boolean finished() {
    return this.finished;
  }

  // Appends a motion description onto the appendable
  private void appendMotionState(int begin, int dur, String attribute, String from, String to) {
    append("\t<animate attributeType=\"xml\" begin=\"" + begin + "000ms\" dur=\""
        + dur + "000ms\" attributeName=\"" + attribute + "\" from=\"" + from + "\" to=\"" + to
        + "\" fill=\"freeze\" />\n");
  }

  // Adds motions to the SVG
  private void processStates(IROMotion motion, List<String> states, IColoredShape shape) {
    // Previous values
    int x = shape.getX();
    int y = shape.getY();
    int width = shape.getWidth();
    int height = shape.getHeight();
    Color color = shape.getColor();
    int red = color.getRed();
    int green = color.getGreen();
    int blue = color.getBlue();
    StringBuilder temp = new StringBuilder();
    temp.append("rgb(").append(red).append(",").append(green).append(",").append(blue).append(")");
    for (String state : states) {
      Scanner scanner = new Scanner(state);
      String command = scanner.next();
      switch (command) {
        case "move":
          String newX = scanner.next();
          String newY = scanner.next();
          String attributeX = "x";
          String attributeY = "y";
          if (shape.getShapeType().equals(ShapeType.ELLIPSE)) {
            attributeX = "cx";
            attributeY = "cy";
          }
          appendMotionState(motion.getStart(), motion.getEnd(), attributeX, Integer.toString(x),
              newX);
          appendMotionState(motion.getStart(), motion.getEnd(), attributeY, Integer.toString(y),
              newY);
          break;
        case "size":
          String newW = scanner.next();
          String newH = scanner.next();
          String attributeW = "width";
          String attributeH = "height";
          if (shape.getShapeType().equals(ShapeType.ELLIPSE)) {
            attributeW = "rx";
            attributeH = "ry";
          }
          appendMotionState(motion.getStart(), motion.getEnd(), attributeW, Integer.toString(width),
              newW);
          appendMotionState(motion.getStart(), motion.getEnd(), attributeH,
              Integer.toString(height),
              newH);
          break;
        case "color":
          int r = scanner.nextInt();
          int g = scanner.nextInt();
          int b = scanner.nextInt();
          StringBuilder newRGB = new StringBuilder();
          newRGB.append("rgb(").append(r).append(",").append(g).append(",").append(b).append(")");
          appendMotionState(motion.getStart(), motion.getEnd(), "fill", temp.toString(),
              newRGB.toString());
          break;
        default:
          // Do nothing
      }
    }
  }
}
