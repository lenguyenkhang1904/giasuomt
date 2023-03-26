package giasuomt.demo.server.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.stereotype.Service;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import giasuomt.demo.server.service.utils.ConstantCMD;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class serverServiceimpl implements serviceService {

	@Override
	public Session connect(String username, String password) {
		   Session session = null;
		try {
			String host = ConstantCMD.HOST;
			   Properties config = new Properties(); 
				config.put("StrictHostKeyChecking", "no");
				JSch jsch = new JSch(); 
				session = jsch.getSession(username, host, 22);
				session.setPassword(password);
				session.setConfig(config);
				session.connect();
				return session;
		} catch (JSchException e) {
			e.printStackTrace();
		} 
	    	
		return null;
	}

	@Override
	public String executionCMD(String cmd) {
		String response = "";
		Session sessionConnected = connect(ConstantCMD.USERNAME,ConstantCMD.PASSWORD);
		if(sessionConnected.isConnected()) {
			try {
			 // Create channel to exec cmd
			 ChannelExec channelExec = (ChannelExec) sessionConnected.openChannel("exec");
			 //set cmd
			 channelExec.setCommand(cmd);
			 InputStream in = channelExec.getInputStream(); 
			 channelExec.connect(5000);
			// read the result from remote server
	            byte[] tmp = new byte[1024];
	            while (true) {
	                while (in.available() > 0) {
	                    int i = in.read(tmp, 0, 1024);
	                    if (i < 0) break;
	                    response += new String(tmp, 0, i);
	                    log.info("response: "+ new String(tmp, 0, i));
	                }
	                if (channelExec.isClosed()) {
	                    if (in.available() > 0) continue;
	                    log.info("response: "+ channelExec.getExitStatus());
	                    break;
	                }
	                try {
	                    Thread.sleep(1000);
	                } catch (Exception ee) {
	                }
	            }
	            
	            return response;
			
			} catch (IOException | JSchException e) {
				e.printStackTrace();
			}

		}
		return null;
	}

	@Override
	public String createUser(String username, String password) {
		
		String cmd = ConstantCMD.ADD_USER + " " + username + ConstantCMD.LINK + " " + 
				ConstantCMD.ECHO + " " + password + " | " + ConstantCMD.PASSWORD_CMD + " " + username +" --stdin";
		String response = executionCMD(cmd);
		return response;
	}
	
}
