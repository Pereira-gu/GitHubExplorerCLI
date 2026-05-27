package br.com.githubexplorer;

import br.com.githubexplorer.exception.GitHubApiException;
import br.com.githubexplorer.exception.UsuarioNaoEncontradoException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitHubService {

    private final HttpClient client = HttpClient.newHttpClient();

    // Cache de Utilizadores (Mantém os últimos 5)
    private final Map<String, Usuario> usuarioCache = Collections.synchronizedMap(
            new LinkedHashMap<>(5, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<String, Usuario> eldest) {
                    return size() > 5;
                }
            }
    );

    // NOVO: Cache de Repositórios (Mantém as listas dos últimos 5 utilizadores pesquisados)
    private final Map<String, List<String>> repositoriosCache = Collections.synchronizedMap(
            new LinkedHashMap<>(5, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<String, List<String>> eldest) {
                    return size() > 5;
                }
            }
    );

    public Usuario buscarUsuario(String username) {
        String usernameChave = username.toLowerCase().trim();

        if (usuarioCache.containsKey(usernameChave)) {
            System.out.println("\n⚡ [CACHE] Recuperando dados da memória para: " + username);
            return usuarioCache.get(usernameChave);
        }

        try (LoadingAnimation animation = new LoadingAnimation("🔍 A aceder à API do GitHub...")) {
            String url = "https://api.github.com/users/" + usernameChave;
            String json = fazerRequisicao(url);

            String login = extrairValor(json, "login");
            String name = extrairValor(json, "name");
            String bio = extrairValor(json, "bio");
            String location = extrairValor(json, "location");

            String reposStr = extrairValor(json, "public_repos");
            int publicRepos = (reposStr != null) ? Integer.parseInt(reposStr) : 0;

            String followersStr = extrairValor(json, "followers");
            int followers = (followersStr != null) ? Integer.parseInt(followersStr) : 0;

            Usuario usuario = new Usuario(login, name, bio, location, publicRepos, followers);
            usuarioCache.put(usernameChave, usuario);
            return usuario;
        }
    }

    public List<String> listarRepositorios(String username) {
        String usernameChave = username.toLowerCase().trim();

        // ATUALIZAÇÃO: Se a lista de repositórios já estiver no cache, devolve imediatamente
        if (repositoriosCache.containsKey(usernameChave)) {
            System.out.println("\n⚡ [CACHE] Recuperando repositórios da memória para: " + username);
            return repositoriosCache.get(usernameChave);
        }

        try (LoadingAnimation animation = new LoadingAnimation("📂 A carregar lista de repositórios...")) {
            String url = "https://api.github.com/users/" + usernameChave + "/repos";
            String json = fazerRequisicao(url);

            Pattern pattern = Pattern.compile("\"name\"\\s*:\\s*\"([^\"]+)\"");
            Matcher matcher = pattern.matcher(json);

            List<String> repositorios = new ArrayList<>();
            while (matcher.find()) {
                repositorios.add(matcher.group(1));
            }

            // Guarda o resultado no cache (mesmo se for uma lista vazia)
            repositoriosCache.put(usernameChave, repositorios);
            return repositorios;
        }
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
                throw new UsuarioNaoEncontradoException("O utilizador informado não existe no GitHub.");
            } else if (response.statusCode() != 200) {
                throw new GitHubApiException("Falha na API do GitHub. Status HTTP: " + response.statusCode(), null);
            }

            return response.body();

        } catch (IOException | InterruptedException e) {
            throw new GitHubApiException("Não foi possível conectar à API do GitHub. Verifique sua conexão com a internet.", e);
        }
    }

    private String extrairValor(String json, String chave) {
        Pattern pattern = Pattern.compile("\"" + chave + "\"\\s*:\\s*\"?([^\",\\n}]+)\"?");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            String valor = matcher.group(1).trim();
            if (valor.startsWith("\"") && valor.endsWith("\"")) {
                valor = valor.substring(1, valor.length() - 1);
            }
            return valor.equals("null") ? null : valor;
        }
        return null;
    }
}