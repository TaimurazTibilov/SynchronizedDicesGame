package dicegame;

/**
 * Интерфейс запуска игры (не знаю, зачем он здесь, но пусть будет)
 *
 * @author Таймураз Тибилов
 */
@FunctionalInterface
public interface GameLauncher {
    /**
     * Метод запуска игры
     */
    void startGame();
}
