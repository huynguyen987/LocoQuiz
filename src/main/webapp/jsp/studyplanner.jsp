<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Study Planner - Lập Kế Hoạch Học Tập</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- FullCalendar CSS -->
    <link href='https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/index.global.min.css' rel='stylesheet' />
    <!-- Chart.js CSS -->
    <link href="https://cdn.jsdelivr.net/npm/chart.js@4.3.0/dist/chart.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/planner.css">
</head>
<body>
<div class="container my-4">
    <h1 class="text-center mb-4">Study Planner - Lập Kế Hoạch Học Tập</h1>

    <!-- Progress Tracker -->
    <div class="mb-4">
        <h3>Tiến Độ Học Tập</h3>
        <div class="progress">
            <div id="progressBar" class="progress-bar" role="progressbar" style="width: 0%;" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100">0%</div>
        </div>
        <p id="progressText" class="mt-2">Hoàn thành 0/0 nhiệm vụ</p>
    </div>

    <!-- Task Creation Form -->
    <div class="card mb-4">
        <div class="card-header">
            <h3>Tạo Nhiệm Vụ Học Tập</h3>
        </div>
        <div class="card-body">
            <form id="taskForm">
                <div class="mb-3">
                    <label for="title" class="form-label">Tiêu đề:</label>
                    <input type="text" class="form-control" id="title" name="title" required>
                </div>

                <div class="mb-3">
                    <label for="description" class="form-label">Mô tả:</label>
                    <textarea class="form-control" id="description" name="description" rows="3"></textarea>
                </div>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="date" class="form-label">Ngày thực hiện:</label>
                        <input type="date" class="form-control" id="date" name="date" required>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="time" class="form-label">Giờ thực hiện:</label>
                        <input type="time" class="form-control" id="time" name="time" required>
                    </div>
                </div>

                <div class="mb-3">
                    <label for="priority" class="form-label">Độ ưu tiên:</label>
                    <select class="form-select" id="priority" name="priority">
                        <option value="Cao">Cao</option>
                        <option value="Trung bình">Trung bình</option>
                        <option value="Thấp">Thấp</option>
                    </select>
                </div>

                <button type="submit" class="btn btn-primary">Thêm Nhiệm Vụ</button>
            </form>
        </div>
    </div>

    <!-- Filters and Search -->
    <div class="card mb-4">
        <div class="card-header">
            <h3>Tìm kiếm và Lọc Nhiệm Vụ</h3>
        </div>
        <div class="card-body">
            <div class="row g-3">
                <div class="col-md-4">
                    <input type="text" id="searchKeyword" class="form-control" placeholder="Tìm kiếm theo từ khóa...">
                </div>
                <div class="col-md-3">
                    <select id="filterStatus" class="form-select">
                        <option value="All">Tất cả trạng thái</option>
                        <option value="Chưa làm">Chưa làm</option>
                        <option value="Đang làm">Đang làm</option>
                        <option value="Hoàn thành">Hoàn thành</option>
                    </select>
                </div>
                <div class="col-md-3">
                    <select id="filterPriority" class="form-select">
                        <option value="All">Tất cả độ ưu tiên</option>
                        <option value="Cao">Cao</option>
                        <option value="Trung bình">Trung bình</option>
                        <option value="Thấp">Thấp</option>
                    </select>
                </div>
                <div class="col-md-2">
                    <button id="clearFilters" class="btn btn-secondary w-100">Xóa Lọc</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Task List -->
    <div class="card mb-4">
        <div class="card-header">
            <h3>Danh Sách Nhiệm Vụ</h3>
        </div>
        <div class="card-body">
            <div id="taskList" class="list-group">
                <!-- Tasks will be dynamically added here -->
            </div>
        </div>
    </div>

    <!-- Statistics and Rewards -->
    <div class="row mb-4">
        <div class="col-md-6">
            <div class="card">
                <div class="card-header">
                    <h3>Thống Kê Học Tập</h3>
                </div>
                <div class="card-body">
                    <canvas id="statsChart"></canvas>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="card">
                <div class="card-header">
                    <h3>Hệ Thống Phần Thưởng</h3>
                </div>
                <div class="card-body">
                    <p id="pointsText">Điểm của bạn: 0</p>
                    <div id="rewards">
                        <!-- Rewards will be dynamically added here -->
                    </div>
                    <button id="claimReward" class="btn btn-success mt-3">Nhận Phần Thưởng</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Study Calendar -->
    <div class="card mb-4">
        <div class="card-header">
            <h3>Lịch Học</h3>
        </div>
        <div class="card-body">
            <div id='calendar'></div>
        </div>
    </div>

</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<!-- FullCalendar JS -->
<script src='https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/index.global.min.js'></script>
<!-- Chart.js -->
<script src="https://cdn.jsdelivr.net/npm/chart.js@4.3.0/dist/chart.umd.min.js"></script>
<!-- Custom JS -->
<script src="${pageContext.request.contextPath}/js/planner.js" defer></script>
</body>
</html>
