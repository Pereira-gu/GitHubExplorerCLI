package my.project;

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
            scanner.nextLine(); // Limpa o buffer

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
                } catch (Exception e) {
                    System.err.println("❌ Erro ao processar requisição: " + e.getMessage());
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
        System.out.println("Nome:         " + (usuario.getName() != null ? usuario.getName() : "Não informado"));
        System.out.println("Username:     " + usuario.getLogin());
        System.out.println("Biografia:    " + (usuario.getBio() != null ? usuario.getBio() : "Sem biografia"));
        System.out.println("Localização:  " + (usuario.getLocation() != null ? usuario.getLocation() : "Não informada"));
        System.out.println("Seguidores:   " + usuario.getFollowers());
        System.out.println("Repositórios: " + usuario.getPublicRepos());
        System.out.println("=======================================");
    }
}