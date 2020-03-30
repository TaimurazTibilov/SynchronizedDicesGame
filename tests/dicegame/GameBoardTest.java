package dicegame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {
    private GameBoard gb1;
    private GameBoard gb2;
    private GameBoard gb3;

    @BeforeEach
    void setUp() {
        gb1 = new GameBoard(1, 1, 1);
        gb2 = new GameBoard(6, 5, 100);
        gb3 = new GameBoard(3, 3, 3);
    }

    @Test
    void getPlayersList() {
        assert gb1.getPlayersList().size() == gb1.NUMBER_OF_PLAYERS;
        assert gb2.getPlayersList().size() == gb2.NUMBER_OF_PLAYERS;
        assert gb3.getPlayersList().size() == gb3.NUMBER_OF_PLAYERS;
    }

    @Test
    void setPlayerChanged() {
        gb1.resetPlayerChanged();
        boolean first = gb1.isPlayerChanged();

        gb1.setPlayerChanged();
        boolean second = gb1.isPlayerChanged();

        gb1.resetPlayerChanged();
        boolean third = gb1.isPlayerChanged();
        assert !first && second;
        assert !(first || third);
    }

    @Test
    void setRoundEnded() {
        boolean fir = gb1.isRoundEnded();
        gb1.setRoundEnded();
        boolean sec = gb1.isRoundEnded();
        assert fir != sec;
    }

    @Test
    void getCurrentPlayer() {
        Player pl = new Player(gb1, "1");
        gb1.playerStartsPlay(pl);
        Player pl2 = new Player(gb1, "2");
        gb1.playerStartsPlay(pl2);

        assert gb1.getCurrentPlayer() == pl2;
        assert gb1.getCurrentPlayer() != pl;
    }

    @Test
    void setRoundLeader() {
        Player pl = new Player(gb1, "1");
        gb1.setRoundLeader(pl);
        Player pl2 = new Player(gb1, "2");
        gb1.setRoundLeader(pl2);

        assert gb1.getRoundLeader() == pl2;
        assert gb1.getRoundLeader() != pl;
    }

}