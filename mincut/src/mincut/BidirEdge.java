package mincut;

public class BidirEdge {

    public int from;
    public int to;
    public double w12;
    public double w21;

    public BidirEdge(int id1, int id2, double u, double v) {
        from = id1;
        to = id2;
        w12 = u;
        w21 = v;
    }
};
