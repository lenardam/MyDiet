package com.lenardam.mydiet;

import static com.lenardam.mydiet.utils.CalendarUtils.daysInWeekArray;
import static com.lenardam.mydiet.utils.CalendarUtils.monthYearFromDate;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.lenardam.mydiet.adapters.DietPlanDateAdapter;
import com.lenardam.mydiet.adapters.ShoppingListAdapter;
import com.lenardam.mydiet.adapters.ShoppingPeriodAdapter;
import com.lenardam.mydiet.model.DietPlan;
import com.lenardam.mydiet.model.Meal;
import com.lenardam.mydiet.model.RecipeIngredient;
import com.lenardam.mydiet.model.ShoppingItem;
import com.lenardam.mydiet.model.ShoppingList;
import com.lenardam.mydiet.utils.CalendarUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShoppingListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingListFragment extends Fragment implements ShoppingPeriodAdapter.OnDateClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String SHOPPING_LIST_DIET_PLAN_TAG = "SHOPPING_LIST_DIET_PLAN_TAG";
    public static final String SHOPPING_LIST_TAG = "SHOPPING_LIST_TAG";
    public static final String SHOPPING_LIST_SELECTED_TAG = "SHOPPING_LIST_SELECTED_TAG";

    // TODO: Rename and change types of parameters

    private ShoppingList shopping_list;
    private ArrayList<ShoppingItem> ingredients_to_buy;
    public static LocalDate shopping_start_date;
    public static LocalDate shopping_end_date;
    private LocalDate selectedDate;
    private ArrayList<LocalDate> selected_week;

    private TextView shoppingMonthYearTextView;
    private ImageButton shoppingButtonPreviousWeek;
    private ImageButton shoppingButtonNextWeek;
    private TextView shoppingPeriodTextView;
    private Button generateShoppingListButton;
    private ShoppingListAdapter ingredients_to_buy_adapter;
    private RecyclerView shoppingPeriodRecyclerView;
    private ShoppingPeriodAdapter shopping_period_adapter;

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
    public static ShoppingListFragment newInstance() {
        ShoppingListFragment fragment = new ShoppingListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        initShoppingPeriodRecycleView(view);
        initRecycleView(view);
    }

    private void initViews(View view) {
        shopping_list = MainActivity.myDiet.getShopping_list();

        if (selectedDate == null) {
            selectedDate = LocalDate.now();
        }

        shoppingMonthYearTextView = (TextView) view.findViewById(R.id.shoppingMonthYearTextView);
        shoppingButtonPreviousWeek = (ImageButton) view.findViewById(R.id.shoppingButtonPreviousWeek);
        shoppingButtonNextWeek = (ImageButton) view.findViewById(R.id.shoppingButtonNextWeek);
        shoppingPeriodTextView = (TextView) view.findViewById(R.id.shoppingPeriodTextView);
        generateShoppingListButton = (Button) view.findViewById(R.id.generateShoppingListButton);

        shoppingButtonNextWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPreviousWeek(view);
            }
        });

        shoppingButtonPreviousWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNextWeek(view);
            }
        });

        generateShoppingListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getShoppingList(shopping_start_date, shopping_end_date);
            }
        });


        if (shopping_list != null) {
            shopping_start_date = shopping_list.getDate_start();
            shopping_end_date = shopping_list.getDate_end();
            ingredients_to_buy = shopping_list.getIngredient_to_buy();
        }

        if (ingredients_to_buy == null) {
            ingredients_to_buy = new ArrayList<ShoppingItem>();
        }
        setShoppingPeriodTextView();

    }

    private void setShoppingPeriodTextView() {
        if (shopping_start_date == null && shopping_end_date == null){
            shoppingPeriodTextView.setText(" ");
        } else if (shopping_start_date != null && shopping_end_date == null) {
            shoppingPeriodTextView.setText(CalendarUtils.formatDate(shopping_start_date));
        }
        else{
            shoppingPeriodTextView.setText(CalendarUtils.formatDate(shopping_start_date) + " - " + CalendarUtils.formatDate(shopping_end_date));
        }

    }

    private void initShoppingPeriodRecycleView(View view) {
        shoppingMonthYearTextView.setText(monthYearFromDate(selectedDate));
        selected_week = daysInWeekArray(selectedDate);

        shoppingPeriodRecyclerView = (RecyclerView) view.findViewById(R.id.shoppingPeriodRecyclerView);
        shopping_period_adapter = new ShoppingPeriodAdapter(selected_week, this);
        shoppingPeriodRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));
        shoppingPeriodRecyclerView.setAdapter(shopping_period_adapter);

    }

    private void setNextWeek(View view) {
        selectedDate = selectedDate.minusWeeks(1);
        selected_week.clear();
        selected_week.addAll(daysInWeekArray(selectedDate));
        shopping_period_adapter.notifyDataSetChanged();
    }

    private void setPreviousWeek(View view) {
        selectedDate = selectedDate.plusWeeks(1);
        selected_week.clear();
        selected_week.addAll(daysInWeekArray(selectedDate));
        shopping_period_adapter.notifyDataSetChanged();
    }

    private void saveShoppingList() {
        MainActivity.myDiet.setShopping_list(shopping_list);
    }

    private void initRecycleView(View view) {
        ingredients_to_buy_adapter = new ShoppingListAdapter(ingredients_to_buy, new ShoppingListAdapter.OnShoppingListCheckboxClickListener() {
            @Override
            public void onCheckboxClicked(int position, boolean isChecked) {
                // Obsługuje zmianę stanu checkboxa
                onShoppingListItemToBuyClick(position, isChecked);
                saveShoppingList();
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
        ingredients_to_buy.get(position).setIs_bought(isChecked);
    }

    public void getShoppingList(LocalDate date_start, LocalDate date_end) {
        shopping_list = new ShoppingList(date_start, date_end);
        for (int i=0; i<MainActivity.myDiet.getDiet_plan().size(); i++){
            LocalDate date = MainActivity.myDiet.getDiet_plan().get(i).getDiet_plan_date();
            if ((date.isAfter(date_start) || date.equals(date_start)) && (date.isBefore(date_end) || date.equals(date_end))){
                ArrayList<Meal> meals = MainActivity.myDiet.getDiet_plan().get(i).getMeals();
                    for (int j=0; j<meals.size(); j++){
                        if (meals.get(j).getRecipe() != null) {
                            ArrayList<RecipeIngredient> ingredients = meals.get(j).getRecipe().getIngredients();
                            for (int k = 0; k < ingredients.size(); k++) {
                                shopping_list.addIngredientToBuy(new ShoppingItem(ingredients.get(k), false));
                            }
                        }
                    }
            }
        }
        ingredients_to_buy.clear();
        ingredients_to_buy.addAll(shopping_list.getIngredient_to_buy());
        updateRecycleView();

    }

    @Override
    public void onDateClick(int position) {
        LocalDate clickedDate = selected_week.get(position);

        //jeżeli nie ma ustawionej daty shopping_start_date to ją ustaw
        if(shopping_start_date == null){
            shopping_start_date = clickedDate;
        }
        //w przeciwnym wypadku, sprawdź czy nowa data jest po dacie shopping_start_date
        //jeżeli jest po dacie shopping_start_date to ustaw shopping_end_date
        //jeżeli jest przed, to zamień miejscami daty
        else if (shopping_end_date == null) {
            if (clickedDate.isAfter(shopping_start_date)) {
                shopping_end_date = clickedDate;
            }
            else {
                shopping_end_date = shopping_start_date;
                shopping_start_date = clickedDate;
            }
        }
        //jeżeli obie daty są ustawione, to znaczy, że zaczęto generować nową listę zakupów
        else {
            shopping_start_date = clickedDate;
            shopping_end_date = null;
        }

        //zaktualizuj widok wyboru dat
        shopping_period_adapter.notifyDataSetChanged();
        setShoppingPeriodTextView();
    }
}