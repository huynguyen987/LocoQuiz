<%@ page import="entity.Competition" %>
<%@ page import="entity.quiz" %>
<%@ page import="entity.Users" %>
<!-- File: /jsp/competition-details-student.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/jsp/components/header.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
    // Lấy thông tin cuộc thi và người dùng từ request
    Competition competition = (Competition) request.getAttribute("competition");
    Users currentUser = (Users) request.getAttribute("currentUser");
    System.out.println("Current user: " + currentUser.getUsername());
    // Kiểm tra nếu competition hoặc currentUser là null
    if (competition == null || currentUser == null) {
        response.sendRedirect(request.getContextPath() + "/CompetitionController?action=list");
        return;
    }

    // Lấy thông tin lớp học và quiz liên quan
    Class classInfo = (Class) request.getAttribute("classInfo");
    quiz quizInfo = (quiz) request.getAttribute("quizInfo");
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chi Tiết Cuộc Thi - <c:out value="${competition.name}" /></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
</head>
<body>

<div class="container">
    <h2>Chi Tiết Cuộc Thi</h2>
    <table class="table">
        <tr>
            <th>Tên Cuộc Thi:</th>
            <td><c:out value="${competition.name}" /></td>
        </tr>
        <tr>
            <th>Mô Tả:</th>
            <td><c:out value="${competition.description}" /></td>
        </tr>
        <tr>
            <th>Lớp Học:</th>
            <td><c:out value="${classInfo != null ? classInfo.name : 'N/A'}" /></td>
        </tr>
        <tr>
            <th>Quiz Liên Kết:</th>
            <td><c:out value="${quizInfo != null ? quizInfo.title : 'N/A'}" /></td>
        </tr>
        <tr>
            <th>Thời Gian Làm Bài:</th>
            <td><c:out value="${competition.timeLimit / 60}" /> phút</td>
        </tr>
        <tr>
            <th>Số Lượng Câu Hỏi:</th>
            <td><c:out value="${competition.questionCount}" /></td>
        </tr>
<%--        <tr>--%>
<%--            <th>Xáo Trộn Câu Hỏi:</th>--%>
<%--            <td><c:out value="${competition.shuffleQuestions ? 'Có' : 'Không'}" /></td>--%>
<%--        </tr>--%>
        <tr>
            <th>Thời Gian Bắt Đầu:</th>
            <td><fmt:formatDate value="${competition.accessStartTime}" pattern="dd-MM-yyyy HH:mm" /></td>
        </tr>
        <tr>
            <th>Thời Gian Kết Thúc:</th>
            <td><fmt:formatDate value="${competition.accessEndTime}" pattern="dd-MM-yyyy HH:mm" /></td>
        </tr>
    </table>

    <div class="actions">
        <form action="${pageContext.request.contextPath}/jsp/competition-participation.jsp?" method="get">
            <input type="hidden" name="competitionId" value="${competition.id}" />
            <button type="submit">Tham Gia Cuộc Thi</button>
        </form>
    </div>
</div>

<%@ include file="/jsp/components/footer.jsp" %>
</body>
</html>
