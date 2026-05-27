package br.com.githubexplorer;

public record Usuario(
        String login,
        String name,
        String bio,
        String location,
        int publicRepos,
        int followers
) {}