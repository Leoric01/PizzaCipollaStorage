#!/bin/bash

curl -X POST http://localhost:8080/api/branches \
  -H "Content-Type: application/json" \
  -d '{"id": "c5934818-6797-4281-bcf7-ce57ddb0be5d", "name": "PPC Kamýk", "address": "Some address", "contactInfo": "+420 123 456 789"}'

curl -X POST http://localhost:8080/api/branches \
  -H "Content-Type: application/json" \
  -d '{"id": "60944b76-5720-42f0-87de-b7501786c374", "name": "PPC Háje", "address": "Some address", "contactInfo": "+420 123 456 789"}'

curl -X POST http://localhost:8080/api/branches \
  -H "Content-Type: application/json" \
  -d '{"id": "29147ca4-0a23-4bb1-a20b-be86ef13cc75", "name": "PPC Smíchov", "address": "Some address", "contactInfo": "+420 123 456 789"}'

curl -X POST http://localhost:8080/api/ingredients \
  -H "Content-Type: application/json" \
  -d '{"id": "dac2d462-ca95-4c6c-8232-e3f355a7b60d", "name": "Šunka", "unit": "kg", "defaultForm": "RAW"}'

curl -X POST http://localhost:8080/api/ingredients \
  -H "Content-Type: application/json" \
  -d '{"id": "5107faf0-e32b-429c-b3ed-b771469865a4", "name": "Sýr", "unit": "kg", "defaultForm": "RAW"}'

curl -X POST http://localhost:8080/api/ingredients \
  -H "Content-Type: application/json" \
  -d '{"id": "39e9bfa0-3473-4225-a9c5-cabd1eb71aa1", "name": "Rajčatová omáčka", "unit": "kg", "defaultForm": "RAW"}'

curl -X POST http://localhost:8080/api/ingredients \
  -H "Content-Type: application/json" \
  -d '{"id": "8c8c0bdd-1af2-49be-899b-7def78518175", "name": "Těsto", "unit": "kg", "defaultForm": "RAW"}'

curl -X POST http://localhost:8080/api/ingredients \
  -H "Content-Type: application/json" \
  -d '{"id": "a16721a6-7d74-4c58-993e-287862ff1e7d", "name": "Oregano", "unit": "kg", "defaultForm": "RAW"}'

curl -X POST http://localhost:8080/api/menu-items \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Pizza Margherita",
    "price": 159.0,
    "vatRate": "LOW",
    "category": "Pizza",
    "ingredients": [
        {"ingredientId": "8c8c0bdd-1af2-49be-899b-7def78518175", "quantity": 1.0}, {"ingredientId": "39e9bfa0-3473-4225-a9c5-cabd1eb71aa1", "quantity": 0.2}, {"ingredientId": "5107faf0-e32b-429c-b3ed-b771469865a4", "quantity": 0.15}, {"ingredientId": "a16721a6-7d74-4c58-993e-287862ff1e7d", "quantity": 0.005}
    ]
}'
