<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>User Registration</title>
  <style>
    /* Basic styling for form */
    body {
      font-family: Arial, sans-serif;
      background-color: #f2f2f2;
    }
    .container {
      width: 400px;
      margin: 50px auto;
      padding: 30px;
      background-color: white;
      border-radius: 4px;
      box-shadow: 0 0 10px rgba(0, 0, 0, 0.04);
    }
    .container h2 {
      text-align: center;
    }
    .container .form-group {
      margin-bottom: 15px;
    }
    .container .form-group label {
      display: block;
      margin-bottom: 5px;
    }
    .container .form-group input,
    .container .form-group select {
      width: 100%;
      padding: 8px;
      box-sizing: border-box;
    }
    .container .error {
      color: red;
      margin-bottom: 15px;
      text-align: center;
    }
    .container .hint {
      font-size: 12px;
      color: #555;
    }
    .container button {
      width: 100%;
      padding: 10px;
      background-color: #4285F4;
      border: none;
      color: white;
      font-size: 16px;
      border-radius: 4px;
      cursor: pointer;
    }
    .container button:hover {
      background-color: #357ae8;
    }
  </style>
  <script>
    // Function to validate form inputs
    function validateForm() {
      let isValid = true;
      // Clear previous error messages
      document.getElementById("errorMessages").innerHTML = "";

      // Get form values
      const username = document.forms["registerForm"]["username"].value.trim();
      const password = document.forms["registerForm"]["password"].value.trim();
      const email = document.forms["registerForm"]["email"].value.trim();
      const phone = document.forms["registerForm"]["phone"].value.trim();

      let errorMessages = [];

      // Validate username (max 20 characters)
      if (username.length > 20) {
        isValid = false;
        errorMessages.push("Username must not exceed 20 characters.");
      }
      if (username.length < 5) {
        isValid = false;
        errorMessages.push("Username must be at least 5 characters.");
      }

      // Validate password (at least 8 characters, at least 1 number)
      const passwordRegex = /^(?=.*[0-9]).{8,}$/;
      if (!passwordRegex.test(password)) {
        isValid = false;
        errorMessages.push("Password must be at least 8 characters and include at least one number.");
      }

      // Validate email
      const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
      if (!emailRegex.test(email)) {
        isValid = false;
        errorMessages.push("Please enter a valid email address.");
      }

      // Validate phone number (10-11 digits)
      const phoneRegex = /^[0-9]{10,11}$/;
      if (!phoneRegex.test(phone)) {
        isValid = false;
        errorMessages.push("Phone number must be 10 or 11 digits.");
      }

      // Display error messages if any
      if (!isValid) {
        let errorHtml = "<ul>";
        errorMessages.forEach(function(message) {
          errorHtml += "<li>" + message + "</li>";
        });
        errorHtml += "</ul>";
        document.getElementById("errorMessages").innerHTML = errorHtml;
      }

      return isValid;
    }

    // Function to show hints
    function showHint(element, hintText) {
      const hintElement = document.getElementById(element.id + "Hint");
      hintElement.textContent = hintText;
    }

    function hideHint(element) {
      const hintElement = document.getElementById(element.id + "Hint");
      hintElement.textContent = "";
    }
  </script>
</head>
<body>
<div class="container">
  <h2>User Registration</h2>

  <!-- Display server-side error message -->
  <c:if test="${not empty error}">
    <div class="error">${error}</div>
  </c:if>

  <form name="registerForm" action="${pageContext.request.contextPath}/register" method="post" onsubmit="return validateForm();">
    <div id="errorMessages" class="error"></div>

    <div class="form-group">
      <label for="username">Username:</label>
      <input type="text" id="username" name="username" maxlength="20"
             onfocus="showHint(this, '5-20 characters: letters, numbers, ., _, -')"
             onblur="hideHint(this)" required>
      <div id="usernameHint" class="hint"></div>
    </div>

    <div class="form-group">
      <label for="password">Password:</label>
      <input type="password" id="password" name="password" minlength="8"
             onfocus="showHint(this, 'At least 8 characters and include at least one number')"
             onblur="hideHint(this)" required>
      <div id="passwordHint" class="hint"></div>
    </div>

    <div class="form-group">
      <label for="email">Email:</label>
      <input type="email" id="email" name="email" maxlength="50"
             onfocus="showHint(this, 'Enter a valid email address')"
             onblur="hideHint(this)" required>
      <div id="emailHint" class="hint"></div>
    </div>

    <div class="form-group">
      <label for="phone">Phone Number:</label>
      <input type="text" id="phone" name="phone" maxlength="11"
             onfocus="showHint(this, '10-11 digits')"
             onblur="hideHint(this)" required>
      <div id="phoneHint" class="hint"></div>
    </div>

    <div class="form-group">
      <label for="gender">Gender:</label>
      <select id="gender" name="gender" required>
        <option value="">--Select Gender--</option>
        <option value="Male">Male</option>
        <option value="Female">Female</option>
        <option value="Other">Other</option>
      </select>
      <div id="genderHint" class="hint"></div>
    </div>

    <div class="form-group">
      <label for="role_id">Role:</label>
      <select id="role_id" name="role_id" required>
        <option value="">--Select Role--</option>
        <option value="1">Teacher</option>
        <option value="2">User</option>
      </select>
      <div id="role_idHint" class="hint"></div>
    </div>

    <button type="submit">Register</button>
    <c:if test="${not empty error}">
      <div class="error">${error}</div>
        <c:remove var="error" scope="session"/>
    </c:if>
  </form>
</div>

<script>
  // Optional: Enhance hints with tooltips or other UI elements
</script>
</body>
</html>
