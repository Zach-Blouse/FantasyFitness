package com.zblouse.fantasyfitness.combat;

import static android.app.Activity.RESULT_OK;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.Manifest;
import android.app.Instrumentation;
import android.content.ContentQueryMap;
import android.os.Looper;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.actions.ActionResultType;
import com.zblouse.fantasyfitness.actions.RandomActionResultTypeGenerator;
import com.zblouse.fantasyfitness.activity.DeviceServiceType;
import com.zblouse.fantasyfitness.activity.LocationDeviceService;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.activity.PermissionDeviceService;
import com.zblouse.fantasyfitness.combat.cards.Ability;
import com.zblouse.fantasyfitness.combat.cards.AbilityTarget;
import com.zblouse.fantasyfitness.combat.cards.AbilityType;
import com.zblouse.fantasyfitness.combat.cards.AttackType;
import com.zblouse.fantasyfitness.combat.cards.BuffAbility;
import com.zblouse.fantasyfitness.combat.cards.BuffType;
import com.zblouse.fantasyfitness.combat.cards.Card;
import com.zblouse.fantasyfitness.combat.cards.CardFirestoreDatabase;
import com.zblouse.fantasyfitness.combat.cards.CardRepository;
import com.zblouse.fantasyfitness.combat.cards.CardService;
import com.zblouse.fantasyfitness.combat.cards.CardType;
import com.zblouse.fantasyfitness.combat.cards.CharacterCard;
import com.zblouse.fantasyfitness.combat.cards.DamageAbility;
import com.zblouse.fantasyfitness.combat.cards.DamageType;
import com.zblouse.fantasyfitness.combat.cards.DeckFirestoreDatabase;
import com.zblouse.fantasyfitness.combat.cards.DeckRepository;
import com.zblouse.fantasyfitness.combat.cards.DeckService;
import com.zblouse.fantasyfitness.combat.cards.HealAbility;
import com.zblouse.fantasyfitness.combat.cards.ItemCard;
import com.zblouse.fantasyfitness.combat.cards.ItemType;
import com.zblouse.fantasyfitness.combat.encounter.Encounter;
import com.zblouse.fantasyfitness.combat.encounter.EncounterDifficultyLevel;
import com.zblouse.fantasyfitness.combat.encounter.EncounterFirestoreDatabase;
import com.zblouse.fantasyfitness.combat.encounter.EncounterRepository;
import com.zblouse.fantasyfitness.combat.encounter.EncounterService;
import com.zblouse.fantasyfitness.user.UserFirestoreDatabase;
import com.zblouse.fantasyfitness.user.UserGameStateFirestoreDatabase;
import com.zblouse.fantasyfitness.user.UserGameStateRepository;
import com.zblouse.fantasyfitness.user.UserGameStateService;
import com.zblouse.fantasyfitness.user.UserRepository;
import com.zblouse.fantasyfitness.user.UserService;
import com.zblouse.fantasyfitness.workout.WorkoutService;
import com.zblouse.fantasyfitness.world.GameLocationService;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RunWith(AndroidJUnit4.class)
public class CombatTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule =
            new IntentsTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule grantPermissionsRule = GrantPermissionRule.grant(Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.FOREGROUND_SERVICE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);


    @Test
    public void combatTest() throws InterruptedException {

        //THIS TEST SETUP IS NEEDED TO AUTHENTICATE WITH THE APPLICATION
        com.firebase.ui.auth.data.model.User user = new User.Builder("google", "test@test.com")
                .setName("Test user")
                .build();
        IdpResponse response = new IdpResponse.Builder(user).build();
        FirebaseUser mockFirebaseUser = Mockito.mock(FirebaseUser.class);
        when(mockFirebaseUser.getUid()).thenReturn("testUserId");

        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        when(mockAuth.getCurrentUser()).thenReturn(mockFirebaseUser);
        UserService userService = new UserService();
        PermissionDeviceService mockPermissionDeviceService = Mockito.mock(PermissionDeviceService.class);
        when(mockPermissionDeviceService.hasPermission(any())).thenReturn(true);
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
        WorkoutService workoutService = new WorkoutService();

        LocationDeviceService mockLocationDeviceService = Mockito.mock(LocationDeviceService.class);
        RandomActionResultTypeGenerator mockRandomActionResultTypeGenerator = Mockito.mock(RandomActionResultTypeGenerator.class);
        when(mockRandomActionResultTypeGenerator.getRandomActionResult(GameLocationService.WOODLANDS)).thenReturn(ActionResultType.COMBAT);

        EncounterService encounterService = new EncounterService();
        DeckService deckService = new DeckService();
        CardService cardService = new CardService();
        CombatService combatService = new CombatService();

        UserGameStateService userGameStateService = new UserGameStateService();

        activityRule.getScenario().onActivity(activity -> {

            activity.setFirebaseAuth(mockAuth);
            userService.setMainActivity(activity);
            workoutService.setMainActivity(activity);
            userGameStateService.setMainActivity(activity);
            encounterService.setMainActivity(activity);
            deckService.setMainActivity(activity);
            cardService.setMainActivity(activity);
            combatService.setMainActivity(activity);
            activity.setUserService(userService);
            activity.setWorkoutService(workoutService);
            activity.setDeviceService(DeviceServiceType.PERMISSION,mockPermissionDeviceService);
            activity.setDeviceService(DeviceServiceType.LOCATION,mockLocationDeviceService);
            activity.setUserGameStateService(userGameStateService);
            activity.getExploreActionService().setRandomActionResultTypeGenerator(mockRandomActionResultTypeGenerator);
            activity.setEncounterService(encounterService);
            activity.setDeckService(deckService);
            activity.setCardService(cardService);
            activity.setCombatService(combatService);
        });

        //Set up mock firestore
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);

        CollectionReference mockCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("users"))).thenReturn(mockCollectionReference);
        DocumentReference mockDocument = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference.document(eq("testUserId"))).thenReturn(mockDocument);

        Task<DocumentSnapshot> mockReadTask = Mockito.mock(Task.class);
        when(mockDocument.get()).thenReturn(mockReadTask);
        ArgumentCaptor<OnCompleteListener<DocumentSnapshot>> onCompleteListenerArgumentCaptorRead = ArgumentCaptor.forClass(OnCompleteListener.class);
        DocumentSnapshot mockDocumentSnapshot = Mockito.mock(DocumentSnapshot.class);

        when(mockReadTask.isSuccessful()).thenReturn(true);
        when(mockReadTask.getResult()).thenReturn(mockDocumentSnapshot);
        when(mockDocumentSnapshot.exists()).thenReturn(true);
        when(mockDocumentSnapshot.get("UID")).thenReturn("testUid");
        when(mockDocumentSnapshot.get("USERNAME")).thenReturn("testUsername");

        UserFirestoreDatabase userFirestoreDatabase = new UserFirestoreDatabase(mockFirestore);
        UserRepository userRepository = new UserRepository(userService, userFirestoreDatabase);
        userService.setUserRepository(userRepository);

        CollectionReference mockGameStateCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("gameState"))).thenReturn(mockGameStateCollectionReference);
        DocumentReference mockGameStateDocument = Mockito.mock(DocumentReference.class);
        when(mockGameStateCollectionReference.document(eq("testUserId"))).thenReturn(mockGameStateDocument);

        Task<DocumentSnapshot> mockGameStateReadTask = Mockito.mock(Task.class);
        when(mockGameStateDocument.get()).thenReturn(mockGameStateReadTask);
        ArgumentCaptor<OnCompleteListener<DocumentSnapshot>> onCompleteListenerArgumentCaptorReadGameState = ArgumentCaptor.forClass(OnCompleteListener.class);
        DocumentSnapshot mockGameStateDocumentSnapshot = Mockito.mock(DocumentSnapshot.class);

        when(mockGameStateReadTask.isSuccessful()).thenReturn(true);
        when(mockGameStateReadTask.getResult()).thenReturn(mockGameStateDocumentSnapshot);
        when(mockGameStateDocumentSnapshot.exists()).thenReturn(true);
        when(mockGameStateDocumentSnapshot.get(eq("gameLocation"), eq(String.class))).thenReturn(GameLocationService.WOODLANDS);
        when(mockGameStateDocumentSnapshot.get(eq("savedDistance"), eq(Double.class))).thenReturn(5500.0);
        when(mockGameStateReadTask.isSuccessful()).thenReturn(true);
        when(mockGameStateReadTask.getResult()).thenReturn(mockGameStateDocumentSnapshot);
        when(mockGameStateDocumentSnapshot.exists()).thenReturn(true);

        Task<Void> mockGameStateUpdateTask = Mockito.mock(Task.class);
        when(mockGameStateDocument.update(anyString(), any())).thenReturn(mockGameStateUpdateTask);
        ArgumentCaptor<OnCompleteListener<Void>> onCompleteListenerArgumentCaptorUpdateGameState = ArgumentCaptor.forClass(OnCompleteListener.class);
        when(mockGameStateUpdateTask.isSuccessful()).thenReturn(true);

        UserGameStateFirestoreDatabase userGameStateFirestoreDatabase = new UserGameStateFirestoreDatabase(mockFirestore);
        UserGameStateRepository userGameStateRepository = new UserGameStateRepository(userGameStateFirestoreDatabase, userGameStateService);
        userGameStateService.setUserGameStateRepository(userGameStateRepository);

        //Set Up Encounter Database
        CollectionReference mockEncounterCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("encounters"))).thenReturn(mockEncounterCollectionReference);
        DocumentReference mockEncounterDocument = Mockito.mock(DocumentReference.class);
        when(mockEncounterCollectionReference.document(eq("Goblin Attack"))).thenReturn(mockEncounterDocument);

        Task<DocumentSnapshot> mockEncounterReadTask = Mockito.mock(Task.class);
        when(mockEncounterDocument.get()).thenReturn(mockEncounterReadTask);
        ArgumentCaptor<OnCompleteListener<DocumentSnapshot>> onCompleteListenerArgumentCaptorReadEncounter = ArgumentCaptor.forClass(OnCompleteListener.class);
        DocumentSnapshot mockEncounterDocumentSnapshot = Mockito.mock(DocumentSnapshot.class);

        when(mockEncounterReadTask.isSuccessful()).thenReturn(true);
        when(mockEncounterReadTask.getResult()).thenReturn(mockEncounterDocumentSnapshot);
        when(mockEncounterDocumentSnapshot.exists()).thenReturn(true);
        Map<String,Object> testEncounter = generateGoblinEncounter();
        when(mockEncounterDocumentSnapshot.getData()).thenReturn(testEncounter);
        when(mockEncounterReadTask.isSuccessful()).thenReturn(true);
        when(mockEncounterReadTask.getResult()).thenReturn(mockEncounterDocumentSnapshot);
        when(mockEncounterDocumentSnapshot.exists()).thenReturn(true);

        EncounterFirestoreDatabase encounterFirestoreDatabase = new EncounterFirestoreDatabase(mockFirestore);
        EncounterRepository encounterRepository = new EncounterRepository(encounterService, encounterFirestoreDatabase);
        encounterService.setEncounterRepository(encounterRepository);

        //Setting Up Deck DB
        CollectionReference mockUserCardsCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("userCards"))).thenReturn(mockUserCardsCollectionReference);
        DocumentReference mockUserCardsDocument = Mockito.mock(DocumentReference.class);
        when(mockUserCardsCollectionReference.document(eq("testUserId"))).thenReturn(mockUserCardsDocument);

        CollectionReference mockDeckCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockUserCardsDocument.collection(eq("decks"))).thenReturn(mockDeckCollectionReference);
        DocumentReference mockDeckDocument = Mockito.mock(DocumentReference.class);
        when(mockDeckCollectionReference.document(eq("userDeck"))).thenReturn(mockDeckDocument);

        Task<DocumentSnapshot> mockDeckReadTask = Mockito.mock(Task.class);
        when(mockDeckDocument.get()).thenReturn(mockDeckReadTask);
        ArgumentCaptor<OnCompleteListener<DocumentSnapshot>> onCompleteListenerArgumentCaptorReadDeck = ArgumentCaptor.forClass(OnCompleteListener.class);
        DocumentSnapshot mockDeckDocumentSnapshot = Mockito.mock(DocumentSnapshot.class);

        when(mockDeckReadTask.isSuccessful()).thenReturn(true);
        when(mockDeckReadTask.getResult()).thenReturn(mockGameStateDocumentSnapshot);
        when(mockDeckDocumentSnapshot.exists()).thenReturn(true);
        when(mockDeckDocumentSnapshot.get(eq("deckList"))).thenReturn(Arrays.asList("1", "2", "3", "4", "5", "6"));
        when(mockDeckReadTask.isSuccessful()).thenReturn(true);
        when(mockDeckReadTask.getResult()).thenReturn(mockDeckDocumentSnapshot);
        when(mockDeckDocumentSnapshot.exists()).thenReturn(true);

        DeckFirestoreDatabase deckFirestoreDatabase = new DeckFirestoreDatabase(mockFirestore);
        DeckRepository deckRepository = new DeckRepository(deckFirestoreDatabase, deckService);
        deckService.setDeckRepository(deckRepository);

        //Setting Up Card DB

        CollectionReference mockCardCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockUserCardsDocument.collection(eq("cards"))).thenReturn(mockCardCollectionReference);
        Query mockQuery = Mockito.mock(Query.class);
        when(mockCardCollectionReference.whereIn(eq("cardUuid"),any())).thenReturn(mockQuery);
        Task<QuerySnapshot> mockQueryTask = Mockito.mock(Task.class);
        when(mockQuery.get()).thenReturn(mockQueryTask);
        ArgumentCaptor<OnCompleteListener<QuerySnapshot>> onCompleteListenerArgumentCaptorCards = ArgumentCaptor.forClass(OnCompleteListener.class);
        when(mockQueryTask.isSuccessful()).thenReturn(true);
        List<QueryDocumentSnapshot> snapshots = new ArrayList<>();

        CharacterCard characterCard1 = new CharacterCard("testUserId", "1","Zach","Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.ICE, AttackType.MELEE,1),5);
        QueryDocumentSnapshot mockCharacterCard1Snapshot = Mockito.mock(QueryDocumentSnapshot.class);
        when(mockCharacterCard1Snapshot.getData()).thenReturn(convertCardToMap(characterCard1));
        snapshots.add(mockCharacterCard1Snapshot);

        ItemCard testItem1 = new ItemCard("testUserId", "2", "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        QueryDocumentSnapshot mockTestItem1Snapshot = Mockito.mock(QueryDocumentSnapshot.class);
        when(mockTestItem1Snapshot.getData()).thenReturn(convertCardToMap(testItem1));
        snapshots.add(mockTestItem1Snapshot);

        ItemCard testItem2 = new ItemCard("testUserId", "3", "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        QueryDocumentSnapshot mockTestItem2Snapshot = Mockito.mock(QueryDocumentSnapshot.class);
        when(mockTestItem2Snapshot.getData()).thenReturn(convertCardToMap(testItem2));
        snapshots.add(mockTestItem2Snapshot);

        ItemCard testItem3 = new ItemCard("testUserId", "4", "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        QueryDocumentSnapshot mockTestItem3Snapshot = Mockito.mock(QueryDocumentSnapshot.class);
        when(mockTestItem3Snapshot.getData()).thenReturn(convertCardToMap(testItem3));
        snapshots.add(mockTestItem3Snapshot);

        ItemCard testItem4 = new ItemCard("testUserId", "5", "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        QueryDocumentSnapshot mockTestItem4Snapshot = Mockito.mock(QueryDocumentSnapshot.class);
        when(mockTestItem4Snapshot.getData()).thenReturn(convertCardToMap(testItem4));
        snapshots.add(mockTestItem4Snapshot);

        ItemCard testItem5 = new ItemCard("testUserId", "6", "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        QueryDocumentSnapshot mockTestItem5Snapshot = Mockito.mock(QueryDocumentSnapshot.class);
        when(mockTestItem5Snapshot.getData()).thenReturn(convertCardToMap(testItem5));
        snapshots.add(mockTestItem5Snapshot);

        QuerySnapshot querySnapshot = Mockito.mock(QuerySnapshot.class);
        when(querySnapshot.iterator()).thenReturn(snapshots.iterator());
        when(mockQueryTask.getResult()).thenReturn(querySnapshot);

        CardFirestoreDatabase cardFirestoreDatabase = new CardFirestoreDatabase(mockFirestore);
        CardRepository cardRepository = new CardRepository(cardFirestoreDatabase, cardService);
        cardService.setCardRepository(cardRepository);

        //Setting Up Firebase

        Instrumentation.ActivityResult firebaseResult = new Instrumentation.ActivityResult(RESULT_OK, response.toIntent());
        intending(hasComponent("com.firebase.ui.auth.KickoffActivity")).respondWith(firebaseResult);

        onView(withId(R.id.app_title)).check(matches(withText("Fantasy Fitness")));
        onView(withId(R.id.login_button)).perform(click());
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
        verify(mockReadTask).addOnCompleteListener(onCompleteListenerArgumentCaptorRead.capture());
        onCompleteListenerArgumentCaptorRead.getValue().onComplete(mockReadTask);
        onView(withId(R.id.user_home_title)).check(matches(withText("Fantasy Fitness")));

        //FINISH AUTHENTICATION, TEST IS AT USER HOME
        //FOLLOWING THIS LINE IS THE BEGINNING OF THE ACTUAL TEST THIS TEST IS SUPPOSED TO BE TESTING
        verify(mockGameStateReadTask).addOnCompleteListener(onCompleteListenerArgumentCaptorReadGameState.capture());
        onCompleteListenerArgumentCaptorReadGameState.getValue().onComplete(mockGameStateReadTask);

        onView(withId(R.id.dialog_card_view)).check(matches(not(isDisplayed())));
        onView(withId(R.id.marsh_button)).perform(click());
        //should now be in combat screen
        verify(mockEncounterReadTask).addOnCompleteListener(onCompleteListenerArgumentCaptorReadEncounter.capture());
        onCompleteListenerArgumentCaptorReadEncounter.getValue().onComplete(mockEncounterReadTask);
        verify(mockDeckReadTask).addOnCompleteListener(onCompleteListenerArgumentCaptorReadDeck.capture());
        onCompleteListenerArgumentCaptorReadDeck.getValue().onComplete(mockDeckReadTask);
        verify(mockQueryTask).addOnCompleteListener(onCompleteListenerArgumentCaptorCards.capture());
        onCompleteListenerArgumentCaptorCards.getValue().onComplete(mockQueryTask);

        onView(withId(R.id.detailed_card)).check(matches(not(isDisplayed())));
        onView(withId(R.id.enemyHand)).check(matches(isDisplayed()));
    }

    private Map<String, Object> generateGoblinEncounter(){
        Map<String, Object> encounterMap = new HashMap<>();
        encounterMap.put(Encounter.ENCOUNTER_DIFFICULTY_LEVEL_FIELD, EncounterDifficultyLevel.EASY.toString());
        encounterMap.put(Encounter.ENCOUNTER_NAME_FIELD, "Goblin Attack");

        List<Card> encounterCards = new ArrayList<>();

        CharacterCard encounterCharacterCard1 = new CharacterCard("encounter", UUID.randomUUID().toString(),"Dark Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),3);
        encounterCards.add(encounterCharacterCard1);
        CharacterCard encounterCharacterCard2 = new CharacterCard("encounter", UUID.randomUUID().toString(),"Dark Zach 2","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),15);
        encounterCards.add(encounterCharacterCard2);
        CharacterCard encounterCharacterCard3 = new CharacterCard("encounter", UUID.randomUUID().toString(),"Dark Ranged Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.RANGED,1),5);
        encounterCards.add(encounterCharacterCard3);
        ItemCard testItem1 = new ItemCard("encounter", UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 4));
        encounterCards.add(testItem1);
        ItemCard testItem2 = new ItemCard("encounter", UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 4));
        encounterCards.add(testItem2);
        ItemCard testItem3 = new ItemCard("encounter", UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 4));
        encounterCards.add(testItem3);

        List<Map<String,Object>> cards = new ArrayList<>();
        for(Card card: encounterCards){
            cards.add(convertCardToMap(card));
        }
        encounterMap.put(Encounter.ENEMY_CARDS_FIELD, cards);

        return encounterMap;
    }

    private Map<String, Object> convertCardToMap(Card card){
        Map<String, Object> cardMap = new HashMap<>();
        cardMap.put(Card.USER_ID_FIELD,card.getUserId());
        cardMap.put(Card.CARD_UUID_FIELD, card.getCardUuid());
        cardMap.put(Card.CARD_DESCRIPTION_FIELD, card.getCardDescription());
        cardMap.put(Card.CARD_TYPE_FIELD, card.getCardType().toString());
        if(card.getCardType().equals(CardType.CHARACTER)){
            CharacterCard characterCard = (CharacterCard)card;
            cardMap.put(CharacterCard.MAX_HEALTH_FIELD, (long)characterCard.getMaxHealth());
            List<Map<String,Object>> abilities = new ArrayList<>();
            for(Ability ability: characterCard.getAbilities()){
                abilities.add(convertAbilityToMap(ability));
            }
            cardMap.put(CharacterCard.ABILITIES_FIELD,abilities);
        } else if(card.getCardType().equals(CardType.ITEM)){
            ItemCard itemCard = (ItemCard)card;
            cardMap.put(ItemCard.ABILITY_FIELD, convertAbilityToMap(itemCard.getAbility()));
            cardMap.put(ItemCard.ITEM_TYPE_FIELD, itemCard.getItemType().toString());
        }
        return cardMap;
    }

    private Map<String, Object> convertAbilityToMap(Ability ability){
        Map<String, Object> abilityMap = new HashMap<>();
        abilityMap.put(Ability.ABILITY_TARGET_FIELD, ability.getAbilityTarget().toString());
        abilityMap.put(Ability.ABILITY_TYPE_FIELD, ability.getAbilityType().toString());
        abilityMap.put(Ability.DESCRIPTION_FIELD, ability.getAbilityDescription());
        abilityMap.put(Ability.NAME_FIELD, ability.getAbilityName());
        if(ability.getAbilityType().equals(AbilityType.DAMAGE)){
            DamageAbility damageAbility = (DamageAbility)ability;
            abilityMap.put(DamageAbility.ATTACK_TYPE_FIELD, damageAbility.getAttackType().toString());
            abilityMap.put(DamageAbility.DAMAGE_AMOUNT_FIELD, (long)damageAbility.getDamageAmount());
            abilityMap.put(DamageAbility.DAMAGE_TYPE_FIELD, damageAbility.getDamageType().toString());
        } else if(ability.getAbilityType().equals(AbilityType.HEAL)){
            HealAbility healAbility = (HealAbility)ability;
            abilityMap.put(HealAbility.HEAL_AMOUNT_FIELD, (long)healAbility.getHealAmount());
        } else if(ability.getAbilityType().equals(AbilityType.BUFF)){
            BuffAbility buffAbility = (BuffAbility) ability;
            abilityMap.put(BuffAbility.BUFF_AMOUNT_FIELD, (long)buffAbility.getBuffAmount());
            abilityMap.put(BuffAbility.BUFF_TYPE_FIELD, buffAbility.getBuffType().toString());
        }
        return abilityMap;
    }


}
