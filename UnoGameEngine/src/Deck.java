import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Deck {
    private List<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();
    }

    public void initialize() {
        cards.clear();

        // Add number cards
        for (Color color : Color.values()) {
            if (color != Color.BLACK) {
                for (int i = 0; i <= 9; i++) {
                    cards.add(new Card(color, Value.values()[i]));
                    cards.add(new Card(color, Value.values()[i]));
                }
            }
        }

        // Add action cards
        for (Color color : Color.values()) {
            if (color != Color.BLACK) {
                cards.add(new Card(color, Value.SKIP));
                cards.add(new Card(color, Value.SKIP));
                cards.add(new Card(color, Value.REVERSE));
                cards.add(new Card(color, Value.REVERSE));
                cards.add(new Card(color, Value.DRAW_TWO));
                cards.add(new Card(color, Value.DRAW_TWO));
            }
        }

        // Add Wild and Wild Draw Four cards
        for (int i = 0; i < 4; i++) {
            cards.add(new Card(Color.BLACK, Value.WILD));
            cards.add(new Card(Color.BLACK, Value.WILD_DRAW_FOUR));
        }
    }


    public void shuffle() {
        Random random = new Random();
        for (int i = 0; i < cards.size(); i++) {
            int j = random.nextInt(cards.size());
            Card temp = cards.get(i);
            cards.set(i, cards.get(j));
            cards.set(j, temp);
        }
    }

    public Card drawCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("No cards are available. The deck is empty.");
        }
        return cards.remove(cards.size() - 1);
    }


    public void addCardToBottom(Card card) {
        cards.add(0, card);
    }

    public int getNumRemainingCards() {
        return cards.size();
    }
}
