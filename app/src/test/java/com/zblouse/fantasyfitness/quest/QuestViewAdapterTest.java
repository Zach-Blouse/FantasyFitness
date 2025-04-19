package com.zblouse.fantasyfitness.quest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.combat.CombatService;
import com.zblouse.fantasyfitness.home.UserHomeFragment;
import com.zblouse.fantasyfitness.user.UserGameStateService;
import com.zblouse.fantasyfitness.user.UserService;
import com.zblouse.fantasyfitness.world.GameLocationService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.Arrays;

@RunWith(RobolectricTestRunner.class)
public class QuestViewAdapterTest {

    @Test
    public void onCreateViewHolderTest(){
        UserService mockUserService = Mockito.mock(UserService.class);
        UserGameStateService mockUserGameStateService = Mockito.mock(UserGameStateService.class);
        CombatService mockCombatService = Mockito.mock(CombatService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setCombatService(mockCombatService);
        mainActivity.setUserService(mockUserService);
        mainActivity.setUserGameStateService(mockUserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        QuestObjective objective1 = new QuestObjective(QuestObjectiveType.VISIT,"2",GameLocationService.THANADEL_VILLAGE,R.id.inn_button,false);
        QuestObjective objective2 = new QuestObjective(QuestObjectiveType.FIGHT,"2",GameLocationService.WOODLANDS,R.id.marsh_button,false);
        Quest quest1 = new Quest("testQuest","testUuid", 5, Arrays.asList(objective1, objective2));

        QuestViewAdapter questViewAdapter = new QuestViewAdapter(testedFragment, Arrays.asList(quest1));
        QuestViewAdapter.ViewHolder viewHolder = questViewAdapter.onCreateViewHolder(returnedView.findViewById(R.id.quest_recyclerView),1);

        assertNotNull(viewHolder.questCard);
        assertNotNull(viewHolder.questNameTextView);
    }

    @Test
    public void onBindViewHolderTest(){
        UserService mockUserService = Mockito.mock(UserService.class);
        UserGameStateService mockUserGameStateService = Mockito.mock(UserGameStateService.class);
        CombatService mockCombatService = Mockito.mock(CombatService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setCombatService(mockCombatService);
        mainActivity.setUserService(mockUserService);
        mainActivity.setUserGameStateService(mockUserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        QuestObjective objective1 = new QuestObjective(QuestObjectiveType.VISIT,"2",GameLocationService.THANADEL_VILLAGE,R.id.inn_button,false);
        QuestObjective objective2 = new QuestObjective(QuestObjectiveType.FIGHT,"2",GameLocationService.WOODLANDS,R.id.marsh_button,false);
        Quest quest1 = new Quest("testQuest","testUuid", 5, Arrays.asList(objective1, objective2));

        QuestViewAdapter questViewAdapter = new QuestViewAdapter(testedFragment, Arrays.asList(quest1));
        QuestViewAdapter.ViewHolder viewHolder = questViewAdapter.onCreateViewHolder(returnedView.findViewById(R.id.quest_recyclerView),1);

        questViewAdapter.onBindViewHolder(viewHolder,0);

        assertEquals(View.VISIBLE, viewHolder.questCard.getVisibility());
        assertEquals(quest1.getQuestName(),viewHolder.questNameTextView.getText());
    }
}
