package org.litesoft.codec;

import java.util.HashMap;
import java.util.Map;

import org.litesoft.annotations.PackageFriendlyForTesting;
import org.litesoft.annotations.Significant;

public class Codec {
    private static final Map<String, Class<? extends Codec>> REGISTERED = new HashMap<>();

    protected final String codexId;

    protected Codec( String codexGroup, String codexSpecialization, int codexVersion ) {
        codexId = assertIdPart( "codexGroup", codexGroup ) +
                  assertIdPart( "codexSpecialization", codexSpecialization ) +
                  assertIdPart( "codexVersion", codexVersion ) +
                  ":";
        Class<? extends Codec> us = this.getClass();
        Class<? extends Codec> previous = REGISTERED.put( codexId, us );
        if ( (previous != null) && (previous != us) ) {
            throw new Error( "CodexId '" + codexId + "'" + ERROR_DUPLICATE_REGISTRATION_MID_TEXT +
                             "\n    " + previous +
                             "\n    " + us );
        }
    }

    private String assertIdPart( String name, String value ) {
        if ( value.contains( ":" ) ) {
            throw new Error( "Codex Id part '" + name + "'" + ERROR_CODEX_ID_PART_CONTAINED_COLON_SUFFIX );
        }
        value = Significant.ConstrainTo.valueOrNull( value );
        if ( value == null ) {
            throw new Error( "Codex Id part '" + name + "'" + ERROR_CODEX_ID_PART_INSIGNIFICANT_SUFFIX );
        }
        return value;
    }

    @SuppressWarnings("SameParameterValue")
    private int assertIdPart( String name, int value ) {
        if ( value < 1 ) {
            throw new Error( "Codex Id part '" + name + "'" + ERROR_CODEX_ID_PART_NOT_POSITIVE_SUFFIX );
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
        if ( !encoded.startsWith( codexId ) ) {
            throw new IllegalArgumentException( errorDidNotStartWithCodexId() + limitLength( encoded ) );
        }
        if ( encoded.equals( codexId ) ) {
            throw new IllegalArgumentException( errorOnlyCodexId() );
        }
        return encoded.substring( codexId.length() );
    }

    private static String limitLength( String value ) {
        return (value.length() <= 20) ? value :
               (value.substring( 0, 17 ) + "..."); // Note: NOT UTF safe!
    }

    @PackageFriendlyForTesting
    String errorDidNotStartWithCodexId() {
        return "supplied encoded string did NOT start with '" + codexId + "', encoded string was: ";
    }

    @PackageFriendlyForTesting
    String errorOnlyCodexId() {
        return "supplied encoded string consisted ONLY of '" + codexId + "'";
    }

    @PackageFriendlyForTesting
    String errorToEncodeTooShort( int minLength ) {
        return "supplied string to class '" + this.getClass().getSimpleName() +
               "' to encode MUST be at least " + minLength +
               " characters long, the string was: ";
    }

    @PackageFriendlyForTesting
    static final String ERROR_DUPLICATE_REGISTRATION_MID_TEXT = " -- duplicate registration attempted, classes are:";
    static final String ERROR_CODEX_ID_PART_CONTAINED_COLON_SUFFIX = " must NOT contain a ':' colon (reserved for Codex Id terminating character)";
    static final String ERROR_CODEX_ID_PART_INSIGNIFICANT_SUFFIX = " must NOT be null or empty";
    static final String ERROR_CODEX_ID_PART_NOT_POSITIVE_SUFFIX = " MUST be positive (1 or greater)";

    @PackageFriendlyForTesting
    static void clearRegistration() {
        REGISTERED.clear();
    }
}
