package giasuomt.demo.person.dto;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import giasuomt.demo.commondata.dto.SavePersonDto;
import giasuomt.demo.location.dto.SaveRegisterAndLearnerAddressDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveRegisterAndLearnerDto extends SavePersonDto implements Serializable {
	
	private String vocative;
	

	
	private Set<SaveRegisterAndLearnerAddressDto> registerAndLearnerAddresses = new HashSet<>();// không có trước đó

	// HIỆN ĐANG LÀ
	// Lưu Student


	// luu School Teacher
	private List<SaveSchoolerDto> schoolers = new LinkedList<>();

	// luu worker

	
	
	// TAGS
	private String registerAndLearnerTags;
	

	private String note;
	
	
	
	//RELATIONSHIP
	private Set<SaveRegisterAndLearnerRelationshipDto> registerAndLearnerRelationships = new HashSet<>();
	
}
