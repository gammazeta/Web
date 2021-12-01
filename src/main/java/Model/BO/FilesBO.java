package Model.BO;
import java.util.ArrayList;

import Model.BEAN.Files;
import Model.DAO.FilesDAO;

public class FilesBO {	
	public static String addFiles(String nameFile1,String nameFile2,String idUser) {
	    return FilesDAO.addFiles(nameFile1,nameFile2,idUser);
	}
	
	public static void updateResultFiles(String idFiles,String result) {
		FilesDAO.updateResultFiles(idFiles,result);
	}
	
	public static ArrayList<Files> getListFiles(String idUser){
		return FilesDAO.getListFiles(idUser);
	}
}
