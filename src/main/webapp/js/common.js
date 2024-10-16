// common.js

document.addEventListener('DOMContentLoaded', () => {
    // Theme Toggle
    const darkModeToggle = document.getElementById('dark-mode-toggle');
    const body = document.body;

    if (darkModeToggle) {
        darkModeToggle.addEventListener('change', () => {
            body.classList.toggle('dark-mode');
            localStorage.setItem('darkMode', body.classList.contains('dark-mode'));
        });

        // Load Theme Preference
        if (localStorage.getItem('darkMode') === 'true') {
            body.classList.add('dark-mode');
            darkModeToggle.checked = true;
        }
    }

    // Live Search Functionality
    const liveSearchInput = document.getElementById('liveSearch');
    const searchResults = document.getElementById('searchResults');
    const contextPath = window.contextPath || '';

    if (liveSearchInput) {
        liveSearchInput.addEventListener('input', () => {
            const query = liveSearchInput.value.trim();

            if (query.length > 0) {
                // Fetch search results
                fetch(`${contextPath}/SearchServlet?query=${encodeURIComponent(query)}`)
                    .then(response => response.json())
                    .then(data => {
                        displaySearchResults(data);
                    });
            } else {
                searchResults.style.display = 'none';
            }
        });
    }

    function displaySearchResults(results) {
        if (!searchResults) return;
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

    if (userProfile && profileMenu) {
        userProfile.addEventListener('click', (e) => {
            e.stopPropagation();
            profileMenu.style.display = profileMenu.style.display === 'block' ? 'none' : 'block';
        });

        document.addEventListener('click', () => {
            profileMenu.style.display = 'none';
        });
    }

    // Notifications Dropdown
    const notifications = document.querySelector('.notifications');
    const notificationDropdown = document.querySelector('.notification-dropdown');

    if (notifications && notificationDropdown) {
        notifications.addEventListener('click', (e) => {
            e.stopPropagation();
            notificationDropdown.style.display = notificationDropdown.style.display === 'block' ? 'none' : 'block';
        });

        document.addEventListener('click', () => {
            notificationDropdown.style.display = 'none';
        });
    }

    // Confirm before deleting class
    document.querySelectorAll('.delete-button').forEach(function(button) {
        button.addEventListener('click', function(event) {
            if (!confirm('Are you sure you want to delete this class? This action cannot be undone.')) {
                event.preventDefault();
            }
        });
    });

    // Sidebar Toggle for Mobile
    const sidebarToggle = document.querySelector('.sidebar-toggle');
    const sidebar = document.querySelector('.sidebar');

    if (sidebarToggle && sidebar) {
        sidebarToggle.addEventListener('click', () => {
            sidebar.classList.toggle('active');
        });
    }

    // Function to handle modal display based on modal ID
    function handleModal(modalId) {
        const modal = document.getElementById(modalId);
        const closeButton = modal.querySelector('.close-button');

        if (modal) {
            // Display the modal
            modal.style.display = 'flex';

            // Close the modal when the close button is clicked
            closeButton.addEventListener('click', () => {
                modal.style.display = 'none';
            });

            // Close the modal when clicking outside the modal content
            window.addEventListener('click', (event) => {
                if (event.target === modal) {
                    modal.style.display = 'none';
                }
            });
        }
    }

    // Handle Quiz Assigned Modal
    const quizAssignedModal = document.getElementById('quizAssignedModal');
    if (quizAssignedModal) {
        handleModal('quizAssignedModal');
    }

    // Handle Student Enrolled Modal
    const studentEnrolledModal = document.getElementById('studentEnrolledModal');
    if (studentEnrolledModal) {
        handleModal('studentEnrolledModal');
    }
});
