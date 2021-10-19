package giasuomt.demo.commondata.generic;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import giasuomt.demo.commondata.model.AbstractEntity;
import giasuomt.demo.location.dto.UpdateAreaDTO;
import giasuomt.demo.location.model.Area;

public abstract class GenericService<T extends AbstractEntity, ID> implements IGenericService<T, ID> {
	@Autowired 
	private JpaRepository<T, ID> repository;  //Cần phải viết @Component GenericRepository (viết ở trong JpaConfig.java) nó mới inject cái repository này được
	
	@Override
	public List<T> findAll() {
		return repository.findAll();
	}
	
	@Override
	public Optional<T> findById(ID id) {
		return repository.findById(id);
	}

	@Override
	public T save(T entity) {
		return repository.save(entity);
	}

	@Override
	public T update(T entity) {
		return repository.save(entity);
	}

	@Override
	public void deleteById(ID id) {
		repository.deleteById(id);
	}

	public Area update(UpdateAreaDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

	public Area finddById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
}
