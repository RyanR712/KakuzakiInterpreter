package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LexerTest
{
    @Test
    void twoPlusTwoEqualsFour()
    {
        assertEquals(2 + 2, 4);
    }

    @Test
    void threePlusThreeEqualsSix()
    {
        assertEquals(3 + 3, 6);
    }

    @Test
    void completeFailure()
    {
        assertEquals(2, 3);
    }
}
