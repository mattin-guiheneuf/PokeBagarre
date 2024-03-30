package com.montaury.pokebagarre.metier;

import com.montaury.pokebagarre.erreurs.ErreurMemePokemon;
import com.montaury.pokebagarre.erreurs.ErreurPokemonNonRenseigne;
import com.montaury.pokebagarre.erreurs.ErreurRecuperationPokemon;
import com.montaury.pokebagarre.webapi.PokeBuildApi;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

class BagarreTests {
    PokeBuildApi fausseApi;
    Bagarre bagarre;

    @BeforeEach
    void preparer() {
        fausseApi = mock(PokeBuildApi.class);
        bagarre = new Bagarre(fausseApi);
    }

    @Test
    void premier_pokemon_null() {
        // GIVEN
        Bagarre bagarre = new Bagarre();

        // WHEN
        Throwable erreur = Assertions.catchThrowable(()->bagarre.demarrer(null, "pikachu"));

        // THEN
        assertThat(erreur).isInstanceOf(ErreurPokemonNonRenseigne.class).hasMessage("Le premier pokemon n'est pas renseigne");
    }

    @Test
    void deuxieme_pokemon_null() {
        // GIVEN
        Bagarre bagarre = new Bagarre();

        // WHEN
        Throwable erreur = Assertions.catchThrowable(()->bagarre.demarrer("pikachu", null));

        // THEN
        assertThat(erreur).isInstanceOf(ErreurPokemonNonRenseigne.class).hasMessage("Le second pokemon n'est pas renseigne");
    }

    @Test
    void premier_pokemon_vide() {
        // GIVEN
        Bagarre bagarre = new Bagarre();

        // WHEN
        Throwable erreur = Assertions.catchThrowable(()->bagarre.demarrer("  ", "pikachu"));

        // THEN
        assertThat(erreur).isInstanceOf(ErreurPokemonNonRenseigne.class).hasMessage("Le premier pokemon n'est pas renseigne");
    }

    @Test
    void deuxieme_pokemon_vide() {
        // GIVEN
        Bagarre bagarre = new Bagarre();

        // WHEN
        Throwable erreur = Assertions.catchThrowable(()->bagarre.demarrer("pikachu", "  "));

        // THEN
        assertThat(erreur).isInstanceOf(ErreurPokemonNonRenseigne.class).hasMessage("Le second pokemon n'est pas renseigne");
    }

    @Test
    void pokemons_meme_nom() {
        // GIVEN
        Bagarre bagarre = new Bagarre();

        // WHEN
        Throwable erreur = Assertions.catchThrowable(()->bagarre.demarrer("pikachu", "pikachu"));

        //THEN
        assertThat(erreur).isInstanceOf(ErreurMemePokemon.class).hasMessage("Impossible de faire se bagarrer un pokemon avec lui-meme");
    }

    @Test
    void mauvaise_recup_api_pokemon1() {
        when(fausseApi.recupererParNom("pikachu")).thenReturn(CompletableFuture.failedFuture(new ErreurRecuperationPokemon("pikachu")));
        when(fausseApi.recupererParNom("mewtwo")).thenReturn(CompletableFuture.completedFuture(new Pokemon("mewtwo", "url2", new Stats(3, 4))));

        var futurVainqueur = bagarre.demarrer("pikachu", "mewtwo");

        assertThat(futurVainqueur).failsWithin(Duration.ofSeconds(2)).withThrowableOfType(ExecutionException.class)
                .havingCause().isInstanceOf(ErreurRecuperationPokemon.class)
                .withMessage("Impossible de recuperer les details sur 'pikachu'");
    }

    @Test
    void mauvaise_recup_api_pokemon2() {
        when(fausseApi.recupererParNom("pikachu")).thenReturn(CompletableFuture.completedFuture(new Pokemon("pikachu", "url1", new Stats(1, 2))));
        when(fausseApi.recupererParNom("mewtwo")).thenReturn(CompletableFuture.failedFuture(new ErreurRecuperationPokemon("mewtwo")));

        var futurVainqueur = bagarre.demarrer("pikachu", "mewtwo");

        assertThat(futurVainqueur).failsWithin(Duration.ofSeconds(2)).withThrowableOfType(ExecutionException.class)
                .havingCause().isInstanceOf(ErreurRecuperationPokemon.class)
                .withMessage("Impossible de recuperer les details sur 'mewtwo'");
    }

    @Test
    void devrait_retourner_pokemon1_si_vainqueur() {
        when(fausseApi.recupererParNom("pikachu")).thenReturn(CompletableFuture.completedFuture(new Pokemon("pikachu", "url1", new Stats(1, 2))));
        when(fausseApi.recupererParNom("mewtwo")).thenReturn(CompletableFuture.completedFuture(new Pokemon("mewtwo", "url2", new Stats(3, 4))));

        var futurVainqueur = bagarre.demarrer("pikachu", "mewtwo");

        assertThat(futurVainqueur).succeedsWithin(Duration.ofSeconds(2)).satisfies(pokemon ->
                {
                    assertThat(pokemon.getNom()).isEqualTo("mewtwo");
                    assertThat(pokemon.getUrlImage()).isEqualTo("url2");
                    assertThat(pokemon.getStats().getAttaque()).isEqualTo(3);
                    assertThat(pokemon.getStats().getDefense()).isEqualTo(4);
                });
    }

































}