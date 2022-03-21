import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;

import java.io.IOException;

public class ServletTest001 implements Servlet {

	public void init(ServletConfig config) throws ServletException {
	
	}

	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		System.out.println("First servlet code");
	}

	public ServletConfig getServletConfig(){
		return null;
	}

	public String getServletInfo(){
		return null;
	}

	public void destroy(){
		
	}
}