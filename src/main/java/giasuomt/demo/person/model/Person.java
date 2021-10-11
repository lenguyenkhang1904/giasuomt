package giasuomt.demo.person.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import giasuomt.demo.commondata.model.AbstractEntity;
import giasuomt.demo.commondata.util.DateUtils;
import giasuomt.demo.commondata.util.RegisteredUserStatus;
import giasuomt.demo.location.model.Area;
import giasuomt.demo.task.model.Task;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "person")
@Getter
@Setter
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" })
public class Person extends AbstractEntity {
//    @Size(min = 3, max = 50, message = "{user.username.size}")
//    @Column(unique = true) //để các giá trị username ko được trùng nhau
	private String username;

	private String password;

	private String registeredStatus;

//THÔNG TIN CÁ NHÂN
	// @NotBlank
	private String fullName;

	// @NotNull //kiểu Enum mình ko nên để @NotBlank mà nên để @NotNull
	private String gender;

	private String birthYear;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtils.DATE_FORMAT) // Quy định date format khi nó add đối
																					// tượng thành Json để trả về
																					// Clients
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

//HIỆN ĐANG LÀ
	@OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Student> students = new ArrayList<>();

	@OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<GraduatedStudent> graduatedStudents = new ArrayList<>();

	@OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<InstitutionTeacher> institutionTeachers = new ArrayList<>();

	@OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SchoolTeacher> schoolTeachers = new ArrayList<>();

	@OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Schooler> schoolers = new ArrayList<>();

	@OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Worker> workers = new ArrayList<>();

//PERSONAL RELATIONSHIP:
	// @Unique
	@NotBlank
	// @Column(unique = true)
	private String tutorCode; // Cần viết tự generate theo dạng 8 số

	@OneToMany(mappedBy = "personA", cascade = CascadeType.ALL)
	private Set<Relationship> relationshipWith;

	@OneToMany(mappedBy = "personB", cascade = CascadeType.ALL)
	private Set<Relationship> relationshipBy;

//TUTOR:
	private String voices;

	// @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	// @JoinTable(name = "tutor_certificate",
	// joinColumns = @JoinColumn(name = "tutor_id"),
	// inverseJoinColumns = @JoinColumn(name = "certificate_id"))
	// private Set<Certificate> certificates = new HashSet<>();

	private String tutorNotices;

	private String advantageNote;

	// @OneToMany(mappedBy = "tutor", fetch = FetchType.LAZY)
	// private Set<Application> applications;

	// @OneToMany(mappedBy = "tutor", fetch = FetchType.LAZY)
	// private Set<Job> jobs;

//LEARNER/REGISTER
	private String learnerNotices;

	@OneToMany(mappedBy = "register", fetch = FetchType.LAZY)
	@JsonIgnore // Để JSP ignore cột này khi truy vấn, để ko bị lập vô tận
	private Set<Task> registeredTasks;

	@ManyToMany(mappedBy = "learners", fetch = FetchType.LAZY)
	@JsonIgnore // Để JSP ignore cột này khi truy vấn, để ko bị lập vô tận
	private Set<Task> learnedTasks = new HashSet<>();

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
}
