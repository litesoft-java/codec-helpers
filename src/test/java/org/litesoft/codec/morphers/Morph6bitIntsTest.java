package org.litesoft.codec.morphers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Morph6bitIntsTest {

    @Test
    void roundTrip() {
        Morph6bitInts encoder = new Morph6bitInts();
        Morph6bitInts decoder = new Morph6bitInts();

        int firstBits = 21;

        int morphed = encoder.toMorphed( firstBits );
        int unmorphed = decoder.fromMorphed( morphed );
        assertEquals( firstBits, unmorphed ); // bits round-tripped don't change!

        assertEquals( firstBits, morphed ); // first bits don't change!
        assertEquals( morphed, unmorphed ); // first bits don't change!

        for ( int bits = Morph6bitInts.MASK; 0 <= bits; bits-- ) {
            int encoderLast = encoder.last6bits;
            int decoderLast = decoder.last6bits;
            assertEquals( encoderLast, decoderLast ); // should stay in sync!

            morphed = encoder.toMorphed( bits );
            assertTrue( morphed <= Morph6bitInts.MASK );
            unmorphed = decoder.fromMorphed( morphed );
            assertEquals( bits, unmorphed ); // bits round-tripped don't change!

            if ( encoderLast == 0 ) {
                assertEquals( bits, morphed ); // 0 last -> bits don't change!
                assertEquals( morphed, unmorphed ); // 0 last -> bits don't change!
            } else {
                assertNotEquals( bits, morphed ); // bits have changed!
                assertNotEquals( morphed, unmorphed ); // bits have changed!
            }
        }
    }
}