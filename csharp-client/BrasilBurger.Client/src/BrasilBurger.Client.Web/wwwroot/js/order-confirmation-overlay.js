// Gestion de l'overlay de confirmation de commande
(function() {
  'use strict';

  // Helper pour formater les prix
  const fmtFcfa = (n) => `${Number(n || 0).toLocaleString("fr-FR")} F CFA`;

  // Éléments DOM de l'overlay
  const overlay = document.getElementById('js-order-confirmation-overlay');
  if (!overlay) return;

  const backdrop = overlay.querySelector('.js-overlay-backdrop');
  const content = overlay.querySelector('.js-overlay-content');
  const closeButtons = overlay.querySelectorAll('.js-close-overlay');
  const confirmButton = overlay.querySelector('.js-confirm-order');
  
  // Conteneurs pour les données
  const articlesContainer = overlay.querySelector('.js-overlay-articles');
  const retrievalMethod = overlay.querySelector('.js-overlay-retrieval-method');
  const retrievalIcon = overlay.querySelector('.js-overlay-retrieval-icon');
  const quartierElement = overlay.querySelector('.js-overlay-quartier');
  const noteContainer = overlay.querySelector('.js-overlay-note-container');
  const noteElement = overlay.querySelector('.js-overlay-note');
  const paymentMethod = overlay.querySelector('.js-overlay-payment-method');
  const paymentIcon = overlay.querySelector('.js-overlay-payment-icon');
  const subtotalElement = overlay.querySelector('.js-overlay-subtotal');
  const deliveryElement = overlay.querySelector('.js-overlay-delivery');
  const totalElement = overlay.querySelector('.js-overlay-total');
  
  // Template pour les articles
  const articleTemplate = document.getElementById('js-overlay-article-template');

  // État de l'overlay
  let isOpen = false;
  let orderData = null;
  let onConfirmCallback = null;

  /**
   * Ouvrir l'overlay avec animation
   */
  function openOverlay() {
    if (isOpen) return;
    
    overlay.classList.remove('hidden');
    overlay.classList.add('flex');
    
    // Forcer un reflow pour que la transition fonctionne
    void overlay.offsetWidth;
    
    // Animer l'entrée
    requestAnimationFrame(() => {
      backdrop.classList.remove('opacity-0');
      backdrop.classList.add('opacity-100');
      content.classList.remove('scale-95', 'opacity-0');
      content.classList.add('scale-100', 'opacity-100');
    });
    
    isOpen = true;
    
    // Empêcher le scroll du body
    document.body.style.overflow = 'hidden';
  }

  /**
   * Fermer l'overlay avec animation
   */
  function closeOverlay() {
    if (!isOpen) return;
    
    backdrop.classList.remove('opacity-100');
    backdrop.classList.add('opacity-0');
    content.classList.remove('scale-100', 'opacity-100');
    content.classList.add('scale-95', 'opacity-0');
    
    // Attendre la fin de l'animation avant de cacher
    setTimeout(() => {
      overlay.classList.remove('flex');
      overlay.classList.add('hidden');
      isOpen = false;
      
      // Réactiver le scroll du body
      document.body.style.overflow = '';
    }, 300);
  }

  /**
   * Remplir l'overlay avec les données de commande
   */
  function populateOverlay(data) {
    orderData = data;
    
    // Vider le conteneur d'articles
    articlesContainer.innerHTML = '';
    
    // Ajouter l'article principal
    if (data.main) {
      const articleEl = createArticleElement(data.main);
      articlesContainer.appendChild(articleEl);
    }
    
    // Ajouter les compléments
    if (data.complements && data.complements.length > 0) {
      data.complements.forEach(complement => {
        const articleEl = createArticleElement(complement);
        articlesContainer.appendChild(articleEl);
      });
    }
    
    // Mode de récupération
    const isDelivery = data.retrievalMethod === 'LIVRER';
    retrievalMethod.textContent = isDelivery ? 'Livraison' : 'À emporter';
    retrievalIcon.textContent = isDelivery ? 'local_shipping' : 'store';
    
    // Quartier (si livraison)
    if (isDelivery && data.quartier) {
      quartierElement.textContent = data.quartier;
      quartierElement.classList.remove('hidden');
    } else {
      quartierElement.classList.add('hidden');
    }
    
    // Note de livraison (si présente)
    if (data.deliveryNote && data.deliveryNote.trim()) {
      noteElement.textContent = data.deliveryNote;
      noteContainer.classList.remove('hidden');
    } else {
      noteContainer.classList.add('hidden');
    }
    
    // Mode de paiement
    const paymentMethods = {
      'OM': { label: 'Orange Money', icon: 'smartphone' },
      'WAVE': { label: 'Wave', icon: 'payments' },
      'ESPECES': { label: 'Espèces', icon: 'payments' }
    };
    
    const payment = paymentMethods[data.paymentMethod] || paymentMethods['OM'];
    paymentMethod.textContent = payment.label;
    paymentIcon.textContent = payment.icon;
    
    // Totaux
    subtotalElement.textContent = fmtFcfa(data.subtotal);
    deliveryElement.textContent = fmtFcfa(data.deliveryPrice);
    totalElement.textContent = fmtFcfa(data.total);
  }

  /**
   * Créer un élément article pour l'overlay
   */
  function createArticleElement(article) {
    const clone = articleTemplate.content.cloneNode(true);
    const container = clone.querySelector('div');
    
    const image = clone.querySelector('.js-overlay-article-image');
    const title = clone.querySelector('.js-overlay-article-title');
    const badge = clone.querySelector('.js-overlay-article-badge');
    const qty = clone.querySelector('.js-overlay-article-qty');
    const total = clone.querySelector('.js-overlay-article-total');
    
    image.src = article.imageUrl || '';
    image.alt = article.title || '';
    title.textContent = article.title || '';
    badge.textContent = article.badge || '';
    qty.textContent = `× ${article.qty || 1}`;
    
    const itemTotal = (article.unitPrice || 0) * (article.qty || 1);
    total.textContent = fmtFcfa(itemTotal);
    
    return container;
  }

  /**
   * Afficher l'overlay de confirmation
   * @param {Object} data - Données de la commande
   * @param {Function} onConfirm - Callback appelée quand l'utilisateur confirme
   */
  window.showOrderConfirmationOverlay = function(data, onConfirm) {
    populateOverlay(data);
    onConfirmCallback = onConfirm;
    openOverlay();
  };

  // Event listeners pour fermer l'overlay
  closeButtons.forEach(btn => {
    btn.addEventListener('click', closeOverlay);
  });

  // Fermer en cliquant sur le backdrop
  backdrop.addEventListener('click', closeOverlay);

  // Empêcher la fermeture en cliquant sur le contenu
  content.addEventListener('click', (e) => {
    e.stopPropagation();
  });

  // Event listener pour la confirmation
  confirmButton.addEventListener('click', () => {
    if (onConfirmCallback && typeof onConfirmCallback === 'function') {
      onConfirmCallback(orderData);
    }
    closeOverlay();
  });

  // Fermer avec la touche Escape
  document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape' && isOpen) {
      closeOverlay();
    }
  });

})();