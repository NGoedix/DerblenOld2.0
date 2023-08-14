package listeners;

import main.Pantalla;
import objects.ComplexObject;
import util.Calc;

import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class Mouse extends MouseAdapter implements MouseWheelListener, MouseInputListener {

    private int x;
    private int y;

    private int tempX;
    private int tempY;

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            Pantalla.setSelecting(new int[] {e.getX(), e.getY(), 0, 0});
        }
        if (e.getButton() == MouseEvent.BUTTON3 && Pantalla.isMoving()) {
            if (Pantalla.getSelectedObjects().length != 0) {
                for (ComplexObject c : Pantalla.getSelectedObjects()) {
                    c.moveX(Pantalla.getCordsMoved()[0]);
                    c.moveY(Pantalla.getCordsMoved()[1]);
                }
            }
            Pantalla.getInstance().setCursor(Cursor.getDefaultCursor());
        }
        Pantalla.setIsMoving(false);
        Pantalla.getInstance().render();
        Pantalla.getInstance().render();
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {
        Pantalla.getInstance().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        Pantalla.setSelecting(null);
        x = e.getX();
        y = e.getY();
        Pantalla.getInstance().render();
        Pantalla.getInstance().render();
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {
        switch (e.getModifiersEx()) {
            case InputEvent.BUTTON2_DOWN_MASK: // Middle button / Rotation of the camera
                double rY = Pantalla.getCamera().getrY();
                double rX = Pantalla.getCamera().getrX();

                Pantalla.getCamera().setrY(rY + (Pantalla.getCamera().getIsToRight() ? (float) (x - e.getX()) : -(float) (x - e.getX())));
                Pantalla.getCamera().setrX(rX + (float) (y - e.getY()));
                break;
            case (InputEvent.SHIFT_DOWN_MASK | InputEvent.BUTTON2_DOWN_MASK): // Shift + Middle click / Movement of the camera
                Pantalla.getCamera().moveX(x - e.getX());
                Pantalla.getCamera().moveY(y - e.getY());
                break;
            case InputEvent.BUTTON1_DOWN_MASK:
                Pantalla.getInstance().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                Pantalla.setSelecting(new int[] {x, y, e.getX() - x, e.getY() - y});
                break;
            case InputEvent.BUTTON3_DOWN_MASK:
                if (Pantalla.getSelectedObjects()[0] != null && Pantalla.isMoving()) {
                    for (ComplexObject c : Pantalla.getSelectedObjects()) {
                        c.moveX(Pantalla.getCordsMoved()[0]);
                        c.moveY(Pantalla.getCordsMoved()[1]);
                    }
                }
                Pantalla.setIsMoving(false);
                Pantalla.getInstance().setCursor(Cursor.getDefaultCursor());
                break;
        }

        // To the selecting square we must keep store the initial x and y
        if (e.getModifiersEx() != InputEvent.BUTTON1_DOWN_MASK) {
            x = e.getX();
            y = e.getY();
        }
        Pantalla.getInstance().render();

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (Pantalla.isMoving()) {
            // Create cursor
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Image image = toolkit.getImage("assets/cursor.png");
            Pantalla.getInstance().setCursor(toolkit.createCustomCursor(image, new Point(15, 15), "custom_moving"));
            // Store local cords
            Pantalla.setCordsMoved(new int[] { (x - e.getX()) * 25, (y - e.getY()) * 25 });
            // Move selected objects
            for (ComplexObject c : Pantalla.getSelectedObjects()) {
                c.moveX((e.getX() - tempX) * 25);
                c.moveY((e.getY() - tempY) * 25);
            }
            Pantalla.getInstance().render();
            Pantalla.getInstance().render();
            tempX = e.getX();
            tempY = e.getY();
        } else {
            x = e.getX();
            y = e.getY();
            tempX = e.getX();
            tempY = e.getY();
            double rX = Math.abs(Pantalla.getCamera().getrX());
            Pantalla.getCamera().setIsToRight(rX % 360 > 90 && rX % 360 < 270);
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        Pantalla.getCamera().render(e.getWheelRotation() * (int) (Calc.getDistanceOrigin() / 90) * 8 + 10);
        Pantalla.getInstance().render();
        Pantalla.getInstance().render();
    }
}
