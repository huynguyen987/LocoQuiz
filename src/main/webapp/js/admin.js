// Filter users based on search input
function filterUsers() {
    // Get the search input value and convert it to uppercase for case-insensitive comparison
    var input = document.getElementById('searchUser');
    var filter = input.value.toUpperCase();
    var table = document.getElementById('userTable');
    var tr = table.getElementsByTagName('tr');

    // Loop through all table rows, excluding the header row
    for (var i = 1; i < tr.length; i++) {
        var tdUsername = tr[i].getElementsByTagName('td')[1];
        var tdRole = tr[i].getElementsByTagName('td')[2];
        if (tdUsername && tdRole) {
            var txtValueUsername = tdUsername.textContent || tdUsername.innerText;
            var txtValueRole = tdRole.textContent || tdRole.innerText;

            // Check if either Username or Role matches the filter
            if (txtValueUsername.toUpperCase().indexOf(filter) > -1 ||
                txtValueRole.toUpperCase().indexOf(filter) > -1) {
                tr[i].style.display = "";
            } else {
                tr[i].style.display = "none";
            }
        }
    }
}

function resetSearch() {
    // Clear the search input
    document.getElementById('searchUser').value = "";

    // Get the table and all its rows
    var table = document.getElementById('userTable');
    var tr = table.getElementsByTagName('tr');

    // Loop through all table rows and display them
    for (var i = 1; i < tr.length; i++) {
        tr[i].style.display = "";
    }
}


