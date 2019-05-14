package me.lanner.liz.paper.math;

public class Matrix extends Vector<Vector<Double>> {

    private final int width;

    public Matrix(int width, int length) {
        super(length);
        this.width = width;
    }

    public void set(Integer x, Integer y, Double v) {
        Vector<Double> vector = get(x);
        if (vector == null) {
            vector = new Vector<>(width);
            set(x, vector);
        }
        vector.set(y, v);
    }

    public Double get(Integer x, Integer y) {
        Vector<Double> vector = get(x);
        if (vector == null) {
            return null;
        }
        return vector.get(y);
    }

}

