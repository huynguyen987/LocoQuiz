<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>Login</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f4f4f4;
      margin: 0;
      padding: 0;
    }
    .container {
      width: 80%;
      margin: auto;
      overflow: hidden;
    }
    form {
      background: #fff;
      padding: 20px;
      margin-top: 20px;
      border-radius: 5px;
      box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    }
    form h2 {
      margin-top: 0;
    }
    form input[type="text"], form input[type="password"] {
      width: 100%;
      padding: 10px;
      margin: 10px 0;
    }
    form input[type="submit"] {
      background: #77aaff;
      color: #fff;
      border: 0;
      padding: 10px;
      cursor: pointer;
    }
  </style>
</head>
<body>
<div class="container">
  <form action="login" method="post">
    <h2>Login</h2>
    <label for="username">Username:</label>
    <input type="text" id="username" name="username" required>
    <label for="password">Password:</label>
    <input type="password" id="password" name="password" required>
    <input type="submit" value="Login">
  </form>
</div>
</body>
</html>