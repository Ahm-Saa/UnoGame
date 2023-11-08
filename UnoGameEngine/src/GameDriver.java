public class GameDriver {
    public static void main(String[] args) {
        Game game = new CustomGameDecorator(new BasicUnoGame());
        game.play();
    }
}