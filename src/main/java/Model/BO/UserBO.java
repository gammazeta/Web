package Model.BO;
import java.util.ArrayList;

import Model.BEAN.User;
import Model.DAO.UserDAO;

public class UserBO {	
	public static void addUser(String idUser, String password) {
	    UserDAO.addUser(idUser,password);
	}	
	
	public static User getUser (String idUser,String password) {
		return UserDAO.getUser(idUser, password);
	}
	
	public static boolean checkIdUser(String idUser) {
		return UserDAO.checkIdUser(idUser);
	}
}
