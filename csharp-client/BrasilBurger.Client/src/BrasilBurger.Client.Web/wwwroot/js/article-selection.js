/**
 * Gestion de la sélection des articles pour le panier
 */
class ArticleSelector {
    constructor() {
        this.selectedArticleId = null;
        this.init();
    }

    init() {
        this.attachEventListeners();
    }

    attachEventListeners() {
        const buttons = document.querySelectorAll('.article-command-btn');

        buttons.forEach(button => {
            button.addEventListener('click', (e) => this.handleButtonClick(e));
        });
    }

    handleButtonClick(event) {
        const clickedButton = event.currentTarget;
        const articleId = parseInt(clickedButton.dataset.articleId);

        // Si le même article est cliqué deux fois, on déselectionne
        if (this.selectedArticleId === articleId) {
            this.deselectArticle(clickedButton);
            return;
        }

        // Déselectionner l'ancien article s'il existe
        if (this.selectedArticleId !== null) {
            const previousButton = document.querySelector(
                `.article-command-btn[data-article-id="${this.selectedArticleId}"]`
            );
            if (previousButton) {
                this.deselectArticle(previousButton);
            }
        }

        // Selectionner le nouvel article
        this.selectArticle(clickedButton, articleId);
    }

    selectArticle(button, articleId) {
        button.classList.remove('bg-orange', 'hover:bg-secondary');
        button.classList.add('bg-gray-400', 'cursor-not-allowed', 'opacity-75');
        button.disabled = true;

        const textSpan = button.querySelector('.btn-text');
        textSpan.textContent = 'Sélectionné';

        this.selectedArticleId = articleId;

        console.log(`Article ${articleId} sélectionné`);
    }

    deselectArticle(button) {
        button.classList.remove('bg-gray-400', 'cursor-not-allowed', 'opacity-75');
        button.classList.add('bg-orange', 'hover:bg-secondary');
        button.disabled = false;

        const textSpan = button.querySelector('.btn-text');
        textSpan.textContent = 'Commander';

        this.selectedArticleId = null;

        console.log('Article déselectionné');
    }

    getSelectedArticleId() {
        return this.selectedArticleId;
    }
}

// Initialiser quand le DOM est chargé
document.addEventListener('DOMContentLoaded', () => {
    new ArticleSelector();
});
