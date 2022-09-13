package org.litesoft.codec;

import java.util.function.IntSupplier;

import org.litesoft.annotations.NotNull;

public class AbstractCodecWithRandomSeed extends Codec {
    protected final IntSupplier randomizingSeedSupplier;

    protected AbstractCodecWithRandomSeed( IntSupplier randomizingSeedSupplier,
                                           String codecGroup, String codecSpecialization, int codecVersion ) {
        super( codecGroup, codecSpecialization, codecVersion );
        this.randomizingSeedSupplier = NotNull.AssertArgument.namedValue( "randomizingSeedSupplier", randomizingSeedSupplier );
    }
}
