import com.jme3.app.SimpleApplication
import com.jme3.system.AppSettings
import com.typesafe.scalalogging.LazyLogging

object MonkeyClient extends SimpleApplication
  with LazyLogging
  with App {

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
    this.flyCam.setEnabled(false)
    val keyboardInputAppState = new KeyboardInputAppState(inputManager)
    this.stateManager.attach(keyboardInputAppState)
  }

}