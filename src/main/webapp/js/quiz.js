// Quiz Class Definition
class Quiz {
    constructor(quizData, totalQuestions = 30, timeLimit = 15 * 60) { // timeLimit in seconds
        this.quizData = quizData; // Array of question objects
        this.totalQuestions = totalQuestions;
        this.timeLimit = timeLimit;
        this.timeLeft = timeLimit;
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
            btn.onclick = () => this.goToQuestion(i);
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
        this.timerId = setInterval(() => this.countdown(), 1000);
    }

    // Countdown Function
    countdown() {
        if (this.timeLeft <= 0) {
            clearInterval(this.timerId);
            this.submitQuiz();
        } else {
            let minutes = Math.floor(this.timeLeft / 60);
            let seconds = this.timeLeft % 60;
            if (seconds < 10) seconds = '0' + seconds;
            this.countdownTimer.textContent = `Time left: ${minutes}:${seconds}`;
            this.timeLeft--;
        }
    }

    // Update Progress Bar
    updateProgressBar() {
        const answered = Object.keys(this.userAnswers).length;
        const percentage = (answered / this.totalQuestions) * 100;
        this.progressBar.style.width = `${percentage}%`;
        this.answeredCount.textContent = answered;
    }

    // Submit Quiz and Show Results
    submitQuiz() {
        clearInterval(this.timerId);
        let score = 0;
        this.quizData.forEach((q, index) => {
            if (this.userAnswers[index] === q.answer) {
                score++;
            }
        });
        this.resultText.textContent = `You scored ${score} out of ${this.totalQuestions}.`;
        this.resultModal.style.display = 'block';
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

    // Jump to Section (Optional for 30 questions)
    jumpToSection(sectionNumber) {
        // For 30 questions, you can divide into 3 sections of 10
        const questionsPerSection = 10;
        const targetIndex = (sectionNumber - 1) * questionsPerSection;
        if (targetIndex >= 0 && targetIndex < this.totalQuestions) {
            this.currentQuestionIndex = targetIndex;
            this.renderQuestion();
        }
    }
}

// Sample Quiz Data (30 Questions)
const sampleQuizData = [
    {
        question: "What is the capital of France?",
        options: ["A. Paris", "B. London", "C. Berlin", "D. Rome"],
        answer: "A. Paris"
    },
    {
        question: "Which planet is known as the Red Planet?",
        options: ["A. Earth", "B. Mars", "C. Jupiter", "D. Saturn"],
        answer: "B. Mars"
    },
    {
        question: "Who wrote 'To be, or not to be'?",
        options: ["A. Charles Dickens", "B. William Shakespeare", "C. Mark Twain", "D. Jane Austen"],
        answer: "B. William Shakespeare"
    },
    {
        question: "What is the largest ocean on Earth?",
        options: ["A. Atlantic Ocean", "B. Indian Ocean", "C. Arctic Ocean", "D. Pacific Ocean"],
        answer: "D. Pacific Ocean"
    },
    {
        question: "What is the process by which plants make their food?",
        options: ["A. Respiration", "B. Transpiration", "C. Photosynthesis", "D. Digestion"],
        answer: "C. Photosynthesis"
    },
    {
        question: "Which gas is most abundant in the Earth's atmosphere?",
        options: ["A. Oxygen", "B. Nitrogen", "C. Carbon Dioxide", "D. Hydrogen"],
        answer: "B. Nitrogen"
    },
    {
        question: "What is the hardest natural substance on Earth?",
        options: ["A. Gold", "B. Iron", "C. Diamond", "D. Silver"],
        answer: "C. Diamond"
    },
    {
        question: "Who painted the Mona Lisa?",
        options: ["A. Vincent Van Gogh", "B. Pablo Picasso", "C. Leonardo da Vinci", "D. Claude Monet"],
        answer: "C. Leonardo da Vinci"
    },
    {
        question: "What is the smallest prime number?",
        options: ["A. 0", "B. 1", "C. 2", "D. 3"],
        answer: "C. 2"
    },
    {
        question: "Which element has the chemical symbol 'O'?",
        options: ["A. Gold", "B. Oxygen", "C. Osmium", "D. Silver"],
        answer: "B. Oxygen"
    },
    // Add 20 more questions similarly...
    {
        question: "What is the boiling point of water at sea level?",
        options: ["A. 90°C", "B. 100°C", "C. 110°C", "D. 120°C"],
        answer: "B. 100°C"
    },
    {
        question: "Who is known as the Father of Computers?",
        options: ["A. Alan Turing", "B. Charles Babbage", "C. Bill Gates", "D. Steve Jobs"],
        answer: "B. Charles Babbage"
    },
    {
        question: "Which language is used to style web pages?",
        options: ["A. HTML", "B. Python", "C. CSS", "D. Java"],
        answer: "C. CSS"
    },
    {
        question: "What does HTTP stand for?",
        options: ["A. HyperText Transfer Protocol", "B. HighText Transfer Protocol", "C. HyperText Transmission Protocol", "D. HighText Transmission Protocol"],
        answer: "A. HyperText Transfer Protocol"
    },
    {
        question: "What is the currency of Japan?",
        options: ["A. Yen", "B. Won", "C. Dollar", "D. Euro"],
        answer: "A. Yen"
    },
    {
        question: "Which organ is responsible for pumping blood?",
        options: ["A. Liver", "B. Brain", "C. Heart", "D. Lungs"],
        answer: "C. Heart"
    },
    {
        question: "What is the largest planet in our Solar System?",
        options: ["A. Earth", "B. Jupiter", "C. Saturn", "D. Mars"],
        answer: "B. Jupiter"
    },
    {
        question: "Who discovered penicillin?",
        options: ["A. Marie Curie", "B. Alexander Fleming", "C. Isaac Newton", "D. Albert Einstein"],
        answer: "B. Alexander Fleming"
    },
    {
        question: "What is the chemical formula for water?",
        options: ["A. CO2", "B. H2O", "C. O2", "D. NaCl"],
        answer: "B. H2O"
    },
    {
        question: "Which country hosted the 2016 Summer Olympics?",
        options: ["A. China", "B. Brazil", "C. UK", "D. Russia"],
        answer: "B. Brazil"
    },
    {
        question: "What is the tallest mountain in the world?",
        options: ["A. K2", "B. Kangchenjunga", "C. Mount Everest", "D. Lhotse"],
        answer: "C. Mount Everest"
    },
    {
        question: "Who developed the theory of relativity?",
        options: ["A. Isaac Newton", "B. Galileo Galilei", "C. Albert Einstein", "D. Nikola Tesla"],
        answer: "C. Albert Einstein"
    },
    {
        question: "Which ocean is the largest?",
        options: ["A. Atlantic", "B. Indian", "C. Arctic", "D. Pacific"],
        answer: "D. Pacific"
    },
    {
        question: "What is the capital city of Australia?",
        options: ["A. Sydney", "B. Melbourne", "C. Canberra", "D. Brisbane"],
        answer: "C. Canberra"
    },
    {
        question: "What is the main gas found in the air we breathe?",
        options: ["A. Oxygen", "B. Nitrogen", "C. Carbon Dioxide", "D. Hydrogen"],
        answer: "B. Nitrogen"
    },
    {
        question: "Which planet has the most moons?",
        options: ["A. Earth", "B. Mars", "C. Jupiter", "D. Saturn"],
        answer: "C. Jupiter"
    },
    {
        question: "Who is the author of '1984'?",
        options: ["A. George Orwell", "B. Aldous Huxley", "C. Mark Twain", "D. J.K. Rowling"],
        answer: "A. George Orwell"
    },
    {
        question: "What is the hardest natural substance?",
        options: ["A. Gold", "B. Iron", "C. Diamond", "D. Platinum"],
        answer: "C. Diamond"
    },
    {
        question: "Which element is represented by the symbol 'Fe'?",
        options: ["A. Fluorine", "B. Iron", "C. Francium", "D. Fermium"],
        answer: "B. Iron"
    },
    {
        question: "What is the largest mammal?",
        options: ["A. Elephant", "B. Blue Whale", "C. Giraffe", "D. Hippopotamus"],
        answer: "B. Blue Whale"
    },
    {
        question: "Who painted the Sistine Chapel ceiling?",
        options: ["A. Leonardo da Vinci", "B. Michelangelo", "C. Raphael", "D. Donatello"],
        answer: "B. Michelangelo"
    }
    // Total of 30 questions
];

// Initialize Quiz on Page Load and Attach to Window
document.addEventListener('DOMContentLoaded', () => {
    window.quiz = new Quiz(sampleQuizData, 30, 15 * 60); // 15 minutes
});
