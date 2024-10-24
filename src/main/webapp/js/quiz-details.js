// File: js/quiz-details.js

document.addEventListener("DOMContentLoaded", function() {
    // Tăng số lượt xem khi trang được tải
    updateViewCount();

    // Xử lý toggle hiển thị mô tả
    const toggleDesc = document.getElementById("toggleDesc");
    const quizDescription = document.getElementById("quizDescription");

    toggleDesc.addEventListener("click", function() {
        if (quizDescription.style.display === "none") {
            quizDescription.style.display = "block";
            toggleDesc.textContent = "[Hide]";
        } else {
            quizDescription.style.display = "none";
            toggleDesc.textContent = "[Show]";
        }
    });
});

/**
 * Hàm cập nhật số lượt xem (views) qua AJAX
 */
function updateViewCount() {
    const quizId = getQuizIdFromURL();
    if (!quizId) return;

    fetch(`/QuizLoco/UpdateViewServlet?quizId=${quizId}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                document.getElementById("viewCount").textContent = data.newViewCount;
            } else {
                console.error("Failed to update view count.");
            }
        })
        .catch(error => console.error('Error:', error));
}

/**
 * Hàm xóa quiz qua AJAX
 * @param {number} quizId
 */
function deleteQuiz(quizId) {
    if (confirm("Are you sure you want to delete this quiz?")) {
        fetch(`/QuizLoco/DeleteQuizServlet?quizId=${quizId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ quizId: quizId })
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert("Quiz deleted successfully.");
                    window.location.href = `/QuizLoco/jsp/teacher.jsp?message=deleteSuccess`;
                } else {
                    alert("Failed to delete quiz.");
                }
            })
            .catch(error => console.error('Error:', error));
    }
}

/**
 * Hàm lấy quizId từ URL
 */
function getQuizIdFromURL() {
    const params = new URLSearchParams(window.location.search);
    return params.get('id') || params.get('quizId');
}

document.addEventListener("DOMContentLoaded", function() {
    // Existing initialization code...

    // Check if the quizCreatorContainer exists before initializing question-related features
    const quizCreatorContainer = document.getElementById("quizCreatorContainer");
    if (quizCreatorContainer) {
        // Initialize question-related features
        document.getElementById("addQuestionBtn").addEventListener("click", addQuestion);
        // ... other event listeners related to questions
    }

    // Handle quiz type selection
    const quizTypeRadioButtons = document.getElementsByName("quizTypeRadio");
    const quizTypeInput = document.getElementById("quizType");
    const chosenQuizType = document.getElementById("chosenQuizType");
    const selectedQuizTypeDiv = document.getElementById("selectedQuizType");

    quizTypeRadioButtons.forEach(function(radio) {
        radio.addEventListener("change", function() {
            if (this.checked) {
                quizTypeInput.value = this.value;
                chosenQuizType.textContent = this.value.replace(/-/g, ' ').toUpperCase();
                selectedQuizTypeDiv.style.display = "block";
            }
        });
    });

    // Initialize the selected quiz type based on the hidden input
    if (quizTypeInput.value) {
        chosenQuizType.textContent = quizTypeInput.value.replace(/-/g, ' ').toUpperCase();
        selectedQuizTypeDiv.style.display = "block";
    }

    // Start Editing Quiz button functionality
    document.getElementById("startQuizBtn").addEventListener("click", function() {
        // You can add any specific functionality here if needed
        alert("Quiz editing started!");
    });
});

