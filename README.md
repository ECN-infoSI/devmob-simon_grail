Projet : Counter-Strike 2 sur Mobile
par Lazare GRAIL et Victor SIMON 

Cette application permet aux utilisateurs de jouer à Counter-Strike 2 sur mobile. Elle offre une interface intuitive pour configurer et rechercher des parties de jeu rapidement.

Une fois connecté, l'utilisateur arrive sur le menu principal (la première activité) qui présente : 
- un affichage du nom d'utilisateur ici "GrailSimon" en haut et centré.
- Un bouton "Réglages et Profil" est positionné en haut à droite, permettant d'accéder aux options de personnalisation.
- En bas de l'écran, deux boutons principaux sont disponibles: "Inventaire et Shop" pour accéder aux équipements et aux achats, et "Match" pour lancer une recherche de partie.

L'interface est habillée d'un arrière-plan thématique CS2 qui renforce l'immersion dans l'univers du jeu.

Lorsque l'utilisateur clique sur le bouton "Match", une fenêtre de dialogue s'ouvre avec trois catégories de paramètres. 
Le mode de jeu peut être choisi entre Compétitif (symbolisé par une icône étoile), Casual (représenté par une icône personne), ou Personnalisé (affiché avec une icône paramètres). 
La taille de l'équipe propose trois options: 1vs1, 2vs2, ou 5vs5 . 
Pour le choix de la carte, les options disponibles sont Dust II, Inferno et Mirage, des cartes emblématiques faisant partie des plus jouées de Counter-Strike. 
Chaque option est visuellement mise en évidence lorsqu'elle est sélectionnée. 
Deux boutons d'action sont présents en bas de la fenêtre: "Annuler" pour fermer le dialogue sans effet, et "Rechercher partie" pour lancer la recherche avec les paramètres choisis par l'utilisateur.

Après avoir cliqué sur "Rechercher partie", l'utilisateur est dirigé vers une page de chargement (la deuxième activité) qui affiche :
- un titre dynamique au format "[Mode de jeu] [Taille]vs[Taille] sur [Carte]", comme par exemple "Compétitif 5v5 sur Dust II".
- une image de la carte sélectionnée qui change automatiquement en fonction du choix fait précédemment, qu'il s'agisse de Dust II, Inferno ou Mirage.
- Une animation de chargement avec des points qui s'ajoutent progressivement ("Chargement...") indique que le système cherche une partie. Le message "En attente d'autres joueurs..." est également affiché pour informer l'utilisateur de l'état de sa recherche.
- Un bouton "Quitter la recherche de partie" est disponible pour permettre à l'utilisateur de revenir à l'écran principal s'il le souhaite.

L'utilisateur peut ainsi naviguer entre la configuration de son match avec des paramètres personnalisés et l'écran de recherche de partie qui reflète visuellement ses choix.
