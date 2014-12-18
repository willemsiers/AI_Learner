package mod6;

import java.util.ArrayList;
import java.util.Arrays;

public class Tokenizer {

	/**
	 * Simple test for the tokenizer
	 */
	public static void main(String[] args) {
		String[] tokens = tokenize("Hello \"(testsubject*\" how are you today?");

		for (String token : tokens) {
			System.out.println(token);
		}
	}

	/**
	 * Only keeps certain words
	 */
	public static String[] tokenize(String input) {
		String[] split = input.toLowerCase().replaceAll("[^a-z']", " ").split("\\s+");
		ArrayList<String> tokens = new ArrayList<String>(Arrays.asList(split));
		// int c= 0;
		// System.out.println("size : "+tokens.size());
		// for (Iterator it = tokens.iterator(); it.hasNext();) {
		// String token = (String) it.next();
		// // System.out.println(token);
		// if(token.equals("")){
		// System.out.println("removing empty..." +c++);
		// it.remove();
		// }
		// }
		return tokens.toArray(new String[0]);
	}
}