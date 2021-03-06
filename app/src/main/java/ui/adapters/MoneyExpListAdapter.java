package ui.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.cheesehole.expencemanager.R;

import java.util.ArrayList;

import ui.activities.MainActivity;

/*
    Created by Жамбыл on 17.06.2015.
  */
public class MoneyExpListAdapter extends BaseExpandableListAdapter {

    // Views
    Context mContext;
    ExpandableListView listView;
    public TextView moneyText;
    Button [] mathButtons;
    Button delete;
    EditText mAddComment;
    View groupView;
    // Values
    String currentText;
    public static boolean isListExpanded = false;

    /*
        Constructor
     */
    public MoneyExpListAdapter(Context context, EditText addComment) {
        mContext = context;
        mAddComment = addComment;
    }

    /*
        GroupView
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View groupView,
                             ViewGroup parent) {
        listView = (ExpandableListView) parent;

        groupView = this.groupView;

        isListExpanded = isExpanded;

        delete = (Button)groupView.findViewById(R.id.money_delete);
        delete.setFocusable(false);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentText != null && currentText.length() > 0) {
                    currentText = currentText.substring(0, currentText.length() - 1);
                    refreshMoneyText();
                }
            }
        });
        delete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                currentText = "";
                refreshMoneyText();
                return false;
            }
        });


        return groupView;
    }

    /*
        ChildView
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View childView, ViewGroup parent) {
        if (childView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            childView = inflater.inflate(R.layout.money_child_view, null);
        }

        initButtons(childView);

        for(Button button : mathButtons) {
            button.setTypeface(MainActivity.robotoLight);
            button.setTextSize(32);
        }

        return childView;
    }

    public void initMoneyText() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        groupView = inflater.inflate(R.layout.money_group_view, null);
        moneyText = (TextView)groupView.findViewById(R.id.moneyText);

        moneyText.setTextSize(24);
        refreshMoneyText();

        // Setting font
        moneyText.setTypeface(MainActivity.robotoRegular);
        moneyText.getRootView().setBackgroundColor(Color.WHITE);
        moneyText.setTextColor(groupView.getResources().getColor(R.color.Cafes));
    }

    // Close calc
    public void close(){
        listView.collapseGroup(0);
    }

    public void refreshMoneyText(){
        moneyText.setText(currentText);
    }


    private void initButtons(View convertView) {
        //Creating a massive of buttons
        mathButtons = new Button[16];

        // Adding buttons
        mathButtons[0] = (Button)convertView.findViewById(R.id.money_0);
        mathButtons[1] = (Button)convertView.findViewById(R.id.money_1);
        mathButtons[2] = (Button)convertView.findViewById(R.id.money_2);
        mathButtons[3] = (Button)convertView.findViewById(R.id.money_3);
        mathButtons[4] = (Button)convertView.findViewById(R.id.money_4);
        mathButtons[5] = (Button)convertView.findViewById(R.id.money_5);
        mathButtons[6] = (Button)convertView.findViewById(R.id.money_6);
        mathButtons[7] = (Button)convertView.findViewById(R.id.money_7);
        mathButtons[8] = (Button)convertView.findViewById(R.id.money_8);
        mathButtons[9] = (Button)convertView.findViewById(R.id.money_9);
        mathButtons[10] = (Button)convertView.findViewById(R.id.money_plus);
        mathButtons[11] = (Button)convertView.findViewById(R.id.money_minus);
        mathButtons[12] = (Button)convertView.findViewById(R.id.money_multiply);
        mathButtons[13] = (Button)convertView.findViewById(R.id.money_division);
        mathButtons[14] = (Button)convertView.findViewById(R.id.money_dot);
        mathButtons[15] = (Button)convertView.findViewById(R.id.money_equals);

        // Setting OnClickListener
        for(Button mathButton : mathButtons){
            mathButton.setOnClickListener(onMathButtonListener);
        }
    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupPosition;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    View.OnClickListener onMathButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            currentText = (String) moneyText.getText();

            String[] dangValues = new String[6];
            dangValues[0] = "+";
            dangValues[1] = "-";
            dangValues[2] = "*";
            dangValues[3] = "/";
            dangValues[4] = ".";
            dangValues[5] = "=";

            String value = (String) ((Button)v.findViewById(v.getId())).getText();

            // Try to set value of the button
            if(!value.equals("=") && checkValue(currentText, dangValues, value)) {
                currentText += value;
                moneyText.setTextSize(24);
                moneyText.setText(currentText);

            }
        }
    };

    @Override
    public void onGroupExpanded(int groupPosition) {
        InputMethodManager imm = (InputMethodManager)mContext.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mAddComment.getWindowToken(), 0);
        mAddComment.setFocusableInTouchMode(false);
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        mAddComment.setFocusableInTouchMode(true);
        super.onGroupCollapsed(groupPosition);
    }

    private boolean checkValue(String currentText, String[] dangValues, String value){
        boolean isDangValue = false;

        // Checking if the value is dangerous
        for(String notStart : dangValues) {
            if(value.equals(notStart)) {
                isDangValue = true;
                break;
            }
        }

        // Preventing type too much
        if(currentText.length()>15){
            return false;
        }

        // Preventing to add a second value after 0 if it is not dot
        if(currentText.length()==1 & currentText.endsWith("0") & !value.equals(".")){
            return false;
        }

        // Preventing dang value to be first
        if(currentText.length()==0 & isDangValue) {
            return false;
        }

        // Preventing dang value to be after dang value;
        for(String notStart : dangValues){
            if(isDangValue & currentText.endsWith(notStart) & !value.equals("0")){
                return false;
            }
        }

        // Preventing to add a second value after 0 if it is not dot for every expression
//        for(String dangValue : dangValues){
//            if(currentText.contains(dangValue)) {
//                for (String expression : currentText.split("\\"+ dangValue)) {
//                    if (expression.length()!=0 & expression.endsWith("0") &  !value.equals("."))
//                        return false;
//                }
//            }
//        }

        return true;
    }
}
