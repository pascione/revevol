<%@page import="com.revevol.simulation.Servlet.Results"%>
<%@page import="com.revevol.simulation.Pojo.Statistics"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.4.3/angular.min.js"></script>
<script type="text/javascript" src="https://www.google.com/jsapi?ext.js"></script>
<script type="text/javascript"
	src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<%
	Results g = new Results();
	Statistics sc = g.getStatistics();
	String json = g.convertToGoogleChartFormat(sc);
	String title = "Simulation time: " + sc.getDuration() + " ms";

%>
<script type='text/javascript'>
	
	var app = angular.module('myApp', []);
	app.controller('MyCtrl', function($scope) {
		$scope.name = "Name"
	});
	app.directive('chart', function() {
		return {
			restrict : 'A',
			link : function($scope, $elm, $attr) {
				// Create the data table.
				var data = new google.visualization.DataTable(<%=json%>);

				// Set chart options
				var options = {
					'title' : '<%=title%>',
					'width' : 1200,
					'height' : 900
				};

				// Instantiate and draw our chart, passing in some options.
				var chart = new google.visualization.ColumnChart($elm[0]);
				chart.draw(data, options);
			}
		}
	});

	google.setOnLoadCallback(function() {
		angular.bootstrap(document.body, [ 'myApp' ]);
	});
	google.load('visualization', '1', {
		packages : [ 'corechart' ]
	});
</script>
<body>
	<%
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if (user != null) {			
	%>
	<table>
		<tr>
			<td><div ng-controller='MyCtrl'></td>
			<td><div chart></div></td>
		</tr>
		<tr>
			<td colspan="2" style="font-size: 20px">Simulation time: <%=sc.getDuration()%></td>			
		</tr>
		<tr>
			<td colspan="2" style="font-size: 20px">Number most frequent: <%=sc.getMostOccurred() %></td>
		</tr>
		<tr>
			<td colspan="2" style="font-size: 20px">Different numbers generated: <%=sc.getDistinctNumbers() %></td>
		</tr>	
	</table>
	
				
	</div>
	<%
		} else {
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