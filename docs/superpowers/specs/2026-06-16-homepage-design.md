# Homepage Klioba — Clubs Cards

## Contexte

Création de la page d'accueil de Klioba avec affichage des clubs HEI sous forme de cards, accessible uniquement aux utilisateurs authentifiés. Les clubs cards doivent refléter les données réelles de la base (via Event/MembershipFee), pas des données mockées.

## Flux utilisateur

1. **Non connecté** → Page d'accueil avec fond sombre, logo "Klioba", message "Connectez-vous pour accéder aux clubs HEI", bouton "Se connecter" pointant vers `/oauth2/authorization/casdoor`
2. **Connecté** → 4 cartes statistiques en haut, grille de clubs cards en dessous

## Composants

### Nouveaux fichiers

| Fichier | Type | Description |
|---------|------|-------------|
| `ClubRepository` | Repository | Opérations de lecture sur les clubs |
| `ClubService` | Service | Calcule les statistiques (cotisations, membres, fonds) par club |

### Fichiers modifiés

| Fichier | Description |
|---------|-------------|
| `TsinjoController` | Endpoint `GET /` enrichi avec données clubs authentifiées |
| `home.html` | Template complet : auth gate + stats + grille clubs |
| `header.html` | Navigation : ajout lien "Clubs" |

## Logique métier — ClubService

```
pour chaque club :
  events = eventService.findAllByClubIdWithPaymentResolution(club.id)
  
  totalCotisations = sum(events with amount > 0 et status == CONFIRMED)
  membres = distinct(events.user.id)
  depenses = sum(events with amount < 0 et status == CONFIRMED)
  fondsRestants = totalCotisations - depenses
  
  retourner ClubStats(id, name, totalCotisations, membres, depenses, fondsRestants)

pour les totaux globaux :
  totalCotisations = sum(clubStats.totalCotisations)
  totalDepenses = sum(clubStats.depenses)
  totalRemaining = sum(clubStats.fondsRestants)
  totalMembers = sum(clubStats.membres)
```

## Structure du template home.html

### Section non connectée
- Fond plein écran `bg-[#16163f]`
- Icône `business_center`
- Titre "Klioba"
- Sous-titre "Connectez-vous pour accéder aux clubs HEI"
- Bouton "Se connecter" → `/oauth2/authorization/casdoor`

### Section connectée
- **4 cartes stats** (2x2 grid, puis 4 colonnes sur desktop) : Total Cotisations, Total Dépenses, Fonds Restants, Total Membres
- **Grille clubs** (1 colonne mobile, 2 tablette, 3 desktop) :
  - Cercle avec initiale du club
  - Nom du club
  - Lignes : Cotisations (montant), Membres (nombre), Fonds restants (montant)
  - Bouton "Voir les détails" → `/club/{id}`
- **Footer** visible uniquement pour connectés

## Sécurité

`authorization.requestMatchers("/").permitAll()` déjà en place. Le template gère l'affichage conditionnel via `sec:authorize`.

## Non-couvert (scope futur)

- Page détail club `/club/{id}` (sera faite séparément)
- Route club listing `/club` (la home fait office de listing)
