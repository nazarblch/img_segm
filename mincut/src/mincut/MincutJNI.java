package mincut;


public class MincutJNI  {

    public double minCut = -1;

    public MincutJNI() {
        System.loadLibrary("mincut_dll");

    }

	synchronized public native int[] minCut(BidirEdge[] edges, Tweight[] tweights, int nodesCount);


}