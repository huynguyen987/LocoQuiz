<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Quản Lý Tài Liệu Tham Khảo</title>
  <!-- Bootstrap CSS -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <!-- Custom CSS -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/referenceMaterials.css">
</head>
<body>
<div class="container my-4">
  <h1 class="text-center mb-4">Quản Lý Tài Liệu Tham Khảo</h1>

  <!-- Reference Material Creation Form -->
  <div class="card mb-4">
    <div class="card-header">
      <h3>Thêm Tài Liệu Tham Khảo Mới</h3>
    </div>
    <div class="card-body">
      <form id="materialForm" class="needs-validation" novalidate>
        <div class="mb-3">
          <label for="title" class="form-label">Tiêu đề:</label>
          <input type="text" class="form-control" id="title" name="title" required>
          <div class="invalid-feedback">
            Vui lòng nhập tiêu đề tài liệu.
          </div>
        </div>

        <div class="mb-3">
          <label for="url" class="form-label">URL:</label>
          <input type="url" class="form-control" id="url" name="url" placeholder="https://example.com" required>
          <div class="invalid-feedback">
            Vui lòng nhập URL hợp lệ.
          </div>
        </div>

        <div class="mb-3">
          <label for="type" class="form-label">Loại tài liệu:</label>
          <select class="form-select" id="type" name="type" required>
            <option value="" selected disabled>Chọn loại tài liệu</option>
            <option value="Book">Sách</option>
            <option value="Article">Bài báo</option>
            <option value="Video">Video</option>
            <option value="Website">Website</option>
            <option value="Course">Khóa học</option> <!-- Thêm loại mới -->
            <option value="Podcast">Podcast</option>
            <option value="Lecture Notes">Lecture Notes</option>
            <option value="Research Paper">Research Paper</option>
          </select>
          <div class="invalid-feedback">
            Vui lòng chọn loại tài liệu.
          </div>
        </div>

        <div class="mb-3">
          <label for="description" class="form-label">Mô tả:</label>
          <textarea class="form-control" id="description" name="description" rows="3" required></textarea>
          <div class="invalid-feedback">
            Vui lòng nhập mô tả tài liệu.
          </div>
        </div>

        <button type="submit" class="btn btn-primary">Thêm Tài Liệu</button>
      </form>
    </div>
  </div>

  <!-- Filters and Search -->
  <div class="card mb-4">
    <div class="card-header">
      <h3>Tìm kiếm và Lọc Tài Liệu</h3>
    </div>
    <div class="card-body">
      <div class="row g-3">
        <div class="col-md-6">
          <input type="text" id="searchKeyword" class="form-control" placeholder="Tìm kiếm theo từ khóa...">
        </div>
        <div class="col-md-4">
          <select id="filterType" class="form-select">
            <option value="All">Tất cả loại tài liệu</option>
            <option value="Book">Sách</option>
            <option value="Article">Bài báo</option>
            <option value="Video">Video</option>
            <option value="Website">Website</option>
            <option value="Course">Khóa học</option>
            <option value="Podcast">Podcast</option>
            <option value="Lecture Notes">Lecture Notes</option>
            <option value="Research Paper">Research Paper</option>
          </select>
        </div>
        <div class="col-md-2">
          <button id="clearFilters" class="btn btn-secondary w-100">Xóa Lọc</button>
        </div>
      </div>
    </div>
  </div>

  <!-- Reference Material List -->
  <div class="card mb-4">
    <div class="card-header">
      <h3>Danh Sách Tài Liệu Tham Khảo</h3>
    </div>
    <div class="card-body">
      <div id="materialList" class="list-group">
        <!-- Materials will be dynamically added here -->
      </div>
    </div>
  </div>
</div>

<!-- Edit Material Modal -->
<div class="modal fade" id="editMaterialModal" tabindex="-1" aria-labelledby="editMaterialModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <form id="editMaterialForm" class="needs-validation" novalidate>
        <div class="modal-header">
          <h5 class="modal-title" id="editMaterialModalLabel">Chỉnh Sửa Tài Liệu Tham Khảo</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <input type="hidden" id="editMaterialId">
          <div class="mb-3">
            <label for="editTitle" class="form-label">Tiêu đề:</label>
            <input type="text" class="form-control" id="editTitle" name="editTitle" required>
            <div class="invalid-feedback">
              Vui lòng nhập tiêu đề tài liệu.
            </div>
          </div>

          <div class="mb-3">
            <label for="editUrl" class="form-label">URL:</label>
            <input type="url" class="form-control" id="editUrl" name="editUrl" placeholder="https://example.com" required>
            <div class="invalid-feedback">
              Vui lòng nhập URL hợp lệ.
            </div>
          </div>

          <div class="mb-3">
            <label for="editType" class="form-label">Loại tài liệu:</label>
            <select class="form-select" id="editType" name="editType" required>
              <option value="" selected disabled>Chọn loại tài liệu</option>
              <option value="Book">Sách</option>
              <option value="Article">Bài báo</option>
              <option value="Video">Video</option>
              <option value="Website">Website</option>
              <option value="Course">Khóa học</option> <!-- Thêm loại mới -->
              <option value="Podcast">Podcast</option>
              <option value="Lecture Notes">Lecture Notes</option>
              <option value="Research Paper">Research Paper</option>
            </select>
            <div class="invalid-feedback">
              Vui lòng chọn loại tài liệu.
            </div>
          </div>

          <div class="mb-3">
            <label for="editDescription" class="form-label">Mô tả:</label>
            <textarea class="form-control" id="editDescription" name="editDescription" rows="3" required></textarea>
            <div class="invalid-feedback">
              Vui lòng nhập mô tả tài liệu.
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
          <button type="submit" class="btn btn-primary">Lưu Thay Đổi</button>
        </div>
      </form>
    </div>
  </div>
</div>

<!-- Toast Notification -->
<div class="position-fixed bottom-0 end-0 p-3" style="z-index: 1100">
  <div id="successToast" class="toast align-items-center text-bg-success border-0" role="alert" aria-live="assertive" aria-atomic="true">
    <div class="d-flex">
      <div class="toast-body">
        Thêm tài liệu thành công!
      </div>
      <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
    </div>
  </div>
</div>

<!-- Bootstrap JS (bao gồm Popper.js) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<!-- Custom JS -->
<script src="${pageContext.request.contextPath}/js/referenceMaterials.js" defer></script>
</body>
</html>
