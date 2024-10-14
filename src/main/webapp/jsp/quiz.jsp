<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Quiz Practice</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/quiz.css">
</head>
<body>
<div class="quiz-container">
    <!-- Quiz Duration Modal -->
    <div id="duration-modal" class="modal">
        <div class="modal-content">
            <h2>Select Quiz Duration</h2>
            <label for="duration-select">Choose time (in minutes):</label>
            <select id="duration-select">
                <option value="5">5 minutes</option>
                <option value="10">10 minutes</option>
                <option value="15" selected>15 minutes</option>
                <option value="20">20 minutes</option>
                <option value="30">30 minutes</option>
            </select>
            <button onclick="quiz.startQuiz()">Start Quiz</button>
        </div>
    </div>

    <!-- Quiz Header with Timer and Progress Bar -->
    <div class="quiz-header">
        <div class="countdown-timer" id="countdown-timer">Time left: 15:00</div>
        <div class="progress-container">
            <div class="progress-bar">
                <div class="progress" id="progress"></div>
            </div>
            <div class="progress-details">
                <span id="answered-count">0</span> / <span id="total-count">0</span> Answered
            </div>
        </div>
    </div>

    <!-- Quiz Content -->
    <div class="quiz-content">
        <div class="question-number" id="question-number">Question 1 of X</div>
        <h2 id="question-text">Loading question...</h2>
        <form id="quiz-form">
            <div class="answer-options" id="answer-options">
                <!-- Answer options will be dynamically inserted here -->
            </div>
        </form>
    </div>

    <!-- Navigation Buttons -->
    <div class="navigation-buttons">
        <button id="prev-btn" onclick="quiz.prevQuestion()">Previous</button>
        <button id="next-btn" onclick="quiz.nextQuestion()">Next</button>
        <button id="submit-btn" onclick="quiz.submitQuiz()">Submit</button>
    </div>
</div>

<script>
    var quiz = {
        currentQuestionIndex: 0,
        questions: [],
        userAnswers: [],
        totalQuestions: 0,
        timer: null,
        timeLeft: 900, // Default to 15 minutes
        quizId: '${quizId}', // Corrected EL expression

        init: function () {
            // Show duration selection modal
            document.getElementById('duration-modal').style.display = 'block';

            // Hide quiz elements until quiz starts
            document.querySelector('.quiz-header').style.display = 'none';
            document.querySelector('.quiz-content').style.display = 'none';
            document.querySelector('.navigation-buttons').style.display = 'none';
        },

        startQuiz: function () {
            // Get selected duration
            const durationSelect = document.getElementById('duration-select');
            const selectedDuration = parseInt(durationSelect.value);

            // Set timeLeft in seconds
            this.timeLeft = selectedDuration * 60;

            // Hide duration modal
            document.getElementById('duration-modal').style.display = 'none';

            // Show quiz elements
            document.querySelector('.quiz-header').style.display = 'block';
            document.querySelector('.quiz-content').style.display = 'block';
            document.querySelector('.navigation-buttons').style.display = 'block';

            // Load quiz questions
            this.loadQuiz();

            // Start the timer
            this.startTimer();
        },

        loadQuiz: function () {
            fetch('TakeQuizServlet?service=loadQuiz&id=' + this.quizId)
                .then(response => response.json())
                .then(data => {
                    this.questions = data;
                    this.totalQuestions = data.length;
                    document.getElementById('total-count').textContent = this.totalQuestions;
                    this.renderQuestion();
                });
        },

        renderQuestion: function () {
            const question = this.questions[this.currentQuestionIndex];
            document.getElementById('question-number').textContent = 'Question ' + (this.currentQuestionIndex + 1) + ' of ' + this.totalQuestions;
            document.getElementById('question-text').textContent = question.question;

            let optionsHtml = '';
            const options = question.options;

            options.forEach((option, index) => {
                console.log(option+" "+index);
                const optionText = typeof option === 'string' ? option : option.text;
                const isChecked = this.userAnswers[this.currentQuestionIndex] == optionText ? 'checked' : '';
                optionsHtml += `
                    <label>
                        <input type="radio" name="answer" value="${optionText}" ${isChecked}>
                        ${optionText}
                    </label><br>
                `;
            });
            document.getElementById('answer-options').innerHTML = optionsHtml;

            // Update navigation buttons
            document.getElementById('prev-btn').disabled = this.currentQuestionIndex === 0;
            document.getElementById('next-btn').style.display = this.currentQuestionIndex === this.totalQuestions - 1 ? 'none' : 'inline-block';
            document.getElementById('submit-btn').style.display = this.currentQuestionIndex === this.totalQuestions - 1 ? 'inline-block' : 'none';

            // Update progress
            this.updateProgress();
        },

        nextQuestion: function () {
            this.saveAnswer();
            if (this.currentQuestionIndex < this.totalQuestions - 1) {
                this.currentQuestionIndex++;
                this.renderQuestion();
            }
        },

        prevQuestion: function () {
            this.saveAnswer();
            if (this.currentQuestionIndex > 0) {
                this.currentQuestionIndex--;
                this.renderQuestion();
            }
        },

        saveAnswer: function () {
            const selectedOption = document.querySelector('input[name="answer"]:checked');
            if (selectedOption) {
                this.userAnswers[this.currentQuestionIndex] = selectedOption.value;
            } else {
                this.userAnswers[this.currentQuestionIndex] = null;
            }
        },

        updateProgress: function () {
            const answeredCount = this.userAnswers.filter(ans => ans !== undefined && ans !== null).length;
            document.getElementById('answered-count').textContent = answeredCount;
            const progressPercentage = (answeredCount / this.totalQuestions) * 100;
            document.getElementById('progress').style.width = progressPercentage + '%';
        },

        submitQuiz: function () {
            this.saveAnswer();

            // Send user's answers to the server
            fetch('ResultController', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    answers: this.userAnswers,
                    quizId: this.quizId
                }),
            })
                .then(response => response.json())
                .then(data => {
                    // Corrected the quizId reference
                    window.location.href = `jsp/viewResult.jsp?score=${data.score}&total=${data.total}&id=${quizId}`;
                });
        },

        startTimer: function () {
            this.timer = setInterval(() => {
                if (this.timeLeft <= 0) {
                    clearInterval(this.timer);
                    alert('Time is up!');
                    this.submitQuiz();
                } else {
                    this.timeLeft--;
                    const minutes = Math.floor(this.timeLeft / 60);
                    const seconds = this.timeLeft % 60;
                    document.getElementById('countdown-timer').textContent = 'Time left: ' + minutes + ':' + (seconds < 10 ? '0' + seconds : seconds);
                }
            }, 1000);
        }
    };

    // Initialize the quiz when the page loads
    window.onload = function () {
        quiz.init();
    };
</script>
</body>
</html>
