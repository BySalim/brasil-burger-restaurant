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

  if (stepperNextBtn) {
    stepperNextBtn.addEventListener("click", () => {
      if (cartStateStep2.currentStep === 1) {
        showStep2();
      }
    });
  }

  if (stepperPrevBtn) {
    stepperPrevBtn.addEventListener("click", () => {
      if (cartStateStep2.currentStep === 2) {
        showStep1();
      }
    });
  }

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
      if (cartStepper) cartStepper.classList.add("hidden");
      if (totalStep1Text) totalStep1Text.textContent = "0 F CFA";
      if (hMontantTotal) hMontantTotal.value = "0";
      return;
    }

    emptyState.classList.add("hidden");
    linesContainer.classList.remove("hidden");
    if (cartFooter) cartFooter.classList.remove("hidden");
    if (cartStepper) cartStepper.classList.remove("hidden");

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
    renderCart();
    
    if (cartStateStep2.currentStep === 2) {
      showStep1();
    }
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
    }
    
    updateTotals();
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
      }
      updateTotals();
    }

    if (e.target === noteTextarea) {
      if (hNote) hNote.value = noteTextarea.value || "";
    }
  });

  // -----------------------------
  // Submit
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
          if (fieldName === 'Form.RetrievalMethod') {
            cartStateStep2.retrievalMethod = value;
            const radio = document.querySelector(`input[name="js-retrieval"][value="${value}"]`);
            if (radio) {
              radio.checked = true;
              radio.dispatchEvent(new Event('change', { bubbles: true }));
            }
          } else if (fieldName === 'Form.PaymentMethod') {
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

  renderCart();
  updateTotals();
});
