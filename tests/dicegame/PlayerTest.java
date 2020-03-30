package dicegame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    Player pl = new GameBoard(1, 1, 1).getPlayersList().get(0);

    @Test
    void updateScore() {
        int x = pl.getScore();
        pl.updateScore(42);

        assert pl.getScore() - x == 42;

        pl.resetScore();
        assert pl.getScore() == x;
    }

    @Test
    void getWonGames() {
        int x = pl.getWonGames();
        pl.win();

        assert pl.getWonGames() - x == 1;
    }

    @Test
    void setShot() {
        boolean x = pl.isAlreadyShot();
        pl.setShot();
        boolean y = pl.isAlreadyShot();
        pl.setShot();

        assert x == pl.isAlreadyShot();
        assert y != pl.isAlreadyShot();
    }
}