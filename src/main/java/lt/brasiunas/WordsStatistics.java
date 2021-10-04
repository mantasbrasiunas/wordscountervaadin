package lt.brasiunas;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.util.stream.Collectors;
import java.util.TreeMap;

import java.util.LinkedHashMap;

/**
 * @author Mantas Brasiunas, mantas@brasiunas.lt
 * @version 2021-10-04
 */
public class WordsStatistics {
	private TreeMap<String, Integer> wordFrequency;
	
	public WordsStatistics() {
		wordFrequency = new TreeMap<String, Integer>();
	}
	
	public synchronized void updateWordsFrequency(
			TreeMap<String, Integer> workerResults) {
		for (java.util.Map.Entry<String, Integer> entry : 
				workerResults.entrySet()) {
			String entryKey = entry.getKey();
			Integer entryValue = entry.getValue();
			if (wordFrequency.containsKey(entryKey) == true) {
				int frequency = wordFrequency.get(entryKey) + entryValue;
				wordFrequency.put(entryKey, frequency);
			} else {
				wordFrequency.put(entryKey, entryValue);
			}
		}
	}
	
	
	
	public void printWordsFrequencyOrdered() {
		LinkedHashMap<String, Integer> sortedNewMap = wordFrequency
		.entrySet()
		.stream()
		.sorted((v1, v2) -> Integer.compare(v2.getValue(), v1.getValue()))
		.collect(Collectors.toMap(
			java.util.Map.Entry::getKey,
			java.util.Map.Entry::getValue,
			(v1, v2) -> v1, LinkedHashMap::new
		));
	};
	
	public void writeStatistics(String fileName, 
			char startIncluded, char endIncluded) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
			TreeMap<String, Integer> filteredMap = wordFrequency
				.entrySet()
				.stream()
				.filter(map -> (
						(map.getKey().charAt(0) >= startIncluded) && 
						(map.getKey().charAt(0) <= endIncluded)
					)
				)
				.collect(
					Collectors.toMap(
						java.util.Map.Entry::getKey,
						java.util.Map.Entry::getValue,
						(v1, v2) -> v1, TreeMap::new
					)
				);
			
			for (java.util.Map.Entry<String, Integer> entry : 
					filteredMap.entrySet()) {
				bw.write(String.format("%s=%d%n", 
					entry.getKey(), entry.getValue()));
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public String getStatistics(char startIncluded, char endIncluded) {
		TreeMap<String, Integer> filteredMap = wordFrequency
				.entrySet()
				.stream()
				.filter(map -> (
						(map.getKey().charAt(0) >= startIncluded) && 
						(map.getKey().charAt(0) <= endIncluded)
					)
				)
				.collect(
					Collectors.toMap(
						java.util.Map.Entry::getKey,
						java.util.Map.Entry::getValue,
						(v1, v2) -> v1, TreeMap::new
					)
				);
				
		StringBuilder sb = new StringBuilder();
		for (java.util.Map.Entry<String, Integer> entry :
				filteredMap.entrySet()) {
			sb.append(String.format("%s%n", entry));
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (java.util.Map.Entry<String, Integer> entry : 
				wordFrequency.entrySet()) {
			sb.append(entry + "\n");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
}
