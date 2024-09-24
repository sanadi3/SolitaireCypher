package assignment2;

import java.util.Random;

public class Deck {
	public static String[] suitsInOrder = {"clubs", "diamonds", "hearts", "spades"};
	public static Random gen = new Random();

	public int numOfCards; // contains the total number of cards in the deck
	public Card head; // contains a pointer to the card on the top of the deck

	/*
	 * TODO: Initializes a Deck object using the inputs provided
	 */
	public Deck(int numOfCardsPerSuit, int numOfSuits) {
		/**** ADD CODE HERE ****/
		if (numOfCardsPerSuit < 1 || numOfCardsPerSuit > 13 || numOfSuits < 1 || numOfSuits > suitsInOrder.length) {
			throw new IllegalArgumentException("Invalid input");
		}

		for (int i = 0; s < numOfSuits; i++) {
			for (int j = 1; j <= numOfCardsPerSuit; j++) {
				PlayingCard card = new PlayingCard(suitsInOrder[i], j);
				addCard(card);
			}
		}

		addCard(new Joker("red"));
		addCard(new Joker("black"));
	}


	/*
	 * TODO: Implements a copy constructor for Deck using Card.getCopy().
	 * This method runs in O(n), where n is the number of cards in d.
	 */
	public Deck(Deck d) {
		/**** ADD CODE HERE ****/
		this.head = null;
		Card currentCard = d.head;

		for (int i = 0; i < d.numOfCards; i++) {
			addCard(currentCard.getCopy());
			currentCard = currentCard.next;
		}
		this.numOfCards = d.numOfCards;
	}
	/*
	 * For testing purposes we need a default constructor.
	 */
	public Deck() {}

	/*
	 * TODO: Adds the specified card at the bottom of the deck. This
	 * method runs in $O(1)$.
	 */
	public void addCard(Card c) {
		/**** ADD CODE HERE ****/
		if (head == null) {
			head = c;
			head.next = head;
			head.prev = head;
			this.numOfCards++;
		} else {
			head.prev.next = c;
			c.prev = head.prev;
			head.prev = c;
			c.next = head;
			numOfCards++;
		}
	}

	/*
	 * TODO: Shuffles the deck using the algorithm described in the pdf.
	 * This method runs in O(n) and uses O(n) space, where n is the total
	 * number of cards in the deck.
	 */
	public void shuffle() {
		/**** ADD CODE HERE ****/
		if (head == null || head.next == head) return; // If deck is empty or has one card there is no need to shuffle.

		Card[] cardsArray = new Card[numOfCards];
		Card current = head;

		// Copy cards into an array
		for (int i = 0; i < numOfCards; i++) {
			cardsArray[i] = current;
			current = current.next;
		}

		// Shuffle the array
		for (int i = numOfCards - 1; i > 0; i--) {
			int j = gen.nextInt(i + 1);
			Card temp = cardsArray[i];
			cardsArray[i] = cardsArray[j];
			cardsArray[j] = temp;
		}

		// Rebuild the deck with a circular doubly linked list structure
		for (int i = 0; i < numOfCards - 1; i++) {
			cardsArray[i].next = cardsArray[i + 1];
			cardsArray[i + 1].prev = cardsArray[i];
		}

		cardsArray[numOfCards - 1].next = cardsArray[0];
		cardsArray[0].prev = cardsArray[numOfCards - 1];
		head = cardsArray[0];
	}

	/*
	 * TODO: Returns a reference to the joker with the specified color in
	 * the deck. This method runs in O(n), where n is the total number of
	 * cards in the deck.
	 */
	public Joker locateJoker(String color) {
		/**** ADD CODE HERE ****/
		Card current = head;
		for (int i = 0; i < numOfCards; i++) {
			if (current instanceof Joker && ((Joker) current).getColor().equalsIgnoreCase(color)) {
				return (Joker) current;
			}
			current = current.next;
		}
		return null;
	}

	/*
	 * TODO: Moved the specified Card, p positions down the deck. You can
	 * assume that the input Card does belong to the deck (hence the deck is
	 * not empty). This method runs in O(p).
	 */
	public void moveCard(Card c, int p) {
		/**** ADD CODE HERE ****/
		c.prev.next = c.next;
		c.next.prev = c.prev;
		Card current = c;
		for(int i = 1; i <= p; i++){
			current = current.next;
		}
		Card nextCard = current.next;
		current.next = c;
		c.prev = current;
		nextCard.prev = c;
		c.next = nextCard;
	}

	/*
	 * TODO: Performs a triple cut on the deck using the two input cards. You
	 * can assume that the input cards belong to the deck and the first one is
	 * nearest to the top of the deck. This method runs in O(1)
	 */
	public void tripleCut(Card firstCard, Card secondCard) {
		/**** ADD CODE HERE ****/
		if (firstCard == null || secondCard == null) {
			return;
		}
		if (firstCard == head) {
			head = secondCard.next;
			head.prev = secondCard;
			return;
		}
		if (secondCard == head.prev) {
			secondCard.next = head;
			head = firstCard;
			return;
		}

		Card beforeStart = startCard.prev;
		Card afterEnd = endCard.next;

		head.prev.next = startCard;
		startCard.prev = head.prev;

		head.prev = beforeStart;
		afterEnd.prev = beforeStart;
		beforeStart.next = afterEnd;

		endCard.next = head;
		head = afterEnd;
		head.prev = beforeStart;

	}

	/*
	 * TODO: Performs a count cut on the deck. Note that if the value of the
	 * bottom card is equal to a multiple of the number of cards in the deck,
	 * then the method should not do anything. This method runs in O(n).
	 */
	public void countCut() {
		/**** ADD CODE HERE ****/
		int cutPosition = head.prev.getValue() % numOfCards;
		if (cutPosition == 0 || cutPosition == numOfCards - 1) {
			return;
		}

		Card current = head;
		for (int i = 0; i < cutPosition; i++) {
			current = current.next;
		}

		Card beforeLast = head.prev.prev;
		Card bottomCard = head.prev;

		current.prev.next = bottomCard;
		bottomCard.prev = current.prev;

		bottomCard.next = head;
		head.prev = beforeLast;
		beforeLast.next = head;

		head = current;
	}

	/*
	 * TODO: Returns the card that can be found by looking at the value of the
	 * card on the top of the deck, and counting down that many cards. If the
	 * card found is a Joker, then the method returns null, otherwise it returns
	 * the Card found. This method runs in O(n).
	 */
	public Card lookUpCard() {
		/**** ADD CODE HERE ****/
		int countValue = head.getValue();
		Card targetCard = head;
		for (int i = 0; i < countValue; i++) {
			targetCard = targetCard.next;
		}
		if (targetCard instanceof Joker) {
			return null;
		}
		return targetCard;
	}


	/*
	 * TODO: Uses the Solitaire algorithm to generate one value for the keystream
	 * using this deck. This method runs in O(n).
	 */
	public int generateNextKeystreamValue() {
		/**** ADD CODE HERE ****/

		Card redJoker = locateJoker("red");
		moveCard(redJoker, 1);
		Card blackJoker = locateJoker("black");
		moveCard(blackJoker, 2);
		Card current = head;

		// iterate through deck using current pointer, if current instanceof Joker && ((Joker)current.getColor == "red") then tripleCut(redJoker, black Joker)
		boolean isRedJokerFirst = false;
		for (int i = 0; i < numOfCards; i++) {
			if (current instanceof Joker){
				if (((Joker) current).redOrBlack.equals("red")){
					isRedJokerFirst = true;
				}
				break;
			}
			current = current.next;
		}

		if (isRedJokerFirst){
			tripleCut(redJoker, blackJoker);
		} else {
			tripleCut(blackJoker, redJoker);
		}

		countCut();

		if (lookUpCard() == null)
			return generateNextKeystreamValue();
		else
			return lookUpCard().getValue();
	}


	public abstract class Card {
		public Card next;
		public Card prev;

		public abstract Card getCopy();
		public abstract int getValue();

	}

	public class PlayingCard extends Card {
		public String suit;
		public int rank;

		public PlayingCard(String s, int r) {
			this.suit = s.toLowerCase();
			this.rank = r;
		}

		public String toString() {
			String info = "";
			if (this.rank == 1) {
				//info += "Ace";
				info += "A";
			} else if (this.rank > 10) {
				String[] cards = {"Jack", "Queen", "King"};
				//info += cards[this.rank - 11];
				info += cards[this.rank - 11].charAt(0);
			} else {
				info += this.rank;
			}
			//info += " of " + this.suit;
			info = (info + this.suit.charAt(0)).toUpperCase();
			return info;
		}

		public PlayingCard getCopy() {
			return new PlayingCard(this.suit, this.rank);
		}

		public int getValue() {
			int i;
			for (i = 0; i < suitsInOrder.length; i++) {
				if (this.suit.equals(suitsInOrder[i]))
					break;
			}

			return this.rank + 13*i;
		}

	}

	public class Joker extends Card{
		public String redOrBlack;

		public Joker(String c) {
			if (!c.equalsIgnoreCase("red") && !c.equalsIgnoreCase("black"))
				throw new IllegalArgumentException("Jokers can only be red or black");

			this.redOrBlack = c.toLowerCase();
		}

		public String toString() {
			//return this.redOrBlack + " Joker";
			return (this.redOrBlack.charAt(0) + "J").toUpperCase();
		}

		public Joker getCopy() {
			return new Joker(this.redOrBlack);
		}

		public int getValue() {
			return numOfCards - 1;
		}

		public String getColor() {
			return this.redOrBlack;
		}
	}

}