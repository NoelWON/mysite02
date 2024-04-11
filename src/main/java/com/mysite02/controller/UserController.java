package com.mysite02.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mysite02.Dao.UserDao;
import com.mysite02.uservo.UserVo;

public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// post방식에서 바디에 한글이 들어가면 encoding 해야 한다.
		// encoding 하는 것
		request.setCharacterEncoding("utf-8");
		
		String action = request.getParameter("a");
		System.out.println(action);
		
		// 회원가입
		if (action.equals("joinform")) {
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/user/joinform.jsp");
			rd.forward(request, response);
		}
		// 회원가입폼에서 가입하기 요청
		else if(action.equals("join")) {
			// input의 name 속성을 파라미터로 갖는다
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			String gender = request.getParameter("gender");
			
			// 유저 값을 담는 객체를 만든다.
			UserVo vo = new UserVo();
			vo.setName(name);
			vo.setEmail(email);
			vo.setPassword(password);
			vo.setGender(gender);
			
			UserDao userDao = new UserDao();
			userDao.insert(vo);
			
			// response 비지니스 로직 후 요청에 대한 응답을 해줘야 한다.
//			request.getRequestDispatcher("/WEB-INF/views/user/joinsuccess.jsp")
//					.forward(request, response);
			// insert delate update 요청은 응답할 때 페이지가 유지 되면 안된다. 새로고침했을 때 중복으로 작동한다.
			// redirect로 url을 바꿔준다.
			response.sendRedirect("/mysite02/user?a=joinsuccess");
		}
		// 회원가입 성공시 리다이렉트 요청
		else if (action.equals("joinsuccess")) {
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/user/joinsuccess.jsp");
			rd.forward(request, response);
		}
		// 로그인 창 요청
		else if(action.equals("Loginform")) {
			request.getRequestDispatcher("/WEB-INF/views/user/loginform.jsp")
			.forward(request, response);
		}
		// 로그인 버튼 누르면 가는 것
		else if(action.equals("login")) {
			//데이터베이스에서 유저 확인 하는 것
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			
			UserVo authUser = new UserDao().findByEmailAndPassword(email, password);
			// 로그인 실패했을 경우 
			if(authUser == null) {
				// jsp에서 사용할 수 있게 key, value 를 달아준다.
				request.setAttribute("result", "fail");
				request.setAttribute("email", email);
				
				request.getRequestDispatcher("/WEB-INF/views/user/loginform.jsp")
				.forward(request, response);
				return;
			}
			// else를 쓰지 않기 위해서 return으로 끝내고 다음 코드로 진행
			// 로그인 처리
			System.out.println("로그인 처리 진행");
			HttpSession session = request.getSession(true);
			session.setAttribute("authUser", authUser);
			
			//main으로 리다이렉트
			response.sendRedirect("/mysite02/main");
			
		}
		// 로그아웃 httpsession 지우는 것
		else if(action.equals("logout")) {
			HttpSession session = request.getSession();
			
			// userVo를 날리고 싶은것
			session.removeAttribute("authUser");
			// session을 없애는 것
			session.invalidate();
			
			response.sendRedirect("/mysite02/main");
		}
		// 업데이트 과제
		else if(action.equals("updateform")) {
			// 로그인된 유저의 정보를 가져와야 한다.( no, name이 담겨 있다.)
			HttpSession session = request.getSession();
			UserVo authUser = (UserVo)session.getAttribute("authUser");
			Long no = authUser.getNo();
			
			System.out.println("test: "+authUser.getNo());
			
			UserVo userVo = new UserDao().findByNo(no);
			request.setAttribute("userVo", userVo);
			
			System.out.println("email: "+userVo.getEmail());
			System.out.println("name: "+userVo.getName());
			
			
			request.getRequestDispatcher("/WEB-INF/views/user/updateform.jsp")
			.forward(request, response);
		}
		// 업데이트 시키기
		else if(action.equals("update")) {
			//where no = 번호 지정에 쓰기 위해
			HttpSession session = request.getSession(true);
			UserVo authUser = (UserVo)session.getAttribute("authUser");
			Long no = authUser.getNo();
			
			// 이름 비밀번호 성별 name 속성 값 가져와야함
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			String gender = request.getParameter("gender");
			
			UserVo vo = new UserVo();
			vo.setNo(no);
			vo.setName(name);
			vo.setPassword(password);
			vo.setGender(gender);
				
			System.out.println(vo.getName()+" "+vo.getPassword()+" "+vo.getGender());
				
			// sql Dao 실행 
			UserDao userDao = new UserDao();
			// 조건 비밀번호 공석 일 때
			userDao.update(vo);
			
			// 리다이렉트를 해줘야함
			response.sendRedirect("/mysite02/user?a=updatesuccess");
		}
		else if(action.equals("updatesuccess")) {
			request.getRequestDispatcher("/WEB-INF/views/user/updatesuccess.jsp")
			.forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
