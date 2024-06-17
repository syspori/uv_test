<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<table border="1">
		<c:forEach var="vo" items="${list }">
			<tr>
				<td>${vo.idx }</td>
				<td>${vo.time }</td>
				<td>${vo.direct_radiation }</td>
			</tr>
		</c:forEach>
	</table>
	
	${no_date }
	
	<a href="#" onclick="location.href='uv_change.do'">uv api</a>
</body>
</html>