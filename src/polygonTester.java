import java.awt.*;

public class polygonTester {

    public static void main(String[] args){
        int[] xpoints = new int[]{0, 10, 1, 0};
        int[] ypoints = new int[]{0, 0, 10, 10};
        Polygon p = new Polygon(xpoints, ypoints, 4);
//        System.out.println(p.contains(new Point(0, 0)));
//        System.out.println(p.contains(new Point(1, 1)));
        for(int i = 0; i < 10; i += 1){
            System.out.println(i);
        }
    }
}
