package lt.brasiunas;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.TreeMap;

/**
 * @author Mantas Brasiunas, mantas@brasiunas.lt
 * @version 2021-10-04
 */
public class WordsCounter implements Runnable {
	private String fileName;
	private InputStream inputStream;
	private TreeMap<String, Integer> result;
	private boolean finished;
	private WordsStatistics wordsStatistics;
	
	
	public WordsCounter(String fileName, WordsStatistics wordsStatistics) {
		this.fileName = fileName;
		this.inputStream = null;
		this.wordsStatistics = wordsStatistics;
		this.result = null;
		this.finished = false;
	}
	
	public WordsCounter(InputStream inputStream, 
			WordsStatistics wordsStatistics)  {
		this.fileName = null;
		this.inputStream = inputStream;
		this.wordsStatistics = wordsStatistics;
		this.result = null;
		this.finished = false;
	}
	
	public boolean isFinished() {
		return finished;
	}
	
	public TreeMap<String, Integer> getResult() {
		if (finished == true) {
			return result;
		} else {
			return null;
		}
	}
	
	private TreeMap<String, Integer> countWords(BufferedReader br) 
			throws IOException {
		TreeMap<String, Integer> result = new TreeMap<String, Integer>();
		String line = null;
		String regex = "[^a-zA-Z]+";
		while ((line = br.readLine()) != null) {
			String[] splittedLine = line.split(regex);
			for (int i = 0; i < splittedLine.length; i++) {
				String word = splittedLine[i].toLowerCase();
				if (word.length() == 0) continue;
				else {}
				int occurance = 
					(result.containsKey(word) == true) ? 
					result.get(word) : 0;
				result.put(word, ++occurance);
			}
		}
		return result;
	}
	
	private void countWords(String fileName) {
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			result = countWords(br);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private void countWords(InputStream inputStream) {
		System.out.println("InputStream");
		try (BufferedReader br = new BufferedReader(
					new InputStreamReader(inputStream))) {
			result = countWords(br);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
	}
	
	public void run() {
		if (fileName != null) {
			countWords(fileName);
		} else {
			countWords(inputStream);
		}
		wordsStatistics.updateWordsFrequency(result);
		finished = true;
		System.out.println("---->Baigta gija.");
	}
}
