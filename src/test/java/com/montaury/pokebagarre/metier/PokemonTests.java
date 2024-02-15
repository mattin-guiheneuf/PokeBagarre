package com.montaury.pokebagarre.metier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PokemonTests {

    @Test
    public void meilleure_attaque_gagne() {
        // GIVEN
        Pokemon pokemon1 = new Pokemon("Joueur1", "test1", new Stats(110, 100));
        Pokemon pokemon2 = new Pokemon("Joueur2", "test2", new Stats(100,100));

        // WHEN
        boolean vainqueurPoke1 = pokemon1.estVainqueurContre(pokemon2);
        boolean vainqueurPoke2 = pokemon2.estVainqueurContre(pokemon1);

        // THEN
        Assertions.assertEquals(true, vainqueurPoke1);
        Assertions.assertEquals(false, vainqueurPoke2);
    }

    @Test
    public void meme_attaque_donc_meilleure_defense_gagne() {
        // GIVEN
        Pokemon pokemon1 = new Pokemon("Joueur1", "test1", new Stats(100, 110));
        Pokemon pokemon2 = new Pokemon("Joueur2", "test2", new Stats(100,100));

        // WHEN
        boolean vainqueurPoke1 = pokemon1.estVainqueurContre(pokemon2);
        boolean vainqueurPoke2 = pokemon2.estVainqueurContre(pokemon1);

        // THEN
        Assertions.assertEquals(true, vainqueurPoke1);
        Assertions.assertEquals(false, vainqueurPoke2);
    }

    @Test
    public void meme_stats_donc_premier_gagne() {
        // GIVEN
        Pokemon pokemon1 = new Pokemon("Joueur1", "test1", new Stats(100, 100));
        Pokemon pokemon2 = new Pokemon("Joueur2", "test2", new Stats(100, 100));

        // WHEN
        boolean vainqueurPoke1 = pokemon1.estVainqueurContre(pokemon2);
        boolean vainqueurPoke2 = pokemon2.estVainqueurContre(pokemon1);

        // THEN
        Assertions.assertEquals(true, vainqueurPoke1);
        Assertions.assertEquals(true, vainqueurPoke2);
    }
}