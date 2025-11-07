# Explication du Projet : Star Wars Rebels Rescue Fleet

## 📋 Vue d'ensemble

Ce projet est une **démonstration d'Architecture Hexagonale** (aussi appelée "Ports and Adapters") utilisant **Java 21** et **Spring Boot**. Il s'agit d'une application thématique Star Wars qui permet d'assembler une flotte de vaisseaux spatiaux pour évacuer des rebelles.

## 🎯 Objectif métier

L'application résout le problème suivant : 
> *"Étant donné un nombre de passagers à évacuer, assembler automatiquement une flotte de vaisseaux spatiaux optimale en utilisant l'inventaire disponible de l'univers Star Wars."*

## 🏗️ Architecture Hexagonale

Le projet est divisé en **2 modules Maven** distincts qui incarnent les principes de l'architecture hexagonale :

### 1. **Module `domain/`** - Le Cœur Métier (Hexagone)

C'est le **cœur de l'application**, complètement isolé des détails techniques (bases de données, frameworks, API externes). Il contient :

#### **API (Port d'entrée - côté gauche de l'hexagone)**
- `AssembleAFleet` : Interface représentant le cas d'usage principal
  - Méthode : `forPassengers(int numberOfPassengers)` → retourne une `Fleet`

#### **SPI (Port de sortie - côté droit de l'hexagone)**
- `StarShipInventory` : Interface pour récupérer l'inventaire des vaisseaux
- `Fleets` : Interface pour sauvegarder et récupérer les flottes

#### **Modèle métier**
- `Fleet` : Record représentant une flotte (id + liste de vaisseaux)
- `StarShip` : Record représentant un vaisseau (nom, capacité passagers, capacité cargo)

#### **Service métier**
- `FleetAssembler` : Implémentation du cas d'usage `AssembleAFleet`
  - **Logique métier** : 
    1. Récupère tous les vaisseaux ayant une capacité passagers > 0
    2. Filtre ceux ayant une capacité cargo ≥ 100 000
    3. Trie par capacité passagers (croissant)
    4. Sélectionne le minimum de vaisseaux nécessaires pour transporter tous les passagers
    5. Sauvegarde et retourne la flotte

### 2. **Module `infrastructure/`** - Les Adaptateurs

C'est la **couche technique** qui implémente les ports définis par le domaine :

#### **Adaptateur d'entrée (REST API)**
- `RescueFleetController` : Contrôleur Spring REST
  - `POST /rescueFleets` : Assemble une flotte pour N passagers
  - `GET /rescueFleets/{id}` : Récupère une flotte par son ID
  - Port d'écoute : **1977** (référence à la sortie de Star Wars!)

#### **Adaptateurs de sortie**
- `SwapiClient` : Implémente `StarShipInventory`
  - Se connecte à l'API publique **SWAPI** (Star Wars API) à https://swapi.dev/
  - Récupère la liste complète des vaisseaux Star Wars
  - Gère la pagination de l'API
  - Filtre les valeurs invalides ("n/a", "unknown")
  - Convertit les données SWAPI en objets métier `StarShip`

- `InMemoryFleets` : Implémentation en mémoire de `Fleets` (pour les tests)

#### **Configuration**
- `DomainConfiguration` : Configure le scan des composants du domaine (services annotés `@DomainService`)

## 🔄 Flux d'exécution

```
1. Client HTTP (fichier .http)
   ↓
2. RescueFleetController (@RestController)
   ↓
3. AssembleAFleet (port API)
   ↓
4. FleetAssembler (implémentation)
   ↓
5. StarShipInventory (port SPI) ← SwapiClient (adaptateur)
   ↓
6. Fleets (port SPI) ← InMemoryFleets (adaptateur)
   ↓
7. Retour de la Fleet assemblée
```

## 📝 Exemple d'utilisation

Le fichier `StarWars Rescue Fleet.http` contient un exemple de requête :

```http
POST localhost:1977/rescueFleets
Content-Type: application/json

{
  "numberOfPassengers": 800
}
```

Cette requête va :
1. Interroger l'API SWAPI pour obtenir tous les vaisseaux Star Wars
2. Appliquer l'algorithme d'assemblage de flotte
3. Retourner une flotte optimale (ex: un vaisseau de 800 passagers ou plusieurs plus petits)
4. Sauvegarder la flotte en mémoire

## 🧪 Tests

- **Test fonctionnel** : `AssembleAFleetFunctionalTest`
  - Teste le cas d'usage avec 1050 passagers
  - Utilise des **stubs** (`StarShipInventoryStub`, `InMemoryFleets`)
  - Vérifie que la flotte a assez de capacité totale
  - Vérifie que tous les vaisseaux ont une capacité passagers > 0
  - Vérifie que tous ont une capacité cargo ≥ 100 000

## ✅ Avantages de cette architecture

1. **Indépendance du domaine** : La logique métier ne dépend d'aucune technologie
2. **Testabilité** : Facile de tester avec des stubs/mocks
3. **Flexibilité** : On peut changer l'API externe, la base de données, le framework web sans toucher au domaine
4. **Clarté** : Séparation claire entre "ce que fait l'application" (domaine) et "comment elle le fait" (infrastructure)

## 🎯 Améliorations DDD Ajoutées (POC)

Ce projet a été amélioré pour servir de **Proof of Concept (POC)** complet pour apprendre le DDD avec Spring Boot :

### 1. **Value Objects avec Validation**
- `PassengerCount` : Encapsule le nombre de passagers avec validation (> 0, < 1M)
- `CargoCapacity` : Encapsule la capacité cargo avec validation et comparaison
- `StarShip` : Amélioré avec validation complète du nom et des capacités

### 2. **Aggregate Root**
- `Fleet` : Annoté `@AggregateRoot` avec méthodes métier (`totalPassengerCapacity()`, `canAccommodate()`)
- Garantit ses invariants (au moins un vaisseau, id non-null)
- Liste immuable de vaisseaux

### 3. **Domain Events**
- `FleetAssembledEvent` : Publié quand une flotte est assemblée
- `DomainEventPublisher` : Port pour publier des événements
- `SpringDomainEventPublisher` : Adaptateur Spring
- `FleetAssembledEventListener` : Exemple d'écouteur d'événements

### 4. **Specifications Pattern**
- `StarShipSpecifications` : Encapsule les règles métier réutilisables
  - `hasPassengerCapacity()` : Vérifie la capacité passagers
  - `hasSufficientCargoCapacity()` : Vérifie la capacité cargo minimale
  - `isSuitableForRescue()` : Combine plusieurs critères

### 5. **Domain Exceptions**
- `FleetDomainException` : Exception de base pour le domaine
- `InsufficientStarShipsException` : Levée quand pas assez de vaisseaux
- `FleetNotFoundException` : Levée quand une flotte n'existe pas
- `DomainExceptionHandler` : Convertit les exceptions en réponses HTTP appropriées

### 6. **Repository Pattern Amélioré**
- Interface `Fleets` annotée `@Repository`
- Nouvelles méthodes : `findById()` (Optional), `exists()`
- `InMemoryFleets` mis à jour avec gestion d'erreurs

### 7. **Annotations DDD**
Ajout d'annotations documentées pour identifier les patterns :
- `@ValueObject` : Pour les Value Objects
- `@AggregateRoot` : Pour les Aggregate Roots
- `@DomainService` : Pour les Domain Services
- `@Repository` : Pour les Repositories
- `@DomainEvent` : Pour les Domain Events
- `@Factory` : Pour les Factories (prêt à l'emploi)

### 8. **Tests Enrichis**
- `PassengerCountTest` : Tests du Value Object
- `CargoCapacityTest` : Tests de validation
- `StarShipTest` : Tests avec cas limites
- Tests d'événements dans `AssembleAFleetFunctionalTest`
- Tests d'exceptions pour les cas d'erreur

### 9. **Stubs de Test**
- `InMemoryEventPublisher` : Pour capturer et vérifier les événements publiés
- Méthodes utilitaires : `getEventsOfType()`, `eventCount()`, `clear()`

### 10. **Documentation**
- `DDD_GUIDE.md` : Guide complet des concepts DDD avec exemples
- Javadoc enrichie sur tous les composants
- Références au DDD Reference d'Eric Evans

## 🛠️ Technologies utilisées

- **Java 21**
- **Spring Boot** (REST, IoC)
- **Maven** (build multi-modules)
- **JUnit 5** & **AssertJ** (tests)
- **RestTemplate** (client HTTP pour SWAPI)
- **SWAPI** (Star Wars API publique)

## 📚 Ressources

Le README référence d'excellents articles et vidéos sur l'architecture hexagonale, notamment sur le blog "Beyond xScratch".

---

*Ce projet est une excellente démonstration pédagogique de l'architecture hexagonale appliquée à un cas concret et ludique ! 🚀*

