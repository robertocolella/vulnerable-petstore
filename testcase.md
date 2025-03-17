
# Petstore API Test Cases

## Test Case 1: Add a New Pet (POST /pet)
**Description**: Add a new pet to the store.

**Request**:
```bash
# Test Case 1: Add Pet - Fido
curl -X POST "http://localhost:8081/v2/pet" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "name": "Fido",
    "status": "available",
    "category": {"id": 1, "name": "Dog"},
    "tags": [{"id": 1, "name": "friendly"}],
    "photoUrls": ["url1", "url2"]
  }'

# Test Case 2: Add Pet - Bella
curl -X POST "http://localhost:8081/v2/pet" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "name": "Bella",
    "status": "available",
    "category": {"id": 2, "name": "Cat"},
    "tags": [{"id": 2, "name": "playful"}],
    "photoUrls": ["url3", "url4"]
  }'

# Test Case 3: Add Pet - Rex
curl -X POST "http://localhost:8081/v2/pet" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "name": "Rex",
    "status": "sold",
    "category": {"id": 1, "name": "Dog"},
    "tags": [{"id": 3, "name": "loyal"}],
    "photoUrls": ["url5", "url6"]
  }'

# Test Case 4: Add Pet - Luna
curl -X POST "http://localhost:8081/v2/pet" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "name": "Luna",
    "status": "available",
    "category": {"id": 3, "name": "Rabbit"},
    "tags": [{"id": 4, "name": "cute"}],
    "photoUrls": ["url7", "url8"]
  }'

# Test Case 5: Add Pet - Simba
curl -X POST "http://localhost:8081/v2/pet" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "name": "Simba",
    "status": "pending",
    "category": {"id": 1, "name": "Dog"},
    "tags": [{"id": 5, "name": "brave"}],
    "photoUrls": ["url9", "url10"]
  }'

```
  

```bash 
curl -X GET "http://localhost:8081/v2/pet" -H "Accept: application/json"
```


```bash 
curl -X GET "http://localhost:8081/v2/pet/findByStatus?status=available" -H "Accept: application/json"
```

```bash 
curl -X GET "http://localhost:8081/v2/pet/findByTags?tags=friendly" -H "Accept: application/json"
```

```bash 

curl -X GET "http://localhost:8081/v2/pet/1" -H "Accept: application/json"
```

```bash 

curl -X PUT "http://localhost:8081/v2/pet" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "id": 1,
    "name": "Fido Updated",
    "status": "sold",
    "category": {"id": 1, "name": "Dog"},
    "tags": [{"id": 1, "name": "friendly"}],
    "photoUrls": ["url1", "url2"]
  }'
```

```bash 
curl -X DELETE "http://localhost:8081/v2/pet/1" \
  -H "Accept: application/json"
```