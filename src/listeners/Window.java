package listeners;

import main.Pantalla;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Window implements WindowListener, ComponentListener {
    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {
        if (Pantalla.isRendering()) {
            Pantalla.getInstance().render();
            Pantalla.getInstance().render();
        }
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        if (Pantalla.isRendering()) {
            Pantalla.getInstance().render();
            Pantalla.getInstance().render();
        }
    }

    @Override
    public void windowActivated(WindowEvent e) {
        if (Pantalla.isRendering()) {
            Pantalla.getInstance().render();
            Pantalla.getInstance().render();
        }
    }

    @Override
    public void windowDeactivated(WindowEvent e) {}

    @Override
    public void componentResized(ComponentEvent e) {
        if (Pantalla.isRendering()) {
            Pantalla.getInstance().render();
            Pantalla.getInstance().render();
        }
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}
