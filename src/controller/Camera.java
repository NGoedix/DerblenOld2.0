package controller;

import main.Pantalla;
import util.Calc;

public class Camera {

    private double cX;
    private double cY;
    private double cZ;

    private double rX;
    private double rY;
    private double rZ;

    private double pX = 0;
    private double pY = 50.0;
    private double pZ = 200.0;

    private double focusX = 0;
    private double focusY = 0;
    private double focusZ = 0;

    private boolean isToRight = false;

    public Camera(int x, int y, int z) {
        this.cX = x;
        this.cY = y;
        this.cZ = z;

        this.rX = 0;
        this.rY = 0;
        this.rZ = 0;
    }

    public Camera() {
        this(0, 0, 5000);
    }

    public double getPX() { return pX; }

    public double getPY() { return pY; }

    public double getPZ() { return pZ; }

    public double getX() {
        return cX;
    }

    public double getY() {
        return cY;
    }

    public double getZ() {
        return cZ;
    }

    public void setPX(double x) {
        this.pX = x;
    }

    public void setPY(double y) {
        this.pY = y;
    }

    public void setPZ(double z) {
        this.pZ = z;
    }

    public void setX(double x) {
        this.cX = x;
    }

    public void setY(double y) {
        this.cY = y;
    }

    public void setZ(double z) {
        this.cZ = z;
    }

    public double getrX() {
        return rX;
    }

    public double getrY() {
        return rY;
    }

    public double getrZ() {
        return rZ;
    }

    public void setrX(double rX) {
        this.rX = rX;
        renderRotation();
    }

    public void setrY(double rY) {
        this.rY = rY;
        renderRotation();
    }

    public void setrZ(double rZ) {
        this.rZ = rZ;
    }

    public double getFocusX() {
        return focusX;
    }

    public double getFocusY() {
        return focusY;
    }

    public double getFocusZ() {
        return focusZ;
    }

    public void setFocusX(double focusX) {
        this.focusX = focusX;
    }

    public void setFocusY(double focusY) {
        this.focusY = focusY;
    }

    public void setFocusZ(double focusZ) {
        this.focusZ = focusZ;
    }

    public boolean getIsToRight() {
        return isToRight;
    }

    public void setIsToRight(boolean is) {
        isToRight = is;
    }

    public void render(int c) {
        double distance = Math.sqrt(Math.pow(focusX - cX, 2) + Math.pow(focusY - cY, 2) + Math.pow(focusZ - cZ, 2)) + c;
        double s = Math.sin((90 - rX) * Math.PI / 180);
        cX = Math.round(focusX + distance * s * Math.cos((90 - rY) * Math.PI / 180));
        cY = Math.round(focusY + distance * -Math.cos((90 - rX) * Math.PI / 180));
        cZ = Math.round(focusZ + distance * s * Math.sin((90 - rY) * Math.PI / 180));
    }

    public void moveX(double x) {
        x = x * (int) (Calc.getDistanceOrigin() / 250);

        double difX = -x * Math.cos(rY * Math.PI / 180);
        double difZ = x * Math.sin(rY * Math.PI / 180);
        focusX += difX;
        focusZ += difZ;
        cX += difX;
        cZ += difZ;
    }

    public void moveY(double y) {
        y = y * (int) (Calc.getDistanceOrigin() / 250);

        double difX = y * Math.sin(rY * Math.PI / 180) * Math.sin(rX * Math.PI / 180);
        double difY = y * Math.cos(rX * Math.PI / 180);
        double difZ = y * Math.sin(rX * Math.PI / 180) * Math.cos(rY * Math.PI / 180);
        focusX -= difX;
        focusY -= difY;
        focusZ -= difZ;
        cX -= difX;
        cY -= difY;
        cZ -= difZ;
    }

    public void renderRotation() {
        render(0);
    }

    public void reset() {
        cX = 0;
        cY = 0;
        cZ = 5000;
        rX = 0;
        rY = 0;
        rZ = 0;
        render(0);
        Pantalla.getInstance().render();
    }
}
