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




