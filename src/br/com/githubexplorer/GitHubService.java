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

    // Repare que as assinaturas NÃO possuem mais "throws Exception"
    public Usuario buscarUsuario(String username) {
        String url = "https://api.github.com/users/" + username;
        String json = fazerRequisicao(url);

        Usuario usuario = new Usuario();
        usuario.setLogin(extrairValor(json, "login"));
        usuario.setName(extrairValor(json, "name"));
        usuario.setBio(extrairValor(json, "bio"));
        usuario.setLocation(extrairValor(json, "location"));

        String reposStr = extrairValor(json, "public_repos");
        if (reposStr != null) usuario.setPublicRepos(Integer.parseInt(reposStr));

        String followersStr = extrairValor(json, "followers"); // método corrigido abaixo
        if (followersStr != null) usuario.setFollowers(Integer.parseInt(followersStr));

        return usuario;
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

    // Onde a mágica do tratamento acontece
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
            // Se a internet cair ou houver timeout, capturamos aqui e lançamos nossa exceção customizada
            throw new GitHubApiException("Não foi possível conectar à API do GitHub. Verifique sua conexão com a internet.", e);
        }
    }

    private String extrairValor(String json, String chave) {
        Pattern pattern = Pattern.compile("\"" + chave + "\"\\s*:\\s*\"?([^\",}]+)\"?");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }
}