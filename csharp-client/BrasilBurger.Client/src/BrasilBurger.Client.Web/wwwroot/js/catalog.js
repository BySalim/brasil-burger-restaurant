document.addEventListener("DOMContentLoaded", () => {
  const page = document.querySelector('[data-page="catalog"]');
  if (!page) return;

  // -----------------------------
  // Helpers
  // -----------------------------
  const fmtFcfa = (n) => `${Number(n || 0).toLocaleString("fr-FR")} F CFA`;

  const parsePriceFromCard = (articleEl) => {
    const priceSpan = articleEl.querySelector('span.text-orange');
    if (!priceSpan) return 0;
    const raw = priceSpan.textContent.replace(/\s/g, "").replace(/\u00A0/g, "");
    const num = raw.replace(/[^\d]/g, "");
    return Number(num || 0);
  };

  const getCardInfoFromButton = (btn) => {
    const item = btn.closest(".js-article-item");
    const card = btn.closest("article");
    const img = card?.querySelector("img");
    const titleEl = card?.querySelector("h3");
    const badgeEl = card?.querySelector('div.absolute.top-3.left-3 span');

    return {
      articleId: Number(btn.dataset.articleId),
      categorie: (item?.dataset?.categorie || "").toUpperCase(),
      typeComplement: (item?.dataset?.typeComplement || "").toUpperCase(),
      title: (titleEl?.textContent || "").trim(),
      badge: (badgeEl?.textContent || "").trim(),
      imageUrl: img?.getAttribute("src") || "",
      unitPrice: parsePriceFromCard(card)
    };
  };

  // -----------------------------
  // DOM - Filtres
  // -----------------------------
  const filterBtns = Array.from(document.querySelectorAll(".js-filter-btn"));
  const complementFilterBtn = document.querySelector(".js-filter-complements");
  const articleItems = Array.from(document.querySelectorAll(".js-article-item"));
  const grid = document.querySelector(".js-articles-grid");

  const setFilterActiveStyle = (btn, isActive) => {
    const active = [
      "bg-gray-900", "text-white", "border-gray-900",
      "dark:bg-white", "dark:text-gray-900", "dark:border-white"
    ];
    const inactive = [
      "bg-white", "text-gray-900", "border-gray-200", "hover:border-gray-400",
      "dark:bg-gray-900", "dark:text-white", "dark:border-gray-800", "dark:hover:border-gray-600"
    ];

    if (isActive) {
      inactive.forEach(c => btn.classList.remove(c));
      active.forEach(c => btn.classList.add(c));
    } else {
      active.forEach(c => btn.classList.remove(c));
      inactive.forEach(c => btn.classList.add(c));
    }
  };

  const applyFilter = (filterKey) => {
    articleItems.forEach(el => {
      const cat = (el.dataset.categorie || "").toUpperCase();

      if (filterKey === "all") {
        el.classList.toggle("hidden", cat === "COMPLEMENT");
        return;
      }

      if (filterKey === "burger") {
        el.classList.toggle("hidden", cat !== "BURGER");
        return;
      }

      if (filterKey === "menu") {
        el.classList.toggle("hidden", cat !== "MENU");
        return;
      }

      if (filterKey === "complement") {
        el.classList.toggle("hidden", cat !== "COMPLEMENT");
        return;
      }
    });

    filterBtns.forEach(b => setFilterActiveStyle(b, b.dataset.filter === filterKey));
  };

  applyFilter("all");

  filterBtns.forEach(btn => {
    btn.addEventListener("click", () => {
      applyFilter(btn.dataset.filter);
    });
  });

  // -----------------------------
  // DOM - Panier
  // -----------------------------
  const aside = document.querySelector(".js-cart-aside");
  const collapsedPanel = aside?.querySelector(".js-cart-collapsed-panel");
  const openPanel = aside?.querySelector(".js-cart-open-panel");
  const collapseBtn = aside?.querySelector(".js-cart-collapse-btn");

  const emptyState = aside?.querySelector(".js-cart-empty-state");
  const linesContainer = aside?.querySelector(".js-cart-lines");

  const step1Wrap = aside?.querySelector(".js-cart-step1");
  const step2Wrap = aside?.querySelector(".js-cart-step2");
  const stepper1 = aside?.querySelector(".js-cart-stepper-step1");
  const stepper2 = aside?.querySelector(".js-cart-stepper-step2");
  const title1 = aside?.querySelector(".js-cart-title-step1");
  const title2 = aside?.querySelector(".js-cart-title-step2");

  const nextStepBtn = aside?.querySelector(".js-cart-next-step-btn");
  const stepperNextBtn = aside?.querySelector(".js-cart-stepper-next");
  const stepperPrevBtn = aside?.querySelector(".js-cart-stepper-prev");

  const clearBtns = Array.from(document.querySelectorAll(".js-cart-clear-btn"));

  const lineTpl = aside?.querySelector("#js-cart-line-template");

  const totalStep1Text = aside?.querySelector(".js-cart-total-text");
  const subtotalText = aside?.querySelector(".js-cart-subtotal-text");
  const deliveryText = aside?.querySelector(".js-cart-delivery-text");
  const payTotalText = aside?.querySelector(".js-cart-pay-total-text");

  const form = document.getElementById("js-cart-form");
  const hiddenWrap = aside?.querySelector(".js-cart-hidden-inputs");
  const cartFooter = aside?.querySelector(".js-cart-footer");
  const cartStepper = aside?.querySelector(".js-cart-stepper");

  const hMontantTotal = document.getElementById("js-cart-total-input");
  const hTypeRecup = document.getElementById("js-cart-type-recuperation");
  const hModePaie = document.getElementById("js-cart-mode-paie");

  const hZone = document.getElementById("js-cart-info-zone");
  const hQuartier = document.getElementById("js-cart-info-quartier");
  const hPrixLivraison = document.getElementById("js-cart-info-prix");
  const hNote = document.getElementById("js-cart-info-note");

  const deliveryFields = aside?.querySelector(".js-cart-delivery-fields");
  const zoneSelect = document.getElementById("js-delivery-zone-select");
  const noteTextarea = document.getElementById("js-delivery-note");

  const submitMessage = aside?.querySelector(".js-cart-submit-message");

  const retrievalRadios = () => Array.from(document.querySelectorAll('input[name="js-retrieval"]'));
  const paymentRadios = () => Array.from(document.querySelectorAll('input[name="js-payment"]'));

  // -----------------------------
  // Cart state
  // -----------------------------
  const state = {
    isOpen: false,
    main: null,
    complements: new Map(),
    selectedCommandeBtn: null
  };

  const cartStateStep2 = {
    currentStep: 1,
    retrievalMethod: "EMPORTER",
    deliveryZoneId: null,
    deliveryQuartierId: null,
    deliveryPrice: 0,
    deliveryNote: "",
    paymentMethod: "OM"
  };

  // -----------------------------
  // LocalStorage - Sauvegarde temporaire du panier
  // -----------------------------
  const CART_STORAGE_KEY = "brasilburger_cart_temp";
  const CART_EXPIRY_DAYS = 1; // Durée de validité : 1 jour

  // Sauvegarder l'état du panier dans localStorage
  const saveCartToStorage = () => {
    try {
      const cartData = {
        timestamp: Date.now(),
        main: state.main,
        complements: Array.from(state.complements.entries()), // Convertir Map en Array
        step2Data: {
          retrievalMethod: cartStateStep2.retrievalMethod,
          deliveryZoneId: cartStateStep2.deliveryZoneId,
          deliveryQuartierId: cartStateStep2.deliveryQuartierId,
          deliveryPrice: cartStateStep2.deliveryPrice,
          deliveryNote: cartStateStep2.deliveryNote,
          paymentMethod: cartStateStep2.paymentMethod
        }
      };
      
      localStorage.setItem(CART_STORAGE_KEY, JSON.stringify(cartData));
    } catch (error) {
      console.error("Erreur lors de la sauvegarde du panier:", error);
    }
  };

  // Charger l'état du panier depuis localStorage
  const loadCartFromStorage = () => {
    try {
      const stored = localStorage.getItem(CART_STORAGE_KEY);
      if (!stored) return false;

      const cartData = JSON.parse(stored);
      
      // Vérifier l'expiration (1 jour = 24 heures = 86400000 ms)
      const expiryTime = CART_EXPIRY_DAYS * 24 * 60 * 60 * 1000;
      const now = Date.now();
      
      if (now - cartData.timestamp > expiryTime) {
        // Données expirées, supprimer
        localStorage.removeItem(CART_STORAGE_KEY);
        return false;
      }

      // Restaurer l'état de l'étape 1 (articles du panier)
      if (cartData.main) {
        state.main = cartData.main;
      }

      // Restaurer les compléments (Array -> Map)
      if (cartData.complements && Array.isArray(cartData.complements)) {
        state.complements = new Map(cartData.complements);
      }

      // Restaurer l'état de l'étape 2
      if (cartData.step2Data) {
        cartStateStep2.retrievalMethod = cartData.step2Data.retrievalMethod || "EMPORTER";
        cartStateStep2.deliveryZoneId = cartData.step2Data.deliveryZoneId || null;
        cartStateStep2.deliveryQuartierId = cartData.step2Data.deliveryQuartierId || null;
        cartStateStep2.deliveryPrice = cartData.step2Data.deliveryPrice || 0;
        cartStateStep2.deliveryNote = cartData.step2Data.deliveryNote || "";
        cartStateStep2.paymentMethod = cartData.step2Data.paymentMethod || "OM";
      }

      return true;
    } catch (error) {
      console.error("Erreur lors du chargement du panier:", error);
      localStorage.removeItem(CART_STORAGE_KEY);
      return false;
    }
  };

  // Supprimer la sauvegarde du panier
  const clearCartStorage = () => {
    try {
      localStorage.removeItem(CART_STORAGE_KEY);
    } catch (error) {
      console.error("Erreur lors de la suppression du panier:", error);
    }
  };


  // -----------------------------
  // Open / close
  // -----------------------------
  const openCart = () => {
    if (!aside || !collapsedPanel || !openPanel) return;
    state.isOpen = true;

    collapsedPanel.classList.add("hidden");
    openPanel.classList.remove("hidden");

    aside.classList.remove("w-20");
    aside.classList.add("w-full", "lg:w-80");

    if (grid) {
      grid.classList.remove("lg:grid-cols-3");
      grid.classList.add("lg:grid-cols-2");
    }
  };

  const collapseCart = () => {
    if (!aside || !collapsedPanel || !openPanel) return;
    state.isOpen = false;

    openPanel.classList.add("hidden");
    collapsedPanel.classList.remove("hidden");

    aside.classList.remove("w-full", "lg:w-80");
    aside.classList.add("w-20");

    if (grid) {
      grid.classList.remove("lg:grid-cols-2");
      grid.classList.add("lg:grid-cols-3");
    }
  };

  collapsedPanel?.addEventListener("click", openCart);
  collapseBtn?.addEventListener("click", collapseCart);

  // -----------------------------
  // Step management
  // -----------------------------
  const showStep1 = () => {
    cartStateStep2.currentStep = 1;
    
    if (step2Wrap) step2Wrap.classList.add("hidden");
    if (step1Wrap) step1Wrap.classList.remove("hidden");

    if (stepper2) stepper2.classList.add("hidden");
    if (stepper1) stepper1.classList.remove("hidden");

    if (title2) title2.classList.add("hidden");
    if (title1) title1.classList.remove("hidden");

    // Footer: montrer step1, cacher step2
    const footerStep1 = cartFooter?.querySelector(".js-cart-step1");
    const footerStep2 = cartFooter?.querySelector(".js-cart-step2");
    if (footerStep1) footerStep1.classList.remove("hidden");
    if (footerStep2) footerStep2.classList.add("hidden");

    if (submitMessage) {
      submitMessage.classList.add("hidden");
      submitMessage.textContent = "";
    }
  };

  const showStep2 = () => {
    if (!state.main) return;
    
    cartStateStep2.currentStep = 2;
    
    if (step1Wrap) step1Wrap.classList.add("hidden");
    if (step2Wrap) step2Wrap.classList.remove("hidden");

    if (stepper1) stepper1.classList.add("hidden");
    if (stepper2) stepper2.classList.remove("hidden");

    if (title1) title1.classList.add("hidden");
    if (title2) title2.classList.remove("hidden");

    // Footer: cacher step1, montrer step2
    const footerStep1 = cartFooter?.querySelector(".js-cart-step1");
    const footerStep2 = cartFooter?.querySelector(".js-cart-step2");
    if (footerStep1) footerStep1.classList.add("hidden");
    if (footerStep2) footerStep2.classList.remove("hidden");

    syncStep2Bindings();
    updateTotals();
  };

  // Navigation stepper - utiliser la délégation d'événements pour gérer les deux steppers
  aside?.addEventListener("click", (e) => {
    const target = e.target.closest("button");
    if (!target) return;
    
    // Bouton suivant dans stepper
    if (target.classList.contains("js-cart-stepper-next") && !target.disabled) {
      if (cartStateStep2.currentStep === 1) {
        showStep2();
      }
    }
    
    // Bouton précédent dans stepper
    if (target.classList.contains("js-cart-stepper-prev") && !target.disabled) {
      if (cartStateStep2.currentStep === 2) {
        showStep1();
      }
    }
  });

  if (nextStepBtn) {
    nextStepBtn.addEventListener("click", () => showStep2());
  }

  // -----------------------------
  // Render cart
  // -----------------------------
  const showComplementFilter = (show) => {
    if (!complementFilterBtn) return;
    if (show) {
      complementFilterBtn.classList.remove("hidden");
    } else {
      complementFilterBtn.classList.add("hidden");
      if (filterBtns.some(b => b.dataset.filter === "complement" && !b.classList.contains("hidden"))) {
        applyFilter("all");
      }
    }
  };

  const resetAllComplementButtons = () => {
    const complementButtons = Array.from(document.querySelectorAll(".js-complement-toggle-btn"));
    complementButtons.forEach(btn => {
      btn.dataset.state = "removed";
      const labelSpan = btn.querySelector("span:first-child");
      const iconSpan = btn.querySelector(".material-symbols-outlined");

      if (labelSpan) labelSpan.textContent = "Ajouter";
      if (iconSpan) iconSpan.textContent = "add";

      btn.classList.remove("bg-red-600", "hover:bg-red-700");
      btn.classList.add("bg-orange", "hover:bg-orange-600");
    });
  };

  const rebuildHiddenArticleQuantifiers = () => {
    if (!hiddenWrap) return;
    hiddenWrap.innerHTML = "";

    const items = [];
    if (state.main) items.push(state.main);
    state.complements.forEach(v => items.push(v));

    items.forEach((item, index) => {
      const idInput = document.createElement("input");
      idInput.type = "hidden";
      idInput.name = `ArticleQuantifiers[${index}].ArticleId`;
      idInput.value = String(item.articleId);
      hiddenWrap.appendChild(idInput);

      const qtyInput = document.createElement("input");
      qtyInput.type = "hidden";
      qtyInput.name = `ArticleQuantifiers[${index}].Quantite`;
      qtyInput.value = String(item.qty || 1);
      hiddenWrap.appendChild(qtyInput);
    });
  };

  const renderCart = () => {
    if (!linesContainer || !emptyState) return;

    linesContainer.innerHTML = "";
    const items = [];
    if (state.main) items.push(state.main);
    state.complements.forEach(v => items.push(v));

    if (items.length === 0) {
      emptyState.classList.remove("hidden");
      linesContainer.classList.add("hidden");
      if (cartFooter) cartFooter.classList.add("hidden");
      
      // Masquer les deux steppers quand le panier est vide
      if (stepper1) stepper1.classList.add("hidden");
      if (stepper2) stepper2.classList.add("hidden");
      
      if (totalStep1Text) totalStep1Text.textContent = "0 F CFA";
      if (hMontantTotal) hMontantTotal.value = "0";
      return;
    }

    emptyState.classList.add("hidden");
    linesContainer.classList.remove("hidden");
    if (cartFooter) cartFooter.classList.remove("hidden");
    
    // Afficher le stepper approprié selon l'étape actuelle
    if (cartStateStep2.currentStep === 1) {
      if (stepper1) stepper1.classList.remove("hidden");
      if (stepper2) stepper2.classList.add("hidden");
    } else if (cartStateStep2.currentStep === 2) {
      if (stepper1) stepper1.classList.add("hidden");
      if (stepper2) stepper2.classList.remove("hidden");
    }

    if (!lineTpl) return;

    let total = 0;
    items.forEach((item, index) => {
      const fragment = lineTpl.content.cloneNode(true);
      const lineEl = fragment.querySelector(".js-cart-line");

      const qty = item.qty || 1;
      const prix = item.unitPrice || 0;
      const lineTotal = prix * qty;
      total += lineTotal;

      if (lineEl) {
        lineEl.dataset.articleId = String(item.articleId);
        lineEl.dataset.lineType = index === 0 ? "main" : "complement";
      }

      const imgEl = fragment.querySelector(".js-cart-line-image");
      if (imgEl) {
        imgEl.src = item.imageUrl || "";
        imgEl.alt = item.title || "";
      }

      const titleEl = fragment.querySelector(".js-cart-line-title");
      if (titleEl) titleEl.textContent = item.title || "";

      const badgeEl = fragment.querySelector(".js-cart-line-badge");
      if (badgeEl) {
        badgeEl.textContent = (item.badge || "").toUpperCase();
      }

      const unitPriceEl = fragment.querySelector(".js-cart-line-unit-price");
      if (unitPriceEl) unitPriceEl.textContent = fmtFcfa(prix);

      const qtyEl = fragment.querySelector(".js-cart-line-quantity");
      if (qtyEl) qtyEl.textContent = String(qty);

      const lineTotalEl = fragment.querySelector(".js-cart-line-total");
      if (lineTotalEl) lineTotalEl.textContent = fmtFcfa(lineTotal);

      linesContainer.appendChild(fragment);
    });

    if (totalStep1Text) totalStep1Text.textContent = fmtFcfa(total);
    if (hMontantTotal) hMontantTotal.value = String(total);
    rebuildHiddenArticleQuantifiers();
    
    // Sauvegarder l'état du panier après chaque modification
    saveCartToStorage();
  };

  if (linesContainer) {
    linesContainer.addEventListener("click", (e) => {
      const minusBtn = e.target.closest(".js-cart-line-minus");
      const plusBtn = e.target.closest(".js-cart-line-plus");
      if (!minusBtn && !plusBtn) return;

      const lineEl = (minusBtn || plusBtn).closest(".js-cart-line");
      if (!lineEl) return;

      const articleId = parseInt(lineEl.dataset.articleId || "0", 10);
      const lineType = lineEl.dataset.lineType || "main";
      const delta = minusBtn ? -1 : 1;

      if (lineType === "main" && state.main && state.main.articleId === articleId) {
        const current = state.main.qty || 1;
        state.main.qty = Math.max(1, current + delta);
      } else if (lineType === "complement" && state.complements.has(articleId)) {
        const item = state.complements.get(articleId);
        const current = item.qty || 1;
        item.qty = Math.max(1, current + delta);
        state.complements.set(articleId, item);
      }

      renderCart();
      updateTotals();
    });
  }

  // -----------------------------
  // Commander button
  // -----------------------------
  const commandeButtons = Array.from(document.querySelectorAll(".js-article-commande-btn"));

  commandeButtons.forEach(btn => {
    btn.addEventListener("click", () => {
      if (state.selectedCommandeBtn === btn) return;

      if (state.selectedCommandeBtn) {
        state.selectedCommandeBtn.disabled = false;
        state.selectedCommandeBtn.classList.remove("bg-gray-400", "cursor-not-allowed", "opacity-70");
        state.selectedCommandeBtn.classList.add("bg-orange", "hover:bg-secondary");
      }

      state.selectedCommandeBtn = btn;
      btn.disabled = true;
      btn.classList.remove("bg-orange", "hover:bg-secondary");
      btn.classList.add("bg-gray-400", "cursor-not-allowed", "opacity-70");

      const info = getCardInfoFromButton(btn);
      state.main = {
        articleId: info.articleId,
        categorie: info.categorie,
        title: info.title,
        badge: info.badge,
        imageUrl: info.imageUrl,
        unitPrice: info.unitPrice,
        qty: 1
      };

      state.complements.clear();
      resetAllComplementButtons();

      const isBurger = info.categorie === "BURGER";
      showComplementFilter(isBurger);

      if (isBurger) {
        applyFilter("complement");
      } else {
        applyFilter("all");
      }

      openCart();
      renderCart();
    });
  });

  // -----------------------------
  // Compléments
  // -----------------------------
  const complementButtons = Array.from(document.querySelectorAll(".js-complement-toggle-btn"));

  const isBurgerSelected = () => state.main && state.main.categorie === "BURGER";

  complementButtons.forEach(btn => {
    btn.addEventListener("click", () => {
      if (!isBurgerSelected()) return;

      const info = getCardInfoFromButton(btn);
      const id = info.articleId;
      const current = state.complements.get(id);
      const isAdded = !!current;

      const labelSpan = btn.querySelector("span:first-child");
      const iconSpan = btn.querySelector(".material-symbols-outlined");

      if (!isAdded) {
        state.complements.set(id, {
          articleId: id,
          categorie: "COMPLEMENT",
          title: info.title,
          badge: info.badge,
          imageUrl: info.imageUrl,
          unitPrice: info.unitPrice,
          qty: 1
        });

        btn.dataset.state = "added";
        if (labelSpan) labelSpan.textContent = "Supprimer";
        if (iconSpan) iconSpan.textContent = "delete";
        btn.classList.remove("bg-orange", "hover:bg-orange-600");
        btn.classList.add("bg-red-600", "hover:bg-red-700");
        
        // Si on est à l'étape 2, revenir à l'étape 1 pour voir le complément ajouté
        if (cartStateStep2.currentStep === 2) {
          showStep1();
        }
      } else {
        state.complements.delete(id);

        btn.dataset.state = "removed";
        if (labelSpan) labelSpan.textContent = "Ajouter";
        if (iconSpan) iconSpan.textContent = "add";
        btn.classList.remove("bg-red-600", "hover:bg-red-700");
        btn.classList.add("bg-orange", "hover:bg-orange-600");
      }

      renderCart();
    });
  });

  // -----------------------------
  // Clear cart
  // -----------------------------
  const clearCart = () => {
    if (state.selectedCommandeBtn) {
      state.selectedCommandeBtn.disabled = false;
      state.selectedCommandeBtn.classList.remove("bg-gray-400", "cursor-not-allowed", "opacity-70");
      state.selectedCommandeBtn.classList.add("bg-orange", "hover:bg-secondary");
    }

    state.selectedCommandeBtn = null;
    state.main = null;
    state.complements.clear();

    resetAllComplementButtons();
    showComplementFilter(false);
    
    // Appliquer le filtre "Tout" pour masquer les compléments et afficher burgers/menus
    applyFilter("all");
    
    // Toujours revenir à step1 quand on vide le panier
    if (cartStateStep2.currentStep === 2) {
      showStep1();
    }
    
    // Masquer les steppers quand le panier est vide
    if (stepper1) stepper1.classList.add("hidden");
    if (stepper2) stepper2.classList.add("hidden");
    
    // Supprimer la sauvegarde localStorage
    clearCartStorage();
    
    renderCart();
  };

  clearBtns.forEach(b => b.addEventListener("click", clearCart));

  // -----------------------------
  // Step2 binding
  // -----------------------------
  const setDeliveryHiddenEnabled = (enabled) => {
    const inputs = [hZone, hQuartier, hPrixLivraison, hNote].filter(Boolean);
    inputs.forEach(i => {
      if (enabled) i.removeAttribute("disabled");
      else i.setAttribute("disabled", "disabled");
    });
  };

  const syncStep2Bindings = () => {
    const r = retrievalRadios().find(x => x.checked);
    const p = paymentRadios().find(x => x.checked);

    if (hTypeRecup) hTypeRecup.value = r ? r.value : "";
    if (hModePaie) hModePaie.value = p ? p.value : "";

    const isDelivery = r?.dataset?.isDelivery === "true";
    deliveryFields?.classList.toggle("hidden", !isDelivery);

    setDeliveryHiddenEnabled(!!isDelivery);

    if (!isDelivery) {
      if (zoneSelect) zoneSelect.selectedIndex = 0;
      if (noteTextarea) noteTextarea.value = "";
      if (hZone) hZone.value = "";
      if (hQuartier) hQuartier.value = "";
      if (hPrixLivraison) hPrixLivraison.value = "";
      if (hNote) hNote.value = "";
      cartStateStep2.deliveryPrice = 0;
      cartStateStep2.deliveryZoneId = null;
      cartStateStep2.deliveryQuartierId = null;
      cartStateStep2.deliveryNote = "";
    }
    
    updateTotals();
    
    // Sauvegarder les changements de l'étape 2
    saveCartToStorage();
  };

  document.addEventListener("change", (e) => {
    if (e.target.matches('input[name="js-retrieval"]') || e.target.matches('input[name="js-payment"]')) {
      syncStep2Bindings();
    }

    if (e.target === zoneSelect) {
      const opt = zoneSelect.selectedOptions?.[0];
      if (opt) {
        if (hZone) hZone.value = String(opt.dataset.zoneId || "");
        if (hQuartier) hQuartier.value = String(opt.dataset.quartierId || "");
        if (hPrixLivraison) hPrixLivraison.value = String(opt.dataset.prixLivraison || "0");
        cartStateStep2.deliveryPrice = parseInt(opt.dataset.prixLivraison || "0", 10);
        cartStateStep2.deliveryZoneId = parseInt(opt.dataset.zoneId || "0", 10) || null;
        cartStateStep2.deliveryQuartierId = parseInt(opt.dataset.quartierId || "0", 10) || null;
      }
      updateTotals();
      saveCartToStorage();
    }

    if (e.target === noteTextarea) {
      if (hNote) hNote.value = noteTextarea.value || "";
      cartStateStep2.deliveryNote = noteTextarea.value || "";
      saveCartToStorage();
    }
  });

  // -----------------------------
  // Submit avec overlay de confirmation
  // -----------------------------
  form?.addEventListener("submit", async (e) => {
    e.preventDefault();

    if (cartStateStep2.currentStep !== 2) return;

    if (!state.main) {
      return;
    }

    syncStep2Bindings();
    rebuildHiddenArticleQuantifiers();
    updateTotals();

    const r = retrievalRadios().find(x => x.checked);
    const p = paymentRadios().find(x => x.checked);

    const isDelivery = r?.dataset?.isDelivery === "true";
    if (isDelivery) {
      const opt = zoneSelect?.selectedOptions?.[0];
      if (!opt) {
        showMessage(false, "Veuillez sélectionner une zone et un quartier pour la livraison.");
        return;
      }
      if (hNote && noteTextarea) hNote.value = noteTextarea.value || "";
    }

    if (!p) {
      showMessage(false, "Veuillez sélectionner un mode de paiement.");
      return;
    }

    // Préparer les données pour l'overlay de confirmation
    let subtotal = 0;
    if (state.main) {
      subtotal += (state.main.unitPrice || 0) * (state.main.qty || 1);
    }
    
    const complements = [];
    state.complements.forEach(item => {
      subtotal += (item.unitPrice || 0) * (item.qty || 1);
      complements.push(item);
    });
    
    const deliveryPrice = cartStateStep2.deliveryPrice || 0;
    const total = subtotal + deliveryPrice;
    
    let quartier = '';
    if (isDelivery && zoneSelect) {
      const selectedOption = zoneSelect.selectedOptions[0];
      if (selectedOption) {
        quartier = selectedOption.textContent || '';
      }
    }
    
    const deliveryNote = noteTextarea?.value || '';
    
    const orderData = {
      main: state.main,
      complements: complements,
      retrievalMethod: cartStateStep2.retrievalMethod,
      quartier: quartier,
      deliveryNote: deliveryNote,
      paymentMethod: cartStateStep2.paymentMethod,
      subtotal: subtotal,
      deliveryPrice: deliveryPrice,
      total: total
    };
    
    // Afficher l'overlay de confirmation
    if (typeof window.showOrderConfirmationOverlay === 'function') {
      window.showOrderConfirmationOverlay(orderData, () => {
        submitOrderToServer();
      });
    } else {
      // Fallback si l'overlay n'est pas disponible
      submitOrderToServer();
    }
  });

  // Fonction pour soumettre la commande au serveur (soumission classique avec rechargement)
  function submitOrderToServer() {
    // Soumettre le formulaire de manière classique
    // La page se rechargera et le contrôleur traitera les données
    form.submit();
  }

  function showMessage(ok, text) {
    if (!submitMessage) return;
    submitMessage.classList.remove("hidden");
    submitMessage.textContent = text;

    submitMessage.classList.remove("border-red-200", "bg-red-50", "text-red-700");
    submitMessage.classList.remove("border-green-200", "bg-green-50", "text-green-700");

    if (ok) {
      submitMessage.classList.add("border-green-200", "bg-green-50", "text-green-700");
      submitMessage.style.borderWidth = "1px";
    } else {
      submitMessage.classList.add("border-red-200", "bg-red-50", "text-red-700");
      submitMessage.style.borderWidth = "1px";
    }
  }

  // -----------------------------
  // Step2 - ChoiceCards sync
  // -----------------------------
  const syncChoiceCards = () => {
    document.querySelectorAll('[data-choice-cards]').forEach(fieldset => {
      const buttons = fieldset.querySelectorAll('.choice-card');
      const hiddenInputId = fieldset.dataset.targetInputId;
      const hiddenInput = document.getElementById(hiddenInputId);
      
      if (!hiddenInput) return;
      
      buttons.forEach(btn => {
        btn.addEventListener('click', () => {
          const value = btn.dataset.choiceValue;
          
          buttons.forEach(b => {
            const isSelected = b.dataset.choiceValue === value;
            b.classList.toggle('is-selected', isSelected);
            b.classList.toggle('border-blue-500', isSelected);
            b.classList.toggle('bg-blue-50/50', isSelected);
            b.classList.toggle('dark:bg-blue-900/10', isSelected);
            b.classList.toggle('border-gray-200', !isSelected);
            b.classList.toggle('dark:border-gray-700', !isSelected);
            b.setAttribute('aria-checked', isSelected.toString());
            b.dataset.isSelected = isSelected.toString();
            
            const checkIcon = b.querySelector('.choice-check-icon');
            if (checkIcon) checkIcon.remove();
            if (isSelected) {
              const icon = document.createElement('span');
              icon.className = 'choice-check-icon absolute top-1 right-1 material-symbols-outlined text-base text-blue-600 dark:text-blue-400';
              icon.textContent = 'check_circle';
              b.appendChild(icon);
            }
          });
          
          hiddenInput.value = value;
          
          const fieldName = hiddenInput.name;
          if (fieldName === 'RetrievalMethod') {
            cartStateStep2.retrievalMethod = value;
            const radio = document.querySelector(`input[name="js-retrieval"][value="${value}"]`);
            if (radio) {
              radio.checked = true;
              radio.dispatchEvent(new Event('change', { bubbles: true }));
            }
          } else if (fieldName === 'PaymentMethod') {
            cartStateStep2.paymentMethod = value;
            const radio = document.querySelector(`input[name="js-payment"][value="${value}"]`);
            if (radio) {
              radio.checked = true;
              radio.dispatchEvent(new Event('change', { bubbles: true }));
            }
          }
        });
      });
    });
  };

  setTimeout(syncChoiceCards, 100);

  // -----------------------------
  // Step2 - Totaux
  // -----------------------------
  const updateTotals = () => {
    if (!subtotalText || !deliveryText || !payTotalText) return;
    
    let subtotal = 0;
    if (state.main) {
      subtotal += (state.main.unitPrice || 0) * (state.main.qty || 1);
    }
    state.complements.forEach(item => {
      subtotal += (item.unitPrice || 0) * (item.qty || 1);
    });
    
    const deliveryPrice = cartStateStep2.deliveryPrice || 0;
    const total = subtotal + deliveryPrice;
    
    subtotalText.textContent = fmtFcfa(subtotal);
    deliveryText.textContent = fmtFcfa(deliveryPrice);
    payTotalText.textContent = fmtFcfa(total);
    
    if (hMontantTotal) hMontantTotal.value = String(total);
  };

  // -----------------------------
  // Restauration du panier au chargement de la page
  // -----------------------------
  const restoreCartOnLoad = () => {
    const hasCartData = loadCartFromStorage();
    
    if (hasCartData && state.main) {
      // Le panier a été restauré depuis localStorage (étape 1 + étape 2)
      
      // Restaurer le filtre de compléments si un burger est sélectionné
      const isBurger = state.main.categorie === "BURGER";
      showComplementFilter(isBurger);
      
      if (isBurger) {
        applyFilter("complement");
      }
      
      // Restaurer l'état visuel des boutons de compléments
      state.complements.forEach((item) => {
        const btn = Array.from(document.querySelectorAll(".js-complement-toggle-btn"))
          .find(b => parseInt(b.dataset.articleId) === item.articleId);
        
        if (btn) {
          btn.dataset.state = "added";
          const labelSpan = btn.querySelector("span:first-child");
          const iconSpan = btn.querySelector(".material-symbols-outlined");
          if (labelSpan) labelSpan.textContent = "Supprimer";
          if (iconSpan) iconSpan.textContent = "delete";
          btn.classList.remove("bg-orange", "hover:bg-orange-600");
          btn.classList.add("bg-red-600", "hover:bg-red-700");
        }
      });
      
      // Restaurer l'état visuel du bouton "Commander" de l'article principal
      const mainBtn = Array.from(document.querySelectorAll(".js-article-commande-btn"))
        .find(b => parseInt(b.dataset.articleId) === state.main.articleId);
      
      if (mainBtn) {
        state.selectedCommandeBtn = mainBtn;
        mainBtn.disabled = true;
        mainBtn.classList.remove("bg-orange", "hover:bg-secondary");
        mainBtn.classList.add("bg-gray-400", "cursor-not-allowed", "opacity-70");
      }
      
      // Restaurer les choix de l'étape 2 dans les Choice Cards
      // Attendre que les Choice Cards soient initialisés
      setTimeout(() => {
        // Restaurer le mode de récupération dans les Choice Cards
        if (cartStateStep2.retrievalMethod) {
          const retrievalInput = document.getElementById('RetrievalMethod');
          if (retrievalInput) {
            retrievalInput.value = cartStateStep2.retrievalMethod;
            
            // Mettre à jour visuellement les Choice Cards de récupération
            const retrievalCards = document.querySelectorAll('[data-choice-cards] .choice-card');
            retrievalCards.forEach(card => {
              if (card.dataset.choiceValue === cartStateStep2.retrievalMethod) {
                card.click(); // Déclenche la sélection visuelle
              }
            });
          }
          
          // Synchroniser avec les radios
          const retrievalRadio = retrievalRadios().find(r => r.value === cartStateStep2.retrievalMethod);
          if (retrievalRadio) {
            retrievalRadio.checked = true;
          }
        }
        
        // Restaurer le mode de paiement dans les Choice Cards
        if (cartStateStep2.paymentMethod) {
          const paymentInput = document.getElementById('PaymentMethod');
          if (paymentInput) {
            paymentInput.value = cartStateStep2.paymentMethod;
            
            // Mettre à jour visuellement les Choice Cards de paiement
            const paymentCards = document.querySelectorAll('[data-choice-cards] .choice-card');
            paymentCards.forEach(card => {
              if (card.dataset.choiceValue === cartStateStep2.paymentMethod) {
                card.click(); // Déclenche la sélection visuelle
              }
            });
          }
          
          // Synchroniser avec les radios
          const paymentRadio = paymentRadios().find(r => r.value === cartStateStep2.paymentMethod);
          if (paymentRadio) {
            paymentRadio.checked = true;
          }
        }
        
        // Restaurer la zone de livraison
        if (cartStateStep2.deliveryQuartierId && zoneSelect) {
          const option = Array.from(zoneSelect.options).find(opt => 
            parseInt(opt.dataset.quartierId) === cartStateStep2.deliveryQuartierId
          );
          if (option) {
            zoneSelect.value = option.value;
            
            // Mettre à jour les champs cachés
            if (hZone) hZone.value = String(option.dataset.zoneId || "");
            if (hQuartier) hQuartier.value = String(option.dataset.quartierId || "");
            if (hPrixLivraison) hPrixLivraison.value = String(option.dataset.prixLivraison || "0");
          }
        }
        
        // Restaurer la note de livraison
        if (cartStateStep2.deliveryNote && noteTextarea) {
          noteTextarea.value = cartStateStep2.deliveryNote;
          if (hNote) hNote.value = cartStateStep2.deliveryNote;
        }
        
        // Synchroniser les bindings de l'étape 2
        syncStep2Bindings();
      }, 150); // Délai pour s'assurer que les Choice Cards sont prêts
      
      // Ouvrir le panier à l'étape 1
      openCart();
      showStep1();
    }
    
    // Rendre le panier (vide ou restauré)
    renderCart();
    updateTotals();
  };

  // Appeler la restauration au chargement de la page
  restoreCartOnLoad();
});