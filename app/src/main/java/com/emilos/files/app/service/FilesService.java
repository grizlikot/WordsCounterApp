package com.emilos.files.app.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * @author Ervin Milos
 * 
 * FilesService is a service that manages all logic with files
 */
@Service
public class FilesService {
	
	public static final String DATA_SEPARATOR = "-";
	public static final String FILE_NAME = "files_";
	public static final String FILE_FORMAT = ".txt";
	
	
	@Autowired
	private AsyncFilesService asyncFilesService;
	
	/**
	 * 1.This method firstly collects words in all files to the list
	 * 2.Secondly, repeated words are counted and collected to the words map
	 * 3.Map is grouped by letters range (A-G, H-N, O-U, V-Z)
	 * 
	 * @param MultipartFile[] uploadedFiles
	 */
	public Map<Integer, Map<String, Long>> countWordsInFiles(MultipartFile[] uploadedFiles) {
		List<MultipartFile> files = Arrays.asList(uploadedFiles);
		List<String> wordsList = new ArrayList<String>();
		
		wordsList = files.stream()
				.map(file -> asyncFilesService.processFile(file))
				.collect(Collectors.toList())
				.stream()
				.map(future -> {
						try {
							return future.get();
						} catch (InterruptedException | ExecutionException e) {
							throw new IllegalStateException(e);
						}		
				})
				.flatMap(List::stream)
				.collect(Collectors.toList());
		
		Map<String, Long> wordsMap = wordsList.stream().
				collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		
		Map<Integer, Map<String, Long>> groupedMap = groupMapByFirstLetter(wordsMap, 7);
		
		saveResultFiles(groupedMap);
		
		return groupedMap;
	}
	

	/**
	 * This method groups map by first letter, the size is 7, because the distance between each group of letters (except the last) is 7
	 * 
	 * @param Map<String, Long> - wordsMap
	 * @param int - bucketSize
	 * @return
	 */
	public Map<Integer, Map<String, Long>> groupMapByFirstLetter(Map<String, Long> wordsMap, int bucketSize) {
		 return wordsMap.entrySet().stream()
	                .collect(
	                        Collectors.groupingBy(
	                                el -> (Character.toUpperCase(el.getKey().charAt(0)) - 'A') / bucketSize,
	                                Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
	                        )
	                );
	}
	
	/**
	 * This methods saves map to four different files. Here we are using lambda expression which wraps stream processing
	 * so we don't need to deal with checked exceptions inside stream
	 * 
	 * @param wordsMap
	 */
	private void saveResultFiles(Map<Integer, Map<String, Long>> wordsMap) {
		for (Entry<Integer, Map<String, Long>> entry : wordsMap.entrySet())
		{
			Path file = Paths.get(FILE_NAME + entry.getKey() + FILE_FORMAT);
			try {
				Files.write(file, () -> entry.getValue().entrySet().stream()
					    .<CharSequence>map(e -> e.getKey() + DATA_SEPARATOR + e.getValue())
					    .iterator());
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
	
}
