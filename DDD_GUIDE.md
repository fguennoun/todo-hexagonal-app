# Guide DDD - Concepts et Exemples

Ce guide explique les concepts DDD (Domain-Driven Design) utilisés dans ce projet.

## 📚 Concepts DDD Implémentés

### 1. **Value Objects** (@ValueObject)

Les Value Objects sont des objets immuables définis uniquement par leurs attributs. Deux Value Objects avec les mêmes valeurs sont considérés égaux.

**Exemples dans ce projet :**
- `StarShip` : Représente un vaisseau spatial
- `CargoCapacity` : Encapsule la capacité de cargo avec validation
- `PassengerCount` : Encapsule le nombre de passagers avec validation

```java
@ValueObject
public record StarShip(String name, int passengersCapacity, CargoCapacity cargoCapacity) {
    // Validation dans le constructeur compact
    public StarShip {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("StarShip name cannot be blank");
        }
    }
}
```

**Caractéristiques :**
- Immuables (records Java)
- Validation dans le constructeur
- Égalité basée sur les valeurs
- Pas d'identité propre

### 2. **Aggregate Root** (@AggregateRoot)

Un Aggregate Root est le point d'entrée d'un agrégat. Tous les accès externes doivent passer par lui pour garantir la cohérence.

**Exemple dans ce projet :**
- `Fleet` : Agrégat représentant une flotte de vaisseaux

```java
@AggregateRoot
public record Fleet(UUID id, List<StarShip> starships) {
    public int totalPassengerCapacity() {
        return StarShip.totalPassengerCapacity(starships);
    }
    
    public boolean canAccommodate(int passengers) {
        return totalPassengerCapacity() >= passengers;
    }
}
```

**Caractéristiques :**
- Possède une identité (UUID)
- Encapsule les règles métier
- Garantit ses invariants
- Point d'entrée unique pour les modifications

### 3. **Domain Service** (@DomainService)

Un Domain Service contient de la logique métier qui ne s'intègre pas naturellement dans une entité ou un Value Object.

**Exemple dans ce projet :**
- `FleetAssembler` : Service qui orchestre l'assemblage d'une flotte

```java
@DomainService
public class FleetAssembler implements AssembleAFleet {
    public Fleet forPassengers(int numberOfPassengers) {
        PassengerCount passengerCount = new PassengerCount(numberOfPassengers);
        // ... logique d'assemblage
    }
}
```

**Quand utiliser un Domain Service :**
- Logique qui implique plusieurs agrégats
- Opérations qui ne sont pas naturelles sur une entité
- Coordination entre différents composants du domaine

### 4. **Repository** (@Repository)

Un Repository fournit l'illusion d'une collection en mémoire pour les Aggregate Roots.

**Exemple dans ce projet :**
- `Fleets` : Repository pour les flottes

```java
@Repository
public interface Fleets {
    Fleet getById(UUID id);
    Optional<Fleet> findById(UUID id);
    Fleet save(Fleet fleet);
    boolean exists(UUID id);
}
```

**Bonnes pratiques :**
- Travaille uniquement avec les Aggregate Roots
- Interface orientée collection
- Cache les détails de persistance

### 5. **Domain Events** (@DomainEvent)

Les Domain Events représentent des faits qui se sont produits dans le domaine.

**Exemple dans ce projet :**
- `FleetAssembledEvent` : Événement publié quand une flotte est assemblée

```java
@DomainEvent
public record FleetAssembledEvent(
    UUID eventId,
    UUID fleetId,
    int numberOfStarShips,
    int totalPassengerCapacity,
    int requestedPassengers,
    Instant occurredAt
) {}
```

**Utilisation :**
```java
// Publication
eventPublisher.publish(new FleetAssembledEvent(...));

// Écoute (dans l'infrastructure)
@EventListener
public void onFleetAssembled(FleetAssembledEvent event) {
    logger.info("Fleet assembled: {}", event.fleetId());
}
```

### 6. **Specifications Pattern**

Les Specifications encapsulent les règles métier sous forme de prédicats réutilisables.

**Exemple dans ce projet :**
- `StarShipSpecifications` : Règles pour sélectionner les vaisseaux

```java
public class StarShipSpecifications {
    public static Predicate<StarShip> hasPassengerCapacity() {
        return starShip -> starShip.passengersCapacity() > 0;
    }
    
    public static Predicate<StarShip> isSuitableForRescue() {
        return hasPassengerCapacity().and(hasSufficientCargoCapacity());
    }
}
```

### 7. **Domain Exceptions**

Les exceptions du domaine expriment les violations de règles métier.

**Exemples dans ce projet :**
- `FleetDomainException` : Exception de base
- `InsufficientStarShipsException` : Pas assez de vaisseaux disponibles
- `FleetNotFoundException` : Flotte introuvable

```java
throw new InsufficientStarShipsException(requiredPassengers, availableCapacity);
```

## 🏗️ Architecture en Couches

### Domaine (domain/)
- **Pur** : Pas de dépendances vers l'infrastructure
- **Testable** : Tests unitaires sans framework
- **Exprime** : Le langage métier (Ubiquitous Language)

### Infrastructure (infrastructure/)
- **Adaptateurs** : Implémente les ports du domaine
- **Techniques** : Spring Boot, REST, HTTP clients
- **Jetable** : Peut être remplacée sans toucher au domaine

## 📋 Checklist DDD

Quand vous ajoutez une nouvelle fonctionnalité :

- [ ] Les concepts métier sont-ils nommés selon le langage ubiquitaire ?
- [ ] Les Value Objects sont-ils immuables et validés ?
- [ ] Les Aggregate Roots protègent-ils leurs invariants ?
- [ ] Les Domain Services contiennent-ils uniquement de la logique métier ?
- [ ] Les Repositories travaillent-ils avec les Aggregate Roots ?
- [ ] Les événements du domaine sont-ils publiés aux moments clés ?
- [ ] Les exceptions expriment-elles des violations de règles métier ?
- [ ] Le domaine est-il indépendant de l'infrastructure ?

## 🧪 Tests DDD

### Tests de Value Objects
```java
@Test
void should_reject_invalid_values() {
    assertThatThrownBy(() -> new PassengerCount(-1))
        .isInstanceOf(IllegalArgumentException.class);
}
```

### Tests de Domain Services
```java
@Test
void should_assemble_fleet() {
    // Given - Arrange avec des stubs
    AssembleAFleet service = new FleetAssembler(stubInventory, stubFleets, stubPublisher);
    
    // When - Act
    Fleet fleet = service.forPassengers(100);
    
    // Then - Assert sur le comportement métier
    assertThat(fleet.canAccommodate(100)).isTrue();
}
```

### Tests d'événements
```java
@Test
void should_publish_event_when_fleet_assembled() {
    InMemoryEventPublisher publisher = new InMemoryEventPublisher();
    // ... créer service avec publisher
    
    service.forPassengers(100);
    
    assertThat(publisher.getEventsOfType(FleetAssembledEvent.class))
        .hasSize(1);
}
```

## 📖 Ressources

- [DDD Reference - Eric Evans](https://www.domainlanguage.com/wp-content/uploads/2016/05/DDD_Reference_2015-03.pdf)
- [Architecture Hexagonale](https://beyondxscratch.com/2017/08/19/hexagonal-architecture-the-practical-guide-for-a-clean-architecture/)

---

**Note :** Ce projet est un POC (Proof of Concept) pour apprendre et comprendre les concepts DDD avec Spring Boot.

