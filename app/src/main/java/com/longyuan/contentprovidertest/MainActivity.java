package com.longyuan.contentprovidertest;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import android.provider.ContactsContract;
import android.widget.ListView;

import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {


    ListView mWordList;

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mWordList = (ListView) findViewById(R.id.listView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        }

    }

    public void showContacts() {

        // A "projection" defines the columns that will be returned for each row
        String[] mProjection =
                {
                        /*UserDictionary.Words._ID,    // Contract class constant for the _ID column name
                        UserDictionary.Words.WORD,   // Contract class constant for the word column name
                        UserDictionary.Words.LOCALE  // Contract class constant for the locale column name*/
                        ContactsContract.Contacts._ID,
                        ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.Contacts.CONTACT_STATUS
                };

        // Defines a string to contain the selection clause
        String mSelectionClause = null;

        // Initializes an array to contain selection arguments
        String[] mSelectionArgs = {""};



          /* This defines a one-element String array to contain the selection argument.
          */
        //String[] mSelectionArgs = {""};

          // Gets a word from the UI
        //String mSearchString = mSearchWord.getText().toString();


        mSelectionClause = null;
        mSelectionArgs = null;


         // Does a query against the table and returns a Cursor object
        Cursor mCursor = getContentResolver().query(
                //UserDictionary.Words.CONTENT_URI,  // The content URI of the words table
                ContactsContract.Contacts.CONTENT_URI,
                mProjection,                       // The columns to return for each row
                mSelectionClause,                   // Either null, or the word the user entered
                mSelectionArgs,                    // Either empty, or the string the user entered
                null);                       // The sort order for the returned rows

// Some providers return null if an error occurs, others throw an exception
        if (null == mCursor) {

        /* Insert code here to handle the error. Be sure not to use the cursor! You may want to
        * call android.util.Log.e() to log this error.
        *
        */
        // If the Cursor is empty, the provider found no matches
        } else if (mCursor.getCount() < 1) {

    /*
     * Insert code here to notify the user that the search was unsuccessful. This isn't necessarily
     * an error. You may want to offer the user the option to insert a new row, or re-type the
     * search term.
     */

        } else {


            // Insert code here to do something with the results
            String[] mWordListColumns =
                    {
                            // UserDictionary.Words.WORD,   // Contract class constant containing the word column name
                            //UserDictionary.Words.LOCALE,  // Contract class constant containing the locale column name*//*
                            ContactsContract.Contacts.DISPLAY_NAME,
                            ContactsContract.Contacts.CONTACT_STATUS

                    };

            // Defines a list of View IDs that will receive the Cursor columns for each row
            int[] mWordListItems = {R.id.dictWord, R.id.locale};

            // Creates a new SimpleCursorAdapter
            SimpleCursorAdapter mCursorAdapter = new SimpleCursorAdapter(
                    getApplicationContext(),               // The application's Context object
                    R.layout.wordlistrow,                  // A layout in XML for one row in the ListView
                    mCursor,                               // The result from the query
                    mWordListColumns,                      // A string array of column names in the cursor
                    mWordListItems,                        // An integer array of view IDs in the row layout
                    0);                                    // Flags (usually none are needed)

            // Sets the adapter for the ListView
            mWordList.setAdapter(mCursorAdapter);


        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
