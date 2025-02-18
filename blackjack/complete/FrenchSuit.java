public enum FrenchSuit implements Suit {
    // Here are fancy symbols if you want to use them!
    // ♠♥♦♣
    
    // TODO create enums for spades, hearts, diamonds, and clubs
    SPADES("♠"),
    HEARTS("♥"),
    DIAMONDS("♦"),
    CLUBS("♣");
    
    private final String symbol;
    
    FrenchSuit(String symbol) {
        this.symbol = symbol;
    }
    
    @Override
    public String symbol() {
        return symbol;
    }
}
