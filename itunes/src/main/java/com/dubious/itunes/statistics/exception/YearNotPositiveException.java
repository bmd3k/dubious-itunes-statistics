package com.dubious.itunes.statistics.exception;


/**
 * Exception thrown on attempts to create a
 * {@link com.dubious.itunes.statistics.service.YearAndQuarter} with a non-positive year.
 */
public class YearNotPositiveException extends StatisticsException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int year;

    /**
     * Constructor.
     * 
     * @param year The non-positive year.
     */
    public YearNotPositiveException(int year) {
        super("Attempted to create YearAndQuarter with non-positive year [" + year + "]");
        this.year = year;
    }

    /**
     * Retrieve the non-positive year.
     * 
     * @return The non-positive year.
     */
    public final int getYear() {
        return year;
    }
}
