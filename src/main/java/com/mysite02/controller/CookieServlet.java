package com.mysite02.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int visitCount = 0;
				
		// 쿠키 읽기 요청이 들어오면 request 에 다 담아준다.
		Cookie[] cookies = request.getCookies();
		if(cookies != null && cookies.length >0) {
			for(Cookie cookie : cookies) {
				if("visit-count".equals(cookie.getName())) {
					// 쿠키의 값을 가져온다.
					visitCount = Integer.parseInt(cookie.getValue());
				}
			}
		}
		
		visitCount ++;
		
		// 쿠키 굽기 이름과 값
		Cookie cookie = new Cookie("visit-count",String.valueOf(visitCount));
		cookie.setPath("/mysite02"); // path 지정
		cookie.setMaxAge(24*60*60);  // 하루동안 expiretime
		
		response.addCookie(cookie);
		
		// 쿠키를 화면에 출력
		response.setContentType("text/html; charset=utf-8");
		response.getWriter().println("<h1>방문횟수"+ visitCount + "</h1>");
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
