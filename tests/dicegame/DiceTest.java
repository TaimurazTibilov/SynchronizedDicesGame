package dicegame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiceTest {

    @Test
    void shoot() {
        Dice dice = new Dice();
        for (int i = 0; i < 1000; i++) {
            assertTrue(0 < dice.shoot() && dice.shoot() < 7);
        }
    }
}