
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Flashcard Practice</title>
  <link rel="stylesheet" href="/jsp/flashcard.css">
</head>
<body>
<div class="container">
  <header>
    <h1>Flashcard Practice</h1>
    <button id="theme-toggle" class="theme-toggle">🌙 Dark Mode</button>
  </header>

  <div class="tabs">
    <button class="tab active" data-tab="create">Create Cards</button>
    <button class="tab" data-tab="practice">Practice</button>
    <button class="tab" data-tab="stats">Statistics</button>
  </div>

  <!-- Create Tab -->
  <div id="create-tab" class="tab-content active">
    <div class="controls">
      <input type="text" id="search-input" placeholder="Search cards...">
      <button id="export-button" class="secondary-button">📤 Export Cards</button>
      <button id="import-button" class="secondary-button">📥 Import Cards</button>
      <input type="file" accept=".json" id="import-file" class="file-input">
    </div>

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

    <div class="input-group">
      <input type="text" id="front-input" placeholder="Front of card">
      <input type="text" id="back-input" placeholder="Back of card">
      <input type="text" id="category-input" placeholder="Category (optional)">
    </div>

    <div class="button-group">
      <button class="primary-button" id="add-card">
        <span>➕</span> Add Card
      </button>
      <label class="secondary-button" style="margin: 0">
        <span>📁</span> Import File
        <input type="file" accept=".txt,.csv" id="file-input" class="file-input">
      </label>
    </div>

    <div class="card-list" id="card-list">
      <h3>Created Cards (0)</h3>
      <div id="cards-container"></div>
    </div>
  </div>

  <!-- Practice Tab -->
  <div id="practice-tab" class="tab-content">
    <div id="practice-container">
      <div class="button-group">
        <button class="secondary-button" id="shuffle-button">
          🔀 Shuffle Cards
        </button>
        <select id="category-filter">
          <option value="all">All Categories</option>
        </select>
      </div>
      <div class="flashcard" id="flashcard">
        <div class="flashcard-inner">
          <div class="flashcard-front"></div>
          <div class="flashcard-back"></div>
        </div>
      </div>
      <div class="navigation">
        <button class="secondary-button" id="prev-button">⬅️ Previous</button>
        <span id="card-counter">0 / 0</span>
        <button class="secondary-button" id="next-button">Next ➡️</button>
      </div>
    </div>
    <div id="empty-state" class="empty-state">
      No flashcards yet. Create some cards to start practicing!
    </div>
  </div>

  <!-- Statistics Tab -->
  <div id="stats-tab" class="tab-content">
    <div class="stats-details">
      <h2>Statistics</h2>
      <div class="stats-chart">
        <canvas id="statsChart"></canvas>
      </div>
      <button class="secondary-button" id="reset-stats">Reset Statistics</button>
    </div>
  </div>
</div>

<!-- Edit Modal -->
<div class="modal" id="edit-modal">
  <div class="modal-content">
    <h2 class="modal-title">Edit Card</h2>
    <input type="text" id="edit-front" placeholder="Front of card" class="mb-3">
    <input type="text" id="edit-back" placeholder="Back of card" class="mb-3">
    <input type="text" id="edit-category" placeholder="Category (optional)" class="mb-3">
    <div class="button-group">
      <button class="secondary-button" id="cancel-edit">Cancel</button>
      <button class="primary-button" id="save-edit">Save Changes</button>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="flashcard.js" defer></script>
</body>
</html>

