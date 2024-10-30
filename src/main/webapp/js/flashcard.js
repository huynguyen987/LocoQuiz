document.addEventListener('DOMContentLoaded', () => {
    let cards = [];
    let currentCard = 0;
    let editingIndex = -1;
    let filteredCards = [];
    let categories = new Set();
    let stats = {
        totalCorrect: 0,
        totalIncorrect: 0,
        studyTime: 0,
        currentStreak: 0,
        bestStreak: 0,
        lastStudyDate: null,
        dailyGoal: 10,
        cardsStudiedToday: 0
    };

    // Study session state
    let studySessionStart = null;
    let studyTimer = null;

    // DOM Elements
    const tabButtons = document.querySelectorAll('.tab');
    const tabContents = document.querySelectorAll('.tab-content');
    const frontInput = document.getElementById('front-input');
    const backInput = document.getElementById('back-input');
    const categoryInput = document.getElementById('category-input');
    const addButton = document.getElementById('add-card');
    const fileInput = document.getElementById('file-input');
    const importFileInput = document.getElementById('import-file');
    const cardsContainer = document.getElementById('cards-container');
    const flashcard = document.getElementById('flashcard');
    const flashcardFront = document.querySelector('.flashcard-front');
    const flashcardBack = document.querySelector('.flashcard-back');
    const prevButton = document.getElementById('prev-button');
    const nextButton = document.getElementById('next-button');
    const cardCounter = document.getElementById('card-counter');
    const practiceContainer = document.getElementById('practice-container');
    const emptyState = document.getElementById('empty-state');
    const shuffleButton = document.getElementById('shuffle-button');
    const editModal = document.getElementById('edit-modal');
    const editFrontInput = document.getElementById('edit-front');
    const editBackInput = document.getElementById('edit-back');
    const editCategoryInput = document.getElementById('edit-category');
    const cancelEditButton = document.getElementById('cancel-edit');
    const saveEditButton = document.getElementById('save-edit');
    const totalCardsElement = document.getElementById('total-cards');
    const lastAddedElement = document.getElementById('last-added');
    const totalCorrectElement = document.getElementById('total-correct');
    const totalIncorrectElement = document.getElementById('total-incorrect');
    const themeToggle = document.getElementById('theme-toggle');
    const searchInput = document.getElementById('search-input');
    const exportButton = document.getElementById('export-button');
    const importButton = document.getElementById('import-button');
    const categoryFilter = document.getElementById('category-filter');
    const resetStatsButton = document.getElementById('reset-stats');
    const statsChartCtx = document.getElementById('statsChart').getContext('2d');
    const correctButton = document.querySelector('.correct-button');
    const incorrectButton = document.querySelector('.incorrect-button');

    // Load Data from localStorage
    function loadData() {
        const savedCards = localStorage.getItem('flashcards');
        const savedStats = localStorage.getItem('flashcard-stats');
        const savedTheme = localStorage.getItem('theme');

        if (savedCards) {
            cards = JSON.parse(savedCards);
            filteredCards = [...cards];
            extractCategories();
            updateCardsList();
            populateCategoryFilter();
        }

        if (savedStats) {
            stats = JSON.parse(savedStats);
            updateStatsElements();
        }

        if (savedTheme === 'dark') {
            document.body.classList.add('dark-mode');
            themeToggle.textContent = '☀️';
        } else {
            themeToggle.textContent = '🌙';
        }
    }

    // Save Data to localStorage
    function saveData() {
        localStorage.setItem('flashcards', JSON.stringify(cards));
        localStorage.setItem('flashcard-stats', JSON.stringify(stats));
    }

    // Extract Categories from Cards
    function extractCategories() {
        categories.clear();
        cards.forEach(card => {
            if (card.category) {
                categories.add(card.category);
            }
        });
    }

    // Populate Category Filter Dropdown
    function populateCategoryFilter() {
        categoryFilter.innerHTML = '<option value="all">All Categories</option>';
        categories.forEach(cat => {
            const option = document.createElement('option');
            option.value = cat;
            option.textContent = cat;
            categoryFilter.appendChild(option);
        });
    }

    // Tab Switching
    tabButtons.forEach(button => {
        button.addEventListener('click', () => {
            const tabName = button.dataset.tab;

            tabButtons.forEach(btn => btn.classList.remove('active'));
            tabContents.forEach(content => content.classList.remove('active'));

            button.classList.add('active');
            document.getElementById(`${tabName}-tab`).classList.add('active');

            if (tabName === 'stats') {
                renderStatsChart();
            }
        });
    });

    // Add Card
    addButton.addEventListener('click', () => {
        const front = frontInput.value.trim();
        const back = backInput.value.trim();
        const category = categoryInput.value.trim();

        if (front && back) {
            const now = new Date();
            const newCard = { front, back, category, lastReviewed: null, interval: 1, nextReview: null };
            cards.push(newCard);
            filteredCards = [...cards];
            extractCategories();
            updateCardsList();
            populateCategoryFilter();
            saveData();
            frontInput.value = '';
            backInput.value = '';
            categoryInput.value = '';

            lastAddedElement.textContent = `${now.getHours()}:${String(now.getMinutes()).padStart(2, '0')}`;
        } else {
            alert('Please enter both front and back of the card.');
        }
    });

    // File Import (TXT/CSV)
    fileInput.addEventListener('change', (e) => {
        const file = e.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = (e) => {
                const text = e.target.result;
                const lines = text.split('\n');
                const newCards = lines.map(line => {
                    const [front, back, category] = line.split(',').map(str => str.trim());
                    return { front, back, category: category || '' };
                }).filter(card => card.front && card.back);

                if (newCards.length > 0) {
                    cards = [...cards, ...newCards];
                    filteredCards = [...cards];
                    extractCategories();
                    updateCardsList();
                    populateCategoryFilter();
                    saveData();
                } else {
                    alert('No valid cards found in the file.');
                }
            };
            reader.readAsText(file);
        }
    });

    // Flashcard Flip
    flashcard.addEventListener('click', () => {
        flashcard.classList.toggle('flipped');
    });

    // Navigation
    prevButton.addEventListener('click', () => {
        if (filteredCards.length === 0) return;
        flashcard.classList.remove('flipped');
        currentCard = (currentCard - 1 + filteredCards.length) % filteredCards.length;
        updateCardDisplay();
    });

    nextButton.addEventListener('click', () => {
        if (filteredCards.length === 0) return;
        flashcard.classList.remove('flipped');
        currentCard = (currentCard + 1) % filteredCards.length;
        updateCardDisplay();
    });

    // Shuffle Cards
    shuffleButton.addEventListener('click', () => {
        if (filteredCards.length < 2) return;
        for (let i = filteredCards.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [filteredCards[i], filteredCards[j]] = [filteredCards[j], filteredCards[i]];
        }
        currentCard = 0;
        updateCardDisplay();
    });

    // Edit Card Modal
    cancelEditButton.addEventListener('click', closeEditModal);

    saveEditButton.addEventListener('click', () => {
        const front = editFrontInput.value.trim();
        const back = editBackInput.value.trim();
        const category = editCategoryInput.value.trim();

        if (front && back) {
            cards[editingIndex] = {
                front,
                back,
                category,
                lastReviewed: cards[editingIndex].lastReviewed,
                interval: cards[editingIndex].interval,
                nextReview: cards[editingIndex].nextReview
            };
            filteredCards = [...cards];
            extractCategories();
            updateCardsList();
            populateCategoryFilter();
            saveData();
            closeEditModal();
        } else {
            alert('Please enter both front and back of the card.');
        }
    });

    // Open Edit Modal (Delegated)
    window.openEditModal = function(index) {
        editingIndex = index;
        const card = filteredCards[index];
        editFrontInput.value = card.front;
        editBackInput.value = card.back;
        editCategoryInput.value = card.category;
        editModal.classList.add('active');
    }

    // Close Edit Modal
    function closeEditModal() {
        editModal.classList.remove('active');
        editingIndex = -1;
    }

    // Click outside modal to close
    window.addEventListener('click', (e) => {
        if (e.target === editModal) {
            closeEditModal();
        }
    });

    // Delete Card
    function deleteCard(index) {
        if (confirm('Are you sure you want to delete this card?')) {
            const actualIndex = cards.indexOf(filteredCards[index]);
            if (actualIndex > -1) {
                cards.splice(actualIndex, 1);
                filteredCards.splice(index, 1);
                extractCategories();
                updateCardsList();
                populateCategoryFilter();
                saveData();
            }
        }
    }

    // Update Cards List in UI
    function updateCardsList() {
        cardsContainer.innerHTML = '';

        filteredCards.forEach((card, index) => {
            const cardElement = document.createElement('div');
            cardElement.className = 'card-item';

            const cardContent = document.createElement('div');
            cardContent.className = 'card-content';
            cardContent.innerHTML = `
                <strong>Front:</strong> ${escapeHTML(card.front)}<br>
                <strong>Back:</strong> ${escapeHTML(card.back)}<br>
                <strong>Category:</strong> ${escapeHTML(card.category || 'None')}
            `;

            const cardActions = document.createElement('div');
            cardActions.className = 'card-actions';

            const editButton = document.createElement('button');
            editButton.className = 'action-button secondary-button';
            editButton.innerHTML = '✏️';
            editButton.addEventListener('click', () => openEditModal(index));

            const deleteButton = document.createElement('button');
            deleteButton.className = 'action-button danger-button';
            deleteButton.innerHTML = '🗑️';
            deleteButton.addEventListener('click', () => deleteCard(index));

            cardActions.appendChild(editButton);
            cardActions.appendChild(deleteButton);

            cardElement.appendChild(cardContent);
            cardElement.appendChild(cardActions);

            cardsContainer.appendChild(cardElement);
        });

        document.querySelector('.card-list h3').textContent = `Created Cards (${filteredCards.length})`;
        updatePracticeView();
        updateStatsElements();
    }

    // Update Card Display in Practice Mode
    function updateCardDisplay() {
        if (filteredCards.length === 0) {
            flashcardFront.textContent = 'No cards available';
            flashcardBack.textContent = 'Create some cards first';
            cardCounter.textContent = '0 / 0';
            return;
        }

        const card = filteredCards[currentCard];
        flashcardFront.textContent = card.front;
        flashcardBack.textContent = card.back;
        cardCounter.textContent = `${currentCard + 1} / ${filteredCards.length}`;
    }

    // Update Practice View
    function updatePracticeView() {
        if (filteredCards.length === 0) {
            practiceContainer.style.display = 'none';
            emptyState.style.display = 'block';
        } else {
            practiceContainer.style.display = 'block';
            emptyState.style.display = 'none';
            updateCardDisplay();
        }
    }

    // Update Statistics Elements
    function updateStatsElements() {
        totalCardsElement.textContent = cards.length;
        totalCorrectElement.textContent = stats.totalCorrect;
        totalIncorrectElement.textContent = stats.totalIncorrect;
    }

    // Theme Toggle
    themeToggle.addEventListener('click', () => {
        document.body.classList.toggle('dark-mode');
        if (document.body.classList.contains('dark-mode')) {
            themeToggle.textContent = '☀️';
            localStorage.setItem('theme', 'dark');
        } else {
            themeToggle.textContent = '🌙';
            localStorage.setItem('theme', 'light');
        }
    });

    // Search Functionality
    searchInput.addEventListener('input', () => {
        const query = searchInput.value.toLowerCase();
        filteredCards = cards.filter(card =>
            card.front.toLowerCase().includes(query) ||
            card.back.toLowerCase().includes(query) ||
            (card.category && card.category.toLowerCase().includes(query))
        );
        currentCard = 0;
        updateCardsList();
        populateCategoryFilter();
    });

    // Export Flashcards as JSON
    exportButton.addEventListener('click', () => {
        const dataStr = JSON.stringify(cards, null, 2);
        const blob = new Blob([dataStr], { type: 'application/json' });
        const url = URL.createObjectURL(blob);

        const a = document.createElement('a');
        a.href = url;
        a.download = 'flashcards.json';
        a.click();

        URL.revokeObjectURL(url);
    });

    // Import Flashcards from JSON
    importButton.addEventListener('click', () => {
        importFileInput.click();
    });

    importFileInput.addEventListener('change', (e) => {
        const file = e.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = (e) => {
                try {
                    const importedCards = JSON.parse(e.target.result);
                    if (Array.isArray(importedCards)) {
                        cards = [...cards, ...importedCards];
                        filteredCards = [...cards];
                        extractCategories();
                        updateCardsList();
                        populateCategoryFilter();
                        saveData();
                    } else {
                        alert('Invalid file format.');
                    }
                } catch (error) {
                    alert('Error reading the file.');
                }
            };
            reader.readAsText(file);
        }
    });

    // Reset Statistics
    resetStatsButton.addEventListener('click', () => {
        if (confirm('Are you sure you want to reset all statistics?')) {
            stats = { totalCorrect: 0, totalIncorrect: 0 };
            saveData();
            updateStatsElements();
            renderStatsChart();
        }
    });

    // Correct Answer
    correctButton.addEventListener('click', () => {
        if (filteredCards.length === 0) return;

        stats.totalCorrect += 1;
        stats.currentStreak += 1;
        if (stats.currentStreak > stats.bestStreak) {
            stats.bestStreak = stats.currentStreak;
        }
        stats.cardsStudiedToday += 1;

        // Update card's spaced repetition interval
        filteredCards[currentCard].interval *= 2;
        const nextReviewDate = new Date();
        nextReviewDate.setDate(nextReviewDate.getDate() + filteredCards[currentCard].interval);
        filteredCards[currentCard].nextReview = nextReviewDate.toISOString();
        filteredCards[currentCard].lastReviewed = new Date().toISOString();

        saveData();
        updateStatsElements();
        renderStatsChart();
        moveToNextCard();
    });

    // Incorrect Answer
    incorrectButton.addEventListener('click', () => {
        if (filteredCards.length === 0) return;

        stats.totalIncorrect += 1;
        stats.currentStreak = 0;
        stats.cardsStudiedToday += 1;

        // Reset card's spaced repetition interval
        filteredCards[currentCard].interval = 1;
        const nextReviewDate = new Date();
        nextReviewDate.setDate(nextReviewDate.getDate() + filteredCards[currentCard].interval);
        filteredCards[currentCard].nextReview = nextReviewDate.toISOString();
        filteredCards[currentCard].lastReviewed = new Date().toISOString();

        saveData();
        updateStatsElements();
        renderStatsChart();
        moveToNextCard();
    });

    // Move to Next Card after answering
    function moveToNextCard() {
        flashcard.classList.remove('flipped');
        currentCard = (currentCard + 1) % filteredCards.length;
        updateCardDisplay();
    }

    // Flashcard Answer Tracking (Deprecated: Removed Double-click Prompt)
    // Removed the double-click prompt in favor of dedicated buttons

    // Spaced Repetition Filtering
    function applySpacedRepetition() {
        const today = new Date();
        filteredCards = cards.filter(card => {
            if (!card.nextReview) return true;
            const nextReview = new Date(card.nextReview);
            return nextReview <= today;
        });
        updateCardsList();
        populateCategoryFilter();
    }

    // Category Filter
    categoryFilter.addEventListener('change', () => {
        const selectedCategory = categoryFilter.value;
        if (selectedCategory === 'all') {
            filteredCards = [...cards];
        } else {
            filteredCards = cards.filter(card => card.category === selectedCategory);
        }
        currentCard = 0;
        updateCardsList();
    });

    // Statistics Chart using Chart.js
    let statsChart;
    function renderStatsChart() {
        if (statsChart) {
            statsChart.destroy();
        }

        statsChart = new Chart(statsChartCtx, {
            type: 'pie',
            data: {
                labels: ['Correct', 'Incorrect'],
                datasets: [{
                    data: [stats.totalCorrect, stats.totalIncorrect],
                    backgroundColor: [
                        '#48bb78', // Green for Correct
                        '#f56565'  // Red for Incorrect
                    ],
                    hoverOffset: 4
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'bottom',
                    },
                    title: {
                        display: true,
                        text: 'Flashcard Performance'
                    }
                }
            },
        });
    }

    // Escape HTML to prevent XSS
    function escapeHTML(str) {
        return str.replace(/[&<>'"]/g, (tag) => {
            const charsToReplace = {
                '&': '&amp;',
                '<': '&lt;',
                '>': '&gt;',
                "'": '&#39;',
                '"': '&quot;'
            };
            return charsToReplace[tag] || tag;
        });
    }

    // Initialize Application
    function init() {
        loadData();
        applySpacedRepetition();
        renderStatsChart();
    }

    // Initialize
    init();
});
