package katherinechen.squigglers.com.tasksplitter;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GroupPageFragment extends Fragment {
    private SessionManager session;
    private DbHelper dbhelper;

    private int userId;
    private int groupId;
    private String accesscode;

    TextView accesscodetext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_page, container, false);

        accesscodetext = (TextView) view.findViewById(R.id.accesscodetext);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //show the access code on the screen
        accesscode = dbhelper.getGroupAccessCode(groupId);
        accesscodetext.setText(accesscode);
    }

    public void setSession(SessionManager session) {
        this.session = session;
    }

    public void setDbhelper(DbHelper dbhelper) {
        this.dbhelper = dbhelper;
    }

    public void setIDs(int groupId, int userId) {
        this.groupId = groupId;
        this.userId = userId;
    }
}
