package lt.brasiunas;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.FileReader;
import java.io.InputStream;
/**
 * @author Mantas Brasiunas, mantas@brasiunas.lt
 * @version 2021-10-04
 */
public class WordsCountersManager {
	private void shutdownWordsCounters(WordsCounter[] wordsCounters, 
			ExecutorService executorService) {
		for (int i = 0; i < wordsCounters.length; i++) {
			while (wordsCounters[i].isFinished() != true) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		}
		executorService.shutdown();
	}
	
	public WordsStatistics countWords(String[] fileNames) {
		int nThreads = Runtime.getRuntime().availableProcessors();
		ExecutorService executorService = 
			Executors.newFixedThreadPool(nThreads);
		
		WordsStatistics wordsStatistics = new WordsStatistics();
		WordsCounter[] wordsCounters = new WordsCounter[fileNames.length];
		
		for (int i = 0; i < fileNames.length; i++) {
			wordsCounters[i] = new WordsCounter(fileNames[i], wordsStatistics);
			executorService.submit(wordsCounters[i]);
		}

		shutdownWordsCounters(wordsCounters, executorService);
		return wordsStatistics;
	}
	
	public WordsStatistics countWords(InputStream[] inputStreams) {
		int nThreads = Runtime.getRuntime().availableProcessors();
		ExecutorService executorService = 
			Executors.newFixedThreadPool(nThreads);
		
		WordsStatistics wordsStatistics = new WordsStatistics();
		WordsCounter[] wordsCounters = new WordsCounter[inputStreams.length];
		
		for (int i = 0; i < inputStreams.length; i++) {
			wordsCounters[i] = new WordsCounter(
					inputStreams[i], wordsStatistics);
			executorService.submit(wordsCounters[i]);
		}
		
		shutdownWordsCounters(wordsCounters, executorService);
		return wordsStatistics;
	}
}
