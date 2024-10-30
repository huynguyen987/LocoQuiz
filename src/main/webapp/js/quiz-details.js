document.addEventListener("DOMContentLoaded", function() {
    // Mở modal khi nhấn nút Tạo Quiz
    document.getElementById("openCreateQuizModal").addEventListener("click", function() {
        document.getElementById("quizModal").style.display = "block";
    });

    // Đóng modal khi nhấn nút X
    document.querySelector(".close").addEventListener("click", function() {
        closeModal();
    });

    // Đóng modal khi nhấn ra ngoài vùng modal
    window.addEventListener("click", function(event) {
        const modal = document.getElementById("quizModal");
        if (event.target === modal) {
            closeModal();
        }
    });
});

/**
 * Hàm đóng modal
 */
function closeModal() {
    document.getElementById("quizModal").style.display = "none";
}
