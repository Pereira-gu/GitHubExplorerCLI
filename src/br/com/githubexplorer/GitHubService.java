package br.com.githubexplorer;

import br.com.githubexplorer.exception.GitHubApiException;
import br.com.githubexplorer.exception.UsuarioNaoEncontradoException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitHubService {

    private final HttpClient client = HttpClient.newHttpClient();

    public Usuario buscarUsuario(String username) {
        String url = "https://api.github.com/users/" + username;
        String json = fazerRequisicao(url);

        String login = extrairValor(json, "login");
        String name = extrairValor(json, "name");
        String bio = extrairValor(json, "bio");
        String location = extrairValor(json, "location");

        String reposStr = extrairValor(json, "public_repos");
        int publicRepos = (reposStr != null) ? Integer.parseInt(reposStr) : 0;

        String followersStr = extrairValor(json, "followers");
        int followers = (followersStr != null) ? Integer.parseInt(followersStr) : 0;

        // Criando o objeto usando o construtor do Record (imutável)
        return new Usuario(login, name, bio, location, publicRepos, followers);
    }

    public void listarRepositorios(String username) {
        String url = "https://api.github.com/users/" + username + "/repos";
        String json = fazerRequisicao(url);

        Pattern pattern = Pattern.compile("\"name\"\\s*:\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(json);

        System.out.println("\n=======================================");
        System.out.println("📂 REPOSITÓRIOS PÚBLICOS DE " + username.toUpperCase() + ":");
        System.out.println("=======================================");

        int contador = 1;
        while (matcher.find()) {
            System.out.println(contador + ". " + matcher.group(1));
            contador++;
        }

        if (contador == 1) {
            System.out.println("Nenhum repositório público encontrado ou perfil vazio.");
        }
        System.out.println("=======================================");
    }

    private String fazerRequisicao(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "Java-HttpClient")
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 404) {
                throw new UsuarioNaoEncontradoException("O usuário informado não existe no GitHub.");
            } else if (response.statusCode() != 200) {
                throw new GitHubApiException("Falha na API do GitHub. Status HTTP: " + response.statusCode(), null);
            }

            return response.body();

        } catch (IOException | InterruptedException e) {
            throw new GitHubApiException("Não foi possível conectar à API do GitHub. Verifique sua conexão com a internet.", e);
        }
    }

    private String extrairValor(String json, String chave) {
        // Regex aprimorada para capturar valores com ou sem aspas de forma limpa
        Pattern pattern = Pattern.compile("\"" + chave + "\"\\s*:\\s*\"?([^\",\\n}]+)\"?");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            String valor = matcher.group(1).trim();
            // Remove aspas residuais se houver
            if (valor.startsWith("\"") && valor.endsWith("\"")) {
                valor = valor.substring(1, valor.length() - 1);
            }
            return valor.equals("null") ? null : valor;
        }
        return null;
    }
}