package com.zblouse.fantasyfitness.combat.cards;

import android.util.Log;

import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.core.DomainService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeckService implements DomainService<Deck> {

    private static final String DECK_SERVICE_ORIGINAL_METHOD = "deckServiceOriginalMethod";
    private static final String DECK_METADATA_KEY = "deckServiceDeck";

    private DeckRepository deckRepository;
    private MainActivity mainActivity;

    public DeckService(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.deckRepository = new DeckRepository(this);
    }

    public DeckService(){

    }

    public void setMainActivity(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    public void setDeckRepository(DeckRepository deckRepository){
        this.deckRepository = deckRepository;
    }

    public void writeNewDeck(String deckName, List<String> cardUuids){
        Deck deck = new Deck(mainActivity.getCurrentUser().getUid(),deckName, cardUuids);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(DECK_SERVICE_ORIGINAL_METHOD,"writeNewDeck");
        deckRepository.writeDeck(deck,metadata);
    }

    public void fetchDeck(String userId, String deckName){
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(DECK_SERVICE_ORIGINAL_METHOD,"fetchDeck");
        deckRepository.fetchDeck(userId,deckName,metadata);
    }

    @Override
    public void repositoryResponse(Deck deck, Map<String, Object> metadata) {
        if(deck != null && !deck.isEmpty()){
            if(deck.needToLoadCards()){
                metadata.put(DomainService.INTER_DOMAIN_SERVICE_ORIGIN_KEY,this);
                metadata.put(DECK_METADATA_KEY,deck);
                mainActivity.getCardService().getCardList(deck.getCardUuids(), metadata);
            } else {
                mainActivity.publishEvent(new DeckFetchEvent(deck, metadata));
            }
        }
    }

    @Override
    public void interDomainServiceResponse(Object responseObject, Map<String, Object> metadata) {
        if(metadata.get(INTER_DOMAIN_SERVICE_RESPONSE_CLASS_KEY).equals(List.class)){
            if(metadata.get(DECK_SERVICE_ORIGINAL_METHOD).equals("fetchDeck")){
                List<Card> cardList = (List) responseObject;
                Deck deck = (Deck)metadata.get(DECK_METADATA_KEY);
                deck.setCards(cardList);
                mainActivity.publishEvent(new DeckFetchEvent(deck, metadata));
            }
        }
    }
}
