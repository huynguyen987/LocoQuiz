<%@ page import="dao.ClassDAO" %>
<%@ page import="entity.classs" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Danh Sách Lớp Học</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/classList.css">
</head>
<body>
<%@ include file="components/header.jsp" %>
<%@ include file="components/sidebar.jsp" %>
<%
  ClassDAO classDAO = new ClassDAO();
  Users teacher = (Users) session.getAttribute("user");
  List<classs> classes = classDAO.getClassesByTeacherId(teacher.getId());
  System.out.println("classes: " + classes.size());
  request.setAttribute("classes", classes);
%>

<main>
  <div class="class-list-container">
    <h1>Danh Sách Lớp Học</h1>

    <!-- Form Tìm Kiếm -->
    <form action="${pageContext.request.contextPath}/ClassListServlet?" method="get">
      <input type="text" name="search" placeholder="Tìm kiếm lớp học" value="${param.search}">
      <button type="submit">Tìm Kiếm</button>
    </form>

    <!-- Danh Sách Lớp Học -->
    <c:if test="${not empty classes}">
      <ul>
        <c:forEach var="classs" items="${classes}">
          <li>
            <h3>${classs.name}</h3>
            <p>${classs.description}</p>
            <p>Mã Lớp: ${classs.class_key}</p>
            <p>Ngày Tạo: <fmt:formatDate value="${classs.created_at}" pattern="yyyy-MM-dd HH:mm"/></p>
            <a href="${pageContext.request.contextPath}/ClassDetailsServlet?classsId=${classs.id}&user=${teacher}">Xem Chi Tiết</a>
          </li>
        </c:forEach>
      </ul>
    </c:if>
    <c:if test="${empty classes}">
      <p>Bạn chưa tạo lớp học nào.</p>
    </c:if>
  </div>
</main>

<%@ include file="components/footer.jsp" %>

<!-- JavaScript -->
<script src="${pageContext.request.contextPath}/js/classList.js"></script>
</body>
</html>
