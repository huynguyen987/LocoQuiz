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
  <title>Quiz Page</title>
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
    .quiz-container {
      background: #fff;
      padding: 20px;
      margin-top: 20px;
      border-radius: 5px;
      box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    }
    .quiz-container h2 {
      margin-top: 0;
    }
    .quiz-container form {
      margin: 0;
    }
    .quiz-container .question {
      margin-bottom: 20px;
    }
    .quiz-container .question h3 {
      margin: 0;
    }
    .quiz-container .question ul {
      list-style: none;
      padding: 0;
    }
    .quiz-container .question li {
      margin-bottom: 10px;
    }
    .quiz-container .submit-btn {
      display: inline-block;
      color: #fff;
      background: #77aaff;
      padding: 10px 20px;
      text-decoration: none;
      border-radius: 5px;
      border: none;
      cursor: pointer;
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
<section class="quiz-container">
  <div class="container">
    <h2>Quiz</h2>
    <form action="results.jsp" method="post">
      <div class="question">
        <h3>Question 1: What is the capital of France?</h3>
        <ul>
          <li><input type="radio" name="q1" value="Paris"> Paris</li>
          <li><input type="radio" name="q1" value="London"> London</li>
          <li><input type="radio" name="q1" value="Berlin"> Berlin</li>
          <li><input type="radio" name="q1" value="Madrid"> Madrid</li>
        </ul>
      </div>
      <div class="question">
        <h3>Question 2: What is 2 + 2?</h3>
        <ul>
          <li><input type="radio" name="q2" value="3"> 3</li>
          <li><input type="radio" name="q2" value="4"> 4</li>
          <li><input type="radio" name="q2" value="5"> 5</li>
          <li><input type="radio" name="q2" value="6"> 6</li>
        </ul>
      </div>
      <button type="submit" class="submit-btn">Submit</button>
    </form>
  </div>
</section>
</body>
</html>