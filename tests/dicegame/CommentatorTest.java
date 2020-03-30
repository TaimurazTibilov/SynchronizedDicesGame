package dicegame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class CommentatorTest {

    private GameBoard gb = new GameBoard(0, 0, 0);
    private Commentator com;
    @BeforeEach
    void setUp() {
        com = new Commentator(gb);
    }

    @Test
    void isCommented() {
        assertFalse(com.isCommented());

        com.switchCommented();
        assertTrue(com.isCommented());
    }
}