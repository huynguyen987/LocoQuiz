// script.js

document.addEventListener('DOMContentLoaded', () => {
    const searchBox = document.getElementById('search-box');
    const sortBox = document.getElementById('sort-box');
    const quizCardsContainer = document.getElementById('quiz-cards-container');
    const quizCards = Array.from(quizCardsContainer.getElementsByClassName('quiz-card'));

    // Search Functionality
    searchBox.addEventListener('input', () => {
        const searchText = searchBox.value.toLowerCase();
        quizCards.forEach(card => {
            const title = card.querySelector('.quiz-title').textContent.toLowerCase();
            if (title.includes(searchText)) {
                card.style.display = '';
            } else {
                card.style.display = 'none';
            }
        });
    });

    // Sort Functionality
    sortBox.addEventListener('change', () => {
        const sortValue = sortBox.value;
        let sortedCards = [];
        if (sortValue === 'az') {
            sortedCards = quizCards.sort((a, b) => {
                const titleA = a.querySelector('.quiz-title').textContent;
                const titleB = b.querySelector('.quiz-title').textContent;
                return titleA.localeCompare(titleB);
            });
        } else if (sortValue === 'latest') {
            sortedCards = quizCards.sort((a, b) => {
                // Assuming we have data attributes for created_at
                const dateA = new Date(a.dataset.createdAt);
                const dateB = new Date(b.dataset.createdAt);
                return dateB - dateA;
            });
        } else if (sortValue === 'newest') {
            sortedCards = quizCards.sort((a, b) => {
                // Assuming we have data attributes for added_at
                const dateA = new Date(a.dataset.addedAt);
                const dateB = new Date(b.dataset.addedAt);
                return dateB - dateA;
            });
        }

        // Re-render the sorted cards
        quizCardsContainer.innerHTML = '';
        sortedCards.forEach(card => quizCardsContainer.appendChild(card));
    });

    // "Add More Quiz" button
    const addQuizBtn = document.getElementById('add-quiz');
    addQuizBtn.addEventListener('click', () => {
        window.location.href = 'AllQuizzesServlet'; // Redirect to all quizzes page
    });
});
