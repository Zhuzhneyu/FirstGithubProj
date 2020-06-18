package cn.kgc.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * JDBC工具类，获取数据库连接和关闭资源
 * 
 * @author Zhuzhenyu
 * 
 */



public class JDBCUtils {
	
	private static String url;
	private static String user;
	private static String password;
	private static String driver;

	/**
	 * 文件的读取，只需要读取一次就可拿到这些值，使用静态代码块
	 */
	static {
	
		try {
			// 读取资源文件，获取值
			// 1.创建properties集合
			Properties pro = new Properties();
			// 获取src路径下的文件的方式 --->ClassLoader 类加载器
			ClassLoader clo = JDBCUtils.class.getClassLoader();
			URL soc = clo.getResource("jdbc.properties");
			String path = soc.getPath();
			// 2.加载文件
			pro.load(new FileReader(path));
			// 3.获取数据，赋值
			url = pro.getProperty("url");
			user = pro.getProperty("username");
			password = pro.getProperty("password");
			driver = pro.getProperty("driver");
			// 4.加载驱动
			Class.forName(driver);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取数据库连接对象
	 * 
	 * @return Connection
	 * @throws SQLException
	 */
	public static Connection getConnection1() throws SQLException {

		return DriverManager.getConnection(url, user, password);
	}
	


	/**
	 * 使用JNDI获取数据库连接
	 * 
	 * @return
	 */
	public static Connection getConnection() {
		Context context = null;
		Connection conn = null;
		try {
			context = new InitialContext();
			DataSource ds = (DataSource) context
					.lookup("java:comp/env/jdbc/news");
			conn = ds.getConnection();
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		}

		return conn;
	}

	/**
	 * 关闭资源
	 * 
	 * @param rs
	 * @param st
	 * @param conn
	 */
	public static void close(ResultSet rs, Statement st, Connection conn) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void test (){
		System.out.print("新的改变---again");
	}

}
