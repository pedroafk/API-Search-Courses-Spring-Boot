# README - Search Courses

## Descrição do Projeto

Este projeto é uma API Spring Boot desenvolvida para gerenciar posts, sites e contagens de cliques. Ele oferece endpoints para buscar posts, registrar cliques e obter estatísticas de cliques por post.

## Tecnologias Utilizadas

- **Java 21**
- **Spring Boot**
- **Spring Data JPA**
- **Hibernate**
- **Maven**
- **MySQL**

## Estrutura do Projeto

O projeto está organizado nos seguintes pacotes:

- `com.searchcourses.api.configs`: Contém classes de configuração da aplicação.
- `com.searchcourses.api.controllers`: Contém os controladores da API.
- `com.searchcourses.api.dtos`: Contém objetos de transferência de dados (DTOs).
- `com.searchcourses.api.entities`: Contém as entidades JPA.
- `com.searchcourses.api.exceptions`: Contém classes de tratamento de exceções.
- `com.searchcourses.api.repositories`: Contém os repositórios JPA para acesso aos dados.
- `com.searchcourses.api.service`: Contém as classes de serviço/business logic.

## Entidades Principais

1. **Posts**: Representa um post com título, resumo, URL, data de publicação e relacionamento com um site.
2. **Sites**: Representa um site com nome, URL, RSS, descrição e uma lista de posts associados.
3. **ClickCount**: Registra a contagem de cliques em um post por data.

---

## Endpoints de Posts

### Listar Posts
**GET** `/api/v2/post`

Retorna todos os posts ordenados por data de publicação (decrescente).

#### Parâmetros Query
| Parâmetro | Tipo   | Obrigatório | Descrição                          |
|-----------|--------|-------------|------------------------------------|
| `content` | string | Não         | Filtra posts por título ou resumo |

#### Resposta de Sucesso - Status Code 200
```json
[
    {
        "id": 74232,
        "title": "🐞 Debug no Laravel com Estilo: Conheça o LaraDumps!",
        "summary": "🐞 Debug no Laravel com Estilo: Conheça o LaraDumps!",
        "url": "https://www.youtube.com/watch?v=YcoEsO-wZHE",
        "site": {
            "id": 6300,
            "name": "Rafael Lunardelli - Pinguim do Laravel - YouTube",
            "url": "https://www.youtube.com/@pinguimDoLaravel",
            "rss": "https://www.youtube.com/feeds/videos.xml?channel_id=UCQvMasQi7VdeEYWy8u6BQQw",
            "about": null,
            "iframe": false
        },
        "indexDate": "2025-05-03 12:35:27",
        "pubDate": "2025-05-03 12:28:46"
    },
]
```

#### Resposta de Erro - Status Code 500
```json
{
    "code": 500,
    "message": "Erro no servidor"
}
```

---

### Registrar Clique em Post
**GET** `/api/v2/post/{id}/click`

Registra um clique no post especificado e retorna sua URL.

#### Parâmetros Path
| Parâmetro | Tipo   | Obrigatório | Descrição       |
|-----------|--------|-------------|-----------------|
| `id`      | integer | Sim         | ID do post      |

#### Resposta de Sucesso - Status Code 200
```json
{
    "url": "https://www.youtube.com/watch?v=-VcuUjOnB1w",
    "code": "74221",
    "title": "🔐 Two Factor Authentication: aprenda o que é e por que é importante para sua segurança digital 💡",
    "count": 1
}
```

#### Resposta de Erro - Status Code 500
```json
{
    "code": 500,
    "message": "ID inválido ou erro na requisição"
}
```

---

### Estatísticas de Cliques
**GET** `/api/v2/post/click/counts`

Retorna métricas agregadas de cliques por post.

#### Resposta de Sucesso - Status Code 200
```json
[
    {
        "title": "Build a LLM from scratch - Sebastian Raschka",
        "date": "2025-05-04 13:53:44",
        "count": 4
    },
]
```

#### Resposta de Erro - Status Code 500
```json
{
    "code": 500,
    "message": "ID inválido ou erro na requisição"
}
```

#### Formato
- Ordenação padrão: `click_count` (decrescente)
- Inclui data do último clique por post

--- 

## Configuração CORS

A API está configurada para permitir requisições apenas do domínio `http://localhost:5173` (React). As configurações CORS podem ser ajustadas no arquivo `CorsConfig.java`.

## Pré-requisitos

- Java 21 instalado.
- Maven instalado.
- Banco de dados configurado (o projeto utiliza JPA com Hibernate, configure o `application.properties` conforme necessário).

## Como Executarlone <url-do-repositorio>

1. Navegue até o diretório do projeto:
   ```bash
   cd API-Search-Courses-Spring-Boot
   ```

2. Compile e execute o projeto:
   ```bash
   mvn spring-boot:run
   ```

3. A API estará disponível em `http://localhost:8080`.

## Exemplos de Requisições

### Buscar todos os posts
```bash
curl -X GET "http://localhost:8080/api/v2/post"
```

### Filtrar posts por conteúdo
```bash
curl -X GET "http://localhost:8080/api/v2/post?content=Two Factor"
```

### Registrar um clique em um post
```bash
curl -X GET "http://localhost:8080/api/v2/post/74214/click"
```

### Obter estatísticas de cliques
```bash
curl -X GET "http://localhost:8080/api/v2/post/click/counts"
```
---
# Esquema do Banco de Dados

## Tabelas Principais

### Tabela de Sites (tb_sites)
```sql
CREATE TABLE `tb_sites` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `about` varchar(255) DEFAULT NULL,
  `iframe` bit(1) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `rss` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);
```

### Tabela de Posts (tb_posts)
```sql
CREATE TABLE `tb_posts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `index_date` varchar(255) DEFAULT NULL,
  `pub_date` varchar(255) DEFAULT NULL,
  `summary` varchar(10000) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `site_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_site_post` FOREIGN KEY (`site_id`) REFERENCES `tb_sites` (`id`)
);
```

### Tabela de Cliques (tb_click_counts)
```sql
CREATE TABLE `tb_click_counts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `count` int DEFAULT NULL,
  `date_click` varchar(255) DEFAULT NULL,
  `post_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_post_click` FOREIGN KEY (`post_id`) REFERENCES `tb_posts` (`id`)
);
```

## Dados de Exemplo

### Inserção de Sites
```sql
INSERT INTO tb_sites (id, name, url, rss, about, iframe)
VALUES
    (5697, 'Developer Iniciative', 'https://deviniciative.wordpress.com/', 'https://deviniciative.wordpress.com/feed/', NULL, false),
    (6300, 'Rafael Lunardelli - Pinguim do Laravel - YouTube', 'https://www.youtube.com/@pinguimDoLaravel', 'https://www.youtube.com/feeds/videos.xml?channel_id=UCQvMasQi7VdeEYWy8u6BQQw', NULL, false),
    (6248, 'Dev na Gringa | Lucas Faria | Substack', 'https://newsletter.nagringa.dev/', 'https://newsletter.nagringa.dev/feed', NULL, false),
    (6210, 'Canal dotNET - YouTube', 'https://www.youtube.com/@CanalDotNET', 'https://www.youtube.com/feeds/videos.xml?channel_id=UCIahKJr2Q50Sprk5ztPGnVg', NULL, false),
    (5882, 'Blog do Leandro Scardua', 'https://www.leandroscardua.com', 'https://www.leandroscardua.com/feed/', NULL, false),
    (6302, 'Victor Hugo Germano', 'https://www.victorhg.com/blog', 'https://www.victorhg.com/blog-feed.xml', NULL, false),
    (6180, 'BrazilJS - YouTube', 'https://www.youtube.com/user/BrazilJS', 'https://www.youtube.com/feeds/videos.xml?channel_id=UCnLdHOuue5i1O7TsH6oh07w', NULL, false),
    (5877, 'Aulas de Computação', 'https://www.youtube.com/channel/UC-g5Yr1ZUqexC5_2zPN_9HQ', 'https://www.youtube.com/feeds/videos.xml?channel_id=UC-g5Yr1ZUqexC5_2zPN_9HQ', NULL, false);
```

### Inserção de Posts
```sql
INSERT INTO tb_posts (id, title, summary, url, index_date, pub_date, site_id)
VALUES
    (74232, '🐞 Debug no Laravel com Estilo: Conheça o LaraDumps!', '🐞 Debug no Laravel com Estilo: Conheça o LaraDumps!', 'https://www.youtube.com/watch?v=YcoEsO-wZHE', '2025-05-03 12:35:27', '2025-05-03 12:28:46', 6300),
    (74231, 'Kubernetes + IAs Generativas: analisando códigos YAML com GitHub Copilot + Visual Studio Code', 'Kubernetes + IAs Generativas: analisando códigos YAML com GitHub Copilot + Visual Studio Code', 'https://www.youtube.com/watch?v=-F0rYt7FtcI', '2025-05-03 01:35:33', '2025-05-03 00:32:54', 6210),
    (74230, 'Azure Governance Visualizer', 'Ola Pessoal, Neste post vou mostrar como usar o Azure Governance Visualizer, conhecido também como AzGovViz, &#8216;e um conjunto de scripts que vai te ajudar a entender melhor o seu ambiente no azure em relação ao AD, group e etc.. Versões utilizadas AzGovViz: 6.6.3Powershell : 7.3.12 Antes de começar o test, temos que explicar o&#8230;&#160;Continue a ler &#187;Azure Governance Visualizer', 'https://leandroscardua.com/blog/azure-governance-visualizer/', '2025-05-02 23:35:40', '2025-05-02 23:00:00', 5882),
    (74228, 'Build a LLM from scratch - Sebastian Raschka', 'Build a LLM from scratch, a great book. You need to know about the subject to improve the way you criticize the craziness of AI market these days. ', 'https://www.victorhg.com/en/post/build-a-llm-from-scratch-sebastian-raschka', '2025-05-02 21:35:26', '2025-05-02 21:15:37', 6302),
    (74229, 'Weekly 500: the last dance', 'Weekly 500: the last dance', 'https://www.youtube.com/watch?v=ltOmWJLtJo4', '2025-05-02 21:35:33', '2025-05-02 20:47:51', 6180),
    (74227, 'Criando um LLM do zero - Sebastian Raschka', 'Criando um LLM do zero, através de um livro bastante interessante. É preciso conhecer a fundo o assunto para criticar ainda mais a loucura das IAs nos dias de hoje', 'https://www.victorhg.com/post/criando-um-llm-do-zero-sebastian-raschka', '2025-05-02 20:35:26', '2025-05-02 19:40:51', 6302),
    (74225, '🔧 Serialização no Laravel: Tipo Primitivo 🐛', '🔧 Serialização no Laravel: Tipo Primitivo 🐛', 'https://www.youtube.com/watch?v=3cckMIK8tBw', '2025-05-02 18:35:26', '2025-05-02 18:06:32', 6300),
    (74224, 'Therac-25: Erros de Software que Custaram Vidas Humanas', 'Therac-25: Erros de Software que Custaram Vidas Humanas', 'https://www.youtube.com/watch?v=RQ7YoEJUvXQ', '2025-05-02 17:35:37', '2025-05-02 16:48:10', 5877),
    (74223, 'Como Preservar Instâncias EC2 para Diagnóstico em Auto Scaling Groups', 'Você libera uma nova versão da sua aplicação. Aparentemente tudo certo, mas minutos depois, instâncias EC2 começam a ser marcadas como unhealthy e são automaticamente terminadas pelo Auto Scaling Group (ASG). Resultado: menos capacidade disponível, degradação de serviço, e o pior — você perde o ambiente defeituoso antes de conseguir analisá-lo. Ai você pensa, vou &#8230; Continuar lendo Como Preservar Instâncias EC2 para Diagnóstico em Auto Scaling&#160;Groups', 'https://deviniciative.wordpress.com/2025/05/02/como-preservar-instancias-ec2-para-diagnostico-em-auto-scaling-groups/', '2025-05-02 16:35:39', '2025-05-02 16:09:31', 5697),
    (74221, '🔐 Two Factor Authentication: aprenda o que é e por que é importante para sua segurança digital 💡', '🔐 Two Factor Authentication: aprenda o que é e por que é importante para sua segurança digital 💡', 'https://www.youtube.com/watch?v=-VcuUjOnB1w', '2025-05-02 14:35:26', '2025-05-02 14:19:20', 6300),
    (74218, 'Como fazer transição de carreira para TI', 'Estrat&#233;gias reais para quem n&#227;o pode largar tudo para estudar.', 'https://newsletter.nagringa.dev/p/como-fazer-transicao-de-carreira-para-ti', '2025-05-02 13:35:30', '2025-05-02 13:02:10', 6248);
```

### Inserção de Cliques
```sql
INSERT INTO `tb_click_counts` (`post_id`, `count`, `date_click`) VALUES
(74221, 5, '2025-05-03 16:48:30'),
(74218, 3, '2025-05-03 16:48:39'),
(74223, 2, '2025-05-03 16:48:40');
```

## Relacionamentos

- `tb_posts` referencia `tb_sites` através do campo `site_id`
- `tb_click_counts` referencia `tb_posts` através do campo `post_id`
