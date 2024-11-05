<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<aside class="sidebar">
  <ul>
    <!-- Nếu người dùng là teacher -->
    <c:if test="${user.roleName == 'teacher'}">
      <li><a href="${pageContext.request.contextPath}/jsp/teacher.jsp"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
      <li><a href="${pageContext.request.contextPath}/jsp/teacher.jsp?action=createClass"><i class="fas fa-plus"></i> Create Class</a></li>
      <li><a href="${pageContext.request.contextPath}/QuizController"><i class="fas fa-plus"></i> Create Quiz</a></li>
      <li><a href="${pageContext.request.contextPath}/AllQuizzesServlet"><i class="fas fa-eye"></i> View Quiz</a></li>
      <li><a href="${pageContext.request.contextPath}/LibraryServlet"><i class="fas fa-folder"></i> Library</a></li>
    </c:if>

    <!-- Nếu người dùng là student -->
    <c:if test="${user.roleName == 'student'}">
      <li><a href="${pageContext.request.contextPath}/jsp/student.jsp"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
      <li><a href="${pageContext.request.contextPath}/jsp/student.jsp?action=Classrooms"><i class="fas fa-chalkboard"></i> Classroom</a></li>
      <li><a href="${pageContext.request.contextPath}/QuizController"><i class="fas fa-plus"></i> Create Quiz</a></li>
      <li><a href="${pageContext.request.contextPath}/jsp/flashcard.jsp"><i class="fas fa-plus"></i> Flashcard</a></li>
      <li><a href="${pageContext.request.contextPath}/LibraryServlet"><i class="fas fa-folder"></i> Library</a></li>
      <li><a href="${pageContext.request.contextPath}/LogoutServlet"><i class="fas fa-sign-out-alt"></i> Logout</a></li>
    </c:if>

    <!-- Bạn có thể thêm các điều kiện khác nếu cần -->
  </ul>
</aside>
