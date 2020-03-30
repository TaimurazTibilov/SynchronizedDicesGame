package dicegame;

public class StartGame {
    public static void main(String[] args) throws Exception {
//        args = new String[] {"5", "3", "100"};
        int players_number;
        if (args.length > 0) {
            players_number = Integer.parseInt(args[0]);
            if (players_number < 2 || players_number > 6)
                throw new IllegalArgumentException("Invalid number of players");
        }
        else
            throw new IllegalArgumentException("Number of players wasn't given");

        int dices_number;
        if (args.length > 1) {
            dices_number = Integer.parseInt(args[1]);
            if (dices_number < 2 || dices_number > 5)
                throw new IllegalArgumentException("Invalid number of dices");
        }
        else
            throw new IllegalArgumentException("Number of dices wasn't given");

        int round_number;
        if (args.length > 2) {
            round_number = Integer.parseInt(args[2]);
            if (round_number < 1 || round_number > 100)
                throw new IllegalArgumentException("Invalid number of rounds");
        }
        else
            throw new IllegalArgumentException("Number of rounds wasn't given");

        new GameBoard(players_number, dices_number, round_number).startGame();
    }
}
