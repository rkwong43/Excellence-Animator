package cs3500.animator.model;

import java.util.List;

/**
 * Represents a read-only {@code IAnimatorModel}. Only has the ability to return the state of shapes
 * at a particular tick.
 */
public class ROAnimatorModelImpl implements IROAnimatorModel {

  private final IAnimatorModel model;

  /**
   * Constructs a {@code ROAnimatorModelImpl} using the given model.
   *
   * @param model The model to represent
   * @throws IllegalArgumentException if the model is null
   */
  public ROAnimatorModelImpl(IAnimatorModel model) {
    this.model = model;
  }

  @Override
  public List<IColoredShape> getShapesAt(int tick) throws IllegalArgumentException {
    if (tick < 0) {
      throw new IllegalArgumentException("Invalid tick, less than zero");
    }
    return this.model.getShapesAtTick(tick);
  }

  @Override
  public String getDescription() {
    return this.model.outputDescription();
  }

  @Override
  public List<IROMotion> getMotionsFor(String id) throws IllegalArgumentException {
    return this.model.getMotionsForShape(id);
  }

  @Override
  public int[] getBounds() throws IllegalStateException {
    return model.getBounds();
  }

  @Override
  public int getFinalTick() {
    return model.getFinalTick();
  }

  @Override
  public List<String> getNames() {
    return model.getNames();
  }

  @Override
  public IROAnimatorModel loop() {
    model.resetShapes();
    return this;
  }

  @Override
  public IColoredShape getShape(String id) {
    return model.getShape(id);
  }
}
