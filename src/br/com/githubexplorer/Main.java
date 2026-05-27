package br.com.githubexplorer;

import br.com.githubexplorer.exception.GitHubApiException;
import br.com.githubexplorer.exception.UsuarioNaoEncontradoException;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GitHubService service = new GitHubService();
        int opcao = -1;

        System.out.println("=======================================");
        System.out.println("  BEM-VINDO AO EXPLORADOR DE GITHUB   ");
        System.out.println("=======================================");

        while (opcao != 0) {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1 - Buscar dados de um perfil");
            System.out.println("2 - Listar repositórios de um perfil");
            System.out.println("0 - Sair do programa");
            System.out.print("Escolha uma opção: ");

            if (!scanner.hasNextInt()) {
                System.out.println("❌ Opção inválida! Digite um número.");
                scanner.next();
                continue;
            }

            opcao = scanner.nextInt();
            scanner.nextLine();

            if (opcao == 0) {
                System.out.println("\nEncerrando o programa... Até logo!");
                break;
            }

            if (opcao == 1 || opcao == 2) {
                System.out.print("Digite o username do GitHub: ");
                String username = scanner.nextLine().trim();

                if (username.isEmpty()) {
                    System.out.println("❌ O username não pode ser vazio.");
                    continue;
                }

                try {
                    switch (opcao) {
                        case 1:
                            Usuario usuario = service.buscarUsuario(username);
                            exibirPerfil(usuario);
                            break;
                        case 2:
                            service.listarRepositorios(username);
                            break;
                    }
                } catch (UsuarioNaoEncontradoException e) {
                    // Erro controlado de negócio (Usuário digitou algo errado)
                    System.out.println("\n🔍 " + e.getMessage());
                } catch (GitHubApiException e) {
                    // Erro controlado de infraestrutura (Internet caiu ou API fora do ar)
                    System.out.println("\n🌐 Erro de Comunicação: " + e.getMessage());
                } catch (Exception e) {
                    // Um "catch-all" genérico caso aconteça algo bizarro não previsto (ex: erro ao converter número)
                    System.out.println("\n💥 Ocorreu um erro inesperado: " + e.getMessage());
                }
            } else {
                System.out.println("❌ Opção inválida! Tente novamente.");
            }
        }

        scanner.close();
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
}