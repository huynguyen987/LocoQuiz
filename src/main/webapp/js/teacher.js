// Confirm before navigating to delete class
document.querySelectorAll('.class-actions a[href*="DeleteClassServlet"]').forEach(function(link) {
    link.addEventListener('click', function(event) {
        if (!confirm('Are you sure you want to delete this class? This action cannot be undone.')) {
            event.preventDefault();
        }
    });
});
