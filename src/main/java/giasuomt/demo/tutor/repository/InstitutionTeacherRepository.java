package giasuomt.demo.tutor.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import giasuomt.demo.commondata.config.JpaConfig.IGenericRepository;
import giasuomt.demo.tutor.model.InstitutionTeacher;
@Repository
public interface InstitutionTeacherRepository extends JpaRepository<InstitutionTeacher, Long> {
	@Query("SELECT s.id FROM InstitutionTeacher s WHERE s.tutor.id=:id")
	Set<Long> findInstitutionTeacherIdByTutorId(@Param("id") Long id);
}