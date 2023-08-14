package listeners;

import main.Pantalla;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class WindowComponent implements ComponentListener {
    @Override
    public void componentResized(ComponentEvent e) {
        if (Pantalla.isRendering()) {
            Pantalla.getInstance().render();
            Pantalla.getInstance().render();
        }
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        if (Pantalla.isRendering()) {
            Pantalla.getInstance().render();
            Pantalla.getInstance().render();
        }
    }

    @Override
    public void componentShown(ComponentEvent e) {
        if (Pantalla.isRendering()) {
            Pantalla.getInstance().render();
            Pantalla.getInstance().render();
        }
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        if (Pantalla.isRendering()) {
            Pantalla.getInstance().render();
            Pantalla.getInstance().render();
        }
    }
}
