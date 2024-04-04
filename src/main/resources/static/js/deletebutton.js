document.addEventListener('DOMContentLoaded', function() {
    var checkboxes = document.querySelectorAll('.checkbox-pea');
    var deleteBtn = document.getElementById('deleteBtn');

    checkboxes.forEach(function(checkbox) {
        checkbox.addEventListener('change', function() {
            var atLeastOneChecked = Array.from(checkboxes).some(function(cb) {
                return cb.checked;
            });

            deleteBtn.style.display = atLeastOneChecked ? 'block' : 'none';
        });
    });
});