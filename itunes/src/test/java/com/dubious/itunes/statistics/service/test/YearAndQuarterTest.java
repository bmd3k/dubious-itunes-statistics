package com.dubious.itunes.statistics.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.exception.YearNotPositiveException;
import com.dubious.itunes.statistics.service.Quarter;
import com.dubious.itunes.statistics.service.YearAndQuarter;

/**
 * Tests of {@link YearAndQuarter}.
 */
public class YearAndQuarterTest {

    /**
     * Do not allow creation of YearAndQuarter with year 0.
     * 
     * @throws StatisticsException On error.
     */
    @Test
    public final void testZeroYear() throws StatisticsException {
        try {
            new YearAndQuarter(0, Quarter.Q1);
            fail("expected exception not thrown");
        } catch (YearNotPositiveException e) {
            assertEquals(new YearNotPositiveException(0), e);
        }
    }

    /**
     * Do not allow creation of YearAndQuarter with negative year.
     * 
     * @throws StatisticsException On error.
     */
    @Test
    public final void testNegativeYear() throws StatisticsException {
        try {
            new YearAndQuarter(-5, Quarter.Q1);
            fail("expected exception not thrown");
        } catch (YearNotPositiveException e) {
            assertEquals(new YearNotPositiveException(-5), e);
        }
    }

    /**
     * Test comparison with an equal.
     * 
     * @throws StatisticsException On error.
     */
    @Test
    public final void testCompareToEqual() throws StatisticsException {
        assertEquals(0, new YearAndQuarter(1995, Quarter.Q1).compareTo(new YearAndQuarter(
                1995,
                Quarter.Q1)));
    }

    /**
     * Test comparison with an object that is greater but in same year.
     * 
     * @throws StatisticsException On error.
     */
    @Test
    public final void testCompareToGreaterThanSameYear() throws StatisticsException {
        assertEquals(-2, new YearAndQuarter(1995, Quarter.Q2).compareTo(new YearAndQuarter(
                1995,
                Quarter.Q4)));
    }

    /**
     * Test comparison with an object that is greater and in different year.
     * 
     * @throws StatisticsException On error.
     */
    @Test
    public final void testCompareToGreaterThanDifferentYear() throws StatisticsException {
        assertEquals(-12, new YearAndQuarter(1994, Quarter.Q2).compareTo(new YearAndQuarter(
                1997,
                Quarter.Q2)));
    }

    /**
     * Test comparison with an object that is lesser but in the same year.
     * 
     * @throws StatisticsException On error.
     */
    @Test
    public final void testCompareToLessThanSameYear() throws StatisticsException {
        assertEquals(3, new YearAndQuarter(1994, Quarter.Q4).compareTo(new YearAndQuarter(
                1994,
                Quarter.Q1)));
    }

    /**
     * Test comparison with an object that is lesser and in a different year.
     * 
     * @throws StatisticsException On error.
     */
    @Test
    public final void testCompareToLessThanDifferentYear() throws StatisticsException {
        assertEquals(15, new YearAndQuarter(1994, Quarter.Q4).compareTo(new YearAndQuarter(
                1991,
                Quarter.Q1)));
    }

    /**
     * Test add no quarters.
     * 
     * @throws StatisticsException On error.
     */
    @Test
    public final void testAddNothing() throws StatisticsException {
        assertEquals(
                new YearAndQuarter(2002, Quarter.Q1),
                new YearAndQuarter(2002, Quarter.Q1).addQuarters(0));
    }

    /**
     * Test add positive number of quarters that gives an object in the same year.
     * 
     * @throws StatisticsException On error.
     */
    @Test
    public final void testAddPositiveToSameYear() throws StatisticsException {
        assertEquals(
                new YearAndQuarter(2002, Quarter.Q3),
                new YearAndQuarter(2002, Quarter.Q1).addQuarters(2));
    }

    /**
     * Test add positive number of quarters tat gives an object in a different year.
     * 
     * @throws StatisticsException On error.
     */
    @Test
    public final void testAddPositiveToDifferentYear() throws StatisticsException {
        assertEquals(
                new YearAndQuarter(2003, Quarter.Q4),
                new YearAndQuarter(2002, Quarter.Q1).addQuarters(7));
    }

    /**
     * Test add negative number of quarters that gives an object in the same year.
     * 
     * @throws StatisticsException On error.
     */
    @Test
    public final void testAddNegativeToSameYear() throws StatisticsException {
        assertEquals(
                new YearAndQuarter(2002, Quarter.Q1),
                new YearAndQuarter(2002, Quarter.Q2).addQuarters(-1));
    }

    /**
     * Test add negative number of quarters that gives an object in a different year.
     * 
     * @throws StatisticsException On error.
     */
    @Test
    public final void testAddNegativeToDifferentYear() throws StatisticsException {
        assertEquals(
                new YearAndQuarter(1996, Quarter.Q3),
                new YearAndQuarter(2002, Quarter.Q2).addQuarters(-23));
    }
}
