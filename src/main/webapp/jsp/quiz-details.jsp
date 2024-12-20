<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="entity.quiz, entity.Tag, entity.Users" %>
<%@ page import="dao.QuizDAO" %>
<%@ page import="java.util.List" %>
<%@ page import="dao.UserLibraryDAO" %>

<%
  // Kiểm tra xem tham số "id" có tồn tại và hợp lệ hay không
  String idParam = request.getParameter("id");
  if (idParam == null || idParam.trim().isEmpty()) {
    out.println("<p>Invalid Quiz ID.</p>");
    return;
  }

  int quizId = 0;
  try {
    quizId = Integer.parseInt(idParam);
  } catch (NumberFormatException e) {
    out.println("<p>Invalid Quiz ID format.</p>");
    return;
  }

  QuizDAO quizDAO = new QuizDAO();
  quiz q = null;
  try {
    q = quizDAO.getQuizById(quizId);
  } catch (Exception e) {
    e.printStackTrace();
    out.println("<p>Error retrieving quiz details.</p>");
    return;
  }

  if (q == null) {
    out.println("<p>Quiz not found.</p>");
    return;
  }

  // Lấy thông tin người dùng từ session để kiểm tra quyền hạn
  Users currentUser = (Users) session.getAttribute("user");
  String userRole = (currentUser != null) ? currentUser.getRoleName() : "";

  // Kiểm tra xem người dùng đã thêm bài kiểm tra này vào thư viện của mình chưa
  UserLibraryDAO userLibraryDAO = new UserLibraryDAO();
  boolean isInLibrary = false;
  if (currentUser != null) {
    isInLibrary = userLibraryDAO.isQuizInLibrary(currentUser.getId(), quizId);
  }

  String role = (String) session.getAttribute("role");
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title><%= q.getName() %> - Quiz Details - QuizLoco</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/quiz-details.css">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link href="<%= request.getContextPath() %>/css/fonts.css" rel="stylesheet">
  <!-- Google Fonts (Optional for better typography) -->
  <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap" rel="stylesheet">
  <!-- Font Awesome for Icons -->
  <script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script>
  <style>
    /* Bạn có thể thêm các kiểu CSS tùy chỉnh tại đây hoặc trong tệp quiz-details.css */
    .btn-back-home {
      display: inline-block;
      padding: 10px 20px;
      background-color: #007bff; /* Grey color */
      color: #fff;
      text-decoration: none;
      border-radius: 4px;
      font-weight: 500;
      font-size: 1rem;
      transition: background-color 0.3s ease, color 0.3s ease;
    }

    .btn-back-home:hover {
      background-color: #0056b3; /* Darker grey */
      color: #f8f9fa;
    }

    .btn-edit, .btn-delete, .btn-quiz, .btn-add {
      display: inline-block;
      padding: 10px 20px;
      margin: 5px;
      text-decoration: none;
      color: #fff;
      border-radius: 4px;
      transition: background-color 0.3s ease;
    }
    .btn-edit {
      background-color: #007bff;
    }
    .btn-edit:hover {
      background-color: #0056b3;
    }
    .btn-delete {
      background-color: #dc3545;
    }
    .btn-delete:hover {
      background-color: #c82333;
    }
    .btn-quiz {
      background-color: #28a745;
    }
    .btn-quiz:hover {
      background-color: #218838;
    }
    .btn-add {
      background-color: #17a2b8;
    }
    .btn-add:hover {
      background-color: #117a8b;
    }
    .active {
      color: #28a745;
    }
    .inactive {
      color: #dc3545;
    }
    .toggle-description {
      cursor: pointer;
      color: #007bff;
      text-decoration: underline;
      font-size: 0.9em;
    }
  </style>
</head>
<body>

<main class="container">
  <div class="quiz-header">
    <a href="#" id="backToHome" class="btn-back-home">Back to Home</a>
    <h1><%= q.getName() %></h1>
    <div class="quiz-meta">
      <span><i class="fas fa-calendar-alt"></i> Created At: <%= q.getCreated_at() %></span>
      <span><i class="fas fa-sync-alt"></i> Updated At: <%= q.getUpdated_at() %></span>
      <span><i class="fas fa-eye"></i> Views: <span id="viewCount"><%= q.getViews() %></span></span>
      <span><i class="fas fa-tags"></i> Type: <%= q.getType_id() %></span>
      <span>
        <i class="fas fa-circle <% if(q.isStatus()) { %>active<% } else { %>inactive<% } %>"></i>
        <%= q.isStatus() ? "Active" : "Inactive" %>
      </span>
    </div>
  </div>

  <section class="quiz-details">
    <h2>Description <span id="toggleDesc" class="toggle-description"></span></h2>
    <p id="quizDescription"><%= q.getDescription() %></p>

    <!-- Display Tags -->
    <div class="quiz-tags">
      <h3>Tags:</h3>
      <ul>
        <%
          List<Tag> tags = q.getTag();
          if (tags != null && !tags.isEmpty()) {
            for (Tag tag : tags) {
        %>
        <li>
          <a href="<%= request.getContextPath() %>/SearchByTagServlet?tag=<%= tag.getName() %>">
            <%= tag.getName() %>
          </a>
        </li>
        <%
          }
        } else {
        %>
        <li>No tags available.</li>
        <%
          }
        %>
      </ul>
    </div>

    <!-- Action Buttons -->
    <div class="quiz-actions">
      <a href="<%= request.getContextPath() %>/TakeQuizServlet?id=<%= q.getId() %>" class="btn-quiz">
        <i class="fas fa-play"></i> Take Quiz
      </a>

      <!-- Thêm phần chức năng Thư viện -->
      <% if (currentUser != null) { %>
      <% if (isInLibrary) { %>
      <!-- Hiển thị nút "Remove from Library" -->
      <form action="<%= request.getContextPath() %>/RemoveFromLibraryServlet" method="post" style="display:inline;">
        <input type="hidden" name="quizId" value="<%= q.getId() %>">
        <button type="submit" class="btn-delete">
          <i class="fas fa-minus"></i> Remove from Library
        </button>
      </form>
      <% } else { %>
      <!-- Hiển thị nút "Add to Library" -->
      <form action="<%= request.getContextPath() %>/AddToLibraryServlet" method="post" style="display:inline;">
        <input type="hidden" name="quizId" value="<%= q.getId() %>">
        <button type="submit" class="btn-add">
          <i class="fas fa-plus"></i> Add to Library
        </button>
      </form>
      <% } %>
      <% } else { %>
      <!-- Hiển thị lời nhắc đăng nhập để thêm vào thư viện -->
      <p><a href="<%= request.getContextPath() %>/jsp/login.jsp">Log in</a> to add this quiz to your library.</p>
      <% } %>

      <%-- Hiển thị nút "Edit" và "Delete" nếu người dùng là admin hoặc teacher --%>
      <%
        // Display Edit and Delete buttons if user is admin or teacher
        if (currentUser != null) {
          String roles = currentUser.getRoleName();
          if (roles != null && (roles.equalsIgnoreCase("admin") || roles.equalsIgnoreCase("teacher"))) {
      %>
      <a href="<%= request.getContextPath() %>/EditQuizServlet?quizId=<%= q.getId() %>" class="btn-edit">
        <i class="fas fa-edit"></i> Edit Quiz
      </a>

      <a href="<%= request.getContextPath() %>/DeleteQuizServlet?id=<%= q.getId() %>" class="btn-delete" onclick="return confirm('Are you sure you want to delete this quiz?');">
        <i class="fas fa-trash-alt"></i> Delete Quiz
      </a>
      <%
          }
        }
      %>
    </div>
  </section>
</main>

<!-- Include JavaScript -->
<script src="<%= request.getContextPath() %>/js/quiz-details.js"></script>
<script>
  document.getElementById('backToHome').addEventListener('click', function(event) {
    event.preventDefault();
    var role = '<%= role %>';
    if (role === 'student') {
      window.location.href = '<%= request.getContextPath() %>/jsp/student.jsp';
    } else if (role === 'teacher') {
      window.location.href = '<%= request.getContextPath() %>/jsp/teacher.jsp';
    } else if (role === 'admin') {
      window.location.href = '<%= request.getContextPath() %>/jsp/admin.jsp';
    } else {
      window.location.href = '<%= request.getContextPath() %>/index.jsp';
    }
  });
</script>
</body>
</html>
