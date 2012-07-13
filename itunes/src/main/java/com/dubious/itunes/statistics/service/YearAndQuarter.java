package com.dubious.itunes.statistics.service;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.joda.time.DateMidnight;
import org.joda.time.ReadableDateTime;

import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.exception.YearNotPositiveException;

/**
 * Represents a Year and quarter within the year. The quarter should be value between 0 and 3.
 */
public class YearAndQuarter {
    private int year;
    private Quarter quarter;

    /**
     * Constructor.
     * 
     * @param year The year.
     * @param quarter The quarter.
     * @throws StatisticsException If the year is non-positive.
     */
    public YearAndQuarter(int year, Quarter quarter) throws StatisticsException {
        // It seems that Joda does not properly support pre-AD dates so we don't support
        // it either. For instance it recognizes 0 AD, which doesn't exist.
        if (year <= 0) {
            throw new YearNotPositiveException(year);
        }
        this.year = year;
        this.quarter = quarter;
    }

    /**
     * Retrieve the year.
     * 
     * @return The year.
     */
    public final int getYear() {
        return year;
    }

    /**
     * Retrieve the quarter.
     * 
     * @return The quarter.
     */
    public final Quarter getQuarter() {
        return quarter;
    }

    /**
     * Add specified number of quarters to create a new {@link YearAndQuarter}.
     * 
     * @param numQuarters The number of quarters to add.
     * @return The new value.
     * @throws StatisticsException If the resulting year is non-positive.
     */
    public final YearAndQuarter addQuarters(int numQuarters) throws StatisticsException {
        // Use Joda to help us with the year calculations, which can be especially complicated when
        // dealing with the past
        return fromDateTime(toDateMidnight().plusMonths(numQuarters * 3));
    }

    /**
     * Compare another {@link YearAndQuarter} to this.
     * 
     * @param yq2 The other.
     * @return The difference in quarters (this - yq2).
     */
    public final int compareTo(YearAndQuarter yq2) {
        // NOTE: We should consider using Joda for this arithmetic
        return ((this.year - yq2.year) * 4) + (this.quarter.ordinal() - yq2.quarter.ordinal());
    }

    /**
     * Convert to a {@link DateMidnight}.
     * 
     * @return DateMidnight The DateMidnight representation of this year and quarter.
     */
    private DateMidnight toDateMidnight() {
        return new DateMidnight(year, (quarter.ordinal() + 1) * 3, 1);
    }

    /**
     * Convert a joda DateTime to a {@link YearAndQuarter}.
     * 
     * @param dateTime The DateTime to convert.
     * @return The result.
     * @throws StatisticsException On error.
     */
    public static final YearAndQuarter fromDateTime(ReadableDateTime dateTime)
            throws StatisticsException {
        return new YearAndQuarter(
                dateTime.getYear(),
                Quarter.values()[(dateTime.getMonthOfYear() - 1) / 3]);
    }

    @Override
    public final boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (other.getClass() != getClass()) {
            return false;
        }

        return EqualsBuilder.reflectionEquals(this, other);
    }

    @Override
    public final int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public final String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }
}
