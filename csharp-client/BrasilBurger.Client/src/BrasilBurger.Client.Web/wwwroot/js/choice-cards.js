// Gestion des cartes de choix - Version simplifiée et corrigée
document.addEventListener('DOMContentLoaded', function() {
    const fieldsets = document.querySelectorAll('[data-choice-cards]');

    fieldsets.forEach(fieldset => {
        const hiddenInput = document.getElementById(fieldset.dataset.targetInputId);
        if (!hiddenInput) return;

        const cards = fieldset.querySelectorAll('.choice-card');

        cards.forEach(card => {
            card.addEventListener('click', function() {
                const value = this.dataset.choiceValue;
                const isCurrentlySelected = this.dataset.isSelected === 'true';

                // Si la carte est déjà sélectionnée, on désélectionne
                if (isCurrentlySelected) {
                    this.classList.remove('is-selected', 'border-blue-500', 'bg-blue-50/50', 'dark:bg-blue-900/10');
                    this.classList.add('border-gray-200', 'dark:border-gray-700');
                    this.setAttribute('aria-checked', 'false');
                    this.dataset.isSelected = 'false';
                    this.style.borderColor = '';

                    // Retirer l'icône de validation
                    const checkIcon = this.querySelector('.choice-check-icon');
                    if (checkIcon) {
                        checkIcon.remove();
                    }

                    // Réinitialiser la couleur de l'icône principale
                    const mainIcon = this.querySelector('.material-symbols-outlined:first-of-type');
                    if (mainIcon) {
                        mainIcon.style.color = '#6b7280';
                    }

                    // Réinitialiser la couleur du label
                    const label = this.querySelector('span:not(.material-symbols-outlined)');
                    if (label) {
                        label.style.color = '#374151';
                    }

                    // Vider l'input caché
                    hiddenInput.value = '';
                    hiddenInput.dispatchEvent(new Event('change', { bubbles: true }));
                    return;
                }

                // Désélectionner toutes les autres cartes du groupe
                cards.forEach(c => {
                    if (c !== this) {
                        c.classList.remove('is-selected', 'border-blue-500', 'bg-blue-50/50', 'dark:bg-blue-900/10');
                        c.classList.add('border-gray-200', 'dark:border-gray-700');
                        c.setAttribute('aria-checked', 'false');
                        c.dataset.isSelected = 'false';
                        c.style.borderColor = '';

                        // Retirer l'icône de validation
                        const checkIcon = c.querySelector('.choice-check-icon');
                        if (checkIcon) {
                            checkIcon.remove();
                        }

                        // Réinitialiser la couleur de l'icône principale
                        const mainIcon = c.querySelector('.material-symbols-outlined:first-of-type');
                        if (mainIcon) {
                            mainIcon.style.color = '#6b7280';
                        }

                        // Réinitialiser la couleur du label
                        const label = c.querySelector('span:not(.material-symbols-outlined)');
                        if (label) {
                            label.style.color = '#374151';
                        }
                    }
                });

                // Sélectionner la carte cliquée
                this.classList.remove('border-gray-200', 'dark:border-gray-700');
                this.classList.add('is-selected', 'border-blue-500', 'bg-blue-50/50', 'dark:bg-blue-900/10');
                this.setAttribute('aria-checked', 'true');
                this.dataset.isSelected = 'true';
                this.style.borderColor = '#3b82f6';

                // Ajouter l'icône de validation en haut à droite
                const checkIcon = document.createElement('span');
                checkIcon.className = 'choice-check-icon absolute top-1.5 right-1.5 material-symbols-outlined text-lg text-blue-600 dark:text-blue-400';
                checkIcon.textContent = 'check_circle';
                this.appendChild(checkIcon);

                // Mettre à jour la couleur de l'icône principale
                const mainIcon = this.querySelector('.material-symbols-outlined:first-of-type');
                if (mainIcon) {
                    mainIcon.style.color = '#2563eb';
                }

                // Mettre à jour la couleur du label
                const label = this.querySelector('span:not(.material-symbols-outlined)');
                if (label) {
                    label.style.color = '#1e40af';
                }

                // Mettre à jour l'input caché
                hiddenInput.value = value;

                // Déclencher un événement change
                hiddenInput.dispatchEvent(new Event('change', { bubbles: true }));
            });

            // Support du clavier
            card.addEventListener('keydown', function(e) {
                if (e.key === 'Enter' || e.key === ' ') {
                    e.preventDefault();
                    this.click();
                }

                // Navigation au clavier (flèches)
                if (e.key === 'ArrowRight' || e.key === 'ArrowDown') {
                    e.preventDefault();
                    const next = this.nextElementSibling;
                    if (next && next.classList.contains('choice-card')) {
                        next.focus();
                    }
                }

                if (e.key === 'ArrowLeft' || e.key === 'ArrowUp') {
                    e.preventDefault();
                    const prev = this.previousElementSibling;
                    if (prev && prev.classList.contains('choice-card')) {
                        prev.focus();
                    }
                }
            });
        });
    });
});
