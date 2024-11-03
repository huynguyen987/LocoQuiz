// File: /js/competition.js

// Competition Class Definition
class Competition {
    constructor(competitionData) {
        this.competitionData = competitionData.questions; // Array of question objects
        this.totalQuestions = competitionData.totalQuestions;
        this.timeLimit = competitionData.timeLimit; // timeLimit in seconds
        this.timeLeft = this.timeLimit;
        this.currentQuestionIndex = 0; // 0-based index
        this.userAnswers = {}; // { questionIndex: selectedOption }
        this.timerId = null;
        this.competitionId = competitionData.competitionId;

        // Bind DOM elements
        this.countdownTimer = document.getElementById('countdown-timer');
        this.questionNumber = document.getElementById('question-number');
        this.questionText = document.getElementById('question-text');
        this.answerOptions = document.getElementById('answer-options');
        this.prevBtn = document.getElementById('prev-btn');
        this.nextBtn = document.getElementById('next-btn');
        this.submitBtn = document.getElementById('submit-btn');
        this.questionSelector = document.getElementById('question-selector');
        this.progressBar = document.getElementById('progress');
        this.answeredCount = document.getElementById('answered-count');
        this.totalCount = document.getElementById('total-count');
        this.resultModal = new bootstrap.Modal(document.getElementById('result-modal'));
        this.resultText = document.getElementById('result-text');

        // Initialize Competition
        this.init();
    }

    init() {
        this.totalCount.textContent = this.totalQuestions;
        this.createQuestionSelector();
        this.renderQuestion();
        this.updateProgressBar();
        this.startTimer();
    }

    // Render Current Question
    renderQuestion() {
        const question = this.competitionData[this.currentQuestionIndex];
        this.questionNumber.textContent = `Câu hỏi ${this.currentQuestionIndex + 1} trong ${this.totalQuestions}`;
        this.questionText.textContent = question.question;

        // Clear previous options
        this.answerOptions.innerHTML = '';

        // Render options
        question.options.forEach((option, index) => {
            const optionDiv = document.createElement('div');
            optionDiv.classList.add('form-check');

            const radioInput = document.createElement('input');
            radioInput.type = 'radio';
            radioInput.classList.add('form-check-input');
            radioInput.name = 'answer';
            radioInput.id = `answer${index}`;
            radioInput.value = option;
            if (this.userAnswers[this.currentQuestionIndex] === option) {
                radioInput.checked = true;
            }
            radioInput.onclick = () => this.saveAnswer(this.currentQuestionIndex, option);

            const label = document.createElement('label');
            label.classList.add('form-check-label');
            label.htmlFor = `answer${index}`;
            label.textContent = option;

            optionDiv.appendChild(radioInput);
            optionDiv.appendChild(label);
            this.answerOptions.appendChild(optionDiv);
        });

        // Update navigation buttons
        this.prevBtn.disabled = this.currentQuestionIndex === 0;
        if (this.currentQuestionIndex === this.totalQuestions - 1) {
            this.nextBtn.style.display = 'none';
            this.submitBtn.style.display = 'inline-block';
        } else {
            this.nextBtn.style.display = 'inline-block';
            this.submitBtn.style.display = 'none';
        }

        // Highlight active question selector
        this.highlightActiveSelector();
    }

    // Save User's Answer
    saveAnswer(index, answer) {
        this.userAnswers[index] = answer;
        this.updateQuestionSelectorStatus(index);
        this.updateProgressBar();
    }

    // Navigate to Next Question
    nextQuestion() {
        if (this.currentQuestionIndex < this.totalQuestions - 1) {
            this.currentQuestionIndex++;
            this.renderQuestion();
        }
    }

    // Navigate to Previous Question
    prevQuestion() {
        if (this.currentQuestionIndex > 0) {
            this.currentQuestionIndex--;
            this.renderQuestion();
        }
    }

    // Create Question Selector Buttons
    createQuestionSelector() {
        for (let i = 0; i < this.totalQuestions; i++) {
            const btn = document.createElement('button');
            btn.type = 'button';
            btn.classList.add('btn', 'btn-outline-secondary', 'btn-sm', 'm-1');
            btn.textContent = i + 1;
            btn.onclick = (e) => {
                e.preventDefault(); // Prevent default button behavior
                this.goToQuestion(i);
            };
            this.questionSelector.appendChild(btn);
        }
    }

    // Go to Specific Question
    goToQuestion(index) {
        this.currentQuestionIndex = index;
        this.renderQuestion();
    }

    // Highlight Active Selector Button
    highlightActiveSelector() {
        const buttons = this.questionSelector.getElementsByTagName('button');
        Array.from(buttons).forEach((btn, index) => {
            btn.classList.remove('active');
            if (index === this.currentQuestionIndex) {
                btn.classList.add('active');
            }
            if (this.userAnswers[index]) {
                btn.classList.add('answered', 'btn-success');
                btn.classList.remove('btn-outline-secondary');
            } else {
                btn.classList.remove('answered', 'btn-success');
                btn.classList.add('btn-outline-secondary');
            }
        });
    }

    // Update Selector Button Status
    updateQuestionSelectorStatus(index) {
        const buttons = this.questionSelector.getElementsByTagName('button');
        const btn = buttons[index];
        if (this.userAnswers[index]) {
            btn.classList.add('answered', 'btn-success');
            btn.classList.remove('btn-outline-secondary');
        } else {
            btn.classList.remove('answered', 'btn-success');
            btn.classList.add('btn-outline-secondary');
        }
    }

    // Start Countdown Timer
    startTimer() {
        this.updateTimerDisplay();
        this.timerId = setInterval(() => this.countdown(), 1000);
    }

    // Countdown Function
    countdown() {
        if (this.timeLeft <= 0) {
            clearInterval(this.timerId);
            this.submitCompetition();
        } else {
            this.timeLeft--;
            this.updateTimerDisplay();
        }
    }

    // Update Timer Display
    updateTimerDisplay() {
        let minutes = Math.floor(this.timeLeft / 60);
        let seconds = this.timeLeft % 60;
        if (seconds < 10) seconds = '0' + seconds;
        this.countdownTimer.textContent = `Thời gian còn lại: ${minutes}:${seconds}`;
    }

    // Update Progress Bar
    updateProgressBar() {
        const answered = Object.keys(this.userAnswers).length;
        const percentage = (answered / this.totalQuestions) * 100;
        this.progressBar.style.width = `${percentage}%`;
        this.progressBar.setAttribute('aria-valuenow', percentage);
        this.answeredCount.textContent = answered;
    }

    // Submit Competition and Show Result
    submitCompetition() {
        clearInterval(this.timerId);
        // Gửi competition data đến server để chấm điểm
        fetch(`${contextPath}/CompetitionController?action=take`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
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
                    // Hiển thị kết quả
                    this.showResult(data.score);
                }
            })
            .catch(error => {
                console.error('Error submitting competition:', error);
                alert('Có lỗi xảy ra khi gửi cuộc thi của bạn.');
            });
    }

    // Hiển thị kết quả
    showResult(score) {
        this.resultText.textContent = `Điểm của bạn: ${score}%`;
        this.resultModal.show();
    }

    // Close Modal
    closeModal() {
        this.resultModal.hide();
    }

    // Exit Competition
    exitCompetition() {
        window.location.href = `${contextPath}/CompetitionController?action=list`;
    }
}

// Function to fetch competition data from server
function fetchCompetitionData(competitionId) {
    return fetch(`${contextPath}/CompetitionController?action=take&competitionId=${competitionId}`)
        .then(response => {
            if (!response.ok) {
                return response.json().then(err => { throw new Error(err.error || 'Failed to load competition data.'); });
            }
            return response.json();
        });
}

// Initialize competition when the page has fully loaded
document.addEventListener('DOMContentLoaded', () => {
    // Lấy competitionId từ DOM
    const competitionId = document.getElementById('competition-id').value;

    // Fetch competition data with competitionId
    fetchCompetitionData(competitionId)
        .then(competitionData => {
            window.competition = new Competition(competitionData);
        })
        .catch(error => {
            console.error('Error loading competition data:', error);
            document.getElementById('question-text').textContent = 'Không thể tải dữ liệu cuộc thi.';
        });
});
