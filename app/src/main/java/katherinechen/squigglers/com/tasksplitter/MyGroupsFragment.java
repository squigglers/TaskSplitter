package katherinechen.squigglers.com.tasksplitter;

import android.app.ListFragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p />
 * interface.
 */

/*public class MyGroupsFragment extends Fragment implements AbsListView.OnItemClickListener {
    private SessionManager session;
    private DbHelper dbhelper;
    OnFragmentInteractionListener mCallback;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String GROUPNAME = "groupName";
    private static final String ACCESSCODE = "accessCode";

    // TODO: Rename and change types of parameters
    private String groupName;
    private String accessCode;

    private OnFragmentInteractionListener mListener;
*/
/**
 * The fragment's ListView/GridView.
 */
//private AbsListView mListView;

/**
 * The Adapter which will be used to populate the ListView/GridView with
 * Views.
 */
//private ListAdapter mAdapter;

// TODO: Rename and change types of parameters
   /* public static MyGroupsFragment newInstance(String group, String code) {
        MyGroupsFragment fragment = new MyGroupsFragment();
        Bundle args = new Bundle();
        args.putString(GROUPNAME, group);
        args.putString(ACCESSCODE, code);
        fragment.setArguments(args);
        return fragment;
    }
    */

/**
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
   /* public MyGroupsFragment() {
    }

*/

/*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbhelper = new DbHelper(getActivity());


        if (getArguments() != null) {
            groupName = getArguments().getString(GROUPNAME);
            accessCode = getArguments().getString(ACCESSCODE);
        }

        // TODO: Change Adapter to display your content
        mAdapter = new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, DummyContent.ITEMS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(  " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
           mCallback.onFragmentInteraction(DummyContent.ITEMS.get(position).id);

        }
    }

    */

/**
 * The default content for this Fragment has a TextView that is shown when
 * the list is empty. If you would like to change the text, call this method
 * to supply the text it should use.
 */
 /*   public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyText instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }
*/

/**
 * This interface must be implemented by activities that contain this
 * fragment to allow an interaction in this fragment to be communicated
 * to the activity and potentially other fragments contained in that
 * activity.
 * <p/>
 * See the Android Training lesson <a href=
 * "http://developer.android.com/training/basics/fragments/communicating.html"
 * >Communicating with Other Fragments</a> for more information.
 */
  /*  public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }
*/
// @Override
/*   public void onListItemClick(ListView l, View v, int position, String id) {
        mCallback.onFragmentInteraction(id);
    }


    public void setSession(SessionManager session)
    {
        this.session = session;
    }
}
    */

public class MyGroupsFragment extends ListFragment {
    private SessionManager session;
    private DbHelper dbhelper;
    private List<ListViewItem> mItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mItems = new ArrayList<ListViewItem>();
        Resources resources = getResources();

        mItems.add(new ListViewItem("Example 1"));
        mItems.add(new ListViewItem("Example 2"));
        mItems.add(new ListViewItem("Example 3"));

        setListAdapter(new ListViewAdapter(getActivity().getApplicationContext(), mItems));

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //getListView().setDivider(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ListViewItem item = mItems.get(position);

        Toast.makeText(getActivity(), item.getItemTitle(), Toast.LENGTH_LONG).show();
        ;
    }

    public void setSession(SessionManager session) {
        this.session = session;
    }

    public void setDbhelper(DbHelper dbhelper) {
        this.dbhelper = dbhelper;
    }
}


