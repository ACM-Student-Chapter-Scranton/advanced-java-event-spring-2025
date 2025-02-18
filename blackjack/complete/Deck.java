import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;
    
    public Deck() {
        cards = new ArrayList<>();
    }

    public int getSize() {
        return cards.size();
    }

    public void add(Card card) {
        card.faceDown();
        cards.add(card);
    }

    public Card removeTopCard() {
        return cards.remove(cards.size() - 1);
    }

    public void shuffle() {
        // Here is a useful method to shuffle items in a Collection.
        Collections.shuffle(cards);
    }

    @Override
    public String toString() {
        return cards.toString();
    }
}
