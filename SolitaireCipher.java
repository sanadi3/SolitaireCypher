package assignment2;

public class SolitaireCipher {
	public Deck key;

	public SolitaireCipher (Deck key) {
		this.key = new Deck(key); // deep copy of the deck
	}

	/* 
	 * TODO: Generates a keystream of the given size
	 */
	public int[] getKeystream(int size) {
		/**** ADD CODE HERE ****/
		int[] keystream = new int[size];

		for (int i = 0; i < size; i++) {
			int nextValue = key.generateNextKeystreamValue();
			keystream[i] = nextValue;
		}
		return keystream;
	}

	/* 
	 * TODO: Encodes the input message using the algorithm described in the pdf.
	 */
	public String encode(String msg) {
		/**** ADD CODE HERE ****/
		msg = msg.toUpperCase().replaceAll("[^A-Z]", "");
		int[] keystream = getKeystream(msg.length());
		char[] messageChars = msg.toCharArray();
		StringBuilder encodedMsg = new StringBuilder();

		for (int i = 0; i < msg.length(); i++) {
			int letterValue = (messageChars[i] - 'A' + keystream[i]) % 26;
			char encodedChar = (char) (letterValue + 'A');
			encodedMsg.append(encodedChar);
		}
		return encodedMsg.toString();
	}

	/* 
	 * TODO: Decodes the input message using the algorithm described in the pdf.
	 */
	public String decode(String msg) {
		/**** ADD CODE HERE ****/
		int[] keystream = getKeystream(msg.length());
		char[] messageChars = msg.toCharArray();
		StringBuilder decodedMsg = new StringBuilder();

		for (int i = 0; i < msg.length(); i++) {
			int letterValue = (messageChars[i] - 'A' - keystream[i] + 26) % 26;
			char decodedChar = (char) (letterValue + 'A');
			decodedMsg.append(decodedChar);
		}

		return decodedMsg.toString();
	}

}
