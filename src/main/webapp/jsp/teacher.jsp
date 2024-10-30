<%@ page import="entity.classs" %>
<%@ page import="java.util.List" %>
<%@ page import="entity.Competition" %>
<%@ page import="dao.ClassDAO" %>
<%@ page import="dao.CompetitionDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    ClassDAO classDAO = new ClassDAO();
    CompetitionDAO CompetitionDAO = new CompetitionDAO();
    Users teacher = (Users) session.getAttribute("user");
    List<classs> classes = classDAO.getClassesByTeacherId(teacher.getId());
    List<Competition> competitions = CompetitionDAO.getCompetitionsByTeacher(teacher.getId());
    System.out.println("classes: " + classes.size());
    System.out.println("competitions: " + competitions.size());
    request.setAttribute("classes", classes);
    request.setAttribute("competitions", competitions);
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Bảng Điều Khiển Giáo Viên - QuizLoco</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css">
</head>
<body>
<%@ include file="components/header.jsp" %>
<%@ include file="components/sidebar.jsp" %>

<main>
    <div class="dashboard-content">
        <h1>Dashboard</h1>

        <!-- Hiển thị thông điệp -->
        <c:if test="${not empty param.message}">
            <c:choose>
                <c:when test="${param.message == 'classCreated'}">
                    <div class="success-message">Đã tạo lớp học thành công.</div>
                </c:when>
                <c:when test="${param.message == 'createError'}">
                    <div class="error-message">Đã xảy ra lỗi khi tạo lớp học. Vui lòng thử lại.</div>
                </c:when>
                <c:otherwise>
                    <div class="info-message">${param.message}</div>
                </c:otherwise>
            </c:choose>
        </c:if>

        <!-- Thống kê Dashboard -->
        <div class="stats-container">
            <div class="stat-card">
                <div class="stat-icon"><i class="fas fa-chalkboard"></i></div>
                <div class="stat-info">
                    <h3>Tổng Số Lớp</h3>
                    <p>${fn:length(classes)}</p>
                </div>
                <div class="stat-actions">
                    <a href="${pageContext.request.contextPath}/ClassListServlet" class="view-button">
                        <i class="fas fa-eye"></i> Xem Chi Tiết
                    </a>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon"><i class="fas fa-trophy"></i></div>
                <div class="stat-info">
                    <h3>Tổng Số Cuộc Thi</h3>
                    <p>${fn:length(competitions)}</p>
                </div>
                <div class="stat-actions">
                    <a href="${pageContext.request.contextPath}/jsp/teacher.jsp?action=viewAllCompetitions" class="view-button">
                        <i class="fas fa-eye"></i> Xem Cuộc Thi
                    </a>
                </div>
            </div>
            <!-- Bạn có thể thêm các thẻ thống kê khác nếu cần -->
        </div>

        <!-- Các Liên Kết Hành Động -->
        <div class="action-links">
            <a href="${pageContext.request.contextPath}/TakeCompetitionServlet?action=configure" class="action-btn">
                <i class="fas fa-trophy"></i> Tạo Cuộc Thi
            </a>
            <a href="${pageContext.request.contextPath}/jsp/createClass.jsp" class="action-btn">
                <i class="fas fa-plus"></i> Tạo Lớp Mới
            </a>
        </div>

        <!-- Danh Sách Lớp Học -->
        <section class="classes-section">
            <h2>Lớp Học Của Bạn</h2>
            <c:if test="${not empty classes}">
                <div class="classes-grid">
                    <c:forEach var="classs" items="${classes}">
                        <div class="class-card">
                            <h3>${classs.name}</h3>
                            <p>${classs.description}</p>
                            <p>Mã Lớp: ${classs.class_key}</p>
                            <p>Ngày Tạo: <fmt:formatDate value="${classs.created_at}" pattern="yyyy-MM-dd HH:mm"/></p>
                            <div class="class-actions">
                                <a href="${pageContext.request.contextPath}/ClassDetailsServlet?classId=${classs.id}" class="view-button">
                                    <i class="fas fa-eye"></i> Xem Chi Tiết
                                </a>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:if>
            <c:if test="${empty classes}">
                <p>Bạn chưa tạo lớp học nào.</p>
            </c:if>
        </section>

        <!-- Danh Sách Cuộc Thi -->
        <section class="competitions-section">
            <h2>Cuộc Thi Của Bạn</h2>
            <c:if test="${not empty competitions}">
                <div class="competitions-grid">
                    <c:forEach var="competition" items="${competitions}">
                        <div class="competition-card">
                            <h3>${competition.name}</h3>
                            <p>${competition.description}</p>
                            <p>Thời Gian Giới Hạn: ${competition.timeLimit / 60} phút</p>
                            <p>Số Câu Hỏi: ${competition.questionCount}</p>
                            <p>Trộn Câu Hỏi:
                                <c:choose>
                                    <c:when test="${competition.shuffleQuestions}">Có</c:when>
                                    <c:otherwise>Không</c:otherwise>
                                </c:choose>
                            </p>
                            <p>Thời Gian Truy Cập: <fmt:formatDate value="${competition.accessStartTime}" pattern="yyyy-MM-dd HH:mm"/> đến <fmt:formatDate value="${competition.accessEndTime}" pattern="yyyy-MM-dd HH:mm"/></p>
                            <div class="competition-actions">
                                <a href="${pageContext.request.contextPath}/CompetitionDetailsServlet?competitionId=${competition.id}" class="view-button">
                                    <i class="fas fa-eye"></i> Xem Chi Tiết
                                </a>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:if>
            <c:if test="${empty competitions}">
                <p>Bạn chưa tạo cuộc thi nào.</p>
            </c:if>
        </section>

    </div>
</main>

<%@ include file="components/footer.jsp" %>

<!-- JavaScript -->
<script src="${pageContext.request.contextPath}/js/common.js"></script>
<!-- Bao gồm thêm các script JS khác nếu cần -->
</body>
</html>
