// script.js

// Initialize tasks from localStorage or as empty array
let tasks = JSON.parse(localStorage.getItem('tasks')) || [];

// Initialize points from localStorage or set to 0
let points = parseInt(localStorage.getItem('points')) || 0;

// Define rewards based on points
const rewards = [
    { id: 1, name: 'Badge Mới Bắt Đầu', requiredPoints: 10, obtained: false },
    { id: 2, name: 'Badge Tiến Bộ', requiredPoints: 20, obtained: false },
    { id: 3, name: 'Badge Xuất Sắc', requiredPoints: 30, obtained: false },
];

// Initialize FullCalendar
document.addEventListener('DOMContentLoaded', function() {
    initializeCalendar();
    displayTasks();
    updateProgress();
    setupEventListeners();
    initializeStatistics();
    initializeRewards();
    checkNotifications(); // Initial check
});

// Function to initialize FullCalendar
function initializeCalendar() {
    const calendarEl = document.getElementById('calendar');
    window.calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        locale: 'vi',
        headerToolbar: {
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek,timeGridDay'
        },
        events: tasks.map(task => ({
            id: task.id,
            title: task.title,
            start: `${task.date}T${task.time}`,
            color: getPriorityColor(task.priority),
            extendedProps: {
                description: task.description,
                status: task.status
            }
        })),
        eventClick: function(info) {
            const event = info.event;
            alert(`Tiêu đề: ${event.title}\nMô tả: ${event.extendedProps.description}\nTrạng thái: ${event.extendedProps.status}`);
        }
    });
    calendar.render();
}

// Function to get color based on priority
function getPriorityColor(priority) {
    switch(priority) {
        case 'Cao':
            return '#dc3545'; // Red
        case 'Trung bình':
            return '#ffc107'; // Yellow
        case 'Thấp':
            return '#198754'; // Green
        default:
            return '#6c757d'; // Gray
    }
}

// Function to set up event listeners
function setupEventListeners() {
    document.getElementById('taskForm').addEventListener('submit', function(e) {
        e.preventDefault();
        addTask();
    });

    document.getElementById('searchKeyword').addEventListener('input', filterTasks);
    document.getElementById('filterStatus').addEventListener('change', filterTasks);
    document.getElementById('filterPriority').addEventListener('change', filterTasks);
    document.getElementById('clearFilters').addEventListener('click', clearFilters);

    // Check notifications every minute
    setInterval(checkNotifications, 60000);

    // Claim reward button
    document.getElementById('claimReward').addEventListener('click', claimReward);
}

// Function to add a new task
function addTask() {
    const title = document.getElementById('title').value.trim();
    const description = document.getElementById('description').value.trim();
    const date = document.getElementById('date').value;
    const time = document.getElementById('time').value;
    const priority = document.getElementById('priority').value;

    if (!title || !date || !time) {
        alert('Vui lòng nhập đầy đủ các trường bắt buộc.');
        return;
    }

    const task = {
        id: Date.now(),
        title,
        description,
        date,
        time,
        priority,
        status: 'Chưa làm',
        notifiedOneHour: false,
        notifiedFiveMinutes: false
    };

    tasks.push(task);
    saveData();
    displayTasks();
    updateProgress();
    resetForm();
    refreshCalendar();
    updateStatistics();
}

// Function to display tasks
function displayTasks(filteredTasks = null) {
    const taskList = document.getElementById('taskList');
    taskList.innerHTML = '';

    const displayData = filteredTasks !== null ? filteredTasks : tasks;

    if (displayData.length === 0) {
        taskList.innerHTML = '<p class="text-center">Không có nhiệm vụ nào.</p>';
        return;
    }

    displayData.forEach(task => {
        const taskItem = document.createElement('div');
        taskItem.className = `list-group-item task-item ${getStatusClass(task.status)}`;

        taskItem.innerHTML = `
            <div class="d-flex w-100 justify-content-between">
                <h5 class="mb-1">${task.title} <span class="badge bg-${getPriorityBadge(task.priority)}">${task.priority}</span></h5>
                <small>${formatDate(task.date)} ${task.time}</small>
            </div>
            <p class="mb-1">${task.description}</p>
            <div class="d-flex align-items-center">
                <label for="statusSelect-${task.id}" class="form-label me-2 mb-0">Trạng thái:</label>
                <select class="form-select form-select-sm" id="statusSelect-${task.id}" onchange="updateStatus(${task.id}, this.value)" style="width: 150px;">
                    <option value="Chưa làm" ${task.status === 'Chưa làm' ? 'selected' : ''}>Chưa làm</option>
                    <option value="Đang làm" ${task.status === 'Đang làm' ? 'selected' : ''}>Đang làm</option>
                    <option value="Hoàn thành" ${task.status === 'Hoàn thành' ? 'selected' : ''}>Hoàn thành</option>
                </select>
                <button class="btn btn-sm btn-outline-danger ms-auto" onclick="deleteTask(${task.id})">Xóa</button>
            </div>
        `;

        taskList.appendChild(taskItem);
    });
}

// Function to get badge color based on priority
function getPriorityBadge(priority) {
    switch(priority) {
        case 'Cao':
            return 'danger';
        case 'Trung bình':
            return 'warning';
        case 'Thấp':
            return 'success';
        default:
            return 'secondary';
    }
}

// Function to format date (dd/mm/yyyy)
function formatDate(dateStr) {
    const [year, month, day] = dateStr.split('-');
    return `${day}/${month}/${year}`;
}

// Function to get status class
function getStatusClass(status) {
    switch(status) {
        case 'Hoàn thành':
            return 'completed';
        case 'Đang làm':
            return 'in-progress';
        default:
            return '';
    }
}

// Function to update task status
function updateStatus(taskId, newStatus) {
    const task = tasks.find(t => t.id === taskId);
    if (!task) return;

    if (['Chưa làm', 'Đang làm', 'Hoàn thành'].includes(newStatus)) {
        task.status = newStatus;
        saveData();
        displayTasks();
        updateProgress();
        refreshCalendar();
        updateStatistics();
        checkRewards();
    } else {
        alert('Trạng thái không hợp lệ.');
    }
}

// Function to delete a task
function deleteTask(taskId) {
    if (confirm('Bạn có chắc chắn muốn xóa nhiệm vụ này?')) {
        tasks = tasks.filter(t => t.id !== taskId);
        saveData();
        displayTasks();
        updateProgress();
        refreshCalendar();
        updateStatistics();
        checkRewards();
    }
}

// Function to update progress bar
function updateProgress() {
    const total = tasks.length;
    const completed = tasks.filter(t => t.status === 'Hoàn thành').length;
    const progressPercent = total === 0 ? 0 : Math.round((completed / total) * 100);

    const progressBar = document.getElementById('progressBar');
    progressBar.style.width = `${progressPercent}%`;
    progressBar.setAttribute('aria-valuenow', progressPercent);
    progressBar.textContent = `${progressPercent}%`;

    const progressText = document.getElementById('progressText');
    progressText.textContent = `Hoàn thành ${completed}/${total} nhiệm vụ`;
}

// Function to reset the task creation form
function resetForm() {
    document.getElementById('taskForm').reset();
}

// Function to refresh FullCalendar events
function refreshCalendar() {
    window.calendar.removeAllEvents();
    window.calendar.addEventSource(tasks.map(task => ({
        id: task.id,
        title: task.title,
        start: `${task.date}T${task.time}`,
        color: getPriorityColor(task.priority),
        extendedProps: {
            description: task.description,
            status: task.status
        }
    })));
}

// Function to check for upcoming tasks and notify
function checkNotifications() {
    const now = new Date();
    tasks.forEach(task => {
        if (task.status !== 'Hoàn thành') {
            const taskDateTime = new Date(`${task.date}T${task.time}`);
            const timeDiff = taskDateTime - now;
            const oneHour = 60 * 60 * 1000;
            const fiveMinutes = 5 * 60 * 1000;

            // Notify 1 hour before
            if (timeDiff > 0 && timeDiff <= oneHour && !task.notifiedOneHour) {
                showToast(`Nhiệm vụ "${task.title}" sẽ đến hạn trong 1 giờ.`);
                task.notifiedOneHour = true;
                saveData();
            }

            // Notify 5 minutes before
            if (timeDiff > 0 && timeDiff <= fiveMinutes && !task.notifiedFiveMinutes) {
                showToast(`Nhiệm vụ "${task.title}" sẽ đến hạn trong 5 phút.`);
                task.notifiedFiveMinutes = true;
                saveData();
            }
        }
    });
}

// Function to show toast notifications
function showToast(message) {
    // Create toast container if not exists
    let toastContainer = document.getElementById('toastContainer');
    if (!toastContainer) {
        toastContainer = document.createElement('div');
        toastContainer.id = 'toastContainer';
        toastContainer.className = 'toast-container position-fixed bottom-0 end-0 p-3';
        document.body.appendChild(toastContainer);
    }

    // Create toast element
    const toastEl = document.createElement('div');
    toastEl.className = 'toast align-items-center text-bg-primary border-0';
    toastEl.setAttribute('role', 'alert');
    toastEl.setAttribute('aria-live', 'assertive');
    toastEl.setAttribute('aria-atomic', 'true');
    toastEl.innerHTML = `
        <div class="d-flex">
            <div class="toast-body">
                ${message}
            </div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
    `;
    toastContainer.appendChild(toastEl);

    // Initialize and show the toast
    const toast = new bootstrap.Toast(toastEl, { delay: 5000 });
    toast.show();

    // Remove the toast element after it hides
    toastEl.addEventListener('hidden.bs.toast', () => {
        toastEl.remove();
    });
}

// Function to filter tasks based on search and filters
function filterTasks() {
    const keyword = document.getElementById('searchKeyword').value.toLowerCase();
    const status = document.getElementById('filterStatus').value;
    const priority = document.getElementById('filterPriority').value;

    let filtered = tasks.filter(task => {
        const matchesKeyword = task.title.toLowerCase().includes(keyword) || task.description.toLowerCase().includes(keyword);
        const matchesStatus = status === 'All' || task.status === status;
        const matchesPriority = priority === 'All' || task.priority === priority;
        return matchesKeyword && matchesStatus && matchesPriority;
    });

    displayTasks(filtered);
}

// Function to clear all filters
function clearFilters() {
    document.getElementById('searchKeyword').value = '';
    document.getElementById('filterStatus').value = 'All';
    document.getElementById('filterPriority').value = 'All';
    displayTasks();
}

// Function to save tasks and points to localStorage
function saveData() {
    localStorage.setItem('tasks', JSON.stringify(tasks));
    localStorage.setItem('points', points.toString());
    updateStatistics();
    updateRewardsUI();
}

// Statistics Initialization
let statsChart;

function initializeStatistics() {
    const ctx = document.getElementById('statsChart').getContext('2d');
    statsChart = new Chart(ctx, {
        type: 'pie',
        data: {
            labels: ['Chưa làm', 'Đang làm', 'Hoàn thành'],
            datasets: [{
                label: 'Thống Kê Nhiệm Vụ',
                data: getStatisticsData(),
                backgroundColor: [
                    '#6c757d',
                    '#ffc107',
                    '#198754'
                ],
                hoverOffset: 4
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'bottom',
                },
                title: {
                    display: true,
                    text: 'Thống Kê Nhiệm Vụ'
                }
            }
        }
    });
    updateStatistics();
}

// Function to get statistics data
function getStatisticsData() {
    const notDone = tasks.filter(t => t.status === 'Chưa làm').length;
    const inProgress = tasks.filter(t => t.status === 'Đang làm').length;
    const completed = tasks.filter(t => t.status === 'Hoàn thành').length;
    return [notDone, inProgress, completed];
}

// Function to update statistics chart
function updateStatistics() {
    if (statsChart) {
        statsChart.data.datasets[0].data = getStatisticsData();
        statsChart.update();
    }
}

// Reward System Initialization
function initializeRewards() {
    const rewardsContainer = document.getElementById('rewards');
    rewardsContainer.innerHTML = '';
    rewards.forEach(reward => {
        const badge = document.createElement('span');
        badge.className = `badge ${reward.obtained ? 'bg-success unlocked' : 'bg-secondary locked'}`;
        badge.textContent = reward.name;
        if (!reward.obtained) {
            badge.title = `Cần ${reward.requiredPoints} điểm để mở khóa`;
        } else {
            badge.title = `Đã mở khóa`;
        }
        rewardsContainer.appendChild(badge);
    });
    updateRewardsUI();
}

// Function to update rewards UI
function updateRewardsUI() {
    const rewardsContainer = document.getElementById('rewards');
    rewardsContainer.innerHTML = '';
    rewards.forEach(reward => {
        const badge = document.createElement('span');
        if (reward.obtained) {
            badge.className = 'badge bg-success me-2 mb-2';
            badge.textContent = `${reward.name} (Đã mở khóa)`;
        } else if (points >= reward.requiredPoints) {
            badge.className = 'badge bg-warning text-dark me-2 mb-2';
            badge.textContent = `${reward.name} (Có thể mở khóa)`;
        } else {
            badge.className = 'badge bg-secondary me-2 mb-2';
            badge.textContent = `${reward.name} (Cần ${reward.requiredPoints} điểm)`;
        }
        rewardsContainer.appendChild(badge);
    });
}

// Function to check and update rewards based on points
function checkRewards() {
    rewards.forEach(reward => {
        if (!reward.obtained && points >= reward.requiredPoints) {
            reward.obtained = true;
            saveData();
            showToast(`Bạn đã mở khóa phần thưởng: ${reward.name}!`);
        }
    });
    updateRewardsUI();
}

// Function to claim reward
function claimReward() {
    // For simplicity, claiming a reward just displays a message
    const availableRewards = rewards.filter(r => r.obtained && !r.claimed);
    if (availableRewards.length === 0) {
        alert('Không có phần thưởng nào để nhận.');
        return;
    }
    availableRewards.forEach(reward => {
        alert(`Bạn đã nhận được phần thưởng: ${reward.name}!`);
        // Mark as claimed if you want to implement claimed status
    });
}

// Function to check and update points based on completed tasks
function checkRewards() {
    const completedTasks = tasks.filter(t => t.status === 'Hoàn thành').length;
    points = completedTasks * 5; // Example: 5 points per completed task
    localStorage.setItem('points', points.toString());
    document.getElementById('pointsText').textContent = `Điểm của bạn: ${points}`;

    rewards.forEach(reward => {
        if (!reward.obtained && points >= reward.requiredPoints) {
            reward.obtained = true;
            showToast(`Bạn đã mở khóa phần thưởng: ${reward.name}!`);
        }
    });
    saveData();
    updateRewardsUI();
}

// Function to initialize points and rewards UI
function initializeRewards() {
    const rewardsContainer = document.getElementById('rewards');
    rewardsContainer.innerHTML = '';
    rewards.forEach(reward => {
        const badge = document.createElement('span');
        if (reward.obtained) {
            badge.className = 'badge bg-success me-2 mb-2';
            badge.textContent = `${reward.name} (Đã mở khóa)`;
        } else if (points >= reward.requiredPoints) {
            badge.className = 'badge bg-warning text-dark me-2 mb-2';
            badge.textContent = `${reward.name} (Có thể mở khóa)`;
        } else {
            badge.className = 'badge bg-secondary me-2 mb-2';
            badge.textContent = `${reward.name} (Cần ${reward.requiredPoints} điểm)`;
        }
        rewardsContainer.appendChild(badge);
    });
    document.getElementById('pointsText').textContent = `Điểm của bạn: ${points}`;
}

// Function to initialize statistics and rewards on load
function initializeStatisticsAndRewards() {
    initializeStatistics();
    initializeRewards();
    updateStatistics();
    updateRewardsUI();
}

// Initial setup
function initializeSetup() {
    initializeStatistics();
    initializeRewards();
    updateStatistics();
    updateRewardsUI();
}

// Call initial setup
initializeSetup();
