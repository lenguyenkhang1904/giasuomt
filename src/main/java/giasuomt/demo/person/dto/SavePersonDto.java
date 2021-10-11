package giasuomt.demo.person.dto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import giasuomt.demo.commondata.dto.SaveUserDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SavePersonDto extends SaveUserDto {
	
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
	private List<SaveStudentDto> saveStudentDtos = new ArrayList<>();
	
	// Lưu GraduatedStudent
	private List<SaveGraduatedStudentDto> saveGraduatedStudentDtos = new ArrayList<>();
	
	// luu Institution Teacher
	private List<SaveInstitutionTeacherDto> saveInstitutionTeacherDtos = new ArrayList<>();
	
	// luu School Teacher
	private List<SaveSchoolTeacherDto> saveSchoolTeacherDtos = new ArrayList<>();
	
	//luu schooler
	private List<SaveSchoolerDto> saveSchoolerDtos=new ArrayList<>();
	
	//luu workers
	private List<SaveWorkerDto> saveWorkerDtos=new ArrayList<>();
	
	// RegisteredUser
}
