package realmstudy.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import realmstudy.MainFragmentActivity;
import realmstudy.R;
import realmstudy.matchList.MatchListMainFragment;

/**
 * Created by $hakt! Vasu on 2/11/2017.
 */
public class MenuActivity extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.menu_page,container,false);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainFragmentActivity)(getActivity())).homePage_title(getString(R.string.menu));
    }

    public void onClick(View view){
       // ShowToast.centerL(getActivity(),view.getClass().getCanonicalName());
        switch (view.getId()){
            case R.id.new_game_menu_lay:
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.mainFrag,new TeamListSelectionFragment()).commit();
                break;
            case R.id.saved_game_menu_lay:

                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.mainFrag,new MatchListMainFragment()).commit();
                break;
            case R.id.profile_menu_lay:
                Fragment f=new OversFragment();
                Bundle b=new Bundle();
                b.putInt("match_id",1492432485);
                f.setArguments(b);
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.mainFrag,f).commit();
                break;
            case R.id.schedule_game_menu_lay:
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.mainFrag,new ScheduleNewGame()).commit();

                break;
            case R.id.add_player_lay:
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.mainFrag,new PlayerListFragment()).commit();
                break;
            case R.id.add_team_menu_lay:
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.mainFrag,new TeamListFragment()).commit();
                break;


        }

    }
}
