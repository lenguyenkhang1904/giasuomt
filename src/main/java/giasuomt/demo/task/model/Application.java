package giasuomt.demo.task.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import giasuomt.demo.comment.model.ApplicationComment;
import giasuomt.demo.comment.model.Comment;
import giasuomt.demo.commondata.model.AbstractEntity;
import giasuomt.demo.job.model.Job;
import giasuomt.demo.person.model.Person;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "application")
@Getter
@Setter
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" })
public class Application extends AbstractEntity {
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "task_id")
	@JsonIgnore
	private Task task;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "person_id")
	private Person person;
	
	//Comments
    @OneToMany(mappedBy = "application")
    @JsonIgnore
    private List<ApplicationComment> comments=new ArrayList<>();
    
//    //Đánh dấu
//	@OneToMany(mappedBy = "application", fetch = FetchType.EAGER)
//	private Set<ApplicationSign> applicationSigns;

	
	//Application này trở thành Job (nếu có)
//	@OneToOne(mappedBy = "application",fetch =FetchType.EAGER)
//JsonIgnore
//	private Job job;
	
	
	
	//getter,setter
	
}
