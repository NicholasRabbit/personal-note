package com.atguigu.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestInterceptorController {

	
	/**
	 * 当有多个拦截器时，
	 * preHandle：按照拦截器数组的正向顺序执行
	 * postHandle：按照拦截器数组的反向顺序执行
	 * afterCompletion：按照拦截器数组的反向顺序执行
	 * 
	 * 当多个拦截器的preHandle有不同的值时
	 * 第一个返回false，第二个返回false：只有第一个preHandle会执行
	 * 第一个返回true，第二个返回false：两个（全部）拦截器的preHandle都会执行
	 * 但是（全部）postHandle都不会执行，而afterCompletion只有第一个（返回false的拦截器之前的所有afterCompletion）会执行
	 * 第一个返回false，第二个返回true:只有第一个的preHandle会执行
	 */
	@RequestMapping("/testInterceptor")
	public String testInterceptor() {
		return "success";
	}
	
}
