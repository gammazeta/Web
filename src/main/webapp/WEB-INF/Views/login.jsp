<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="US-ASCII">
<title>Login</title>
<style>
<%@include file="/WEB-INF/Css/style.css"%>
</style>
</head>
<body>
	<div class="card">
		<h2>Login</h2>
		<form name="form" method="POST">
			<b class="label">Id User</b> 
			<input type="text" name="idUser" id="idUser"> 
			<br> <br> 
			<b class="label">Pass</b> 
			<input type="password" id="password" name="password">
		</form>
	</div>
	
	<br> <br>
	<button onclick="login()">Login</button>
	<button onclick="register()">Register</button>
	
	<%
	String message =  (String)request.getAttribute("message");
	%>
	<% if(message != null) {%>
		<h3 class="message"> 
			<%=message%>
		</h3>
	<%} %>
	
	
	
	<script>
		function login() {
			if (check()) {
				document.form.action = "login"
				document.form.submit();
			}
		}

		function register() {
			if(check()){
				document.form.action = "register"
				document.form.submit();
			}
		}

		function check() {
			let idUser = document.getElementById("idUser").value;
			let password = document.getElementById("password").value;
			
			if (idUser == "") {
				alert("Id user must not be null !");
				return false;
			}
			if (password == "") {
				alert("Password must not be null !");
				return false;
			}
			return true;
		}
	</script>
</body>
</html>