package giasuomt.demo.task.dto;

import java.util.HashSet;
import java.util.LinkedList;


import java.util.List;
import java.util.Set;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import giasuomt.demo.commondata.util.Gender;
import giasuomt.demo.commondata.util.HienDangLa;
import giasuomt.demo.educational.model.SubjectGroup;
import giasuomt.demo.finance.util.AmoutPerTime;
import giasuomt.demo.finance.util.UnitOfMoney;
import giasuomt.demo.location.model.TaskPlaceAddress;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseTaskForWebDto {
	
	private String id;
	
	private String requireApperance;

	private String requireNote;
	
	private String subjectApperance;

	private String subjectNote;
	
	private Set<ResponseTaskPlaceAddressDto> taskPlaceAddresses = new HashSet<>();
	
	private int lessonNumber; // Số buổi

	
	private AmoutPerTime lessonNumberPerTime; // Số buổi tính theo

	private float hour; // Số giờ


	private AmoutPerTime hourPerTime; // Số giờ tính theo
	
	private String freeTime;
	
	private int salary;
	
	private UnitOfMoney unitOfSalary;
	
	private AmoutPerTime salaryPerTime;
	

	
	private String taskPlaceType;
	

	private Set<Gender> genderRequired=new HashSet<>();
	
	private Set<SubjectGroup> subjectGroups=new HashSet<>();
	
	private Set<HienDangLa> hienDangLaRequired=new HashSet<>();
	
	// Số học viên
		private String learnerNumber;
	//Ghi Chú cho gia sư
		private String taskNote;
}
