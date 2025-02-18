public class Card {
    private final Suit suit;
    private final Rank rank;
    private boolean isFaceUp;
    
    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.isFaceUp = true;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    public void faceUp() {
        isFaceUp = true;
    }

    public void faceDown() {
        isFaceUp = false;
    }

    @Override
    public String toString() {
        if (isFaceUp) {
            return rank.symbol() + suit.symbol();
        } else {
            return "??";
        }
    }

    /*
     * This is complicated, but Java requires a certain contract when it comes to the equals method.
     * If it is obj is null, it must return false.
     * Calls on equals of the same object must be reflexive, symmetric, transitive, and consistent.
     * The hashCode must also be equal for two objects to be equal, so that is why we are also checking that.
     * Many things like Collections depend on calling the equals method and the hashCode method, so that is why
     * we are overriding them.
     * Look on the Javadocs for the toString method for further explanation.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj != null && this.getClass() == obj.getClass() && this.hashCode() == obj.hashCode()) {
            Card otherCard = (Card) obj;
            return this.suit == otherCard.suit && this.rank == otherCard.rank;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
}
