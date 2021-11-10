package giasuomt.demo.person.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import giasuomt.demo.commondata.dto.SaveUserDto;
import giasuomt.demo.person.model.Worker;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SavePersonDto extends SaveUserDto implements Serializable{

	private Long id;

	// THÔNG TIN CÁ NHÂN
	private String tempAddNo;

	private String tempAddSt;

	private String tempAddNote;

	private Long tempAreaId;

	private String perAddNo;

	private String perAddSt;

	private String perAddNote;

	private Long perAreaId;

	private String iDNo;

	private LocalDateTime issuedOn;

	private String infoImgs;

	// NĂNG LỰC:
	private String voices;

	private String tutorNotices;

	private String advantageNote;

	// ĐĂNG KÝ NHẬN LỚP

	// NHẬN LỚP

	// VỊ TRÍ TƯƠNG ĐỐI CỦA GIA SƯ
	private String xRelCoo;

	private String yRelCoo;

	private Long relAreaId;

	// HIỆN ĐANG LÀ
	// Lưu Student
	private List<SaveStudentDto> saveStudentDtos = new LinkedList<>();

	// Lưu GraduatedStudent
	private List<SaveGraduatedStudentDto> saveGraduatedStudentDtos = new LinkedList<>();

	// luu Institution Teacher
	private List<SaveInstitutionTeacherDto> saveInstitutionTeacherDtos = new LinkedList<>();

	// luu School Teacher
	private List<SaveSchoolTeacherDto> saveSchoolTeacherDtos = new LinkedList<>();

	// luu schooler
	private List<SaveSchoolerDto> saveSchoolerDtos = new LinkedList<>();

	// luu worker
	private List<SaveWorkerDto> saveWorkerDtos = new LinkedList<>();

	// Certificate
	private List<Long> certificateIds = new LinkedList<>();

	// RelationShip
	private List<SaveRelationshipDto> saveRelationshipDtosWith = new LinkedList<>();

	// RegisteredUser
	
	
	//Arvatar
	private Long idAvatar;
	
	private String tutorCode;
}
