package example.administrator.a2019_openhack_team22;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainAdapter extends BaseAdapter {
    ArrayList<ListMainItem> listMainItems;

    public MainAdapter(){
        listMainItems = new ArrayList<>();
    }
    @Override
    public int getCount() {
        return listMainItems.size();
    }

    @Override
    public ListMainItem getItem(int i) {
        return listMainItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int pos = i;
        final Context context = viewGroup.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_main_list, viewGroup, false);
        }


        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득;
        TextView txtGoalName = view.findViewById(R.id.txt_goal_name) ;
        TextView txtPercent = view.findViewById(R.id.txt_percent) ;
        ProgressBar mainProgress = view.findViewById(R.id.main_progress) ;

        txtGoalName.setText(listMainItems.get(i).getGoalName());
        txtPercent.setText(listMainItems.get(i).getPercent() + "%");
        mainProgress.setProgress(listMainItems.get(i).getPercent());

        return view;
    }

    public void addItem(ListMainItem item){
        listMainItems.add(item);
    }

}
