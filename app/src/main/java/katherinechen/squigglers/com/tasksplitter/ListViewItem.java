package katherinechen.squigglers.com.tasksplitter;

/**
 * Created by lindsaycampbell on 10/24/14.
 */
public class ListViewItem {
    private String itemTitle;

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public ListViewItem(String title) {
        this.itemTitle = title;
    }
}
