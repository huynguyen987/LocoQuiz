<%@ page import="java.util.List" %>
<%@ page import="dao.UsersDAO" %>
<%@ page import="entity.Users" %>
<%@ page import="java.sql.SQLException" %>
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
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Manage Users</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
<header>
  <h1>Manage Users</h1>
</header>

<div class="user-management">
  <h2>User List</h2>
  <table>
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
      <td><%= user.getRole_id() == 1 ? "Admin" : (user.getRole_id() == 2 ? "Teacher" : "Student") %></td>
      <td><%= user.getEmail() %></td>
      <td>
        <form action="${pageContext.request.contextPath}/UserServlet" method="post">

        <input type="hidden" name="userId" value="<%= user.getId() %>">
          <button type="submit" name="action" value="updateRole">Update Role</button>
          <button type="submit" name="action" value="toggleStatus">
            <%= user.getRole_id() == 1 ? "Deactivate" : "Activate" %>
          </button>
          <button type="submit" name="action" value="deleteUser" onclick="return confirm('Are you sure you want to delete this user?');">Delete</button>
        </form>
      </td>
    </tr>
    <% } %>
    </tbody>
  </table>
</div>

<footer>
  <p>© 2023 Quiz Admin Dashboard. All rights reserved.</p>
</footer>

<script src="${pageContext.request.contextPath}/js/admin.js"></script>
</body>
</html>
