package giasuomt.demo.job.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import giasuomt.demo.commondata.model.AbstractEntityNotId;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Tutor_By_The_Time_Creating_Job")
@Getter
@Setter
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" })
public class TutorByTheTimeCreatingJob extends AbstractEntityNotId {
	// @Column(updatable = false) //Column này ko update được
	// @Column(unique = true)
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long keyId;
	private Long id; // Cần viết tự generate theo dạng 8 số
	private String tempAddNo;
	private String tempAddSt;
	private String tempAddNote;
	private String perAddNo;
	private String perAddSt;
	private String perAddNote;
	// VỊ TRÍ TƯƠNG ĐỐI CỦA GIA SƯ - vị trí này được xác định theo: vị trí các lớp
	// đã nhận (trọng số theo thời gian và số lớp), các lớp đã đăng ký (trọng số
	// theo thời gian và số lớp), nơi ở hiện tại mà gia sư khai báo (trọng số theo
	// thời gian và nơi ở là tạm trú hay thường trú)
	private String xRelCoo;
	private String yRelCoo;
	// MEDIA
	private String avatar;
	private String publicImgs;
	private String privateImgs;
	private String infoImgs;
	private String tutorNotices;
	private String advantageNote;
	private String tutorTag;
}
