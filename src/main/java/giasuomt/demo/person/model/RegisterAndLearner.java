package giasuomt.demo.person.model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import giasuomt.demo.commondata.model.Person;
import giasuomt.demo.location.model.RegisterAndLearnerAddress;
import giasuomt.demo.task.model.Registration;
import giasuomt.demo.user.model.User;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "register_and_learner")
@Getter
@Setter
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" })
@NamedEntityGraph(name = "registerAndLearner", attributeNodes = {

		@NamedAttributeNode("publicImgs"), @NamedAttributeNode("privateImgs"),
		@NamedAttributeNode("registerAndLearnerAddresses"),
		@NamedAttributeNode("relationshipWith"),

})
public class RegisterAndLearner extends Person {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String vocative;

	@OneToMany(mappedBy = "registerAndLearner", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<RegisterAndLearnerAddress> registerAndLearnerAddresses = new HashSet<>();// không có trước đó

	// MEDIA
	private String avatar;

	@ElementCollection
	private Set<String> publicImgs = new HashSet<>();

	@ElementCollection
	private Set<String> privateImgs = new HashSet<>();

	// HIỆN ĐANG LÀ
	@OneToMany(mappedBy = "registerAndLearner", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Schooler> schoolers = new LinkedList<>();

    private String registerAndLearnerTags;

	// RegisterAndLearner RELATIONSHIP:
	@OneToMany(mappedBy = "personA", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<RegisterAndLearnerRelationship> relationshipWith = new HashSet<>();

	@OneToMany(mappedBy = "personB", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private Set<RegisterAndLearnerRelationship> relationshipBy = new HashSet<>();

	private String note;

	@OneToOne(mappedBy = "registerAndLearner")
	@JsonIgnore
	private User user;

//LEARNER/REGISTER	
	@OneToMany(mappedBy = "registerAndLearner")
	@JsonIgnore
	private Set<Registration> registrations = new HashSet<>();

	@OneToMany(mappedBy = "registerAndLearner")
	@JsonIgnore
	private Set<TutorInvitation> tutorInvitations = new HashSet<>();

	@OneToMany(mappedBy = "registerAndLearner")
	@JsonIgnore
	private Set<TutorInterest> tutorInterests = new HashSet<>();

	public void addRegisterAndLearnerAddress(RegisterAndLearnerAddress registerAndLearnerAddress) {
		registerAndLearnerAddress.setRegisterAndLearner(this);
		this.registerAndLearnerAddresses.add(registerAndLearnerAddress);
	}

	public void removeRegisterAndLearnerAddress(RegisterAndLearnerAddress registerAndLearnerAddress) {
		this.registerAndLearnerAddresses.remove(registerAndLearnerAddress);
	}

	public void addSchooler(Schooler schooler) {
		schooler.setRegisterAndLearner(this);
		this.schoolers.add(schooler);
	}

	public void removeSchooler(Schooler schooler) {
		this.schoolers.remove(schooler);
	}

	public void removeRelationshipWith(RegisterAndLearnerRelationship relationship) {
		this.relationshipWith.remove(relationship);
	}

	public void addRelationshipWith(RegisterAndLearnerRelationship relationship) {
		relationship.setPersonA(this);
		this.relationshipWith.add(relationship);
	}
}
