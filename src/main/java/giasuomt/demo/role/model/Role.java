package giasuomt.demo.role.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import giasuomt.demo.commondata.model.AbstractEntity;
import giasuomt.demo.user.model.User;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "role")
@Getter
@Setter
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" })
public class Role extends AbstractEntity {
	
	private String name;

	private String description;
	
	@ManyToMany(mappedBy = "roles",fetch = FetchType.EAGER)
	@JsonIgnore
	private Set<User> users=new HashSet<>();
	
	public Role addRoleName(String name)
	{
		this.name=name;
		return this;
	} 
}
