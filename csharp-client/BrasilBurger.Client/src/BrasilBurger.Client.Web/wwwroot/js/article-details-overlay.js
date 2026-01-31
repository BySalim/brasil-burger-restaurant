// Gestion de l'overlay de détails d'article (BURGER et MENU uniquement)
(function() {
  'use strict';

  console.log('📦 Script article-details-overlay.js chargé');

  // Helper pour décoder les entités HTML
  function decodeHtmlEntities(text) {
    const textArea = document.createElement('textarea');
    textArea.innerHTML = text;
    return textArea.value;
  }

  // Helper pour formater les prix
  const fmtFcfa = (n) => `${Number(n || 0).toLocaleString("fr-FR")} F CFA`;

  // Éléments DOM de l'overlay
  const overlay = document.getElementById('js-article-details-overlay');
  if (!overlay) {
    console.error('❌ Overlay #js-article-details-overlay non trouvé');
    return;
  }

  console.log('✅ Overlay trouvé');

  const backdrop = overlay.querySelector('.js-overlay-backdrop');
  const content = overlay.querySelector('.js-overlay-content');
  const closeButton = overlay.querySelector('.js-close-overlay');
  
  // Éléments de contenu
  const imageEl = overlay.querySelector('.js-overlay-image');
  const badgeEl = overlay.querySelector('.js-overlay-badge');
  const priceEl = overlay.querySelector('.js-overlay-price');
  const titleEl = overlay.querySelector('.js-overlay-title');
  const descriptionEl = overlay.querySelector('.js-overlay-description');
  const menuCompositionSection = overlay.querySelector('.js-overlay-menu-composition');
  const menuItemsContainer = overlay.querySelector('.js-overlay-menu-items');
  const orderBtn = overlay.querySelector('.js-overlay-order-btn');
  
  // Template pour les items de menu
  const menuItemTemplate = document.getElementById('js-menu-item-template');

  // État de l'overlay
  let isOpen = false;
  let currentArticleData = null;

  /**
   * Ouvrir l'overlay avec animation
   */
  function openOverlay() {
    if (isOpen) return;
    
    console.log('🔓 Ouverture de l\'overlay');
    
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
    
    console.log('🔒 Fermeture de l\'overlay');
    
    backdrop.classList.remove('opacity-100');
    backdrop.classList.add('opacity-0');
    content.classList.remove('scale-100', 'opacity-100');
    content.classList.add('scale-95', 'opacity-0');
    
    // Attendre la fin de l'animation avant de cacher
    setTimeout(() => {
      overlay.classList.remove('flex');
      overlay.classList.add('hidden');
      isOpen = false;
      currentArticleData = null;
      
      // Réactiver le scroll du body
      document.body.style.overflow = '';
    }, 300);
  }

  /**
   * Créer un élément item de menu
   */
  function createMenuItem(item) {
    if (!menuItemTemplate) {
      console.error('❌ Template #js-menu-item-template non trouvé');
      return null;
    }

    console.log('🔨 Création item menu:', item);

    const clone = menuItemTemplate.content.cloneNode(true);
    const container = clone.querySelector('div');
    
    const image = clone.querySelector('.js-menu-item-image');
    const title = clone.querySelector('.js-menu-item-title');
    
    if (image) {
      image.src = item.imageUrl || '';
      image.alt = item.title || '';
      console.log('  📷 Image:', item.imageUrl);
    }
    
    if (title) {
      title.textContent = item.title || '';
      console.log('  📌 Titre:', item.title);
    }
    
    return container;
  }

  /**
   * Remplir l'overlay avec les données de l'article
   */
  function populateOverlay(data) {
    currentArticleData = data;
    
    console.log('📋 Population de l\'overlay avec:', data);
    
    // Image
    if (imageEl) {
      imageEl.src = data.imageUrl || '';
      imageEl.alt = data.title || '';
      console.log('  📷 Image principale:', data.imageUrl);
    }
    
    // Badge
    if (badgeEl) {
      badgeEl.textContent = data.badge || '';
      badgeEl.className = 'js-overlay-badge inline-flex items-center px-4 py-2 rounded-lg backdrop-blur-sm border border-white/10 text-xs font-bold uppercase tracking-wider shadow-lg ' + (data.badgeClasses || '');
      console.log('  🏷️ Badge:', data.badge);
    }
    
    // Prix
    if (priceEl) {
      const priceFormatted = Number(data.unitPrice || 0).toLocaleString("fr-FR");
      priceEl.textContent = priceFormatted;
      console.log('  💰 Prix:', priceFormatted);
    }
    
    // Titre
    if (titleEl) {
      titleEl.textContent = data.title || '';
      console.log('  📌 Titre:', data.title);
    }
    
    // Description
    if (descriptionEl) {
      descriptionEl.textContent = data.description || '';
      console.log('  📝 Description:', data.description);
    }
    
    // Composition du menu (visible uniquement pour les MENU)
    if (data.categorie === 'MENU' && data.menuItems && data.menuItems.length > 0) {
      console.log('📋 Menu détecté avec', data.menuItems.length, 'items');
      
      if (menuCompositionSection) {
        menuCompositionSection.classList.remove('hidden');
        console.log('  ✅ Section composition affichée');
      }
      
      if (menuItemsContainer) {
        menuItemsContainer.innerHTML = '';
        console.log('  🧹 Container nettoyé');
        
        data.menuItems.forEach((item, index) => {
          console.log(`  📦 Ajout item ${index + 1}:`, item);
          const menuItemEl = createMenuItem(item);
          if (menuItemEl) {
            menuItemsContainer.appendChild(menuItemEl);
            console.log(`  ✅ Item ${index + 1} ajouté au DOM`);
          } else {
            console.error(`  ❌ Échec création item ${index + 1}`);
          }
        });
        
        console.log('  ✅ Tous les items ajoutés');
      } else {
        console.error('  ❌ Container menu items non trouvé');
      }
    } else {
      console.log('⚪ Pas de composition de menu à afficher');
      if (menuCompositionSection) {
        menuCompositionSection.classList.add('hidden');
      }
    }
    
    // Mettre à jour le bouton de commande
    if (orderBtn) {
      orderBtn.dataset.articleId = data.articleId;
      orderBtn.dataset.categorie = data.categorie;
      console.log('  🛒 Bouton commande configuré');
    }
  }

  /**
   * Extraire les données d'un article depuis la carte
   */
  function getArticleDataFromCard(card) {
    console.log('🔍 Extraction des données de la carte');
    
    const img = card.querySelector('img');
    const titleEl = card.querySelector('h3');
    const badgeEl = card.querySelector('div.absolute.top-3.left-3 span');
    const descriptionEl = card.querySelector('p.text-sm.text-gray-500');
    
    // Extraire le prix
    const priceSpan = card.querySelector('span.text-orange, span.font-extrabold.text-xl, span.font-extrabold.text-base');
    let unitPrice = 0;
    if (priceSpan) {
      const raw = priceSpan.textContent.replace(/\s/g, "").replace(/\u00A0/g, "");
      const num = raw.replace(/[^\d]/g, "");
      unitPrice = Number(num || 0);
    }
    
    const articleId = Number(card.dataset.articleId);
    const categorie = (card.dataset.articleCategorie || card.dataset.categorie || '').toUpperCase();
    const badgeClasses = card.dataset.badgeClasses || '';
    
    const data = {
      articleId: articleId,
      categorie: categorie,
      title: (titleEl?.textContent || '').trim(),
      badge: (badgeEl?.textContent || '').trim(),
      badgeClasses: badgeClasses,
      imageUrl: img?.getAttribute('src') || '',
      unitPrice: unitPrice,
      description: (descriptionEl?.textContent || '').trim(),
      menuItems: []
    };
    
    console.log('  📊 Données de base extraites:', {
      articleId: data.articleId,
      categorie: data.categorie,
      title: data.title
    });
    
    // Pour les MENU, extraire la composition si elle existe dans les données
    if (categorie === 'MENU') {
      console.log('  🔎 Recherche de data-menu-items...');
      const menuItemsAttr = card.getAttribute('data-menu-items');
      
      if (menuItemsAttr) {
        console.log('  ✅ Attribut data-menu-items trouvé');
        console.log('  📄 Contenu brut (100 premiers chars):', menuItemsAttr.substring(0, 100));
        
        try {
          // Décoder les entités HTML puis parser le JSON
          const decodedJson = decodeHtmlEntities(menuItemsAttr);
          console.log('  📄 JSON décodé (100 premiers chars):', decodedJson.substring(0, 100));
          
          data.menuItems = JSON.parse(decodedJson);
          console.log('  ✅ Menu items parsés avec succès:', data.menuItems.length, 'items');
          console.log('  📦 Items:', data.menuItems);
        } catch (e) {
          console.error('  ❌ Erreur lors du parsing des items du menu:', e);
          console.log('  📄 Contenu complet de data-menu-items:', menuItemsAttr);
        }
      } else {
        console.warn('  ⚠️ Aucun attribut data-menu-items trouvé pour ce menu');
        console.log('  📋 Tous les attributs data-* de la carte:');
        Array.from(card.attributes).forEach(attr => {
          if (attr.name.startsWith('data-')) {
            console.log(`    - ${attr.name}: ${attr.value.substring(0, 50)}...`);
          }
        });
      }
    }
    
    return data;
  }

  /**
   * Afficher l'overlay de détails pour un article
   */
  window.showArticleDetailsOverlay = function(articleCard) {
    console.log('🎯 showArticleDetailsOverlay appelé');
    const data = getArticleDataFromCard(articleCard);
    populateOverlay(data);
    openOverlay();
  };

  // Event listeners pour fermer l'overlay
  if (closeButton) {
    closeButton.addEventListener('click', closeOverlay);
    console.log('✅ Event listener sur bouton fermeture configuré');
  }

  // Fermer en cliquant sur le backdrop
  if (backdrop) {
    backdrop.addEventListener('click', closeOverlay);
    console.log('✅ Event listener sur backdrop configuré');
  }

  // Empêcher la fermeture en cliquant sur le contenu
  if (content) {
    content.addEventListener('click', (e) => {
      e.stopPropagation();
    });
    console.log('✅ Event listener sur contenu configuré');
  }

  // Fermer avec la touche Escape
  document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape' && isOpen) {
      closeOverlay();
    }
  });
  console.log('✅ Event listener Escape configuré');

  // Event listener pour le bouton de commande dans l'overlay
  if (orderBtn) {
    orderBtn.addEventListener('click', () => {
      console.log('🛒 Bouton Commander cliqué dans l\'overlay');
      
      // Fermer l'overlay
      closeOverlay();
      
      // Trouver le bouton de commande correspondant dans la page
      const articleId = orderBtn.dataset.articleId;
      const commandeBtn = document.querySelector(`.js-article-commande-btn[data-article-id="${articleId}"]`);
      
      if (commandeBtn) {
        console.log('✅ Bouton commande original trouvé, simulation du clic');
        // Simuler un clic sur le bouton de commande original
        commandeBtn.click();
      } else {
        console.error('❌ Bouton commande original non trouvé');
      }
    });
    console.log('✅ Event listener sur bouton Commander configuré');
  }

  // Intercepter les clics sur les cartes BURGER et MENU pour afficher l'overlay
  document.addEventListener('click', (e) => {
    // Vérifier si le clic est sur une carte d'article (mais pas sur le bouton de commande)
    const card = e.target.closest('.js-article-card');
    
    // Ne pas ouvrir l'overlay si on clique sur un bouton
    if (e.target.closest('button')) {
      return;
    }
    
    if (card) {
      const categorie = (card.dataset.articleCategorie || card.dataset.categorie || '').toUpperCase();
      
      console.log('🖱️ Clic sur carte détecté, catégorie:', categorie);
      
      // Afficher l'overlay uniquement pour BURGER et MENU
      if (categorie === 'BURGER' || categorie === 'MENU') {
        console.log('✅ Catégorie valide, ouverture de l\'overlay');
        showArticleDetailsOverlay(card);
      } else {
        console.log('⚪ Catégorie non valide pour l\'overlay');
      }
    }
  });
  console.log('✅ Event listener sur clics de cartes configuré');

  console.log('✅ Script article-details-overlay.js initialisé avec succès');

})();