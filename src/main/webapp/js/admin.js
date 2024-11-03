// Filter users based on search input
function filterUsers() {
    const searchInput = document.getElementById('searchUser').value.toLowerCase();
    const rows = document.querySelectorAll('tbody tr');

    rows.forEach(row => {
        const name = row.cells[1].textContent.toLowerCase();
        const role = row.cells[2].textContent.toLowerCase();
        row.style.display = (name.includes(searchInput) || role.includes(searchInput)) ? '' : 'none';
    });
}

// Reset search filter
function resetSearch() {
    document.getElementById('searchUser').value = '';
    filterUsers();
}

// Update user role (stub function for backend integration)
function updateRole(userId) {
    alert(`Update role for User ID: ${userId}`);
    // AJAX call to update role on the server
}

// Toggle user status (stub function for backend integration)
function toggleStatus(userId) {
    alert(`Toggle status for User ID: ${userId}`);
    // AJAX call to toggle status on the server
}

// Delete user (stub function for backend integration)
function deleteUser(userId) {
    if (confirm(`Are you sure you want to delete User ID: ${userId}?`)) {
        alert(`User ID: ${userId} deleted.`);
        // AJAX call to delete the user on the server
    }
}
