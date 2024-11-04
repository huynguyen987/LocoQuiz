<%@ page import="dao.ClassDAO" %>
<%@ page import="dao.QuizDAO" %>
<%@ page import="entity.classs" %>
<%@ page import="dao.CompetitionDAO" %>
<%@ page import="entity.Competition" %>
<%@ page import="entity.quiz" %>
<%@ page import="entity.Users" %>
<!-- File: /jsp/competition-list.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/jsp/components/header.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    Users currentUser = (Users) session.getAttribute("user");
    System.out.println("currentUser: " + currentUser.getUsername());
    if (currentUser == null) {
        response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Danh Sách Cuộc Thi</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
</head>
<body>

<main class="container my-5">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>Danh Sách Cuộc Thi</h2>
        <c:if test="${currentUser.hasRole('teacher') || currentUser.hasRole('admin')}">
            <a href="${pageContext.request.contextPath}/CompetitionController?action=create" class="btn btn-primary">
                <i class="fas fa-plus"></i> Tạo Mới
            </a>
        </c:if>
    </div>

    <div class="table-responsive">
        <table class="table table-striped table-hover align-middle">
            <thead class="table-dark">
            <tr>
                <th>ID</th>
                <th>Tên Cuộc Thi</th>
                <th>Lớp Học</th>
                <th>Quiz Liên Kết</th>
                <th>Thời Gian Làm Bài</th>
                <th>Số Lượng Câu Hỏi</th>
                <th>Thao Tác</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="competition" items="${competitions}">
                <% CompetitionDAO comDao = new CompetitionDAO();
                    request.setAttribute("classInfo", comDao);
                %>
                <c:set var="classInfo" value="${comDao.getClassInfo(competition.id)}" />
                <c:set var="quizInfo" value="${comDao.getQuizInfo(competition.id)}" />
                    <td>${competition.id}</td>
                    <td>${competition.name}</td>
                    <td>${competition.classId}</td>
                    <td>${competition.quizId}</td>
                    <td>${competition.timeLimit / 60} phút</td>
                    <td>${competition.questionCount}</td>
                    <td>
                        <a href="${pageContext.request.contextPath}/CompetitionController?action=view&id=${competition.id}" class="btn btn-info btn-sm me-1" title="Chi Tiết">
                            <i class="fas fa-eye"></i>
                        </a>
                        <c:if test="${currentUser.hasRole('teacher') || currentUser.hasRole('admin')}">
                            <form action="${pageContext.request.contextPath}/CompetitionController" method="post" class="d-inline" onsubmit="return confirm('Bạn có chắc chắn muốn xóa cuộc thi này không?');">
                                <input type="hidden" name="action" value="delete" />
                                <input type="hidden" name="id" value="${competition.id}" />
                                <button type="submit" class="btn btn-danger btn-sm" title="Xóa">
                                    <i class="fas fa-trash-alt"></i>
                                </button>
                            </form>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty competitions}">
                <tr>
                    <td colspan="7" class="text-center">Không có cuộc thi nào.</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
    <!-- Confirmation Modal -->
    <div class="modal fade" id="confirmDeleteModal" tabindex="-1" aria-labelledby="confirmDeleteLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header bg-danger text-white">
                    <h5 class="modal-title" id="confirmDeleteLabel">Xác Nhận Xóa</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    Bạn có chắc chắn muốn xóa cuộc thi này không?
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="button" class="btn btn-danger" id="confirmDeleteBtn">Xóa</button>
                </div>
            </div>
        </div>
    </div>
    <!-- Trong bảng danh sách cuộc thi -->
    <form action="${pageContext.request.contextPath}/CompetitionController" method="post" class="d-inline delete-form">
        <input type="hidden" name="action" value="delete" />
        <input type="hidden" name="id" value="${competition.id}" />
        <button type="button" class="btn btn-danger btn-sm" data-bs-toggle="modal" data-bs-target="#confirmDeleteModal" data-id="${competition.id}" title="Xóa">
            <i class="fas fa-trash-alt"></i>
        </button>
    </form>


</main>

<%@ include file="/jsp/components/footer.jsp" %>

<!-- Bootstrap JS and dependencies (Popper.js) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<!-- Thêm script này trước thẻ đóng </body> -->
<script>
    var deleteCompetitionId;

    var confirmDeleteModal = document.getElementById('confirmDeleteModal');
    confirmDeleteModal.addEventListener('show.bs.modal', function (event) {
        var button = event.relatedTarget;
        deleteCompetitionId = button.getAttribute('data-id');
    });

    document.getElementById('confirmDeleteBtn').addEventListener('click', function () {
        var form = document.querySelector(`form.delete-form input[value="${deleteCompetitionId}"]`).parentElement;
        form.submit();
    });
</script>


</body>
</html>
