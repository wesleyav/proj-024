### Country

#### createCountry
```json
{
    "name": "Brasil"
}
```

#### updateCountry
```json
{
    "name": "Espanha"
}
```

### City

#### createCityWithCountry
```json
{
    "city": "São Paulo",
    "country": {
        "country": "Brasil"
    }
}
```

#### updateCityById
```json
{
    "name": "São Paulo"
}
```

### Address

#### createAddress
```json
{
    "address": "2202 Avenida Paulista",
    "address2": "2202 Avenida Paulista",
    "district": "São Paulo",
    "postalCode": "01310-932",
    "phone": "99999999999",
    "cityId": {
        "name": "São Paulo",
        "country": {
            "name": "Brasil"
        }
    }
}
```

#### updateAddressById
```json
{
    "address2": "Novo endereço 2",
    "district": "Novo distrito",
    "phone": "Novo telefone"
}
```