import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClueGame {
    private final List<Player> players;
    private final List<Room> rooms;
    private Deck characterDeck;
    private Deck placeDeck;
    private Deck roomDeck;
    private final Dice dice;
    private Card hiddenCharacter;
    private Card hiddenPlace;
    private Card hiddenRoom;
    private boolean gameWon;

    public ClueGame(int numPlayers) {
        players = new ArrayList<>();
        rooms = new ArrayList<>();
        dice = new Dice();
        gameWon = false;
        initGame(numPlayers);
    }

    private void initGame(int numPlayers) {
        // Characters
        List<Card> characters = List.of(
                new Card("Emma"),
                new Card("Liam"),
                new Card("Jack"),
                new Card("Sophia"),
                new Card("Emily"),
                new Card("Ella")
        );
        characterDeck = new Deck(characters);
        characterDeck.shuffle();

        // Places
        List<Card> places = List.of(
                new Card("under the vase"),
                new Card("hidden drawer"),
                new Card("behind the photo"),
                new Card("inside the box"),
                new Card("under the table"),
                new Card("on top of the closet")
        );
        placeDeck = new Deck(places);
        placeDeck.shuffle();

        // Rooms
        List<Card> roomsList = List.of(
                new Card("greenhouse"),
                new Card("billiard room"),
                new Card("study room"),
                new Card("reception room"),
                new Card("bedroom"),
                new Card("piano room"),
                new Card("dining room"),
                new Card("kitchen"),
                new Card("library")
        );
        roomDeck = new Deck(roomsList);
        roomDeck.shuffle();

        // Add rooms to the game using RoomBuilder
        for (Card _ : roomsList) {
            Room build = new RoomBuilder().setName().build();
            rooms.add(build);
        }

        // Set aside hidden cards
        hiddenCharacter = characterDeck.drawCard();
        hiddenPlace = placeDeck.drawCard();
        hiddenRoom = roomDeck.drawCard();

        // Initialize players and deal cards
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player("Player " + (i + 1)));
        }
        dealCards();
    }

    private void dealCards() {
        List<Card> allCards = new ArrayList<>();
        allCards.addAll(characterDeck.cards());
        allCards.addAll(placeDeck.cards());
        allCards.addAll(roomDeck.cards());

        int playerIndex = 0;
        while (!allCards.isEmpty()) {
            players.get(playerIndex).addCard(allCards.removeFirst());
            playerIndex = (playerIndex + 1) % players.size();
        }
    }

    public void startGame() {
        // Main game loop
        while (!gameWon) {
            for (Player player : players) {
                takeTurn(player);
                // Check if a player has won or if game continues
                if (gameWon) break;
            }
        }
        System.out.println("Game over!");
    }

    private void takeTurn(Player player) {
        int roll = dice.roll();
        System.out.println(player.getName() + " rolled a " + roll);

        Room newRoom = getNewRoom(player.getCurrentRoom(), roll);
        if (newRoom == null) {
            System.out.println(player.getName() + " cannot move to a new room.");
            return;
        }
        player.setCurrentRoom(newRoom);
        System.out.println(player.getName() + " moves to " + newRoom.name());

        // Make a guess
        Card guessedCharacter = characterDeck.cards().get(new Random().nextInt(characterDeck.cards().size())); // Random guess
        Card guessedPlace = placeDeck.cards().get(new Random().nextInt(placeDeck.cards().size())); // Random guess
        System.out.println(player.getName() + " guesses " + guessedCharacter.getName() + " in " + newRoom.name() + " with " + guessedPlace.getName());

        for (Player otherPlayer : players) {
            if (otherPlayer != player) {
                if (otherPlayer.hasCard(guessedCharacter) || otherPlayer.hasCard(guessedPlace) || otherPlayer.getCurrentRoom().name().equals(newRoom.name())) {
                    // Show card to guessing player (simulate)
                    System.out.println(otherPlayer.getName() + " shows a card to " + player.getName());
                    return;
                }
            }
        }

        // Check if the guess is correct (final guess scenario)
        if (guessedCharacter.getName().equals(hiddenCharacter.getName()) &&
                guessedPlace.getName().equals(hiddenPlace.getName()) &&
                newRoom.name().equals(hiddenRoom.getName())) {
            System.out.println(player.getName() + " has won the game by correctly guessing " + guessedCharacter.getName() + " in " + newRoom.name() + " with " + guessedPlace.getName());
            gameWon = true;
        }
    }

    private Room getNewRoom(Room currentRoom, int roll) {
        List<Room> possibleRooms = new ArrayList<>();
        for (Room room : rooms) {
            int roomIndex = rooms.indexOf(room);
            if ((roll % 2 == 0 && roomIndex % 2 == 0) || (roll % 2 != 0 && roomIndex % 2 != 0)) {
                if (currentRoom == null || Math.abs(roomIndex - rooms.indexOf(currentRoom)) > 1) {
                    possibleRooms.add(room);
                }
            }
        }
        if (possibleRooms.isEmpty()) {
            return null;
        }
        return possibleRooms.get(new Random().nextInt(possibleRooms.size()));
    }

    public static void main(String[] args) {
        ClueGame game = new ClueGame(4);  // Example with 4 players
        game.startGame();
    }
}

