import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Game {

    final int numberTypesOfFoes = 10;
    final int numberPlayers = 4;
    int playerTurn = 0;
    Card eventCard = null;
    Quest quest = null;
    public PrintWriter output;
    public Scanner input;

    Deck adventureDeck = new Deck(50);
    Deck eventDeck = new Deck(50);
    ArrayList<Player> players = new ArrayList<>(numberPlayers);
    Deck adventureDiscardDeck = new Deck(50);
    Deck eventDiscardDeck = new Deck(50);
    ArrayList<String> winners = new ArrayList<>(numberPlayers);

    public Game() {
        input = new Scanner(System.in);
        output = new PrintWriter(System.out, true);
    }

    public Game(Scanner in, PrintWriter out) {
        input = in;
        output = out;
    }

    public void print(String str) {
        output.print(str);
        output.flush();
    }

    public Deck getAdventureDeck() {
        return adventureDeck;
    }

    public Deck getEventDeck() {
        return eventDeck;
    }

    public int GetAdventureDeckSize() {
        return adventureDeck.size();
    }

    public int GetNumberFoes() {
        return GetNumberOfType("F", adventureDeck);
    }

    public int GetNumberWeapons() {
        int count = 0;
        for (Card card : adventureDeck.getDeck()) {
            if (!Objects.equals(card.getType(), "F")) {
                count++;
            }
        }
        return count;
    }

    private int GetNumberOfType(String type, Deck deck) {
        int count = 0;
        for (Card card : deck.getDeck()) {
            if (Objects.equals(card.getType(), type)) {
                count++;
            }
        }
        return count;
    }

    public int GetEventDeckSize() {
        return eventDeck.size();
    }

    public int GetNumberQuestCards() {
        return GetNumberOfType("Q", eventDeck);
    }

    public int GetNumberEventCards() {
        return GetNumberOfType("E", eventDeck);
    }

    public void InitializeAdventureDeck() {
        Card newCard;
        int[] value = {5, 10, 15, 20, 25, 30, 35, 40, 50, 70};
        int[] numberFoes = {8, 7, 8, 7, 7, 4, 4, 2, 2, 1}; // of certain value
        int[] wValue = {5, 10, 10, 15, 20, 30};
        String[] wType = {"D", "H", "S", "B", "L", "E"};
        int[] numberWeapons = {6, 12, 16, 8, 6, 2}; // of certain value
        int numberWeaponTypes = 6;

        for (int i = 0; i < numberTypesOfFoes; i++) {
            for (int j = 0; j < numberFoes[i]; j++) {
                newCard = new Card(value[i], "F", Card.CardType.ADVENTURE);
                adventureDeck.add(newCard);

            }
        }

        for (int i = 0; i < numberWeaponTypes; i++) {
            for (int j = 0; j < numberWeapons[i]; j++) {
                newCard = new Card(wValue[i], wType[i], Card.CardType.ADVENTURE);
                adventureDeck.add(newCard);
            }
        }

    }

    public void InitializeEventDeck() {
        Card newCard;
        int[] value = {2, 3, 4, 5};
        int[] numberQuests = {3, 4, 3, 2}; // of certain value
        int numberTypesOfQuests = 4;

        for (int i = 0; i < numberTypesOfQuests; i++) {
            for (int j = 0; j < numberQuests[i]; j++) {
                newCard = new Card(value[i], "Q", Card.CardType.EVENT);
                eventDeck.add(newCard);
            }
        }

        int[] eValue = {1, 2, 3}; // of certain value
        int[] numberEvents = {1, 2, 2}; // of certain value
        int numberTypesOfEvents = 3;

        for (int i = 0; i < numberTypesOfEvents; i++) {
            for (int j = 0; j < numberEvents[i]; j++) {
                newCard = new Card(eValue[i], "E", Card.CardType.EVENT);
                eventDeck.add(newCard);
            }
        }

    }

    public void distributeCards() {
        int defaultNumCard = 12;
        for (int i = 0; i < defaultNumCard; i++) {
            players.get(0).hand.add(drawAdventureCard());
            players.get(1).hand.add(drawAdventureCard());
            players.get(2).hand.add(drawAdventureCard());
            players.get(3).hand.add(drawAdventureCard());
        }

        for (Player player : players) {
            player.hand.sort();
        }
    }

    public Card drawAdventureCard() {
        if (adventureDeck.isEmpty()) {
            adventureDeck.addAll(adventureDiscardDeck.removeAll());
            adventureDeck.shuffle();
        }
        return adventureDeck.drawCard();
    }

    public boolean checkForWinner() {
        for (Player player : players) {
            if (player.hasWon()) {
                print("Winners: " + getWinners());
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> getListOfWinners() {
        ArrayList<String> winners = new ArrayList<>();
        for (Player player : players) {
            if (player.hasWon()) {
                winners.add(player.playerNumber + "");
            }
        }
        return winners;
    }

    public String getWinners() {
        winners = getListOfWinners();
        StringBuilder winner = new StringBuilder();
        if (!winners.isEmpty()) {
            print("End of game!\nWinners!\n");
            for (String s : winners) {
                winner.append(s).append(" ");
            }
        }
        print(winner.toString());
        return winner.toString();
    }

    public Card drawEventCard() {
        if (eventDeck.isEmpty()) {
            eventDeck.addAll(eventDiscardDeck.removeAll());
            eventDeck.shuffle();
        }
        return eventDeck.drawCard();
    }

    public void resolveEvent(Card newCard) {
        if (newCard.cardType == Card.CardType.ADVENTURE) {
            throw new RuntimeException("Drew adventure card!!!!!");
        }
        Player currentPlayer = players.get(playerTurn);
        Card card;
        switch (newCard.cardValue) {
            case 1: // Plague: current player loses 2 shields
                print("Lose 2 shields\n");
                currentPlayer.plague();
                nextPlayer();
                break;
            case 2: // Queen’s favor: current player draws 2 adventure cards and possibly trims their hand (UC-03)
                print("Draw 2 adventure cards\n");
                card = drawAdventureCard();
                print("Drew: " + card + "\n");
                currentPlayer.addCard(card);
                card = drawAdventureCard();
                print("Drew: " + card + "\n");
                currentPlayer.addCard(card);
                currentPlayer.hand.sort();
                trimCards();
                break;
            case 3: // Prosperity: All players draw 2 adventure cards and each of them possibly trims their hand (UC-03)
                print("Everyone draws 2 adventure cards\n");
                for (Player player : players) {
                    card = drawAdventureCard();
                    print("Drew: " + card + "\n");
                    player.addCard(card);

                    card = drawAdventureCard();
                    print("Drew: " + card + "\n");
                    player.addCard(card);
                    player.hand.sort();
                    trimCards();
                    nextPlayer();
                }

                break;

        }
        playerTurn = (playerTurn + 1) % numberPlayers;
    }


    public boolean requestSponsorships() {
        quest = new Quest(eventCard.cardValue);
        for (int i = 0; i < numberPlayers; i++) {
            print("Quest of " + eventCard.cardValue + " stages\n");
            displayHand(playerTurn);

            while (true) {
                String prompt = "Player " + ((playerTurn + 1) + " do you want to sponsor (y/N): ");
                String response = PromptInput(prompt);
                print(response + "\n");
                if (response.equalsIgnoreCase("n")) {
                    break;
                } else if (response.equalsIgnoreCase("y")) {
                    players.get(playerTurn).sponsor = true;
                    quest.sponsor = players.get(playerTurn);
                    return true;
                }
            }

            playerTurn = nextPlayer();

        }
        eventDiscardDeck.add(eventCard);
        quest = null;

        return false;// Continue this process until a player agrees to sponsor or all players decline.
    }


    public int nextPlayer() {
        print("Press <Enter> to end your turn\n");
        input.nextLine();
        print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");

        return (playerTurn + 1) % numberPlayers;
    }

    public Card startTurn() {
        print("Current player: " + players.get(playerTurn).playerNumber + "\n");

        Card newCard = drawEventCard();
        print("Drew: " + newCard + "\n");
        eventCard = newCard;
        switch (newCard.type) {
            case "E":
                resolveEvent(newCard);
                eventDiscardDeck.add(eventCard);
                break;
            case "Q":
                break;
        }
        return newCard;
    }

    public String PromptInput(String prompt) {
        print(prompt);
        return input.nextLine();
    }

    public int readCardInput(String input) {
        int cardIndex = -1;
        try {
            cardIndex = Integer.parseInt(input);
        } catch (NumberFormatException _) {
        }
        return cardIndex;
    }

    public void trimCards() {
        for (Player player : players) {
            int count = player.numberToTrim();
//            if (count > 0) {

            for (int i = 0; i < count; i++) {
                while (true) {
                    print("Player " + player.playerNumber + " need to discard " + (count - i) + " cards\n");
                    displayHand(playerTurn);
                    String userInput = PromptInput("Choose a card to discard: ");
                    print(userInput + "\n");

                    int cardIndex = -1;
                    try {
                        cardIndex = Integer.parseInt(userInput);
                    } catch (NumberFormatException _) {
                        print("\nInvalid card index.\n");
                        continue;
                    }

                    if (cardIndex >= 0 && cardIndex < player.hand.size()) {
                        Card card = player.removeCard(cardIndex);
                        // Validate card type (foe or weapon) and uniqueness within the stage
                        if (card != null) {
                            print("Discard " + card + "\n");
                            adventureDiscardDeck.add(card);
                            break;
                        } else {
                            print("\nInvalid card selection.\n");
                        }
                    } else {
                        print("\nInvalid card index.\n");
                    }
                }
            }
            if (count > 0) {
                displayHand(player.playerNumber - 1);
                print("Press <Enter> to end your turn\n");
                input.nextLine();
                print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
            }
        }

    }

    public void displayHand(int playerIndex) {
        print("Hand: " + players.get(playerIndex).handToString() + "\n");
    }

    public void sponsorSetsUpQuest() {
        Player sponsor = quest.sponsor;
        for (int i = 0; i < quest.numStages; i++) {
            buildStage(sponsor, i);
        }

        // Check if all stages are valid
        if (quest.stages.size() == quest.numStages) {
            // Quest is ready to be resolved
            print("Quest setup successful!\n");
        } else {
            print("Quest setup failed: Insufficient stages.\n");
        }

        print(quest + "\n");

        nextPlayer();

    }

    public Stage buildStage(Player sponsor, int stageIndex) {
        Stage stage = new Stage();
        boolean stageIsValid = false;
        quest.addStage(stage);

        while (true) {
            displayHand(playerTurn);
            String userInput = PromptInput("Sponsor, choose a card for stage " + (stageIndex + 1) + "/" + quest.numStages + ": ");
            print(userInput);
            if (userInput.equalsIgnoreCase("quit") && stageIsValid) {
                print("\n\n");
                break;
            } else if (userInput.equalsIgnoreCase("quit")) {
                print("\nInsufficient value for this stage.\n");
                continue;
            }

            int cardIndex;
            try {
                cardIndex = Integer.parseInt(userInput);
            } catch (NumberFormatException _) {
                print("\nInvalid card index.\n");
                continue;
            }

            if (cardIndex >= 0 && cardIndex < sponsor.hand.size()) {
                Card card = sponsor.hand.removeCard(cardIndex);
                // Validate card type (foe or weapon) and uniqueness within the stage
                if (card != null && stage.isValidCard(card)) {
                    stage.addCard(card);
                    stage.value = stage.calculateValue();
                    print("\nSelected: " + card + "\n\n");
                    if (quest.isStageValid(stageIndex)) {
                        stageIsValid = true;
                    }
                } else {
                    sponsor.hand.add(card);
                    print("\nInvalid card selection.\n");
                }
            } else {
                print("\nInvalid card index.\n");
            }
        }
        return stage;
    }

    public ArrayList<Player> eligibleParticipants() {
        ArrayList<Player> eligibleParticipants;
        if (quest.currentStage == 0) {
            eligibleParticipants = new ArrayList<>();
            for (Player player : players) {
                if (player.playerNumber - 1 != playerTurn) {
                    eligibleParticipants.add(player);
                }
            }
        } else {
            eligibleParticipants = quest.stages.get(quest.currentStage).participants;
        }
        print("Eligible Participants: " + eligibleParticipants + "\n");
        quest.stages.get(quest.currentStage).participants = eligibleParticipants;
        return eligibleParticipants;
    }

    public void participateInQuest() {
        eligibleParticipants();
        ArrayList<Player> participants = quest.stages.get(quest.currentStage).participants;
        ArrayList<Player> toRemove = new ArrayList<>();

        for (Player player : participants) {
            if (player.playerNumber != quest.sponsor.playerNumber) {
                print(player.handToString());
                while (true) {
                    String prompt = "\nPlayer " + player.playerNumber + " do you want to participate in the quest (y/N): ";
                    String response = PromptInput(prompt);
                    print(response + "\n");
                    if (response.equalsIgnoreCase("n")) {
                        toRemove.add(player);
                        break;
                    } else if (response.equalsIgnoreCase("y")) {
                        Card card = drawAdventureCard();
                        print("\nDrew: " + card + "\n");
                        player.addCard(card);

                        print(player.handToString() + "\n");
                        break;
                    }
                }


            }
            nextPlayer();
        }
        trimCards();
        participants.removeAll(toRemove);
        quest.stages.get(quest.currentStage).participants = participants;
    }


    public void handleParticipantAttacks() {
        ArrayList<Player> participants = quest.stages.get(quest.currentStage).participants;

        for (Player participant : participants) {
            participant.attackValue = participant.setupAttack(input, output);
            print("Press <Enter> to end your turn\n");
            input.nextLine();
            print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        }

    }

    public void resolveStage() {
        if (quest.numStages - 1 == quest.currentStage) {
            quest.stages.add(new Stage());
        }

        for (int i = 0; i < quest.stages.get(quest.currentStage).participants.size(); i++) {
            Player participant = quest.stages.get(quest.currentStage).participants.get(i);
            participant.sponsor = false;

            // participants with an attack equal or greater to the value of the current stage are eligible for the next stage
            if (participant.attackValue >= quest.stages.get(quest.currentStage).value) {

                // If this is the last stage, they are winners of this quest and earn as many shields as there are stages to this quest.
                if (quest.numStages - 1 == quest.currentStage) {
                    participant.shields += quest.numStages;
                }
                quest.stages.get(quest.currentStage + 1).participants.add(participant);
            }
            adventureDiscardDeck.addAll(participant.attack);
            participant.attack.removeAll();
            participant.attackValue = 0;
        }

        if (quest.numStages - 1 == quest.currentStage) {
            print("Quest completed by players: " + quest.stages.get(quest.currentStage + 1).participants + "\n");
        }

        if (quest.numStages - 1 == quest.currentStage || quest.stages.get(quest.currentStage + 1).participants.isEmpty()) {
//            print("Quest completed by players: " + quest.stages.get(quest.currentStage + 1).participants + "\n");
            // draws the same number of cards + the number of stages, and then possibly trims their hand
            for (int i = 0; i < quest.countCardsUsed() + quest.numStages; i++) {
                quest.sponsor.addCard(adventureDeck.drawCard());
            }
            quest.sponsor.hand.sort();
            trimCards();
            for (Stage stage : quest.stages) {
                adventureDiscardDeck.add(stage.foeCard);
                stage.foeCard = null;
                adventureDiscardDeck.addAll(stage.weaponCards);
                stage.weaponCards.removeAll();
            }
            eventDiscardDeck.add(eventCard);
            eventCard = null;
            quest = null;
            playerTurn = nextPlayer();
        } else {
            quest.currentStage++;
            print("Players continuing the quest: " + quest.stages.get(quest.currentStage).participants + "\n");
        }


    }

    public Player getPlayer(int playerNumber) {
        return players.get(playerNumber - 1);
    }
}
