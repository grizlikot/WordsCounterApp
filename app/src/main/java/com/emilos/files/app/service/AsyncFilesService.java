package com.emilos.files.app.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * Ervin Milos
 *
 * Separate service class for asynchronous operations.
 * It was moved to the separate class because Async annotation won't work within the same class (the original method will be called)
 * 
 */
@Service
public class AsyncFilesService {
	
	public static final String LINE_DELIMITER = " ";

	/**
	 * This method asynchronously processes file collecting from it all words and putting to list
	 * 
	 * @param MultipartFile file
	 * @return Future<List><String>> processFile - list of words collected from the file
	 */
	@Async
	public Future<List<String>> processFile(MultipartFile file) {
		try {
			return new AsyncResult<>(new BufferedReader(new InputStreamReader(file.getInputStream())).lines()
					.map(line -> line.split(LINE_DELIMITER))
					.flatMap(Arrays::stream)
					.collect(Collectors.toList()));	
		} catch (IOException e) {
			return AsyncResult.forExecutionException(e);
		}
	}
}
