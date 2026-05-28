# 🚀 GitHub Explorer CLI

Uma aplicação de linha de comando (CLI) robusta desenvolvida em **Java 21 puro (Vanilla Java)** para consultar dados de perfis e listar repositórios públicos do GitHub de forma otimizada.

O projeto foi projetado aplicando conceitos avançados de arquitetura de software, concorrência moderna e otimização de recursos para garantir uma excelente experiência de usuário no terminal.

---

## 🛠️ Diferenciais de Engenharia & Tecnologias

Diferente de scripts lineares comuns, este projeto simula a robustez de um sistema corporativo de alta performance através dos seguintes pilares:

* **Java 21 Records:** O modelo de dados do usuário foi totalmente refatorado utilizando `record`, garantindo imutabilidade de dados de ponta a ponta, redução drástica de código boilerplate e melhor desempenho de memória.
* **Concorrência com Virtual Threads (Java 21):** Para evitar o congelamento do terminal durante as requisições HTTP, a animação visual de carregamento (*Loading Spinner*) roda em paralelo utilizando as novas *Virtual Threads* do Java, garantindo leveza e reatividade à CLI.
* **Mecanismo de Cache Duplo (LRU Thread-Safe):** Implementação manual de uma estratégia de cache em memória para Perfis e Repositórios. Utilizando `LinkedHashMap` sincronizado estruturado como um cache *Least Recently Used (LRU)*, a aplicação limita o armazenamento aos últimos 5 itens pesquisados. Se o dado está na memória, o retorno é instantâneo (redução de latência de ~1s para <1ms) e poupa o *Rate Limit* da API do GitHub.
* **Arquitetura Baseada em Comandos:** Interface interativa flexível inspirada em consoles profissionais. Substitui menus numéricos engessados por um interpretador de comandos em texto estruturado com tratamento rigoroso de exceções customizadas para erros de negócio (ex: 404) e infraestrutura (ex: queda de rede).

---

## 📖 Comandos Disponíveis

Ao iniciar a aplicação, você interagirá com o prompt `github-cli>`. Os comandos suportados são:

| Comando | Descrição | Exemplo de Uso |
| :--- | :--- | :--- |
| `help` | Exibe o menu com as instruções e lista de comandos. | `help` |
| `user <username>` | Busca e exibe formatado os dados públicos de um perfil. | `user torvalds` |
| `repos <username>` | Lista todos os repositórios públicos de um usuário. | `repos torvalds` |
| `exit` ou `quit` | Encerra a execução do programa de forma limpa. | `exit` |

---

## 🚀 Como Executar o Projeto

Você pode rodar o projeto compilando os arquivos-fonte na hora ou utilizando o executável empacotado.

### Pré-requisitos
* **JDK 21** instalado e configurado nas Variáveis de Ambiente (`PATH`).

### Opção 1: Executando via JAR Pronto (Recomendado)
Se você baixou o arquivo executável unificado do projeto, basta abrir o terminal na pasta dele e rodar:
```bash
java -jar GitHubExplorer.jar
```

### Opção 2: Compilando "no braço" pelo Terminal
Se você clonou o código-fonte e deseja compilar o ecossistema manualmente:

Compile todas as classes e exceções direcionando para uma pasta de saída:
```bash
javac -d out src/br/com/githubexplorer/*.java src/br/com/githubexplorer/exception/*.java
```

Execute o ponto de entrada principal:
```bash
java -cp out br.com.githubexplorer.Main
```

### 📁 Estrutura de Pacotes
A arquitetura do código segue rigorosamente as boas práticas de separação de responsabilidades e organização do ecossistema Java:

```plaintext
src/
└── br/
    └── com/
        └── githubexplorer/
            ├── exception/
            │   ├── GitHubApiException.java            # Erros de infraestrutura/rede
            │   └── UsuarioNaoEncontradoException.java # Erros de negócio (404)
            ├── GitHubService.java                    # Lógica de negócio, HTTP Client e Cache
            ├── LoadingAnimation.java                 # Gerenciador do Spinner em Virtual Thread
            ├── Usuario.java                          # Modelo de dados (Java 21 Record)
            └── Main.java                             # Interface de entrada e loop do menu
```

## 🤝 Conecte-se Comigo

<div align="center">
  <a href="https://www.linkedin.com/in/guspereira-dev/" target="_blank">
    <img src="https://img.shields.io/badge/-LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" />
  </a>
  <a href="mailto:dev.gustavospereira@gmail.com" target="_blank">
    <img src="https://img.shields.io/badge/-Email-D14836?style=for-the-badge&logo=gmail&logoColor=white" />
  </a>
</div>

---

<p align="center">
  <em>"Às vezes, o sistema não está lento por falta de infraestrutura. É só falta de olhar para a base da computação."</em>
</p>
