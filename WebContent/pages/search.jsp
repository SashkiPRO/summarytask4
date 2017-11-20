<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<title>Main page</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/reset.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/styles.css" />

</head>
<body>

	<div class="wrapper" align="center">
		<%@ include file="/WEB-INF/jspf/header.jspf"%>

		<div class="context">
			<%@ include file="/WEB-INF/jspf/navigation.jspf"%>
			<c:if test="${empty course_list}">
				<p id="course_empty">List of courses are empty!</p>
				${search }
			</c:if>
			<c:if test="${not empty course_list}">
				<div class="course_list">
<ul id="unit">

					<c:forEach items="${course_list}" var="item">

						<c:if test="${not empty user&& user.roleid==2}">
						
							<li>
								


									<h1>${item.course_name}</h1>
									<p>		<span><fmt:message key="course_name"/>: </span>${item.course_theme}</p>
										<p>	<span><fmt:message key="teacher"/>: </span>${item.teacherFirstName}
											${item.teacherLastName}</p>
									<p>		<span><fmt:message key="number_of_students"/>: </span>${item.studentCount}</p>
										<p>	<span><fmt:message key="course_start_date"/>: </span>${item.startCourse}</p>
										<p>	<span><fmt:message key="course_finish_date"/>: </span>${item.finishCourse}</p>
											<p>	<span><fmt:message key="course_duration"/>: </span>${item.duration} days</p>
										<p>	<span><fmt:message key="status_name"/>: </span>${item.state}</p>
									
										<c:if test="${item.state=='RECRUITED'}">
										<c:if test="${item.signed==true }"><input id="signed_button" type="button" disabled="disabled" value="<fmt:message key="Signed_button"/>"/></c:if>
										<c:if test="${item.signed==false }">
										<form action="controller?command=sign_course&id=${item.id}" method="post">
										<input class="manage_button" type="submit" value="Подписаться">
										</form>
										</c:if>
										
									
										
										</c:if>
									
								
							</li>
						
						</c:if>
						<c:if test="${not empty user&& user.roleid==1}">
						
							<li>
								
									
									<h1>${item.course_name}</h1>
									<p>		<span><fmt:message key="course_theme"/>: </span>${item.course_theme}</p>
										<p>	<span><fmt:message key="teacher"/>: </span>${item.teacherFirstName}
											${item.teacherLastName}</p>
									<p>		<span><fmt:message key="number_of_students"/>: </span>${item.studentCount}</p>
										<p>	<span><fmt:message key="course_start_date"/>: </span>${item.startCourse}</p>
										<p>	<span><fmt:message key="course_finish_date"/>: </span>${item.finishCourse}</p>
											<p><span><fmt:message key="course_duration"/>: </span>${item.duration} days</p>
										<p>	<span><fmt:message key="status_name"/>: </span>${item.state}</p>
										
										
									
								
							</li>
						
						</c:if>
						<c:if test="${not empty user&& user.roleid==0}">
						
							<li>
								
									
								
										<h1>${item.course_name}</h1>
									<p>		<span><fmt:message key="course_theme"/>: </span>${item.course_theme}</p>
										<p>	<span><fmt:message key="teacher"/>: </span>${item.teacherFirstName}
											${item.teacherLastName}</p>
									<p>		<span><fmt:message key="number_of_students"/>: </span>${item.studentCount}</p>
										<p>	<span><fmt:message key="course_start_date"/>: </span>${item.startCourse}</p>
										<p>	<span><fmt:message key="course_finish_date"/>: </span>${item.finishCourse}</p>
											<p><span><fmt:message key="course_duration"/>: </span>${item.duration} days</p>
										<p>	<span><fmt:message key="status_name"/>: </span>${item.state}</p>
								
							</li>
						
						</c:if>
							<c:if test="${ empty user}">
						
							<li>
								





									<h1>${item.course_name}</h1>
									<p>		<span><fmt:message key="course_theme"/>: </span>${item.course_theme}</p>
										<p>	<span><fmt:message key="teacher"/>: </span>${item.teacherFirstName}
											${item.teacherLastName}</p>
									<p>		<span><fmt:message key="number_of_students"/>: </span>${item.studentCount}</p>
										<p>	<span><fmt:message key="course_start_date"/>: </span>${item.startCourse}</p>
										<p>	<span><fmt:message key="course_finish_date"/>: </span>${item.finishCourse}</p>
											<p>	<span><fmt:message key="course_duration"/>: </span>${item.duration} days</p>
										<p>	<span><fmt:message key="status_name"/>: </span>${item.state}</p>
										<c:if test="${item.state=='RECRUITED'}">
										<p style="color: red">Please, sign to registration!</p>
										</c:if>
										
									
								
							</li>
						
						</c:if>
						
					</c:forEach>
</ul>
				</div>
			
			<div>
				
			
					<ul>

						
					</ul>
				</div>
				
				
				
				
			</c:if>

		</div>



		<%@ include file="/WEB-INF/jspf/footer.jspf"%>
	</div>
</body>
</html>