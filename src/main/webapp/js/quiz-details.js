// File: js/quiz-details.js

document.addEventListener("DOMContentLoaded", function() {
    // Tăng số lượt xem khi trang được tải
    updateViewCount();

    // Xử lý toggle hiển thị mô tả (nếu cần)
    // const toggleDesc = document.getElementById("toggleDesc");
    // const quizDescription = document.getElementById("quizDescription");

    // Kiểm tra sự tồn tại của quizCreatorContainer
    const quizCreatorContainer = document.getElementById("quizCreatorContainer");
    if (quizCreatorContainer) {
        // Khởi tạo các tính năng liên quan đến câu hỏi
        const addQuestionBtn = document.getElementById("addQuestionBtn");
        if (addQuestionBtn) {
            addQuestionBtn.addEventListener("click", function() {
                // Định nghĩa hàm addQuestion ở đây hoặc import từ tệp khác
                // Ví dụ:
                // addQuestion();
                alert("Add question functionality is not implemented yet.");
            });
        }
        // ... các sự kiện khác liên quan đến câu hỏi
    }

    // Xử lý lựa chọn loại quiz
    const quizTypeRadioButtons = document.getElementsByName("quizTypeRadio");
    const quizTypeInput = document.getElementById("quizType");
    const chosenQuizType = document.getElementById("chosenQuizType");
    const selectedQuizTypeDiv = document.getElementById("selectedQuizType");

    // Chuyển đổi NodeList thành mảng để sử dụng forEach
    Array.from(quizTypeRadioButtons).forEach(function(radio) {
        radio.addEventListener("change", function() {
            if (this.checked) {
                quizTypeInput.value = this.value;
                chosenQuizType.textContent = getQuizTypeName(this.value);
                selectedQuizTypeDiv.style.display = "block";
            }
        });
    });

    // Khởi tạo loại quiz đã chọn dựa trên giá trị input ẩn
    if (quizTypeInput.value) {
        chosenQuizType.textContent = getQuizTypeName(quizTypeInput.value);
        selectedQuizTypeDiv.style.display = "block";
    }

    // Chức năng nút Start Editing Quiz
    document.getElementById("startQuizBtn").addEventListener("click", function() {
        // Hiển thị giao diện chỉnh sửa quiz
        document.getElementById("quizCreatorContainer").style.display = 'flex';
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
                const viewCountElement = document.getElementById("viewCount");
                if (viewCountElement) {
                    viewCountElement.textContent = data.newViewCount;
                }
            } else {
                console.error("Failed to update view count.");
            }
        })
        .catch(error => console.error('Error:', error));
}

/**
 * Hàm lấy quizId từ URL
 */
function getQuizIdFromURL() {
    const params = new URLSearchParams(window.location.search);
    return params.get('id') || params.get('quizId');
}

/**
 * Hàm chuyển đổi typeId thành tên loại quiz
 */
function getQuizTypeName(typeId) {
    switch(typeId) {
        case '1':
            return 'Multiple Choice';
        case '2':
            return 'Fill in the Blank';
        case '3':
            return 'Matching';
        default:
            return 'Unknown';
    }
}
