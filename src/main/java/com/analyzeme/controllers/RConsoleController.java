package com.analyzeme.controllers;

import com.analyzeme.R.facade.TypeOfReturnValue;
import com.analyzeme.analyzers.r.RAnalyzer;
import com.analyzeme.analyzers.r.TypeOfCall;
import com.analyzeme.repository.FileInfo;
import com.analyzeme.repository.ProjectInfo;
import com.analyzeme.repository.UsersRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Ольга on 12.04.2016.
 */
@RestController
public class RConsoleController {

	RAnalyzer rAnalyzer = new RAnalyzer();

	/**
	 * @param userId
	 * @param projectId
	 * @param typeOfCall
	 * @param typeOfResult
	 * @param scriptName
	 * @param scriptText
	 * @return ResponseEntity<> with
	 * HttpStatus.Conflict if there was an exception during running the script
	 * HttpStatus.Accepted and result if ran successfully
	 */

	@RequestMapping(value = "/{user_id}/{project_id}/run/script", method = RequestMethod.POST)
	public ResponseEntity<Object> runRForNumber(@PathVariable("user_id") int userId,
												@PathVariable("project_id") String projectId,
												@RequestHeader("type_of_call") TypeOfCall typeOfCall,
												@RequestHeader("type_of_result") TypeOfReturnValue typeOfResult,
												@RequestHeader("name") String scriptName,
												@RequestBody String scriptText) {
		Object result;
		try {
			result = rAnalyzer.runScript(userId, projectId, scriptName, scriptText, typeOfResult, typeOfCall);
		} catch (Exception e) {
			return new ResponseEntity<Object>(HttpStatus.CONFLICT);
		}
		return new ResponseEntity<Object>(result, HttpStatus.ACCEPTED);
	}


	/**
	 * saves script for user into concrete project
	 *
	 * @param userId     unique user id
	 * @param projectId  unique project id
	 * @param scriptName unique script name
	 * @return unique name
	 * @throws Exception
	 */
	@RequestMapping(value = "/{user_id}/{project_id}/save/script", method = RequestMethod.POST)
	public String saveScript(@PathVariable("user_id") int userId,
							 @PathVariable("project_id") String projectId,
							 @RequestHeader("name") String scriptName,
							 @RequestBody String scriptText) throws Exception {
		if (UsersRepository.getRepo().checkInitialization() == null) {
			return null;
		}
		ProjectInfo project = UsersRepository.getRepo().findUser(userId).getProjects().findProjectById(projectId);
		if (project == null) {
			return null;
		}
		return project.addNewFile(scriptName, scriptText);
	}

	/**
	 * (has problems with repeating names)
	 * doesn't distinguish script and not-script files yet
	 * <p/>
	 * gets file by its unique name
	 *
	 * @param scriptName unique script name
	 * @return file data in String
	 * @throws Exception
	 */
	@RequestMapping(value = "/{user_id}/{project_id}/get/script", method = RequestMethod.GET)
	public String getScript(@PathVariable("user_id") int userId,
							@PathVariable("project_id") String projectId,
							@RequestHeader("name") String scriptName) throws Exception {

		if (UsersRepository.getRepo().checkInitialization() == null) {
			return null;
		}
		FileInfo file = UsersRepository.getRepo().findFile(scriptName, new String[]{String.valueOf(userId), projectId});
		if (file == null) {
			return null;
		}
		return IOUtils.toString(file.getData());
	}
}