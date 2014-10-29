package katherinechen.squigglers.com.tasksplitter;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

public class CreateTaskFragment extends Fragment {
    private SessionManager session;
    private DbHelper dbhelper;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManager(getActivity());
        dbhelper = new DbHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_create_task, container, false);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        setGroupSpinner();  //fills group drop down menu
    }



    //fills the group drop down menu with the user's groups
    private void setGroupSpinner() {
        Cursor cursor = dbhelper.getUserGroupsCursor(session.getUserId());

        //create an array to specify which fields we want to display
        String[] from = new String[]{DbHelper.Groupy.NAME};
        //create an array of the display item we want to bind our data to
        int[] to = new int[] {android.R.id.text1};

        //create simple cursor adapter
        SimpleCursorAdapter adapter =
                new SimpleCursorAdapter(getActivity(), android.R.layout.simple_spinner_item, cursor, from, to, 0);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //get reference to our spinner
        Spinner spinner = (Spinner) view.findViewById(R.id.groupSpinner);
        spinner.setAdapter(adapter);

        //get group selected in order to fill assign to spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = (Cursor)parent.getItemAtPosition(position);
                int selectedGroupId = c.getInt(c.getColumnIndexOrThrow(DbHelper.Groupy._ID));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setSession(SessionManager session)
    {
        this.session = session;
    }

    public void setDbhelper(DbHelper dbhelper) {
        this.dbhelper = dbhelper;
    }
}
