package dicegame;

/**
 * Класс игрока, каждый игрок реализует интерфейс для
 * выполнение бросков кубика в игре в своем потоке
 *
 * @author Таймураз Тибилов
 */
public class Player implements Runnable {
    private final GameBoard GAME_BOARD;
    public final String NUMBER;
    private int score = 0;
    private int wonGames = 0;
    private boolean isAlreadyShot = false;

    public Player(GameBoard gameBoard, String num) {
        GAME_BOARD = gameBoard;
        NUMBER = num;
    }

    /**
     * Обновляет значение счета на указанное значение
     *
     * @param update значение обновления счета
     */
    public synchronized void updateScore(int update) {
        score += update;
    }

    /**
     * Возвращает значение счета текущего раунда
     *
     * @return счет игрока в текущем раунде
     */
    public synchronized int getScore() {
        return score;
    }

    /**
     * Обнуляет счет игрока
     */
    public synchronized void resetScore() {
        score = 0;
    }

    /**
     * Инкрементирует число выигранных раундов
     */
    public synchronized void win() {
        wonGames++;
    }

    /**
     * Возвращает число выигранных раундов
     *
     * @return число выигранных раундов
     */
    public synchronized int getWonGames() {
        return wonGames;
    }

    /**
     * Переключает значение - бросил ли игрок кубики или нет
     */
    public synchronized void setShot() {
        isAlreadyShot = !isAlreadyShot;
    }

    /**
     * Возвращает значение - бросил ли игрок кубики или нет
     *
     * @return бросил ли игрок кубики или нет
     */
    public synchronized boolean isAlreadyShot() {
        return isAlreadyShot;
    }

    /**
     * Запускает последовательно ходы игрока, пока игра не закончится и
     * кто-либо из игроков не выиграет. Все игроки представляют собой
     * потоки-демоны для корректого завершения программы
     */
    @Override
    public void run() {
        while (!GAME_BOARD.isGameEnded()) {
            synchronized (GAME_BOARD.DICES) {
                while (GAME_BOARD.isRoundEnded() || isAlreadyShot) {
                    GAME_BOARD.DICES.notifyAll();
                    try {
                        GAME_BOARD.DICES.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                GAME_BOARD.playerStartsPlay(this);
                GAME_BOARD.setPlayerChanged();
                for (Dice dice : GAME_BOARD.DICES)
                    updateScore(dice.shoot());

                synchronized (GAME_BOARD.NOTIFIER) {
                    setShot();
                    GAME_BOARD.NOTIFIER.notifyAll();
                    while (!GAME_BOARD.COMMENTATOR.isCommented()) {
                        try {
                            GAME_BOARD.NOTIFIER.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    GAME_BOARD.COMMENTATOR.switchCommented();
                }
            }
        }
    }
}
