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

@WebServlet("/register")
public class RegisterController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public RegisterController() {
		super();

	}

	//Phương thức get của register sẽ chuyển đến cho login xử lý 
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect("login");
	}

	/*
	 *Kiểm tra id người dùng đã được dùng chưa
	 *Nếu id chưa được dùng thì đăng ký người dùng thành công và cho vào home
	 *Nếu id đã dùng thì đăng ký không thành công
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String idUser = request.getParameter("idUser");
			String password = request.getParameter("password");
			
			//kiểm tra id mới này đã có trong database chưa 
			boolean checkId = UserBO.checkIdUser(idUser);
			if (checkId) {
				//Nếu id đã có thì không cho đăng ký , chuyển đến lại login 
				request.setAttribute("message", "Id user is used , try different name");
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/WEB-INF/Views/login.jsp");
				rd.forward(request, response);
			} else {
				//Nếu id chưa được dùng thì đăng ký user thành công , cho vào home 
				UserBO.addUser(idUser, password);
				User user = UserBO.getUser(idUser, password);
				request.getSession().setAttribute("user", user);
				response.sendRedirect("home");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
