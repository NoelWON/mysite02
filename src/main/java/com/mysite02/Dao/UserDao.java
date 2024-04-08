package com.mysite02.Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.mysite02.uservo.UserVo;

public class UserDao {

	public Boolean insert(UserVo vo) {
		Boolean result = false;
		// JDBC 코드가 들어가는 위치
		// sql injection 방지를 해야 한다. 따라서 값을 바인딩 해야 한다.
		// String sql = "insert into user values(null,'원찬희','won@gmail.com',password('1234'),'male');";
		
		
		try {
			// 0. JDBC 클래스 로딩
			Class.forName("org.mariadb.jdbc.Driver");
			// 1. 워크벤치와 연결을 해야한다. (서버(IP+port+스키마이름), 계정 , 비밀번호 )
			// connection interface
			String url = "jdbc:mariadb://192.168.100.8:3307/webdb?chatset=utf8";
			Connection conn = DriverManager.getConnection(url,"webdb","webdb");
			System.out.println("connection success!");
			// 2. sql 작성 & 실행 
			// preparedStatement interface 쿼리를 준비하고 바인딩 후 실행
			// 쿼리 준비하는 단계
			String sql = "insert into user values(null,?,?,password(?),?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			// ? 에 들어갈 값(파라미터) 바인딩 작업
			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getEmail());
			pstmt.setString(3, vo.getPassword());
			pstmt.setString(4, vo.getGender());
			// 바인딩 후 sql 실행 실행 결과 몇개가 성공했는지 나오게 된다. 
			int count = pstmt.executeUpdate();
			// 실행
			result = count == 1;
			 
			// 3. sql 문에서 select일경우			
			// ResultSet interface (select) pstmt.excuteQuery()를 쓴다.

		} catch (ClassNotFoundException e) {
			System.out.println("shit Driver loading fail:   " + e);
		} catch (SQLException e) {
			System.out.println("Error:   " + e);
		}
		
		return result;
	}
}
