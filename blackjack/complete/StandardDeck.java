public class StandardDeck extends Deck {
    
    public StandardDeck() {
        for (FrenchSuit suit : FrenchSuit.values()) {
            for (StandardRank rank : StandardRank.values()) {
                Card card = new Card(rank, suit);
                this.add(card);
            }
        }
    }
}
