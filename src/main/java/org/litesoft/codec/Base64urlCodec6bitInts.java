package org.litesoft.codec;

/**
 * Encodes and Decodes Base64url data (per <a href="https://datatracker.ietf.org/doc/html/rfc4648#section-5">RFC 4648 §5</a>) with No padding
 */
public class Base64urlCodec6bitInts {
    public static final int MASK = 0x3F;
    public static final String BAD_CHARACTER_PREFIX = "Character '";
    public static final String BAD_CHARACTER_SUFFIX = "' not valid for base64url(s)";

    public char encode( int bits6 ) {
        bits6 &= MASK;
        if ( bits6 < 26 ) {
            return toChar( 'A', 0, bits6 );
        }
        if ( bits6 < 52 ) {
            return toChar( 'a', 26, bits6 );
        }
        if ( bits6 < 62 ) {
            return toChar( '0', 52, bits6 );
        }
        return (bits6 == 62) ? '-' : '_';
    }

    public int decode( char c )
            throws IllegalArgumentException {
        if ( isCharBetween( c, 'A', 'Z' ) ) {
            return toInt( 'A', 0, c );
        }
        if ( isCharBetween( c, 'a', 'z' ) ) {
            return toInt( 'a', 26, c );
        }
        if ( isCharBetween( c, '0', '9' ) ) {
            return toInt( '0', 52, c );
        }
        if ( c == '-' ) {
            return 62;
        }
        if ( c == '_' ) {
            return 63;
        }
        throw new IllegalArgumentException( BAD_CHARACTER_PREFIX + c + BAD_CHARACTER_SUFFIX );
    }

    private static boolean isCharBetween( char c, char lowInclusive, char highInclusive ) {
        return (lowInclusive <= c) && (c <= highInclusive);
    }

    private static char toChar( char rangeBaseChar, int offset, int bits6 ) {
        return (char)(rangeBaseChar + (bits6 - offset));
    }

    private static int toInt( char rangeBaseChar, int offset, char sourceChar ) {
        return offset + (sourceChar - rangeBaseChar);
    }
}
