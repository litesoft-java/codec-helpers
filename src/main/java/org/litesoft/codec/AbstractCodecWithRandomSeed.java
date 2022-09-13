package org.litesoft.codec;

import java.util.function.IntSupplier;

import org.litesoft.annotations.NotNull;

public class AbstractCodecWithRandomSeed extends Codec {
    protected final IntSupplier randomizingSeedSupplier;

    protected AbstractCodecWithRandomSeed( IntSupplier randomizingSeedSupplier,
                                           String codexGroup, String codexSpecialization, int codexVersion ) {
        super( codexGroup, codexSpecialization, codexVersion );
        this.randomizingSeedSupplier = NotNull.AssertArgument.namedValue( "randomizingSeedSupplier", randomizingSeedSupplier );
    }
}
