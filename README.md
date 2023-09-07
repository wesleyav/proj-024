![](http://img.shields.io/static/v1?label=STATUS&message=EM%20DESENVOLVIMENTO&color=GREEN&style=for-the-badge)

# Data Transfer Object

Uma classe DTO (Data Transfer Object) em Java é frequentemente usada em APIs para encapsular dados que são transmitidos entre o cliente e o servidor. Ela serve como um objeto intermediário que ajuda a estruturar e organizar os dados transferidos, tornando a comunicação mais eficiente e menos propensa a erros. No contexto de uma API HTTP, uma classe DTO pode desempenhar várias funções importantes:

1. **Filtrar dados indesejados**: Uma classe DTO pode ser usada para selecionar apenas os campos relevantes da requisição HTTP que devem ser processados pelo servidor. Isso ajuda a reduzir a sobrecarga de processamento no servidor, evitando que ele processe informações desnecessárias.
2. **Controlar a exposição de dados**: Em muitos casos, você não deseja expor todos os detalhes internos de seus objetos de domínio diretamente na API. Uma classe DTO permite que você exponha apenas os campos que são apropriados para a comunicação externa, mantendo a encapsulamento e a segurança dos dados internos.
3. **Versionamento**: À medida que sua API evolui ao longo do tempo, você pode precisar adicionar, remover ou modificar campos nos objetos de domínio. Usar classes DTO permite que você gerencie a versão da API de forma mais eficaz, introduzindo novas versões de DTOs sem afetar a estrutura interna dos objetos de domínio.
4. **Reduzir acoplamento**: Usar uma classe DTO ajuda a reduzir o acoplamento entre o cliente e o servidor. O cliente não precisa saber como os objetos de domínio são estruturados internamente, apenas precisa conhecer a estrutura dos DTOs. Isso facilita a manutenção e a evolução da API.
5. **Validação**: Uma classe DTO também pode ser usada para validar os dados da requisição antes de processá-los. Isso ajuda a garantir que os dados recebidos sejam consistentes e válidos antes de serem utilizados pelo servidor.

Este projeto é uma implementação simples de uma API REST com o intuito de entender melhor o funcionamento de classes DTO. Essa API oferece funcionalidades de consulta e manipulação de dados baseado em uma classe fictícia denominada `address`.

## Planejamento das classes

Entity - representa a entidade e reflete uma tabela no banco de dados.
RequestDTO - representa os dados permitidos a serem enviados na requisição.
ResponseDTO - representa os dados a serem retornados da requisição.
UpdateDTO - representa somente os dados que podem ser atualizados pelo usuário.

| Address    | AddressRequestDTO | AddressResponseDTO | AddressUpdateDTO |
| :--------- | :---------------- | :----------------- | :--------------- |
| addressId  |                   |                    |                  |
| address    | address           | address            | address          |
| address2   | address2          | address2           | address2         |
| district   | district          | district           | district         |
| postalCode | postalCode        | postalCode         |                  |
| phone      | phone             | phone              | phone            |
| lastUpdate |                   | lastUpdate         |                  |

## Diagrama de classes

![](docs/diagrama-de-classes.png)

## Endpoints

### Address

| Método HTTP | Prefixo | Endpoint                 | Descrição                                                   |
| ----------- | ------- | ------------------------ | ----------------------------------------------------------- |
| GET         | /api/v1 | /addresses               | Retorna uma lista de addresses                              |
| GET         | /api/v1 | /addresses-with-response | Retorna uma lista de addresses com o response personalizado |
| GET         | /api/v1 | /addresses/1             | Retorna o address com o id 1                                |
| POST        | /api/v1 | /addresses               | Cria um address                                             |
| PUT         | /api/v1 | /addresses/1             | Atualiza o address de id 1                                  |
| DELETE      | /api/v1 | /addresses/1             | Remove o address com o id 1                                 |

POST - `api/v1/addresses`

```json
{
    "address": "address",
    "address2": "address2",
    "district": "district",
    "postalCode": "postalCode",
    "phone": "phone"
}
```

GET - `api/v1/addresses`

```json
[
    {
        "addressId": 1,
        "address": "address",
        "address2": "address2",
        "district": "district",
        "postalCode": "postalCode",
        "phone": "phone",
        "lastUpdate": "2023-09-07T02:08:45.280904Z"
    }
]
```

GET - `addresses-with-response`

```json
[
    {
        "address": "address",
        "address2": "address2",
        "district": "district",
        "postalCode": "postalCode",
        "phone": "phone",
        "lastUpdate": "2023-09-07T02:08:45.280904Z"
    }
]
```

PUT - `addresses/1`

```json
{
    "address": "atualizado",
    "address2": "atualizado",
    "district": "atualizado",
    "phone": "atualizado"
}
```
