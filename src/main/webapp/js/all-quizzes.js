// all-quizzes.js
let currentPage = 1;

document.addEventListener('DOMContentLoaded', function() {
    fetchQuizzes(currentPage);
});

function changePage(direction) {
    currentPage += direction;
    if (currentPage < 1) currentPage = 1;
    document.getElementById('page-number').innerText = currentPage;
    fetchQuizzes(currentPage);
}

function fetchQuizzes(page) {
    fetch(`/api/quizzes?page=${page}`)
        .then(response => response.json())
        .then(data => {
            const quizContainer = document.getElementById('quiz-container');
            quizContainer.innerHTML = ''; // Clear previous quizzes
            data.forEach(quiz => {
                const quizElement = document.createElement('div');
                quizElement.className = 'quiz';
                quizElement.innerHTML = `
                    <h2>${quiz.title}</h2>
                    <p>${quiz.description}</p>
                    <a href="/quiz/${quiz.id}" class="start-quiz">Start Quiz</a>
                `;
                quizContainer.appendChild(quizElement);
            });
        })
        .catch(error => console.error('Error fetching quizzes:', error));
}