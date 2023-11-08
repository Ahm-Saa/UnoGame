public class Card {
    public enum Type { NUMBER, SKIP, REVERSE, DRAW_TWO, WILD, WILD_DRAW_FOUR }

    private Color color;
    private Value value;

    public Card(Color color, Value value) {
        this.color = color;
        this.value = value;
    }

    public Color getColor() {
        return color;
    }

    public Value getValue() {
        return value;
    }

    public void setColor(Color color) {
        this.color = color;
    }


    public boolean isValidMove(Card topCard) {
        if (color == Color.BLACK || topCard.getColor() == color || topCard.getValue() == value) {
            return true;
        }
        return false;
    }

    public Type getType() {
        if (color == Color.BLACK) {
            if (value == Value.WILD_DRAW_FOUR) {
                return Type.WILD_DRAW_FOUR;
            } else {
                return Type.WILD;
            }
        }
        switch (value) {
            case SKIP:
                return Type.SKIP;
            case REVERSE:
                return Type.REVERSE;
            case DRAW_TWO:
                return Type.DRAW_TWO;
            default:
                return Type.NUMBER;
        }
    }

    @Override
    public String toString() {
        return color + " " + value;
    }
}