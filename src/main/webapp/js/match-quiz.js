// Variables
let quizData;
let userMatches = {};
let timeLimit = 900; // default time limit in seconds (15 minutes)
let timerInterval;
let selectedQuestionCount = 5;
let shuffleQuestions = false;
let correctAnswers = {}; // To store correct answers mapping

// Function to Toggle Custom Time Input Visibility
function toggleCustomTimeInput(value) {
    const customTimeInput = document.getElementById('custom-time');
    if (value === 'custom') {
        customTimeInput.style.display = 'block';
        customTimeInput.focus();
    } else {
        customTimeInput.style.display = 'none';
    }
}

// Function to Toggle Custom Question Count Input Visibility
function toggleCustomQuestionCountInput(value) {
    const customQuestionCountInput = document.getElementById('custom-question-count');
    if (value === 'custom') {
        customQuestionCountInput.style.display = 'block';
        customQuestionCountInput.focus();
    } else {
        customQuestionCountInput.style.display = 'none';
    }
}

// Function to Increment Question Count
function incrementQuestionCount() {
    const input = document.getElementById('custom-question-count');
    let currentValue = parseInt(input.value, 10) || 1;
    if (currentValue < maxQuestionCount) {
        input.value = currentValue + 1;
    }
}

// Function to Decrement Question Count
function decrementQuestionCount() {
    const input = document.getElementById('custom-question-count');
    let currentValue = parseInt(input.value, 10) || 1;
    if (currentValue > 1) {
        input.value = currentValue - 1;
    }
}

// Function to Initialize Quiz
function initializeQuiz() {
    // Get selected time
    const timeSelect = document.getElementById('time-select');
    const selectedTimeValue = timeSelect.value;
    let selectedTime;

    if (selectedTimeValue === 'custom') {
        const customTimeInput = document.getElementById('custom-time');
        selectedTime = parseInt(customTimeInput.value, 10);

        if (isNaN(selectedTime)) {
            alert('Please enter a valid custom time.');
            return;
        }

        // Validate custom time (min: 300 seconds, max: 3600 seconds)
        if (selectedTime < 300 || selectedTime > 3600) {
            alert('Please enter a time between 5 and 60 minutes.');
            return;
        }
    } else {
        selectedTime = parseInt(selectedTimeValue, 10);
    }

    // Get selected number of questions
    const questionCountSelect = document.getElementById('question-count-select');
    const questionCountValue = questionCountSelect.value;
    let selectedQuestionCountLocal;

    if (questionCountValue === 'custom') {
        const customQuestionCountInput = document.getElementById('custom-question-count');
        selectedQuestionCountLocal = parseInt(customQuestionCountInput.value, 10);

        if (isNaN(selectedQuestionCountLocal)) {
            alert('Please enter a valid number of questions.');
            return;
        }

        // Validate selectedQuestionCount between 1 and maxQuestionCount
        if (selectedQuestionCountLocal < 1 || selectedQuestionCountLocal > maxQuestionCount) {
            alert(`Please enter a number between 1 and ${maxQuestionCount}.`);
            return;
        }
    } else {
        selectedQuestionCountLocal = parseInt(questionCountValue, 10);
    }

    // Get shuffle option
    const shuffleSelect = document.getElementById('shuffle-select');
    const shuffleValue = shuffleSelect.value;
    shuffleQuestions = (shuffleValue === 'true');

    // Assign to global variable
    timeLimit = selectedTime;
    selectedQuestionCount = selectedQuestionCountLocal;

    // Hide the settings modal and show the quiz container
    const settingsModal = document.getElementById('settings-modal');
    settingsModal.style.display = 'none';
    const quizContainer = document.querySelector('.quiz-container');
    quizContainer.style.display = 'block';

    // Fetch quiz data
    fetchQuizData();
}

// Function to Fetch Quiz Data
function fetchQuizData() {
    // Prepare the URL with parameters
    let url = `${contextPath}/TakeQuizServlet?service=loadQuiz&id=${quizId}&time=${timeLimit}&questionCount=${selectedQuestionCount}&shuffle=${shuffleQuestions}`;

    fetch(url)
        .then(response => response.json())
        .then(data => {
            if (data.error) {
                alert(`Error: ${data.error}`);
                return;
            }
            quizData = data;
            renderQuiz();
        })
        .catch(error => {
            console.error('Error fetching quiz data:', error);
            alert('Failed to load quiz data. Please try again.');
        });
}

// Function to Render Quiz
function renderQuiz() {
    // Update Quiz Title
    document.getElementById('quiz-title').textContent = quizData.title || 'Match the Items';

    // Update Timer Display
    document.getElementById('time-left').textContent = formatTime(timeLimit);

    // Get containers
    const questionsContainer = document.getElementById('questions');
    const optionsContainer = document.getElementById('options');

    // Clear existing items
    questionsContainer.innerHTML = '';
    optionsContainer.innerHTML = '';

    // Extract questions and shuffle if necessary
    let questions = [...quizData.questions];
    if (shuffleQuestions) {
        shuffleArray(questions);
    }
    // Limit to selectedQuestionCount
    questions = questions.slice(0, selectedQuestionCount);

    // Store the correctAnswers mapping for later
    correctAnswers = {};
    questions.forEach((question) => {
        correctAnswers[question.sequence] = question.correct;
    });

    // Populate questions (draggable)
    questions.forEach(question => {
        const questionDiv = document.createElement('div');
        questionDiv.classList.add('draggable');
        questionDiv.setAttribute('data-id', question.sequence);
        questionDiv.setAttribute('draggable', 'true');
        questionDiv.setAttribute('aria-grabbed', 'false');
        questionDiv.setAttribute('role', 'option');
        questionDiv.setAttribute('tabindex', '0');
        questionDiv.textContent = question.question;
        questionsContainer.appendChild(questionDiv);
    });

    // Populate options (correct answers)
    let options = questions.map(q => q.correct);
    if (shuffleQuestions) {
        shuffleArray(options);
    }
    options.forEach((option, index) => {
        const optionDiv = document.createElement('div');
        optionDiv.classList.add('dropzone');
        optionDiv.setAttribute('data-id', index + 1); // Assign unique IDs
        optionDiv.setAttribute('aria-dropeffect', 'move');
        optionDiv.setAttribute('role', 'option');
        optionDiv.setAttribute('tabindex', '0');
        optionDiv.textContent = option;
        optionsContainer.appendChild(optionDiv);
    });

    // Initialize drag and drop
    initializeDragAndDrop();

    // Start timer
    startTimer();
}

// Function to Shuffle Array Elements
function shuffleArray(array) {
    for (let i = array.length -1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i+1));
        [array[i], array[j]] = [array[j], array[i]];
    }
    return array;
}

// Function to Initialize Drag and Drop
function initializeDragAndDrop() {
    interact('.draggable').draggable({
        inertia: true,
        autoScroll: true,
        listeners: {
            move (event) {
                const target = event.target;
                // Keep the dragged position in the data-x/data-y attributes
                const x = (parseFloat(target.getAttribute('data-x')) || 0) + event.dx;
                const y = (parseFloat(target.getAttribute('data-y')) || 0) + event.dy;

                // Translate the element
                target.style.transform = `translate(${x}px, ${y}px)`;

                // Update the position attributes
                target.setAttribute('data-x', x);
                target.setAttribute('data-y', y);
            },
            end (event) {
                const target = event.target;
                const dropzoneId = target.getAttribute('data-dropzone');

                if (!dropzoneId) {
                    // Snap back to original position
                    target.style.transform = 'translate(0px, 0px)';
                    target.removeAttribute('data-x');
                    target.removeAttribute('data-y');
                }
            }
        }
    });

    interact('.dropzone').dropzone({
        accept: '.draggable',
        overlap: 0.75,

        ondropactivate: function (event) {
            event.target.classList.add('drop-active');
        },
        ondragenter: function (event) {
            const draggableElement = event.relatedTarget;
            const dropzoneElement = event.target;

            dropzoneElement.classList.add('over');
            draggableElement.classList.add('can-drop');
        },
        ondragleave: function (event) {
            event.target.classList.remove('over');
            event.relatedTarget.classList.remove('can-drop');
        },
        ondrop: function (event) {
            const draggableElement = event.relatedTarget;
            const dropzoneElement = event.target;

            // Check if dropzone already has a draggable
            if (dropzoneElement.querySelector('.draggable')) {
                alert('This option is already matched to a question.');
                return;
            }

            // Get IDs
            const draggableId = draggableElement.getAttribute('data-id');
            const dropzoneText = dropzoneElement.textContent.trim();

            // Assign match
            userMatches[draggableId] = dropzoneText;

            // Snap the draggable to the dropzone
            draggableElement.style.transform = 'translate(0px, 0px)';
            draggableElement.removeAttribute('data-x');
            draggableElement.removeAttribute('data-y');
            draggableElement.setAttribute('data-dropzone', dropzoneElement.getAttribute('data-id'));

            // Append the draggable to the dropzone
            dropzoneElement.appendChild(draggableElement);

            // Update Progress Bar
            updateProgress();
        },
        ondropdeactivate: function (event) {
            event.target.classList.remove('drop-active');
            event.target.classList.remove('over');
        }
    });
}

// Function to Start Timer
function startTimer() {
    const countdownTimer = document.getElementById('time-left');
    let timeLeft = timeLimit;

    // Initialize Timer Display
    updateTimerDisplay(countdownTimer, timeLeft);

    // Start Countdown
    timerInterval = setInterval(() => {
        timeLeft--;
        updateTimerDisplay(countdownTimer, timeLeft);

        if (timeLeft <= 0) {
            clearInterval(timerInterval);
            submitQuiz();
        }
    }, 1000);
}

// Function to Update Timer Display
function updateTimerDisplay(element, timeLeft) {
    let minutes = Math.floor(timeLeft / 60);
    let seconds = timeLeft % 60;
    if (seconds < 10) seconds = '0' + seconds;
    element.textContent = `${minutes}:${seconds}`;
}

// Function to Format Time (Utility)
function formatTime(seconds) {
    let mins = Math.floor(seconds / 60);
    let secs = seconds % 60;
    if (secs < 10) secs = '0' + secs;
    return `${mins}:${secs}`;
}

function submitQuiz() {
    // Disable further dragging
    interact('.draggable').draggable(false);

    // Prepare userAnswers
    let userAnswers = {};
    for (let key in userMatches) {
        userAnswers[key] = userMatches[key];
    }

    // Prepare data to send to server
    const submissionData = {
        quizId: parseInt(quizId),
        userAnswers: userAnswers
    };

    // Send POST request to SubmitMatchQuizServlet
    fetch(`${contextPath}/SubmitMatchQuizServlet`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(submissionData)
    })
        .then(response => response.json())
        .then(data => {
            if (data.error) {
                alert(`Error: ${data.error}`);
            } else {
                const score = data.score;
                const total = data.total;
                document.getElementById('result-text').textContent = `You scored ${score} out of ${total}.`;
                document.getElementById('result-modal').style.display = 'block';
            }
        })
        .catch(error => {
            console.error('Error submitting quiz:', error);
            alert('There was an error submitting your quiz.');
        });

    // Clear Timer
    clearInterval(timerInterval);
}


// Function to Update Progress Bar
function updateProgress() {
    const total = selectedQuestionCount;
    const matched = Object.keys(userMatches).length;
    const progressBar = document.getElementById('progress-bar');
    const percentage = (matched / total) * 100;
    progressBar.style.width = `${percentage}%`;
}

// Function to Reset Progress Bar
function resetProgress() {
    const progressBar = document.getElementById('progress-bar');
    progressBar.style.width = `0%`;
}

// Function to Close Modal
function closeModal(modalId) {
    let modal = document.getElementById(modalId);
    modal.style.display = 'none';
}

// Function to Reset Quiz
function resetQuiz() {
    userMatches = {};

    // Reset draggable items
    let draggables = document.querySelectorAll('.draggable');
    draggables.forEach(draggable => {
        draggable.classList.remove('correct', 'incorrect');
        draggable.style.transform = 'translate(0px, 0px)';
        draggable.removeAttribute('data-dropzone');
        document.getElementById('questions').appendChild(draggable);
    });

    // Reset dropzones
    let dropzones = document.querySelectorAll('.dropzone');
    dropzones.forEach(dropzone => {
        // Remove any draggables inside dropzones
        let children = dropzone.querySelectorAll('.draggable');
        children.forEach(child => {
            dropzone.removeChild(child);
        });
    });

    // Reset Progress Bar
    resetProgress();

    // Close Modal
    let resultModal = document.getElementById('result-modal');
    resultModal.style.display = 'none';

    // Restart Timer
    clearInterval(timerInterval);
    startTimer();

    // Re-enable dragging
    interact('.draggable').draggable(true);
}

// Close modal when clicking outside of it
window.onclick = function(event) {
    let resultModal = document.getElementById('result-modal');
    let settingsModal = document.getElementById('settings-modal');
    if (event.target == resultModal) {
        resultModal.style.display = "none";
    }
    if (event.target == settingsModal) {
        settingsModal.style.display = "none";
    }
}

// Initialize the Quiz Settings Modal on Page Load
window.onload = function() {
    // Show the settings modal
    const settingsModal = document.getElementById('settings-modal');
    settingsModal.style.display = 'block';

    // Initialize quiz container as hidden
    const quizContainer = document.querySelector('.quiz-container');
    quizContainer.style.display = 'none';

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
};
