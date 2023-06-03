package giasuomt.demo.job.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import giasuomt.demo.commondata.model.AbstractEntityNotId;
import giasuomt.demo.finance.model.JobFinance;
import giasuomt.demo.person.model.Tutor;
import giasuomt.demo.task.model.Application;
import giasuomt.demo.task.model.Task;
import giasuomt.demo.tutorReview.model.TutorReview;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "job")
@Getter
@Setter
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" })
@NamedEntityGraph(name = "job", attributeNodes = { @NamedAttributeNode("retainedImgsIdentification"),
		@NamedAttributeNode(value = "tutor", subgraph = "tutor-subclass"), @NamedAttributeNode("jobFinances"),
		@NamedAttributeNode("jobProgresses"), @NamedAttributeNode("application"),
		@NamedAttributeNode("taskByTheTimeCreatingJob"), @NamedAttributeNode("tutorByTheTimeCreatingJob"),
		@NamedAttributeNode(value = "task", subgraph = "task-subclass"),

}, subgraphs = { @NamedSubgraph(name = "tutor-subclass", attributeNodes = {

 @NamedAttributeNode("relArea"), @NamedAttributeNode("subjectGroupMaybes"),
		@NamedAttributeNode("subjectGroupSures"), @NamedAttributeNode("voices") }),
		@NamedSubgraph(name = "task-subclass", attributeNodes = { @NamedAttributeNode("taskPlaceAddresses"),
				@NamedAttributeNode("subjectGroups"), @NamedAttributeNode("taskSign"),
				@NamedAttributeNode("registrations"), @NamedAttributeNode("jobs"),
				@NamedAttributeNode("genderRequired"), @NamedAttributeNode("voiceRequired"),
				@NamedAttributeNode("hienDangLaRequired"), @NamedAttributeNode("taskSign") })

})
public class Job extends AbstractEntityNotId {
	@Id
	private String id;
	@ManyToOne
	@JoinColumn(name = "task_id")
	@JsonIgnore
	private Task task;
	@ManyToOne
	@JoinColumn(name = "tutor_id")
	private Tutor tutor;
	// Lấy application để lấy comment
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "application_id", referencedColumnName = "id")
	private Application application;
//Thông tin Task và Tutor tại thời điểm giao (để lưu trữ lại)	
	@OneToOne(fetch = FetchType.LAZY)
	private TaskByTheTimeCreatingJob taskByTheTimeCreatingJob;
	@OneToOne(fetch = FetchType.LAZY)
	private TutorByTheTimeCreatingJob tutorByTheTimeCreatingJob;
//Link GGT, Link HĐ, Hình thức giao (hợp đồng đc kí tại)
//	@OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
//	@JoinColumn(name = "referral_id")
//	private Referral referral;
//	@OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
//	@JoinColumn(name = "contract_id")
//	private Contract contract;
//	private String contractSignAt; //Online, Offline, hoặc tại địa chỉ chi nhánh nào đó
//Nhân viên giao lớp đã xác minh, và dặn dò Tutor tại thời điểm giao:
	private String verifiedTutorInfo; // - Đã xác nhận: hình, CMND, thẻ SV, thẻ GV, bằng cấp, chứng chỉ, thành tích,
										// kinh nghiệm, năng lực

	private String adviceToTutor; // Đã dặn dò:gọi PH ngay, gọi xong báo, báo sau khi dạy 1 buổi đầu, báo ngay khi
									// có vấn đề

//Giấy tờ giữ lại
	@ElementCollection // nếu sử dụng kiểu dữ liệu List vâng vâng
	private Set<String> retainedImgsIdentification = new HashSet<>();
//TÀI CHÍNH	
	@OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
	private Set<JobFinance> jobFinances = new HashSet<>();
//TÌNH HÌNH
	@OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
//	@Fetch(FetchMode.SUBSELECT)
	private Set<JobProgress> jobProgresses = new HashSet<>();
//KẾT QUẢ
	@Enumerated(EnumType.STRING)
	private JobResult jobResult;
	private String failReason;
//(Tìm thêm gia sư nếu fail?) YES or NO
	@Type(type = "true_false") // xác định cách biểu diễn boolean khi lưu xuống db
	private Boolean findAnotherTutorIfFail;
//Tutor Review
	@OneToMany(mappedBy = "job")
	@JsonIgnore
	private Set<TutorReview> tutorReviews = new HashSet<>();

}
