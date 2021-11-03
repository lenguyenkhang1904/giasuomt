package giasuomt.demo.staff.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import giasuomt.demo.commondata.responseHandler.ResponseHandler;
import giasuomt.demo.location.repository.IAreaRepository;
import giasuomt.demo.person.model.Person;
import giasuomt.demo.staff.dto.TaskListResponse;
import giasuomt.demo.staff.service.IMainStaffService;
import giasuomt.demo.staff.service.MainStaffService;
import giasuomt.demo.task.model.Application;
import lombok.AllArgsConstructor;
@RequestMapping("/api/mainstaff")
@RestController
@AllArgsConstructor
public class MainStaffController {
	

	 
	private IMainStaffService mainStaffService;
	
	@GetMapping("/findAll")
	public ResponseEntity<Object> findall() {
		
		List<TaskListResponse> mainList=mainStaffService.findAllTask(); 
		
		List<Person> personBelongToTaskList=mainStaffService.findAllPersonBelongToTask();
		if(mainList==null && personBelongToTaskList!=null)
			return ResponseHandler.getResponse("no content",personBelongToTaskList, HttpStatus.BAD_REQUEST);
		else if(mainList!=null && personBelongToTaskList==null)
			return ResponseHandler.getResponse(mainList,"no content", HttpStatus.BAD_REQUEST);
		else if(mainList!=null && personBelongToTaskList==null)
			return ResponseHandler.getResponse("no content","no content", HttpStatus.BAD_REQUEST);

			
		return ResponseHandler.getResponse(mainList,personBelongToTaskList, HttpStatus.OK);
	}
}
