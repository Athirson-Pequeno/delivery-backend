package com.tizo.delivery.util;

import com.tizo.delivery.repository.StoreRepository;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

@Service
public class SlugGenerator {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final Pattern EDGESDHASHES = Pattern.compile("(^-|-$)");
    private static final Pattern MULTIPLE_DASHES = Pattern.compile("-+");

    private final StoreRepository storeRepository;

    public SlugGenerator(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public String generateSlug(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Input não pode ser nulo");
        }

        // Remover acentos e caracteres especiais
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("[^\\p{ASCII}]", "");

        // Converter para minúsculas
        String slug = normalized.toLowerCase(Locale.forLanguageTag("pt-BR"));

        // Substituir espaços por hífens
        slug = WHITESPACE.matcher(slug).replaceAll("-");

        // Remover caracteres inválidos
        slug = NONLATIN.matcher(slug).replaceAll("");

        // Remover hífens duplicados
        slug = MULTIPLE_DASHES.matcher(slug).replaceAll("-");

        // Remover hífens no início e fim
        slug = EDGESDHASHES.matcher(slug).replaceAll("");

        // Se ficar vazio, usar um valor padrão
        if (slug.isEmpty()) {
            slug = "store";
        }

        // Gerar slug único
        return generateUniqueSlug(slug);
    }

    private String generateUniqueSlug(String baseSlug) {
        // Primeiro verifica se o slug base já existe
        if (storeRepository.countStoreBySlug(baseSlug) == 0) {
            return baseSlug;
        }

        // Se existir, adiciona sufixo numérico
        int counter = 1;
        String uniqueSlug;

        do {
            uniqueSlug = baseSlug + "-" + counter;
            counter++;
        } while (storeRepository.countStoreBySlug(uniqueSlug) > 0);

        return uniqueSlug;
    }
}