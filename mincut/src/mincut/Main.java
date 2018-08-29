package mincut;

/**
 * Created with IntelliJ IDEA.
 * User: nazar
 * Date: 10/13/13
 * Time: 8:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String[] args) {
        MincutJNI o = new MincutJNI();

        BidirEdge[] e = {new BidirEdge(0, 1, 2, 3)};

        Tweight[] tw = {
                new Tweight(0, 5, 2),
                new Tweight(1, 4, 0)
        };

        o.minCut(e, tw, 2);

        System.out.print(o.minCut);
    }
}
