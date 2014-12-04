package mod6;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import weka.core.Instances;
import weka.core.converters.TextDirectoryLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class ArffBuilder {
	
	private static final boolean DEBUG = false;

	public static void main(String[] args) throws Exception {

		System.out.println("Building...");
		
		final String[] FOLDERS = { "spammail", "blogstrain", "blogstest" };
		final boolean APPLY_FILTER = false;

		for (String name : FOLDERS) {
			Instances instances = buildArff(name, APPLY_FILTER);
			saveInstances(name + (APPLY_FILTER ? "_filtered.arff" : ".arff"), instances);
		}
		
		System.out.println("Done.");
	}

	/**
	 * Create Instances from dataset at `foldername`
	 */
	public static Instances buildArff(String foldername, boolean applyFilter)
			throws Exception {
		// load all data files from a directory into a dataset
		TextDirectoryLoader loader = new TextDirectoryLoader();
		loader.setDebug(DEBUG);
		loader.setDirectory(new File(foldername));

		Instances dataset = loader.getDataSet();

		if (applyFilter) {
			// apply the StringToWordVector filter
			StringToWordVector filter = new StringToWordVector();
			filter.setInputFormat(dataset);
			dataset = Filter.useFilter(dataset, filter);
		}

		return dataset;
	}

	/**
	 * Save instances to disk
	 */
	public static void saveInstances(String filename, Instances data) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileOutputStream(filename, false), true);
			pw.println(data.toString());
		} catch (IOException exc) {
			System.out.println("could not open output file");
		} finally {
			if (pw != null)
				pw.close();
		}

	}
}
