package katherinechen.squigglers.com.tasksplitter;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class CreateTaskFragment extends Fragment {
    private SessionManager session;
    private DbHelper dbhelper;
    private Button signInButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_task, container, false);

        return view;
    }

    public void setSession(SessionManager session)
    {
        this.session = session;
    }

    public void setDbhelper(DbHelper dbhelper) {
        this.dbhelper = dbhelper;
    }
}
