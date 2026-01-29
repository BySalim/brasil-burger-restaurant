// wwwroot/js/catalog.js

document.addEventListener("DOMContentLoaded", function () {
    // ---------------------------------------------------------------------
    // Sélecteurs DOM principaux
    // ---------------------------------------------------------------------
    const articleCards = document.querySelectorAll(".js-article-card");
    const filterButtons = document.querySelectorAll(".js-filter-btn");
    const complementFilterButton = document.querySelector(".js-filter-complements");

    const commandeButtons = document.querySelectorAll(".js-article-commande-btn");
    const complementButtons = document.querySelectorAll(".js-complement-toggle-btn");

    const cartAside = document.querySelector(".js-cart-aside");
    const cartOpenPanel = document.querySelector(".js-cart-open-panel");
    const cartCollapsedPanel = document.querySelector(".js-cart-collapsed-panel");
    const cartCollapseButton = document.querySelector(".js-cart-collapse-btn");
    const cartExpandButton = document.querySelector(".js-cart-expand-btn");

    const cartEmptyState = document.querySelector(".js-cart-empty-state");
    const cartLinesContainer = document.querySelector(".js-cart-lines");
    const cartFooter = document.querySelector(".js-cart-footer");
    const cartHiddenInputsContainer = document.querySelector(".js-cart-hidden-inputs");
    const cartTotalText = document.querySelector(".js-cart-total-text");
    const cartTotalInput = document.getElementById("js-cart-total-input");
    const cartClearButton = document.querySelector(".js-cart-clear-btn");
    const cartStepper = document.querySelector(".js-cart-stepper");

    let currentFilter = "all";
    let selectedArticleButton = null;

    // État du panier (JS uniquement)
    const cartState = {
        main: null,              // burger/menu
        complements: new Map()   // Map<int, article complément>
    };

    // ---------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------
    function extractArticleDataFromCard(card) {
        if (!card) return null;

        const articleId = parseInt(card.dataset.articleId || "0", 10);
        const libelle = card.dataset.articleLibelle || "";
        const imageUrl = card.dataset.articleImage || "";
        const prix = parseInt(card.dataset.articlePrix || "0", 10);
        const categorie = card.dataset.articleCategorie || card.dataset.articleCategory || "";
        const badgeText = (card.dataset.badgeText || "").toUpperCase();
        const badgeClasses = card.dataset.badgeClasses || "";

        if (!articleId || !categorie) {
            return null;
        }

        return {
            articleId,
            libelle,
            imageUrl,
            prix,
            categorie,
            badgeText,
            badgeClasses,
            quantity: 1
        };
    }

    function formatPrix(value) {
        if (!Number.isFinite(value)) {
            return "0";
        }
        return value.toLocaleString("fr-FR");
    }

    // ---------------------------------------------------------------------
    // Filtres (Tout / Burgers / Menus / Compléments)
    // ---------------------------------------------------------------------
    function applyFilterToCards() {
        articleCards.forEach(function (card) {
            const category = card.dataset.articleCategory;
            if (!category) return;

            let show = false;

            switch (currentFilter) {
                case "all":
                    show = category === "BURGER" || category === "MENU";
                    break;
                case "burgers":
                    show = category === "BURGER";
                    break;
                case "menus":
                    show = category === "MENU";
                    break;
                case "complements":
                    show = category === "COMPLEMENT";
                    break;
                default:
                    show = true;
                    break;
            }

            card.classList.toggle("hidden", !show);
        });
    }

    function updateFilterButtonsVisualState() {
        const activeClasses = [
            "bg-gray-900",
            "dark:bg-white",
            "text-white",
            "dark:text-gray-900",
            "shadow-lg",
            "transform",
            "hover:scale-105",
            "ring-2",
            "ring-transparent",
            "hover:ring-offset-2",
            "hover:ring-gray-900",
            "dark:hover:ring-white"
        ];

        filterButtons.forEach(function (btn) {
            const filter = btn.dataset.filter;
            if (!filter) return;

            const isActive = filter === currentFilter;
            activeClasses.forEach(c => btn.classList.toggle(c, isActive));
        });
    }

    function setFilter(filter) {
        currentFilter = filter;
        updateFilterButtonsVisualState();
        applyFilterToCards();
    }

    filterButtons.forEach(function (btn) {
        btn.addEventListener("click", function () {
            const filter = this.dataset.filter;
            if (!filter) return;

            if (filter === "complements" && this.classList.contains("hidden")) {
                return;
            }

            setFilter(filter);
        });
    });

    // Par défaut : on ne montre que Burgers + Menus
    setFilter("all");

    function showComplementFilter() {
        if (!complementFilterButton) return;
        complementFilterButton.classList.remove("hidden");
    }

    function hideComplementFilter() {
        if (!complementFilterButton) return;

        complementFilterButton.classList.add("hidden");

        if (currentFilter === "complements") {
            setFilter("all");
        }
    }

    function resetSelectedArticleButton() {
        if (!selectedArticleButton) return;

        selectedArticleButton.disabled = false;
        selectedArticleButton.classList.remove("bg-gray-400", "cursor-not-allowed", "opacity-70");
        selectedArticleButton.classList.add("bg-orange", "hover:bg-secondary");
        selectedArticleButton = null;
    }

    function resetAllComplementButtons() {
        complementButtons.forEach(function (btn) {
            btn.dataset.state = "removed";
            const labelSpan = btn.querySelector("span:first-child");
            const iconSpan = btn.querySelector(".material-symbols-outlined");

            if (labelSpan) labelSpan.textContent = "Ajouter";
            if (iconSpan) iconSpan.textContent = "add";

            btn.classList.remove("bg-red-600", "hover:bg-red-700");
            btn.classList.add("bg-orange", "hover:bg-orange-600");
        });
    }

    // ---------------------------------------------------------------------
    // Panier ouvert / réduit
    // ---------------------------------------------------------------------
    function setCartState(state) {
        if (!cartAside || !cartOpenPanel || !cartCollapsedPanel) return;

        if (state === "collapsed") {
            cartAside.dataset.state = "collapsed";
            cartAside.classList.remove("w-80", "lg:mr-6");
            cartAside.classList.add("w-20", "lg:pr-4");
            cartOpenPanel.classList.add("hidden");
            cartCollapsedPanel.classList.remove("hidden");
        } else {
            cartAside.dataset.state = "open";
            cartAside.classList.remove("w-20", "lg:pr-4");
            cartAside.classList.add("w-80", "lg:mr-6");
            cartOpenPanel.classList.remove("hidden");
            cartCollapsedPanel.classList.add("hidden");
        }
    }

    if (cartAside && cartOpenPanel && cartCollapsedPanel) {
        setCartState("collapsed");

        if (cartCollapseButton) {
            cartCollapseButton.addEventListener("click", function (e) {
                e.stopPropagation();
                setCartState("collapsed");
            });
        }

        if (cartExpandButton) {
            cartExpandButton.addEventListener("click", function (e) {
                e.stopPropagation();
                setCartState("open");
            });
        }

        cartCollapsedPanel.addEventListener("click", function () {
            setCartState("open");
        });
    }

    // ---------------------------------------------------------------------
    // Rendu du panier (Étape 1)
    // ---------------------------------------------------------------------
    function renderCartFromState() {
        if (!cartLinesContainer ||
            !cartEmptyState ||
            !cartFooter ||
            !cartHiddenInputsContainer ||
            !cartTotalInput ||
            !cartTotalText) {
            return;
        }

        cartLinesContainer.innerHTML = "";
        cartHiddenInputsContainer.innerHTML = "";

        const items = [];
        if (cartState.main) {
            items.push(cartState.main);
        }
        cartState.complements.forEach(v => items.push(v));

        if (items.length === 0) {
            cartEmptyState.classList.remove("hidden");
            cartLinesContainer.classList.add("hidden");
            cartFooter.classList.add("hidden");
            if (cartStepper) cartStepper.classList.add("hidden");

            cartTotalInput.value = "0";
            cartTotalText.textContent = "0 F CFA";
            return;
        }

        cartEmptyState.classList.add("hidden");
        cartLinesContainer.classList.remove("hidden");
        cartFooter.classList.remove("hidden");
        if (cartStepper) cartStepper.classList.remove("hidden");

        const template = document.getElementById("js-cart-line-template");
        if (!template) return;

        let total = 0;

        items.forEach(function (item, index) {
            const fragment = template.content.cloneNode(true);
            const lineEl = fragment.querySelector(".js-cart-line");

            const lineType = item.lineType || (index === 0 ? "main" : "complement");
            const quantity = item.quantity ?? 1;
            const prix = item.prix || 0;
            const lineTotalValue = prix * quantity;
            total += lineTotalValue;

            if (lineEl) {
                lineEl.dataset.articleId = String(item.articleId);
                lineEl.dataset.lineType = lineType;
            }

            const imgEl = fragment.querySelector(".js-cart-line-image");
            if (imgEl) {
                imgEl.src = item.imageUrl || "";
                imgEl.alt = item.libelle || "";
            }

            const titleEl = fragment.querySelector(".js-cart-line-title");
            if (titleEl) {
                titleEl.textContent = item.libelle || "";
            }

            const badgeEl = fragment.querySelector(".js-cart-line-badge");
            if (badgeEl) {
                badgeEl.textContent = (item.badgeText || "").toUpperCase();
                badgeEl.className =
                    "js-cart-line-badge inline-flex items-center px-1.5 py-0.5 rounded text-[10px] font-bold uppercase tracking-wider shrink-0 ml-1 h-fit " +
                    (item.badgeClasses || "bg-gray-700 text-white");
            }

            const unitPriceEl = fragment.querySelector(".js-cart-line-unit-price");
            if (unitPriceEl) {
                unitPriceEl.textContent = `${formatPrix(prix)} F CFA`;
            }

            const qtyEl = fragment.querySelector(".js-cart-line-quantity");
            if (qtyEl) {
                qtyEl.textContent = String(quantity);
            }

            const lineTotalEl = fragment.querySelector(".js-cart-line-total");
            if (lineTotalEl) {
                lineTotalEl.textContent = `${formatPrix(lineTotalValue)} F CFA`;
            }

            cartLinesContainer.appendChild(fragment);

            // Champs cachés pour PaiementCommandePostVm.ArticleQuantifiers
            const articleIdInput = document.createElement("input");
            articleIdInput.type = "hidden";
            articleIdInput.name = `ArticleQuantifiers[${index}].ArticleId`;
            articleIdInput.value = String(item.articleId);
            cartHiddenInputsContainer.appendChild(articleIdInput);

            const quantiteInput = document.createElement("input");
            quantiteInput.type = "hidden";
            quantiteInput.name = `ArticleQuantifiers[${index}].Quantite`;
            quantiteInput.value = String(quantity);
            cartHiddenInputsContainer.appendChild(quantiteInput);
        });

        cartTotalInput.value = String(total);
        cartTotalText.textContent = `${formatPrix(total)} F CFA`;
    }

    // Quantité +/- dans le panier (délégation)
    if (cartLinesContainer) {
        cartLinesContainer.addEventListener("click", function (event) {
            const minusBtn = event.target.closest(".js-cart-line-minus");
            const plusBtn = event.target.closest(".js-cart-line-plus");
            if (!minusBtn && !plusBtn) return;

            const lineEl = (minusBtn || plusBtn).closest(".js-cart-line");
            if (!lineEl) return;

            const articleId = parseInt(lineEl.dataset.articleId || "0", 10);
            const lineType = lineEl.dataset.lineType || "main";
            const delta = minusBtn ? -1 : 1;

            if (lineType === "main" && cartState.main && cartState.main.articleId === articleId) {
                const current = cartState.main.quantity ?? 1;
                cartState.main.quantity = Math.max(1, current + delta);
            } else if (lineType === "complement" && cartState.complements.has(articleId)) {
                const item = cartState.complements.get(articleId);
                const current = item.quantity ?? 1;
                item.quantity = Math.max(1, current + delta);
                cartState.complements.set(articleId, item);
            }

            renderCartFromState();
        });
    }

    // Bouton "Supprimer" : vider panier
    if (cartClearButton) {
        cartClearButton.addEventListener("click", function () {
            cartState.main = null;
            cartState.complements.clear();
            resetSelectedArticleButton();
            resetAllComplementButtons();
            hideComplementFilter();
            renderCartFromState();
        });
    }

    // ---------------------------------------------------------------------
    // Boutons "Commander" (BURGER / MENU)
    // ---------------------------------------------------------------------
    commandeButtons.forEach(function (btn) {
        btn.addEventListener("click", function () {
            if (selectedArticleButton === this) {
                return; // même article, rien à faire
            }

            // 1) Réinitialiser l'ancien bouton
            resetSelectedArticleButton();

            // 2) Marquer ce bouton comme sélectionné (désactivé / grisé)
            selectedArticleButton = this;
            this.disabled = true;
            this.classList.remove("bg-orange", "hover:bg-secondary");
            this.classList.add("bg-gray-400", "cursor-not-allowed", "opacity-70");

            // 3) Extraire les données de la carte
            const card = this.closest(".js-article-card");
            const article = extractArticleDataFromCard(card);
            if (!article) {
                console.warn("Impossible d'extraire les données de l'article pour le panier.");
                return;
            }

            article.quantity = 1;
            article.lineType = "main";

            // 4) Vider le panier et ajouter le nouvel article principal
            cartState.main = article;
            cartState.complements.clear();
            resetAllComplementButtons();

            // 5) Gestion du filtre Compléments
            const category = article.categorie;
            if (category === "BURGER") {
                showComplementFilter();
                setFilter("complements");
            } else {
                hideComplementFilter();
            }

            // 6) Ouvrir le panier
            setCartState("open");

            // 7) Recalculer le rendu
            renderCartFromState();
        });
    });

    // ---------------------------------------------------------------------
    // Boutons "Ajouter / Supprimer" des compléments
    // ---------------------------------------------------------------------
    complementButtons.forEach(function (btn) {
        btn.addEventListener("click", function () {
            // Compléments uniquement si un BURGER est dans le panier
            if (!cartState.main || cartState.main.categorie !== "BURGER") {
                return;
            }

            const isAdded = this.dataset.state === "added";
            const labelSpan = this.querySelector("span:first-child");
            const iconSpan = this.querySelector(".material-symbols-outlined");

            const card = this.closest(".js-article-card");
            const article = extractArticleDataFromCard(card);
            if (!article) return;

            const articleId = article.articleId;

            if (!isAdded) {
                // Ajout
                this.dataset.state = "added";

                if (labelSpan) labelSpan.textContent = "Supprimer";
                if (iconSpan) iconSpan.textContent = "delete";

                this.classList.remove("bg-orange", "hover:bg-orange-600");
                this.classList.add("bg-red-600", "hover:bg-red-700");

                article.quantity = 1;
                article.lineType = "complement";
                cartState.complements.set(articleId, article);
            } else {
                // Suppression
                this.dataset.state = "removed";

                if (labelSpan) labelSpan.textContent = "Ajouter";
                if (iconSpan) iconSpan.textContent = "add";

                this.classList.remove("bg-red-600", "hover:bg-red-700");
                this.classList.add("bg-orange", "hover:bg-orange-600");

                cartState.complements.delete(articleId);
            }

            renderCartFromState();
        });
    });

    // Rendu initial (panier vide)
    renderCartFromState();
});
