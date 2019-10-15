package codewars.Ten_Minute_Walk;

/**
 * @author michael.malevannyy@gmail.com, 08.10.2019
 */

public class TenMinWalk {
    public static boolean isValid(char[] walk) {
        if(walk.length != 10)
            return false;

        int x=0;
        int y=0;
        for (char c : walk) {
            switch (c) {
                // 'n', 's', 'w', 'e']).
                case 'n':
                    ++y;
                    break;
                case 's':
                    --y;
                    break;
                case 'w':
                    --x;
                    break;
                case 'e':
                    ++x;
                    break;
            }
        }

        return x==0 && y==0;
    }
}