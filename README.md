# Desafio Bexs

### Pré requisitos
* Java 11
* Maven 3
* GIT

### Executar a aplicação
Para gerar o artefato (jar), executar o seguinte comando na raiz do projeto:
* mvn clean install

Dentro da pasta `target` será gerado o `artefato route-0.0.1-SNAPSHOT.jar`. Para executar o projeto use o comando a seguir:
* java -jar target/route-0.0.1-SNAPSHOT.jar input-routes.csv

Ao executar a aplicação, será possível buscar as rotas tanto pela linha de comando ou pela API Rest.

### Estrutura de pacotes
```
scr
└── main
    └── java
        └── com.gmail.tthiagoaze.route
            ├── bo              # pacote para objetos de business
            ├── controller      # pacote para classes de Rest Controller
            ├── dto             # pacote para classes de objetos de request e response
            ├── exception       # pacote para classes para tratamento de exceções
            ├── model           # pacote para classes que representam os modelos/entidades
            ├── repository      # pacote para classes de persistência de dados
            ├── service         # pacote para classes de regras de negócio
            ├── util            # pacote para classes utilitárias
```

### Solução
* Service abstrai as regras de negócio e cada service tem sua responsábilidade.
* Repository é responsável pela abstração de leitura/gravação de dados, sendo assim, se houver uma mudança para um banco de dados, somente será necessário alterar esta camada.
* Cache foi criado para evitar o reprocessamento custoso da busca de rota.

### APIs
* Criar uma nova rota
    * POST http://localhost:8080/route
    * Payload
    ```
    {
        "departure": "AAA",
        "arrival": "BBB",
        "price": 5
    }
    ```
  ```
  curl --location --request POST 'http://localhost:8080/route' \
  --header 'Content-Type: application/json' \
  --data-raw '{
     "departure": "AAA",
     "arrival": "BBB",
     "price": 5
  }'
  ```
  
* Buscar uma rota
    * GET http://localhost:8080/route/GRU/CDG
    ```
    curl --location --request GET 'http://localhost:8080/route/GRU/CDG'
    ```
  
* Swagger

    As APIs podem ser consultadas pelo Swagger na seguinte url: http://localhost:8080/swagger-ui.html