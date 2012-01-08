package com.dubious.itunes.statistics.exception;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Base exception class for the iTunes statistics project.
 */
public abstract class StatisticsException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String[] EXCLUDED_COMPARISON_FIELDS = new String[] {"cause"};

    /**
     * Constructor.
     * 
     * @param message Exception message.
     */
    public StatisticsException(String message) {
        super(message);
    }

    /**
     * Constructor.
     * 
     * @param message Exception message.
     * @param cause Underlying cause of the exception.
     */
    public StatisticsException(String message, Throwable cause) {
        super(message, cause);
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

        return EqualsBuilder.reflectionEquals(this, other, EXCLUDED_COMPARISON_FIELDS);
    }

    @Override
    public final int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, EXCLUDED_COMPARISON_FIELDS);
    }

    @Override
    public final String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }

}
