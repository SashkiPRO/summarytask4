<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="ua.nure.botsula.st4.db.entity.CourseState"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<title>Admin page</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/reset.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/styles.css" />

</head>
<body>

	<div class="wrapper" align="center">
		<%@ include file="/WEB-INF/jspf/header.jspf"%>

		
<%@ include file="/pages/admin/content.jspf"%>


		<%@ include file="/WEB-INF/jspf/footer.jspf"%>
	</div>
</body>
</html>