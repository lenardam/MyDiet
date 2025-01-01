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
    private static final String SHOPPING_LIST_DIET_PLAN_TAG = "SHOPPING_LIST_DIET_PLAN_TAG";
    private static final String SHOPPING_LIST_TAG = "SHOPPING_LIST_TAG";

    // TODO: Rename and change types of parameters
    private DietPlan diet_plan;
    private ShoppingList shopping_list;
    private EditText date_from_edit_text;
    private EditText date_to_edit_text;
    private ImageButton date_pickler_button;

    private ArrayList<RecipeIngredient> ingredients_to_buy;
    private ArrayList<RecipeIngredient> ingredients_bought;
    ShoppingListAdapter ingredients_to_buy_adapter;
    ShoppingListAdapter ingredients_bought_adapter;

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
    public static ShoppingListFragment newInstance(DietPlan diet_plan, ShoppingList shopping_list) {
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
            diet_plan = (DietPlan) getArguments().getSerializable(SHOPPING_LIST_DIET_PLAN_TAG);
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

        if (ingredients_to_buy == null) {
            ingredients_to_buy = new ArrayList<RecipeIngredient>();
        }
        if (ingredients_bought == null) {
            ingredients_bought = new ArrayList<RecipeIngredient>();
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
                builder.setCalendarConstraints(constraintsBuilder.build());
                builder.setPositiveButtonText("Wybierz");
                builder.setNegativeButtonText("Anuluj");

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

                    // Wyświetlenie wybranego zakresu dat
                    Toast.makeText(getContext(), "Wybrano zakres: " + formattedStartDate + " do " + formattedEndDate, Toast.LENGTH_SHORT).show();
                });
                // Pokazywanie DatePicker
                materialDatePicker.show(getChildFragmentManager(), "DATE_PICKER");
            }
        });

    }

    private void initRecycleView(View view) {
        ingredients_to_buy_adapter = new ShoppingListAdapter(ingredients_to_buy, false, new ShoppingListAdapter.OnShoppingListCheckboxClickListener() {
            @Override
            public void onCheckboxClicked(int position, boolean isChecked) {
                // Obsługuje zmianę stanu checkboxa
                onShoppingListItemToBuyClick(position, isChecked);
            }
        });
        ingredients_bought_adapter = new ShoppingListAdapter(ingredients_bought, true, new ShoppingListAdapter.OnShoppingListCheckboxClickListener() {
            @Override
            public void onCheckboxClicked(int position, boolean isChecked) {
                // Obsługuje zmianę stanu checkboxa
                onShoppingListItemBoughtClick(position, isChecked);
            }
        });

        RecyclerView rv_shoppingListToBuy = view.findViewById(R.id.rv_shoppingListToBuy);
        RecyclerView rv_shoppingListBought = view.findViewById(R.id.rv_shoppingListBought);

        rv_shoppingListToBuy.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_shoppingListToBuy.setAdapter(ingredients_to_buy_adapter);
        rv_shoppingListBought.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_shoppingListBought.setAdapter(ingredients_bought_adapter);
    }

    private void updateRecycleView() {
        ingredients_to_buy_adapter.notifyDataSetChanged();
        ingredients_bought_adapter.notifyDataSetChanged();
    }

    public void onShoppingListItemToBuyClick(int position, boolean isChecked) {
        // Twoja logika obsługi kliknięcia
        RecipeIngredient ingredient = ingredients_to_buy.get(position);
        ingredients_bought.add(ingredient);
        ingredients_to_buy.remove(position);
        updateRecycleView();
    }

    public void onShoppingListItemBoughtClick(int position, boolean isChecked) {
        // Twoja logika obsługi kliknięcia
        RecipeIngredient ingredient = ingredients_bought.get(position);
        ingredients_to_buy.add(ingredient);
        ingredients_bought.remove(position);
        updateRecycleView();
    }

    public void getShoppingList(Date date_start, Date date_end) {

    }

}