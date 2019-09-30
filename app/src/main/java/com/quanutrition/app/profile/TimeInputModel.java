package com.quanutrition.app.profile;

import java.util.ArrayList;

public class TimeInputModel {
    String id,day;
    ArrayList<TimeInputChildModel> list;

    public TimeInputModel(String id, String day, ArrayList<TimeInputChildModel> list) {
        this.id = id;
        this.day = day;
        this.list = list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public ArrayList<TimeInputChildModel> getList() {
        return list;
    }

    public void setList(ArrayList<TimeInputChildModel> list) {
        this.list = list;
    }

    public void addListItem(TimeInputChildModel model){
        int flag=0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getFrom().equalsIgnoreCase(model.getFrom()) && list.get(i).getTo().equalsIgnoreCase(model.getTo())) {
                if (model.getActivity() == null) {
                    flag++;
                }else{
                    if(model.getActivity().equalsIgnoreCase(list.get(i).getActivity())){
                        flag++;
                    }
                }
            }

        }
        if(flag==0)
        this.list.add(model);
    }

    public void removeItem(int pos){
        this.list.remove(pos);
    }
}
