import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BasicUnoGame implements Game {
    private static final int INITIAL_HAND_SIZE = 7;

    private Deck deck;
    private DiscardPile discardPile;
    private List<Player> players;
    private int currentPlayerIndex;
    private boolean direction;

    public BasicUnoGame() {
        this.deck = new Deck();
        this.discardPile = new DiscardPile();
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.direction = true; // true for clockwise, false for counterclockwise
    }

    public void play() {
        initializeGame();

        while (!gameEnded()) {
            Player currentPlayer = getCurrentPlayer();
            playTurn(currentPlayer);

            if (currentPlayer.hasUno()) {
                System.out.println(currentPlayer.getName() + " says: Uno!");
            }

            if (currentPlayer.hasEmptyHand()) {
                System.out.println(currentPlayer.getName() + " wins!");
                break;
            }

            switchPlayer();
        }
    }

    private void initializeGame() {
        Scanner scanner = new Scanner(System.in);

        // Handle number of players input exception
        int numPlayers = 0;
        boolean validNumPlayers = false;
        while (!validNumPlayers) {
            System.out.print("Enter the number of players (2-10): ");
            String input = scanner.nextLine();

            try {
                numPlayers = Integer.parseInt(input);
                if (numPlayers >= 2 && numPlayers <= 10) {
                    validNumPlayers = true;
                } else {
                    System.out.println("Invalid input! Please enter a number between 2 and 10.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
            }
        }

        for (int i = 1; i <= numPlayers; i++) {
            // Handle player name input exception
            String name = "";
            boolean validName = false;
            while (!validName) {
                System.out.print("Enter the name of Player " + i + ": ");
                name = scanner.nextLine();

                if (name.matches("[a-zA-Z]+")) {
                    validName = true;
                } else {
                    System.out.println("Invalid input! Player name should only contain letters.");
                }
            }

            players.add(new Player(name));
        }

        deck.initialize();
        deck.shuffle();

        for (Player player : players) {
            dealCards(player, INITIAL_HAND_SIZE);
            //System.out.println(player.getName() + "'s hand: " + player.getHand());
        }


        Card topCard = deck.drawCard();
        while (topCard.getType() == Card.Type.WILD) {
            deck.addCardToBottom(topCard);
            topCard = deck.drawCard();
        }
        discardPile.addCard(topCard);
        System.out.println("Top card of the discard pile: " + topCard);
    }

    private void dealCards(Player player, int numCards) {
        for (int i = 0; i < numCards; i++) {
            try {
                Card card = deck.drawCard();
                player.drawCard(card);
            } catch (IllegalStateException e) {
                System.out.println("No cards are available. The deck is empty.");
                break; // Exit the loop if the deck is empty
            }
        }
    }


    private Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    private void switchPlayer() {
        if (direction) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } else {
            currentPlayerIndex = (currentPlayerIndex - 1 + players.size()) % players.size();
        }
    }

    private boolean gameEnded() {
        for (Player player : players) {
            if (player.hasEmptyHand()) {
                return true;
            }
        }
        return false;
    }

    public void playTurn(Player player) {
        System.out.println("--------");
        System.out.println("It's " + player.getName() + "'s turn");
        System.out.println(player.getName() + "'s hand: " + player.getHand()); // Print player's hand

        Scanner scanner = new Scanner(System.in);
        boolean drawInputValid = false;
        while (!drawInputValid) {
            System.out.print("Choose a card to play (or enter 'draw' to draw a card): ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("draw")) {
                Card drawnCard = deck.drawCard();
                player.drawCard(drawnCard);
                System.out.println("You drew a card: " + drawnCard);
                drawInputValid = true;
            } else {
                try {
                    int index = Integer.parseInt(input);
                    if (index >= 0 && index < player.getHandSize()) {
                        Card selectedCard = player.getCard(index);
                        if (selectedCard.isValidMove(discardPile.getTopCard())) {
                            player.playCard(selectedCard);
                            discardPile.addCard(selectedCard);
                            System.out.println(player.getName() + " played: " + selectedCard);
                            handleSpecialCard(selectedCard);
                            drawInputValid = true;
                        } else {
                            System.out.println("Invalid move! Please try again.");
                        }
                    } else {
                        System.out.println("Invalid input! Please try again.");
                    }
                } catch (NumberFormatException e) {
                    if (!input.equalsIgnoreCase("draw")) {
                        System.out.println("Invalid input! Please enter a valid card index or 'draw'.");
                    } else {
                        System.out.println("Invalid input! Please enter 'draw' in the correct format.");
                    }
                }
            }
        }

        System.out.println(player.getName() + "'s hand: " + player.getHand()); // Print player's hand
        System.out.println("Top card of the discard pile: " + discardPile.getTopCard());
        System.out.println("--------");
    }


    private void handleSpecialCard(Card card) {
        switch (card.getType()) {
            case REVERSE:
                direction = !direction;
                System.out.println("Direction reversed!");
                switchPlayer();
                break;
            case SKIP:
                System.out.println("Turn skipped!");
                switchPlayer();
                break;
            case DRAW_TWO:
                Player nextPlayer = getNextPlayer();
                dealCards(nextPlayer, 2);
                System.out.println(nextPlayer.getName() + " drew 2 cards and their turn is skipped!");
                switchPlayer();
                break;
            case WILD:
                Player currentPlayer = getCurrentPlayer();
                Color chosenColor = chooseColor(currentPlayer);
                card.setColor(chosenColor);
                System.out.println(currentPlayer.getName() + " chose the color: " + chosenColor);
                break;
            case WILD_DRAW_FOUR:
                Player currentPlayer2 = getCurrentPlayer();
                Color chosenColor2 = chooseColor(currentPlayer2);
                card.setColor(chosenColor2);
                System.out.println(currentPlayer2.getName() + " chose the color: " + chosenColor2);
                Player nextPlayer2 = getNextPlayer();
                dealCards(nextPlayer2, 4);
                System.out.println(nextPlayer2.getName() + " drew 4 cards and their turn is skipped!");
                switchPlayer();
                break;
        }
    }

    private Color chooseColor(Player player) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(player.getName() + ", choose a color (RED, GREEN, BLUE, YELLOW): ");
        String input = scanner.nextLine().toUpperCase();
        Color chosenColor;
        switch (input) {
            case "RED":
                chosenColor = Color.RED;
                break;
            case "GREEN":
                chosenColor = Color.GREEN;
                break;
            case "BLUE":
                chosenColor = Color.BLUE;
                break;
            case "YELLOW":
                chosenColor = Color.YELLOW;
                break;
            default:
                System.out.println("Invalid color choice. Choosing RED by default.");
                chosenColor = Color.RED;
                break;
        }
        return chosenColor;
    }


    private Player getNextPlayer() {
        int nextPlayerIndex;
        if (direction) {
            nextPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } else {
            nextPlayerIndex = (currentPlayerIndex - 1 + players.size()) % players.size();
        }
        return players.get(nextPlayerIndex);
    }
}