package com.quanutrition.app.selectiondialogs;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quanutrition.app.R;
import com.quanutrition.app.databinding.DialogTimepickerBinding;
import com.shawnlin.numberpicker.NumberPicker;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DialogUtils {

    static AlertDialog alertDialog1;


    public interface OnSingleItemSelectedListener{
        void onItemSelected(int position, SingleSelectionModel item);
    }

    public interface OnMultipleItemsSelected{
        void onMultipleItemsSelected(ArrayList<MultipleSelectionModel> items);
    }

    public interface OnNumberPicked{
        void onNumberPicked(String selected, int position, String unit);
    }

    public interface OnCustomItemPicked{
        void onNumberPicked(String selected);
    }


    public static void getSingleSelectionDialog(Context context, ArrayList<SingleSelectionModel> list, final OnSingleItemSelectedListener onSingleItemSelectedListener){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        LayoutInflater linf = LayoutInflater.from(context);
        final View inflator = linf.inflate(R.layout.selection_dialog, null);
        alertDialog.setView(inflator);
        alertDialog.setCancelable(true);
        RecyclerView recyclerView = inflator.findViewById(R.id.selection_re);
        final ArrayList<SingleSelectionModel> modelList = list;
        SingleSelectionAdapter adapter = new SingleSelectionAdapter(modelList, context, new SingleSelectionAdapter.OnClickListener() {
            @Override
            public void onItemSelected(View view, int position) {
                onSingleItemSelectedListener.onItemSelected(position,modelList.get(position));
                alertDialog1.dismiss();
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        alertDialog1 = null;
        alertDialog1 = alertDialog.show();
        alertDialog1.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dialog_background_drawable));
    }

    public static ArrayList<SingleSelectionModel> getSingleArrayListWithResource(Context context, int id){
        String[] array = context.getResources().getStringArray(id);
        ArrayList<SingleSelectionModel> list = new ArrayList<>();
        for(int i=0;i<array.length;i++){
            list.add(new SingleSelectionModel(i+"",array[i]));
        }
        return list;
    }

    public static ArrayList<MultipleSelectionModel> getMultipleArrayListWithResource(Context context, int id){
        String[] array = context.getResources().getStringArray(id);
        ArrayList<MultipleSelectionModel> list = new ArrayList<>();
        for(int i=0;i<array.length;i++){
            list.add(new MultipleSelectionModel(i+"",array[i],false));
        }
        return list;
    }

    public static void getMultipleSelectionDialog(Context context, final ArrayList<MultipleSelectionModel> list, final OnMultipleItemsSelected onMultipleItemsSelected){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        LayoutInflater linf = LayoutInflater.from(context);
        final View inflator = linf.inflate(R.layout.multiple_selection_dialog, null);
        alertDialog.setView(inflator);
        alertDialog.setCancelable(true);
        TextView titleTV = inflator.findViewById(R.id.title);
        titleTV.setVisibility(View.GONE);
        RecyclerView recyclerView = inflator.findViewById(R.id.selection_re);
        final ArrayList<MultipleSelectionModel> modelList = list;
        MultipleSelectionAdapter adapter = new MultipleSelectionAdapter(modelList, context);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        (inflator.findViewById(R.id.done)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<MultipleSelectionModel> selected = new ArrayList<>();
                for(int j=0;j<modelList.size();j++){
                    if(modelList.get(j).isSelected()){
                        selected.add(modelList.get(j));
                    }
                }
                onMultipleItemsSelected.onMultipleItemsSelected(modelList);
                alertDialog1.dismiss();
            }
        });

        alertDialog1 = null;
        alertDialog1 = alertDialog.show();
        alertDialog1.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dialog_background_drawable));
    }

    public static void getMultipleSelectionDialogWithTitle(Context context,String title,final ArrayList<MultipleSelectionModel> list, final OnMultipleItemsSelected onMultipleItemsSelected){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        LayoutInflater linf = LayoutInflater.from(context);
        final View inflator = linf.inflate(R.layout.multiple_selection_dialog, null);
        alertDialog.setView(inflator);
        alertDialog.setCancelable(true);

        TextView titleTV = inflator.findViewById(R.id.title);
        titleTV.setVisibility(View.VISIBLE);
        titleTV.setText(title);

        RecyclerView recyclerView = inflator.findViewById(R.id.selection_re);
        final ArrayList<MultipleSelectionModel> modelList = list;
        MultipleSelectionAdapter adapter = new MultipleSelectionAdapter(modelList, context);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        (inflator.findViewById(R.id.done)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<MultipleSelectionModel> selected = new ArrayList<>();
                for(int j=0;j<modelList.size();j++){
                    if(modelList.get(j).isSelected()){
                        selected.add(modelList.get(j));
                    }
                }
                onMultipleItemsSelected.onMultipleItemsSelected(modelList);
                alertDialog1.dismiss();
            }
        });

        alertDialog1 = null;
        alertDialog1 = alertDialog.show();
        alertDialog1.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dialog_background_drawable));
    }


    public static void getSingleSearchDialog(final Context context, ArrayList<SingleSelectionModel> list, final OnSingleItemSelectedListener onSingleItemSelectedListener){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        LayoutInflater linf = LayoutInflater.from(context);
        final View inflator = linf.inflate(R.layout.single_search_dialog, null);
        alertDialog.setView(inflator);
        alertDialog.setCancelable(true);
        final RecyclerView recyclerView = inflator.findViewById(R.id.selection_re);
        EditText search = inflator.findViewById(R.id.search);
        final ArrayList<SingleSelectionModel> filteredList = new ArrayList<>();
        final ArrayList<SingleSelectionModel> modelList = list;
        final SingleSelectionAdapter adapter = new SingleSelectionAdapter(modelList, context, new SingleSelectionAdapter.OnClickListener() {
            @Override
            public void onItemSelected(View view, int position) {
                onSingleItemSelectedListener.onItemSelected(position,modelList.get(position));
                alertDialog1.dismiss();
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!(TextUtils.isEmpty(charSequence))) {
                    filteredList.clear();
                    SingleSelectionAdapter filteredAdapter = new SingleSelectionAdapter(filteredList, context, new SingleSelectionAdapter.OnClickListener() {
                        @Override
                        public void onItemSelected(View view, int position) {
                            onSingleItemSelectedListener.onItemSelected(position,filteredList.get(position));
                            alertDialog1.dismiss();
                        }
                    });
                    for(SingleSelectionModel model:modelList){

                        String charString = charSequence + "";
                        if (model.getLabel().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(model);
                        }
                    }
                    recyclerView.setAdapter(filteredAdapter);
                    filteredAdapter.notifyDataSetChanged();
                }else {
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        alertDialog1 = null;
        alertDialog1 = alertDialog.show();
        alertDialog1.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dialog_background_drawable));
    }


    public static void getMultipleSearchDialog(final Context context, ArrayList<MultipleSelectionModel> list, final OnMultipleItemsSelected onMultipleItemsSelected){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        LayoutInflater linf = LayoutInflater.from(context);
        final View inflator = linf.inflate(R.layout.multiple_search_dialog, null);
        alertDialog.setView(inflator);
        alertDialog.setCancelable(true);
        final RecyclerView recyclerView = inflator.findViewById(R.id.selection_re);
        EditText search = inflator.findViewById(R.id.search);
        TextView done = inflator.findViewById(R.id.done);
        final ArrayList<MultipleSelectionModel> filteredList = new ArrayList<>();
        final ArrayList<MultipleSelectionModel> modelList = list;
        final MultipleSelectionAdapter adapter = new MultipleSelectionAdapter(modelList, context);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMultipleItemsSelected.onMultipleItemsSelected(modelList);
                alertDialog1.dismiss();
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!(TextUtils.isEmpty(charSequence))) {
                    filteredList.clear();
                    MultipleSelectionSearchAdapter filteredAdapter = new MultipleSelectionSearchAdapter(filteredList, context,modelList);
                    for(MultipleSelectionModel model:modelList){

                        String charString = charSequence + "";
                        if (model.getLabel().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(model);
                        }
                    }
                    recyclerView.setAdapter(filteredAdapter);
                    filteredAdapter.notifyDataSetChanged();
                }else {
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        alertDialog1 = null;
        alertDialog1 = alertDialog.show();
        alertDialog1.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dialog_background_drawable));
    }




    private static int value = -1;
    private static String unitSelected;
    public static void getSimpleNumberPicker(final Context context, String title, final String unit, int last_position, final OnNumberPicked onNumberPicked){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        LayoutInflater linf = LayoutInflater.from(context);
        final View inflator = linf.inflate(R.layout.simple_number_dialog, null);
        alertDialog.setView(inflator);
        alertDialog.setCancelable(true);
        value = last_position;
        final String[] data = new String[250];
        final String[] dataLbs = new String[550];
        for(int i=1;i<=250;i++){
            data[i-1]=i+"";
        }
        for(int i=1;i<=550;i++){
            dataLbs[i-1]=i+"";
        }

        final NumberPicker numberPicker = inflator.findViewById(R.id.number_picker);
        final NumberPicker numberPickerUnit = inflator.findViewById(R.id.number_picker_unit);
        TextView done = inflator.findViewById(R.id.done);
        TextView titleTV = inflator.findViewById(R.id.title);
        titleTV.setText(title);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(250);
        numberPicker.setDisplayedValues(data);
        numberPicker.setValue(last_position);
        numberPicker.setFadingEdgeEnabled(true);
        numberPicker.setScrollerEnabled(true);
        numberPicker.setWrapSelectorWheel(true);


        final String[] units = {"Kgs","Lbs"};
        numberPickerUnit.setMinValue(1);
        numberPickerUnit.setMaxValue(2);
        numberPickerUnit.setDisplayedValues(units);
        unitSelected=unit;
        if(unit.equalsIgnoreCase("kgs")){
            numberPickerUnit.setValue(1);
            numberPicker.setMinValue(1);
            numberPicker.setDisplayedValues(data);
            numberPicker.setMaxValue(data.length);
            numberPicker.setValue(value-1);
        }else{
            numberPickerUnit.setValue(2);
            numberPicker.setMinValue(1);
            numberPicker.setDisplayedValues(dataLbs);
            numberPicker.setMaxValue(dataLbs.length);
            numberPicker.setValue(value-1);
        }
        numberPickerUnit.setFadingEdgeEnabled(true);
        numberPickerUnit.setScrollerEnabled(true);
        numberPickerUnit.setWrapSelectorWheel(true);


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(unitSelected.equalsIgnoreCase("kgs"))
                onNumberPicked.onNumberPicked(data[value-1],value-1,unitSelected);
                else
                    onNumberPicked.onNumberPicked(dataLbs[value-1],value-1,unitSelected);
                alertDialog1.dismiss();

               /* String[] array = context.getResources().getStringArray(R.array.height);
                numberPicker.setMinValue(1);
                numberPicker.setMaxValue(array.length);
                numberPicker.setDisplayedValues(array);
                numberPicker.setValue(10);*/


            }
        });
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                Log.d("Number picker", String.format(Locale.US, "oldVal: %d, newVal: %d", oldVal, newVal));
                value = newVal;
            }
        });

        numberPickerUnit.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(units[newVal-1].equalsIgnoreCase("kgs")){
                    unitSelected ="Kgs";
                    numberPicker.setMinValue(1);
                    numberPicker.setMaxValue(data.length);
                    numberPicker.setDisplayedValues(data);
                    numberPicker.setValue(60);
                }else{
                    unitSelected ="Lbs";
                    numberPicker.setMinValue(1);
                    numberPicker.setDisplayedValues(dataLbs);
                    numberPicker.setMaxValue(dataLbs.length);
                    numberPicker.setValue(value-1);
                }
            }
        });

        alertDialog1 = null;
        alertDialog1 = alertDialog.show();
        alertDialog1.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dialog_background_drawable));
    }


    public static void getSimpleNumberPickerHeight(final Context context, String title, final String unit, int last_position, final OnNumberPicked onNumberPicked){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        LayoutInflater linf = LayoutInflater.from(context);
        final View inflator = linf.inflate(R.layout.simple_number_dialog, null);
        alertDialog.setView(inflator);
        alertDialog.setCancelable(true);
        value = last_position;
        final String[] data = new String[250];

         final String[] array = context.getResources().getStringArray(R.array.height);


        for(int i=1;i<=250;i++){
            data[i-1]=i+"";
        }


        final NumberPicker numberPicker = inflator.findViewById(R.id.number_picker);
        final NumberPicker numberPickerUnit = inflator.findViewById(R.id.number_picker_unit);
        TextView done = inflator.findViewById(R.id.done);
        TextView titleTV = inflator.findViewById(R.id.title);
        titleTV.setText(title);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(250);
        numberPicker.setDisplayedValues(data);
        numberPicker.setValue(last_position);
        numberPicker.setFadingEdgeEnabled(true);
        numberPicker.setScrollerEnabled(true);
        numberPicker.setWrapSelectorWheel(true);


        final String[] units = {"cms","ft. in."};
        numberPickerUnit.setMinValue(1);
        numberPickerUnit.setMaxValue(2);
        numberPickerUnit.setDisplayedValues(units);
        unitSelected=unit;
        if(unit.equalsIgnoreCase("cms")){
            numberPickerUnit.setValue(1);
            numberPicker.setMinValue(1);
            numberPicker.setDisplayedValues(data);
            numberPicker.setMaxValue(data.length);
            numberPicker.setValue(value-1);
        }else{
            numberPickerUnit.setValue(2);
            numberPicker.setMinValue(1);
            numberPicker.setDisplayedValues(array);
            numberPicker.setMaxValue(array.length);
            numberPicker.setValue(40);
        }
        numberPickerUnit.setFadingEdgeEnabled(true);
        numberPickerUnit.setScrollerEnabled(true);
        numberPickerUnit.setWrapSelectorWheel(true);


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(unitSelected.equalsIgnoreCase("cms"))
                onNumberPicked.onNumberPicked(data[value-1],value-1,unitSelected);
                else
                    onNumberPicked.onNumberPicked(array[value-1],value-1,unitSelected);
                alertDialog1.dismiss();




            }
        });
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                Log.d("Number picker", String.format(Locale.US, "oldVal: %d, newVal: %d", oldVal, newVal));
                value = newVal;
            }
        });

        numberPickerUnit.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(units[newVal-1].equalsIgnoreCase("cms")){
                    unitSelected ="cms";
                    numberPicker.setMinValue(1);
                    numberPicker.setDisplayedValues(data);
                    numberPicker.setMaxValue(data.length);
                    value=100;
                    numberPicker.setValue(100);
                }else{
                    unitSelected ="ft. in.";
                    numberPicker.setMinValue(1);
                    value=40;
                    numberPicker.setValue(40);
                     numberPicker.setDisplayedValues(array);

                }
            }
        });

        alertDialog1 = null;
        alertDialog1 = alertDialog.show();
        alertDialog1.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dialog_background_drawable));
    }

    static String selected;
    public static void getCustomPicker(final Context context, String title, final String[] data, final OnCustomItemPicked onNumberPicked) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        LayoutInflater linf = LayoutInflater.from(context);
        final View inflator = linf.inflate(R.layout.custom_number_dialog, null);
        alertDialog.setView(inflator);
        alertDialog.setCancelable(true);
        final NumberPicker numberPicker = inflator.findViewById(R.id.number_picker);
        TextView done = inflator.findViewById(R.id.done);
        TextView titleTV = inflator.findViewById(R.id.title);
        titleTV.setText(title);
        selected=data[0];
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(data.length);
        numberPicker.setDisplayedValues(data);
        numberPicker.setValue(1);
        numberPicker.setFadingEdgeEnabled(true);
        numberPicker.setScrollerEnabled(true);
        numberPicker.setWrapSelectorWheel(true);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNumberPicked.onNumberPicked(selected);
                alertDialog1.dismiss();
            }
        });
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selected = data[newVal-1];
            }
        });
        alertDialog1 = null;
        alertDialog1 = alertDialog.show();
        alertDialog1.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dialog_background_drawable));
    }

    public static void getTimePicker(final Context context,boolean isCancelable,final OnCustomItemPicked onCustomItemPicked){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        LayoutInflater linf = LayoutInflater.from(context);
        DialogTimepickerBinding binding = DialogTimepickerBinding.inflate(linf);
        final View inflator = binding.getRoot();
        alertDialog.setView(inflator);
        alertDialog.setCancelable(isCancelable);

        final android.widget.NumberPicker hour = binding.hour;
        final String[] hourValues = new String[]{"01","02","03","04","05","06","07","08","09","10","11","12"};
        hour.setMinValue(0);
        hour.setMaxValue(11);
        hour.setDisplayedValues(hourValues);
//        hour.setWrapSelectorWheel(true);

        final android.widget.NumberPicker min = binding.min;
        final String[] minValues = new String[]{"00","15","30","45"};
        min.setMaxValue(3);
        min.setMinValue(0);
        min.setDisplayedValues(minValues);
//        min.setWrapSelectorWheel(true);

        final android.widget.NumberPicker ampm = binding.ampm;
        final String[] ampmValues = {"AM","PM"};
        ampm.setMaxValue(1);
        ampm.setMinValue(0);
        ampm.setDisplayedValues(ampmValues);

        inflator.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = hourValues[hour.getValue()]+":"+minValues[min.getValue()]+" "+ampmValues[ampm.getValue()];
                onCustomItemPicked.onNumberPicked(time);
                alertDialog1.dismiss();
            }
        });

        alertDialog1 = null;
        alertDialog1 = alertDialog.show();
        alertDialog1.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dialog_background_drawable));
    }

}
