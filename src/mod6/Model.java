package mod6;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;

public class Model {

	public static double K = 1f;
	public static int MIN_FREQ = 1;
	public static int MAX_FREQ = Integer.MAX_VALUE;

	public int totalCount;
	public HashMap<String, Integer> vocabulary;

	public Model(String name) throws IOException {
		StringBuilder builder = new StringBuilder();
		File[] files = Learner.getFiles("blogstrain/" + name);
		String sep = " ";
		for (File file : files) {
			builder.append(Learner.readFile(file.getAbsolutePath(),
					Charset.defaultCharset()));
			builder.append(sep);
		}
		HashMap<String, Integer> vocabulary = new HashMap<String, Integer>();
		String[] fileText = Tokenizer.tokenize(builder.toString());
		int totalCount = 0;
		for (String word : fileText) {
			if (!word.equals(" ") && !word.equals("")) {
				totalCount++;
				Integer count = vocabulary.get(word);
				if (count == null) {
					count = 0;
				}
				vocabulary.put(word, count + 1);
			}else{
				System.out.println("found: "+word);
			}
		}

		// filter low frequency words
		for (Iterator it = vocabulary.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			Integer freq = vocabulary.get(key);
			if (freq < MIN_FREQ || freq > MAX_FREQ) {
				it.remove();
			}
		}

		this.vocabulary = vocabulary;
		this.totalCount = totalCount;
	}

	public double getProbability(String word, int docSize, int vocabSize) {
		Integer inCnt = vocabulary.get(word);
		inCnt = (inCnt == null) ? 0 : inCnt;
		double prob = ((float) inCnt + K) / (docSize + K * vocabSize);
		return Math.log10(prob) / Math.log10(2);
	}

}
