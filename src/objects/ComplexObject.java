package objects;

import main.Pantalla;

import java.util.ArrayList;
import java.util.Arrays;

public class ComplexObject {

    protected ArrayList<Triangle> triangles;

    private boolean isSelected = false;

    protected double rX = 0;
    protected double rY = 0;
    protected double rZ = 0;

    protected double x = 0;
    protected double y = 0;
    protected double z = 0;

    public ComplexObject() {
        triangles = new ArrayList<>();
    }

    public ComplexObject(Triangle[] triangles) {
        this.triangles = new ArrayList<>(Arrays.asList(triangles));
    }

    public ArrayList<Triangle> getTrianglesList() {
        return triangles;
    }

    public Triangle[] getTrianglesArray() {
        Triangle[] t = new Triangle[triangles.size()];
        triangles.toArray(t);
        return t;
    }

    public void moveX(double x) {
        double difX = -x * Math.cos(Pantalla.getCamera().getrY() * Math.PI / 180);
        double difZ = x * Math.sin(Pantalla.getCamera().getrY() * Math.PI / 180);
        this.x += difX;
        z += difZ;
    }

    public void moveY(double y) {
        double difX = y * Math.sin(Pantalla.getCamera().getrY() * Math.PI / 180) * Math.sin(Pantalla.getCamera().getrX() * Math.PI / 180);
        double difY = y * Math.cos(Pantalla.getCamera().getrX() * Math.PI / 180);
        double difZ = y * Math.sin(Pantalla.getCamera().getrX() * Math.PI / 180) * Math.cos(Pantalla.getCamera().getrY() * Math.PI / 180);
        x -= difX;
        this.y -= difY;
        z -= difZ;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
