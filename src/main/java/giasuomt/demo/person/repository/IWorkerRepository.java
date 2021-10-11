package giasuomt.demo.person.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import giasuomt.demo.person.model.Worker;

@Repository
public interface IWorkerRepository extends JpaRepository<Worker, Long> {

}
