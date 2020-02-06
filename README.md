# Excellence Animator

_Project done in CS3500, Object-Oriented Design_

Purpose of this project is to create an animator able to read animations consisting of
rectangles and ellipses from files and play them.

_Java Swing used for rendering._

For designs used:
  * Implemented a decoupled model, controller, and view for future
  updates and extendability.
  * Used abstractions and inheritance to prevent code duplication
  and extend functionality.
  * Factory pattern was used to create different views depending on the
  type of file read.
  * Thoroughly tested using mock controllers, models, and views.
  * Read-only versions of classes vital to the model were passed to the controller
  and view to prevent mutation.

**Functionality:**
  * Create new shapes to add to animation.
  * Read animations from text or SVG files.
  * Save animations created or edited to text or SVG files.
  * Edit keyframes of the animation to change behavior of certain shapes.
  * Control looping, current point of the animation, and speed of playback.
  * Allows for deletion of shapes and keyframes.
 
 Screenshot of an animation possible:
 ![Example of skyscrapers](resources/animator_screenshot1.png)
 
 