package com.tizo.delivery.docs;

public final class ExampleJsons {

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
