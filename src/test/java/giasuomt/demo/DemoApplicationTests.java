package giasuomt.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jcraft.jsch.Session;

import giasuomt.demo.server.service.serverServiceimpl;
import giasuomt.demo.server.service.serviceService;
import giasuomt.demo.server.service.utils.ConstantCMD;

@SpringBootTest
class DemoApplicationTests {
	
	@Autowired
	private serviceService serviceService;

	@Test
	void connectServer() {
		String pw = "a779ifr3!Ab1";
		String username = "root";
		Session session = serviceService.connect(username, pw);
		System.out.println(session.isConnected());
	} 
	@Test 
	void execmd() {
		String pw = "a779ifr3!Ab1";
		String username = "root";
		String cmd = "ls";
		System.out.println(serviceService.executionCMD(cmd));
	}
	
	@Test 
	void createUser() {
		String pw = "456789";
		String username = "thuthu";
		System.out.println(serviceService.createUser(username, pw));
	}

}
