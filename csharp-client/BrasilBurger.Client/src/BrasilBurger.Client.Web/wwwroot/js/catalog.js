document.addEventListener("DOMContentLoaded", () => {
  const page = document.querySelector('[data-page="catalog"]');
  if (!page) return;

  // -----------------------------
  // Helpers
  // -----------------------------
  const fmtFcfa = (n) => `${Number(n || 0).toLocaleString("fr-FR")} F CFA`;

  const parsePriceFromCard = (articleEl) => {
    // récupère le prix (texte orange) et le convertit en int
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
    // Actif: sombre
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
    // all => BURGER + MENU uniquement (jamais COMPLEMENT)
    // burger/menu/complement => exact
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

  // default filter
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
  const content = aside?.querySelector(".js-cart-content");

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

  const linesContainer = aside?.querySelector(".js-cart-lines");
  const lineTpl = aside?.querySelector("#js-cart-line-template");

  const totalStep1Text = aside?.querySelector(".js-cart-total-text");
  const subtotalText = aside?.querySelector(".js-cart-subtotal-text");
  const deliveryText = aside?.querySelector(".js-cart-delivery-text");
  const payTotalText = aside?.querySelector(".js-cart-pay-total-text");

  const form = document.getElementById("js-cart-form");
  const hiddenWrap = aside?.querySelector(".js-cart-hidden-inputs");

  const hMontantTotal = document.getElementById("js-cart-montant-total");
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
    step: 1,
    main: null, // {articleId, categorie, title, badge, imageUrl, unitPrice, qty}
    complements: new Map(), // id -> same + qty
    selectedCommandeBtn: null
  };

  // -----------------------------
  // Open / close
  // -----------------------------
  const openCart = () => {
    if (!aside || !collapsedPanel || !openPanel) return;
    state.isOpen = true;

    collapsedPanel.classList.add("hidden");
    openPanel.classList.remove("hidden");

    // width switch
    aside.classList.remove("w-20");
    aside.classList.add("w-full", "lg:w-80");

    // grid columns switch
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
    state.step = 1;
    step2Wrap?.classList.add("hidden");
    step1Wrap?.classList.remove("hidden");

    stepper2?.classList.add("hidden");
    stepper1?.classList.remove("hidden");

    title2?.classList.add("hidden");
    title1?.classList.remove("hidden");

    submitMessage?.classList.add("hidden");
    submitMessage && (submitMessage.textContent = "");
  };

  const showStep2 = () => {
    if (!state.main) return; // panier vide => pas d'étape 2
    state.step = 2;
    step1Wrap?.classList.add("hidden");
    step2Wrap?.classList.remove("hidden");

    stepper1?.classList.add("hidden");
    stepper2?.classList.remove("hidden");

    title1?.classList.add("hidden");
    title2?.classList.remove("hidden");

    // pré-sélection: si rien de choisi, cocher la 1ère option (récup + paiement)
    const r = retrievalRadios();
    if (r.length && !r.some(x => x.checked)) r[0].checked = true;

    const p = paymentRadios();
    if (p.length && !p.some(x => x.checked)) p[0].checked = true;

    syncStep2Bindings();
    updateTotals();
  };

  nextStepBtn?.addEventListener("click", showStep2);
  stepperNextBtn?.addEventListener("click", showStep2);
  stepperPrevBtn?.addEventListener("click", showStep1);

  // -----------------------------
  // Render
  // -----------------------------
  const computeSubtotal = () => {
    let sum = 0;
    if (state.main) sum += state.main.unitPrice * state.main.qty;
    state.complements.forEach(c => { sum += c.unitPrice * c.qty; });
    return sum;
  };

  const getDeliveryFee = () => {
    const selectedRetrieval = retrievalRadios().find(x => x.checked);
    const isDelivery = selectedRetrieval?.dataset?.isDelivery === "true";
    if (!isDelivery) return 0;

    const opt = zoneSelect?.selectedOptions?.[0];
    const fee = opt ? Number(opt.dataset.prixLivraison || 0) : 0;
    return fee;
  };

  const updateTotals = () => {
    const subtotal = computeSubtotal();
    const delivery = getDeliveryFee();
    const total = subtotal + delivery;

    if (totalStep1Text) totalStep1Text.textContent = fmtFcfa(subtotal);

    if (subtotalText) subtotalText.textContent = fmtFcfa(subtotal);
    if (deliveryText) deliveryText.textContent = fmtFcfa(delivery);
    if (payTotalText) payTotalText.textContent = fmtFcfa(total);

    if (hMontantTotal) hMontantTotal.value = String(total);
  };

  const setEmptyUI = () => {
    emptyState?.classList.remove("hidden");
    content?.classList.add("hidden");
    hiddenWrap?.classList.add("hidden");
  };

  const setNonEmptyUI = () => {
    emptyState?.classList.add("hidden");
    content?.classList.remove("hidden");
    hiddenWrap?.classList.remove("hidden");
  };

  const clearLines = () => {
    if (linesContainer) linesContainer.innerHTML = "";
  };

  const renderLine = (item, type) => {
    if (!lineTpl || !linesContainer) return;

    const node = lineTpl.content.cloneNode(true);
    const root = node.querySelector(".js-cart-line");
    root.dataset.articleId = String(item.articleId);
    root.dataset.lineType = type;

    const img = node.querySelector(".js-line-img");
    const title = node.querySelector(".js-line-title");
    const badge = node.querySelector(".js-line-badge");
    const unit = node.querySelector(".js-line-unit");
    const total = node.querySelector(".js-line-total");
    const qty = node.querySelector(".js-line-qty");

    const btnMinus = node.querySelector(".js-line-minus");
    const btnPlus = node.querySelector(".js-line-plus");
    const btnRemove = node.querySelector(".js-line-remove");

    img && img.setAttribute("src", item.imageUrl || "");
    img && img.setAttribute("alt", item.title || "");

    if (title) title.textContent = item.title || "";
    if (badge) badge.textContent = item.badge || "";
    if (unit) unit.textContent = `Unitaire : ${fmtFcfa(item.unitPrice)}`;
    if (qty) qty.textContent = String(item.qty);
    if (total) total.textContent = fmtFcfa(item.unitPrice * item.qty);

    // remove button only for complements (optionnel)
    if (type === "complement") {
      btnRemove?.classList.remove("hidden");
    }

    const refresh = () => {
      if (qty) qty.textContent = String(item.qty);
      if (total) total.textContent = fmtFcfa(item.unitPrice * item.qty);
      updateTotals();
      rebuildHiddenArticleQuantifiers();
    };

    btnPlus?.addEventListener("click", () => {
      item.qty += 1;
      refresh();
    });

    btnMinus?.addEventListener("click", () => {
      if (type === "main") {
        if (item.qty <= 1) return;
        item.qty -= 1;
        refresh();
        return;
      }

      // complement: si qty==1 => supprimer la ligne
      if (item.qty <= 1) {
        state.complements.delete(item.articleId);
        // remettre le bouton complément en "Ajouter"
        resetComplementButton(item.articleId);
        renderCart();
        return;
      }

      item.qty -= 1;
      refresh();
    });

    btnRemove?.addEventListener("click", () => {
      if (type !== "complement") return;
      state.complements.delete(item.articleId);
      resetComplementButton(item.articleId);
      renderCart();
    });

    linesContainer.appendChild(node);
  };

  const renderCart = () => {
    if (!state.main) {
      setEmptyUI();
      showStep1();
      updateTotals();
      return;
    }

    setNonEmptyUI();
    showStep1();

    clearLines();
    renderLine(state.main, "main");

    // complements
    Array.from(state.complements.values()).forEach(c => renderLine(c, "complement"));

    updateTotals();
    rebuildHiddenArticleQuantifiers();
  };

  // -----------------------------
  // Hidden inputs (ArticleQuantifiers)
  // -----------------------------
  const rebuildHiddenArticleQuantifiers = () => {
    if (!hiddenWrap) return;

    // supprimer les anciens champs ArticleQuantifiers[*]
    Array.from(hiddenWrap.querySelectorAll('input[name^="ArticleQuantifiers["]')).forEach(n => n.remove());

    const items = [];
    if (state.main) items.push({ articleId: state.main.articleId, qty: state.main.qty });
    state.complements.forEach(c => items.push({ articleId: c.articleId, qty: c.qty }));

    items.forEach((x, i) => {
      const a = document.createElement("input");
      a.type = "hidden";
      a.name = `ArticleQuantifiers[${i}].ArticleId`;
      a.value = String(x.articleId);

      const q = document.createElement("input");
      q.type = "hidden";
      q.name = `ArticleQuantifiers[${i}].Quantite`;
      q.value = String(x.qty);

      hiddenWrap.appendChild(a);
      hiddenWrap.appendChild(q);
    });
  };

  // -----------------------------
  // Commander (BURGER / MENU)
  // -----------------------------
  const commandeButtons = Array.from(document.querySelectorAll(".js-article-commande-btn"));

  const setCommandeBtnSelected = (btn) => {
    // reset old
    if (state.selectedCommandeBtn) {
      state.selectedCommandeBtn.disabled = false;
      state.selectedCommandeBtn.classList.remove("bg-gray-400", "cursor-not-allowed", "opacity-70");
      state.selectedCommandeBtn.classList.add("bg-orange", "hover:bg-secondary");
    }

    state.selectedCommandeBtn = btn;
    btn.disabled = true;
    btn.classList.remove("bg-orange", "hover:bg-secondary");
    btn.classList.add("bg-gray-400", "cursor-not-allowed", "opacity-70");
  };

  const resetAllComplementButtons = () => {
    const btns = Array.from(document.querySelectorAll(".js-complement-toggle-btn"));
    btns.forEach(b => {
      b.dataset.state = "removed";
      const labelSpan = b.querySelector("span:first-child");
      const iconSpan = b.querySelector(".material-symbols-outlined");
      if (labelSpan) labelSpan.textContent = "Ajouter";
      if (iconSpan) iconSpan.textContent = "add";

      b.classList.remove("bg-red-600", "hover:bg-red-700");
      b.classList.add("bg-orange", "hover:bg-orange-600");
    });
  };

  const resetComplementButton = (articleId) => {
    const btn = document.querySelector(`.js-complement-toggle-btn[data-article-id="${articleId}"]`);
    if (!btn) return;
    btn.dataset.state = "removed";
    const labelSpan = btn.querySelector("span:first-child");
    const iconSpan = btn.querySelector(".material-symbols-outlined");
    if (labelSpan) labelSpan.textContent = "Ajouter";
    if (iconSpan) iconSpan.textContent = "add";

    btn.classList.remove("bg-red-600", "hover:bg-red-700");
    btn.classList.add("bg-orange", "hover:bg-orange-600");
  };

  const showComplementFilter = (shouldShow) => {
    if (!complementFilterBtn) return;
    complementFilterBtn.classList.toggle("hidden", !shouldShow);

    if (!shouldShow) {
      // revenir sur "Tout"
      applyFilter("all");
    }
  };

  commandeButtons.forEach(btn => {
    btn.addEventListener("click", () => {
      // select
      setCommandeBtnSelected(btn);

      // open cart
      openCart();

      // clear cart & complements
      resetAllComplementButtons();
      state.complements.clear();

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

      // Compléments visibles seulement si burger
      const isBurger = info.categorie === "BURGER";
      showComplementFilter(isBurger);

      // Si burger => diriger automatiquement vers compléments
      if (isBurger) {
        applyFilter("complement");
      } else {
        applyFilter("all");
      }

      renderCart();
    });
  });

  // -----------------------------
  // Compléments (toggle Ajouter/Supprimer)
  // -----------------------------
  const complementButtons = Array.from(document.querySelectorAll(".js-complement-toggle-btn"));

  const isBurgerSelected = () => state.main && state.main.categorie === "BURGER";

  complementButtons.forEach(btn => {
    btn.addEventListener("click", () => {
      if (!isBurgerSelected()) {
        // pas de burger => ignorer (ou tu peux afficher un message si tu veux)
        return;
      }

      const info = getCardInfoFromButton(btn);
      const id = info.articleId;
      const current = state.complements.get(id);
      const isAdded = !!current;

      const labelSpan = btn.querySelector("span:first-child");
      const iconSpan = btn.querySelector(".material-symbols-outlined");

      if (!isAdded) {
        // add
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
      } else {
        // remove
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
    // reset commande button
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
    renderCart();
  };

  clearBtns.forEach(b => b.addEventListener("click", clearCart));

  // -----------------------------
  // Step2 binding + validation
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
    }
  };

  document.addEventListener("change", (e) => {
    if (e.target.matches('input[name="js-retrieval"]') || e.target.matches('input[name="js-payment"]')) {
      syncStep2Bindings();
      updateTotals();
    }

    if (e.target === zoneSelect) {
      const opt = zoneSelect.selectedOptions?.[0];
      if (opt) {
        if (hZone) hZone.value = String(opt.dataset.zoneId || "");
        if (hQuartier) hQuartier.value = String(opt.dataset.quartierId || "");
        if (hPrixLivraison) hPrixLivraison.value = String(opt.dataset.prixLivraison || "0");
      }
      updateTotals();
    }

    if (e.target === noteTextarea) {
      if (hNote) hNote.value = noteTextarea.value || "";
    }
  });

  // -----------------------------
  // Submit (fetch, sans reload)
  // -----------------------------
  form?.addEventListener("submit", async (e) => {
    e.preventDefault();

    if (state.step !== 2) return;

    // validations minimales côté client
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

    const fd = new FormData(form);
    try {
      const res = await fetch(form.action, {
        method: "POST",
        body: fd,
        headers: { "X-Requested-With": "XMLHttpRequest" }
      });

      if (!res.ok) {
        let msg = "Une erreur est survenue lors de la finalisation.";
        try {
          const data = await res.json();
          if (data?.errors) {
            // afficher une 1ère erreur lisible
            const firstKey = Object.keys(data.errors)[0];
            const firstVal = data.errors[firstKey];
            if (Array.isArray(firstVal) && firstVal.length) msg = firstVal[0];
          }
        } catch (_) { /* ignore */ }
        showMessage(false, msg);
        return;
      }

      showMessage(true, "Commande enregistrée (mock). Vous pouvez continuer.");
      clearCart();
      collapseCart();
    } catch (err) {
      showMessage(false, "Impossible d'envoyer la commande. Vérifiez votre connexion.");
    }
  });

  function showMessage(ok, text) {
    if (!submitMessage) return;
    submitMessage.classList.remove("hidden");
    submitMessage.textContent = text;

    // style
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

  // initial UI
  renderCart();
});
