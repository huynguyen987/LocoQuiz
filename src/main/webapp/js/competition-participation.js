// File: /js/competition-participation.js

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
            displayCompetitionDetails(data);
        })
        .catch(error => {
            console.error('Error fetching competition details:', error);
            alert(`Error: ${error.message}`);
            window.location.href = `${contextPath}/jsp/student.jsp?action=Classrooms`;
        });
}

function displayCompetitionDetails(data) {
    const competitionDetailsDiv = document.getElementById('competition-details');
    const startBtn = document.getElementById('start-competition-btn');

    // Xóa nội dung cũ
    competitionDetailsDiv.innerHTML = '';

    // Hiển thị chi tiết cuộc thi
    const competitionInfo = document.createElement('div');
    competitionInfo.classList.add('competition-info');

    competitionInfo.innerHTML = `
        <h2>${data.competitionId ? `Competition ID: ${data.competitionId}` : 'Competition'}</h2>
        <p><strong>Total Questions:</strong> ${data.totalQuestions}</p>
        <p><strong>Time Limit:</strong> ${data.timeLimit / 60} minutes</p>
    `;

    competitionDetailsDiv.appendChild(competitionInfo);

    // Hiển thị nút bắt đầu thi
    startBtn.style.display = 'block';
    startBtn.addEventListener('click', () => {
        // Chuyển hướng đến trang thi với competitionId
        window.location.href = `${contextPath}/jsp/competition-exam.jsp?competitionId=${data.competitionId}`;
    });
}
