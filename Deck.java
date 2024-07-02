import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record Deck(List<Card> cards) {
    public Deck(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        return cards.removeFirst();
    }

    @Override
    public List<Card> cards() {
        return new ArrayList<>(cards);
    }
}


