package bowling.domain.core;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class FallenPinsTest {
    @DisplayName("단일객체만 생성하는 지 확인")
    @Test
    void singleton() {
        FallenPins fallenPins = FallenPins.of(0);
        FallenPins fallenPins2 = FallenPins.of(0);
        assertThat(fallenPins == fallenPins2).isTrue();
        assertThat(fallenPins.equals(fallenPins2)).isTrue();
    }

    @DisplayName("잘못 설정된 넘어진 핀 개수를 설정하면 오류 발생 유무 확인")
    @ParameterizedTest
    @ValueSource(ints = {-1,11})
    void wrongFellenPinsCount(final int wrongPins) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> FallenPins.of(wrongPins))
            .withMessage(FallenPins.ERROR_MESSAGE);
    }

    @DisplayName("strike 유무 확인")
    @Test
    void isStrike() {
        assertThat(TwoFallenPins.mutable().firstFallenPins(0).isStrike()).isFalse();
        assertThat(TwoFallenPins.mutable().firstFallenPins(10).isStrike()).isTrue();
    }

    static Stream<Arguments> provideSpareTestCase(){
        return Stream.of(
            Arguments.of(1, 9, true)
            , Arguments.of(2, 8, true)
            , Arguments.of(5, 5, true)
            , Arguments.of(9, 1, true)
            , Arguments.of(4, 6, true)
            , Arguments.of(2, 0, false)
            , Arguments.of(3, 2, false)
            , Arguments.of(1, 7, false)
            , Arguments.of(0, 7, false)
            , Arguments.of(0, 0, false)
        );
    }

    @DisplayName("spare 유무 확인")
    @ParameterizedTest
    @MethodSource("provideSpareTestCase")
    void isSpare(int first, int second, boolean result) {

        TwoFallenPins towFallenPins = TwoFallenPins.mutable()
                                                   .collect(first, second);

        assertThat(towFallenPins.isSpare()).isEqualTo(result);
    }

    static Stream<Arguments> provideMissTestCase(){
        return Stream.of(
            Arguments.of(1, 9, false)
            , Arguments.of(2, 8, false)
            , Arguments.of(5, 5, false)
            , Arguments.of(9, 1, false)
            , Arguments.of(4, 6, false)
            , Arguments.of(2, 0, true)
            , Arguments.of(3, 2, true)
            , Arguments.of(1, 7, true)
            , Arguments.of(0, 7, true)
            , Arguments.of(0, 0, false)
        );
    }

    @DisplayName("miss 유무 확인")
    @ParameterizedTest
    @MethodSource("provideMissTestCase")
    void isMiss(int first, int second, boolean result) {
        TwoFallenPins towFallenPins = TwoFallenPins.mutable()
                                                   .collect(first, second);
        assertThat(towFallenPins.isMiss()).isEqualTo(result);
    }

    @DisplayName("gutter 유무 확인")
    @Test
    void isGutter() {
        assertThat(TwoFallenPins.mutable().firstFallenPins(0).isGutter()).isTrue();
        assertThat(TwoFallenPins.mutable().firstFallenPins(10).isGutter()).isFalse();

        assertThat(TwoFallenPins.mutable().firstFallenPins(0).secondFallenPins(0).isGutter()).isTrue();
        assertThat(TwoFallenPins.mutable().firstFallenPins(0).secondFallenPins(10).isGutter()).isFalse();

        TwoFallenPins towFallenPins = TwoFallenPins.mutable()
                                                   .collect(0, 0);

        assertThat(towFallenPins.isGutter()).isTrue();
    }
}
