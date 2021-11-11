//작성자 : 윤정도

package bean;

import java.util.Date;

public class UserBean {
	int u_id;
	int point;
	String id;
	String pw;
	String name;
	String email;
	Date registrationDate;
	boolean isAdmin;
	
	
	public int getU_id() {
		return u_id;
	}
	public void setU_id(int u_id) {
		this.u_id = u_id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public boolean isAdmin() {
		return isAdmin;
	}
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	public void printInfo() {
		System.out.println("U_id : " + u_id);
		System.out.println("Id : " + id);
		System.out.println("Pass : " + pw);
		System.out.println("Point : " + point);
		System.out.println("Admin : " + isAdmin);
	}
}
