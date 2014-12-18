package mod6;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;

public class Model {

	public static double K = 0.0000001d;
	public static int MIN_FREQ = 1;
	public static int MAX_FREQ = Integer.MAX_VALUE;

	public int totalCount;
	public HashMap<String, Integer> vocabulary;

	public String name;

	public Model(String path, String name) throws IOException {
		this.name = name;

		StringBuilder builder = new StringBuilder();
		File[] files = Learner.getFiles("blogstrain/" + path);
		String sep = " ";
		for (File file : files) {
			builder.append(Learner.readFile(file.getAbsolutePath(), Charset.defaultCharset()));
			builder.append(sep);
		}
		vocabulary = new HashMap<String, Integer>();
		String[] fileText = Tokenizer.tokenize(builder.toString());
		totalCount = 0;
		for (String word : fileText) {
			if (!word.equals(" ") && !word.equals("")) {
				totalCount++;
				Integer count = vocabulary.get(word);
				if (count == null) {
					count = 0;
				}
				vocabulary.put(word, count + 1);
			} else {
				System.out.println("found: " + word);
			}
		}

		//filterByFrequency();
	}

	// filter low or high frequency words
	private void filterByFrequency() {
		for (Iterator<String> it = vocabulary.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			Integer freq = vocabulary.get(key);
			if (freq < MIN_FREQ || freq > MAX_FREQ) {
				it.remove();
				totalCount-=freq;
			}
		}		
	}

	public void addData(Classification classification) {
		
		for (String word : classification.words) {
			totalCount++;
			Integer count = vocabulary.get(word);
			if (count == null) {
				count = 0;
			}
			vocabulary.put(word, count + 1);
		}
	}

	public double getProbability(String word, int docSize, int vocabSize) {
		Integer inCnt = vocabulary.get(word);
		inCnt = (inCnt == null) ? 0 : inCnt;
		double prob = ((float) inCnt + K) / (docSize + K * vocabSize);
		return Math.log10(prob) / Math.log10(2);
	}

}
