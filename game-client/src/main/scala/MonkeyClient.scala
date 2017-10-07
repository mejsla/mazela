import java.util.concurrent.{ThreadFactory, TimeUnit, TimeoutException}

import com.jme3.app.SimpleApplication
import com.jme3.system.AppSettings
import com.typesafe.scalalogging.LazyLogging
import se.mejsla.camp.mazela.client.jme.{GameboardAppstate, ProtobufAppState}
import se.mejsla.camp.mazela.network.client.grizzly.GrizzlyNetworkClient

object MonkeyClient extends SimpleApplication
  with LazyLogging
  with App {

  val networkClient = new GrizzlyNetworkClient(100, threadFactory)
  val gameboardAppstate = new GameboardAppstate
  val networkAppstate = new ProtobufAppState(networkClient, gameboardAppstate)

  val setting = new AppSettings(true);
  setting.setRenderer(AppSettings.JOGL_OPENGL_BACKWARD_COMPATIBLE);
  setting.setAudioRenderer(AppSettings.JOAL);
  setting.setTitle("Mazela");
  setting.setWidth(1024);
  setting.setHeight(768);
  setSettings(setting);
  setShowSettings(false);
  start();

  override def simpleInitApp(): Unit = {
    println("hello")

    networkClient.startAsync
    val keyboardInputAppState = new KeyboardInputAppState(inputManager, networkClient)

    this.flyCam.setEnabled(false)
    stateManager.attach(gameboardAppstate)
    stateManager.attach(keyboardInputAppState)

    try {
      networkClient.awaitRunning(5, TimeUnit.SECONDS)
      stateManager.attach(networkAppstate)
      stateManager.attach(keyboardInputAppState)
    } catch {
      case ex: TimeoutException =>
        logger.error("Network client did not start in time", ex)
        beginShutdown()
    }

  }

  private def threadFactory = new ThreadFactory() {
    private var threadNumber = 0
    final  private val threadGroup = new ThreadGroup("GameClientGroup")
    override def newThread(r: Runnable): Thread = {
      val threadName = "MonkeyClient-" + {
        threadNumber += 1; threadNumber - 1
      }
      logger.debug("Allocating new thread: " + threadName)
      val t = new Thread(threadGroup, r, threadName)
      t.setDaemon(true)
      t
    }
  }

  private def beginShutdown(): Unit = {
    networkClient.stopAsync
    if (networkAppstate != null) this.stateManager.detach(networkAppstate)
    if (gameboardAppstate != null) this.stateManager.detach(gameboardAppstate)
    this.stop()
  }

}