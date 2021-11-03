package giasuomt.demo.commondata.generic;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import giasuomt.demo.commondata.model.AbstractEntity;


public abstract class GenericService<DTO, T extends AbstractEntity, ID> implements IGenericService<DTO, T, ID> {
	@Autowired
	private JpaRepository<T, ID> repository; // Cần phải viết @Component GenericRepository (viết ở trong JpaConfig.java)
												// nó mới inject cái repository này được
	

	@Override
	public List<T> findAll() {
		return repository.findAll();
	}
	
//	@CachePut(value = "entity", key = "#entity.id")
	@Override
	public T save(DTO dto, T entity) {
		try {

			return repository.save(entity);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	//@Cacheable(value = "areas", key = "#id")
	@Override
	public Optional<T> findById(ID id) {
		return repository.findById(id);
	}

	@Override
	public void deleteById(ID id) {
		try {

			repository.deleteById(id);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean checkExistIdOfT(ID id) {

		return repository.existsById(id);
	}
}
