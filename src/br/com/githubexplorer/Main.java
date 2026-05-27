package br.com.githubexplorer;

import br.com.githubexplorer.exception.GitHubApiException;
import br.com.githubexplorer.exception.UsuarioNaoEncontradoException;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GitHubService service = new GitHubService();

        System.out.println("==================================================");
        System.out.println("      🚀 VANILLA JAVA GITHUB EXPLORER CLI         ");
        System.out.println("==================================================");
        System.out.println("Digite 'help' a qualquer momento para ver os comandos.");

        while (true) {
            System.out.print("\ngithub-cli> ");
            String entrada = scanner.nextLine().trim();

            if (entrada.isEmpty()) {
                continue;
            }

            // Separa o comando dos argumentos por espaços em branco
            String[] partes = entrada.split("\\s+", 2);
            String comando = partes[0].toLowerCase();
            String argumento = partes.length > 1 ? partes[1].trim() : "";

            if (comando.equals("exit") || comando.equals("quit")) {
                System.out.println("\nEncerrando o programa... Até logo!");
                break;
            }

            switch (comando) {
                case "help":
                    exibirAjuda();
                    break;

                case "user":
                    if (argumento.isEmpty()) {
                        System.out.println("❌ Uso correto: user <username>");
                        break;
                    }
                    try {
                        Usuario usuario = service.buscarUsuario(argumento);
                        exibirPerfil(usuario);
                    } catch (UsuarioNaoEncontradoException e) {
                        System.out.println("\n🔍 " + e.getMessage());
                    } catch (GitHubApiException e) {
                        System.out.println("\n🌐 Erro de Comunicação: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("\n💥 Erro inesperado: " + e.getMessage());
                    }
                    break;

                case "repos":
                    if (argumento.isEmpty()) {
                        System.out.println("❌ Uso correto: repos <username>");
                        break;
                    }
                    try {
                        List<String> repos = service.listarRepositorios(argumento);
                        exibirRepositorios(argumento, repos);
                    } catch (UsuarioNaoEncontradoException e) {
                        System.out.println("\n🔍 " + e.getMessage());
                    } catch (GitHubApiException e) {
                        System.out.println("\n🌐 Erro de Comunicação: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("\n💥 Erro inesperado: " + e.getMessage());
                    }
                    break;

                default:
                    System.out.println("❌ Comando '" + comando + "' não reconhecido. Digite 'help' para ver as opções.");
            }
        }

        scanner.close();
    }

    private static void exibirAjuda() {
        System.out.println("\n--- 📖 COMANDOS DISPONÍVEIS ---");
        System.out.println("  user <username>   - Busca e exibe os dados públicos do perfil.");
        System.out.println("  repos <username>  - Lista todos os repositórios públicos do perfil.");
        System.out.println("  help              - Exibe este menu de ajuda.");
        System.out.println("  exit / quit       - Encerra a aplicação.");
        System.out.println("---------------------------------");
    }

    private static void exibirPerfil(Usuario usuario) {
        System.out.println("\n=======================================");
        System.out.println("📊 DADOS DO PERFIL:");
        System.out.println("=======================================");
        System.out.println("Nome:         " + (usuario.name() != null ? usuario.name() : "Não informado"));
        System.out.println("Username:     " + usuario.login());
        System.out.println("Biografia:    " + (usuario.bio() != null ? usuario.bio() : "Sem biografia"));
        System.out.println("Localização:  " + (usuario.location() != null ? usuario.location() : "Não informada"));
        System.out.println("Seguidores:   " + usuario.followers());
        System.out.println("Repositórios: " + usuario.publicRepos());
        System.out.println("=======================================");
    }

    private static void exibirRepositorios(String username, List<String> repos) {
        System.out.println("\n=======================================");
        System.out.println("📂 REPOSITÓRIOS PÚBLICOS DE " + username.toUpperCase() + ":");
        System.out.println("=======================================");

        if (repos.isEmpty()) {
            System.out.println("Nenhum repositório público encontrado ou perfil vazio.");
        } else {
            for (int i = 0; i < repos.size(); i++) {
                System.out.println((i + 1) + ". " + repos.get(i));
            }
        }
        System.out.println("=======================================");
    }
}