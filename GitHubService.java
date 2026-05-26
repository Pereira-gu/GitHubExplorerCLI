package my.project;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitHubService {

    private final HttpClient client = HttpClient.newHttpClient();

    // Opção 1: Buscar dados gerais do perfil
    public Usuario buscarUsuario(String username) throws Exception {
        String url = "https://api.github.com/users/" + username;
        String json = fazerRequisicao(url);

        Usuario usuario = new Usuario();
        usuario.setLogin(extrairValor(json, "login"));
        usuario.setName(extrairValor(json, "name"));
        usuario.setBio(extrairValor(json, "bio"));
        usuario.setLocation(extrairValor(json, "location"));

        String reposStr = extrairValor(json, "public_repos");
        if (reposStr != null) usuario.setPublicRepos(Integer.parseInt(reposStr));

        String followersStr = extrairValor(json, "followers");
        if (followersStr != null) usuario.setFollowers(Integer.parseInt(followersStr));

        return usuario;
    }

    // Opção 2: Listar repositórios (Array JSON)
    public void listarRepositorios(String username) throws Exception {
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

    // Método privado auxiliar para requisição HTTP
    private String fazerRequisicao(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "Java-HttpClient")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 404) {
            throw new RuntimeException("Perfil não encontrado no GitHub.");
        } else if (response.statusCode() != 200) {
            throw new RuntimeException("Erro na API do GitHub. Status: " + response.statusCode());
        }

        return response.body();
    }

    // Método privado auxiliar para extrair valores com Regex
    private String extrairValor(String json, String chave) {
        Pattern pattern = Pattern.compile("\"" + chave + "\"\\s*:\\s*\"?([^\",}]+)\"?");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }
}