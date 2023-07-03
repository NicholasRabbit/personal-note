package com.atguigu.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.atguigu.bean.Teacher;

@Controller
public class TestController {

	@RequestMapping("/testListener")
	public void testListener(HttpSession session) {
		//获取spring所管理的teacher对象
		ServletContext servletContext = session.getServletContext();
		ApplicationContext ac = (ApplicationContext)servletContext.getAttribute("ac");
		Teacher teacher = ac.getBean("teacher", Teacher.class);
		System.out.println(teacher);
	}
		
}
