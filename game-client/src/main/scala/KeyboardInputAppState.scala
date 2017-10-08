import java.nio.ByteBuffer
import java.util.concurrent.atomic.AtomicBoolean

import com.jme3.app.Application
import com.jme3.app.state.{AbstractAppState, AppStateManager}
import com.jme3.input.InputManager
import com.jme3.input.KeyInput
import com.jme3.input.controls.{AnalogListener, InputListener}
import com.jme3.input.controls.KeyTrigger
import com.typesafe.scalalogging.LazyLogging
import se.mejsla.camp.mazela.network.client.NetworkClient
import se.mejsla.camp.mazela.network.common.protos.mazela_protocol.{ClientInput, Envelope}
import se.mejsla.camp.mazela.network.common.protos.mazela_protocol.Envelope.MessageType
import se.mejsla.camp.mazela.network.common.{NotConnectedException, OutgoingQueueFullException}

class KeyboardInputAppState(val inputManager: InputManager,
                            val networkClient: NetworkClient)
  extends AbstractAppState
    with LazyLogging {

  var upp = new AtomicBoolean(false)
  var ner = new AtomicBoolean(false)
  var vänster = new AtomicBoolean(false)
  var höger = new AtomicBoolean(false)
  var needsUpdate = new AtomicBoolean(false)


  @Override
  override def initialize(stateManager: AppStateManager, app: Application): Unit = {
    println("scala inoutmanager initialized")
    inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A))
    inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D))
    inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W))
    inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S))
    inputManager.addMapping("Quit", new KeyTrigger(KeyInput.KEY_ESCAPE))
    inputManager.addMapping("Qui", new KeyTrigger(KeyInput.KEY_1))
    inputManager.addListener(keyboardListener, "Left", "Right", "Up", "Down", "Qui")

    super.initialize(stateManager, app)
  }

 // class KeyboardListener extends AnalogListener {
  val keyboardListener: AnalogListener = (name: String, value: Float, tpf: Float) => {
    logger.debug("Analog input: {}, {}, {}", name, value, tpf)
    name match {
      case "Left" =>
        vänster.set(true)
        needsUpdate.set(true)
      case "Right" =>
        höger.set(true)
        needsUpdate.set(true)
      case "Up" =>
        upp.set(true)
        needsUpdate.set(true)
      case "Down" =>
        ner.set(true)
        needsUpdate.set(true)
      case "Quit" =>
        println("lol")
      case _ => println("???")
    }
  }

  override def update(tpf: Float): Unit = {
    if (needsUpdate.get) {
      val message = ByteBuffer.wrap(
       Envelope(messageType = MessageType.ClientInput)
         .withClientInput(ClientInput(down=ner.get, up=upp.get,
           left=vänster.get, right=höger.get)
       ).toByteArray)
      try {
        networkClient.sendMessage(message)
        needsUpdate.set(false)
      } catch {
        case ex@(_: OutgoingQueueFullException | _: NotConnectedException) =>
          logger.error("Unable to send keyboard message", ex)
      }
    }
    super.update(tpf)
  }

}

