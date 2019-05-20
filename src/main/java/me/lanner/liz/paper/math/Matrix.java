package me.lanner.liz.paper.math;

public class Matrix<K> extends Vector<K, Vector<Integer, Double>> {

    private final int width;

    public Matrix(int width, int length) {
        super(length);
        this.width = width;
    }

    public void set(K x, Integer y, Double v) {
        Vector<Integer, Double> vector = get(x);
        if (vector == null) {
            vector = new Vector<>(width);
            set(x, vector);
        }
        vector.set(y, v);
    }

    public Double get(K x, Integer y) {
        Vector<Integer, Double> vector = get(x);
        if (vector == null) {
            return null;
        }
        return vector.get(y);
    }

}

