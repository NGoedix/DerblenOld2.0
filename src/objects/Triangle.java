package objects;

import java.util.ArrayList;

public class Triangle {

    private ComplexObject obj;

    Vertice v1, v2, v3;

    public Triangle(ComplexObject obj, Vertice v1, Vertice v2, Vertice v3) {
        this.obj = obj;
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    /*public Triangle(Vertice v1, Vertice v2, Vertice v3) {
        this(null, v1, v2, v3);
    }*/

    public ArrayList<Vertice> getVertices() {
        return new ArrayList<Vertice>() {{
            add(v1);
            add(v2);
            add(v3);
        }};
    }

    public ComplexObject getObj() {
        return obj;
    }
}