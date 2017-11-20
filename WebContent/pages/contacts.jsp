<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
     <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<title>Main page</title>
<link rel="stylesheet" type="text/css" href="styles/reset.css" />
<link rel="stylesheet" type="text/css" href="styles/styles.css" />


</head>
<body>

<div class="wrapper" align="center">
<%@ include file="/WEB-INF/jspf/header.jspf"%>

<div class="context">
<%@ include file="/WEB-INF/jspf/navigation.jspf"%>
<div class="teachers_list_all">
	<div class="contact_info">
		<h1 style="margin-bottom: 20px;">`EducationServise` - Компания предоставляет курсы разной тематики в Харькове</h1>
		<p>Итак, если для вас актуальны курсы нашей компании  - Наши контакты:</p>
		<div class="line"></div>
		<h1>Адрес:</h1>	г. Харьков пр. Л-Свободы, 51-Б
		<div class="line"></div>
		<h1>Телефоны</h1> +380997907915, +380992958440	
			<div class="line"></div>
			<h1>Email</h1>oleksandr.botsula@nure.ua
			</div>
</div>
		</div>




<%@ include file="/WEB-INF/jspf/footer.jspf"%>
</div>
</body>
</html>