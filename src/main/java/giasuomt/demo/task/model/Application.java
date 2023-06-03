package giasuomt.demo.task.model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
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
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import giasuomt.demo.comment.model.ApplicationComment;
import giasuomt.demo.commondata.model.AbstractEntity;
import giasuomt.demo.commondata.model.AbstractEntityNotId;
import giasuomt.demo.person.model.Tutor;
import giasuomt.demo.task.util.ApplicationSign;
import giasuomt.demo.task.util.TaskSign;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "application")
@Getter
@Setter
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" })
@NamedEntityGraph(name = "application",attributeNodes = {
		@NamedAttributeNode(value="tutor",subgraph = "tutor-subclass"),
		@NamedAttributeNode("applicationSigns")
},
subgraphs = {
		@NamedSubgraph(name="tutor-subclass",
				attributeNodes = {
				
						@NamedAttributeNode("relArea"),
						@NamedAttributeNode("tutorAddressArea"),
						@NamedAttributeNode("subjectGroupMaybes"),
						@NamedAttributeNode("subjectGroupSures"),
						@NamedAttributeNode("voices")	
				} )
}

		)
public class Application extends AbstractEntityNotId {

	
	@Id
	private String id;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "task_id")
	@JsonIgnore
	private Task task;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "tutor_id")
	private Tutor tutor;

	// Comments
	@OneToMany(mappedBy = "application")
	private Set<ApplicationComment> comments = new HashSet<>();

//    //Đánh dấu
	@ElementCollection(targetClass = ApplicationSign.class)
	@Enumerated(EnumType.STRING)
	private Set<ApplicationSign> applicationSigns = new HashSet<>();

	// Application này trở thành Job (nếu có)
//	@OneToOne(mappedBy = "application",fetch =FetchType.EAGER)
//JsonIgnore
//	private Job job;

	// getter,setter

}
