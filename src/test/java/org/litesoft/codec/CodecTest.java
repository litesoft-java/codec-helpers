package org.litesoft.codec;

import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class CodecTest {

    @BeforeEach
    void setup() {
        Codec.clearRegistration();
    }

    @Test
    void validateToEncode() {
        XY1Codec codex = new XY1Codec();
        expectedExceptionContains( "expected to be Significant", () -> codex.validateToEncode( null, 0 ) );
        expectedExceptionContains( "expected to be Significant", () -> codex.validateToEncode( "", 0 ) );
        expectedExceptionContains( "expected to be Significant", () -> codex.validateToEncode( "  ", 0 ) );
        expectedExceptionContains( codex.errorToEncodeTooShort( 5 ), () -> codex.validateToEncode( "four", 5 ) );
        Codec.clearRegistration();
    }

    @Test
    void validateToDecode() {
        XY1Codec codex = new XY1Codec();
        expectedExceptionContains( codex.errorDidNotStartWithCodexId(), () -> codex.validateToDecode( "XYZ" ) );
        expectedExceptionContains( codex.errorOnlyCodexId(), () -> codex.validateToDecode( "XY1:" ) );
        assertEquals( "Fred", codex.validateToDecode( "XY1:Fred" ) );
        Codec.clearRegistration();
    }

    @Test
    void constructor() {
        XY1Codec codex1 = new XY1Codec();
        XY1Codec codex2 = new XY1Codec();

        MyCodec1 codex3 = new MyCodec1( "X", "Y", 2 );

        expectedErrorContains( Codec.ERROR_DUPLICATE_REGISTRATION_MID_TEXT, () -> new MyCodec1( "X", "Y", 1 ) );

        expectedErrorContains( "codexGroup'" + Codec.ERROR_CODEX_ID_PART_CONTAINED_COLON_SUFFIX, () -> new MyCodec1( "X:", "Y", 1 ) );
        expectedErrorContains( "codexGroup'" + Codec.ERROR_CODEX_ID_PART_INSIGNIFICANT_SUFFIX, () -> new MyCodec1( "", "Y", 1 ) );
        expectedErrorContains( "codexSpecialization'" + Codec.ERROR_CODEX_ID_PART_CONTAINED_COLON_SUFFIX, () -> new MyCodec1( "X", "Y:", 1 ) );
        expectedErrorContains( "codexSpecialization'" + Codec.ERROR_CODEX_ID_PART_INSIGNIFICANT_SUFFIX, () -> new MyCodec1( "X", "", 1 ) );
        expectedErrorContains( "codexVersion'" + Codec.ERROR_CODEX_ID_PART_NOT_POSITIVE_SUFFIX, () -> new MyCodec1( "X", "Y", 0 ) );
        expectedErrorContains( "codexVersion'" + Codec.ERROR_CODEX_ID_PART_NOT_POSITIVE_SUFFIX, () -> new MyCodec1( "X", "Y", -1 ) );
        Codec.clearRegistration();
    }

    static class XY1Codec extends Codec {
        public XY1Codec() {
            super( "X", "Y", 1 );
        }
    }

    static class MyCodec1 extends Codec {
        public MyCodec1( String codexGroup, String codexSpecialization, int codexVersion ) {
            super( codexGroup, codexSpecialization, codexVersion );
        }
    }

    private void expectedExceptionContains( String errorTextContains, Supplier<String> supplier ) {
        try {
            String result = supplier.get();
            fail( "expected exception, but got: " + result );
        }
        catch ( IllegalArgumentException e ) {
            String msg = e.getMessage();
            if ( !msg.contains( errorTextContains ) ) {
                throw e;
            }
        }
    }

    private void expectedErrorContains( String errorTextContains, Supplier<Codec> supplier ) {
        try {
            Codec result = supplier.get();
            fail( "expected exception, but got: " + result );
        }
        catch ( Error e ) {
            String msg = e.getMessage();
            if ( !msg.contains( errorTextContains ) ) {
                throw new RuntimeException(e);
            }
        }
    }
}