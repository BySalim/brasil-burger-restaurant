/**
 * Gestion des livraisons :  sélection multiple et actions bulk
 */

/**
 * Met à jour le bouton d'action bulk selon les checkboxes cochées
 */
function updateBulkActionButton() {
    const checkboxes = document.querySelectorAll('.delivery-checkbox:checked:not(:disabled)');
    const button = document.getElementById('bulkCompleteButton');
    const countBadge = document.getElementById('bulkActionCount');
    const hiddenInput = document.getElementById('bulkSelectedIds');

    const selectedCount = checkboxes.length;
    const selectedIds = Array.from(checkboxes).map(cb => cb.value);

    // Mettre à jour l'input caché avec les IDs
    hiddenInput.value = selectedIds.join(',');

    if (selectedCount > 0) {
        // Activer le bouton
        button.disabled = false;
        countBadge.textContent = selectedCount;
        countBadge.classList.remove('hidden');
    } else {
        // Désactiver le bouton
        button.disabled = true;
        countBadge.classList.add('hidden');
    }
}

/**
 * Cocher/décocher toutes les livraisons
 */
function toggleAllDeliveries(masterCheckbox) {
    const checkboxes = document.querySelectorAll('.delivery-checkbox:not(:disabled)');

    checkboxes.forEach(checkbox => {
        checkbox.checked = masterCheckbox.checked;
    });

    updateBulkActionButton();
}

/**
 * Confirmation avant soumission du formulaire bulk
 */
document.addEventListener('DOMContentLoaded', function() {
    const bulkForm = document.getElementById('bulkCompleteForm');

    if (bulkForm) {
        bulkForm.addEventListener('submit', function(e) {
            const count = document.querySelectorAll('.delivery-checkbox:checked:not(:disabled)').length;

            if (count === 0) {
                e. preventDefault();
                alert('Aucune livraison sélectionnée');
                return false;
            }

            if (! confirm(`Confirmer la fin de ${count} livraison(s) ? `)) {
                e.preventDefault();
                return false;
            }
        });
    }

    // Initialiser l'état du bouton au chargement
    updateBulkActionButton();
});
