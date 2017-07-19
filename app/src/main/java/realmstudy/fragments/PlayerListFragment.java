package realmstudy.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import javax.inject.Inject;

import realmstudy.MainFragmentActivity;
import realmstudy.MyApplication;
import realmstudy.R;
import realmstudy.adapter.PlayerListAdapter;
import realmstudy.data.RealmObjectData.Player;
import realmstudy.databaseFunctions.RealmDB;
import realmstudy.interfaces.DialogInterface;

import io.realm.Realm;

/**
 * Created by developer on 27/12/16.
 */

public class PlayerListFragment extends Fragment implements DialogInterface {

    private static final int MY_PERMISSIONS_REQUEST_CONTACTS = 420;
    private static final int PICK_CONTACT = 421;
    private RecyclerView
            list_view;
    private android.support.design.widget.FloatingActionButton add;
    private android.support.design.widget.FloatingActionButton add_from_contacts;
    PlayerListAdapter adapter;
    @Inject
    Realm realm;
    int teamID=-1;

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

        if(getArguments()!=null){
            teamID=getArguments().getInt("id");
        }

        list_view = (RecyclerView) v.findViewById(R.id.list_view);
        add = (android.support.design.widget.FloatingActionButton) v.findViewById(R.id.add);
        add_from_contacts = (android.support.design.widget.FloatingActionButton) v.findViewById(R.id.add_from_contacts);
        adapter = new PlayerListAdapter(getActivity(), realm.where(Player.class).equalTo("teamID",teamID).findAll());
        list_view.setAdapter(adapter);
        v.findViewById(R.id.progress_bar).setVisibility(View.GONE);
        list_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainFragmentActivity) getActivity()).showNewTeamDialog(1, PlayerListFragment.this,teamID);
            }
        });
        add_from_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                        ) {
                    // PlayerListFragment
                    requestPermissions(
                            new String[]{Manifest.permission.READ_CONTACTS},
                            MY_PERMISSIONS_REQUEST_CONTACTS);

                    Toast.makeText(getActivity(), "sdfkjsij", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "_______", Toast.LENGTH_SHORT).show();
                    pickFromContacts();
                }
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
        if(getActivity()!=null)
        getActivity().setTitle((teamID!=-1? RealmDB.getTeam(realm,teamID).nick_name:"" )+" "+getString(R.string.players));
        if (((AppCompatActivity)getActivity()).getSupportActionBar() != null){
            ((MainFragmentActivity)getActivity()).removeNaviHome();


        }
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
        switch (reqCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {

                    Uri contactData = data.getData();
                    Cursor c = getActivity().getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {


                        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getActivity().getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                    null, null);
                            phones.moveToFirst();
                            cNumber = phones.getString(phones.getColumnIndex("data1"));
                            System.out.println("number is:" + cNumber);
                        }
                        name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));


                    }

                    if (!name.trim().isEmpty()) {
                        realm.beginTransaction();
                        Player playerObj = realm.createObject(Player.class, realm.where(Player.class).findAll().size());
                        playerObj.setPh_no(cNumber);
                        playerObj.setName(name);
                        playerObj.setTeamID(teamID);
                        realm.commitTransaction();
                    }

                }
                break;
        }
    }

    @Override
    public void onSuccess(String result, boolean success) {

    }
}