<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <!-- Các thẻ head như meta, title, link CSS, v.v. -->
</head>
<body>
<header>
  <div class="container">
    <!-- Sử dụng EL để xác định trang chủ dựa trên vai trò của người dùng -->
    <c:set var="homePage" value="index.jsp" />
    <c:if test="${user.roleName == 'student'}">
      <c:set var="homePage" value="student.jsp" />
    </c:if>
    <c:if test="${user.roleName == 'teacher'}">
      <c:set var="homePage" value="teacher.jsp" />
    </c:if>
    <c:if test="${user.roleName == 'admin'}">
      <c:set var="homePage" value="admin.jsp" />
    </c:if>

    <a href="${pageContext.request.contextPath}/jsp/${homePage}" class="logo">QuizLoco</a>

    <div class="user-info">
      <c:choose>
        <c:when test="${not empty user}">
          <span>Welcome, ${user.username}!</span>
          <a href="${pageContext.request.contextPath}/LogoutServlet" class="logout-btn">Logout</a>
          <a href="${pageContext.request.contextPath}/jsp/user-profile.jsp" class="dashboard-link">Profile</a>
        </c:when>
        <c:otherwise>
          <a href="${pageContext.request.contextPath}/jsp/login.jsp" class="login-btn">Login</a>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</header>
</body>
</html>
