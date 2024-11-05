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
        <input type="text" id="search-input" placeholder="Search cards..." aria-label="Search Cards">
        <div class="control-buttons">
          <button id="export-button" class="secondary-button">Export 📤</button>
          <button id="import-button" class="secondary-button">Import 📥</button>
          <input type="file" accept=".json" id="import-file" class="file-input" aria-hidden="true">
        </div>
      </div>

      <!-- Stats Summary -->
      <div class="stats-summary">
        <div class="stat-item">
          <div class="stat-value" id="total-cards">0</div>
          <div class="stat-label">Total Cards</div>
        </div>
        <div class="stat-item">
          <div class="stat-value" id="last-added">-</div>
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

      <!-- Input Group -->
      <div class="input-group">
        <input type="text" id="front-input" placeholder="Front of card" aria-label="Front of Card">
        <input type="text" id="back-input" placeholder="Back of card" aria-label="Back of Card">
        <input type="text" id="category-input" placeholder="Category (optional)" aria-label="Category">
      </div>

      <!-- Button Group -->
      <div class="button-group">
        <button class="primary-button" id="add-card">
          Add Card ➕
        </button>
        <label class="secondary-button">
          Import File 📁
          <input type="file" accept=".txt,.csv" id="file-input" class="file-input" aria-hidden="true">
        </label>
        <button class="danger-button" id="delete-all-cards">
          Delete All Cards 🗑️
        </button>
        <button class="secondary-button" id="refresh-cards">
          Refresh List 🔄
        </button>
      </div>

      <!-- Additional Information Above Flashcard List -->
      <div class="pagination-info" id="pagination-info">Showing 1-10 of 0 flashcards</div>
      <div class="pagination-controls">
        <button id="prev-page" class="secondary-button">Previous</button>
        <button id="next-page" class="secondary-button">Next</button>
      </div>

      <!-- Card List -->
      <div class="card-list" id="card-list">
        <h3>Created Cards (0)</h3>
        <div id="cards-container"></div>
      </div>
    </section>

    <!-- Practice Tab -->
    <section id="practice-tab" class="tab-content" role="tabpanel">
      <div id="practice-container">
        <div class="button-group">
          <button class="secondary-button" id="shuffle-button">Shuffle 🔀</button>
          <div class="dropdown">
            <label for="practice-category-filter" class="dropdown-label">Filter by Category:</label>
            <select id="practice-category-filter" aria-label="Filter by Category">
              <option value="all">All Categories</option>
              <!-- Categories will be populated dynamically -->
            </select>
          </div>
        </div>
        <div class="flashcard" id="flashcard" tabindex="0" aria-label="Flashcard">
          <div class="flashcard-inner">
            <div class="flashcard-front">Front</div>
            <div class="flashcard-back">Back</div>
          </div>
        </div>
        <div class="navigation">
          <button class="secondary-button" id="prev-button">⬅️ Previous</button>
          <span id="card-counter">0 / 0</span>
          <button class="secondary-button" id="next-button">Next ➡️</button>
        </div>
        <!-- Updated Answer Buttons -->
        <div class="answer-buttons">
          <button class="remember-button">Remember ✅</button>
          <button class="did-not-remember-button">Did Not Remember Yet ❌</button>
        </div>
      </div>
      <div id="empty-state" class="empty-state">
        No flashcards yet. Create some cards to start practicing!
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
          <div id="quiz-timer" class="quiz-timer">Time Left: 05:00</div>
          <div id="quiz-question" class="quiz-question">Question will appear here</div>
          <div id="quiz-options" class="quiz-options">
            <!-- Options will be dynamically inserted here -->
          </div>
          <div id="quiz-feedback" class="quiz-feedback"></div>
          <div class="button-group">
            <button class="secondary-button" id="next-quiz-question" disabled>Next ➡️</button>
            <button class="danger-button" id="exit-quiz-session">Exit 🛑</button>
          </div>
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
        <div class="stats-charts">
          <div class="stats-chart">
            <canvas id="performanceChart"></canvas>
          </div>
          <div class="stats-chart">
            <canvas id="quizPerformanceChart"></canvas>
          </div>
        </div>
        <div class="stats-quiz-history">
          <h3>Quiz Session History</h3>
          <table id="quiz-history-table">
            <thead>
            <tr>
              <th>Date</th>
              <th>Category</th>
              <th>Time (mins)</th>
              <th>Questions</th>
              <th>Correct</th>
              <th>Incorrect</th>
            </tr>
            </thead>
            <tbody>
            <!-- Quiz sessions will be populated dynamically -->
            </tbody>
          </table>
        </div>
        <button class="secondary-button" id="reset-stats">Reset Statistics</button>
      </div>
    </section>
  </main>
</div>

<!-- Edit Modal -->
<div class="modal" id="edit-modal" role="dialog" aria-modal="true" aria-labelledby="edit-modal-title">
  <div class="modal-content">
    <h2 id="edit-modal-title">Edit Card</h2>
    <input type="text" id="edit-front" placeholder="Front of card" aria-label="Edit Front of Card" class="mb-3">
    <input type="text" id="edit-back" placeholder="Back of card" aria-label="Edit Back of Card" class="mb-3">
    <input type="text" id="edit-category" placeholder="Category (optional)" aria-label="Edit Category" class="mb-3">
    <div class="button-group">
      <button class="secondary-button" id="cancel-edit">Cancel</button>
      <button class="primary-button" id="save-edit">Save Changes</button>
    </div>
  </div>
</div>

<!-- Notification -->
<div id="notification" class="notification" style="display: none;"></div>

<!-- Scripts -->
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="${pageContext.request.contextPath}/js/flashcard.js" defer></script>
</body>
</html>
