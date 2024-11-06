<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <!-- Meta Tags -->
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Flashcard Practice</title>
  <!-- Stylesheets -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/flashcard.css">
  <!-- Favicon -->
  <link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico">
</head>
<body>
<div class="container">
  <!-- Header -->
  <header>
    <h1>Flashcard Practice</h1>
    <button id="theme-toggle" class="theme-toggle" aria-label="Toggle Dark Mode">🌙</button>
  </header>
  <!-- Back to home button (back to student page) -->
  <a href="${pageContext.request.contextPath}/jsp/student.jsp" class="back-button">← Back to Home</a>
  <!-- Navigation Tabs -->
  <nav class="tabs" role="tablist">
    <button class="tab active" data-tab="create" role="tab" aria-selected="true">Create Cards</button>
    <button class="tab" data-tab="practice" role="tab">Practice</button>
    <button class="tab" data-tab="quiz" role="tab">Quiz Mode</button>
    <button class="tab" data-tab="stats" role="tab">Statistics</button>
  </nav>

  <!-- Main Content -->
  <main>
    <!-- Create Tab -->
    <section id="create-tab" class="tab-content active" role="tabpanel">
      <!-- Controls -->
      <div class="controls">
        <input type="text" id="search-input" placeholder="Search cards...">
        <div class="control-buttons">
          <button class="secondary-button" id="import-button">Import Cards 📥</button>
          <button class="primary-button" id="export-button">Export Cards 📤</button>
          <button class="danger-button" id="delete-all-cards">Delete All Cards 🗑️</button>
          <button class="secondary-button" id="refresh-cards">Refresh Cards 🔄</button>
        </div>
      </div>

      <!-- File Input for Importing Cards -->
      <input type="file" id="import-file" accept=".json,.txt,.csv" style="display: none;">

      <!-- Stats Summary -->
      <div class="stats-summary">
        <div class="stat-item">
          <div class="stat-value" id="total-cards">0</div>
          <div class="stat-label">Total Cards</div>
        </div>
        <div class="stat-item">
          <div class="stat-value" id="last-added">--:--</div>
          <div class="stat-label">Last Added</div>
        </div>
        <div class="stat-item">
          <div class="stat-value" id="total-correct">0</div>
          <div class="stat-label">Remembered</div>
        </div>
        <div class="stat-item">
          <div class="stat-value" id="total-incorrect">0</div>
          <div class="stat-label">Did Not Remember Yet</div>
        </div>
      </div>

      <!-- Add Card Form -->
      <div class="input-group">
        <label for="front-input">Front:</label>
        <input type="text" id="front-input" placeholder="Enter front text">
        <label for="back-input">Back:</label>
        <input type="text" id="back-input" placeholder="Enter back text">
        <label for="category-input">Category:</label>
        <input type="text" id="category-input" placeholder="Enter category (optional)">
        <div class="button-group">
          <button class="primary-button" id="add-card">Add Card ➕</button>
          <label for="file-input" class="secondary-button">Import from File 📂</label>
          <input type="file" id="file-input" accept=".json,.txt,.csv" class="file-input">
        </div>
      </div>

      <!-- Created Cards List -->
      <div class="card-list">
        <h3>Created Cards (0)</h3>
        <div id="cards-container">
          <!-- Cards will be dynamically inserted here -->
        </div>
        <!-- Pagination Controls -->
        <div class="pagination-info" id="pagination-info">Showing 0-0 of 0 flashcards</div>
        <div class="pagination-controls">
          <button id="prev-page" class="secondary-button">Previous</button>
          <button id="next-page" class="secondary-button">Next</button>
        </div>
      </div>
    </section>

    <!-- Practice Tab -->
    <section id="practice-tab" class="tab-content" role="tabpanel">
      <!-- Category Filter -->
      <div class="input-group">
        <label for="practice-category-filter">Filter by Category:</label>
        <select id="practice-category-filter">
          <option value="all">All Categories</option>
          <!-- Categories will be dynamically populated here -->
        </select>
      </div>
      <!-- Flashcard Practice Container -->
      <div id="practice-container">
        <!-- Flashcard -->
        <div id="flashcard" class="flashcard">
          <div class="flashcard-inner">
            <div class="flashcard-front">
              <!-- Front text will appear here -->
            </div>
            <div class="flashcard-back">
              <!-- Back text will appear here -->
            </div>
          </div>
        </div>
        <!-- Navigation -->
        <div class="navigation">
          <button class="secondary-button" id="prev-button">⬅️ Previous</button>
          <div id="card-counter">1 / 10</div>
          <button class="secondary-button" id="next-button">Next ➡️</button>
        </div>
        <!-- Answer Buttons -->
        <div class="answer-buttons">
          <button class="remember-button">Remembered ✅</button>
          <button class="did-not-remember-button">Did Not Remember Yet ❌</button>
        </div>
        <!-- Shuffle Button -->
        <div class="button-group">
          <button class="secondary-button" id="shuffle-button">Shuffle Cards 🔀</button>
        </div>
      </div>
      <!-- Empty State -->
      <div id="empty-state" class="empty-state">
        No flashcards available for practice. Create some cards first!
      </div>
    </section>

    <!-- Quiz Mode Tab -->
    <section id="quiz-tab" class="tab-content" role="tabpanel">
      <div id="quiz-container">
        <!-- Quiz Configuration Form -->
        <div id="quiz-config" class="quiz-config">
          <h2>Configure Quiz Session</h2>
          <div class="input-group">
            <label for="quiz-category-select">Select Category:</label>
            <select id="quiz-category-select" aria-label="Select Quiz Category">
              <option value="all">All Categories</option>
              <!-- Categories will be populated dynamically -->
            </select>
          </div>
          <div class="input-group">
            <label for="quiz-time-input">Set Time (minutes):</label>
            <input type="number" id="quiz-time-input" min="0" max="60" value="5" aria-label="Set Quiz Time">
            <small>Set to <strong>0</strong> for no time limit.</small>
          </div>
          <div class="input-group">
            <label for="quiz-amount-input">Number of Questions:</label>
            <input type="number" id="quiz-amount-input" min="1" max="100" value="10" aria-label="Set Number of Questions">
          </div>
          <div class="button-group">
            <button class="primary-button" id="start-quiz-session">Start Quiz Session ⏱️</button>
          </div>
        </div>

        <!-- Quiz Session Interface -->
        <div id="quiz-session" class="quiz-session" style="display: none;">
          <div class="quiz-header">
            <div id="quiz-timer" class="quiz-timer">Time Left: 05:00</div>
            <div id="quiz-progress">Question 1 of 10</div>
            <div class="progress-bar">
              <div id="progress-fill" class="progress-fill" style="width: 0%;"></div>
            </div>
          </div>
          <div id="quiz-question" class="quiz-question">Question will appear here</div>
          <div id="quiz-options" class="quiz-options">
            <!-- Options will be dynamically inserted here -->
          </div>
          <!-- Added quiz-feedback element -->
          <div id="quiz-feedback" class="quiz-feedback"></div>
          <div class="button-group">
            <button class="secondary-button" id="next-quiz-question" disabled>Next ➡️</button>
            <button class="danger-button" id="exit-quiz-session">Exit 🛑</button>
          </div>
        </div>

        <!-- Quiz Result Screen -->
        <div id="quiz-result" class="quiz-result" style="display: none;">
          <h2>Quiz Completed!</h2>
          <p id="result-score">You scored 0%</p>
          <p id="result-correct">Correct Answers: 0</p>
          <p id="result-incorrect">Incorrect Answers: 0</p>
          <p id="result-time">Total Time: 0 seconds</p>
          <button class="primary-button" id="restart-quiz-button">Restart Quiz 🔄</button>
        </div>
      </div>
      <div id="quiz-empty-state" class="empty-state">
        No flashcards available for quiz. Create some cards first!
      </div>
    </section>

    <!-- Statistics Tab -->
    <section id="stats-tab" class="tab-content" role="tabpanel">
      <div class="stats-details">
        <h2>Statistics</h2>
        <!-- Stats Overview -->
        <div class="stats-overview">
          <div class="stat-item">
            <div class="stat-value" id="total-cards-stats">0</div>
            <div class="stat-label">Total Cards</div>
          </div>
          <div class="stat-item">
            <div class="stat-value" id="total-remembered">0</div>
            <div class="stat-label">Remembered</div>
          </div>
          <div class="stat-item">
            <div class="stat-value" id="total-did-not-remember">0</div>
            <div class="stat-label">Did Not Remember Yet</div>
          </div>
        </div>
        <!-- Stats Charts -->
        <div class="stats-charts">
          <div class="stats-chart">
            <canvas id="performanceChart"></canvas>
          </div>
          <div class="stats-chart">
            <canvas id="quizPerformanceChart"></canvas>
          </div>
        </div>
        <!-- Quiz History -->
        <div class="stats-quiz-history">
          <h3>Quiz History</h3>
          <table id="quiz-history-table">
            <thead>
            <tr>
              <th>Date</th>
              <th>Category</th>
              <th>Time (min)</th>
              <th>Questions</th>
              <th>Correct</th>
              <th>Incorrect</th>
            </tr>
            </thead>
            <tbody>
            <!-- Quiz history will be dynamically populated here -->
            </tbody>
          </table>
        </div>
        <!-- Reset Statistics Button -->
        <button class="danger-button" id="reset-stats">Reset Statistics 🗑️</button>
      </div>
    </section>
  </main>
</div>

<!-- Edit Modal -->
<div id="edit-modal" class="modal">
  <div class="modal-content">
    <h2 class="modal-title">Edit Flashcard</h2>
    <input type="text" id="edit-front" placeholder="Front text">
    <input type="text" id="edit-back" placeholder="Back text">
    <input type="text" id="edit-category" placeholder="Category (optional)">
    <div class="button-group">
      <button class="secondary-button" id="cancel-edit">Cancel</button>
      <button class="primary-button" id="save-edit">Save Changes</button>
    </div>
  </div>
</div>

<!-- Notification -->
<div id="notification" class="notification"></div>

<!-- Scripts -->
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="${pageContext.request.contextPath}/js/flashcard.js" defer></script>
</body>
</html>
