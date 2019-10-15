package codewars.Ten_Minute_Walk;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author michael.malevannyy@gmail.com, 08.10.2019
 */

public class TenMinWalkTest {

    @Test
    public void isValid() {
        assertEquals("Should return true", true, TenMinWalk.isValid(new char[] {'n','s','n','s','n','s','n','s','n','s'}));
        assertEquals("Should return false", false, TenMinWalk.isValid(new char[] {'w','e','w','e','w','e','w','e','w','e','w','e'}));
        assertEquals("Should return false", false, TenMinWalk.isValid(new char[] {'w'}));
        assertEquals("Should return false", false, TenMinWalk.isValid(new char[] {'n','n','n','s','n','s','n','s','n','s'}));
    }
}