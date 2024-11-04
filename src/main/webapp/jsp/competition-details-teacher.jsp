<%@ page import="entity.Users" %>
<!-- File: /jsp/competition-list.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/jsp/components/header.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
  Users currentUser = (Users) session.getAttribute("user");
  System.out.println("User view competition list: " + currentUser.getUsername());
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
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common2.css">
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
    <table class="competition-list-table">
      <thead>
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
        <tr>
          <td>${competition.id}</td>
          <td>${competition.name}</td>
          <td>${competition.className}</td>
          <td>${competition.quizName}</td>
          <td>${competition.timeLimit / 60} phút</td>
          <td>${competition.questionCount}</td>
          <td>
            <a href="${pageContext.request.contextPath}/CompetitionController?action=view&id=${competition.id}" class="btn btn-info btn-sm me-1" title="Chi Tiết">
              <i class="fas fa-eye"></i>
            </a>
            <c:if test="${currentUser.hasRole('teacher') || currentUser.hasRole('admin')}">
              <a href="${pageContext.request.contextPath}/CompetitionController?action=edit&id=${competition.id}" class="btn btn-warning btn-sm me-1" title="Chỉnh Sửa">
                <i class="fas fa-edit"></i>
              </a>
              <button class="btn btn-danger btn-sm delete-button" data-id="${competition.id}" title="Xóa">
                <i class="fas fa-trash-alt"></i>
              </button>
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
  <div class="modal-confirm" id="deleteConfirmationModal">
    <div class="modal-content">
      <span class="btn-close">&times;</span>
      <h4>Xác Nhận Xóa</h4>
      <p>Bạn có chắc chắn muốn xóa cuộc thi này không?</p>
      <div class="modal-footer">
        <button class="btn btn-secondary">Hủy</button>
        <button class="btn btn-danger">Xóa</button>
      </div>
    </div>
  </div>

</main>

<!-- Bootstrap JS và các plugin cần thiết -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<!-- Font Awesome JS (nếu cần thiết) -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/js/all.min.js"></script>
<!-- Custom JS for Delete Confirmation -->
<script>
  // Get modal elements
  const modal = document.getElementById('deleteConfirmationModal');
  const closeModal = document.querySelector('.btn-close');
  const cancelButton = document.querySelector('.btn-secondary');
  const confirmButton = document.querySelector('.btn-danger');

  // Function to open modal
  function openModal(competitionId) {
    modal.style.display = 'flex';
    // Store competitionId in confirm button data attribute
    confirmButton.setAttribute('data-id', competitionId);
  }

  // Function to close modal
  function closeModalFunc() {
    modal.style.display = 'none';
    confirmButton.removeAttribute('data-id');
  }

  // Event listeners for closing modal
  closeModal.addEventListener('click', closeModalFunc);
  cancelButton.addEventListener('click', closeModalFunc);
  window.addEventListener('click', function(event) {
    if (event.target == modal) {
      closeModalFunc();
    }
  });

  // Attach openModal to delete buttons
  const deleteButtons = document.querySelectorAll('.delete-button');
  deleteButtons.forEach(button => {
    button.addEventListener('click', function() {
      const competitionId = this.getAttribute('data-id');
      openModal(competitionId);
    });
  });

  // Confirm delete action
  confirmButton.addEventListener('click', function() {
    const competitionId = this.getAttribute('data-id');
    if (competitionId) {
      // Redirect to delete URL or submit a form
      window.location.href = `${window.location.origin}${window.location.pathname}?action=delete&id=${competitionId}`;
    }
  });
</script>

<%@ include file="/jsp/components/footer.jsp" %>
</body>
</html>
