package com.yongheng.wenfou.controller;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

	//Spring会将标有@ExceptionHandler注解的方法织入到RequestMapping方法中
	@ExceptionHandler({ Exception.class })
	public ModelAndView handlerException(Exception ex) {
		Logger logger = Logger.getLogger(getClass());
		logger.error(ex.getMessage());
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("error");
		modelAndView.addObject("error", ex.getMessage());
		return modelAndView;
	}

}
