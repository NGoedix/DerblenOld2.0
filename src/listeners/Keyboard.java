package listeners;

import main.Pantalla;
import util.Arduino;
import util.ObjectReader;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_ESCAPE) System.exit(0);
        if (key == KeyEvent.VK_O && !e.isAltDown() && !e.isShiftDown() && e.isControlDown())  ObjectReader.importFile();
        if (key == KeyEvent.VK_R && !e.isAltDown() && !e.isShiftDown() && e.isControlDown()) Pantalla.getCamera().reset();
        if (key == KeyEvent.VK_A && !Arduino.isConnected()) Arduino.connectArduino();
        if (key == KeyEvent.VK_G && !Pantalla.isMoving()) Pantalla.setIsMoving(true);
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
