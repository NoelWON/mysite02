package com.mysite02.Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysite02.uservo.UserVo;

public class UserDao {

	public Boolean insert(UserVo vo) {
		Boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		// JDBC 코드가 들어가는 위치
		// sql injection 방지를 해야 한다. 따라서 값을 바인딩 해야 한다.
		// String sql = "insert into user values(null,'원찬희','won@gmail.com',password('1234'),'male');";
		try {
			// 0. JDBC 클래스 로딩
			Class.forName("org.mariadb.jdbc.Driver");
			// 1. 워크벤치와 연결을 해야한다. (서버(IP+port+스키마이름), 계정 , 비밀번호 )
			// connection interface
			String url = "jdbc:mariadb://192.168.100.8:3307/webdb?chatset=utf8";
			conn = DriverManager.getConnection(url,"webdb","webdb");
			System.out.println("connection success!");
			// 2. sql 작성 & 실행 
			// preparedStatement interface 쿼리를 준비하고 바인딩 후 실행
			// 쿼리 준비하는 단계
			String sql = "insert into user values(null,?,?,password(?),?)";
			pstmt = conn.prepareStatement(sql);
			// ? 에 들어갈 값(파라미터) 바인딩 작업
			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getEmail());
			pstmt.setString(3, vo.getPassword());
			pstmt.setString(4, vo.getGender());
			// 바인딩 후 sql 실행 실행 결과 몇개가 성공했는지 나오게 된다. 
			int count = pstmt.executeUpdate();
			// 실행
			result = count == 1;

		} catch (ClassNotFoundException e) {
			System.out.println("shit Driver loading fail:   " + e);
		} catch (SQLException e) {
			System.out.println("Error:   " + e);
		}
		// 연결을 닫아준다.
		finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					conn.close();
				}	
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	
	public UserVo findByEmailAndPassword(String email, String password) {
		UserVo result = null;
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			String url = "jdbc:mariadb://192.168.100.8:3307/webdb?chatset=utf8";
			conn = DriverManager.getConnection(url,"webdb","webdb");
			System.out.println("connection success!");
			
			
			String sql = "select no, name from user where email =? and password = password(?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, email);
			pstmt.setString(2, password);
			// sql 문에서 select일경우			
			// ResultSet interface (select) pstmt.excuteQuery()를 쓴다.
			rs = pstmt.executeQuery();
			
			// 결과 처리 (쿼리문에서 여러 개 일 때)   한개만 나오니까 if 여러개일때 while 
			if(rs.next()) {
				Long no = rs.getLong(1);
				String name = rs.getString(2);
				
				result = new UserVo();
				result.setNo(no);
				result.setName(name);
			}
		
		} catch (ClassNotFoundException e) {
			System.out.println("shit Driver loading fail:   " + e);
		} catch (SQLException e) {
			System.out.println("Error:   " + e);
		}finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					conn.close();
				}	
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		return result;
	}

}
