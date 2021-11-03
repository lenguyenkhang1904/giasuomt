package giasuomt.demo.person.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import giasuomt.demo.commondata.config.DaySerializer;
import giasuomt.demo.commondata.model.AbstractEntity;
import giasuomt.demo.commondata.util.DateUtils;
import giasuomt.demo.location.model.Area;
import giasuomt.demo.task.model.Application;
import giasuomt.demo.task.model.Task;
import giasuomt.demo.uploadfile.model.FileEntity;
import giasuomt.demo.uploadfile.model.ResponsiveFile;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "person")
@Getter
@Setter
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" })
public class Person extends AbstractEntity {
//	@Size(min = 3, max = 50, message = "{user.username.size}")
//	@Column(unique = true) // để các giá trị username ko được trùng nhau
	private String username;

	private String password;

	private String registeredStatus;

	private String avatar;

//THÔNG TIN CÁ NHÂN
	// @NotBlank
	private String fullName;

	// @NotNull //kiểu Enum mình ko nên để @NotBlank mà nên để @NotNull
	private String gender;

	private String birthYear;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtils.DATE_FORMAT) // Quy định date format khi nó add đối // tượng thành Json để trả về // Clients																	
	@DateTimeFormat(pattern = DateUtils.DATE_FORMAT) // Quy định date format để lưu xuống database
	private LocalDate birthDate;

	private String phones;

	private String emails;

	private String zaloes;

	private String fbs;

	private String iDNo;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtils.DATE_FORMAT) // Quy định date format khi nó add đối
																					// tượng thành Json để trả về
																					// Clients
	@DateTimeFormat(pattern = DateUtils.DATE_FORMAT) // Quy định date format để lưu xuống database
	private LocalDateTime issuedOn;

	private String tempAddNo;

	private String tempAddSt;

	private String tempAddNote;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "temp_area_id")
	private Area tempArea;

	private String perAddNo;

	private String perAddSt;

	private String perAddNote;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "per_area_id")
	private Area perArea;

//VỊ TRÍ TƯƠNG ĐỐI CỦA GIA SƯ - vị trí này được xác định theo: vị trí các lớp đã nhận (trọng số theo thời gian và số lớp), các lớp đã đăng ký (trọng số theo thời gian và số lớp), nơi ở hiện tại mà gia sư khai báo (trọng số theo thời gian và nơi ở là tạm trú hay thường trú) 
	private String xRelCoo;

	private String yRelCoo;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "rel_area_id")
	private Area relArea;

//MEDIA	
	private String avatars;

	private String publicImgs;

	private String privateImgs;

	private String infoImgs;

//HIỆN ĐANG LÀ
	@OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Student> students = new ArrayList<>();

	@OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<GraduatedStudent> graduatedStudents = new ArrayList<>();

	@OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<InstitutionTeacher> institutionTeachers = new ArrayList<>();

	@OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SchoolTeacher> schoolTeachers = new ArrayList<>();

	@OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Schooler> schoolers = new ArrayList<>();

	@OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Worker> workers = new ArrayList<>();

//PERSONAL RELATIONSHIP:
	@OneToMany(mappedBy = "personA", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Relationship> relationshipWith = new ArrayList<>();

	@OneToMany(mappedBy = "personB", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<Relationship> relationshipBy = new ArrayList<>();

//TUTOR:
	// @Unique
	@NotBlank
	// @Column(unique = true)
	private String tutorCode; // Cần viết tự generate theo dạng 8 số

	private String voices;

	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	@JoinTable(name = "tutor_certificate", joinColumns = @JoinColumn(name = "person_id"), inverseJoinColumns = @JoinColumn(name = "certificate_id"))
	private List<Certificate> certificates = new ArrayList<>();

	private String tutorNotices;

	private String advantageNote;

	@OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Application> applications = new ArrayList<>();

	// @OneToMany(mappedBy = "tutor", fetch = FetchType.LAZY)
	// private Set<Job> jobs;

//LEARNER/REGISTER
	private String learnerNotices;

	@ManyToMany(mappedBy = "registers")
	@JsonIgnore
	private List<Task> registerOfTasks = new ArrayList<>();;

	@ManyToMany(mappedBy = "learners")
	@JsonIgnore
	private List<Task> learnerOfTasks = new ArrayList<>();

//FOR STT Được cấp trong ngày
	private Integer noOfPersonInday;
	
	
	
	
// FOR API SAVE
	public void addSchooler(Schooler schooler) {
		schooler.setPerson(this);
		this.schoolers.add(schooler);
	}

	public void removeSchooler(Schooler schooler) {
		this.schoolers.remove(schooler);
	}

	public void addWorker(Worker worker) {
		worker.setPerson(this);
		this.workers.add(worker);
	}

	public void removeWorker(Worker worker) {
		this.workers.remove(worker);
	}

	public void addStudent(Student student) {
		student.setPerson(this);
		this.students.add(student);
	}

	public void removeStudent(Student student) {
		this.students.remove(student);
	}

	public void addGraduatedStudent(GraduatedStudent graduatedStudent) {
		graduatedStudent.setPerson(this);
		this.graduatedStudents.add(graduatedStudent);
	}

	public void removeGraduatedStudent(GraduatedStudent graduatedStudent) {
		this.graduatedStudents.remove(graduatedStudent);
	}

	public void addInstitutionTeacher(InstitutionTeacher institutionTeacher) {
		institutionTeacher.setPerson(this);
		this.institutionTeachers.add(institutionTeacher);
	}

	public void removeInstitutionTeacher(InstitutionTeacher institutionTeacher) {
		this.institutionTeachers.remove(institutionTeacher);
	}

	public void addSchoolTeacher(SchoolTeacher schoolTeacher) {
		schoolTeacher.setPerson(this);
		this.schoolTeachers.add(schoolTeacher);
	}

	public void removeSchoolTeacher(SchoolTeacher schoolTeacher) {
		this.schoolTeachers.remove(schoolTeacher);
	}

	public void removeRelationshipWith(Relationship relationship) {
		this.relationshipWith.remove(relationship);
	}

	public void addRelationshipWith(Relationship relationship) {
		relationship.setPersonA(this);
		this.relationshipWith.add(relationship);
	}
}
