package br.com.githubexplorer;

import java.util.concurrent.atomic.AtomicBoolean;

public class LoadingAnimation implements AutoCloseable {
    private final Thread animationThread;
    private final AtomicBoolean running = new AtomicBoolean(true);

    public LoadingAnimation(String mensagem) {
        // Criamos uma Virtual Thread do Java 21 para a animação rodar em paralelo
        this.animationThread = Thread.ofVirtual().start(() -> {
            String[] spinner = {"|", "/", "-", "\\"};
            int i = 0;
            System.out.print("\r" + mensagem + " " + spinner[i]);

            while (running.get()) {
                try {
                    Thread.sleep(100);
                    i = (i + 1) % spinner.length;
                    // O '\r' faz o cursor voltar para o início da linha no terminal
                    System.out.print("\r" + mensagem + " " + spinner[i]);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            // Limpa a linha do terminal ao terminar
            System.out.print("\r" + " ".repeat(mensagem.length() + 4) + "\r");
        });
    }

    @Override
    public void close() {
        running.set(false);
        try {
            animationThread.join(); // Garante que a animação parou completamente
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}