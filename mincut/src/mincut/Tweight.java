package mincut;

public class Tweight {
    public int index;
    public double w12;
    public double w21;

    public Tweight(int id, double u, double v) {
        index = id;
        w12 = u;
        w21 = v;
    }
}
