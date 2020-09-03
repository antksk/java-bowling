package bowling.domain.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static bowling.domain.core.RolledStateFactory.firstBowl;
import static bowling.domain.core.RolledStateFactory.secondBowl;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("두번의 투구로 볼링 결과 테스트")
class RolledResultTest {
    @DisplayName("첫번째 투구가 잘못된 경우 확인")
    @Test
    void firstBowlTest() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> firstBowl(Integer.MIN_VALUE))
            .withMessage(Pins.ERROR_MESSAGE);

        assertThatIllegalArgumentException()
            .isThrownBy(() -> firstBowl(Integer.MAX_VALUE))
            .withMessage(Pins.ERROR_MESSAGE);
    }

    @DisplayName("첫번째 투구후 남은 핀수를 기준으로, 두번째 투구가 잘못된 경우 확인")
    @Test
    void secondBowlTest() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> secondBowl(firstBowl(5), 6))
            .withMessage(RolledStateFactory.ERROR_MESSAGE_SECOND_BOWL);

        assertThatIllegalArgumentException()
            .isThrownBy(() -> secondBowl(firstBowl(0), 11))
            .withMessage(RolledStateFactory.ERROR_MESSAGE_SECOND_BOWL);
    }

    @DisplayName("첫번째 투구가 스트라이크이면, 두번째 투구는 의미 없음")
    @Test
    void strike() {
        RolledResult firstBowlRolledResult = firstBowl(10);
        assertThat(firstBowlRolledResult.description()).isEqualTo("X");

        // 첫번째 투구가 스트라이크이면, 두번째 투구의 결과는 의미 없이 스트라이크로 처리됨
        assertThat(secondBowl(firstBowlRolledResult, 10) == firstBowlRolledResult).isTrue();
    }

    @DisplayName("첫번째 투구가 스트라이크가 아니면, 두번째 투구 결과가 확인이 필요함")
    @Test
    void incompleteState() {
        assertThat(firstBowl(0).description()).isEqualTo("-|?");
        assertThat(firstBowl(5).description()).isEqualTo("5|?");
    }

    @DisplayName("두번째 투구 결과로 스페어 처리되었는지 확인")
    @Test
    void spare() {
        assertThat(secondBowl(firstBowl(0), 10).description()).isEqualTo("-|/");

        RolledResult rolledResult = secondBowl(firstBowl(5), 5);
        assertThat(rolledResult.description()).isEqualTo("5|/");

        secondBowlRolledResultConfrim(rolledResult);
    }

    @DisplayName("두번째 투구 결과로 미스 처리되었는지 확인")
    @Test
    void miss() {
        assertThat(secondBowl(firstBowl(1), 0).description()).isEqualTo("1|-");
        assertThat(secondBowl(firstBowl(0), 1).description()).isEqualTo("-|1");

        RolledResult firstBowlRolledResult = firstBowl(5);
        RolledResult rolledResult = secondBowl(firstBowlRolledResult, 2);
        assertThat(rolledResult.description()).isEqualTo("5|2");

        secondBowlRolledResultConfrim(rolledResult);
    }

    @DisplayName("두번의 투구로 한개의 핀도 처리하지 못한 경우")
    @Test
    void gutter(){
        assertThat(secondBowl(firstBowl(0), 0).description()).isEqualTo("-");
    }

    // 두번째 투구가 완료된 결과는 2번(zero base)의의 쓰러진 핀 갯수가 나옴, 잘못된 투구 회수를 입력했는지 확인
    private void secondBowlRolledResultConfrim(RolledResult rolledResult) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> rolledResult.countOfFallenPinsByRolls(Integer.MIN_VALUE))
            .withMessage(Spare.ERROR_MESSAGE);
        assertThatIllegalArgumentException()
            .isThrownBy(() -> rolledResult.countOfFallenPinsByRolls(Integer.MAX_VALUE))
            .withMessage(Spare.ERROR_MESSAGE);
    }
}
