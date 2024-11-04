document.addEventListener('DOMContentLoaded', () => {
    let cards = [];
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
        cardsStudiedToday: 0,
        quizSessions: [] // Array to store quiz session results
    };

    // Pagination Variables
    const cardsPerPage = 10;
    let currentPage = 1;
    let totalPages = 1;

    // Practice Mode Variables
    let currentCardIndex = 0;

    // Quiz Session Variables
    let quizSessionActive = false;
    let quizSessionTimer = null;
    let quizSessionDuration = 5 * 60; // Default 5 minutes in seconds
    let quizSessionTimeLeft = quizSessionDuration;
    let quizSessionCorrect = 0;
    let quizSessionIncorrect = 0;
    let selectedQuizCategory = 'all';
    let quizTotalQuestions = 10;
    let quizQuestionsAsked = 0;
    let quizQuestions = [];
    let currentQuizQuestion = '';
    let quizCorrectAnswer = '';

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
    const categoryFilter = document.getElementById('practice-category-filter');
    const resetStatsButton = document.getElementById('reset-stats');
    const performanceChartCtx = document.getElementById('performanceChart').getContext('2d');
    const quizPerformanceChartCtx = document.getElementById('quizPerformanceChart').getContext('2d');
    const rememberButton = document.querySelector('.remember-button');
    const didNotRememberButton = document.querySelector('.did-not-remember-button');
    const quizTab = document.getElementById('quiz-tab');
    const quizContainer = document.getElementById('quiz-container');
    const quizConfig = document.getElementById('quiz-config');
    const quizSessionDiv = document.getElementById('quiz-session');
    const quizQuestion = document.getElementById('quiz-question');
    const quizOptionsContainer = document.getElementById('quiz-options');
    const quizFeedback = document.getElementById('quiz-feedback');
    const nextQuizButton = document.getElementById('next-quiz-question');
    const exitQuizButton = document.getElementById('exit-quiz-session');
    const quizEmptyState = document.getElementById('quiz-empty-state');
    const quizCategoryFilter = document.getElementById('quiz-category-select');
    const quizTimeInput = document.getElementById('quiz-time-input');
    const quizAmountInput = document.getElementById('quiz-amount-input');
    const startQuizButton = document.getElementById('start-quiz-session');
    const deleteAllCardsButton = document.getElementById('delete-all-cards');
    const refreshCardsButton = document.getElementById('refresh-cards');
    const paginationInfo = document.getElementById('pagination-info');
    const prevPageButton = document.getElementById('prev-page');
    const nextPageButton = document.getElementById('next-page');
    const notificationDiv = document.getElementById('notification');
    const quizHistoryTableBody = document.querySelector('#quiz-history-table tbody');

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
            populateQuizCategoryFilter();
        }

        if (savedStats) {
            stats = JSON.parse(savedStats);
            updateStatsElements();
            populateQuizHistoryTable();
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

    // Populate Category Filter Dropdown in Practice Mode
    function populateCategoryFilter() {
        categoryFilter.innerHTML = '<option value="all">All Categories</option>';
        categories.forEach(cat => {
            const option1 = document.createElement('option');
            option1.value = cat;
            option1.textContent = cat;
            categoryFilter.appendChild(option1);
        });
    }

    // Populate Category Filter Dropdown in Quiz Mode
    function populateQuizCategoryFilter() {
        quizCategoryFilter.innerHTML = '<option value="all">All Categories</option>';
        categories.forEach(cat => {
            const option = document.createElement('option');
            option.value = cat;
            option.textContent = cat;
            quizCategoryFilter.appendChild(option);
        });
    }

    // Define disableNavigationTabs Function
    function disableNavigationTabs(disable) {
        tabButtons.forEach(button => {
            if (disable) {
                button.disabled = true;
                button.classList.add('disabled-tab');
            } else {
                button.disabled = false;
                button.classList.remove('disabled-tab');
            }
        });
    }

    // Tab Switching
    tabButtons.forEach(button => {
        button.addEventListener('click', () => {
            if (quizSessionActive) {
                showNotification('Cannot switch tabs during an active quiz session.', 'error');
                return;
            }

            const tabName = button.dataset.tab;

            tabButtons.forEach(btn => btn.classList.remove('active'));
            tabContents.forEach(content => content.classList.remove('active'));

            button.classList.add('active');
            document.getElementById(`${tabName}-tab`).classList.add('active');

            if (tabName === 'stats') {
                renderStatsCharts();
            }

            if (tabName === 'quiz') {
                enterQuizMode();
            }
        });
    });

    // Update Statistics Elements
    function updateStatsElements() {
        totalCardsElement.textContent = cards.length;
        totalCorrectElement.textContent = stats.totalCorrect;
        totalIncorrectElement.textContent = stats.totalIncorrect;

        // Update stats in the Statistics tab
        document.getElementById('total-cards-stats').textContent = cards.length;
        document.getElementById('total-remembered').textContent = stats.totalCorrect;
        document.getElementById('total-did-not-remember').textContent = stats.totalIncorrect;
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

    // Search Functionality with Debounce
    function debounce(func, delay) {
        let timeout;
        return function(...args) {
            clearTimeout(timeout);
            timeout = setTimeout(() => func.apply(this, args), delay);
        };
    }

    searchInput.addEventListener('input', debounce(() => {
        const query = searchInput.value.toLowerCase();
        filteredCards = cards.filter(card =>
            card.front.toLowerCase().includes(query) ||
            card.back.toLowerCase().includes(query) ||
            (card.category && card.category.toLowerCase().includes(query))
        );
        currentPage = 1;
        updateCardsList();
        populateCategoryFilter();
    }, 300));

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
                        populateQuizCategoryFilter();
                        saveData();
                        updatePagination();
                        updateStatsElements();
                        populateQuizHistoryTable();
                        showNotification('Flashcards imported successfully.', 'success');
                    } else {
                        showNotification('Invalid file format.', 'error');
                    }
                } catch (error) {
                    showNotification('Error reading the file.', 'error');
                }
            };
            reader.readAsText(file);
        }
    });

    // Reset Statistics
    resetStatsButton.addEventListener('click', () => {
        if (confirm('Are you sure you want to reset all statistics?')) {
            stats = {
                totalCorrect: 0,
                totalIncorrect: 0,
                studyTime: 0,
                currentStreak: 0,
                bestStreak: 0,
                lastStudyDate: null,
                dailyGoal: 10,
                cardsStudiedToday: 0,
                quizSessions: []
            };
            saveData();
            updateStatsElements();
            renderStatsCharts();
            populateQuizHistoryTable();
            showNotification('Statistics have been reset.', 'success');
        }
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
            populateQuizCategoryFilter();
            saveData();
            updatePagination();
            frontInput.value = '';
            backInput.value = '';
            categoryInput.value = '';

            lastAddedElement.textContent = `${now.getHours()}:${String(now.getMinutes()).padStart(2, '0')}`;
            updateStatsElements();
            showNotification('New flashcard added successfully.', 'success');
        } else {
            showNotification('Please enter both front and back of the card.', 'error');
        }
    });

    // File Import (TXT/CSV)
    fileInput.addEventListener('change', (e) => {
        const file = e.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = (e) => {
                const text = e.target.result;
                let newCards = [];
                if (file.type === 'application/json' || file.name.endsWith('.json')) {
                    try {
                        newCards = JSON.parse(text);
                        if (!Array.isArray(newCards)) throw new Error('Invalid JSON format.');
                    } catch (error) {
                        showNotification('Invalid JSON file.', 'error');
                        return;
                    }
                } else if (file.type === 'text/csv' || file.type === 'text/plain' || file.name.endsWith('.csv') || file.name.endsWith('.txt')) {
                    const lines = text.split('\n');
                    // Remove header if present
                    const startIndex = lines[0].toLowerCase().includes('front') ? 1 : 0;
                    newCards = lines.slice(startIndex).map(line => {
                        const parts = parseCSVLine(line);
                        const front = parts[0] ? parts[0].trim() : '';
                        const back = parts[1] ? parts[1].trim() : '';
                        const category = parts[2] ? parts[2].trim() : '';
                        return { front, back, category };
                    }).filter(card => card.front && card.back);
                }

                if (newCards.length > 0) {
                    cards = [...cards, ...newCards];
                    filteredCards = [...cards];
                    extractCategories();
                    updateCardsList();
                    populateCategoryFilter();
                    populateQuizCategoryFilter();
                    saveData();
                    updatePagination();
                    updateStatsElements();
                    populateQuizHistoryTable();
                    showNotification(`${newCards.length} flashcards imported successfully.`, 'success');
                } else {
                    showNotification('No valid cards found in the file.', 'error');
                }
            };
            reader.readAsText(file);
        }
    });

    // Helper function to parse CSV lines considering quoted commas
    function parseCSVLine(line) {
        const regex = /(".*?"|[^",\s]+)(?=\s*,|\s*$)/g;
        const matches = line.match(regex);
        return matches ? matches.map(m => m.replace(/^"|"$/g, '')) : [];
    }

    // Flashcard Flip
    flashcard.addEventListener('click', () => {
        flashcard.classList.toggle('flipped');
    });

    // Navigation
    prevButton.addEventListener('click', () => {
        if (filteredCards.length === 0) return;
        flashcard.classList.remove('flipped');
        currentCardIndex = (currentCardIndex - 1 + filteredCards.length) % filteredCards.length;
        updateCardDisplay();
    });

    nextButton.addEventListener('click', () => {
        if (filteredCards.length === 0) return;
        flashcard.classList.remove('flipped');
        currentCardIndex = (currentCardIndex + 1) % filteredCards.length;
        updateCardDisplay();
    });

    // Shuffle Cards
    shuffleButton.addEventListener('click', () => {
        if (filteredCards.length < 2) return;
        for (let i = filteredCards.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [filteredCards[i], filteredCards[j]] = [filteredCards[j], filteredCards[i]];
        }
        currentCardIndex = 0;
        updateCardDisplay();
        showNotification('Flashcards shuffled.', 'success');
    });

    // Edit Card Modal
    let editingIndex = -1;

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
            populateQuizCategoryFilter();
            saveData();
            closeEditModal();
            updatePagination();
            updateStatsElements();
            showNotification('Flashcard updated successfully.', 'success');
        } else {
            showNotification('Please enter both front and back of the card.', 'error');
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
        editFrontInput.focus();
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
                populateQuizCategoryFilter();
                saveData();
                updatePagination();
                updateStatsElements();
                showNotification('Flashcard deleted successfully.', 'success');
            }
        }
    }

    // Delete All Cards
    deleteAllCardsButton.addEventListener('click', () => {
        if (confirm('Are you sure you want to delete all flashcards? This action cannot be undone.')) {
            cards = [];
            filteredCards = [];
            extractCategories();
            updateCardsList();
            populateCategoryFilter();
            populateQuizCategoryFilter();
            saveData();
            updatePagination();
            updateStatsElements();
            populateQuizHistoryTable();
            showNotification('All flashcards have been deleted.', 'success');
        }
    });

    // Refresh Flashcard List
    refreshCardsButton.addEventListener('click', () => {
        filteredCards = [...cards];
        currentPage = 1;
        updateCardsList();
        populateCategoryFilter();
        populateQuizCategoryFilter();
        saveData();
        updatePagination();
        updateStatsElements();
        showNotification('Flashcard list has been refreshed.', 'success');
    });

    // Initial call to set the counter on page load
    updateStatsElements();

    // Update Cards List in UI with Pagination
    function updateCardsList() {
        cardsContainer.innerHTML = '';

        // Calculate total pages
        totalPages = Math.ceil(filteredCards.length / cardsPerPage);
        if (totalPages === 0) totalPages = 1;

        // Get cards for the current page
        const startIndex = (currentPage - 1) * cardsPerPage;
        const endIndex = Math.min(startIndex + cardsPerPage, filteredCards.length);
        const cardsToDisplay = filteredCards.slice(startIndex, endIndex);

        cardsToDisplay.forEach((card, index) => {
            const actualIndex = startIndex + index;
            const cardElement = document.createElement('div');
            cardElement.className = 'card-item';
            cardElement.dataset.index = actualIndex; // Add data-index for reference

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
            editButton.className = 'action-button secondary-button edit-button';
            editButton.innerHTML = '✏️';
            editButton.addEventListener('click', () => openEditModal(actualIndex));

            const deleteButton = document.createElement('button');
            deleteButton.className = 'action-button danger-button delete-button';
            deleteButton.innerHTML = '🗑️';
            deleteButton.addEventListener('click', () => deleteCard(actualIndex));

            cardActions.appendChild(editButton);
            cardActions.appendChild(deleteButton);

            cardElement.appendChild(cardContent);
            cardElement.appendChild(cardActions);

            cardsContainer.appendChild(cardElement);
        });

        document.querySelector('.card-list h3').textContent = `Created Cards (${cards.length})`;
        updatePaginationInfo();
        updatePracticeView();
        updateQuizEmptyState();
        updateStatsElements();
    }

    // Pagination Controls
    prevPageButton.addEventListener('click', () => {
        if (currentPage > 1) {
            currentPage--;
            updateCardsList();
        }
    });

    nextPageButton.addEventListener('click', () => {
        if (currentPage < totalPages) {
            currentPage++;
            updateCardsList();
        }
    });

    function updatePagination() {
        if (currentPage > totalPages) {
            currentPage = totalPages;
        }
        updateCardsList();
    }

    function updatePaginationInfo() {
        const start = (currentPage - 1) * cardsPerPage + 1;
        const end = Math.min(currentPage * cardsPerPage, filteredCards.length);
        paginationInfo.textContent = `Showing ${start}-${end} of ${filteredCards.length} flashcards`;
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

    // Update Quiz Empty State
    function updateQuizEmptyState() {
        if (cards.length === 0) {
            quizContainer.style.display = 'none';
            quizEmptyState.style.display = 'block';
        } else {
            quizContainer.style.display = 'block';
            quizEmptyState.style.display = 'none';
        }
    }

    // Update Card Display in Practice Mode
    function updateCardDisplay() {
        if (filteredCards.length === 0) {
            flashcardFront.textContent = 'No cards available';
            flashcardBack.textContent = 'Create some cards first';
            cardCounter.textContent = '0 / 0';
            return;
        }

        const card = filteredCards[currentCardIndex];
        flashcardFront.textContent = card.front;
        flashcardBack.textContent = card.back;
        cardCounter.textContent = `${currentCardIndex + 1} / ${filteredCards.length}`;
    }

    // Remember Button in Practice Mode
    rememberButton.addEventListener('click', () => {
        if (filteredCards.length === 0) return;

        if (quizSessionActive) {
            // In Quiz Mode - Do nothing or disable in practice mode
            return;
        }

        stats.totalCorrect += 1;
        stats.currentStreak += 1;
        if (stats.currentStreak > stats.bestStreak) {
            stats.bestStreak = stats.currentStreak;
        }
        stats.cardsStudiedToday += 1;

        // Update card's spaced repetition interval
        filteredCards[currentCardIndex].interval *= 2;
        const nextReviewDate = new Date();
        nextReviewDate.setDate(nextReviewDate.getDate() + filteredCards[currentCardIndex].interval);
        filteredCards[currentCardIndex].nextReview = nextReviewDate.toISOString();
        filteredCards[currentCardIndex].lastReviewed = new Date().toISOString();

        saveData();
        updateStatsElements();
        renderStatsCharts();
        showNotification('Great! You remembered correctly.', 'success');
        moveToNextCard();
    });

    // Did Not Remember Yet Button in Practice Mode
    didNotRememberButton.addEventListener('click', () => {
        if (filteredCards.length === 0) return;

        if (quizSessionActive) {
            // In Quiz Mode - Do nothing or disable in practice mode
            return;
        }

        stats.totalIncorrect += 1;
        stats.currentStreak = 0;
        stats.cardsStudiedToday += 1;

        // Reset card's spaced repetition interval
        filteredCards[currentCardIndex].interval = 1;
        const nextReviewDate = new Date();
        nextReviewDate.setDate(nextReviewDate.getDate() + filteredCards[currentCardIndex].interval);
        filteredCards[currentCardIndex].nextReview = nextReviewDate.toISOString();
        filteredCards[currentCardIndex].lastReviewed = new Date().toISOString();

        saveData();
        updateStatsElements();
        renderStatsCharts();
        showNotification('Don\'t worry! You did not remember yet.', 'error');
        moveToNextCard();
    });

    // Move to Next Card after answering
    function moveToNextCard() {
        flashcard.classList.remove('flipped');
        currentCardIndex = (currentCardIndex + 1) % filteredCards.length;
        updateCardDisplay();
    }

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

    // Category Filter in Practice Mode
    categoryFilter.addEventListener('change', () => {
        const selectedCategory = categoryFilter.value;
        if (selectedCategory === 'all') {
            filteredCards = [...cards];
        } else {
            filteredCards = cards.filter(card => card.category === selectedCategory);
        }
        currentPage = 1;
        updateCardsList();
    });

    // Statistics Chart using Chart.js
    let performanceChart;
    let quizPerformanceChart;

    function renderStatsCharts() {
        renderPerformanceChart();
        renderQuizPerformanceChart();
    }

    function renderPerformanceChart() {
        if (performanceChart) {
            performanceChart.destroy();
        }

        performanceChart = new Chart(performanceChartCtx, {
            type: 'pie',
            data: {
                labels: ['Remembered', 'Did Not Remember Yet'],
                datasets: [{
                    data: [stats.totalCorrect, stats.totalIncorrect],
                    backgroundColor: [
                        '#48bb78', // Green for Remembered
                        '#f56565'  // Red for Did Not Remember Yet
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

    function renderQuizPerformanceChart() {
        if (quizPerformanceChart) {
            quizPerformanceChart.destroy();
        }

        const quizData = stats.quizSessions.reduce((acc, session) => {
            acc.correct += session.correct;
            acc.incorrect += session.incorrect;
            return acc;
        }, { correct: 0, incorrect: 0 });

        quizPerformanceChart = new Chart(quizPerformanceChartCtx, {
            type: 'bar',
            data: {
                labels: ['Remembered', 'Did Not Remember Yet'],
                datasets: [{
                    label: 'Quiz Performance',
                    data: [quizData.correct, quizData.incorrect],
                    backgroundColor: [
                        '#4299e1', // Blue for Remembered
                        '#f56565'  // Red for Did Not Remember Yet
                    ],
                    borderColor: [
                        '#2b6cb0',
                        '#c53030'
                    ],
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            precision:0
                        },
                        title: {
                            display: true,
                            text: 'Number of Answers'
                        }
                    },
                    x: {
                        title: {
                            display: true,
                            text: 'Answer Type'
                        }
                    }
                },
                plugins: {
                    legend: {
                        display: false,
                    },
                    title: {
                        display: true,
                        text: 'Quiz Performance Overview'
                    }
                }
            },
        });
    }

    // Populate Quiz History Table
    function populateQuizHistoryTable() {
        quizHistoryTableBody.innerHTML = '';

        stats.quizSessions.forEach(session => {
            const tr = document.createElement('tr');

            const dateTd = document.createElement('td');
            const date = new Date(session.date);
            dateTd.textContent = date.toLocaleString();

            const categoryTd = document.createElement('td');
            categoryTd.textContent = session.category || 'All';

            const timeTd = document.createElement('td');
            timeTd.textContent = session.time !== 0 ? session.time : 'No Limit';

            const questionsTd = document.createElement('td');
            questionsTd.textContent = session.questions || '-';

            const correctTd = document.createElement('td');
            correctTd.textContent = session.correct;

            const incorrectTd = document.createElement('td');
            incorrectTd.textContent = session.incorrect;

            tr.appendChild(dateTd);
            tr.appendChild(categoryTd);
            tr.appendChild(timeTd);
            tr.appendChild(questionsTd);
            tr.appendChild(correctTd);
            tr.appendChild(incorrectTd);

            quizHistoryTableBody.appendChild(tr);
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
        renderStatsCharts();
    }

    // Initialize
    init();

    // ------------------- Quiz Mode Functionality -------------------

    // Enter Quiz Mode
    function enterQuizMode() {
        if (cards.length === 0) {
            quizContainer.style.display = 'none';
            quizEmptyState.style.display = 'block';
            return;
        }
        quizContainer.style.display = 'block';
        quizEmptyState.style.display = 'none';
        populateQuizCategoryFilter();
        setupQuizSession();
    }

    // Setup Quiz Session Interface
    function setupQuizSession() {
        quizConfig.style.display = 'block';
        quizSessionDiv.style.display = 'none';
        quizFeedback.textContent = '';
        quizQuestion.textContent = 'Configure your quiz session to begin.';
        quizOptionsContainer.innerHTML = '';
        nextQuizButton.disabled = true;
    }

    // Start Quiz Session
    startQuizButton.addEventListener('click', () => {
        selectedQuizCategory = quizCategoryFilter.value;
        const timeInput = parseInt(quizTimeInput.value, 10);
        quizTotalQuestions = parseInt(quizAmountInput.value, 10);

        if (isNaN(quizTotalQuestions) || quizTotalQuestions < 1 || quizTotalQuestions > 100) {
            showNotification('Please enter a valid number of questions (1-100).', 'error');
            return;
        }

        if (isNaN(timeInput) || timeInput < 0 || timeInput > 60) {
            showNotification('Please enter a valid time between 0 and 60 minutes.', 'error');
            return;
        }

        quizSessionDuration = timeInput * 60; // Convert minutes to seconds
        quizSessionTimeLeft = quizSessionDuration;
        quizSessionCorrect = 0;
        quizSessionIncorrect = 0;
        quizQuestionsAsked = 0;
        quizQuestions = [];
        currentQuizQuestion = '';
        quizCorrectAnswer = '';

        // Prepare quiz questions
        const availableCards = selectedQuizCategory === 'all' ? cards : cards.filter(card => card.category === selectedQuizCategory);
        if (availableCards.length === 0) {
            showNotification('No flashcards available for the selected category.', 'error');
            return;
        }

        // Shuffle available cards and select the number of questions
        const shuffled = availableCards.sort(() => 0.5 - Math.random());
        quizQuestions = shuffled.slice(0, Math.min(quizTotalQuestions, availableCards.length));

        // Enable quiz session
        quizSessionActive = true;

        // Disable navigation tabs
        disableNavigationTabs(true);

        // Hide configuration and show quiz session interface
        quizConfig.style.display = 'none';
        quizSessionDiv.style.display = 'block';
        quizFeedback.textContent = '';
        nextQuizButton.disabled = true;

        // Start Timer if time limit is set (time > 0)
        if (quizSessionDuration > 0) {
            startQuizTimer();
        } else {
            updateQuizTimerDisplay(); // Show "Unlimited"
        }

        // Generate the first quiz question
        generateQuizQuestion();
    });

    // Start Quiz Timer
    function startQuizTimer() {
        updateQuizTimerDisplay();
        quizSessionTimer = setInterval(() => {
            quizSessionTimeLeft--;
            updateQuizTimerDisplay();

            if (quizSessionTimeLeft <= 0) {
                endQuizSession();
            }
        }, 1000);
    }

    // Update Quiz Timer Display
    function updateQuizTimerDisplay() {
        const quizTimerDiv = document.getElementById('quiz-timer');
        if (quizSessionDuration === 0) {
            quizTimerDiv.textContent = `Time Left: Unlimited`;
            return;
        }

        const minutes = Math.floor(quizSessionTimeLeft / 60).toString().padStart(2, '0');
        const seconds = (quizSessionTimeLeft % 60).toString().padStart(2, '0');
        quizTimerDiv.textContent = `Time Left: ${minutes}:${seconds}`;
    }

    // End Quiz Session
    function endQuizSession() {
        clearInterval(quizSessionTimer);
        quizSessionActive = false;

        quizFeedback.textContent = `Quiz Session Ended! Correct: ${quizSessionCorrect}, Incorrect: ${quizSessionIncorrect}`;
        nextQuizButton.disabled = true;

        // Save quiz session results
        stats.quizSessions.push({
            date: new Date().toISOString(),
            category: selectedQuizCategory === 'all' ? 'All' : selectedQuizCategory,
            time: quizSessionDuration > 0 ? (quizSessionDuration / 60) : 0,
            questions: quizQuestionsAsked,
            correct: quizSessionCorrect,
            incorrect: quizSessionIncorrect
        });

        // Update overall statistics
        stats.totalCorrect += quizSessionCorrect;
        stats.totalIncorrect += quizSessionIncorrect;

        saveData();
        updateStatsElements();
        renderStatsCharts();
        populateQuizHistoryTable();

        showNotification(`Quiz Session Completed!\nCorrect: ${quizSessionCorrect}\nIncorrect: ${quizSessionIncorrect}`, 'success');
        setupQuizSession(); // Reset the quiz session interface
        disableNavigationTabs(false);
    }

    // Generate a Quiz Question
    function generateQuizQuestion() {
        if (quizQuestionsAsked >= quizTotalQuestions) {
            endQuizSession();
            return;
        }

        if (quizSessionDuration !== 0 && quizSessionTimeLeft <= 0) {
            endQuizSession();
            return;
        }

        const card = quizQuestions[quizQuestionsAsked];
        currentQuizQuestion = card.front;
        quizCorrectAnswer = card.back;

        // Generate options
        const options = generateQuizOptions(quizCorrectAnswer);
        displayQuizQuestion(card.front, options);
    }

    // Generate Multiple-Choice Options
    function generateQuizOptions(correctAnswer) {
        const incorrectAnswers = cards
            .filter(c => c.back !== correctAnswer)
            .map(c => c.back);

        // Shuffle and take first 3 incorrect answers
        const shuffled = incorrectAnswers.sort(() => 0.5 - Math.random());
        const selectedIncorrect = shuffled.slice(0, 3);

        // Combine correct answer with incorrect ones and shuffle
        const allOptions = [...selectedIncorrect, correctAnswer].sort(() => 0.5 - Math.random());

        return allOptions;
    }

    // Display Quiz Question and Options
    function displayQuizQuestion(question, options) {
        quizQuestion.textContent = question;
        quizOptionsContainer.innerHTML = '';

        options.forEach(option => {
            const button = document.createElement('button');
            button.className = 'quiz-option-button secondary-button';
            button.textContent = option;
            button.addEventListener('click', () => handleQuizAnswer(option));
            quizOptionsContainer.appendChild(button);
        });

        nextQuizButton.disabled = true;
    }

    // Handle Quiz Answer Selection
    function handleQuizAnswer(selectedOption) {
        if (!quizSessionActive) return;

        if (selectedOption === quizCorrectAnswer) {
            quizFeedback.textContent = '✅ Remembered Correctly!';
            quizSessionCorrect += 1;
            stats.currentStreak += 1;
            if (stats.currentStreak > stats.bestStreak) {
                stats.bestStreak = stats.currentStreak;
            }
            // Update card's spaced repetition interval
            const card = cards.find(c => c.back === quizCorrectAnswer);
            if (card) {
                card.interval = card.interval ? card.interval * 2 : 2;
                const nextReviewDate = new Date();
                nextReviewDate.setDate(nextReviewDate.getDate() + card.interval);
                card.nextReview = nextReviewDate.toISOString();
                card.lastReviewed = new Date().toISOString();
            }
            showNotification('Great! You remembered correctly.', 'success');
        } else {
            quizFeedback.textContent = `❌ Did Not Remember! Correct Answer: ${quizCorrectAnswer}`;
            quizSessionIncorrect += 1;
            stats.currentStreak = 0;
            // Reset card's spaced repetition interval
            const card = cards.find(c => c.back === quizCorrectAnswer);
            if (card) {
                card.interval = 1;
                const nextReviewDate = new Date();
                nextReviewDate.setDate(nextReviewDate.getDate() + card.interval);
                card.nextReview = nextReviewDate.toISOString();
                card.lastReviewed = new Date().toISOString();
            }
            showNotification(`Oops! The correct answer was: ${quizCorrectAnswer}`, 'error');
        }

        stats.quizSessions[stats.quizSessions.length - 1].questions = quizQuestionsAsked + 1;

        saveData();
        updateStatsElements();
        renderStatsCharts();
        populateQuizHistoryTable();
        quizQuestionsAsked += 1;
        nextQuizButton.disabled = false;
    }

    // Next Quiz Question
    nextQuizButton.addEventListener('click', () => {
        quizFeedback.textContent = '';
        nextQuizButton.disabled = true;
        generateQuizQuestion();
    });

    // Exit Quiz Session
    exitQuizButton.addEventListener('click', () => {
        if (confirm('Are you sure you want to exit the quiz session early?')) {
            endQuizSession();
        }
    });

    // ------------------- End of Quiz Mode Functionality -------------------

    // ------------------- Notification Functionality -------------------

    function showNotification(message, type) {
        notificationDiv.textContent = message;
        notificationDiv.className = `notification ${type}`;
        notificationDiv.style.display = 'block';

        // Hide after 3 seconds
        setTimeout(() => {
            notificationDiv.style.display = 'none';
            notificationDiv.className = 'notification';
        }, 3000);
    }

    // ------------------- End of Notification Functionality -------------------
});
