package com.emilos.files.app.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.emilos.files.app.service.FilesService;

/**
 * 
 * @author Ervin Milos
 * 
 * FilesController handles all requests from web application
 */
@RestController
@RequestMapping("/files")
public class FilesController {

	@Autowired
	private FilesService filesService;

	/**
	 * This method retrieves request from web application and calls method which counts words in files
	 * 
	 * @param MultipartFile[] - files retrieved from web application
	 * @return Map that contains key as number of file and value as Map of words and amounts
	 */
	@PostMapping("/count")
	public ResponseEntity<Map<Integer, Map<String, Long>>> countWordsInFiles(@RequestParam("files") MultipartFile[] files) {
		Map<Integer, Map<String, Long>> results = filesService.countWordsInFiles(files);
		return new ResponseEntity<>(results, HttpStatus.OK);
	}
}
