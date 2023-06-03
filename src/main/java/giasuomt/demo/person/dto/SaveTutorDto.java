package giasuomt.demo.person.dto;

import java.io.Serializable;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import giasuomt.demo.commondata.dto.SavePersonDto;
import giasuomt.demo.commondata.util.DateTimeUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveTutorDto extends SavePersonDto implements Serializable {

	// Arvatar

	// THÔNG TIN CÁ NHÂN

	private String tutorAddressAreaId;

	private String tutorAddress;

	private String placeOfBirth;

	// NĂNG LỰC:
	private String voices;

	private String tutorNotices;

	private String advantageNote;

	// ĐĂNG KÝ NHẬN LỚP

	// NHẬN LỚP

	// VỊ TRÍ TƯƠNG ĐỐI CỦA GIA SƯ
	private String xRelCoo;

	private String yRelCoo;
	
	private Set<String> relAreaIds = new HashSet<>();

	// HIỆN ĐANG LÀ

	private String hienDangLa;

	private String studyingInsitution;

	private String teachingInstitution;

	private String major;

	// TAGS
	private String tutorTag;

	private String nowLevel;

	private String subject;

	private String subjectClass;

	private String expNotices;

	// Registered Subjects

}
