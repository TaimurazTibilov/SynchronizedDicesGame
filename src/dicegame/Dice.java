package dicegame;

import java.util.Random;

/**
 * Класс кубика, позволяет получить случайное значение от 1 до 6
 *
 * @author Таймураз Тибилов
 */
public class Dice {
    public static final Random RANDOM = new Random();

    /**
     * Генератор случайных чисел от 1 до 6 включительно
     *
     * @return случ. число от 1 до 6
     */
    public int shoot() {
        return RANDOM.nextInt(6) + 1;
    }
}
