import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.io.IOException;


public class ServletTest001 implements Servlet{

	
	/*写一个类实现Servlet接口，必须实现Servlet接口里的所有抽象方法,
	此程序编译生成的class文件必须放到WEB-INF目录下的classes文件夹里，
	同时要编写同目录下的web.xml文件，让Tomcat服务器知道如何调动程序员写的程序

	最后整个文件夹，即A002-ServletTest001目录要放到Tomcat目录webapps下，进行部署，
	注意，要编写好web.xml文件进行配置，使得Tomcat知道用户输入路径和ServletTest001.class文件的对应关系
	*/
	public void service(ServletRequest req, ServletResponse res) throws ServletException,IOException {
	   //在service(..)方法里写一段代码，可输出结果到Tomcat的控制台
	   System.out.println("First servlet code.");
	}

	public void init(ServletConfig config) throws ServletException {
	
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