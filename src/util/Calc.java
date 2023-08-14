package util;

import main.Pantalla;
import objects.ComplexObject;
import objects.Triangle;
import objects.Vertice;
import controller.Camera;

import java.awt.*;
import java.util.ArrayList;

public class Calc {

    private Calc() {}

    /**
     *
     * @param c camera viewing the vertex
     * @param v Vertex to be projected onto a plane
     * @return Projection in the plane of a vertex with respect to a camera
     */
    public static ArrayList<Integer> mathAxis(Camera c, Vertice v, ComplexObject obj) {
        int x = v.getX() + (obj == null ? 0 : (int) obj.getX());
        int y = v.getY() + (obj == null ? 0 : (int) obj.getY());
        int z = v.getZ() + (obj == null ? 0 : (int) obj.getZ());

        double k = Math.cos(c.getrY() * Math.PI / 180); // a
        double l = Math.sin(c.getrZ() * Math.PI / 180); // b
        double p = Math.cos(c.getrZ() * Math.PI / 180); // c
        double j = Math.sin(c.getrY() * Math.PI / 180); // d
        double m = Math.sin(c.getrX() * Math.PI / 180);
        double i = Math.cos(c.getrX() * Math.PI / 180);

        double b = k * (z - c.getZ()) + j * (l * (y - c.getY()) + p * (x - c.getX()));
        double n = p * (y - c.getY()) - l * (x - c.getX());

        double tempXX = (int) (k * (l * (y - c.getY()) + p * (x - c.getX())) - j * (z - c.getZ()));
        double tempZX = (int) (Math.cos(c.getrX() * Math.PI / 180) * (k * (z - c.getZ()) + j * (l * (y - c.getY()) + p
                * (x - c.getX()))) - Math.sin(c.getrX() * Math.PI / 180) * (p * (y - c.getY()) - l * (x - c.getX())));

        double tempYY = (int) (m * (b) + i * (n));
        double tempZY = (int) (i * (b) - m * (n));

        double proportions = Toolkit.getDefaultToolkit().getScreenSize().getWidth() / Toolkit.getDefaultToolkit().getScreenSize().getHeight();

        return new ArrayList<Integer>() {{
            add((int) ((c.getPZ() / tempZX) * tempXX - c.getPX() / proportions));
            add((int) ((c.getPZ() / tempZY) * tempYY - c.getPY() / proportions));
        }};
    }


    /**
     * @param c Camera that view the triangle
     * @param v1,v2,v3 Vertices of a triangle on which we want the normal
     * @return List with triangle normals
     */
    public static double getEscalar(Camera c, ComplexObject obj, Vertice v1, Vertice v2, Vertice v3) {
        // Getting lines to the triangle
        double line1X;
        double line1Y;
        double line1Z;

        double line2X;
        double line2Y;
        double line2Z;

        if (obj != null) {
            line1X = (v2.getX() + obj.getX()) - (v1.getX() + obj.getX());
            line1Y = (v2.getY() + obj.getY()) - (v1.getY() + obj.getY());
            line1Z = (v2.getZ() + obj.getZ()) - (v1.getZ() + obj.getZ());

            line2X = (v3.getX() + obj.getX()) - (v1.getX() + obj.getX());
            line2Y = (v3.getY() + obj.getY()) - (v1.getY() + obj.getY());
            line2Z = (v3.getZ() + obj.getZ()) - (v1.getZ() + obj.getZ());
        } else {
            line1X = v2.getX() - v1.getX();
            line1Y = v2.getY() - v1.getY();
            line1Z = v2.getZ() - v1.getZ();

            line2X = v3.getX() - v1.getX();
            line2Y = v3.getY() - v1.getY();
            line2Z = v3.getZ() - v1.getZ();
        }

        // The normal of the 2 lines (triangle)
        double normalX = line1Y * line2Z - line1Z * line2Y;
        double normalY = line1Z * line2X - line1X * line2Z;
        double normalZ = line1X * line2Y - line1Y * line2X;

        double j = Math.sqrt(Math.pow(normalX, 2) + Math.pow(normalY, 2) + Math.pow(normalZ, 2));
        normalX /= j;
        normalY /= j;
        normalZ /= j;

        // Point in Vertex
        double x;
        double y;
        double z;
        if (obj != null) {
            x = v1.getX() + obj.getX();
            y = v1.getY() + obj.getY();
            z = v1.getZ() + obj.getZ();
        } else {
            x = v1.getX();
            y = v1.getY();
            z = v1.getZ();
        }


        // Point of the camera
        double cX = c.getX();
        double cY = c.getY();
        double cZ = c.getZ();

        // VECTOR A
        double AX = x - cX;
        double AY = y - cY;
        double AZ = z - cZ;

        double l = Math.sqrt(Math.pow(AX, 2) + Math.pow(AY, 2) + Math.pow(AZ, 2));
        AX /= l;
        AY /= l;
        AZ /= l;

        // returning the scalar vector
        return AX * normalX + AY * normalY + AZ * normalZ;
    }

    /**
     *
     * @param scalar Value between <b>0 and 1</b>
     * @param selected If the value must be in the blue scale
     * @return Color contained on a <b>grayscale</b>
     */
    public static Color getColor(double scalar, boolean selected) {
        if (scalar < 0 || scalar > 1)
            return Color.BLACK;

        int rgbNum = 80 + (int) (scalar * 130.0f);

        if (!selected) {
            return new Color(rgbNum, rgbNum, rgbNum);
        } else {
            return new Color(rgbNum, 20 + rgbNum, 45 + rgbNum);
        }
    }

    /**
     *
     * @param t Triangle that we want the average distance about their vertices
     * @return Average distance about their vertices
     */
    public static double getDistance(Triangle t) {
        double dis = 0;
        Vertice[] v = new Vertice[3];

        v[0] = t.getVertices().get(0);
        v[1] = t.getVertices().get(1);
        v[2] = t.getVertices().get(2);

        for (Vertice vertice : v) {
            dis += Math.sqrt(Math.pow(Pantalla.getCamera().getX() - vertice.getX(), 2)
                    + Math.pow(Pantalla.getCamera().getY() - vertice.getY(), 2)
                    + Math.pow(Pantalla.getCamera().getZ() - vertice.getZ(), 2));
        }
        dis /= 3;
        return dis;
    }

    /**
     *
     * @return Distance about Camera and Focus of the camera
     */
    public static double getDistanceOrigin() {
        double focusX = Pantalla.getCamera().getFocusX();
        double focusY = Pantalla.getCamera().getFocusY();
        double focusZ = Pantalla.getCamera().getFocusZ();

        double cX = Pantalla.getCamera().getX();
        double cY = Pantalla.getCamera().getY();
        double cZ = Pantalla.getCamera().getZ();

        double dX = cX - focusX;
        double dY = cY - focusY;
        double dZ = cZ - focusZ;

        return Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2) + Math.pow(dZ, 2));
    }

    /**
     *
     * @param point The point of the mouse
     * @param triangles The Array that contain all the triangles
     * @return The triangle that contain the mouse or null
     */
    public static Triangle isPointInPolygon(Point point, ArrayList<Triangle> triangles) {

        point = new Point((int) point.getX() - Pantalla.getInstance().getWidth() / 2, (int) point.getY() - Pantalla.getInstance().getHeight() / 2);
        //System.out.println("Point: (" + point.getX() + ", " + point.getY() + ")");

        for (Triangle t : triangles) {
            ArrayList<Integer> v1 = mathAxis(Pantalla.getCamera(), t.getVertices().get(0), t.getObj());
            ArrayList<Integer> v2 = mathAxis(Pantalla.getCamera(), t.getVertices().get(1), t.getObj());
            ArrayList<Integer> v3 = mathAxis(Pantalla.getCamera(), t.getVertices().get(2), t.getObj());

            double det = (v2.get(0) - v1.get(0)) * (v3.get(1) - v1.get(1)) - (v2.get(1) - v1.get(1)) * (v3.get(0) - v1.get(0));

            if (det * ((v2.get(0) - v1.get(0)) * (point.getY() - v1.get(1)) - (v2.get(1) - v1.get(1)) * (point.getX() - v1.get(0))) > 0 &&
                    det * ((v3.get(0) - v2.get(0)) * (point.getY() - v2.get(1)) - (v3.get(1) - v2.get(1)) * (point.getX() - v2.get(0))) > 0 &&
                    det * ((v1.get(0) - v3.get(0)) * (point.getY() - v3.get(1)) - (v1.get(1) - v3.get(1)) * (point.getX() - v3.get(0))) > 0)
                return t;
        }

        return null;
    }

    public static Triangle[] isSquareInPolygons(int[] square, ArrayList<Triangle> triangles) {
        ArrayList<Triangle> trs = new ArrayList<>();
        Point[] points = new Point[4];

        points[0] = new Point(square[0] - Pantalla.getInstance().getWidth() / 2, square[1] - Pantalla.getInstance().getHeight() / 2);
        points[1] = new Point((square[0] + square[2]) - Pantalla.getInstance().getWidth() / 2, square[1] - Pantalla.getInstance().getHeight() / 2);
        points[2] = new Point(square[0] - Pantalla.getInstance().getWidth() / 2, (square[1] + square[3]) - Pantalla.getInstance().getHeight() / 2);
        points[3] = new Point((square[0] + square[2]) - Pantalla.getInstance().getWidth() / 2, (square[1] + square[3]) - Pantalla.getInstance().getHeight() / 2);

        for (Triangle t : triangles) {
            // Check if object was selected
            boolean objectSelected = false;
            for (Triangle tr : trs) {
                if (tr.getObj() == t.getObj()) {
                    objectSelected = true;
                    break;
                }
            }
            if (objectSelected) continue;

            ArrayList<Integer> v1 = mathAxis(Pantalla.getCamera(), t.getVertices().get(0), t.getObj());
            ArrayList<Integer> v2 = mathAxis(Pantalla.getCamera(), t.getVertices().get(1), t.getObj());
            ArrayList<Integer> v3 = mathAxis(Pantalla.getCamera(), t.getVertices().get(2), t.getObj());

            for (int i = 0; i < points.length; i++) {
                Point actualPoint = points[i];
                Point nextPoint;
                if (i != points.length - 1)
                    nextPoint = points[i + 1];
                else
                    nextPoint = points[0];

                if ((ccw(actualPoint.x, actualPoint.y, v1.get(0), v1.get(1), v2.get(0), v2.get(1)) !=
                ccw(nextPoint.x, nextPoint.y, v1.get(0), v1.get(1), v2.get(0), v2.get(1))) &&
                        (ccw(actualPoint.x, actualPoint.y, nextPoint.x, nextPoint.y, v1.get(0), v1.get(1)) !=
                                ccw(actualPoint.x, actualPoint.y, nextPoint.x, nextPoint.y, v2.get(0), v2.get(1)))) {
                    trs.add(t);
                }

                if ((ccw(actualPoint.x, actualPoint.y, v2.get(0), v2.get(1), v3.get(0), v3.get(1)) !=
                        ccw(nextPoint.x, nextPoint.y, v2.get(0), v2.get(1), v3.get(0), v3.get(1))) &&
                        (ccw(actualPoint.x, actualPoint.y, nextPoint.x, nextPoint.y, v2.get(0), v2.get(1)) !=
                                ccw(actualPoint.x, actualPoint.y, nextPoint.x, nextPoint.y, v3.get(0), v3.get(1)))) {
                    trs.add(t);
                }

                if ((ccw(actualPoint.x, actualPoint.y, v3.get(0), v3.get(1), v1.get(0), v1.get(1)) !=
                        ccw(nextPoint.x, nextPoint.y, v3.get(0), v3.get(1), v1.get(0), v1.get(1))) &&
                        (ccw(actualPoint.x, actualPoint.y, nextPoint.x, nextPoint.y, v3.get(0), v3.get(1)) !=
                                ccw(actualPoint.x, actualPoint.y, nextPoint.x, nextPoint.y, v1.get(0), v1.get(1)))) {
                    trs.add(t);
                }
            }
        }

        if (trs.size() > 0) {
            Triangle[] aux = new Triangle[trs.size()];
            trs.toArray(aux);
            return aux;
        } else {
            return null;
        }
    }

    private static boolean ccw(int ax, int ay, int bx, int by, int cx, int cy) {
        return (cy - ay) * (bx - ax) > (by - ay) * (cx - ax);
    }
}
