package org.litesoft.codec;

import java.util.HashMap;
import java.util.Map;

import org.litesoft.annotations.PackageFriendlyForTesting;
import org.litesoft.annotations.Significant;

public class Codec {
    private static final Map<String, Class<? extends Codec>> REGISTERED = new HashMap<>();

    protected final String codecId;

    protected Codec( String codecGroup, String codecSpecialization, int codecVersion ) {
        codecId = assertIdPart( "codecGroup", codecGroup ) +
                  assertIdPart( "codecSpecialization", codecSpecialization ) +
                  assertIdPart( "codecVersion", codecVersion ) +
                  ":";
        Class<? extends Codec> us = this.getClass();
        Class<? extends Codec> previous = REGISTERED.put( codecId, us );
        if ( (previous != null) && (previous != us) ) {
            throw new Error( "CodecId '" + codecId + "'" + ERROR_DUPLICATE_REGISTRATION_MID_TEXT +
                             "\n    " + previous +
                             "\n    " + us );
        }
    }

    private String assertIdPart( String name, String value ) {
        if ( value.contains( ":" ) ) {
            throw new Error( "Codec Id part '" + name + "'" + ERROR_CODEX_ID_PART_CONTAINED_COLON_SUFFIX );
        }
        value = Significant.ConstrainTo.valueOrNull( value );
        if ( value == null ) {
            throw new Error( "Codec Id part '" + name + "'" + ERROR_CODEX_ID_PART_INSIGNIFICANT_SUFFIX );
        }
        return value;
    }

    @SuppressWarnings("SameParameterValue")
    private int assertIdPart( String name, int value ) {
        if ( value < 1 ) {
            throw new Error( "Codec Id part '" + name + "'" + ERROR_CODEX_ID_PART_NOT_POSITIVE_SUFFIX );
        }
        return value;
    }

    @SuppressWarnings("SameParameterValue")
    protected String validateToEncode( String toEncode, int minLength ) {
        toEncode = Significant.AssertArgument.namedValue( "to encode", toEncode );
        if ( toEncode.length() < minLength ) {
            throw new IllegalArgumentException( errorToEncodeTooShort( minLength ) + toEncode );
        }
        return toEncode;
    }

    protected String validateToDecode( String encoded ) {
        encoded = Significant.AssertArgument.namedValue( "encoded", encoded );
        if ( !encoded.startsWith( codecId ) ) {
            throw new IllegalArgumentException( errorDidNotStartWithCodecId() + limitLength( encoded ) );
        }
        if ( encoded.equals( codecId ) ) {
            throw new IllegalArgumentException( errorOnlyCodecId() );
        }
        return encoded.substring( codecId.length() );
    }

    private static String limitLength( String value ) {
        return (value.length() <= 20) ? value :
               (value.substring( 0, 17 ) + "..."); // Note: NOT UTF safe!
    }

    @PackageFriendlyForTesting
    String errorDidNotStartWithCodecId() {
        return "supplied encoded string did NOT start with '" + codecId + "', encoded string was: ";
    }

    @PackageFriendlyForTesting
    String errorOnlyCodecId() {
        return "supplied encoded string consisted ONLY of '" + codecId + "'";
    }

    @PackageFriendlyForTesting
    String errorToEncodeTooShort( int minLength ) {
        return "supplied string to class '" + this.getClass().getSimpleName() +
               "' to encode MUST be at least " + minLength +
               " characters long, the string was: ";
    }

    @PackageFriendlyForTesting
    static final String ERROR_DUPLICATE_REGISTRATION_MID_TEXT = " -- duplicate registration attempted, classes are:";
    static final String ERROR_CODEX_ID_PART_CONTAINED_COLON_SUFFIX = " must NOT contain a ':' colon (reserved for Codec Id terminating character)";
    static final String ERROR_CODEX_ID_PART_INSIGNIFICANT_SUFFIX = " must NOT be null or empty";
    static final String ERROR_CODEX_ID_PART_NOT_POSITIVE_SUFFIX = " MUST be positive (1 or greater)";

    @PackageFriendlyForTesting
    static void clearRegistration() {
        REGISTERED.clear();
    }
}
