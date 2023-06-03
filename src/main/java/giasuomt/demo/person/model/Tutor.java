package giasuomt.demo.person.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import giasuomt.demo.commondata.model.Person;
import giasuomt.demo.commondata.util.Calendar;
import giasuomt.demo.commondata.util.DateTimeUtils;
import giasuomt.demo.educational.model.SubjectGroup;
import giasuomt.demo.job.model.Job;
import giasuomt.demo.location.model.Area;
import giasuomt.demo.task.model.Application;
import giasuomt.demo.tutorReview.model.TutorReview;
import giasuomt.demo.user.model.User;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Tutor")
@Getter
@Setter
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" })
public class Tutor extends Person { 
	// @Column(updatable = false) //Column này ko update được
	// @Column(unique = true)
	@Id
	private Long id; // Cần viết tự generate theo dạng 8 số


	private  String tutorAddress;

	@ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name = "tutor_Address_area_id")
	private Area tutorAddressArea;

//VỊ TRÍ TƯƠNG ĐỐI CỦA GIA SƯ - vị trí này được xác định theo: vị trí các lớp đã nhận (trọng số theo thời gian và số lớp), các lớp đã đăng ký (trọng số theo thời gian và số lớp), nơi ở hiện tại mà gia sư khai báo (trọng số theo thời gian và nơi ở là tạm trú hay thường trú) 
	private String xRelCoo;

	private String yRelCoo;
 
	
	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	@JoinTable(name = "tutor_area_rel", joinColumns = @JoinColumn(name = "tutor_id"), inverseJoinColumns = @JoinColumn(name = "tutor_area_id"))
	private Set<Area> relArea=new HashSet<>();

//MEDIA	
	private String avatar;

	
	@ElementCollection
	private Set<String> publicImgs=new HashSet<>();

	
	@ElementCollection
	private Set<String> privateImgs=new HashSet<>();

	private String expNotices;
//HIỆN ĐANG LÀ
	
	
	private String  hienDangLa;
	
	private String  nowLevel;
	
	@LastModifiedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.DATE_TIME_FORMAT) //Quy định date format khi nó add đối tượng thành Json để trả về Clients
	@DateTimeFormat(pattern = DateTimeUtils.DATE_TIME_FORMAT) 
	private LocalDateTime nowLevelUpdatedAt;
	
	private String studyingInsitution;
	
	private String teachingInstitution;
	
	private String major;



//PERSONAL RELATIONSHIP:
//	@OneToMany(mappedBy = "personA", cascade = CascadeType.ALL, orphanRemoval = true)
//	private List<Relationship> relationshipWith = new LinkedList<>();
//
//	@OneToMany(mappedBy = "personB", cascade = CascadeType.ALL, orphanRemoval = true)
//	@JsonIgnore
//	private List<Relationship> relationshipBy = new LinkedList<>();

//TUTOR:
	

	private String voices;

	private String tutorTag;

	private String tutorNotices;

	private String advantageNote;

	@OneToMany(mappedBy = "tutor", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Application> applications = new LinkedList<>();

	@OneToMany(mappedBy = "tutor", fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<Job> jobs = new HashSet<>();

	private Double exp;

	
	@OneToOne(mappedBy = "tutor")
	@JsonIgnore
	private User user;
	
	//Subject Group
	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	@JoinTable(name = "task_subjectGroupMaybe", joinColumns = @JoinColumn(name = "tutor_id"), inverseJoinColumns = @JoinColumn(name = "subjectGroup_id"))
	private Set<SubjectGroup> subjectGroupMaybes=new HashSet<>();

	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	@JoinTable(name = "task_subjectGroupSure", joinColumns = @JoinColumn(name = "tutor_id"), inverseJoinColumns = @JoinColumn(name = "subjectGroup_id"))
	private Set<SubjectGroup> subjectGroupSures=new HashSet<>();
	
	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	@JoinTable(name = "task_subjectGroupFail", joinColumns = @JoinColumn(name = "tutor_id"), inverseJoinColumns = @JoinColumn(name = "subjectGroup_id"))
	private Set<SubjectGroup> subjectGroupFails=new HashSet<>();

	private String subject;
	
	private String subjectClass;
	
	//tutor
	@OneToMany(mappedBy = "tutor")
	@JsonIgnore
	private Set<TutorReview> tutorReviews;
	
	private Double averageStarNumbers;
	
	@ElementCollection(targetClass = Calendar.class)
	@Enumerated(EnumType.STRING)
	private Set<Calendar> calendars=new HashSet<>(); 
	
	
	@OneToMany(mappedBy = "tutor")
	@JsonIgnore
	private Set<TutorInvitation> tutorInvitations=new HashSet<>();
	
	@OneToMany(mappedBy = "tutor")
	@JsonIgnore
	private Set<TutorInterest> tutorInterests=new HashSet<>();
	
	
	private String placeOfBirth;
	
	private Integer successJobsNumbers = 0;


	@Override
	public String toString() {
		return "Tutor [id=" + id + ", tutorAddress=" + tutorAddress + ", tutorAddressArea=" + tutorAddressArea
				+ ", xRelCoo=" + xRelCoo + ", yRelCoo=" + yRelCoo + ", relArea=" + relArea + ", avatar=" + avatar
				+ ", publicImgs=" + publicImgs + ", privateImgs=" + privateImgs + ", expNotices=" + expNotices
				+ ", hienDangLa=" + hienDangLa + ", nowLevel=" + nowLevel + ", nowLevelUpdatedAt=" + nowLevelUpdatedAt
				+ ", studyingInsitution=" + studyingInsitution + ", teachingInstitution=" + teachingInstitution
				+ ", major=" + major + ", voices=" + voices + ", tutorTags=" + "" + ", tutorNotices="
				+ tutorNotices + ", advantageNote=" + advantageNote + ", applications=" + applications + ", jobs="
				+ jobs + ", exp=" + exp + ", user=" + user + ", subjectGroupMaybes=" + subjectGroupMaybes
				+ ", subjectGroupSures=" + subjectGroupSures + ", subject=" + subject + ", subjectClass=" + subjectClass
				+ ", tutorReviews=" + tutorReviews + ", averageStarNumbers=" + averageStarNumbers + ", calendars="
				+ calendars + ", tutorInvitations=" + tutorInvitations + ", tutorInterests=" + tutorInterests
				+ ", placeOfBirth=" + placeOfBirth + "]";
	}

// FOR API SAVE



}
