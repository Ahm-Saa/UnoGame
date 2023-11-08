public abstract class GameDecorator implements Game {
    private Game decoratedGame;

    public GameDecorator(Game decoratedGame) {
        this.decoratedGame = decoratedGame;
    }

    public void play() {
        decoratedGame.play();
    }
}