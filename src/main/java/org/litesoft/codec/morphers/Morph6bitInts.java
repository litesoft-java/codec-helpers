package org.litesoft.codec.morphers;

import org.litesoft.annotations.PackageFriendlyForTesting;

/**
 * Support rolling XOR for symmetric morphing.
 */
public class Morph6bitInts {
    public static final int MASK = 0x3F; // 6 bits!
    @PackageFriendlyForTesting
    int last6bits = 0;

    public int toMorphed( int orig6bits ) {
        orig6bits &= MASK;
        int morphed = last6bits ^ orig6bits;
        last6bits = morphed;
        return morphed;
    }

    public int fromMorphed( int morphed6bits ) {
        morphed6bits &= MASK;
        int unmorphed = last6bits ^ morphed6bits;
        last6bits = morphed6bits;
        return unmorphed;
    }
}
