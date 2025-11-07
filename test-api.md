# Test de l'API Star Wars Rebels Rescue

## Assembler une flotte de secours

```bash
# Assembler une flotte pour 1050 passagers
curl -X POST http://localhost:8080/rescueFleets \
  -H "Content-Type: application/json" \
  -d '{"numberOfPassengers": 1050}'
```

## Récupérer une flotte par son ID
```bash
# Remplacer {id} par l'ID retourné dans la réponse précédente
curl http://localhost:8080/rescueFleets/{id}
```

## Exemples avec Postman

1. **Assembler une flotte (POST)**
   - URL: `http://localhost:8080/rescueFleets`
   - Method: `POST`
   - Headers: 
     - `Content-Type: application/json`
   - Body (raw JSON):
     ```json
     {
         "numberOfPassengers": 1050
     }
     ```

2. **Obtenir une flotte (GET)**
   - URL: `http://localhost:8080/rescueFleets/{id}`
   - Method: `GET`
   - Remplacer `{id}` par l'ID reçu dans la réponse du POST

## Réponses attendues

### POST /rescueFleets
```json
{
    "id": "uuid-de-la-flotte",
    "starships": [
        {
            "name": "nom-du-vaisseau",
            "capacity": nombre-de-passagers
        }
    ]
}
```

### GET /rescueFleets/{id}
Même format que la réponse du POST.

## Gestion des erreurs

- **400 Bad Request** : Si le nombre de passagers est invalide
- **404 Not Found** : Si la flotte demandée n'existe pas
- **500 Internal Server Error** : Si une erreur interne survient
