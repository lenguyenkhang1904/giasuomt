package giasuomt.demo.person.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import giasuomt.demo.person.model.RegisterAndLearnerRelationship;

@Repository
public interface IRegisterAndLearnerRelationshipRepository extends JpaRepository<RegisterAndLearnerRelationship, Long> {

	
	
}