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
        XY1Codec codec = new XY1Codec();
        expectedExceptionContains( "expected to be Significant", () -> codec.validateToEncode( null, 0 ) );
        expectedExceptionContains( "expected to be Significant", () -> codec.validateToEncode( "", 0 ) );
        expectedExceptionContains( "expected to be Significant", () -> codec.validateToEncode( "  ", 0 ) );
        expectedExceptionContains( codec.errorToEncodeTooShort( 5 ), () -> codec.validateToEncode( "four", 5 ) );
        Codec.clearRegistration();
    }

    @Test
    void validateToDecode() {
        XY1Codec codec = new XY1Codec();
        expectedExceptionContains( codec.errorDidNotStartWithCodecId(), () -> codec.validateToDecode( "XYZ" ) );
        expectedExceptionContains( codec.errorOnlyCodecId(), () -> codec.validateToDecode( "XY1:" ) );
        assertEquals( "Fred", codec.validateToDecode( "XY1:Fred" ) );
        Codec.clearRegistration();
    }

    @Test
    void constructor() {
        XY1Codec codec1 = new XY1Codec();
        XY1Codec codec2 = new XY1Codec();

        MyCodec1 codec3 = new MyCodec1( "X", "Y", 2 );

        expectedErrorContains( Codec.ERROR_DUPLICATE_REGISTRATION_MID_TEXT, () -> new MyCodec1( "X", "Y", 1 ) );

        expectedErrorContains( "codecGroup'" + Codec.ERROR_CODEX_ID_PART_CONTAINED_COLON_SUFFIX, () -> new MyCodec1( "X:", "Y", 1 ) );
        expectedErrorContains( "codecGroup'" + Codec.ERROR_CODEX_ID_PART_INSIGNIFICANT_SUFFIX, () -> new MyCodec1( "", "Y", 1 ) );
        expectedErrorContains( "codecSpecialization'" + Codec.ERROR_CODEX_ID_PART_CONTAINED_COLON_SUFFIX, () -> new MyCodec1( "X", "Y:", 1 ) );
        expectedErrorContains( "codecSpecialization'" + Codec.ERROR_CODEX_ID_PART_INSIGNIFICANT_SUFFIX, () -> new MyCodec1( "X", "", 1 ) );
        expectedErrorContains( "codecVersion'" + Codec.ERROR_CODEX_ID_PART_NOT_POSITIVE_SUFFIX, () -> new MyCodec1( "X", "Y", 0 ) );
        expectedErrorContains( "codecVersion'" + Codec.ERROR_CODEX_ID_PART_NOT_POSITIVE_SUFFIX, () -> new MyCodec1( "X", "Y", -1 ) );
        Codec.clearRegistration();
    }

    static class XY1Codec extends Codec {
        public XY1Codec() {
            super( "X", "Y", 1 );
        }
    }

    static class MyCodec1 extends Codec {
        public MyCodec1( String codecGroup, String codecSpecialization, int codecVersion ) {
            super( codecGroup, codecSpecialization, codecVersion );
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
                throw new RuntimeException( e );
            }
        }
    }
}