package Controllers;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import Model.BEAN.User;
import Model.BEAN.Files;
import Model.BO.FilesBO;

@WebServlet("/home")
@MultipartConfig
public class HomeController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public HomeController() {
		super();
	}

	/*
	 * Khi người dùng vào Home thì đầu tiên sẽ check xem user đã đăng nhập chưa 
	 * Nếu user chưa đăng nhập thì chuyển hướng đến login 
	 * Nếu đã đăng nhập thì load dữ liệu lên cho user và cho vào home 
	 * Dữ liệu load lên trên home sẽ là 
	 * các file code mà user đã gửi đến server để kiểm tra giống nhau 
	 * và kết quả so sánh mà server đã xử lý 
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			//Kiểm tra user đã đăng nhập chưa bằng session 
			User user = (User) request.getSession().getAttribute("user");
			if (user != null) {
				/*
				 * Lấy dữ liệu lên dể hiển thị trên home cho user 
				 */
				ArrayList<Files> listFiles = FilesBO.getListFiles(user.getIdUser());
				request.setAttribute("listFiles", listFiles);

				RequestDispatcher rd = getServletContext().getRequestDispatcher("/WEB-INF/Views/home.jsp");
				rd.forward(request, response);
			} else {
				//Người dùng chưa đăng nhập thì chuyển hướng đến login 
				response.sendRedirect("login");
			}
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	/*
	 * Đây là hàm kiểm tra xem 2 file code giống nhau bao nhiêu phần trăm 
	 * Dữ liệu đầu vào sẽ là 2 file code mà user upload 
	 * Việc xử lý sẽ được đẩy ra riêng 1 thread độc lập chạy xong xong với chương trình 
	 * Khi thread xử lý đã kiểm tra xong 2 file giống nhau bao nhiêu phần trăm thì đẩy vào database 
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			//Lấy dữ liệu các file mà người dùng upload 
			List<Part> fileParts = request.getParts().stream()
					.filter(part -> "files".equals(part.getName()) && part.getSize() > 0).collect(Collectors.toList());
			
			/*
			 * Ta chỉ lấy tên file để đưa vào database 
			 * Sau khi hàm xử lý so sánh file xong thì sẽ đẩy tiếp kết quả so sánh vào database 
			 * Server không lưu lại nội dung của file  
			 */
			String nameFile1 = "", nameFile2 = "";
			int dem = 0;
			for (Part filePart : fileParts) {
				String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
				System.out.println(fileName);
				if (dem == 0) {
					nameFile1 = fileName;
				} else {
					nameFile2 = fileName;
				}
				dem++;
			}
			
			//Lấy id của người dùng hiện tại từ session 
			User user = (User) request.getSession().getAttribute("user");
			String idUser = user.getIdUser();

			//Đẩy dữ liệu tên file user đã upload vào database 
			String idFiles = FilesBO.addFiles(nameFile1, nameFile2, idUser);

			//Đẩy việc xử lý kết quả cho 1 thread mới chạy song song với chương trình 
			new HandleFiles(idFiles, fileParts).start();

			/*
			 * Chuyển user đến method get của home
			 * để user biết là file đã được gửi thành công và chờ kết quả 
			 */
			response.sendRedirect("home");
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}

/*
 * Thread xử lý kiểm tra mức độ giống nhau của file code
 * Khi xử lý xong thì kết quả sẽ được đẩy vào database 
 * Do đề không yêu cầu nên không dùng socket để thông báo realtime việc xử lý xong tới user 
 */
class HandleFiles extends Thread {
	String idFiles;
	List<Part> fileParts;
	
	//lấy dữ liệu file mà user đã upload 
	public HandleFiles(String idFiles, List<Part> fileParts) {
		this.idFiles = idFiles;
		this.fileParts = fileParts;
	}

	public void run() {
		try {
			/*
			 * Lấy ra nội dung file user đã upload đưa vào String 
			 */
			String fileContent1 = "";
			String fileContent2 = "";
			int dem = 0;
			for (Part filePart : fileParts) {
				InputStream fileContent = filePart.getInputStream();
				String stringContent = new String(fileContent.readAllBytes(), StandardCharsets.UTF_8);

				if (dem == 0) {
					fileContent1 = stringContent;
				} else {
					fileContent2 = stringContent;
				}
				dem++;
			}

			/*
			 * Chuyển từ String sang mãng các String 
			 * Mỗi phần tử của mãng sẽ là 1 dòng trong file 
			 */
			ArrayList<String> content1 = new ArrayList<>(Arrays.asList(fileContent1.split("[\\r\\n]+")));
			ArrayList<String> content2 = new ArrayList<>(Arrays.asList(fileContent2.split("[\\r\\n]+")));

			/*
			 * Đưa vào hàm kiểm tra giống nhau để xử lý 
			 * Lựa ra file có kích thước nhỏ hơn là file1 
			 */
			if (content1.size() < content2.size()) {
				//Hàm kiểm tra file giống nhau 
				checkSame(content1, content2);
			} else {
				//Hàm kiểm tra file giống nhau 
				checkSame(content2, content1);
			}

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/*
	 * Thuật toán kiểm tra giống nhau của 2 file như sau 
	 * Kiểm tra xem trong 2 file có bao nhiêu dòng giống nhau 
	 * Xuất kết quả phần trăm mức độ giống nhau ra 
	 */
	void checkSame(ArrayList<String> content1, ArrayList<String> content2) {
		int dem = 0;
		//Kiểm tra từng dòng của file1 với từng dòng của file2 
		for (int i = 0; i < content1.size(); i++) {
			for (int j = 0; j < content2.size(); j++) {
				if (content1.get(i).equals(content2.get(j))) {
					//Nếu dúng nhau thì dem++ và remove dòng đó đi để không check nửa 
					dem++;
					content2.remove(j);
				}
			}
		}
		//Tính phần trăm và làm tròn kết quả 
		BigDecimal percentCopy = new BigDecimal(dem * 100 / content1.size()).setScale(2, RoundingMode.HALF_UP);
		
		//Lưu dữ liệu vào database 
		FilesBO.updateResultFiles(idFiles, percentCopy.toString() + "%");

	}
}
