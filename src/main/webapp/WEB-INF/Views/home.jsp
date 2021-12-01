<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<%@ page import="Model.BO.*"%>
<%@ page import="Model.BEAN.*"%>
<%@page import="java.util.ArrayList"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="US-ASCII">
<title>Home Page</title>
<style><%@include file="/WEB-INF/Css/style.css"%></style>

</head>
<body>
	<%
	User user = (User) request.getSession().getAttribute("user");
	ArrayList<Files> listFiles = (ArrayList<Files>) request.getAttribute("listFiles");
	%>

	<h3>
		Hello
		<%=user.getIdUser()%>
		| <a href="logout">Logout</a>
	</h3>
	<h1>Detect Copy Code</h1>

	<form name="form" action="home" method="post" enctype="multipart/form-data">
		<b>File 1 : </b><input type="file" name="files" id="file1"/> 
		<b>File 2 : </b><input type="file" name="files" id="file2"/>
		<button onclick="checkCopy()" type="button">Check Copy</button>
	</form>
	
	
	<br><br>
	<hr />

	<table>
		<tr>
			<th>File 1</th>
			<th>File 2</th>
			<th>Percent copy</th>
		</tr>
		<%
		for (int i = 0; i < listFiles.size(); i++) {
		%>
		<tr>
			<td><%=listFiles.get(i).getNameFile1()%></td>
			<td><%=listFiles.get(i).getNameFile2()%></td>
			<td><%=listFiles.get(i).getResult()%></td>
		</tr>
		<%
		}
		%>
	</table>
	<br>
	<button onclick="refreshResult()">refresh result</button>

	<script>
		function checkCopy(){
			let file1 = document.getElementById("file1");
			let file2 = document.getElementById("file2");
			console.log(file1.files.length);
			if(file1.files.length == 0){
				alert("chose file 1");
				return;
			}
			if(file2.files.length == 0){
				alert("chose file 2");
				return;
			}
			document.form.submit();
		}
		
		function refreshResult(){
			location.reload();
		}
	</script>

</body>
</html>