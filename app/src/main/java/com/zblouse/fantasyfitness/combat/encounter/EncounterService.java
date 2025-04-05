package com.zblouse.fantasyfitness.combat.encounter;

import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.combat.cards.AbilityTarget;
import com.zblouse.fantasyfitness.combat.cards.AttackType;
import com.zblouse.fantasyfitness.combat.cards.BuffAbility;
import com.zblouse.fantasyfitness.combat.cards.BuffType;
import com.zblouse.fantasyfitness.combat.cards.Card;
import com.zblouse.fantasyfitness.combat.cards.CharacterCard;
import com.zblouse.fantasyfitness.combat.cards.DamageAbility;
import com.zblouse.fantasyfitness.combat.cards.DamageType;
import com.zblouse.fantasyfitness.combat.cards.HealAbility;
import com.zblouse.fantasyfitness.combat.cards.ItemCard;
import com.zblouse.fantasyfitness.combat.cards.ItemType;
import com.zblouse.fantasyfitness.core.DomainService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EncounterService implements DomainService<Encounter> {

    private MainActivity mainActivity;
    private EncounterRepository encounterRepository;

    public EncounterService(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.encounterRepository = new EncounterRepository(this);
        initializeEncounters();
    }

    public void fetchEncounter(String encounterName, Map<String, Object> metadata){
        encounterRepository.fetchEncounter(encounterName, metadata);
    }

    @Override
    public void repositoryResponse(Encounter encounter, Map<String, Object> metadata) {
        if(encounter != null){
            mainActivity.publishEvent(new EncounterFetchEvent(encounter,metadata));
        }
    }

    @Override
    public void interDomainServiceResponse(Object responseObject, Map<String, Object> metadata) {

    }

    //Should not be called during ops, only called during development until encounters are finalized
    public void initializeEncounters(){
        List<Card> banditAttackEncounterDeck = new ArrayList<>();
        Card banditCard1 = new CharacterCard("fantasyFitness", UUID.randomUUID().toString(),"Bandit","A bandit.",new DamageAbility("Punch","The bandit punches.", AbilityTarget.SINGLE_ENEMY, DamageType.NORMAL, AttackType.MELEE,2),12);
        banditAttackEncounterDeck.add(banditCard1);
        Card banditCard2 = new CharacterCard("fantasyFitness", UUID.randomUUID().toString(),"Bandit Medic","A bandit trained in medicine.",new HealAbility("Patch Wounds","The bandit heals.", AbilityTarget.SINGLE_ALLY, 2),10);
        banditAttackEncounterDeck.add(banditCard2);
        Card banditCard3 = new CharacterCard("fantasyFitness", UUID.randomUUID().toString(),"Bandit","A bandit.",new DamageAbility("Punch","The bandit punches.", AbilityTarget.SINGLE_ENEMY, DamageType.NORMAL, AttackType.MELEE,2),13);
        banditAttackEncounterDeck.add(banditCard3);
        Card banditCard4 = new CharacterCard("fantasyFitness", UUID.randomUUID().toString(),"Sling Bandit","A bandit with a sling.",new DamageAbility("Stone Throw","The goblin looses an arrow.", AbilityTarget.SINGLE_ENEMY, DamageType.NORMAL, AttackType.RANGED,1),10);
        banditAttackEncounterDeck.add(banditCard4);
        Card banditCard5 = new CharacterCard("fantasyFitness", UUID.randomUUID().toString(),"Bandit Leader","A bandit who is bigger than the other bandits, which is how you know he is the leader.",new DamageAbility("Natural Charisma","The bandit captain inspires fear that damages your self-confidence.", AbilityTarget.SINGLE_ENEMY, DamageType.PSYCHIC, AttackType.RANGED,3),15);
        banditAttackEncounterDeck.add(banditCard5);
        Card banditBowCard1 = new ItemCard("fantasyFitness", UUID.randomUUID().toString(),"Goblin Bow","A crudely built bow.", ItemType.EQUIPPABLE, new DamageAbility("Bow","The goblin looses an arrow.", AbilityTarget.SINGLE_ENEMY, DamageType.NORMAL, AttackType.RANGED,3));
        banditAttackEncounterDeck.add(banditBowCard1);
        Card banditBowCard2 = new ItemCard("fantasyFitness", UUID.randomUUID().toString(),"Goblin Bow","A crudely built bow.", ItemType.EQUIPPABLE, new DamageAbility("Bow","The goblin looses an arrow.", AbilityTarget.SINGLE_ENEMY, DamageType.NORMAL, AttackType.RANGED,3));
        banditAttackEncounterDeck.add(banditBowCard2);
        Card banditSpearCard1 = new ItemCard("fantasyFitness", UUID.randomUUID().toString(),"Goblin Spear","A crudely built spear.", ItemType.EQUIPPABLE, new DamageAbility("Stab","The goblin stabs with its spear.", AbilityTarget.SINGLE_ENEMY, DamageType.NORMAL, AttackType.MELEE,3));
        banditAttackEncounterDeck.add(banditSpearCard1);
        Card banditSpearCard2 = new ItemCard("fantasyFitness", UUID.randomUUID().toString(),"Goblin Spear","A crudely built spear.", ItemType.EQUIPPABLE, new DamageAbility("Stab","The goblin stabs with its spear.", AbilityTarget.SINGLE_ENEMY, DamageType.NORMAL, AttackType.MELEE,3));
        banditAttackEncounterDeck.add(banditSpearCard2);
        Card banditSpearCard3 = new ItemCard("fantasyFitness", UUID.randomUUID().toString(),"Goblin Spear","A crudely built spear.", ItemType.EQUIPPABLE, new DamageAbility("Stab","The goblin stabs with its spear.", AbilityTarget.SINGLE_ENEMY, DamageType.NORMAL, AttackType.MELEE,3));
        banditAttackEncounterDeck.add(banditSpearCard3);
        ItemCard banditHelmetCard1 = new ItemCard(mainActivity.getCurrentUser().getUid(),UUID.randomUUID().toString(),"Helmet", "Makes it harder to hit your head.",ItemType.EQUIPPABLE,new BuffAbility("Helmet","Helmet protects your head.",AbilityTarget.SINGLE_ALLY, BuffType.HEALTH,3));
        banditAttackEncounterDeck.add(banditHelmetCard1);
        ItemCard banditHelmetCard2 = new ItemCard(mainActivity.getCurrentUser().getUid(),UUID.randomUUID().toString(),"Helmet", "Makes it harder to hit your head.",ItemType.EQUIPPABLE,new BuffAbility("Helmet","Helmet protects your head.",AbilityTarget.SINGLE_ALLY, BuffType.HEALTH,3));
        banditAttackEncounterDeck.add(banditHelmetCard2);
        ItemCard banditHealingPotion1 = new ItemCard(mainActivity.getCurrentUser().getUid(), UUID.randomUUID().toString(),"Basic Healing Potion", "Heals a character by 5hp.", ItemType.CONSUMABLE, new HealAbility("Healing Potion","heals",AbilityTarget.SINGLE_ALLY, 5));
        banditAttackEncounterDeck.add(banditHealingPotion1);
        ItemCard banditHealingPotion2 = new ItemCard(mainActivity.getCurrentUser().getUid(), UUID.randomUUID().toString(),"Basic Healing Potion", "Heals a character by 5hp.", ItemType.CONSUMABLE, new HealAbility("Healing Potion","heals",AbilityTarget.SINGLE_ALLY, 5));
        banditAttackEncounterDeck.add(banditHealingPotion2);

        Encounter banditAttackEncounter = new Encounter("Bandit", EncounterDifficultyLevel.MEDIUM,banditAttackEncounterDeck);
        encounterRepository.writeEncounter(banditAttackEncounter,new HashMap<>());
        List<Card> goblinAttackEncounterDeck = new ArrayList<>();
        Card goblinCard1 = new CharacterCard("fantasyFitness", UUID.randomUUID().toString(),"Goblin","A nasty goblin.",new DamageAbility("Punch","The goblin punches.", AbilityTarget.SINGLE_ENEMY, DamageType.NORMAL, AttackType.MELEE,1),10);
        goblinAttackEncounterDeck.add(goblinCard1);
        Card goblinCard2 = new CharacterCard("fantasyFitness", UUID.randomUUID().toString(),"Goblin","A nasty goblin.",new DamageAbility("Punch","The goblin punches.", AbilityTarget.SINGLE_ENEMY, DamageType.NORMAL, AttackType.MELEE,1),10);
        goblinAttackEncounterDeck.add(goblinCard2);
        Card goblinCard3 = new CharacterCard("fantasyFitness", UUID.randomUUID().toString(),"Goblin","A nasty goblin.",new DamageAbility("Punch","The goblin punches.", AbilityTarget.SINGLE_ENEMY, DamageType.NORMAL, AttackType.MELEE,1),10);
        goblinAttackEncounterDeck.add(goblinCard3);
        Card goblinCard4 = new CharacterCard("fantasyFitness", UUID.randomUUID().toString(),"Goblin","A nasty goblin.",new DamageAbility("Stone Throw","The goblin looses an arrow.", AbilityTarget.SINGLE_ENEMY, DamageType.NORMAL, AttackType.RANGED,1),10);
        goblinAttackEncounterDeck.add(goblinCard4);
        Card goblinCard5 = new CharacterCard("fantasyFitness", UUID.randomUUID().toString(),"Goblin","A nasty goblin.",new DamageAbility("Stone Throw","The goblin looses an arrow.", AbilityTarget.SINGLE_ENEMY, DamageType.NORMAL, AttackType.RANGED,1),10);
        goblinAttackEncounterDeck.add(goblinCard5);
        Card bowCard1 = new ItemCard("fantasyFitness", UUID.randomUUID().toString(),"Goblin Bow","A crudely built bow.", ItemType.EQUIPPABLE, new DamageAbility("Bow","The goblin looses an arrow.", AbilityTarget.SINGLE_ENEMY, DamageType.NORMAL, AttackType.RANGED,3));
        goblinAttackEncounterDeck.add(bowCard1);
        Card bowCard2 = new ItemCard("fantasyFitness", UUID.randomUUID().toString(),"Goblin Bow","A crudely built bow.", ItemType.EQUIPPABLE, new DamageAbility("Bow","The goblin looses an arrow.", AbilityTarget.SINGLE_ENEMY, DamageType.NORMAL, AttackType.RANGED,3));
        goblinAttackEncounterDeck.add(bowCard2);
        Card spearCard1 = new ItemCard("fantasyFitness", UUID.randomUUID().toString(),"Goblin Spear","A crudely built spear.", ItemType.EQUIPPABLE, new DamageAbility("Stab","The goblin stabs with its spear.", AbilityTarget.SINGLE_ENEMY, DamageType.NORMAL, AttackType.MELEE,3));
        goblinAttackEncounterDeck.add(spearCard1);
        Card spearCard2 = new ItemCard("fantasyFitness", UUID.randomUUID().toString(),"Goblin Spear","A crudely built spear.", ItemType.EQUIPPABLE, new DamageAbility("Stab","The goblin stabs with its spear.", AbilityTarget.SINGLE_ENEMY, DamageType.NORMAL, AttackType.MELEE,3));
        goblinAttackEncounterDeck.add(spearCard2);
        Card spearCard3 = new ItemCard("fantasyFitness", UUID.randomUUID().toString(),"Goblin Spear","A crudely built spear.", ItemType.EQUIPPABLE, new DamageAbility("Stab","The goblin stabs with its spear.", AbilityTarget.SINGLE_ENEMY, DamageType.NORMAL, AttackType.MELEE,3));
        goblinAttackEncounterDeck.add(spearCard3);
        ItemCard helmetCard1 = new ItemCard(mainActivity.getCurrentUser().getUid(),UUID.randomUUID().toString(),"Helmet", "Makes it harder to hit your head.",ItemType.EQUIPPABLE,new BuffAbility("Helmet","Helmet protects your head.",AbilityTarget.SINGLE_ALLY, BuffType.HEALTH,3));
        goblinAttackEncounterDeck.add(helmetCard1);
        ItemCard helmetCard2 = new ItemCard(mainActivity.getCurrentUser().getUid(),UUID.randomUUID().toString(),"Helmet", "Makes it harder to hit your head.",ItemType.EQUIPPABLE,new BuffAbility("Helmet","Helmet protects your head.",AbilityTarget.SINGLE_ALLY, BuffType.HEALTH,3));
        goblinAttackEncounterDeck.add(helmetCard2);

        Encounter goblinAttackEncounter = new Encounter("Goblin Attack", EncounterDifficultyLevel.EASY,goblinAttackEncounterDeck);
        encounterRepository.writeEncounter(goblinAttackEncounter,new HashMap<>());
    }
}
