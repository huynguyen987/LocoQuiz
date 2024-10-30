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

  <!-- Navigation Tabs -->
  <nav class="tabs" role="tablist">
    <button class="tab active" data-tab="create" role="tab" aria-selected="true">Create Cards</button>
    <button class="tab" data-tab="practice" role="tab">Practice</button>
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
          <div class="stat-label">Correct Answers</div>
        </div>
        <div class="stat-item">
          <div class="stat-value" id="total-incorrect">0</div>
          <div class="stat-label">Incorrect Answers</div>
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
          <select id="category-filter" aria-label="Filter by Category">
            <option value="all">All Categories</option>
          </select>
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
        <!-- New Answer Buttons -->
        <div class="answer-buttons">
          <button class="correct-button">Correct ✅</button>
          <button class="incorrect-button">Incorrect ❌</button>
        </div>
      </div>
      <div id="empty-state" class="empty-state">
        No flashcards yet. Create some cards to start practicing!
      </div>
    </section>

    <!-- Statistics Tab -->
    <section id="stats-tab" class="tab-content" role="tabpanel">
      <div class="stats-details">
        <h2>Statistics</h2>
        <div class="stats-chart">
          <canvas id="statsChart"></canvas>
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

<!-- Scripts -->
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="${pageContext.request.contextPath}/js/flashcard.js" defer></script>
</body>
</html>
