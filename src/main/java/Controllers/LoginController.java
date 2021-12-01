package Controllers;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import Model.BEAN.User;
import Model.BO.UserBO;

@WebServlet("/login")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public LoginController() {
		super();

	}

	/*
	 *Kiểm tra user đã đăng nhập chưa 
	 *Nếu user đã đăng nhập thì dẫn đến home
	 *Nếu chưa đăng nhập thì cho login 
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//Kiểm tra session 
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/WEB-INF/Views/login.jsp");
			rd.forward(request, response);
		} else {
			response.sendRedirect("home");
		}
	}

	/*
	 *Kiểm tra người dùng nhập đúng id và mật khẩu không 
	 *Nếu đúng thì cho đăng nhập 
	 *Nếu không đúng thì cho login  
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String idUser = request.getParameter("idUser");
			String password = request.getParameter("password");
			
			//Kiểm tra id và password từ database 
			User user = UserBO.getUser(idUser, password);
			if (user != null) {
				request.getSession().setAttribute("user", user);
				response.sendRedirect("home");
			} else {
				//Thông báo đăng nhập không thành công 
				request.setAttribute("message", "Name or password not true");

				RequestDispatcher rd = getServletContext().getRequestDispatcher("/WEB-INF/Views/login.jsp");
				rd.forward(request, response);
			}
		} catch (Exception e) {
			System.out.println(e);
		}

	}

}
