package com.lenardam.mydiet;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.lenardam.mydiet.adapters.ShoppingListAdapter;
import com.lenardam.mydiet.model.DietPlan;
import com.lenardam.mydiet.model.Meal;
import com.lenardam.mydiet.model.RecipeIngredient;
import com.lenardam.mydiet.model.ShoppingList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShoppingListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String SHOPPING_LIST_DIET_PLAN_TAG = "SHOPPING_LIST_DIET_PLAN_TAG";
    public static final String SHOPPING_LIST_TAG = "SHOPPING_LIST_TAG";
    public static final String SHOPPING_LIST_SELECTED_TAG = "SHOPPING_LIST_SELECTED_TAG";

    // TODO: Rename and change types of parameters
    private ArrayList<DietPlan> diet_plan;
    private ShoppingList shopping_list;
    private EditText date_from_edit_text;
    private EditText date_to_edit_text;
    private ImageButton date_pickler_button;

    private ArrayList<RecipeIngredient> ingredients_to_buy;
    ShoppingListAdapter ingredients_to_buy_adapter;

    public ShoppingListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ShoppingListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShoppingListFragment newInstance(ArrayList<DietPlan> diet_plan, ShoppingList shopping_list) {
        ShoppingListFragment fragment = new ShoppingListFragment();
        Bundle args = new Bundle();
        args.putSerializable(SHOPPING_LIST_DIET_PLAN_TAG, diet_plan);
        args.putSerializable(SHOPPING_LIST_TAG, shopping_list);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            diet_plan = (ArrayList<DietPlan>) getArguments().getSerializable(SHOPPING_LIST_DIET_PLAN_TAG);
            shopping_list = (ShoppingList) getArguments().getSerializable(SHOPPING_LIST_TAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.shopping_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initRecycleView(view);
    }

    private void initViews(View view) {
        date_from_edit_text = (EditText) view.findViewById(R.id.dateFromEditText);
        date_to_edit_text = (EditText) view.findViewById(R.id.dateToEditText);
        date_pickler_button = (ImageButton) view.findViewById(R.id.imageButton);

        if (shopping_list != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            date_from_edit_text.setText(sdf.format(shopping_list.getDate_start()));
            date_to_edit_text.setText(sdf.format(shopping_list.getDate_end()));
            ingredients_to_buy = shopping_list.getIngredient_to_buy();
        }

        if (ingredients_to_buy == null) {
            ingredients_to_buy = new ArrayList<RecipeIngredient>();
        }

        date_pickler_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
                builder.setTitleText("Wybierz zakres dat");
//                // Ustawienie daty początkowej i końcowej (opcjonalne)
//                builder.setSelection(Pair.create(MaterialDatePicker.todayInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds() + 1000 * 60 * 60 * 24 * 7));


                // Ustawienie maksymalnej daty (dzisiaj + 10 dni)
                Calendar calendar = Calendar.getInstance();
                long minDate = calendar.getTimeInMillis();
                calendar.add(Calendar.DAY_OF_YEAR, 10);  // Dodanie 10 dni do dzisiejszej daty
                long maxDate = calendar.getTimeInMillis();

                // Opcjonalne ustawienie maksymalnej daty
                CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
                constraintsBuilder.setStart(minDate);//ustawienie daty minimalnej today
                constraintsBuilder.setEnd(maxDate);//ustawienie daty maksymalnej -- docelowo maksymalna data z wybranymi przepisami
                constraintsBuilder.setFirstDayOfWeek(Calendar.MONDAY);
                builder.setCalendarConstraints(constraintsBuilder.build());
                builder.setPositiveButtonText("Wybierz");

                MaterialDatePicker<Pair<Long, Long>> materialDatePicker = builder.build();

                materialDatePicker.addOnPositiveButtonClickListener(selection -> {
                    // Przetwarzanie wybranego zakresu dat
                    // Przekształcenie dat z long na format dd/MM/yyyy
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                    // Konwersja z long na datę
                    Date startDate = new Date(selection.first);
                    Date endDate = new Date(selection.second);

                    // Formatowanie daty na łańcuch
                    String formattedStartDate = sdf.format(startDate);
                    String formattedEndDate = sdf.format(endDate);

                    // Ustawienie wybranych dat w EditText
                    date_from_edit_text.setText(formattedStartDate);
                    date_to_edit_text.setText(formattedEndDate);

                    getShoppingList(startDate, endDate);

                    Bundle result = new Bundle();
                    result.putSerializable(SHOPPING_LIST_SELECTED_TAG, shopping_list);
                    getParentFragmentManager().setFragmentResult(SHOPPING_LIST_SELECTED_TAG, result);
                    requireActivity().getSupportFragmentManager().popBackStack();

                });
                // Pokazywanie DatePicker
                materialDatePicker.show(getChildFragmentManager(), "DATE_PICKER");
            }
        });

    }

    private void initRecycleView(View view) {
        ingredients_to_buy_adapter = new ShoppingListAdapter(ingredients_to_buy, new ShoppingListAdapter.OnShoppingListCheckboxClickListener() {
            @Override
            public void onCheckboxClicked(int position, boolean isChecked) {
                // Obsługuje zmianę stanu checkboxa
                onShoppingListItemToBuyClick(position, isChecked);
            }
        });

        RecyclerView rv_shoppingListToBuy = view.findViewById(R.id.rv_shoppingListToBuy);

        rv_shoppingListToBuy.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_shoppingListToBuy.setAdapter(ingredients_to_buy_adapter);
    }

    private void updateRecycleView() {
        ingredients_to_buy_adapter.notifyDataSetChanged();
    }

    public void onShoppingListItemToBuyClick(int position, boolean isChecked) {
        // Przenoszenie składnika między listami
    }

    public void getShoppingList(Date date_start, Date date_end) {
        shopping_list = new ShoppingList(date_start, date_end);
        for (int i=0; i<diet_plan.size(); i++){
            Date date = diet_plan.get(i).getDiet_plan_date();
            if (date.after(date_start) && date.before(date_end)){
                ArrayList<Meal> meals = diet_plan.get(i).getMeals();
                    for (int j=0; j<meals.size(); j++){
                        if (meals.get(j).getRecipe() != null) {
                            ArrayList<RecipeIngredient> ingredients = meals.get(j).getRecipe().getIngredients();
                            for (int k = 0; k < ingredients.size(); k++) {
                                shopping_list.addIngredientToBuy(ingredients.get(k));
                            }
                        }
                    }
            }
        }
        ingredients_to_buy.clear();
        ingredients_to_buy.addAll(shopping_list.getIngredient_to_buy());
        updateRecycleView();

    }

}