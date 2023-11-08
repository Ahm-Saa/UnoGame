import java.util.ArrayList;
import java.util.List;

public class DiscardPile {
    private List<Card> cards;

    public DiscardPile() {
        this.cards = new ArrayList<>();
    }

    public Card getTopCard() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.get(cards.size() - 1);
    }

    public void addCard(Card card) {
        cards.add(card);
    }
}
