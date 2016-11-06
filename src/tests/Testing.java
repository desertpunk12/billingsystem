package tests;

import java.util.Arrays;
import java.util.SimpleTimeZone;
import java.util.stream.Stream;

/**
 * Created by dpunk12 on 11/5/2016.
 */
public class Testing {

    public static void xx(Object ... x){
        for (Object xx :x) {
            System.out.println(xx);

        }
    }

    public static void main(String[] args) {
//       xx(1,2,3,"wow","wew");
        String[] params = {"wewe","ffa"};
        System.out.println(
                Arrays.stream(params)
                        .reduce("(",(q, e) -> q+=e+","));
        System.out.println("("+String.join(",",params)+")");
    }
}
