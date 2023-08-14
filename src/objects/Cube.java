package objects;

public class Cube extends ComplexObject {

    public Cube () {
        // FRONT
        Vertice v1, v2, v3;
        v1 = new Vertice(1000, -1000, 1000);
        v2 = new Vertice(1000, 1000, 1000);
        v3 = new Vertice(-1000, 1000, 1000);
        triangles.add(new Triangle(this, v1, v2, v3));

        v1 = new Vertice(1000, -1000, 1000);
        v2 = new Vertice(-1000, 1000, 1000);
        v3 = new Vertice(-1000, -1000, 1000);
        triangles.add(new Triangle(this, v1, v2, v3));

        // BACK
        v1 = new Vertice(-1000, -1000, -1000);
        v2 = new Vertice(-1000, 1000, -1000);
        v3 = new Vertice(1000, 1000, -1000);
        triangles.add(new Triangle(this, v1, v2, v3));

        v1 = new Vertice(-1000, -1000, -1000);
        v2 = new Vertice(1000, 1000, -1000);
        v3 = new Vertice(1000, -1000, -1000);
        triangles.add(new Triangle(this, v1, v2, v3));

        // RIGHT
        v1 = new Vertice(-1000, -1000, 1000);
        v2 = new Vertice(-1000, 1000, 1000);
        v3 = new Vertice(-1000, 1000, -1000);
        triangles.add(new Triangle(this, v1, v2, v3));

        v1 = new Vertice(-1000, -1000, 1000);
        v2 = new Vertice(-1000, 1000, -1000);
        v3 = new Vertice(-1000, -1000, -1000);
        triangles.add(new Triangle(this, v1, v2, v3));

        // LEFT
        v1 = new Vertice(1000, -1000, -1000);
        v2 = new Vertice(1000, 1000, -1000);
        v3 = new Vertice(1000, 1000, 1000);
        triangles.add(new Triangle(this, v1, v2, v3));

        v1 = new Vertice(1000, -1000, -1000);
        v2 = new Vertice(1000, 1000, 1000);
        v3 = new Vertice(1000, -1000, 1000);
        triangles.add(new Triangle(this, v1, v2, v3));

        // BOTTOM
        v1 = new Vertice(1000, -1000, -1000);
        v2 = new Vertice(1000, -1000, 1000);
        v3 = new Vertice(-1000, -1000, 1000);
        triangles.add(new Triangle(this, v1, v2, v3));

        v1 = new Vertice(1000, -1000, -1000);
        v2 = new Vertice(-1000, -1000, 1000);
        v3 = new Vertice(-1000, -1000, -1000);
        triangles.add(new Triangle(this, v1, v2, v3));

        // UP
        v1 = new Vertice(1000, 1000, 1000);
        v2 = new Vertice(1000, 1000, -1000);
        v3 = new Vertice(-1000, 1000, -1000);
        triangles.add(new Triangle(this, v1, v2, v3));

        v1 = new Vertice(1000, 1000, 1000);
        v2 = new Vertice(-1000, 1000, -1000);
        v3 = new Vertice(-1000, 1000, 1000);
        triangles.add(new Triangle(this, v1, v2, v3));
    }
}
