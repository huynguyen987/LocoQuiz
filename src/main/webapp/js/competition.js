// File: /js/competition.js

class Competition {
    constructor(competitionData) {
        this.competitionId = competitionData.competitionId;
        this.totalQuestions = competitionData.totalQuestions;
        this.timeLimit = competitionData.timeLimit; // in seconds
        this.questions = competitionData.questions;
        this.currentQuestionIndex = 0;
        this.userAnswers = {}; // {0: "Option A", 1: "Option B", ...}
        this.timeLeft = this.timeLimit;
        this.timerId = null;

        // DOM Elements
        this.countdownTimer = document.getElementById('countdown-timer');
        this.progressBar = document.getElementById('progress');
        this.answeredCount = document.getElementById('answered-count');
        this.totalCount = document.getElementById('total-count');
        this.questionSelector = document.getElementById('question-selector');
        this.questionNumber = document.getElementById('question-number');
        this.questionText = document.getElementById('question-text');
        this.answerOptions = document.getElementById('answer-options');
        this.submitBtn = document.getElementById('submit-btn');
        this.resultModal = new bootstrap.Modal(document.getElementById('result-modal'), {
            keyboard: false
        });
        this.resultText = document.getElementById('result-text');

        // Initialize
        this.initializeCompetition();
    }

    initializeCompetition() {
        this.totalCount.textContent = this.totalQuestions;
        this.renderQuestionSelector();
        this.renderCurrentQuestion();
        this.updateProgressBar();
        this.startTimer();
        this.attachEventListeners();
    }

    renderQuestionSelector() {
        for (let i = 0; i < this.totalQuestions; i++) {
            const btn = document.createElement('button');
            btn.classList.add('btn', 'btn-outline-secondary', 'm-1');
            btn.textContent = i + 1;
            btn.addEventListener('click', () => this.goToQuestion(i));
            this.questionSelector.appendChild(btn);
        }
    }

    renderCurrentQuestion() {
        const question = this.questions[this.currentQuestionIndex];
        this.questionNumber.textContent = `Question ${this.currentQuestionIndex + 1}/${this.totalQuestions}`;
        this.questionText.textContent = question.questionText;
        this.answerOptions.innerHTML = '';

        question.options.forEach(option => {
            const div = document.createElement('div');
            div.classList.add('form-check');

            const input = document.createElement('input');
            input.classList.add('form-check-input');
            input.type = 'radio';
            input.name = `answer_${this.currentQuestionIndex}`;
            input.id = `answer_${this.currentQuestionIndex}_option_${option}`;
            input.value = option;
            if (this.userAnswers[this.currentQuestionIndex] === option) {
                input.checked = true;
            }
            input.addEventListener('change', () => this.selectAnswer(option));

            const label = document.createElement('label');
            label.classList.add('form-check-label');
            label.htmlFor = input.id;
            label.textContent = option;

            div.appendChild(input);
            div.appendChild(label);
            this.answerOptions.appendChild(div);
        });

        this.highlightActiveSelector();
    }

    attachEventListeners() {
        document.getElementById('prev-btn').addEventListener('click', () => this.prevQuestion());
        document.getElementById('next-btn').addEventListener('click', () => this.nextQuestion());
        this.submitBtn.addEventListener('click', () => this.submitCompetition());
    }

    goToQuestion(index) {
        this.currentQuestionIndex = index;
        this.renderCurrentQuestion();
    }

    prevQuestion() {
        if (this.currentQuestionIndex > 0) {
            this.currentQuestionIndex--;
            this.renderCurrentQuestion();
        }
    }

    nextQuestion() {
        if (this.currentQuestionIndex < this.totalQuestions - 1) {
            this.currentQuestionIndex++;
            this.renderCurrentQuestion();
        }
    }

    selectAnswer(option) {
        this.userAnswers[this.currentQuestionIndex] = option;
        this.updateProgressBar();
        this.updateSubmitButton();
    }

    updateSubmitButton() {
        if (Object.keys(this.userAnswers).length === this.totalQuestions) {
            this.submitBtn.style.display = 'inline-block';
        } else {
            this.submitBtn.style.display = 'none';
        }
    }

    highlightActiveSelector() {
        const buttons = this.questionSelector.getElementsByTagName('button');
        Array.from(buttons).forEach((btn, index) => {
            btn.classList.remove('active');
            if (index === this.currentQuestionIndex) {
                btn.classList.add('active');
            }
            if (this.hasAnswered(index)) {
                btn.classList.add('answered', 'btn-success');
                btn.classList.remove('btn-outline-secondary');
            } else {
                btn.classList.remove('answered', 'btn-success');
                btn.classList.add('btn-outline-secondary');
            }
        });
    }

    hasAnswered(index) {
        return this.userAnswers.hasOwnProperty(index);
    }

    updateProgressBar() {
        const answered = Object.keys(this.userAnswers).length;
        const percentage = (answered / this.totalQuestions) * 100;
        this.progressBar.style.width = `${percentage}%`;
        this.progressBar.setAttribute('aria-valuenow', percentage);
        this.answeredCount.textContent = answered;
    }

    startTimer() {
        this.updateTimerDisplay();
        this.timerId = setInterval(() => this.countdown(), 1000);
    }

    countdown() {
        if (this.timeLeft <= 0) {
            clearInterval(this.timerId);
            this.submitCompetition();
        } else {
            this.timeLeft--;
            this.updateTimerDisplay();
        }
    }

    updateTimerDisplay() {
        let minutes = Math.floor(this.timeLeft / 60);
        let seconds = this.timeLeft % 60;
        if (seconds < 10) seconds = '0' + seconds;
        this.countdownTimer.textContent = `Time remaining: ${minutes}:${seconds}`;
    }

    submitCompetition() {
        clearInterval(this.timerId);
        // Send competition data to server for grading
        fetch(`${contextPath}/TakeCompetitionController?action=submit`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: 'include', // Include session cookies
            body: JSON.stringify({
                competitionId: this.competitionId,
                userAnswers: this.userAnswers,
                timeTaken: this.timeLimit - this.timeLeft // Time taken in seconds
            })
        })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => { throw new Error(err.error || 'Failed to submit competition.'); });
                }
                return response.json();
            })
            .then(data => {
                if (data.error) {
                    alert(`Error: ${data.error}`);
                } else {
                    // Display result
                    this.showResult(data.score, data.totalQuestions);
                }
            })
            .catch(error => {
                console.error('Error submitting competition:', error);
                alert(`An error occurred: ${error.message}`);
            });
    }

    showResult(score, totalQuestions) {
        this.resultText.textContent = `Your score: ${score}%\nCorrect answers: ${Math.round((score / 100) * totalQuestions)} / ${totalQuestions}`;
        this.resultModal.show();
    }

    exitCompetition() {
        window.location.href = `${contextPath}/CompetitionController?action=list`;
    }
}

// Function to fetch competition data from server
function fetchCompetitionData(competitionId) {
    return fetch(`${contextPath}/TakeCompetitionController?action=take&competitionId=${competitionId}`, {
        method: 'GET',
        credentials: 'include' // Include session cookies
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(err => { throw new Error(err.error || 'Failed to load competition data.'); });
            }
            return response.json();
        });
}

// Initialize competition when the page has fully loaded
document.addEventListener('DOMContentLoaded', () => {
    // Get competitionId from DOM
    const competitionIdInput = document.getElementById('competition-id');
    if (!competitionIdInput) {
        console.error('Competition ID input not found.');
        return;
    }
    const competitionId = competitionIdInput.value;

    if (!competitionId) {
        console.error('Competition ID is missing.');
        return;
    }

    // Fetch competition data with competitionId
    fetchCompetitionData(competitionId)
        .then(competitionData => {
            window.competition = new Competition(competitionData);
            console.log('Competition data:', competitionData);
        })
        .catch(error => {
            console.error('Error loading competition data:', error);
            const questionText = document.getElementById('question-text');
            if (questionText) {
                questionText.textContent = 'Unable to load competition data.';
            }
            alert(`Error: ${error.message}`);
        });
});
