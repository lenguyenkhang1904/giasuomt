package giasuomt.demo.person.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import giasuomt.demo.person.model.Schooler;

@Repository
public interface ISchoolerRepository extends JpaRepository<Schooler, Long> {

}
