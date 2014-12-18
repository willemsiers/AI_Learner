package mod6;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class Learner {

	//Simple test that uses console input
	public static void main(String[] args) throws IOException {
		Learner learner = new Learner( new Model("M", "male"), new Model("F", "female"));
		learner.queryData();
		// testFolder("blogstest" + "/M",maleModel, femaleModel);
	}
	
	Model maleModel;
	Model femaleModel;

	public Learner(Model maleModel, Model femaleModel) throws IOException {
		// Learn from training set
		this.maleModel = maleModel;
		this.femaleModel = femaleModel;
	}

	private void queryData() {
		try {
			while (true) {
				BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
				String line = bufferRead.readLine();
				Classification classifiedAs = classifyString(line);
				System.out.println("I guess it's: " + classifiedAs);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Classification classifyString(String data) {
		String[] fileText = Tokenizer.tokenize(data);

		double maleProb = 0, femaleProb = 0;
		int docWordCnt = fileText.length;
		int vocabSize = maleModel.vocabulary.size() + femaleModel.vocabulary.size();
		for (String word : fileText) {
			if (!word.equals(" ")) {
				maleProb += maleModel.getProbability(word, docWordCnt, vocabSize);
				femaleProb += femaleModel.getProbability(word, docWordCnt, vocabSize);
			}else{
				System.out.println("ERROR!");
			}
		}
		Model writer = maleProb > femaleProb ? maleModel : femaleModel;
		// System.out.printf("Writer: %s\tProb: %f\t(Other prob: %f)\n", writer,
		// maleProb, femaleProb);
		Classification classification = new Classification(writer.name, (writer.equals(maleModel) ? maleProb : femaleProb), writer, fileText);
		return classification;
	}
	
	public void addClassification(Classification classification) {
		classification.model.addData(classification);
	}

	public void testFolder(final String TEST_DATA_PATH, Model maleModel, Model femaleModel) throws IOException {
		// Test on test set
		File[] testFiles = getFiles(TEST_DATA_PATH);

		// Holds the count of times a file was classified as a certain class
		HashMap<String, Integer> classes = new HashMap<String, Integer>();

		for (File file : testFiles) {
			Classification givenClass = this.classifyFile(file);
			if (!classes.containsKey(givenClass)) {
				classes.put(givenClass.model.name, 0);
			}
			classes.put(givenClass.model.name, classes.get(givenClass) + 1);
		}

		System.out.println("\nRESULTS:");
		System.out.printf("Number of tested files in %s:\t%d\n", TEST_DATA_PATH, testFiles.length);
		for (String key : classes.keySet()) {
			System.out.printf("Classified as \t%s:\t%d\t(Class Probability: %f%%)\n", key, classes.get(key),
					((double) classes.get(key)) / ((double) testFiles.length) * 100d);
		}
	}

	/**
	 * Find in which class the given file belongs according to the given models
	 * 
	 * @param file
	 *            - File to classify
	 * @param maleModel
	 *            - Holds information to check probability that a certain word
	 *            is of that class
	 * @param femaleModel
	 *            - Holds information to check probability that a certain word
	 *            is of that class
	 * @return A string with the class it was classified as
	 */
	public Classification classifyFile(File file) throws IOException {
		StringBuilder builder = new StringBuilder();
		builder.append(readFile(file.getAbsolutePath(), Charset.defaultCharset()));

		return classifyString(builder.toString());
	}

	/**
	 * Utility to get the contents of a file as one String
	 */
	public static String readFile(String path, Charset encoding) throws IOException {
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
