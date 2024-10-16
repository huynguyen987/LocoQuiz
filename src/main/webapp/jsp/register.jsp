<!DOCTYPE html>
<html>
<head>
  <title>Register</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/register.css">
</head>
<body>
<div class="register-container">
  <h2>Register</h2>
  <form action="<%= request.getContextPath() %>/register" method="post">
    <input type="text" name="username" placeholder="Username" required>
    <input type="password" name="password_hash" placeholder="Password" required>
    <input type="email" name="email" placeholder="Email" required>
    <select name="gender" required>
      <option value="" disabled selected>Gender</option>
      <option value="Male">Male</option>
      <option value="Female">Female</option>
      <option value="Other">Other</option>
    </select>
    <select name="role_id" required>
      <option value="" disabled selected>Role</option>
      <option value="2">User</option>
      <option value="1">Teacher</option>
    </select>
    <input type="submit" value="Register">
  </form>

  <!-- Display error message if username or email exists -->
  <% String errorMessage = (String) request.getAttribute("error"); %>
  <% if (errorMessage != null) { %>
  <div class="error-message">
    <%= errorMessage %>
  </div>
  <% } %>

  <!-- Add Back to Home Button -->
  <div class="back-home">
    <a href="<%= request.getContextPath() %>/index.jsp" class="btn-back">Back to Home</a>
  </div>
</div>
</body>
</html>
