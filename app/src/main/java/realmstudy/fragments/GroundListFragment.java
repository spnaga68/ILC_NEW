package realmstudy.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import io.realm.Realm;
import realmstudy.MyApplication;
import realmstudy.R;
import realmstudy.adapter.GroundListAdapter;
import realmstudy.data.RealmObjectData.Ground;
import realmstudy.interfaces.DialogInterface;

/**
 * Created by developer on 27/12/16.
 */

public class GroundListFragment extends Fragment implements DialogInterface {

    private static final int MY_PERMISSIONS_REQUEST_CONTACTS = 420;
    private static final int PICK_CONTACT = 421;
    private RecyclerView
            list_view;
    private android.support.design.widget.FloatingActionButton add;
    private android.support.design.widget.FloatingActionButton add_from_contacts;
    GroundListAdapter adapter;
    @Inject
    Realm realm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.player_list_view, container, false);
        ((MyApplication) getActivity().getApplication()).getComponent().inject(this);

        list_view = (RecyclerView) v.findViewById(R.id.list_view);
        add = (android.support.design.widget.FloatingActionButton) v.findViewById(R.id.add);
        add_from_contacts = (android.support.design.widget.FloatingActionButton) v.findViewById(R.id.add_from_contacts);
        adapter = new GroundListAdapter(getActivity(), realm.where(Ground.class).findAll());
        list_view.setAdapter(adapter);
        list_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                GroundListFragment f = new GroundListFragment();
                f.setArguments(b);
                (getActivity()).getSupportFragmentManager().beginTransaction().add(R.id.mainFrag, f).addToBackStack(null).commit();
            }
        });


        return v;
    }

    private void pickFromContacts() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null)
            getActivity().setTitle(getString(R.string.ground));
//        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
//            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
//
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_CONTACTS) {

            // If request is cancelled, the result arrays are empty.
            System.out.println("___" + grantResults.length + "____" + grantResults[0]);
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                pickFromContacts();

            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.

            }
            //  return;
        }
        //  break;

    }


    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        String cNumber = "";
        String name = "";
    }

    @Override
    public void onSuccess(String result, boolean success) {

    }
}