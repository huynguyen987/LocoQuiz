// File: /js/competition-exam.js

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
            displayExamDetails(data);
        })
        .catch(error => {
            console.error('Error fetching competition details:', error);
            alert(`Error: ${error.message}`);
            window.location.href = `${contextPath}/jsp/student.jsp?action=Classrooms`;
        });
}

function displayExamDetails(data) {
    const examDetailsDiv = document.getElementById('exam-details');
    const startExamBtn = document.getElementById('start-exam-btn');

    // Xóa nội dung cũ
    examDetailsDiv.innerHTML = '';

    // Hiển thị chi tiết cuộc thi
    const examInfo = document.createElement('div');
    examInfo.classList.add('exam-info');

    examInfo.innerHTML = `
        <h2>${data.competitionId ? `Competition ID: ${data.competitionId}` : 'Competition'}</h2>
        <p><strong>Total Questions:</strong> ${data.totalQuestions}</p>
        <p><strong>Time Limit:</strong> ${data.timeLimit / 60} minutes</p>
    `;

    examDetailsDiv.appendChild(examInfo);

    // Hiển thị nút bắt đầu thi
    startExamBtn.style.display = 'block';
    startExamBtn.addEventListener('click', () => {
        // Chuyển hướng đến trang thi thực tế
        window.location.href = `${contextPath}/jsp/competition-exam-interface.jsp?competitionId=${data.competitionId}`;
    });
}
