<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page import="java.util.logging.Logger"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<head>
</head>

<body>
	<% Logger logger=Logger.getLogger(this.getClass().getName());%>
	<%
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if (user != null) {
			logger.info("Access granted");
	%>

	<p>
		Hello, <%=user.getNickname()%> (You can <a
			href="<%=userService.createLogoutURL(request.getRequestURI())%>">sign
			out</a>.)
	</p>
	<table>
		<tr>
			<td>
				<form action="/Simulation.jsp" method="get">
					<input type="submit" value="Start Simulation" />
				</form>
			</td>
			<td>
				<form action="/Angular.jsp" method="get">
					<input type="submit" value="Retrieve Simulation" />
				</form>
			</td>
			<td>
				<form action="/Log" method="get">
					<input type="submit" value="Show Logs" />
				</form>
			</td>
		</tr>
	</table>



	<%
		} else {
			logger.info("Access Required");
	%>
	<p>
		Hello! <a
			href="<%=userService.createLoginURL(request.getRequestURI())%>">Sign
			in</a>
	</p>
	<%
		}
	%>



</body>
</html>