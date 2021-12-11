package giasuomt.demo.job.dto;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveJobDto {
	
	private Long id;
	
	private String taskId;
	
	private Long tutorId;
	
	private Long applicationId;
	
	private List<Long> retainedImgsIdentificationId=new LinkedList<>();
	
	private String verifiedTutorInfo; 

	private String adviceToTutor;

	private String retainedIdentification;
	

}
