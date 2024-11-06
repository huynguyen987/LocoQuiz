<%@ page import="java.util.List" %>
<%@ page import="dao.UsersDAO" %>
<%@ page import="entity.Users" %>
<%@ page import="java.sql.SQLException" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Manage Users</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
<%
  // Initialize UsersDAO
  UsersDAO usersDAO = new UsersDAO();

  // Fetch list of users
  List<Users> usersList;
  try {
    usersList = usersDAO.getAllUsers();
  } catch (SQLException | ClassNotFoundException e) {
    throw new RuntimeException(e);
  }


%>

<div class="container">
  <!-- Sidebar Navigation -->
  <aside class="sidebar">
    <h2>Admin Panel</h2>
    <nav>
      <ul>
        <li><a href="${pageContext.request.contextPath}/jsp/admin.jsp">Dashboard</a></li>
        <li class="active"><a href="${pageContext.request.contextPath}/jsp/manage_users.jsp">Manage Users</a></li>
        <li><a href="${pageContext.request.contextPath}/jsp/manage_quizzes.jsp">Manage Quizzes</a></li>
        <li><a href="${pageContext.request.contextPath}/LogoutServlet">Logout</a></li>
      </ul>
    </nav>
  </aside>

  <!-- Main Content -->
  <main class="main-content">
    <!-- Header Section -->
    <header>
      <h1>User Management</h1>
    </header>

    <!-- Display Messages -->
    <c:if test="${not empty param.message}">
      <div class="alert success">${param.message}</div>
    </c:if>
    <c:if test="${not empty param.error}">
      <div class="alert error">${param.error}</div>
    </c:if>

    <div class="user-filters">
      <input type="text" id="searchUser" placeholder="Search by name or role">
      <button type="button" onclick="filterUsers()">Search</button>
      <button type="button" onclick="resetSearch()">Reset</button>
      <a href="${pageContext.request.contextPath}/jsp/add_user.jsp" class="button add-user-btn">Add New User</a>
    </div>

    <!-- User Table -->
    <div class="table-responsive">
      <table id="userTable">
        <thead>
        <tr>
          <th>User ID</th>
          <th>Username</th>
          <th>Role</th>
          <th>Email</th>
          <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <% for (Users user : usersList) { %>
        <tr>
          <td><%= user.getId() %></td>
          <td><%= user.getUsername() %></td>
          <td>
            <%=
            user.getRole_id() == Users.ROLE_ADMIN ? "Admin" :
                    (user.getRole_id() == Users.ROLE_TEACHER ? "Teacher" : "Student")
            %>
          </td>
          <td><%= user.getEmail() %></td>
          <td class="actions">
            <form action="${pageContext.request.contextPath}/UserServlet" method="post">
              <input type="hidden" name="userId" value="<%= user.getId() %>">
              <button type="submit" name="action" value="updateRole" class="action-btn update">Update Role</button>
              <button type="submit" name="action" value="toggleStatus" class="action-btn toggle">
                <%=
                user.getRole_id() == Users.ROLE_ADMIN ? "Demote to Teacher" :
                        (user.getRole_id() == Users.ROLE_TEACHER ? "Promote to Student" : "Promote to Admin")
                %>
              </button>
              <button type="submit" name="action" value="deleteUser" class="action-btn delete" onclick="return confirm('Are you sure you want to delete this user?');">Delete</button>
            </form>
          </td>
        </tr>
        <% } %>
        </tbody>
      </table>
    </div>

<!-- Footer Section -->
<footer>
  <p>© 2023 Quiz Admin Dashboard. All rights reserved.</p>
</footer>

<script src="${pageContext.request.contextPath}/js/admin.js"></script>
</body>
</html>