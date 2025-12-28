/**
 * Deliveries Checkboxes Management
 * Handles checkbox selection and bulk actions
 */

// Toggle all deliveries checkboxes
function toggleAllDeliveries(masterCheckbox) {
    const checkboxes = document.querySelectorAll('input.delivery-checkbox:not([disabled])');
    checkboxes.forEach(checkbox => {
        checkbox.checked = masterCheckbox.checked;
    });
    updateBulkActionButton();
}

// Update bulk action button state
function updateBulkActionButton() {
    const selectedCheckboxes = document.querySelectorAll('input.delivery-checkbox:checked:not([disabled])');
    const bulkButton = document.querySelector('[onclick*="completeSelectedDeliveries"]');
    const bulkButtonParent = bulkButton?.closest('button');

    if (bulkButtonParent) {
        if (selectedCheckboxes.length > 0) {
            // Enable button
            bulkButtonParent.disabled = false;
            bulkButtonParent.classList.remove('bg-gray-200', 'dark:bg-slate-700', 'text-gray-400', 'dark:text-gray-500', 'cursor-not-allowed');
            bulkButtonParent.classList.add('bg-primary', 'hover:bg-primary/90', 'text-white', 'shadow-md', 'hover:shadow-lg');

            // Update count badge
            const countBadge = bulkButtonParent.querySelector('span.bg-white\\/20');
            if (countBadge) {
                countBadge.textContent = selectedCheckboxes.length;
                countBadge.classList.remove('hidden');
            }
        } else {
            // Disable button
            bulkButtonParent.disabled = true;
            bulkButtonParent.classList.add('bg-gray-200', 'dark:bg-slate-700', 'text-gray-400', 'dark:text-gray-500', 'cursor-not-allowed');
            bulkButtonParent.classList.remove('bg-primary', 'hover:bg-primary/90', 'text-white', 'shadow-md', 'hover:shadow-lg');
            // Hide count badge
            const countBadge = bulkButtonParent.querySelector('span.bg-white\\/20');
            if (countBadge) {
                countBadge.classList.add('hidden');
            }
        }
    }

// Update master checkbox state
    updateMasterCheckbox();
}
// Update master checkbox state (checked/indeterminate)
function updateMasterCheckbox() {
    const masterCheckbox = document.getElementById('selectAll');
    if (!masterCheckbox) return;
    const allCheckboxes = document.querySelectorAll('input.delivery-checkbox:not([disabled])');
    const checkedCheckboxes = document.querySelectorAll('input.delivery-checkbox:checked:not([disabled])');

    if (checkedCheckboxes.length === 0) {
        masterCheckbox.checked = false;
        masterCheckbox.indeterminate = false;
    } else if (checkedCheckboxes.length === allCheckboxes.length) {
        masterCheckbox.checked = true;
        masterCheckbox.indeterminate = false;
    } else {
        masterCheckbox.checked = false;
        masterCheckbox.indeterminate = true;
    }
}
// Complete selected deliveries (placeholder - to implement on backend)
function completeSelectedDeliveries() {
    const selectedCheckboxes = document.querySelectorAll('input.delivery-checkbox:checked:not([disabled])');
    const selectedIds = Array.from(selectedCheckboxes).map(cb => cb.value);
    if (selectedIds.length === 0) {
        alert('Veuillez sélectionner au moins une livraison');
        return;
    }

    if (confirm(`Voulez-vous vraiment marquer ${selectedIds.length} livraison(s) comme terminée(s) ?`)) {
        console.log('Completing deliveries:', selectedIds);
        // TODO: Implement AJAX call to backend
        // Example:
        // fetch('/deliveries/complete-bulk', {
        //     method: 'POST',
        //     headers: {'Content-Type': 'application/json'},
        //     body: JSON.stringify({ids: selectedIds})
        // }).then(response => {
        //     if (response.ok) {
        //         location.reload();
        //     }
        // });
    }
}
// Complete single delivery (placeholder - to implement on backend)
function completeDelivery(deliveryId) {
    if (confirm('Voulez-vous vraiment marquer cette livraison comme terminée ?')) {
        console.log('Completing delivery:', deliveryId);
// TODO: Implement AJAX call to backend
// Example:
// fetch(/deliveries/${deliveryId}/complete, {
//     method: 'POST'
// }).then(response => {
//     if (response.ok) {
//         location.reload();
//     }
// });
    }
}
// Initialize on page load
document.addEventListener('DOMContentLoaded', function() {
    updateBulkActionButton();
});
