# 🚀 Vanilla Java GitHub Explorer

Um explorador de perfis do GitHub desenvolvido em **Java Puro**, sem frameworks ou dependências externas. O projeto utiliza a API nativa `java.net.http` e um parser JSON manual com Regex para consolidar conhecimentos nos fundamentos da linguagem.

## ✨ Features

*   **HttpClient Nativo:** Conexões com a API REST do GitHub usando `java.net.http`.
*   **Parser JSON Manual:** Extração de dados com as classes `Pattern` e `Matcher`.
*   **Tratamento de Exceções:** Arquitetura limpa com *Custom Exceptions* para falhas de rede e de busca.
*   **Arquitetura POO:** Separação de responsabilidades (Model, Service, View) em uma aplicação de linha de comando (CLI).

## 🛠️ Requisitos

*   **Java JDK 11 ou superior**.
*   Variáveis de ambiente do Java (`Path`) configuradas.

Para validar a instalação, execute no terminal:
```bash
java -version
javac -version
```

## 📂 Estrutura do Projeto

```
GitHubExplorerCLI/
├── README.md
├── Main.java
├── GitHubService.java
├── Usuario.java
├── GitHubApiException.java
└── UsuarioNaoEncontradoException.java
```

## 🚀 Como Executar

1.  **Acesse a pasta do projeto:**
    ```bash
    cd /caminho/para/seu/projeto/GitHubExplorerCLI
    ```

2.  **Compile os fontes:**
    ```bash
    javac *.java
    ```

3.  **Inicie a aplicação:**
    ```bash
    java Main
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
