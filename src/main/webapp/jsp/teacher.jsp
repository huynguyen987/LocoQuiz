<%@ page import="entity.classs" %>
<%@ page import="java.util.List" %>
<%@ page import="entity.Competition" %>
<%@ page import="dao.ClassDAO" %>
<%@ page import="dao.CompetitionDAO" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="entity.Users" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    ClassDAO classDAO = new ClassDAO();
    CompetitionDAO competitionDAO = new CompetitionDAO();
    if (session.getAttribute("user") == null) {
        response.sendRedirect(request.getContextPath() + "/jsp/loginServlet");
        return;
    }
    Users teacher = (Users) session.getAttribute("user");
    if (teacher == null) {
        response.sendRedirect(request.getContextPath() + "/jsp/loginServlet");
        return;
    }
    // Lấy tham số tìm kiếm từ request
    String classSearch = request.getParameter("classSearch");
    String competitionSearch = request.getParameter("competitionSearch");

    List<classs> classes;
    List<Competition> competitions;

    if (classSearch != null && !classSearch.trim().isEmpty()) {
        classes = classDAO.searchClassesByTeacherId2(classSearch.trim(), teacher.getId());
    } else {
        classes = classDAO.getClassesByTeacherId(teacher.getId());
    }

    if (competitionSearch != null && !competitionSearch.trim().isEmpty()) {
        competitions = competitionDAO.searchCompetitionsByTeacher(competitionSearch.trim(), teacher.getId());
    } else {
        competitions = competitionDAO.getCompetitionsByTeacher(teacher.getId());
    }

    request.setAttribute("classes", classes);
    request.setAttribute("competitions", competitions);
    request.setAttribute("classSearch", classSearch);
    request.setAttribute("competitionSearch", competitionSearch);
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
            </div>

            <div class="stat-card">
                <div class="stat-icon"><i class="fas fa-trophy"></i></div>
                <div class="stat-info">
                    <h3>Tổng Số Cuộc Thi</h3>
                    <p>${fn:length(competitions)}</p>
                </div>
                <div class="stat-actions">
                    <a href="${pageContext.request.contextPath}/CompetitionController?action=list" class="view-button">
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

            <!-- Thanh tìm kiếm lớp học -->
            <form method="get" action="">
                <input type="text" name="classSearch" placeholder="Tìm kiếm lớp học" value="${fn:escapeXml(classSearch)}"/>
                <button type="submit">Tìm kiếm</button>
            </form>

            <c:if test="${not empty classes}">
                <div class="classes-grid">
                    <c:forEach var="classs" items="${classes}">
                        <div class="class-card">
                            <h3>${classs.name}</h3>
                            <p>${classs.description}</p>
                            <p>Mã Lớp: ${classs.class_key}</p>
                            <p>Ngày Tạo: <fmt:formatDate value="${classs.created_at}" pattern="yyyy-MM-dd HH:mm"/></p>
                            <div class="class-actions">
                                <a href="${pageContext.request.contextPath}/ClassDetailsServlet?classsId=${classs.id}&user=${teacher}" class="view-button">
                                    <i class="fas fa-eye"></i> Xem Chi Tiết
                                </a>
                                <form action="${pageContext.request.contextPath}/DeleteClassServlet" method="post" class="delete-form">
                                    <input type="hidden" name="classId" value="${classs.id}">
                                    <button type="submit" class="button delete-button" aria-label="Xóa lớp">
                                        <i class="fas fa-trash-alt"></i>
                                    </button>
                                </form>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:if>
            <c:if test="${empty classes}">
                <p>Không tìm thấy lớp học nào.</p>
            </c:if>
        </section>

        <!-- Danh Sách Cuộc Thi -->
        <section class="competitions-section">
            <h2>Cuộc Thi Của Bạn</h2>

            <!-- Thanh tìm kiếm cuộc thi -->
            <form method="get" action="">
                <input type="text" name="competitionSearch" placeholder="Tìm kiếm cuộc thi" value="${fn:escapeXml(competitionSearch)}"/>
                <button type="submit">Tìm kiếm</button>
            </form>

            <c:if test="${not empty competitions}">
                <div class="competitions-grid">
                    <c:forEach var="competition" items="${competitions}">
                        <div class="competition-card">
                            <h3>${competition.name}</h3>
                            <p>${competition.description}</p>
                            <p><strong>Thời Gian Giới Hạn:</strong> ${competition.timeLimit / 60} phút</p>
                            <p><strong>Số Câu Hỏi:</strong> ${competition.questionCount}</p>
                            <p><strong>Trộn Câu Hỏi:</strong>
                                <c:choose>
                                    <c:when test="${competition.shuffleQuestions}">Có</c:when>
                                    <c:otherwise>Không</c:otherwise>
                                </c:choose>
                            </p>
                            <p><strong>Thời Gian Truy Cập:</strong> <fmt:formatDate value="${competition.accessStartTime}" pattern="yyyy-MM-dd HH:mm"/> đến <fmt:formatDate value="${competition.accessEndTime}" pattern="yyyy-MM-dd HH:mm"/></p>
                            <div class="competition-actions">
                                <a href="${pageContext.request.contextPath}/CompetitionDetailsServlet?competitionId=${competition.id}" class="button view-button">
                                    <i class="fas fa-eye"></i> Xem Chi Tiết
                                </a>
                                <form action="${pageContext.request.contextPath}/DeleteCompetitionServlet" method="post" class="delete-form">
                                    <input type="hidden" name="competitionId" value="${competition.id}">
                                    <button type="submit" class="button delete-button" aria-label="Xóa Cuộc Thi">
                                        <i class="fas fa-trash-alt"></i>
                                    </button>
                                </form>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:if>
            <c:if test="${empty competitions}">
                <p>Không tìm thấy cuộc thi nào.</p>
            </c:if>
        </section>

    </div>
    <!-- Style cho nút xóa -->
    <style>
        .button.delete-button, .button.delete-button:hover {
            background-color: #ff0000;
            color: #fff;
        }
    </style>
</main>

<%@ include file="components/footer.jsp" %>

<!-- JavaScript -->
<script src="${pageContext.request.contextPath}/js/common.js"></script>
<!-- Bao gồm thêm các script JS khác nếu cần -->
</body>
</html>
