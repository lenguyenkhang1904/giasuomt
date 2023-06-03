package giasuomt.demo.commondata.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Component;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware") 
public class JpaConfig {

	// Auditor Aware là cấu hình để dùng được các annotation @CreatedBy, @ModifiedBy
	// ở bên các model (AbstractEntity)
	@Bean
	AuditorAware<String> auditorAware() {
		return new AuditorAwareImpl(); // AuditorAwareImpl() viết ở AuditorAwareImpl.java -> để lấy thông tin user
										// trong Security Context
	}
	@Component
	public interface IGenericRepository<T , ID> extends JpaRepository<T, ID> { // viết cái này để bên GenericService nó
																			// có thể inject cái @Autowired private
													// JpaRepository<T, ID> repository;
	}
}
//@EnableJpaAuditing để sử dụng được
// @CreatedDate,@LastModifiedDate, @CreatedBy, @ModifiedBy ở bên
// các model (AbstractEntity)
// auditorAwareRef = "auditorAware" là reference tới hàm mà mình
// viết để lấy thông tin user