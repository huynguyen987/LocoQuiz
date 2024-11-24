// Initialize tasks from localStorage or as empty array
let tasks = JSON.parse(localStorage.getItem('tasks')) || [];

// Initialize points from localStorage or set to 0
let points = parseInt(localStorage.getItem('points')) || 0;

// Initialize rewards from localStorage or default array
let rewards = JSON.parse(localStorage.getItem('rewards')) || [
    { id: 1, name: 'Badge Mới Bắt Đầu', requiredPoints: 10, claimed: false },
    { id: 2, name: 'Badge Tiến Bộ', requiredPoints: 20, claimed: false },
    { id: 3, name: 'Badge Xuất Sắc', requiredPoints: 30, claimed: false },
];

// Pagination variables
let currentPage = 1;
const tasksPerPage = 10;

// Initialize FullCalendar
document.addEventListener('DOMContentLoaded', function() {
    initializeCalendar();
    displayTasks();
    updateDailyProgress();
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

    document.getElementById('searchKeyword').addEventListener('input', function() {
        currentPage = 1;
        filterTasks();
    });
    document.getElementById('filterStatus').addEventListener('change', function() {
        currentPage = 1;
        filterTasks();
    });
    document.getElementById('filterPriority').addEventListener('change', function() {
        currentPage = 1;
        filterTasks();
    });
    document.getElementById('filterTimeRange').addEventListener('change', function() {
        currentPage = 1;
        filterTasks();
    });
    document.getElementById('filterDateFrom').addEventListener('change', function() {
        currentPage = 1;
        filterTasks();
    });
    document.getElementById('filterDateTo').addEventListener('change', function() {
        currentPage = 1;
        filterTasks();
    });
    document.getElementById('clearFilters').addEventListener('click', function() {
        currentPage = 1;
        clearFilters();
    });

    // Navigation tabs
    const navLinks = document.querySelectorAll('.nav-link');
    navLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            showSection(this.getAttribute('href').substring(1));
            navLinks.forEach(l => l.classList.remove('active'));
            this.classList.add('active');
        });
    });

    // Check notifications every minute
    setInterval(checkNotifications, 60000);

    // Claim reward button
    document.getElementById('claimReward').addEventListener('click', claimReward);
}

// Function to show the selected section
function showSection(sectionId) {
    const sections = document.querySelectorAll('.section');
    sections.forEach(sec => {
        sec.style.display = sec.id === sectionId ? 'block' : 'none';
    });
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
    currentPage = 1;
    displayTasks();
    updateDailyProgress();
    resetForm();
    refreshCalendar();
    updateStatistics();
}

// Function to display tasks with pagination
function displayTasks(filteredTasks = null) {
    const taskList = document.getElementById('taskList');
    taskList.innerHTML = '';

    const displayData = filteredTasks !== null ? filteredTasks : tasks;

    if (displayData.length === 0) {
        taskList.innerHTML = '<p class="text-center">Không có nhiệm vụ nào.</p>';
        document.getElementById('pagination').innerHTML = '';
        return;
    }

    const totalPages = Math.ceil(displayData.length / tasksPerPage);
    const startIndex = (currentPage - 1) * tasksPerPage;
    const endIndex = startIndex + tasksPerPage;
    const tasksToShow = displayData.slice(startIndex, endIndex);

    tasksToShow.forEach(task => {
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

    // Render pagination
    renderPagination(totalPages);
}

// Function to render pagination buttons
function renderPagination(totalPages) {
    const paginationEl = document.getElementById('pagination');
    paginationEl.innerHTML = '';

    if (totalPages <= 1) return;

    const ul = document.createElement('ul');
    ul.className = 'pagination';

    for (let i = 1; i <= totalPages; i++) {
        const li = document.createElement('li');
        li.className = `page-item ${i === currentPage ? 'active' : ''}`;
        const a = document.createElement('a');
        a.className = 'page-link';
        a.href = '#';
        a.textContent = i;
        a.addEventListener('click', function(e) {
            e.preventDefault();
            currentPage = i;
            filterTasks();
        });
        li.appendChild(a);
        ul.appendChild(li);
    }

    paginationEl.appendChild(ul);
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
        const oldStatus = task.status;
        task.status = newStatus;
        if (oldStatus !== 'Hoàn thành' && newStatus === 'Hoàn thành') {
            // Task was just completed
            points += 5; // Add 5 points for completing a task
            document.getElementById('pointsText').textContent = `Điểm của bạn: ${points}`;
            checkRewards();
        }
        saveData();
        displayTasks();
        updateDailyProgress();
        refreshCalendar();
        updateStatistics();
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
        updateDailyProgress();
        refreshCalendar();
        updateStatistics();
        checkRewards();
    }
}

// Function to update daily progress
function updateDailyProgress() {
    const today = new Date().toISOString().split('T')[0];
    const tasksToday = tasks.filter(task => task.date === today);
    const total = tasksToday.length;
    const completed = tasksToday.filter(t => t.status === 'Hoàn thành').length;
    const progressPercent = total === 0 ? 0 : Math.round((completed / total) * 100);

    const progressBar = document.getElementById('progressBar');
    progressBar.style.width = `${progressPercent}%`;
    progressBar.setAttribute('aria-valuenow', progressPercent);
    progressBar.textContent = `${progressPercent}%`;

    const progressText = document.getElementById('progressText');
    progressText.textContent = `Hôm nay: Hoàn thành ${completed}/${total} nhiệm vụ`;
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
    const timeRange = document.getElementById('filterTimeRange').value;
    const dateFrom = document.getElementById('filterDateFrom').value;
    const dateTo = document.getElementById('filterDateTo').value;

    let filtered = tasks.filter(task => {
        const matchesKeyword = task.title.toLowerCase().includes(keyword) || task.description.toLowerCase().includes(keyword);
        const matchesStatus = status === 'All' || task.status === status;
        const matchesPriority = priority === 'All' || task.priority === priority;
        const matchesTimeRange = matchesTimeRangeFilter(task.date, timeRange);
        const matchesDateRange = matchesDateRangeFilter(task.date, dateFrom, dateTo);
        return matchesKeyword && matchesStatus && matchesPriority && matchesTimeRange && matchesDateRange;
    });

    displayTasks(filtered);
}

// Function to check if task date matches the selected time range
function matchesTimeRangeFilter(taskDate, timeRange) {
    if (timeRange === 'All') return true;

    const taskDateObj = new Date(taskDate);
    const now = new Date();
    let pastDate;

    switch(timeRange) {
        case 'Today':
            return isSameDay(taskDateObj, now);
        case 'PastWeek':
            pastDate = new Date();
            pastDate.setDate(now.getDate() - 7);
            return taskDateObj >= pastDate && taskDateObj <= now;
        case 'PastMonth':
            pastDate = new Date();
            pastDate.setMonth(now.getMonth() - 1);
            return taskDateObj >= pastDate && taskDateObj <= now;
        case 'PastYear':
            pastDate = new Date();
            pastDate.setFullYear(now.getFullYear() - 1);
            return taskDateObj >= pastDate && taskDateObj <= now;
        default:
            return true;
    }
}

function isSameDay(date1, date2) {
    return date1.getDate() === date2.getDate()
        && date1.getMonth() === date2.getMonth()
        && date1.getFullYear() === date2.getFullYear();
}

// Function to check if task date falls within the selected date range
function matchesDateRangeFilter(taskDate, dateFrom, dateTo) {
    if (!dateFrom && !dateTo) return true;

    const taskDateObj = new Date(taskDate);
    if (dateFrom && !dateTo) {
        const dateFromObj = new Date(dateFrom);
        return taskDateObj >= dateFromObj;
    }
    if (!dateFrom && dateTo) {
        const dateToObj = new Date(dateTo);
        return taskDateObj <= dateToObj;
    }
    if (dateFrom && dateTo) {
        const dateFromObj = new Date(dateFrom);
        const dateToObj = new Date(dateTo);
        return taskDateObj >= dateFromObj && taskDateObj <= dateToObj;
    }
    return true;
}

// Function to clear all filters
function clearFilters() {
    document.getElementById('searchKeyword').value = '';
    document.getElementById('filterStatus').value = 'All';
    document.getElementById('filterPriority').value = 'All';
    document.getElementById('filterTimeRange').value = 'All';
    document.getElementById('filterDateFrom').value = '';
    document.getElementById('filterDateTo').value = '';
    currentPage = 1;
    displayTasks();
}

// Function to save tasks and points to localStorage
function saveData() {
    localStorage.setItem('tasks', JSON.stringify(tasks));
    localStorage.setItem('points', points.toString());
    localStorage.setItem('rewards', JSON.stringify(rewards));
    updateStatistics();
    updateRewardsUI();
}

// Statistics Initialization
function initializeStatistics() {
    const ctx = document.getElementById('statsChart').getContext('2d');
    window.statsChart = new Chart(ctx, {
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
                legend: { position: 'bottom' },
                title: { display: true, text: 'Thống Kê Nhiệm Vụ Theo Trạng Thái' }
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
    if (window.statsChart) {
        window.statsChart.data.datasets[0].data = getStatisticsData();
        window.statsChart.update();
    }
}

// Reward System
function initializeRewards() {
    updateRewardsUI();
    document.getElementById('pointsText').textContent = `Điểm của bạn: ${points}`;
}

function updateRewardsUI() {
    const rewardsContainer = document.getElementById('rewards');
    rewardsContainer.innerHTML = '';
    rewards.forEach(reward => {
        const badge = document.createElement('span');
        if (reward.claimed) {
            badge.className = 'badge bg-success me-2 mb-2';
            badge.textContent = `${reward.name} (Đã nhận)`;
        } else if (points >= reward.requiredPoints) {
            badge.className = 'badge bg-warning text-dark me-2 mb-2';
            badge.textContent = `${reward.name} (Có thể nhận)`;
        } else {
            badge.className = 'badge bg-secondary me-2 mb-2';
            badge.textContent = `${reward.name} (Cần ${reward.requiredPoints} điểm)`;
        }
        rewardsContainer.appendChild(badge);
    });
}

function claimReward() {
    const eligibleRewards = rewards.filter(reward => !reward.claimed && points >= reward.requiredPoints);
    if (eligibleRewards.length === 0) {
        alert('Bạn chưa đủ điểm để nhận phần thưởng.');
    } else {
        eligibleRewards.forEach(reward => {
            reward.claimed = true;
            showToast(`Bạn đã nhận được phần thưởng: ${reward.name}!`);
        });
        saveData();
        updateRewardsUI();
    }
}

function checkRewards() {
    const eligibleRewards = rewards.filter(reward => !reward.claimed && points >= reward.requiredPoints);
    if (eligibleRewards.length > 0) {
        eligibleRewards.forEach(reward => {
            showToast(`Bạn có thể nhận phần thưởng: ${reward.name}!`);
        });
    }
    saveData();
    updateRewardsUI();
}
