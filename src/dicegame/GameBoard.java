package dicegame;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляет собой игровое поле.
 * Содержит функциональные поля для корректой работы и синхронизации потоков,
 * а также перезапускает уровни
 *
 * @author Таймураз Тибилов
 */
public class GameBoard implements GameLauncher {

    public final int NUMBER_OF_PLAYERS;
    public final int NUMBER_OF_DICES;
    public final int NUMBER_OF_ROUNDS;
    public final int MAX_SCORE;

    public final Commentator COMMENTATOR = new Commentator(this);
    public final List<Dice> DICES = new ArrayList<>();
    private final List<Player> PLAYERS = new ArrayList<>();
    public final Object NOTIFIER = new Object();

    private int notShotPlayers;
    private boolean isEndOfRound = false;
    private boolean isPlayerChanged = false;
    private Player currentPlayer;
    private Player roundLeader;
    private Player gameLeader;

    /**
     * Инициализирует поля для игры
     *
     * @param noP количество игроков
     * @param noD количество костей
     * @param noR количество раундов до победы
     */
    public GameBoard(int noP, int noD, int noR) {
        NUMBER_OF_PLAYERS = noP;
        notShotPlayers = noP;
        NUMBER_OF_DICES = noD;
        MAX_SCORE = noD * 6;
        NUMBER_OF_ROUNDS = noR;
        for (int i = 0; i < noD; i++)
            DICES.add(new Dice());
        for (int i = 0; i < noP; i++)
            PLAYERS.add(new Player(this, String.valueOf(i + 1)));
    }

    /**
     * Возвращает список игроков
     *
     * @return список игроков
     */
    public synchronized List<Player> getPlayersList() {
        return PLAYERS;
    }

    /**
     * Устанавливает флаг сменившегося хода
     */
    public synchronized void setPlayerChanged() {
        isPlayerChanged = true;
    }

    /**
     * Возвращает, сменился ли ход и текущий игрок
     *
     * @return флаг сменившегося хода
     */
    public synchronized boolean isPlayerChanged() {
        return isPlayerChanged;
    }

    /**
     * Сбрасывает флаг сменившегося хода
     */
    public synchronized void resetPlayerChanged() {
        isPlayerChanged = false;
    }

    /**
     * Ставит флаг закончившегося раунда
     */
    public synchronized void setRoundEnded() {
        isEndOfRound = true;
    }

    /**
     * Возвращает истину, если раунд закончился
     *
     * @return флаг закончившегося раунда
     */
    public synchronized boolean isRoundEnded() {
        return isEndOfRound;
    }

    /**
     * Возвращает истину, если все игроки сделали свой ход
     *
     * @return все ли игроки бросили кубики
     */
    public synchronized boolean isAllPlayed() {
        return notShotPlayers == 0;
    }

    /**
     * Возвращает истину, если игра завершилась
     *
     * @return завершилась ли игра
     */
    public synchronized boolean isGameEnded() {
        return gameLeader != null && NUMBER_OF_ROUNDS == gameLeader.getWonGames();
    }

    /**
     * Возвращает игрока, чей ход в раунде является текущим
     *
     * @return текущий игрок
     */
    public synchronized Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Задает начальные значения для игрока, если к ему перешел ход
     *
     * @param player игрок, к которому перешел ход
     */
    public synchronized void playerStartsPlay(Player player) {
        currentPlayer = player;
        if (--notShotPlayers == 0)
            setRoundEnded();
    }

    /**
     * Перезапускает начальные значения после завершения раунда,
     * а также запоминает текущего лидера игры
     */
    public synchronized void resetRound() {
        notShotPlayers = NUMBER_OF_PLAYERS;
        isEndOfRound = false;
        roundLeader.win();
        if (gameLeader == null || gameLeader.getWonGames() < roundLeader.getWonGames())
            gameLeader = roundLeader;
        for (Player player : PLAYERS) {
            if (player.isAlreadyShot())
                player.setShot();
            player.resetScore();
        }
    }

    /**
     * Записывает нового лидера текущего раунда
     *
     * @param leader игрок, ставший текущим лидером
     */
    public synchronized void setRoundLeader(Player leader) {
        roundLeader = leader;
    }

    /**
     * Возвращает текущего лидера раунда
     *
     * @return текущий лидер раунда
     */
    public synchronized Player getRoundLeader() {
        return roundLeader;
    }

    /**
     * Возвращает текущего лидера игры
     *
     * @return текущий лидер игры
     */
    public synchronized Player getGameLeader() {
        return gameLeader;
    }

    /**
     * Запускает игру. Игроки и комментатор представляют собой
     * отдельные исполняемые потоки
     */
    @Override
    public void startGame() {
        Thread commentator = new Thread(COMMENTATOR);
        commentator.start();
        for (Player player : PLAYERS) {
            Thread thr = new Thread(player);
            thr.setDaemon(true);
            thr.start();
        }
    }
}
