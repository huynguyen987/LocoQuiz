<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 9/16/2024
  Time: 11:01 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>Results Page</title>
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
    header {
      background: #333;
      color: #fff;
      padding-top: 30px;
      min-height: 70px;
      border-bottom: #77aaff 3px solid;
    }
    header a {
      color: #fff;
      text-decoration: none;
      text-transform: uppercase;
      font-size: 16px;
    }
    header ul {
      padding: 0;
      list-style: none;
    }
    header li {
      float: left;
      display: inline;
      padding: 0 20px 0 20px;
    }
    header #branding {
      float: left;
    }
    header #branding h1 {
      margin: 0;
    }
    header nav {
      float: right;
      margin-top: 10px;
    }
    .results-container {
      background: #fff;
      padding: 20px;
      margin-top: 20px;
      border-radius: 5px;
      box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    }
    .results-container h2 {
      margin-top: 0;
    }
  </style>
</head>
<body>
<header>
  <div class="container">
    <div id="branding">
      <h1>Quiz Practice</h1>
    </div>
    <nav>
      <ul>
        <li><a href="index.jsp">Home</a></li>
        <li><a href="quiz.jsp">Take a Quiz</a></li>
        <li><a href="results.jsp">Results</a></li>
      </ul>
    </nav>
  </div>
</header>
<section class="results-container">
  <div class="container">
    <h2>Results</h2>
    <p>Thank you for taking the quiz. Here are your results:</p>
    <p>Question 1: <%= request.getParameter("q1") %></p>
    <p>Question 2: <%= request.getParameter("q2") %></p>
  </div>
</section>
</body>
</html>