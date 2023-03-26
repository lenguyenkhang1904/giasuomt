package giasuomt.demo.server.service;

import com.jcraft.jsch.Session;

public interface serviceService {
	Session connect(String username,String password);
	String executionCMD(String cmd);
	String createUser(String username,String password);
}
