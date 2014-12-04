package mod6;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class Learner {

	public static void main(String[] args) throws IOException {

		// Learn from training set
		Model maleModel = new Model("M");
		Model femaleModel = new Model("F");

		// Test on test set
		File[] testFiles = getFiles("blogstest/" + "M");

		maleModel = new Model("M");
		femaleModel = new Model("F");

		// Holds the count of times a file was classified as a certain class
		HashMap<String, Integer> classes = new HashMap<String, Integer>();

		for (File file : testFiles) {
			String givenClass = classify(file, maleModel, femaleModel);
			if (!classes.containsKey(givenClass)) {
				classes.put(givenClass, 0);
			}
			classes.put(givenClass, classes.get(givenClass) + 1);
		}

		System.out.println("\nRESULTS:");
		for (String key : classes.keySet()) {
			System.out.printf("Class: %s\tTotal test Set Probability: %f%%\n",
					key, ((float) classes.get(key)) / testFiles.length * 100);
		}
	}

	/**
	 * Find in which class the given file belongs according to the given models
	 * @param file - File to classify
	 * @param maleModel - Holds information to check probability that a certain word is of that class
	 * @param femaleModel - Holds information to check probability that a certain word is of that class
	 * @return A string with the class it was classified as
	 */
	private static String classify(File file, Model maleModel, Model femaleModel)
			throws IOException {
		StringBuilder builder = new StringBuilder();
		builder.append(readFile(file.getAbsolutePath(),
				Charset.defaultCharset()));

		String[] fileText = builder.toString().toLowerCase()
				.replaceAll("[^a-z']", " ").replaceAll("[\\s]{2,}", " ")
				.split(" ");

		double maleProb = 0, femaleProb = 0;
		int docWordCnt = fileText.length;
		int vocabSize = maleModel.vocabulary.size()
				+ femaleModel.vocabulary.size();
		for (String word : fileText) {
			if (word != " ") {
				maleProb += maleModel.getProbability(word, docWordCnt,
						vocabSize);
				femaleProb += femaleModel.getProbability(word, docWordCnt,
						vocabSize);
			}
		}
		String writer = maleProb > femaleProb ? "male" : "female";
		// System.out.printf("Writer: %s\tProb: %f\t(Other prob: %f)\n", writer,
		// maleProb, femaleProb);
		return writer;
	}

	/**
	 * Utility to get the contents of a file as one String
	 */
	public static String readFile(String path, Charset encoding)
			throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	/**
	 * Utility to get a File[] of all files in a directory
	 */
	public static File[] getFiles(String path) {
		File dir = new File(path);
		File[] directoryListing = dir.listFiles();
		return directoryListing;
	}

}
