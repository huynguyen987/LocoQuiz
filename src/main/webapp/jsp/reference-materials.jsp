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
      <form id="materialForm">
        <div class="mb-3">
          <label for="title" class="form-label">Tiêu đề:</label>
          <input type="text" class="form-control" id="title" name="title" required>
        </div>

        <div class="mb-3">
          <label for="url" class="form-label">URL:</label>
          <input type="url" class="form-control" id="url" name="url" placeholder="https://example.com">
        </div>

        <div class="mb-3">
          <label for="type" class="form-label">Loại tài liệu:</label>
          <select class="form-select" id="type" name="type">
            <option value="Book">Sách</option>
            <option value="Article">Bài báo</option>
            <option value="Video">Video</option>
            <option value="Website">Website</option>
          </select>
        </div>

        <div class="mb-3">
          <label for="description" class="form-label">Mô tả:</label>
          <textarea class="form-control" id="description" name="description" rows="3"></textarea>
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

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<!-- Custom JS -->
<script src="${pageContext.request.contextPath}/js/referenceMaterials.js" defer></script>
</body>
</html>
