package comparer;

import main.Pantalla;
import objects.Triangle;
import objects.Vertice;
import util.Calc;

import java.util.Arrays;
import java.util.Comparator;

public class CompareDistance implements Comparator<Triangle> {

    @Override
    public int compare(Triangle o1, Triangle o2) {
        return (int) (Calc.getDistance(o2) - Calc.getDistance(o1));
    }
}
