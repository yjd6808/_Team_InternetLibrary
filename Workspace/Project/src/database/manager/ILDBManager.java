//작성자 : 윤정도

package database.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ILDBManager {
	protected void CloseStatement(Statement statement) {
		try {
			if (statement != null)
				statement.close();
		} catch(Exception e) {e.printStackTrace();}
	}
	
	protected void CloseConnection(Connection connection) {
		try {
			if (connection != null)
				connection.close();
		} catch(Exception e) {e.printStackTrace();}
	}
	
	protected void CloseResultSet(ResultSet resultSet) {
		try {
			if (resultSet != null)
				resultSet.close();
		} catch(Exception e) {e.printStackTrace();}
	}
	
	protected void RollbackConnection(Connection connection) {
		try {
			if (connection != null)
				connection.rollback();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
