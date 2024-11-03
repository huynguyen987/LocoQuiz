<%@ page import="entity.Competition" %>
<%@ page import="java.util.List" %>
<%@ page import="entity.quiz" %>
<%@ page import="entity.classs" %>
<!-- File: /jsp/competition-form.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/jsp/components/header.jsp" %>
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
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
</head>
<body>

<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <h2 class="mb-4 text-center"><c:out value="${pageTitle}" /></h2>

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <c:out value="${errorMessage}" />
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>

            <div class="card">
                <div class="card-body">
                    <form class="needs-validation" action="${pageContext.request.contextPath}/CompetitionController" method="post" novalidate>
                        <input type="hidden" name="action" value="${competition != null ? 'edit' : 'create'}" />
                        <c:if test="${competition != null}">
                            <input type="hidden" name="id" value="${competition.id}" />
                        </c:if>

                        <!-- Tên Cuộc Thi -->
                        <div class="mb-3">
                            <label for="name" class="form-label">Tên Cuộc Thi <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" id="name" name="name"
                                   value="<c:out value="${competition != null ? competition.name : ''}" />" required />
                        </div>

                        <!-- Mô Tả -->
                        <div class="mb-3">
                            <label for="description" class="form-label">Mô Tả <span class="text-danger">*</span></label>
                            <textarea class="form-control" id="description" name="description" rows="4" required><c:out value="${competition != null ? competition.description : ''}" /></textarea>
                        </div>

                        <!-- Lớp Học -->
                        <div class="mb-3">
                            <label for="classId" class="form-label">Lớp Học <span class="text-danger">*</span></label>
                            <select class="form-select" id="classId" name="classId" required>
                                <option value="">-- Chọn Lớp Học --</option>
                                <c:forEach var="cls" items="${classes}">
                                    <option value="${cls.id}"
                                            <c:if test="${competition != null && cls.id == competition.classId}">selected</c:if>>
                                            ${cls.name}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Quiz Liên Kết -->
                        <div class="mb-3">
                            <label for="quizId" class="form-label">Quiz Liên Kết <span class="text-danger">*</span></label>
                            <select class="form-select" id="quizId" name="quizId" required>
                                <option value="">-- Chọn Quiz --</option>
                                <c:forEach var="quiz" items="${quizzes}">
                                    <option value="${quiz.id}"
                                            <c:if test="${competition != null && quiz.id == competition.quizId}">selected</c:if>>
                                            ${quiz.name}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Thời Gian Làm Bài -->
                        <div class="mb-3">
                            <label for="timeLimit" class="form-label">Thời Gian Làm Bài (phút) <span class="text-danger">*</span></label>
                            <input type="number" class="form-control" id="timeLimit" name="timeLimit" min="1"
                                   value="<c:out value="${competition != null ? competition.timeLimit / 60 : ''}" />" required />
                        </div>

                        <!-- Số Lượng Câu Hỏi -->
                        <div class="mb-3">
                            <label for="questionCount" class="form-label">Số Lượng Câu Hỏi <span class="text-danger">*</span></label>
                            <input type="number" class="form-control" id="questionCount" name="questionCount" min="1"
                                   value="<c:out value="${competition != null ? competition.questionCount : ''}" />" required />
                        </div>

                        <!-- Xáo Trộn Câu Hỏi -->
                        <div class="form-check mb-3">
                            <input class="form-check-input" type="checkbox" id="shuffle" name="shuffle"
                                   <c:if test="${competition != null && competition.shuffleQuestions}">checked</c:if> />
                            <label class="form-check-label" for="shuffle">
                                Xáo Trộn Câu Hỏi
                            </label>
                        </div>

                        <!-- Thời Gian Bắt Đầu -->
                        <div class="mb-3">
                            <label for="accessStartTime" class="form-label">Thời Gian Bắt Đầu <span class="text-danger">*</span></label>
                            <fmt:formatDate var="formattedStartTime" value="${competition.accessStartTime}" pattern="yyyy-MM-dd'T'HH:mm" />
                            <input type="datetime-local" class="form-control" id="accessStartTime" name="accessStartTime"
                                   value="${formattedStartTime}" required />
                        </div>

                        <!-- Thời Gian Kết Thúc -->
                        <div class="mb-3">
                            <label for="accessEndTime" class="form-label">Thời Gian Kết Thúc <span class="text-danger">*</span></label>
                            <fmt:formatDate var="formattedEndTime" value="${competition.accessEndTime}" pattern="yyyy-MM-dd'T'HH:mm" />
                            <input type="datetime-local" class="form-control" id="accessEndTime" name="accessEndTime"
                                   value="${formattedEndTime}" required />
                        </div>

                        <!-- Hành Động -->
                        <div class="d-flex justify-content-between">
                            <button type="submit" class="btn btn-primary"><c:out value="${competition != null ? 'Cập Nhật' : 'Tạo'}" /></button>
                            <a href="${pageContext.request.contextPath}/CompetitionController?action=list" class="btn btn-secondary">Hủy</a>
                        </div>
                    </form>
                    <!-- Thêm vào cuối form, trước thẻ </form> -->
                    <script>
                        // Example starter JavaScript for disabling form submissions if there are invalid fields
                        (function () {
                            'use strict'

                            var forms = document.querySelectorAll('.needs-validation')

                            // Loop over them and prevent submission
                            Array.prototype.slice.call(forms)
                                .forEach(function (form) {
                                    form.addEventListener('submit', function (event) {
                                        if (!form.checkValidity()) {
                                            event.preventDefault()
                                            event.stopPropagation()
                                        }

                                        form.classList.add('was-validated')
                                    }, false)
                                })
                        })()
                    </script>

                </div>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap JS và các plugin cần thiết -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<%@ include file="/jsp/components/footer.jsp" %>
</body>
</html>
