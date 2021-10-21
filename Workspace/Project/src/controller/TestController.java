package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.bcel.internal.generic.NEW;


class MyValue {
	  public String name = "홍길동";
	  public int age = 24;  
}

@WebServlet("/TestController")
public class TestController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public TestController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		String val = request.getParameter("value");
		if (val != null) {
			ObjectMapper mapper = new ObjectMapper();
			System.out.println(val);
			System.out.println(mapper.writeValueAsString(new MyValue()));
			response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
			response.getWriter().append(mapper.writeValueAsString(new MyValue()));
		}
		else
			System.out.println("데이터 없음");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
