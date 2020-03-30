package dicegame;

/**
 * Класс представляет собой комментатора игры в кости.
 * Он отслеживает ходы игроков и ведет счет игры, я вляется также
 * своего рода менеджером игры
 *
 * @author Таймураз Тибилов
 */
public class Commentator implements Runnable {
    private final GameBoard GAME_BOARD;
    private boolean isCommented = false;

    public Commentator(GameBoard gameBoard) {
        GAME_BOARD = gameBoard;
    }

    /**
     * Возвращает, успел ли комментатор прокомментировать ход текущего игрока
     *
     * @return прокомментирован ли ход
     */
    public synchronized boolean isCommented() {
        return isCommented;
    }

    /**
     * Переключает состояние прокомментированного хода
     */
    public synchronized void switchCommented() {
        isCommented = !isCommented;
    }

    /**
     * Объявляет результаты игры: турнирную таблицу и победившего игрока
     */
    private void commentResult() {
        System.out.println();
        System.out.println("Игра закончилась!!! Турнирная таблица: ");
        GAME_BOARD.getPlayersList().sort((x, y) -> {
            if (x.getWonGames() >= y.getWonGames())
                return -1;
            else
                return 1;
        });
        for (Player player : GAME_BOARD.getPlayersList()) {
            System.out.println("Игрок №" + player.NUMBER
                    + " выиграл " + player.getWonGames() + " игр; ");
        }
        System.out.println("Победил игрок №" + GAME_BOARD.getGameLeader().NUMBER);
    }

    /**
     * Комментирует ход текущего игрока - сколько очков набрал, стал ли лидером,
     * а также определяет конец раунда и игры
     *
     * @param current Объект текущего игрока, совершившего ход
     */
    private void comment(Player current) {
        if (current.getScore() == GAME_BOARD.MAX_SCORE) {
            System.out.println("Игрок №" + current.NUMBER
                    + " сделал ход и выбил максимум очков!");
            GAME_BOARD.setRoundLeader(current);
            GAME_BOARD.setRoundEnded();
        } else {
            System.out.println("Игрок №" + current.NUMBER
                    + " сделал ход и выбил " + current.getScore() + " очков!");
            if (GAME_BOARD.getRoundLeader() == null
                    || current.getScore() > GAME_BOARD.getRoundLeader().getScore()) {
                GAME_BOARD.setRoundLeader(current);
                System.out.println("Игрок №" + current.NUMBER + " становится лидером этого раунда!");
            }
            if (GAME_BOARD.isAllPlayed())
                GAME_BOARD.setRoundEnded();
        }
        if (GAME_BOARD.isRoundEnded()) {
            System.out.println("Раунд закончился! Победителем раунда стал игрок №"
                    + GAME_BOARD.getRoundLeader().NUMBER + ", который набрал "
                    + GAME_BOARD.getRoundLeader().getScore() + " очков!");
            GAME_BOARD.resetRound();
        }
    }

    /**
     * Запускает в фоне комментатора, который ждет, пока
     * текущий игрок сделает ход. Завершает работу, когда игра заканчивается
     */
    @Override
    public void run() {
        while (!GAME_BOARD.isGameEnded()) {
            synchronized (GAME_BOARD.NOTIFIER) {
                while (!GAME_BOARD.isPlayerChanged()) {
                    GAME_BOARD.NOTIFIER.notifyAll();
                    try {
                        GAME_BOARD.NOTIFIER.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                GAME_BOARD.resetPlayerChanged();
                Player current = GAME_BOARD.getCurrentPlayer();
                comment(current);
                switchCommented();
            }
        }
        commentResult();
    }
}
