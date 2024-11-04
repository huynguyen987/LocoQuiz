<%@ page import="entity.Competition" %>
<%@ page import="java.util.List" %>
<%@ page import="entity.classs" %>
<%@ page import="entity.quiz" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
  Competition competition = (Competition) request.getAttribute("competition");
  List<classs> classes = (List<classs>) request.getAttribute("classes");
  List<quiz> quizzes = (List<quiz>) request.getAttribute("quizzes");
  String pageTitle = (String) request.getAttribute("pageTitle");
  String errorMessage = (String) request.getAttribute("errorMessage");
%>
<!-- Nhiệm vụ: Tạo form tạo hoặc chỉnh sửa cuộc thi -->

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title><c:out value="${pageTitle}" /></title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/configure-comp.css">
</head>
<body>
<div class="container">
  <h2><c:out value="${pageTitle}" /></h2>

  <c:if test="${not empty errorMessage}">
    <div class="error-message">
      <c:out value="${errorMessage}" />
    </div>
  </c:if>

  <form action="${pageContext.request.contextPath}/TakeCompetitionServlet" method="post">
    <input type="hidden" name="action" value="${competition != null ? 'update' : 'create'}" />
    <c:if test="${competition != null}">
      <input type="hidden" name="id" value="${competition.id}" />
    </c:if>

    <div class="form-group">
      <label for="name">Tên Cuộc Thi:</label>
      <input type="text" id="name" name="name" value="<c:out value="${competition != null ? competition.name : ''}" />" required />
    </div>

    <div class="form-group">
      <label for="description">Mô Tả:</label>
      <textarea id="description" name="description" required><c:out value="${competition != null ? competition.description : ''}" /></textarea>
    </div>

    <div class="form-group">
      <label for="classId">Lớp Học:</label>
      <select id="classId" name="classId" required>
        <option value="">-- Chọn Lớp Học --</option>
        <c:forEach var="cls" items="${classes}">
          <option value="${cls.id}" <c:if test="${competition != null && cls.id == competition.classId}">selected</c:if>>${cls.name}</option>
        </c:forEach>
      </select>
    </div>

    <div class="form-group">
      <label for="quizId">Quiz Liên Kết:</label>
      <select id="quizId" name="quizId" required>
        <option value="">-- Chọn Quiz --</option>
        <c:forEach var="quiz" items="${quizzes}">
          <option value="${quiz.id}" <c:if test="${competition != null && quiz.id == competition.quizId}">selected</c:if>>${quiz.name}</option>
        </c:forEach>
      </select>
    </div>

    <div class="form-group">
      <label for="timeLimit">Thời Gian Làm Bài (phút):</label>
      <input type="number" id="timeLimit" name="timeLimit" min="1" value="<c:out value="${competition != null ? competition.timeLimit / 60 : ''}" />" required />
    </div>

    <div class="form-group">
      <label for="questionCount">Số Lượng Câu Hỏi:</label>
      <input type="number" id="questionCount" name="questionCount" min="1" value="<c:out value="${competition != null ? competition.questionCount : ''}" />" required />
    </div>

    <div class="form-group">
      <label for="shuffle">Xáo Trộn Câu Hỏi:</label>
      <input type="checkbox" id="shuffle" name="shuffle" <c:if test="${competition != null && competition.shuffleQuestions}">checked</c:if> />
    </div>

    <div class="form-group">
      <label for="accessStartTime">Thời Gian Bắt Đầu:</label>
      <fmt:formatDate var="formattedStartTime" value="${competition.accessStartTime}" pattern="yyyy-MM-dd'T'HH:mm" />
      <input type="datetime-local" id="accessStartTime" name="accessStartTime" value="${formattedStartTime}" required />
    </div>

    <div class="form-group">
      <label for="accessEndTime">Thời Gian Kết Thúc:</label>
      <fmt:formatDate var="formattedEndTime" value="${competition.accessEndTime}" pattern="yyyy-MM-dd'T'HH:mm" />
      <input type="datetime-local" id="accessEndTime" name="accessEndTime" value="${formattedEndTime}" required />
    </div>

    <div class="form-actions">
      <button type="submit"><c:out value="${competition != null ? 'Cập Nhật' : 'Tạo'}" /></button>
      <a href="${pageContext.request.contextPath}/TakeCompetitionServlet?action=list">Hủy</a>
    </div>
  </form>
</div>

</body>
</html>
