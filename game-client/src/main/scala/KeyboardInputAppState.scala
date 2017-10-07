import java.util.concurrent.atomic.AtomicBoolean

import com.jme3.app.Application
import com.jme3.app.state.{AbstractAppState, AppStateManager}
import com.jme3.input.InputManager
import com.jme3.input.KeyInput
import com.jme3.input.controls.AnalogListener
import com.jme3.input.controls.KeyTrigger
import com.typesafe.scalalogging.LazyLogging

class KeyboardInputAppState(val inputManager: InputManager)
  extends AbstractAppState
    with LazyLogging {

  val keyboardListener = new KeyboardListener()

  var up = new AtomicBoolean(false)
  var down = new AtomicBoolean(false)
  var left = new AtomicBoolean(false)
  var right = new AtomicBoolean(false)
  var needsUpdate = new AtomicBoolean(false)


  @Override
  override def initialize(stateManager: AppStateManager, app: Application): Unit = {
    inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A))
    inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D))
    inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W))
    inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S))
    inputManager.addMapping("Quit", new KeyTrigger(KeyInput.KEY_ESCAPE))
    inputManager.addMapping("Qui", new KeyTrigger(KeyInput.KEY_1))
    inputManager.addListener(keyboardListener, "Left", "Right", "Up", "Down", "Qui")

    super.initialize(stateManager, app)
  }

  class KeyboardListener extends AnalogListener {
    override def onAnalog(name: String, value: Float, tpf: Float): Unit = {
      logger.debug("Analog input: {}, {}, {}", name, value, tpf)
      name match {
        case "Left" =>
          left.set(true)
          needsUpdate.set(true)
        case "Right" =>
          right.set(true)
          needsUpdate.set(true)
        case "Up" =>
          up.set(true)
          needsUpdate.set(true)
        case "Down" =>
          down.set(true)
          needsUpdate.set(true)
        case "Quit" =>
          println("lol")
        case _ => println("???")
      }
    }
  }

}

