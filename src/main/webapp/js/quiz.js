// File: src/main/webapp/js/quiz.js

// Quiz Class Definition
class Quiz {
    constructor(quizData) {
        this.quizData = quizData.questions; // Array of question objects
        this.totalQuestions = quizData.totalQuestions;
        this.timeLimit = quizData.timeLimit; // timeLimit in seconds
        this.timeLeft = this.timeLimit;
        this.currentQuestionIndex = 0; // 0-based index
        this.userAnswers = {}; // { questionIndex: selectedOption }
        this.timerId = null;

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
        this.resultModal = document.getElementById('result-modal');
        this.resultText = document.getElementById('result-text');
        this.sectionSelect = document.getElementById('section-select');

        // Initialize Quiz
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
        const question = this.quizData[this.currentQuestionIndex];
        this.questionNumber.textContent = `Question ${this.currentQuestionIndex + 1} of ${this.totalQuestions}`;
        this.questionText.textContent = question.question;

        // Clear previous options
        this.answerOptions.innerHTML = '';

        // Render options
        question.options.forEach((option, index) => {
            const optionDiv = document.createElement('div');
            optionDiv.classList.add('answer-option');

            const radioInput = document.createElement('input');
            radioInput.type = 'radio';
            radioInput.name = 'answer';
            radioInput.id = `answer${index}`;
            radioInput.value = option;
            if (this.userAnswers[this.currentQuestionIndex] === option) {
                radioInput.checked = true;
            }
            radioInput.onclick = () => this.saveAnswer(this.currentQuestionIndex, option);

            const label = document.createElement('label');
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
                btn.classList.add('answered');
            } else {
                btn.classList.remove('answered');
            }
        });
    }

    // Update Selector Button Status
    updateQuestionSelectorStatus(index) {
        const buttons = this.questionSelector.getElementsByTagName('button');
        const btn = buttons[index];
        if (this.userAnswers[index]) {
            btn.classList.add('answered');
        } else {
            btn.classList.remove('answered');
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
            this.submitQuiz();
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
        this.countdownTimer.innerHTML = `Time left: ${minutes}:${seconds}`;
    }

    // Update Progress Bar
    updateProgressBar() {
        const answered = Object.keys(this.userAnswers).length;
        const percentage = (answered / this.totalQuestions) * 100;
        this.progressBar.style.width = `${percentage}%`;
        this.answeredCount.textContent = answered;
    }

    // Submit Quiz and Redirect to Result Page
    submitQuiz() {
        clearInterval(this.timerId);
        // Gửi quiz data đến server để chấm điểm
        fetch(`${contextPath}/SubmitQuizServlet`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                quizId: quizId,
                userAnswers: this.userAnswers,
                timeTaken: this.timeLeft < this.timeLimit ? (this.timeLimit - this.timeLeft) : this.timeLimit // Tính thời gian làm bài
            })
        })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => { throw new Error(err.error || 'Failed to submit quiz.'); });
                }
                return response.json();
            })
            .then(data => {
                if (data.error) {
                    alert(`Error: ${data.error}`);
                } else {
                    // Chuyển hướng đến trang kết quả
                    window.location.href = data.redirectUrl;
                }
            })
            .catch(error => {
                console.error('Error submitting quiz:', error);
                alert('There was an error submitting your quiz.');
            });
    }

    // Close Modal
    closeModal() {
        this.resultModal.style.display = 'none';
    }

    // Restart Quiz
    restartQuiz() {
        this.userAnswers = {};
        this.currentQuestionIndex = 0;
        this.timeLeft = this.timeLimit;
        this.renderQuestion();
        this.updateProgressBar();

        // Reset question selectors
        const buttons = this.questionSelector.getElementsByTagName('button');
        Array.from(buttons).forEach(btn => {
            btn.classList.remove('answered', 'active');
        });

        this.resultModal.style.display = 'none';
        clearInterval(this.timerId);
        this.startTimer();
    }

    // Jump to Section
    jumpToSection(sectionNumber) {
        // Calculate questions per section
        const sections = 3;
        const questionsPerSection = Math.ceil(this.totalQuestions / sections);
        const targetIndex = (sectionNumber - 1) * questionsPerSection;
        if (targetIndex >= 0 && targetIndex < this.totalQuestions) {
            this.currentQuestionIndex = targetIndex;
            this.renderQuestion();
        }
    }

    // Exit Quiz
    exitQuiz() {
        window.location.href = `${contextPath}/jsp/student.jsp`;
    }
}

// Function to fetch quiz data from server
function fetchQuizData(quizId, timeLimit, questionCount, shuffleQuestions) {
    return fetch(`${contextPath}/TakeQuizServlet?service=loadQuiz&id=${quizId}&time=${timeLimit}&questionCount=${questionCount}&shuffle=${shuffleQuestions}`)
        .then(response => {
            if (!response.ok) {
                return response.json().then(err => { throw new Error(err.error || 'Failed to load quiz data.'); });
            }
            return response.json();
        });
}

// Function to start quiz after selecting settings
function startQuiz() {
    // Time selection
    const timeSelect = document.getElementById('time-select');
    const selectedTimeValue = timeSelect.value;
    let selectedTime = selectedTimeValue === 'custom'
        ? parseInt(document.getElementById('custom-time').value, 10)
        : parseInt(selectedTimeValue, 10);

    // Validate time
    if (isNaN(selectedTime) || selectedTime < 300 || selectedTime > 3600) {
        alert('Please enter a valid time between 300 and 3600 seconds.');
        return;
    }

    // Number of questions selection
    const questionCountSelect = document.getElementById('question-count-select');
    const questionCountValue = questionCountSelect.value;
    let selectedQuestionCount = questionCountValue === 'custom'
        ? parseInt(document.getElementById('custom-question-count').value, 10)
        : parseInt(questionCountValue, 10);

    // Validate question count
    if (isNaN(selectedQuestionCount) || selectedQuestionCount < 1 || selectedQuestionCount > maxQuestionCount) {
        alert(`Please enter a valid number of questions between 1 and ${maxQuestionCount}.`);
        return;
    }

    // Shuffle option
    const shuffleSelect = document.getElementById('shuffle-select');
    const shuffleQuestions = shuffleSelect.checked;

    // Hide the modal and show the quiz container
    document.getElementById('quiz-settings-modal').style.display = 'none';
    document.querySelector('.quiz-container').style.display = 'block';

    // Fetch quiz data with selected parameters
    fetchQuizData(quizId, selectedTime, selectedQuestionCount, shuffleQuestions)
        .then(quizData => {
            window.quiz = new Quiz(quizData);
        })
        .catch(error => {
            console.error('Error loading quiz data:', error);
            document.getElementById('question-text').textContent = 'Cannot load quiz data.';
        });
}

// Function to toggle custom time input visibility
function toggleCustomTimeInput(value) {
    const customTimeInput = document.getElementById('custom-time');
    customTimeInput.style.display = (value === 'custom') ? 'block' : 'none';
}

// Function to toggle custom question count input visibility
function toggleCustomQuestionCountInput(value) {
    const customQuestionCountInput = document.getElementById('custom-question-count');
    customQuestionCountInput.style.display = (value === 'custom') ? 'block' : 'none';
}

// Initialize quiz when the page has fully loaded
document.addEventListener('DOMContentLoaded', () => {
    // Hide quiz container initially
    const quizContainer = document.querySelector('.quiz-container');
    quizContainer.style.display = 'none';

    // Show modal to select quiz settings
    const quizSettingsModal = document.getElementById('quiz-settings-modal');
    quizSettingsModal.style.display = 'flex';

    // Optional: Validate custom-question-count input on manual entry
    const customQuestionCountInput = document.getElementById('custom-question-count');
    customQuestionCountInput.addEventListener('input', function() {
        let value = parseInt(this.value, 10);
        if (isNaN(value) || value < 1) {
            this.value = 1;
        } else if (value > maxQuestionCount) {
            this.value = maxQuestionCount;
        }
    });

    // Optional: Validate custom-time input on manual entry
    const customTimeInput = document.getElementById('custom-time');
    customTimeInput.addEventListener('input', function() {
        let value = parseInt(this.value, 10);
        if (isNaN(value) || value < 300) {
            this.value = 300;
        } else if (value > 3600) {
            this.value = 3600;
        }
    });
});
