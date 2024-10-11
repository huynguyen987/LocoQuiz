// Theme Toggle
const darkModeToggle = document.getElementById('dark-mode-toggle');
const body = document.body;

darkModeToggle.addEventListener('change', () => {
    body.classList.toggle('dark-mode');
    localStorage.setItem('darkMode', body.classList.contains('dark-mode'));
});

// Load Theme Preference
window.addEventListener('load', () => {
    if (localStorage.getItem('darkMode') === 'true') {
        body.classList.add('dark-mode');
        darkModeToggle.checked = true;
    }
});

// Live Search Functionality
const liveSearchInput = document.getElementById('liveSearch');
const searchResults = document.getElementById('searchResults');

liveSearchInput.addEventListener('input', () => {
    const query = liveSearchInput.value.trim();

    if (query.length > 0) {
        // Simulate fetching search results
        fetch(`${contextPath}/SearchServlet?query=${encodeURIComponent(query)}`)
            .then(response => response.json())
            .then(data => {
                displaySearchResults(data);
            });
    } else {
        searchResults.style.display = 'none';
    }
});

function displaySearchResults(results) {
    searchResults.innerHTML = '';

    if (results.length > 0) {
        results.forEach(result => {
            const li = document.createElement('li');
            li.textContent = result.title;
            li.addEventListener('click', () => {
                window.location.href = `${contextPath}/jsp/quiz-details.jsp?id=${result.id}`;
            });
            searchResults.appendChild(li);
        });

        searchResults.style.display = 'block';
    } else {
        searchResults.style.display = 'none';
    }
}

// User Profile Dropdown
const userProfile = document.querySelector('.user-profile');
const profileMenu = document.querySelector('.profile-menu');

userProfile.addEventListener('click', (e) => {
    e.stopPropagation();
    profileMenu.style.display = profileMenu.style.display === 'block' ? 'none' : 'block';
});

document.addEventListener('click', () => {
    profileMenu.style.display = 'none';
});

// Notifications Dropdown
const notifications = document.querySelector('.notifications');
const notificationDropdown = document.querySelector('.notification-dropdown');

notifications.addEventListener('click', (e) => {
    e.stopPropagation();
    notificationDropdown.style.display = notificationDropdown.style.display === 'block' ? 'none' : 'block';
});

document.addEventListener('click', () => {
    notificationDropdown.style.display = 'none';
});
