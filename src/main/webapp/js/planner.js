// Initialize tasks from localStorage or as empty array
let tasks = [];
try {
    tasks = JSON.parse(localStorage.getItem('tasks')) || [];
    if (!Array.isArray(tasks)) {
        tasks = [];
    }
} catch (e) {
    tasks = [];
}

// Initialize points from localStorage or set to 0
let points = parseInt(localStorage.getItem('points')) || 0;

// Default rewards array
const defaultRewards = [
    { id: 1, name: 'Badge Mới Bắt Đầu', requiredPoints: 10, claimed: false },
    { id: 2, name: 'Badge Tiến Bộ', requiredPoints: 20, claimed: false },
    { id: 3, name: 'Badge Chăm Chỉ', requiredPoints: 50, claimed: false },
    { id: 4, name: 'Badge Chuyên Cần', requiredPoints: 100, claimed: false },
    { id: 5, name: 'Badge Siêu Sao', requiredPoints: 200, claimed: false },
    { id: 6, name: 'Badge Huyền Thoại', requiredPoints: 300, claimed: false },
    { id: 7, name: 'Badge Bậc Thầy', requiredPoints: 400, claimed: false },
    { id: 8, name: 'Badge Đỉnh Cao', requiredPoints: 500, claimed: false },
    { id: 9, name: 'Badge Vô Địch', requiredPoints: 600, claimed: false },
    { id: 10, name: 'Badge Siêu Việt', requiredPoints: 700, claimed: false },
    { id: 11, name: 'Badge Huyền Thoại Sống', requiredPoints: 800, claimed: false },
    { id: 12, name: 'Badge Bất Bại', requiredPoints: 900, claimed: false },
    { id: 13, name: 'Badge Thần Thoại', requiredPoints: 1000, claimed: false },
    { id: 14, name: 'Badge Huyền Thoại Bất Tử', requiredPoints: 1200, claimed: false },
    { id: 15, name: 'Badge Vĩ Đại Nhất', requiredPoints: 1500, claimed: false },
];

// Function to initialize rewards
function initializeRewardsData() {
    const storedRewards = JSON.parse(localStorage.getItem('rewards'));

    if (storedRewards) {
        // Merge storedRewards with defaultRewards
        const rewardsMap = {};
        storedRewards.forEach(r => rewardsMap[r.id] = r);

        defaultRewards.forEach(dr => {
            if (rewardsMap[dr.id]) {
                dr.claimed = rewardsMap[dr.id].claimed;
            }
        });
    }
    // Save the merged rewards to localStorage
    localStorage.setItem('rewards', JSON.stringify(defaultRewards));
    return defaultRewards;
}

// Initialize rewards
let rewards = initializeRewardsData();

// Pagination variables
let currentPage = 1;
const tasksPerPage = 10;

// Check if tasks are empty and populate with sample tasks
if (tasks.length === 0) {
    populateSampleTasks();
    saveData();
}

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

// Function to populate sample tasks
function populateSampleTasks() {
    const sampleTasks = [
        // Past tasks
        { title: 'Ôn tập Toán', description: 'Ôn tập chương 1', date: getRelativeDate(-10), time: '09:00', priority: 'Cao', status: 'Hoàn thành' },
        { title: 'Đọc sách Văn', description: 'Đọc tác phẩm "Chí Phèo"', date: getRelativeDate(-8), time: '14:00', priority: 'Trung bình', status: 'Hoàn thành' },
        { title: 'Học Tiếng Anh', description: 'Học từ vựng Unit 2', date: getRelativeDate(-7), time: '16:00', priority: 'Thấp', status: 'Hoàn thành' },
        { title: 'Làm bài tập Lý', description: 'Giải bài tập chương 3', date: getRelativeDate(-5), time: '10:00', priority: 'Cao', status: 'Hoàn thành' },
        { title: 'Thực hành Hóa', description: 'Làm thí nghiệm về axit-bazơ', date: getRelativeDate(-3), time: '13:00', priority: 'Trung bình', status: 'Hoàn thành' },
        { title: 'Ôn tập Sinh', description: 'Ôn tập di truyền học', date: getRelativeDate(-2), time: '15:00', priority: 'Thấp', status: 'Hoàn thành' },
        // Today's tasks
        { title: 'Học Lịch Sử', description: 'Chiến tranh thế giới thứ 2', date: getRelativeDate(0), time: '09:00', priority: 'Cao', status: 'Đang làm' },
        { title: 'Thể dục', description: 'Chạy bộ 5km', date: getRelativeDate(0), time: '17:00', priority: 'Trung bình', status: 'Chưa làm' },
        { title: 'Học Địa Lý', description: 'Khí hậu nhiệt đới gió mùa', date: getRelativeDate(0), time: '14:00', priority: 'Thấp', status: 'Chưa làm' },
        // Future tasks
        { title: 'Làm bài tập Toán', description: 'Giải hệ phương trình', date: getRelativeDate(1), time: '10:00', priority: 'Cao', status: 'Chưa làm' },
        { title: 'Đọc sách Văn', description: 'Đọc tác phẩm "Số Đỏ"', date: getRelativeDate(2), time: '15:00', priority: 'Trung bình', status: 'Chưa làm' },
        { title: 'Học Tiếng Anh', description: 'Ngữ pháp thì hiện tại hoàn thành', date: getRelativeDate(3), time: '16:00', priority: 'Thấp', status: 'Chưa làm' },
        { title: 'Làm bài tập Lý', description: 'Bài tập về điện trường', date: getRelativeDate(4), time: '09:00', priority: 'Cao', status: 'Chưa làm' },
        { title: 'Thực hành Hóa', description: 'Thí nghiệm về oxi hóa khử', date: getRelativeDate(5), time: '13:00', priority: 'Trung bình', status: 'Chưa làm' },
        { title: 'Ôn tập Sinh', description: 'Sinh học phân tử', date: getRelativeDate(6), time: '15:00', priority: 'Thấp', status: 'Chưa làm' },
        { title: 'Học Lịch Sử', description: 'Cách mạng tháng Tám', date: getRelativeDate(7), time: '09:00', priority: 'Cao', status: 'Chưa làm' },
        { title: 'Thể dục', description: 'Bơi lội', date: getRelativeDate(8), time: '17:00', priority: 'Trung bình', status: 'Chưa làm' },
        { title: 'Học Địa Lý', description: 'Địa lý các châu lục', date: getRelativeDate(9), time: '14:00', priority: 'Thấp', status: 'Chưa làm' },
        // Multiple tasks on the same day
        { title: 'Ôn tập Toán nâng cao', description: 'Giải bất phương trình', date: getRelativeDate(1), time: '11:00', priority: 'Cao', status: 'Chưa làm' },
        { title: 'Học Tiếng Anh giao tiếp', description: 'Chủ đề du lịch', date: getRelativeDate(2), time: '16:30', priority: 'Trung bình', status: 'Chưa làm' },
        { title: 'Đọc sách', description: 'Đọc "Đắc Nhân Tâm"', date: getRelativeDate(0), time: '20:00', priority: 'Thấp', status: 'Chưa làm' },
        { title: 'Làm bài tập Hóa', description: 'Bài tập hữu cơ', date: getRelativeDate(3), time: '14:00', priority: 'Cao', status: 'Chưa làm' },
        { title: 'Ôn tập Lý', description: 'Động lực học', date: getRelativeDate(4), time: '09:30', priority: 'Trung bình', status: 'Chưa làm' },
        { title: 'Thực hành Sinh', description: 'Quan sát tế bào', date: getRelativeDate(5), time: '13:30', priority: 'Thấp', status: 'Chưa làm' },
        { title: 'Học Lịch Sử', description: 'Chiến tranh lạnh', date: getRelativeDate(6), time: '10:00', priority: 'Cao', status: 'Chưa làm' },
        { title: 'Thể dục', description: 'Yoga', date: getRelativeDate(7), time: '18:00', priority: 'Trung bình', status: 'Chưa làm' },
    ];

    // Assign unique IDs and notification flags
    sampleTasks.forEach(task => {
        task.id = Date.now() + Math.floor(Math.random() * 100000);
        task.notifiedOneHour = false;
        task.notifiedFiveMinutes = false;
    });

    tasks = sampleTasks;
}

// Helper function to get date relative to today
function getRelativeDate(offset) {
    const date = new Date();
    date.setDate(date.getDate() + offset);
    return date.toISOString().split('T')[0];
}

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
                status: task.status,
                priority: task.priority
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

    // Delete All Tasks Button
    document.getElementById('deleteAllTasks').addEventListener('click', deleteAllTasks);

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

    // Reset rewards button
    document.getElementById('resetRewards').addEventListener('click', resetRewards);

    // Download Calendar button
    document.getElementById('downloadCalendar').addEventListener('click', function() {
        exportCalendarToExcel();
    });

    // Statistics controls
    document.getElementById('statsTimeRange').addEventListener('change', function() {
        const timeRange = this.value;
        if (timeRange === 'Custom') {
            document.getElementById('customDateRange').style.display = 'block';
        } else {
            document.getElementById('customDateRange').style.display = 'none';
            updateStatistics();
        }
    });

    document.getElementById('statsDateFrom').addEventListener('change', updateStatistics);
    document.getElementById('statsDateTo').addEventListener('change', updateStatistics);
    document.getElementById('chartType').addEventListener('change', updateStatistics);
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
    showToast('Nhiệm vụ đã được thêm thành công.', 'success');
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

// Function to delete all tasks
function deleteAllTasks() {
    if (confirm('Bạn có chắc muốn xóa hết nhiệm vụ không?')) {
        tasks = [];
        saveData();
        currentPage = 1;
        displayTasks();
        updateDailyProgress();
        refreshCalendar();
        updateStatistics();
        showToast('Tất cả nhiệm vụ đã được xóa.', 'success');
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
            status: task.status,
            priority: task.priority
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
                showToast(`Nhiệm vụ "${task.title}" sẽ đến hạn trong 1 giờ.`, 'warning');
                task.notifiedOneHour = true;
                saveData();
            }

            // Notify 5 minutes before
            if (timeDiff > 0 && timeDiff <= fiveMinutes && !task.notifiedFiveMinutes) {
                showToast(`Nhiệm vụ "${task.title}" sẽ đến hạn trong 5 phút.`, 'warning');
                task.notifiedFiveMinutes = true;
                saveData();
            }
        }
    });
}

// Function to show toast notifications
function showToast(message, type = 'info') {
    // Create toast container if not exists
    let toastContainer = document.getElementById('toastContainer');
    if (!toastContainer) {
        toastContainer = document.createElement('div');
        toastContainer.id = 'toastContainer';
        toastContainer.className = 'toast-container position-fixed bottom-0 end-0 p-3';
        document.body.appendChild(toastContainer);
    }

    // Determine the appropriate Bootstrap classes based on the type
    let toastClass = 'text-bg-primary'; // default is 'info' (blue)
    if (type === 'success') {
        toastClass = 'text-bg-success';
    } else if (type === 'warning') {
        toastClass = 'text-bg-warning';
    } else if (type === 'error') {
        toastClass = 'text-bg-danger';
    }

    // Create toast element
    const toastEl = document.createElement('div');
    toastEl.className = `toast align-items-center ${toastClass} border-0`;
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
    window.statsChart = null;
    updateStatistics();
}

// Function to get statistics data
function getStatisticsData(tasksList, chartType) {
    if (chartType === 'pie' || chartType === 'bar') {
        const notDone = tasksList.filter(t => t.status === 'Chưa làm').length;
        const inProgress = tasksList.filter(t => t.status === 'Đang làm').length;
        const completed = tasksList.filter(t => t.status === 'Hoàn thành').length;

        return {
            labels: ['Chưa làm', 'Đang làm', 'Hoàn thành'],
            datasets: [{
                label: 'Thống Kê Nhiệm Vụ',
                data: [notDone, inProgress, completed],
                backgroundColor: [
                    '#6c757d',
                    '#ffc107',
                    '#198754'
                ],
                hoverOffset: 4
            }]
        };
    } else if (chartType === 'line') {
        // For line chart, show tasks statuses over time
        const dateStatusCounts = {};

        tasksList.forEach(task => {
            const date = task.date;
            if (!dateStatusCounts[date]) {
                dateStatusCounts[date] = { 'Chưa làm': 0, 'Đang làm': 0, 'Hoàn thành': 0 };
            }
            dateStatusCounts[date][task.status]++;
        });

        const labels = Object.keys(dateStatusCounts).sort();
        const notDoneData = labels.map(date => dateStatusCounts[date]['Chưa làm']);
        const inProgressData = labels.map(date => dateStatusCounts[date]['Đang làm']);
        const completedData = labels.map(date => dateStatusCounts[date]['Hoàn thành']);

        return {
            labels: labels,
            datasets: [
                {
                    label: 'Chưa làm',
                    data: notDoneData,
                    fill: false,
                    borderColor: '#6c757d',
                    tension: 0.1
                },
                {
                    label: 'Đang làm',
                    data: inProgressData,
                    fill: false,
                    borderColor: '#ffc107',
                    tension: 0.1
                },
                {
                    label: 'Hoàn thành',
                    data: completedData,
                    fill: false,
                    borderColor: '#198754',
                    tension: 0.1
                }
            ]
        };
    }
}

// Function to update statistics chart
function updateStatistics() {
    const timeRange = document.getElementById('statsTimeRange').value;
    let filteredTasks = filterTasksByTimeRange(tasks, timeRange);

    // If custom date range is selected
    if (timeRange === 'Custom') {
        const dateFrom = document.getElementById('statsDateFrom').value;
        const dateTo = document.getElementById('statsDateTo').value;
        filteredTasks = filterTasksByCustomDateRange(filteredTasks, dateFrom, dateTo);
    }

    const chartType = document.getElementById('chartType').value;

    // Update summary statistics
    const totalTasks = filteredTasks.length;
    const completedTasks = filteredTasks.filter(t => t.status === 'Hoàn thành').length;
    const pendingTasks = totalTasks - completedTasks;

    document.getElementById('totalTasks').textContent = totalTasks;
    document.getElementById('completedTasks').textContent = completedTasks;
    document.getElementById('pendingTasks').textContent = pendingTasks;

    // Get data for chart
    const chartData = getStatisticsData(filteredTasks, chartType);

    // If chart already exists, destroy it before creating a new one
    if (window.statsChart) {
        window.statsChart.destroy();
    }

    // Create new chart
    const ctx = document.getElementById('statsChart').getContext('2d');
    window.statsChart = new Chart(ctx, {
        type: chartType,
        data: chartData,
        options: getChartOptions(chartType)
    });
}

// Function to get chart options based on chart type
function getChartOptions(chartType) {
    let options = {
        responsive: true,
        plugins: {
            legend: { position: 'bottom' },
            title: { display: true, text: 'Thống Kê Nhiệm Vụ' }
        }
    };
    if (chartType === 'line' || chartType === 'bar') {
        options.scales = {
            y: {
                beginAtZero: true,
                precision: 0
            }
        };
    }
    return options;
}

// Function to filter tasks by time range
function filterTasksByTimeRange(tasksList, timeRange) {
    const now = new Date();
    let filteredTasks = tasksList;

    switch(timeRange) {
        case 'Today':
            filteredTasks = tasksList.filter(task => isSameDay(new Date(task.date), now));
            break;
        case 'PastWeek':
            const weekStart = new Date(now);
            weekStart.setDate(now.getDate() - 7);
            filteredTasks = tasksList.filter(task => new Date(task.date) >= weekStart && new Date(task.date) <= now);
            break;
        case 'PastMonth':
            const monthStart = new Date(now.getFullYear(), now.getMonth(), 1);
            filteredTasks = tasksList.filter(task => new Date(task.date) >= monthStart && new Date(task.date) <= now);
            break;
        case 'All':
        default:
            // No filtering
            break;
    }
    return filteredTasks;
}

// Function to filter tasks by custom date range
function filterTasksByCustomDateRange(tasksList, dateFrom, dateTo) {
    if (!dateFrom && !dateTo) return tasksList;

    const dateFromObj = dateFrom ? new Date(dateFrom) : null;
    const dateToObj = dateTo ? new Date(dateTo) : null;

    return tasksList.filter(task => {
        const taskDate = new Date(task.date);
        if (dateFromObj && dateToObj) {
            return taskDate >= dateFromObj && taskDate <= dateToObj;
        } else if (dateFromObj) {
            return taskDate >= dateFromObj;
        } else if (dateToObj) {
            return taskDate <= dateToObj;
        } else {
            return true;
        }
    });
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
            showToast(`Bạn đã nhận được phần thưởng: ${reward.name}!`, 'success');
        });
        saveData();
        updateRewardsUI();
    }
}

function resetRewards() {
    if (confirm('Bạn có chắc chắn muốn reset toàn bộ phần thưởng không?')) {
        // Reset points to 0
        points = 0;
        document.getElementById('pointsText').textContent = `Điểm của bạn: ${points}`;

        // Reset claimed status of rewards
        rewards.forEach(reward => {
            reward.claimed = false;
        });

        saveData();
        updateRewardsUI();
        showToast('Phần thưởng đã được reset.', 'success');
    }
}

function checkRewards() {
    const eligibleRewards = rewards.filter(reward => !reward.claimed && points >= reward.requiredPoints);
    if (eligibleRewards.length > 0) {
        eligibleRewards.forEach(reward => {
            showToast(`Bạn có thể nhận phần thưởng: ${reward.name}!`, 'info');
        });
    }
    saveData();
    updateRewardsUI();
}

// Function to export calendar events to Excel
function exportCalendarToExcel() {
    // Get events from FullCalendar
    const events = window.calendar.getEvents();

    // Map events to an array of objects
    const data = events.map(event => ({
        'Tiêu đề': event.title,
        'Mô tả': event.extendedProps.description,
        'Ngày giờ bắt đầu': event.start ? event.start.toLocaleString() : '',
        'Ngày giờ kết thúc': event.end ? event.end.toLocaleString() : '',
        'Trạng thái': event.extendedProps.status,
        'Độ ưu tiên': event.extendedProps.priority
    }));

    // Create worksheet
    const worksheet = XLSX.utils.json_to_sheet(data);

    // Create workbook and add the worksheet
    const workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, 'Lịch Học');

    // Generate binary string from workbook
    const wbout = XLSX.write(workbook, { bookType: 'xlsx', type: 'binary' });

    // Save the file
    saveAs(new Blob([s2ab(wbout)], { type: "application/octet-stream" }), 'lich_hoc.xlsx');
}

// Helper function to convert string to ArrayBuffer
function s2ab(s) {
    const buf = new ArrayBuffer(s.length); //convert s to arrayBuffer
    const view = new Uint8Array(buf);
    for (let i=0; i<s.length; i++) {
        view[i] = s.charCodeAt(i) & 0xFF;
    }
    return buf;
}
