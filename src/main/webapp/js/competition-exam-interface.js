// File: /js/competition-exam-interface.js

document.addEventListener('DOMContentLoaded', () => {
    // Lấy competitionId từ URL
    const urlParams = new URLSearchParams(window.location.search);
    const competitionId = urlParams.get('competitionId');

    if (!competitionId) {
        alert('Competition ID is missing.');
        window.location.href = `${contextPath}/jsp/student.jsp?action=Classrooms`;
        return;
    }

    // Fetch competition details
    fetchCompetitionDetails(competitionId);
});

function fetchCompetitionDetails(competitionId) {
    fetch(`${contextPath}/TakeCompetitionController?action=take&competitionId=${competitionId}`, {
        method: 'GET',
        credentials: 'include' // Bao gồm session cookies
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(err => { throw new Error(err.error || 'Failed to load competition details.'); });
            }
            return response.json();
        })
        .then(data => {
            startExam(data);
        })
        .catch(error => {
            console.error('Error fetching competition details:', error);
            alert(`Error: ${error.message}`);
            window.location.href = `${contextPath}/jsp/student.jsp?action=Classrooms`;
        });
}

function startExam(data) {
    const examInterfaceDiv = document.getElementById('exam-interface');

    // Xóa nội dung cũ
    examInterfaceDiv.innerHTML = '';

    // Hiển thị câu hỏi và tùy chọn
    let currentQuestionIndex = 0;
    const userAnswers = {};

    const totalQuestions = data.totalQuestions;
    const timeLimit = data.timeLimit; // in seconds
    let timeLeft = timeLimit;
    let timerId = null;

    // Create Timer Display
    const timerDiv = document.createElement('div');
    timerDiv.id = 'exam-timer';
    timerDiv.classList.add('alert', 'alert-info', 'text-center');
    timerDiv.textContent = `Time Remaining: ${formatTime(timeLeft)}`;
    examInterfaceDiv.appendChild(timerDiv);

    // Create Question Display
    const questionDiv = document.createElement('div');
    questionDiv.id = 'exam-question';
    questionDiv.classList.add('mb-4');
    examInterfaceDiv.appendChild(questionDiv);

    // Create Navigation Buttons
    const navDiv = document.createElement('div');
    navDiv.classList.add('d-flex', 'justify-content-between', 'mb-4');

    const prevBtn = document.createElement('button');
    prevBtn.classList.add('btn', 'btn-secondary');
    prevBtn.textContent = 'Previous';
    prevBtn.disabled = true;
    prevBtn.addEventListener('click', () => navigateQuestion(-1));

    const nextBtn = document.createElement('button');
    nextBtn.classList.add('btn', 'btn-secondary');
    nextBtn.textContent = 'Next';
    nextBtn.addEventListener('click', () => navigateQuestion(1));

    navDiv.appendChild(prevBtn);
    navDiv.appendChild(nextBtn);
    examInterfaceDiv.appendChild(navDiv);

    // Create Submit Button
    const submitBtn = document.createElement('button');
    submitBtn.classList.add('btn', 'btn-primary', 'w-100');
    submitBtn.textContent = 'Submit Competition';
    submitBtn.disabled = true;
    submitBtn.addEventListener('click', submitCompetition);
    examInterfaceDiv.appendChild(submitBtn);

    // Render Initial Question
    renderQuestion(currentQuestionIndex);

    // Start Timer
    timerId = setInterval(() => {
        timeLeft--;
        timerDiv.textContent = `Time Remaining: ${formatTime(timeLeft)}`;
        if (timeLeft <= 0) {
            clearInterval(timerId);
            alert('Time is up! Submitting competition.');
            submitCompetition();
        }
    }, 1000);

    function renderQuestion(index) {
        const questionData = data.questions[index];
        questionDiv.innerHTML = `
            <h4>Question ${index + 1}/${totalQuestions}</h4>
            <p>${questionData.question}</p>
            <div>
                ${questionData.options.map(option => `
                    <div class="form-check">
                        <input class="form-check-input" type="radio" name="question_${index}" id="option_${index}_${option}" value="${option}" ${userAnswers[index] === option ? 'checked' : ''} onchange="recordAnswer(${index}, '${option}')">
                        <label class="form-check-label" for="option_${index}_${option}">
                            ${option}
                        </label>
                    </div>
                `).join('')}
            </div>
        `;

        // Update Navigation Buttons
        prevBtn.disabled = index === 0;
        nextBtn.disabled = index === totalQuestions - 1;

        // Check if all questions are answered to enable submit button
        if (Object.keys(userAnswers).length === totalQuestions) {
            submitBtn.disabled = false;
        } else {
            submitBtn.disabled = true;
        }
    }

    function navigateQuestion(direction) {
        currentQuestionIndex += direction;
        renderQuestion(currentQuestionIndex);
    }

    window.recordAnswer = function(index, answer) {
        userAnswers[index] = answer;
        if (Object.keys(userAnswers).length === totalQuestions) {
            submitBtn.disabled = false;
        } else {
            submitBtn.disabled = true;
        }
    };

    function submitCompetition() {
        clearInterval(timerId);

        // Gửi dữ liệu đáp án về server
        fetch(`${contextPath}/TakeCompetitionController?action=submit`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: 'include',
            body: JSON.stringify({
                competitionId: data.competitionId,
                userAnswers: userAnswers,
                timeTaken: timeLimit - timeLeft // Time taken in seconds
            })
        })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => { throw new Error(err.error || 'Failed to submit competition.'); });
                }
                return response.json();
            })
            .then(result => {
                displayResult(result);
            })
            .catch(error => {
                console.error('Error submitting competition:', error);
                alert(`An error occurred: ${error.message}`);
            });
    }

    function displayResult(result) {
        examInterfaceDiv.innerHTML = `
            <div class="alert alert-success">
                <h4 class="alert-heading">Competition Submitted Successfully!</h4>
                <p>Your Score: ${result.score}%</p>
                <hr>
                <p class="mb-0">Correct Answers: ${result.correctCount} / ${result.totalQuestions}</p>
            </div>
            <a href="${contextPath}/jsp/student.jsp?action=Classrooms" class="btn btn-primary">Back to Classes</a>
        `;
    }

    function formatTime(seconds) {
        const mins = Math.floor(seconds / 60);
        const secs = seconds % 60;
        return `${mins}:${secs < 10 ? '0' + secs : secs}`;
    }
}
