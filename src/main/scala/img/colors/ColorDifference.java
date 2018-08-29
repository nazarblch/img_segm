package img;

import java.awt.Color;

public class ColorDifference {

    static double norm = 255 * Math.sqrt (3.0);

    static public int[] toArray(int argb) {
        int r = (argb)&0xFF;
        int g = (argb>>8)&0xFF;
        int b = (argb>>16)&0xFF;
        int [] arr = {r, g, b};
        return arr;
    }

    static public int toBAW(int argb) {
        int r = (argb)&0xFF;
        int g = (argb>>8)&0xFF;
        int b = (argb>>16)&0xFF;

        if (r < 2 && g < 2 && b < 2) return 0;
        else return 1;
    }

	static public double findDifference(int argb1, int argb2) {
        return colorDistance(toArray(argb1), toArray(argb2));
	}

    public static double colorDistance (int[] color1, int[] color2)
    {
        double a = color1[0] - color2[0];
        double b = color1[1] - color2[1];
        double c = color1[2] - color2[2];

        return Math.sqrt (a*a + b*b + c*c) / norm;
    }


    public static int average(int[] rgbs) {
        int[] res = {0, 0, 0};

        for (int i = 0; i < rgbs.length; i ++) {
           int [] color = toArray(rgbs[i]);
           res[0] += color[0];
           res[1] += color[1];
           res[2] += color[2];
        }

        Color c = new Color(res[0]/rgbs.length, res[1]/rgbs.length, res[2]/rgbs.length);

        return c.getRGB();
    }


}
