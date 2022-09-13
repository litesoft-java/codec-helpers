package org.litesoft.codec;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class Base64UrlCodec6BitIntsTest {
    @SuppressWarnings("SpellCheckingInspection")
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";

    final Base64urlCodec6bitInts codex = new Base64urlCodec6bitInts();

    @Test
    void roundTripAllValidOptions() {
        assertEquals( 64, CHARS.length() );
        for ( int i = 0; i < CHARS.length(); i++ ) {
            char c = CHARS.charAt( i );
            int decoded = codex.decode( c );
            assertEquals( i, decoded, "decoded '" + c + "'" );
            char cEncoded = codex.encode( decoded );
            assertEquals( c, cEncoded, "encode(" + decoded + ")" );
        }
    }

    @Test
    void testBadAsciiCharacters() {
        for ( int i = 0; i <= 127; i++ ) {
            char c = (char)i;
            if ( -1 == CHARS.indexOf( c ) ) {// Not in CHARS
                try {
                    int decoded = codex.decode( c );
                    fail( "Expected exception, but got " + decoded + " from (int of character): " + i );
                }
                catch ( IllegalArgumentException expected ) {
                    String msg = expected.getMessage();
                    if ( !msg.startsWith( Base64urlCodec6bitInts.BAD_CHARACTER_PREFIX ) ||
                         !msg.endsWith( Base64urlCodec6bitInts.BAD_CHARACTER_SUFFIX ) ) {
                        throw expected;
                    }
                }
            }
        }
    }
}