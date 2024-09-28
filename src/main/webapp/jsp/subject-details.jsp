<%--
  Created by IntelliJ IDEA.
  User: hient
  Date: 9/26/2024
  Time: 1:27 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
  <title>${subject.title} - QuizLoco</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
<%-- Include header --%>
<header>
  <div class="container">
    <h1><a href="${pageContext.request.contextPath}/index.jsp" class="logo">QuizLoco</a></h1>
    <nav>
      <ul>
        <li><a href="${pageContext.request.contextPath}/index.jsp">Home</a></li>
        <li><a href="${pageContext.request.contextPath}/index.jsp#about">About</a></li>
        <li><a href="${pageContext.request.contextPath}/index.jsp#services">Services</a></li>
        <li><a href="${pageContext.request.contextPath}/index.jsp#contact">Contact</a></li>
      </ul>
    </nav>

    <!-- Display user details based on session -->
    <div class="auth-links">
      <c:choose>
        <c:when test="${not empty sessionScope.username}">
          <div class="user-info">
            <p>Hello, ${sessionScope.username}!</p>
            <a href="${pageContext.request.contextPath}/LogoutServlet" class="btn-logout">Logout</a>
          </div>
        </c:when>
        <c:otherwise>
          <a href="${pageContext.request.contextPath}/jsp/login.jsp" class="btn-login">Login</a>
        </c:otherwise>
      </c:choose>
    </div>

    <div class="mobile-menu-toggle">
      <span></span>
      <span></span>
      <span></span>
    </div>
  </div>
</header>

<main>
  <div class="container">
    <h2>${subject.title}</h2>
    <img src="${pageContext.request.contextPath}/ImageServlet?subjectId=${subject.id}" alt="${subject.title}" class="subject-thumbnail-large" />    <p>${subject.description}</p>
    <p><strong>Category:</strong> ${subject.category}</p>
    <p><strong>Status:</strong> ${subject.status}</p>

    <h3>Lessons</h3>
    <ul class="lesson-list">
      <c:forEach var="lesson" items="${subject.lessons}">
        <li>
          <h4>${lesson.title}</h4>
          <p>${lesson.content}</p>
          <!-- Bạn có thể thêm các liên kết hoặc chức năng khác liên quan đến bài học tại đây -->
        </li>
      </c:forEach>
    </ul>
  </div>
</main>

<%-- Include footer --%>
<footer id="contact">
  <div class="container">
    <p>&copy; 2024 QuizLoco. All rights reserved.</p>
  </div>
</footer>

<script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>
