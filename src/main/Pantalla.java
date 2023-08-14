package main;

import comparer.CompareDistance;
import controller.Camera;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Arrays;
import listeners.Keyboard;
import listeners.Mouse;
import listeners.WindowComponent;
import objects.ComplexObject;
import objects.Cube;
import objects.Triangle;
import objects.Vertice;
import util.Calc;

public class Pantalla extends Canvas {


    private static Pantalla pantalla;

//    private static Triangle[] triangles = new Triangle[0];
    private static ArrayList<Triangle> triangles = new ArrayList<>();
    private static ComplexObject[] objects = new ComplexObject[0];
    private static Camera[] cameras = new Camera[0];

    private static int[] selectingProperties;

    private static boolean isMoving = false;
    private static int[] cordsMoved;

    private static int renderCamera = 0;
    private static boolean isFirstRendered;

    private static Graphics graphics;

    private static final Color colorSelectingBox = new Color(0, 120, 160, 160);

    public Pantalla(int width, int height) {
        // Store the instance of the canvas
        pantalla = this;

        // Configuration of the instance
        setSize(new Dimension(width, height));
        setFocusable(true);
        setPreferredSize(new Dimension(width, height));
        setVisible(true);
        setBackground(Color.BLACK);
        addKeyListener(new Keyboard());
        addMouseListener(new Mouse());
        addMouseMotionListener(new Mouse());
        addMouseWheelListener(new Mouse());

        // Add default object
        Cube cube = new Cube();
        addObject(cube);

        triangles = objects[0].getTrianglesList();

        // Add the main camera
        Camera camera = new Camera();
        addCamera(camera);
    }

    public void start() {
        this.requestFocus();
        this.createBufferStrategy(3);
        this.render();
        isFirstRendered = true;
    }

    public void render() {
        BufferStrategy bufferStrategy = this.getBufferStrategy();
        graphics = bufferStrategy.getDrawGraphics();

        // Clear the screen
        graphics.clearRect(0, 0, this.getWidth(), this.getHeight());

        // Order the triangles about the distance to the camera
        try {
            triangles.sort(new CompareDistance());
        } catch (IllegalArgumentException ignored) {}

        // Select objects about triangles of the objects
        if (selectingProperties != null)
            selectObjects();

        // Draw each triangle by distance from the camera
        for(Triangle t : triangles)
            drawTriangle(getCamera(), t);

        // Draw the square of the selection
        if (selectingProperties != null && !(selectingProperties[2] == 0 && selectingProperties[3] == 0))
            drawSelectingSquare();

        // Print the logs
        graphics.setColor(Color.WHITE);
        logs();

        // Show
        bufferStrategy.show();
        graphics.dispose();
    }

    private void drawPoint(int x, int y, int width, int height) {
        graphics.fillRect(x + this.getWidth() / 2, y + this.getHeight() / 2, width, height);
    }

    private void fillTriangle(double scalar, Polygon p, boolean mark) {
        graphics.setColor(Calc.getColor(-scalar, !mark));
        graphics.fillPolygon(p);
    }

    public void drawSelectingSquare() {
        graphics.setColor(colorSelectingBox);
        if(selectingProperties[2] < 0) {
            selectingProperties[0] += selectingProperties[2];
            selectingProperties[2] =- selectingProperties[2];
        }
        if(selectingProperties[3] < 0) {
            selectingProperties[1] += selectingProperties[3];
            selectingProperties[3] =- selectingProperties[3];
        }
        graphics.fillRect(selectingProperties[0], selectingProperties[1], selectingProperties[2], selectingProperties[3]);
    }

    private void drawTriangle(Camera c, Triangle t) {
        Vertice v1 = t.getVertices().get(0);
        Vertice v2 = t.getVertices().get(1);
        Vertice v3 = t.getVertices().get(2);

        double scalar = Calc.getEscalar(c, t.getObj(), v1, v2, v3);

        if (scalar < 0) {
            ArrayList<Integer> vertex1 = Calc.mathAxis(c, v1, t.getObj());
            ArrayList<Integer> vertex2 = Calc.mathAxis(c, v2, t.getObj());
            ArrayList<Integer> vertex3 = Calc.mathAxis(c, v3, t.getObj());

            int diffHeight = this.getHeight() / 2;
            int diffWidth = this.getWidth() / 2;

            int[] x = new int[]{vertex1.get(0) + diffWidth, vertex2.get(0) + diffWidth, vertex3.get(0) + diffWidth};
            int[] y = new int[]{vertex1.get(1) + diffHeight, vertex2.get(1) + diffHeight, vertex3.get(1) + diffHeight};

            fillTriangle(scalar, new Polygon(x, y, 3), t.getObj().isSelected());
        }

    }

    private void logs() {
        graphics.drawString("Rotación Cam X: " + cameras[renderCamera].getrX(), 10, 20);
        graphics.drawString("Rotación Cam Y: " + cameras[renderCamera].getrY(), 10, 40);
        graphics.drawString("Rotación Cam Z: " + cameras[renderCamera].getrZ(), 10, 60);
        graphics.drawString("Posición Cam X: " + cameras[renderCamera].getX(), 10, 100);
        graphics.drawString("Posición Cam Y: " + cameras[renderCamera].getY(), 10, 120);
        graphics.drawString("Posición Cam Z: " + cameras[renderCamera].getZ(), 10, 140);
        graphics.drawString("Focus Cam X: " + cameras[renderCamera].getFocusX(), 10, 180);
        graphics.drawString("Focus Cam Y: " + cameras[renderCamera].getFocusY(), 10, 200);
        graphics.drawString("Focus Cam Z: " + cameras[renderCamera].getFocusZ(), 10, 220);
        graphics.drawString("Plano X: " + cameras[renderCamera].getPX(), 10, 260);
        graphics.drawString("Plano Y: " + cameras[renderCamera].getPY(), 10, 280);
        graphics.drawString("Plano Z: " + cameras[renderCamera].getPZ(), 10, 300);
        if (this.getHeight() > 400) {
            graphics.drawString("Esc -> Cerrar", 10, this.getHeight() - 100);
            graphics.drawString("O -> Abrir archivo .obj", 10, this.getHeight() - 80);
            graphics.drawString("G -> Mover objeto(s) seleccionado(s)", 10, this.getHeight() - 60);
            graphics.drawString("Botón central del ratón -> Rotar cámara sobre el objeto", 10, this.getHeight() - 40);
            graphics.drawString("Botón central del ratón + SHIFT -> Mover cámara", 10, this.getHeight() - 20);
        }
    }

    private void selectObjects() {
        // Set objects to not selected state
        for (ComplexObject co : objects)
            co.setSelected(false);

        Triangle tr;
        Triangle[] trs;

        if (selectingProperties[2] != 0) {
            trs = Calc.isSquareInPolygons(selectingProperties, triangles);
            if (trs != null) {
                for (Triangle t : trs) {
                    t.getObj().setSelected(true);
                }
            }
        } else {
            tr = Calc.isPointInPolygon(new Point(selectingProperties[0], selectingProperties[1]), triangles);
            if (tr == null) {
                isMoving = false;
            } else {
                tr.getObj().setSelected(true);
            }
            selectingProperties = null;
        }

    }

    public static void addObject(ComplexObject obj) {
        objects = Arrays.copyOf(objects, objects.length + 1);
        objects[objects.length - 1] = obj;
    }

    public static void addCamera(Camera cam) {
        cameras = Arrays.copyOf(cameras, cameras.length + 1);
        cameras[cameras.length - 1] = cam;
    }

    public static boolean setCamera(int n) {
        if (n >= 0 && n <= cameras.length - 1) {
            renderCamera = n;
            return true;
        } else {
            return false;
        }
    }

    public static boolean isRendering() {
        return isFirstRendered;
    }

    public static Camera getCamera() {
        return cameras[renderCamera];
    }

    public static Pantalla getInstance() {
        return pantalla;
    }

    public static void setObjects(ComplexObject[] objects) {
        if (objects == null) return; // If no objects return

        Pantalla.objects = objects; // Store the objects
        triangles.clear(); // Reset the triangles

        for (ComplexObject co : objects) {
            triangles.addAll(co.getTrianglesList());
        }

        getInstance().render();
        getInstance().render();
    }


    public static void addObjects(ComplexObject[] objects) {
        if (objects == null) return; // If no objects return

        Pantalla.objects = Arrays.copyOf(Pantalla.objects, Pantalla.objects.length + objects.length);
    }

    public static void setSelecting(int[] arr) {
        selectingProperties = arr;
    }

    public static boolean isMoving() {
        return isMoving;
    }

    public static void setIsMoving(boolean isMoving) {
        boolean mark = false;
        for (ComplexObject co : objects) {
            if (co.isSelected()) {
                mark = true;
                break;
            }
        }

        if (mark) {
            cordsMoved = new int[2]; // Initialize the cords to [0, 0]
            Pantalla.isMoving = isMoving;
        }
    }

    public static void setCordsMoved(int[] cordsMoved) {
        Pantalla.cordsMoved = cordsMoved;
    }

    public static ComplexObject[] getSelectedObjects() {
        ComplexObject[] c = new ComplexObject[0];
        for (ComplexObject co : objects) {
            if (co.isSelected()) {
                c = Arrays.copyOf(c, c.length + 1);
                c[c.length - 1] = co;
            }
        }
        return c;
    }

    public static int[] getCordsMoved() {
        return cordsMoved;
    }
}