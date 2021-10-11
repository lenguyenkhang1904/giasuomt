package giasuomt.demo.person.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import giasuomt.demo.commondata.util.DateTimeUtils;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class SaveWorkerDto {
	
	private Long id;
	
	private String confirmImgs;

	private String company;
	
	private String jobPosition;

	private LocalDateTime companyUpdatedAt;

}
