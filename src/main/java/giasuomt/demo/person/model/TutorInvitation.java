package giasuomt.demo.person.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import giasuomt.demo.commondata.model.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Invitation_Tutor")
@Getter
@Setter
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" })
public class TutorInvitation extends AbstractEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	private RegisterAndLearner registerAndLearner;

	private String learnerAndReqisterPhone;

	@ManyToOne(fetch = FetchType.LAZY)
	private Tutor tutor;

	private String note;
}
