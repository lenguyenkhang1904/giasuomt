package giasuomt.demo.commondata.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;



public class AuditorAwareImpl implements AuditorAware<String>{
	@Override
	public Optional<String> getCurrentAuditor() {
		String currentUsername=SecurityContextHolder.getContext().getAuthentication().getName();
		return Optional.ofNullable(currentUsername);
	}

	/*
	 * @Override public Optional<String> getCurrentAuditor() { //getCurrentAuditor
	 * là trả về người dùng hiện tại //Để biết được là user nào hiện tại -> thì lấy
	 * ở trong Security Context ra //Bởi vì: Đã làm ở JWT: khi authenticate (xác
	 * thực) xong cho 1 Token, thì sẽ tiến hành add Authentication đó cùng với thông
	 * tin user vào Security Context SecurityContext currentSecurityContext =
	 * SecurityContextHolder.getContext(); //Security Context được quản lý bởi
	 * SecurityContextHolder //CỰC KỲ LƯU Ý: CHỖ SecurityContext phải import Spring
	 * Core, chứ ko import Springfox giống như mấy lúc làm JWT. String
	 * currentUsername = currentSecurityContext.getAuthentication().getName();
	 * return Optional.ofNullable(currentUsername); }
	 */
}
