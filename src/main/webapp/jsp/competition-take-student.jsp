<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="entity.*" %>
<%@ include file="components/header.jsp" %>
<%@ include file="components/sidebar.jsp" %>

<%
  Users currentUser = (Users) session.getAttribute("user");
  Competition competition = (Competition) request.getAttribute("competition");
//  Users currentUser = (Users) session.getAttribute("user");
  System.out.println("currentUser: " + (currentUser != null ? currentUser.getUsername() : "null"));

  if (competition == null || currentUser == null || !currentUser.hasRole("student")) {
    response.sendRedirect(request.getContextPath() + "/CompetitionController?action=list");
    return;
  }

  // Pass contextPath to JavaScript
  String contextPath = request.getContextPath();
%>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Tham Gia Cuộc Thi - <%= competition.getName() %></title>
  <!-- Include CSS và JS cần thiết -->
  <link rel="stylesheet" href="<%= contextPath %>/css/common.css">
  <!-- Bootstrap CSS (nếu cần) -->
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <!-- Font Awesome -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css">
  <!-- Pass contextPath to JavaScript -->
  <script type="text/javascript">
    var contextPath = '<%= contextPath %>';
  </script>
</head>
<body>
<main>
  <div class="container mt-5">
    <!-- Hidden input to store competition ID -->
    <input type="hidden" id="competition-id" value="<%= competition.getId() %>" />

    <!-- Countdown Timer -->
    <div id="countdown-timer" class="mb-3 text-danger font-weight-bold"></div>

    <!-- Progress Bar -->
    <div class="progress mb-3">
      <div id="progress" class="progress-bar" role="progressbar" style="width: 0%;" aria-valuenow="0"
           aria-valuemin="0" aria-valuemax="100"></div>
    </div>

    <!-- Answered Questions Count -->
    <div class="d-flex justify-content-between mb-3">
      <div>
        <span id="answered-count">0</span> / <span id="total-count">0</span> câu đã trả lời
      </div>
      <div>
        <button id="prev-btn" class="btn btn-secondary">Trước</button>
        <button id="next-btn" class="btn btn-primary">Tiếp theo</button>
        <button id="submit-btn" class="btn btn-success" style="display: none;">Nộp bài</button>
      </div>
    </div>

    <!-- Question Number -->
    <div id="question-number" class="mb-2 h5"></div>

    <!-- Question Text -->
    <div id="question-text" class="mb-3"></div>

    <!-- Answer Options -->
    <div id="answer-options" class="mb-3"></div>

    <!-- Question Selector -->
    <div id="question-selector" class="mb-3"></div>
  </div>

  <!-- Result Modal -->
  <div class="modal fade" id="result-modal" tabindex="-1" aria-labelledby="result-modal-label" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Kết quả của bạn</h5>
          <button type="button" class="close" data-dismiss="modal" aria-label="Đóng">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <div class="modal-body">
          <p id="result-text"></p>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-primary" onclick="window.competition.exitCompetition()">Quay lại danh sách</button>
        </div>
      </div>
    </div>
  </div>
</main>
<!-- Include jQuery và Bootstrap JS -->
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<!-- Bootstrap JS -->
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
<!-- Include competition.js -->
<script src="<%= contextPath %>/js/competition.js"></script>

<!-- Additional Scripts for Theme Toggle, Live Search, Dropdowns, and Table Filtering -->
<script type="text/javascript">
  // Theme Toggle
  const darkModeToggle = document.getElementById('dark-mode-toggle');
  const body = document.body;

  if (darkModeToggle) {
    darkModeToggle.addEventListener('change', () => {
      body.classList.toggle('dark-mode');
      localStorage.setItem('darkMode', body.classList.contains('dark-mode'));
    });

    // Load Theme Preference
    window.addEventListener('load', () => {
      if (localStorage.getItem('darkMode') === 'true') {
        body.classList.add('dark-mode');
        darkModeToggle.checked = true;
      }
    });
  }

  // Live Search Functionality
  const liveSearchInput = document.getElementById('liveSearch');
  const searchResults = document.getElementById('searchResults');

  if (liveSearchInput && searchResults) {
    liveSearchInput.addEventListener('input', () => {
      const query = liveSearchInput.value.trim();

      if (query.length > 0) {
        // Fetch search results from server
        fetch(`${contextPath}/SearchServlet?query=${encodeURIComponent(query)}`)
                .then(response => response.json())
                .then(data => {
                  displaySearchResults(data);
                })
                .catch(error => {
                  console.error('Error fetching search results:', error);
                });
      } else {
        searchResults.style.display = 'none';
      }
    });
  }

  function displaySearchResults(results) {
    if (!searchResults) return; // Kiểm tra sự tồn tại của phần tử

    searchResults.innerHTML = '';

    if (results.length > 0) {
      results.forEach(result => {
        const li = document.createElement('li');
        li.textContent = result.title;
        li.addEventListener('click', () => {
          window.location.href = `${contextPath}/jsp/quiz-details.jsp?id=${result.id}`;
        });
        searchResults.appendChild(li);
      });

      searchResults.style.display = 'block';
    } else {
      searchResults.style.display = 'none';
    }
  }

  // User Profile Dropdown
  const userProfile = document.querySelector('.user-profile');
  const profileMenu = document.querySelector('.profile-menu');

  if (userProfile && profileMenu) {
    userProfile.addEventListener('click', (e) => {
      e.stopPropagation();
      profileMenu.style.display = profileMenu.style.display === 'block' ? 'none' : 'block';
    });

    document.addEventListener('click', () => {
      profileMenu.style.display = 'none';
    });
  }

  // Notifications Dropdown
  const notifications = document.querySelector('.notifications');
  const notificationDropdown = document.querySelector('.notification-dropdown');

  if (notifications && notificationDropdown) {
    notifications.addEventListener('click', (e) => {
      e.stopPropagation();
      notificationDropdown.style.display = notificationDropdown.style.display === 'block' ? 'none' : 'block';
    });

    document.addEventListener('click', () => {
      notificationDropdown.style.display = 'none';
    });
  }

  // Dynamic filtering for 'My Classes' table
  document.addEventListener('DOMContentLoaded', function() {
    const searchInput = document.getElementById('searchInput');
    const table = document.getElementById('classTable');

    if (searchInput && table) {
      searchInput.addEventListener('input', function() {
        const filter = searchInput.value.toLowerCase();
        const rows = table.getElementsByTagName('tr');

        // Loop through table rows, starting from 1 to skip header row
        for (let i = 1; i < rows.length; i++) {
          const classNameCell = rows[i].getElementsByTagName('td')[0];
          const teacherNameCell = rows[i].getElementsByTagName('td')[1];
          const className = classNameCell.textContent.toLowerCase();
          const teacherName = teacherNameCell.textContent.toLowerCase();

          if (className.includes(filter) || teacherName.includes(filter)) {
            rows[i].style.display = '';
          } else {
            rows[i].style.display = 'none';
          }
        }
      });
    }
  });
</script>
</body>
</html>
