package com.tizo.delivery.docs;

public final class ExampleJsons {

    public static final String STORE_RESPONSE_FICTICIO = """
        {
            "id": "11111111-2222-3333-4444-555555555555",
            "storeSlug": "ficticio-lanches",
            "name": "Fictício Lanches",
            "address": {
                "street": "Rua Inventada",
                "number": "100",
                "neighborhood": "Centro Fictício",
                "city": "Cidade Exemplo",
                "cep": "12345000"
            },
            "phoneNumber": "+5511999999999",
            "productCategories": [
                "Lanches",
                "Bebidas",
                "Sobremesas"
            ],
            "allCategories": [
                "Lanches",
                "Bebidas",
                "Sobremesas"
            ],
            "products": {
                "content": [
                    {
                        "id": 1001,
                        "name": "X-Burguer Fictício",
                        "description": "Pão, carne de soja, queijo, alface e tomate",
                        "imagePath": "http://localhost:8080/images/products/ficticio-lanches/xburguer.jpg",
                        "category": "Lanches",
                        "productSizes": [
                            { "id": 201, "sizeName": "Pequeno", "price": 15.00, "size": "150g" },
                            { "id": 202, "sizeName": "Médio", "price": 20.00, "size": "200g" },
                            { "id": 203, "sizeName": "Grande", "price": 25.00, "size": "300g" }
                        ],
                        "extrasGroups": [
                            {
                                "id": 301,
                                "name": "Queijos",
                                "minSelections": 0,
                                "maxSelections": 2,
                                "extras": [
                                    { "id": 401, "name": "Cheddar", "value": 2.50, "limit": 1, "active": true },
                                    { "id": 402, "name": "Catupiry", "value": 2.50, "limit": 1, "active": true }
                                ]
                            },
                            {
                                "id": 302,
                                "name": "Molhos",
                                "minSelections": 0,
                                "maxSelections": 3,
                                "extras": [
                                    { "id": 403, "name": "Maionese", "value": 1.00, "limit": 1, "active": true },
                                    { "id": 404, "name": "Barbecue", "value": 1.50, "limit": 1, "active": true },
                                    { "id": 405, "name": "Mostarda", "value": 1.00, "limit": 1, "active": true }
                                ]
                            }
                        ]
                    },
                    {
                        "id": 1002,
                        "name": "Suco Fictício",
                        "description": "Suco natural de laranja ou morango",
                        "imagePath": "http://localhost:8080/images/products/ficticio-lanches/suco.jpg",
                        "category": "Bebidas",
                        "productSizes": [
                            { "id": 204, "sizeName": "Copo Pequeno", "price": 5.00, "size": "200ml" },
                            { "id": 205, "sizeName": "Copo Grande", "price": 8.00, "size": "400ml" }
                        ],
                        "extrasGroups": []
                    },
                    {
                        "id": 1003,
                        "name": "Brownie Fictício",
                        "description": "Brownie de chocolate com nozes",
                        "imagePath": "http://localhost:8080/images/products/ficticio-lanches/brownie.jpg",
                        "category": "Sobremesas",
                        "productSizes": [
                            { "id": 206, "sizeName": "Unidade", "price": 7.50, "size": "" }
                        ],
                        "extrasGroups": [
                            {
                                "id": 303,
                                "name": "Coberturas",
                                "minSelections": 0,
                                "maxSelections": 2,
                                "extras": [
                                    { "id": 406, "name": "Chocolate Branco", "value": 1.50, "limit": 1, "active": true },
                                    { "id": 407, "name": "Chocolate Preto", "value": 1.50, "limit": 1, "active": true }
                                ]
                            }
                        ]
                    }
                ],
                "page": 0,
                "size": 20,
                "totalElements": 3,
                "totalPages": 1
            }
        }
    """;

    public static final String STORE_RESPONSE = """
        {
            "id": "ada10422-c7af-4f12-84a3-005ffa15586c",
            "storeSlug": "bar-do-arroz",
            "name": "Bar do arroz",
            "address": {
                "street": "Rua dos bobos",
                "number": "12",
                "neighborhood": "Catabi",
                "city": "Coitadolandia",
                "cep": "58475000"
            },
            "phoneNumber": "+5583993849744",
            "productCategories": [
                "Acompanhamento",
                "Cachorro quente",
                "Pastel",
                "Pizza"
            ],
            "allCategories": [
                "Acompanhamento",
                "Cachorro quente",
                "Pastel",
                "Pizza"
            ],
            "products": {
                "content": [
                    {
                        "id": 885,
                        "name": "Batata Rústica",
                        "description": "Batata cortada em palitos grossos com casca e temperos",
                        "imagePath": "http://192.168.0.123:8080/images/products/bar-do-arroz/93a15f63-0190-4a4c-8882-dabb912f862a_batata-rusticajpg",
                        "category": "Acompanhamento"
                    },
                    {
                        "id": 882,
                        "name": "Cachorro quente",
                        "description": "Cachorro quente de frango ou de carne, com vinagrete, batata palha e milho e ervilha",
                        "imagePath": "http://192.168.0.123:8080/images/products/bar-do-arroz/333ac6c8-a6cb-43b5-bd09-dab62a4065f8_cachorro-quentejpg",
                        "category": "Cachorro quente"
                    }
                ],
                "page": 0,
                "size": 20,
                "totalElements": 4,
                "totalPages": 1
            }
        }
    """;

    public static final String ORDER = """
            {
              "id": "123e4567-e89b-12d3-a456-426614174000",
              "storeName": "Bar do Fictício",
              "orderStatus": "PENDING",
              "createdAt": "2025-10-11T12:27:57.644879Z",
              "updatedAt": "2025-10-11T12:30:00.000000Z",
              "observation": "Sem cebola, por favor",
              "payment": {
                "method": "PIX",
                "paymentStatus": "PENDING",
                "totalAmount": 50.0,
                "discount": 0.0,
                "fee": 5.0,
                "finalAmount": 55.0,
                "change": 0.0
              },
              "customerInfo": {
                "id": 1,
                "name": "João da Silva",
                "phoneNumber": "11999998888",
                "address": {
                  "street": "Rua das Flores",
                  "number": "123",
                  "neighborhood": "Jardim Fictício"
                }
              },
              "orderItems": [
                {
                  "id": 1,
                  "productId": 882,
                  "productName": "Cachorro quente",
                  "size": "20 cm",
                  "sizeDescription": "Médio",
                  "unitPrice": 15.0,
                  "quantity": 2,
                  "subtotal": 30.0,
                  "extrasTotal": 5.0,
                  "total": 35.0,
                  "extrasGroup": [
                    {
                      "name": "Adicionais",
                      "extras": [
                        { "id": 1, "name": "Queijo cheddar", "price": 2.0, "limit": 1, "quantity": 1 },
                        { "id": 2, "name": "Ovo de codorna", "price": 0.50, "limit": 1, "quantity": 1 }
                      ]
                    }
                  ]
                }
              ],
              "_links": {
                "self": { "href": "http://localhost:8080/api/v1/orders/1/123e4567-e89b-12d3-a456-426614174000" },
                "storeHome": { "href": "http://localhost:8080/api/v1/store/1?page=0&size=100" }
              }
            }
            """;

    public static final String ORDERS_PAGED = """
            {
              "_embedded": {
                "orderResponseDtoList": [
                  %s
                ]
              },
              "page": {
                "size": 10,
                "totalElements": 2,
                "totalPages": 1,
                "number": 0
              }
            }
            """;

    public static final String ORDERS_TODAY = """
            {
              "content": [
                %s
              ],
              "page": 0,
              "size": 50,
              "totalElements": 1,
              "totalPages": 1,
              "pageable": {
                "pageNumber": 0,
                "pageSize": 50,
                "sort": { "empty": false, "sorted": true, "unsorted": false },
                "offset": 0,
                "paged": true,
                "unpaged": false
              }
            }
            """;

    public static final String PRODUCT_LIST = """
            [
                {
                    "id": 882,
                    "name": "Cachorro quente",
                    "description": "Cachorro quente de frango ou de carne, com vinagrete, batata palha e milho e ervilha",
                    "imagePath": "http://localhost:8080/images/products/bar-do-arroz/333ac6c8-a6cb-43b5-bd09-dab62a4065f8_cachorro-quentejpg",
                    "category": "Cachorro quente",
                    "productSizes": [
                        {
                            "id": 122,
                            "sizeName": "Grande",
                            "price": 18.00,
                            "size": "25 cm"
                        },
                        {
                            "id": 123,
                            "sizeName": "Médio",
                            "price": 15.00,
                            "size": "20 cm"
                        },
                        {
                            "id": 124,
                            "sizeName": "Pequeno",
                            "price": 12.00,
                            "size": "15"
                        }
                    ],
                    "extrasGroups": [
                        {
                            "id": 29,
                            "name": "Tipo de carne",
                            "minSelections": 1,
                            "maxSelections": 1,
                            "extras": [
                                {
                                    "id": 115,
                                    "name": "Frango",
                                    "value": 0.00,
                                    "limit": 1,
                                    "active": true
                                },
                                {
                                    "id": 116,
                                    "name": "Carne",
                                    "value": 0.00,
                                    "limit": 1,
                                    "active": true
                                }
                            ]
                        },
                        {
                            "id": 30,
                            "name": "Adicionais",
                            "minSelections": 0,
                            "maxSelections": 3,
                            "extras": [
                                {
                                    "id": 117,
                                    "name": "Ovo de codorna",
                                    "value": 0.50,
                                    "limit": 1,
                                    "active": true
                                },
                                {
                                    "id": 118,
                                    "name": "Batata palha",
                                    "value": 1.00,
                                    "limit": 1,
                                    "active": true
                                },
                                {
                                    "id": 119,
                                    "name": "Queijo cheddar",
                                    "value": 2.00,
                                    "limit": 1,
                                    "active": true
                                },
                                {
                                    "id": 120,
                                    "name": "Queijo Catupiry",
                                    "value": 2.00,
                                    "limit": 1,
                                    "active": true
                                }
                            ]
                        }
                    ]
                },
                {
                    "id": 883,
                    "name": "Pastel de Frango",
                    "description": "Pastel de frango",
                    "imagePath": "http://localhost:8080/images/products/bar-do-arroz/26947f36-b324-4a2e-b077-d875e23a784f_pasteljpg",
                    "category": "Pastel",
                    "productSizes": [
                        {
                            "id": 125,
                            "sizeName": "Grande",
                            "price": 22.00,
                            "size": "25 cm"
                        },
                        {
                            "id": 126,
                            "sizeName": "Médio",
                            "price": 18.00,
                            "size": "20 cm"
                        }
                    ],
                    "extrasGroups": [
                        {
                            "id": 31,
                            "name": "Queijos",
                            "minSelections": 0,
                            "maxSelections": 2,
                            "extras": [
                                {
                                    "id": 121,
                                    "name": "Queijo cheddar",
                                    "value": 5.00,
                                    "limit": 1,
                                    "active": true
                                },
                                {
                                    "id": 122,
                                    "name": "Queijo Catupiry",
                                    "value": 2.00,
                                    "limit": 1,
                                    "active": true
                                }
                            ]
                        }
                    ]
                },
                {
                    "id": 884,
                    "name": "Pizza de calabresa",
                    "description": "Pizza de calabresa, tomate, cebola, azeitona preta e molho",
                    "imagePath": "http://localhost:8080/images/products/bar-do-arroz/a94ede24-508e-4781-b0cb-badccf2558f3_pizza-de-calabresajpg",
                    "category": "Pizza",
                    "productSizes": [
                        {
                            "id": 127,
                            "sizeName": "Grande",
                            "price": 40.00,
                            "size": "40 cm"
                        },
                        {
                            "id": 128,
                            "sizeName": "Médio",
                            "price": 35.00,
                            "size": "30 cm"
                        },
                        {
                            "id": 129,
                            "sizeName": "Pequeno",
                            "price": 30.00,
                            "size": "25 cm"
                        }
                    ],
                    "extrasGroups": [
                        {
                            "id": 32,
                            "name": "Molhos",
                            "minSelections": 0,
                            "maxSelections": 3,
                            "extras": [
                                {
                                    "id": 123,
                                    "name": "Borda de cheddar",
                                    "value": 10.00,
                                    "limit": 1,
                                    "active": true
                                },
                                {
                                    "id": 124,
                                    "name": "Borda de catupirry",
                                    "value": 10.00,
                                    "limit": 1,
                                    "active": true
                                }
                            ]
                        }
                    ]
                },
                {
                    "id": 885,
                    "name": "Batata Rústica",
                    "description": "Batata cortada em palitos grossos com casca e temperos",
                    "imagePath": "http://localhost:8080/images/products/bar-do-arroz/93a15f63-0190-4a4c-8882-dabb912f862a_batata-rusticajpg",
                    "category": "Acompanhamento",
                    "productSizes": [
                        {
                            "id": 130,
                            "sizeName": "Média",
                            "price": 12.50,
                            "size": ""
                        },
                        {
                            "id": 131,
                            "sizeName": "Grande",
                            "price": 16.50,
                            "size": ""
                        }
                    ],
                    "extrasGroups": [
                        {
                            "id": 33,
                            "name": "Molhos",
                            "minSelections": 0,
                            "maxSelections": 3,
                            "extras": [
                                {
                                    "id": 125,
                                    "name": "Ketchup",
                                    "value": 0.50,
                                    "limit": 1,
                                    "active": true
                                },
                                {
                                    "id": 126,
                                    "name": "Maionese",
                                    "value": 0.50,
                                    "limit": 1,
                                    "active": true
                                },
                                {
                                    "id": 127,
                                    "name": "Barbecue",
                                    "value": 1.00,
                                    "limit": 1,
                                    "active": true
                                }
                            ]
                        }
                    ]
                }
            ]
            """;

    private ExampleJsons() {
    }

    public static final String STORE_CREATED_RESPONSE = """
            {
                "id": "5eea5710-b410-4192-85e0-d6ceb863a1eb",
                "name": "Bar da carne",
                      "slug": "bar-da-carne",
                      "phoneNumber": "5511999998888",
                      "address": {
                         "street": "Rua1",
                         "number": "01",
                      "neighborhood": "Bairro Y",
                      "city": "Cidade X",
                      "cep": "54321-000"
                      }
              }
            """;

    public static final String PRODUCT = """
            {
              "id": 1,
              "name": "Cachorro quente",
              "description": "Pão com salsicha, vinagrete, batata palha e carne moída",
              "imagePath": "http://localhost:8080/images/hamburguer.jpg",
              "category": "Lanches",
              "productSizes": [
                { "id": 1, "sizeName": "Médio", "price": 25.5, "size": "20 cm" },
                { "id": 2, "sizeName": "Grande", "price": 35.0, "size": "25 cm" }
              ],
              "extrasGroups": [
                {
                  "id": 1,
                  "name": "Molhos",
                  "minSelections": 0,
                  "maxSelections": 3,
                  "extras": [
                    { "id": 1, "name": "Barbecue", "value": 3.5, "limit": 1, "active": true },
                    { "id": 2, "name": "Mostarda", "value": 2.0, "limit": 1, "active": true }
                  ]
                }
              ]
            }
            """;
}
