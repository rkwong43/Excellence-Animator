package cs3500.animator.model;

import cs3500.animator.util.AnimationBuilder;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Represents an animator that keeps track of shapes with their IDs, motions, colors. Can also
 * output a textual description of all shapes and their corresponding motions.
 */
public final class IAnimatorModelImpl implements IAnimatorModel {

  private Map<String, IColoredShape> shapes; // Shapes and their IDs
  private Map<String, IColoredShape> originalShapes; // Shapes and their IDs when first added
  private Map<String, List<IMotion>> motions; // Motions and their corresponding Shape IDs
  private Map<String, int[]> prevSize; // Previous sizes of shapes before they change
  private Map<String, Color> prevColor; // Previous colors of shapes before they change
  private Map<String, int[]> prevPos; // Previous positions of shapes before they change
  private List<String> ids; // List of all IDs
  private int[] bounds; // Bounding box for the canvas
  private int finalTick = 0;


  /**
   * Constructs an animator with no shapes.
   */
  public IAnimatorModelImpl() {
    prevSize = new HashMap<>();
    prevColor = new HashMap<>();
    prevPos = new HashMap<>();
    shapes = new HashMap<>();
    originalShapes = new HashMap<>();
    motions = new HashMap<>();
    ids = new ArrayList<>();
  }

  @Override
  public void setBounds(int[] bounds) throws IllegalArgumentException {
    if (bounds == null || bounds.length < 4) {
      throw new IllegalArgumentException("Illegal of bounds given");
    }

    this.bounds = bounds;
  }

  @Override
  public int[] getBounds() throws IllegalStateException {
    if (bounds == null) {
      throw new IllegalStateException("Bounds not declared");
    }
    return new int[]{bounds[0], bounds[1], bounds[2], bounds[3]};
  }

  @Override
  public void addShape(ShapeType shape, String id, int x, int y, int width, int height, int r,
      int g, int b)
      throws IllegalArgumentException {
    // Checking if the shape or ID is null
    if (shape == null || id == null) {
      throw new IllegalArgumentException("Null parameters");
    }
    // Checking if the width and height are less than zero
    if (width < 0 || height < 0) {
      throw new IllegalArgumentException("Width or height less than zero");
    }
    // Checking if the shape already exists
    if (ids.contains(id)) {
      throw new IllegalArgumentException("Shape already added: " + id);
    }
    // Checking if the color is valid
    checkColors(r, g, b);
    IColoredShape shapeToBeAdded;
    IColoredShape original;
    Color color = new Color(r, g, b);
    switch (shape) {
      case RECTANGLE:
        shapeToBeAdded = new ColoredRectangle(x, y, width, height, color);
        original = new ColoredRectangle(x, y, width, height, color);
        break;
      case ELLIPSE:
        shapeToBeAdded = new ColoredEllipse(x, y, width, height, color);
        original = new ColoredEllipse(x, y, width, height, color);
        break;
      default:
        throw new IllegalArgumentException("Shape not supported");
    }
    prevPos.put(id, new int[]{x, y});
    prevSize.put(id, new int[]{width, height});
    prevColor.put(id, color);
    shapes.put(id, shapeToBeAdded);
    motions.put(id, new ArrayList<>());
    originalShapes.put(id, original);
    ids.add(id);
  }

  /**
   * Checks if the given color RGB values are within the range [0, 255].
   *
   * @param r Red value
   * @param g Green value
   * @param b Blue value
   * @throws IllegalArgumentException if the colors are not in the range [0, 255]
   */
  private void checkColors(int r, int g, int b) throws IllegalArgumentException {
    if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
      throw new IllegalArgumentException("Invalid color values");
    }
  }

  @Override
  public void addMotion(IMotion motion, String id) throws IllegalArgumentException {
    // If either ID or motion is null
    if (motion == null || id == null) {
      throw new IllegalArgumentException("Parameters null");
    }
    // Checks if the ID exists
    checkID(id);
    addMotionToMap(motion, id, this.motions);
    int end = motion.getEnd();
    if (end > finalTick) {
      finalTick = end;
    }
  }

  /**
   * Adds the motion to the {@code Map<String, List<IMotion>>}. If there is not a list initialized,
   * creates one. Otherwise, it adds the {@code IMotion} to the list.
   *
   * @param motion Motion to be added
   * @param id The ID to add motion to
   * @param motionList The map to add the motion to
   * @throws IllegalArgumentException if the given parameters are null
   */
  private void addMotionToMap(IMotion motion, String id, Map<String, List<IMotion>> motionList)
      throws IllegalArgumentException {
    if (motion == null || id == null || motionList == null) {
      throw new IllegalArgumentException("Null parameters");
    }
    // Adds the motion to the list
    motionList.get(id).add(motion);
    // Sorts the motions based on start time
    motionList.get(id).sort(new MotionStartTimeComparator());
  }

  @Override
  public String outputDescription() {
    StringBuilder result = new StringBuilder();
    for (String id : ids) {
      IColoredShape shape = originalShapes.get(id);
      // Adds the shape name declaration
      result.append("shape ").append(id).append(" ")
          .append(shape.getShapeType().toString().toLowerCase()).append("\n");
      // Creating a record of a previous state
      Color color = shape.getColor();
      Map<String, Integer> previousValues = new HashMap<>();
      previousValues.put("width", shape.getWidth());
      previousValues.put("height", shape.getHeight());
      previousValues.put("x", shape.getX());
      previousValues.put("y", shape.getY());
      previousValues.put("r", color.getRed());
      previousValues.put("g", color.getGreen());
      previousValues.put("b", color.getBlue());
      // Adding the description of each motion:
      if (motions.get(id) != null) {
        for (IMotion motion : motions.get(id)) {
          List<String> states = motion.getStates();
          // Adding the header "motion id start"
          result.append("motion ").append(id).append(" ").append(motion.getStart()).append(" ");
          // Appending the previous state
          // Adds "x y width height r g b   end"
          String previous = descriptionBuilder(previousValues);
          result.append(previous).append("\t\t").append(motion.getEnd()).append(" ");
          // Computing the states
          for (String state : states) {
            Scanner scan = new Scanner(state);
            String command = scan.next();
            // Updates the values depending on the state
            switch (command) {
              case "move":
                int newX = scan.nextInt();
                int newY = scan.nextInt();
                previousValues.replace("x", newX);
                previousValues.replace("y", newY);
                break;
              case "size":
                int newWidth = scan.nextInt();
                int newHeight = scan.nextInt();
                previousValues.replace("width", newWidth);
                previousValues.replace("height", newHeight);
                break;
              case "color":
                int r = scan.nextInt();
                int g = scan.nextInt();
                int b = scan.nextInt();
                previousValues.replace("r", r);
                previousValues.replace("g", g);
                previousValues.replace("b", b);
                break;
              default:
            }
          }
          // Appends an updated description with the updated values
          result.append(descriptionBuilder(previousValues)).append("\n");
        }
      }
    }
    return result.toString();
  }

  /**
   * Creates a {@code StringBuilder} that will take values from a map to create a {@code String} of
   * values to be used in {@code outputDescription()}.
   *
   * @param previousValues Map of values to be used
   * @return String of values
   */
  private String descriptionBuilder(Map<String, Integer> previousValues) {
    if (previousValues == null) {
      throw new IllegalArgumentException("Null parameters");
    }
    StringBuilder prev = new StringBuilder();
    prev.append(previousValues.get("x")).append(" ").append(previousValues.get("y"))
        .append(" ")
        .append(previousValues.get("width")).append(" ").append(previousValues.get("height"))
        .append(" ").append(previousValues.get("r")).append(" ").append(previousValues.get("g"))
        .append(" ")
        .append(previousValues.get("b"));
    return prev.toString();
  }

  // Moves all the shapes to the specified tick
  private void move(int end) {
    // If the end is 0, do nothing
    if (end < 0) {
      throw new IllegalArgumentException("Invalid tick, less than zero");
    } else if (end == 0) {
      return;
    }
    // Changes shapes of the ones in progress of moving or changing state
    for (String id : ids) {
      List<IMotion> motionList = motions.get(id);
      // No more motions, shape disappears, or if there are no motions at all (only initialized)
      if (motionList.isEmpty() || end > motionList.get(motionList.size() - 1).getEnd()) {
        IColoredShape shape = shapes.get(id);
        // If the tick is after the last motion, makes it disappear and stores its previous
        // size
        prevSize.put(id, new int[]{shape.getWidth(), shape.getHeight()});
        shape.setShape(shape.getX(), shape.getY(), -1, -1);
      } else {
        for (IMotion motion : motionList) {
          int startTick = motion.getStart();
          int endTick = motion.getEnd();
          // Only runs motions that are in progress
          if (end >= startTick && end <= endTick) {
            processStateChange(motion, id, startTick, endTick, end);
          }
        }
      }
    }
  }

  @Override
  public void resetShapes() {
    for (String id : ids) {
      IColoredShape s = originalShapes.get(id);
      ShapeType type = s.getShapeType();
      switch (type) {
        case ELLIPSE:
          shapes.replace(id,
              new ColoredEllipse(s.getX(), s.getY(), s.getWidth(), s.getHeight(), s.getColor()));
          break;
        case RECTANGLE:
          shapes.replace(id,
              new ColoredRectangle(s.getX(), s.getY(), s.getWidth(), s.getHeight(),
                  s.getColor()));
          break;
        default:
          throw new UnsupportedOperationException("Shape not supported: " + type.toString());
      }
    }
  }

  // Uses linear interpolation to process the motion of the given shape
  private void processStateChange(IMotion motion, String id, int startTick, int endTick, int end) {
    List<String> states = motion.getStates();
    IColoredShape shape = shapes.get(id);
    // Deleted first keyframe, shape is invisible
    if (shape.getWidth() == -1 || shape.getHeight() == -1) {
      int[] prev = prevSize.get(id);
      shape.setShape(shape.getX(), shape.getY(), prev[0], prev[1]);
    }
    for (String state : states) {
      Scanner scan = new Scanner(state);
      String command = scan.next();
      switch (command) {
        case "move":
          int newX = scan.nextInt();
          int newY = scan.nextInt();
          int[] prev = prevPos.get(id);
          newY = linearInterpolation(prev[1], newY, startTick, endTick, end);
          newX = linearInterpolation(prev[0], newX, startTick, endTick, end);
          prevPos.put(id, new int[]{newX, newY});
          shape.setShape(newX, newY, shape.getWidth(), shape.getHeight());
          break;
        case "size":
          int newW = scan.nextInt();
          int newH = scan.nextInt();
          int[] prevS = prevSize.get(id);
          newW = linearInterpolation(prevS[0], newW, startTick, endTick, end);
          newH = linearInterpolation(prevS[1], newH, startTick, endTick, end);
          prevSize.put(id, new int[]{newW, newH});
          shape.setShape(shape.getX(), shape.getY(), newW, newH);
          break;
        case "color":
          int newR = scan.nextInt();
          int newG = scan.nextInt();
          int newB = scan.nextInt();
          Color oldColor = prevColor.get(id);
          newR = linearInterpolation(oldColor.getRed(), newR, startTick, endTick, end);
          newG = linearInterpolation(oldColor.getGreen(), newG, startTick, endTick, end);
          newB = linearInterpolation(oldColor.getBlue(), newB, startTick, endTick, end);
          Color newColor = new Color(newR, newG, newB);
          prevColor.put(id, newColor);
          shape.setColor(newColor);
          break;
        default:
          throw new UnsupportedOperationException("Unsupported state change: " + command);
      }
    }
  }

  // Linear interpolation to calculate a value
  private int linearInterpolation(double initialVal, double finalVal, int start, int end,
      int tick) {
    double duration = end - start;
    if (initialVal == finalVal) {
      return (int) initialVal;
    }
    return (int) ((initialVal * (end - tick) / duration) + (finalVal * (tick - start)
        / duration));
  }

  @Override
  public void removeShape(String id) throws IllegalArgumentException {
    // Will check if the ID exists and removes the motion, if any
    removeMotions(id);
    shapes.remove(id);
    ids.remove(id);
  }

  // Removes motions
  private void removeMotions(String id) throws IllegalArgumentException {
    checkID(id);
    motions.get(id).clear();
  }

  @Override
  public List<IColoredShape> getShapesAtTick(int tick) throws IllegalArgumentException {
    if (tick < 0) {
      throw new IllegalArgumentException("Negative tick");
    }
    List<IColoredShape> result = new ArrayList<>();
    move(tick);
    // Creates a deep copy of each shape, to prevent mutation when passed outside
    for (String id : ids) {
      IColoredShape temp = shapes.get(id);
      IColoredShape shapeToBeAdded = shapeCopy(temp);
      result.add(shapeToBeAdded);
    }
    return result;
  }

  // Provides a deep copy of the given shape
  private IColoredShape shapeCopy(IColoredShape shape) {
    IColoredShape result;
    switch (shape.getShapeType()) {
      case ELLIPSE:
        result = new ColoredEllipse(shape.getX(), shape.getY(), shape.getWidth(),
            shape.getHeight(), shape.getColor());
        break;
      case RECTANGLE:
        result = new ColoredRectangle(shape.getX(), shape.getY(), shape.getWidth(),
            shape.getHeight(), shape.getColor());
        break;
      default:
        // Throw state exception, enum restricts usage of other shapes
        throw new IllegalStateException("Invalid shape");
    }
    return result;
  }

  /**
   * Checks if the ID has a shape corresponding to it.
   *
   * @throws IllegalArgumentException if the shape does not exist
   */
  private void checkID(String id) {
    if (!shapes.containsKey(id)) {
      // Shape doesn't exist
      throw new IllegalArgumentException("Shape ID does not correspond to a shape.");
    }
  }

  @Override
  public List<IROMotion> getMotionsForShape(String id) throws IllegalArgumentException {
    if (id == null) {
      throw new IllegalArgumentException("Null ID given");
    } else if (!ids.contains(id)) {
      throw new IllegalArgumentException("ID does not correspond to a shape");
    }
    List<IROMotion> result = new ArrayList<>();
    // Creates read-only motions
    for (IMotion motion : motions.get(id)) {
      result.add(new ROMotionImpl(motion));
    }
    return result;
  }

  @Override
  public int getFinalTick() {
    return this.finalTick;
  }

  @Override
  public List<String> getNames() {
    return new ArrayList<>(ids);
  }

  @Override
  public IColoredShape getShape(String id) {
    checkID(id);
    return shapeCopy(shapes.get(id));
  }

  @Override
  public void addKeyframe(String id, String state, int tick, int... params) {
    checkID(id);
    List<IMotion> motionList = motions.get(id);
    if (state == null) {
      throw new IllegalArgumentException("Null state given");
    }

    // Empty motions, basically adding first keyframe for the shape
    if (motionList.isEmpty()) {
      IMotion motion = new ShapeMotion(tick, tick);
      motionList.add(motion);
      addStates(id, state, motion, params);
      return;
    }

    int lastIndex = motionList.size() - 1;
    IMotion first = motionList.get(0);
    // Adding keyframe before first motion
    if (tick < first.getStart()) {
      IMotion motion = new ShapeMotion(tick, first.getStart());
      addStates(id, state, motion, params);
      if (first.getStart() == first.getEnd()) {
        motionList.set(0, motion);
      } else {
        addMotion(motion, id);
      }
      return;
    }

    // Last motion
    if (tick > motionList.get(lastIndex).getEnd()) {
      IMotion motion = new ShapeMotion(motionList.get(lastIndex).getEnd(), tick);
      addStates(id, state, motion, params);
      if (first.getStart() == first.getEnd()) {
        motionList.set(0, motion);
      } else {
        addMotion(motion, id);
      }
      return;
    }

    boolean add = false;
    // Finds the spot to put a motion into
    for (int k = 1; k < motionList.size() - 1; k++) {
      // Adding another state change to an existing motion
      if (tick == motionList.get(k - 1).getEnd() || tick == motionList.get(k + 1).getStart()) {
        addStates(id, state, motionList.get(k - 1), params);
        break;
      } else if (tick > motionList.get(k - 1).getEnd() && tick < motionList.get(k + 1).getStart()) {
        add = true;
        break;
      }
    }
    if (add || motionList.size() == 2) {
      // In between 2 motions
      // Adds the new motion, attaches it to the next motion
      IMotion motion = new ShapeMotion(motionList.get(0).getEnd(), tick);
      addMotion(motion, id);
      addStates(id, state, motion, params);
      motionList.get(1).setStart(tick);
    }
  }

  // Adds states to the given motion
  private void addStates(String id, String state, IMotion motion, int... params) {
    try {
      switch (state) {
        case "color":
          motion.addState(state + " " + params[0] + " " + params[1] + " " + params[2]);
          prevColor.put(id, new Color(params[0], params[1], params[2]));
          break;
        case "size":
          motion.addState(state + " " + params[0] + " " + params[1]);
          prevSize.put(id, params);
          break;
        case "move":
          motion.addState(state + " " + params[0] + " " + params[1]);
          prevPos.put(id, params);
          break;
        default:
          throw new IllegalArgumentException("Invalid state: " + state);
      }
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalArgumentException("Invalid number of parameters given.");
    }
  }

  @Override
  public void removeKeyframe(String id, int tick) {
    checkID(id);
    if (tick < 0) {
      throw new IllegalArgumentException("Tick less than zero.");
    }
    List<IMotion> motionList = motions.get(id);
    IColoredShape shape = shapes.get(id);
    // No motions to remove or edit
    if (motionList.isEmpty()) {
      return;
    }
    // Tick is when the shape is initialized
    if (tick == motionList.get(0).getStart()) {
      motionList.remove(0);
      // Sets the shape invisible
      prevSize.put(id, new int[]{shape.getWidth(), shape.getHeight()});
      shape.setShape(shape.getX(), shape.getY(), -1, -1);
    } else if (tick == motionList.get(motionList.size() - 1).getEnd()) {
      // Tick is the last motion
      motionList.remove(motionList.size() - 1);
    } else {
      getMotion(tick, motionList);
    }
  }

  // Finds the associated motion with the tick
  private void getMotion(int tick, List<IMotion> motionList) {
    // Motions are organized by start times
    for (int k = 0; k < motionList.size(); k++) {
      IMotion m = motionList.get(k);
      // Non-consecutive motions
      try {
        IMotion prev = motionList.get(k - 1);
        if (tick == m.getStart() && (m.getStart() - prev.getEnd() > 1)) {
          // Chains them up
          prev.setEnd(m.getStart());
        }
      } catch (IndexOutOfBoundsException e) {
        // Do nothing, it's the first motion
        continue;
      }

      // Motion to remove
      if (m.getEnd() == tick) {
        // If this is the first motion
        if (k == 0) {
          try {
            // Sets the next motion's beginning to the point when the shape is initialized
            motionList.get(k + 1).setStart(m.getStart());
          } catch (IndexOutOfBoundsException e) {
            // Do nothing, this is the only motion so it's fine to just remove it
          }
          motionList.remove(m);
          return;
        }
        // Chains up the motions before and after
        try {
          IMotion next = motionList.get(k + 1);
          next.setStart(m.getStart());
        } catch (IndexOutOfBoundsException e) {
          // No motions exist afterwards, so it's fine to just remove the motion
        }
        motionList.remove(m);
        return;
      }
    }
  }

  //********************************************************************************************

  /**
   * Represents a builder that builds an instance of {@code IAnimatorModelImpl}.
   */
  public static final class Builder implements AnimationBuilder<IAnimatorModel> {

    Map<String, String> shapeDeclarations; // Shape declarations, not instantiated yet
    IAnimatorModel model; // Model to build

    /**
     * Constructs a {@code Builder} for an {@code IAnimatorModel}.
     */
    public Builder() {
      shapeDeclarations = new HashMap<>();
      model = new IAnimatorModelImpl();
    }

    /**
     * Constructs a final document.
     *
     * @return the newly constructed document
     */
    @Override
    public IAnimatorModel build() {
      return this.model;
    }

    /**
     * Specify the bounding box to be used for the animation.
     *
     * @param x The leftmost x value
     * @param y The topmost y value
     * @param width The width of the bounding box
     * @param height The height of the bounding box
     * @return This {@link AnimationBuilder}
     */
    @Override
    public AnimationBuilder<IAnimatorModel> setBounds(int x, int y, int width, int height) {
      model.setBounds(new int[]{x, y, width, height});
      return this;
    }

    /**
     * Adds a new shape to the growing document.
     *
     * @param name The unique name of the shape to be added. No shape with this name should already
     *              exist.
     * @param type The type of shape (e.g. "ellipse", "rectangle") to be added. The set of supported
     *            shapes is unspecified, but should include "ellipse" and "rectangle" as a minimum.
     * @return This {@link AnimationBuilder}
     */
    @Override
    public AnimationBuilder<IAnimatorModel> declareShape(String name, String type) {
      if (name != null && type != null && !shapeDeclarations.containsKey(name)) {
        shapeDeclarations.put(name, type);
      }
      return this;
    }

    /**
     * Adds a transformation to the growing document.
     *
     * @param name The name of the shape (added with {@link AnimationBuilder#declareShape})
     * @param t1 The start time of this transformation
     * @param x1 The initial x-position of the shape
     * @param y1 The initial y-position of the shape
     * @param w1 The initial width of the shape
     * @param h1 The initial height of the shape
     * @param r1 The initial red color-value of the shape
     * @param g1 The initial green color-value of the shape
     * @param b1 The initial blue color-value of the shape
     * @param t2 The end time of this transformation
     * @param x2 The final x-position of the shape
     * @param y2 The final y-position of the shape
     * @param w2 The final width of the shape
     * @param h2 The final height of the shape
     * @param r2 The final red color-value of the shape
     * @param g2 The final green color-value of the shape
     * @param b2 The final blue color-value of the shape
     * @return This {@link AnimationBuilder}
     */
    @Override
    public AnimationBuilder<IAnimatorModel> addMotion(String name, int t1, int x1, int y1, int w1,
        int h1, int r1, int g1, int b1, int t2, int x2, int y2, int w2, int h2, int r2, int g2,
        int b2) {
      ShapeType type;
      try {
        switch (shapeDeclarations.get(name)) {
          case "ellipse":
            type = ShapeType.ELLIPSE;
            break;
          case "rectangle":
            type = ShapeType.RECTANGLE;
            break;
          default:
            throw new IllegalStateException(
                "Update switch in Builder to accept unsupported shapes");
        }
      } catch (NullPointerException e) {
        throw new IllegalStateException("No such shape declared");
      }

      try {
        model.addShape(type, name, x1, y1, w1, h1, r1, g1, b1);
      } catch (IllegalArgumentException e) {
        // Do nothing, shape has already been added
      }
      IMotion motion = new ShapeMotion(t1, t2);
      // Change in x and y
      if ((x1 != x2) || (y1 != y2)) {
        motion.addState("move " + x2 + " " + y2);
      }

      // Change in width and height
      if ((w1 != w2) || (h1 != h2)) {
        motion.addState("size " + w2 + " " + h2);
      }

      // Change in color
      if ((r1 != r2) || (g1 != g2) || (b1 != b2)) {
        motion.addState("color " + r2 + " " + g2 + " " + b2);
      }
      model.addMotion(motion, name);
      return this;
    }

    /**
     * Adds an individual keyframe to the growing document.
     *
     * @param name The name of the shape (added with {@link AnimationBuilder#declareShape})
     * @param t The time for this keyframe
     * @param x The x-position of the shape
     * @param y The y-position of the shape
     * @param w The width of the shape
     * @param h The height of the shape
     * @param r The red color-value of the shape
     * @param g The green color-value of the shape
     * @param b The blue color-value of the shape
     * @return This {@link AnimationBuilder}
     */
    @Override
    public AnimationBuilder<IAnimatorModel> addKeyframe(String name, int t, int x, int y, int w,
        int h, int r, int g, int b) {
      // Returns this because keyframes are not used in our model implementation
      return this;
    }
  }
}
