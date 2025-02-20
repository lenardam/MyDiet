package com.lenardam.mydiet;

import static com.lenardam.mydiet.utils.CalendarUtils.daysInWeekArray;
import static com.lenardam.mydiet.utils.CalendarUtils.monthYearFromDate;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lenardam.mydiet.adapters.ShoppingListAdapter;
import com.lenardam.mydiet.adapters.ShoppingPeriodAdapter;
import com.lenardam.mydiet.model.Meal;
import com.lenardam.mydiet.model.RecipeIngredient;
import com.lenardam.mydiet.model.ShoppingItem;
import com.lenardam.mydiet.model.ShoppingList;
import com.lenardam.mydiet.utils.CalendarUtils;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShoppingListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingListFragment extends Fragment implements ShoppingPeriodAdapter.OnDateClickListener, ShoppingListAdapter.OnShoppingListItemClickListener {

    public static final String SHOPPING_LIST_DIET_PLAN_TAG = "SHOPPING_LIST_DIET_PLAN_TAG";
    public static final String SHOPPING_LIST_TAG = "SHOPPING_LIST_TAG";
    public static final String SHOPPING_LIST_SELECTED_TAG = "SHOPPING_LIST_SELECTED_TAG";
    private ShoppingList shoppingList;
    private ArrayList<ShoppingItem> ingredientsToBuy;
    public static LocalDate shoppingStartDate;
    public static LocalDate shoppingEndDate;
    private LocalDate selectedDate;
    private ArrayList<LocalDate> selectedWeek;
    private String[] units;

    private TextView shoppingMonthYearTextView;
    private ImageButton shoppingButtonPreviousWeek;
    private ImageButton shoppingButtonNextWeek;
    private TextView shoppingPeriodTextView;
    private Button generateShoppingListButton;
    private ShoppingListAdapter shoppingListAdapter;
    private RecyclerView shoppingPeriodRecyclerView;
    private ShoppingPeriodAdapter shoppingPeriodAdapter;
    private FloatingActionButton addItemFAB;
    private RecyclerView rv_shoppingListToBuy;

    public ShoppingListFragment() {
        // Required empty public constructor
    }

    public static ShoppingListFragment newInstance() {
        ShoppingListFragment fragment = new ShoppingListFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).setBottomNavigationItem(R.id.menu_bottom_item_shopping_list_fragment);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initShoppingPeriodRecycleView(view);
        initRecycleView(view);
    }

    private void initViews(View view) {
        shoppingList = MainActivity.myDiet.getShoppingList();
        units = getResources().getStringArray(R.array.shopping_units);

        if (shoppingList == null || shoppingList.getDateStart() == null) {
            selectedDate = LocalDate.now();
        }
        else {
            selectedDate = shoppingList.getDateStart();
        }

        shoppingMonthYearTextView = (TextView) view.findViewById(R.id.fr_shopping_list_tv_month_year);
        shoppingButtonPreviousWeek = (ImageButton) view.findViewById(R.id.fr_shopping_list_btn_previous_week);
        shoppingButtonNextWeek = (ImageButton) view.findViewById(R.id.fr_shopping_list_btn_next_week);
        shoppingPeriodTextView = (TextView) view.findViewById(R.id.fr_shopping_list_tv_shopping_period);
        generateShoppingListButton = (Button) view.findViewById(R.id.fr_shopping_list_btn_generate_shopping_list);
        addItemFAB = (FloatingActionButton) view.findViewById(R.id.fr_shopping_list_fab_shopping_list);

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
                if (shoppingStartDate != null && shoppingEndDate != null) {
//                    shopping_list = new ShoppingList(shopping_start_date, shopping_end_date);
                    getShoppingList(shoppingStartDate, shoppingEndDate);
                }
            }
        });

        addItemFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(shoppingList != null) {
                    initNewIngredientDialog();
                    updateRecycleView();
                }
                else {
                    Toast newToast = Toast.makeText(getContext(), R.string.shopping_list_not_exists_error_text, Toast.LENGTH_SHORT);
                    newToast.show();
                }
            }
        });


        if (shoppingList != null) {
            shoppingStartDate = shoppingList.getDateStart();
            shoppingEndDate = shoppingList.getDateEnd();
            ingredientsToBuy = shoppingList.getIngredientToBuy();
        }

        if (ingredientsToBuy == null) {
            ingredientsToBuy = new ArrayList<ShoppingItem>();
        }
        setShoppingPeriodTextView();

    }

    private void setShoppingPeriodTextView() {
        if (shoppingStartDate == null && shoppingEndDate == null){
            shoppingPeriodTextView.setText(" ");
        } else if (shoppingStartDate != null && shoppingEndDate == null) {
            shoppingPeriodTextView.setText(CalendarUtils.formatDate(shoppingStartDate));
        }
        else{
            shoppingPeriodTextView.setText(CalendarUtils.formatDate(shoppingStartDate) + " - " + CalendarUtils.formatDate(shoppingEndDate));
        }

    }

    private void initShoppingPeriodRecycleView(View view) {
        shoppingMonthYearTextView.setText(monthYearFromDate(selectedDate));
        selectedWeek = daysInWeekArray(selectedDate);

        shoppingPeriodRecyclerView = (RecyclerView) view.findViewById(R.id.fr_shopping_list_rv_shopping_period);
        shoppingPeriodAdapter = new ShoppingPeriodAdapter(selectedWeek, this);
        shoppingPeriodRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));
        shoppingPeriodRecyclerView.setAdapter(shoppingPeriodAdapter);

    }

    private void setNextWeek(View view) {
        selectedDate = selectedDate.minusWeeks(1);
        shoppingMonthYearTextView.setText(monthYearFromDate(selectedDate));
        selectedWeek.clear();
        selectedWeek.addAll(daysInWeekArray(selectedDate));
        shoppingPeriodAdapter.notifyDataSetChanged();
    }

    private void setPreviousWeek(View view) {
        selectedDate = selectedDate.plusWeeks(1);
        shoppingMonthYearTextView.setText(monthYearFromDate(selectedDate));
        selectedWeek.clear();
        selectedWeek.addAll(daysInWeekArray(selectedDate));
        shoppingPeriodAdapter.notifyDataSetChanged();
    }

    private void saveShoppingList() {
        MainActivity.myDiet.setShoppingList(shoppingList);
    }

    private void initRecycleView(View view) {
        shoppingListAdapter = new ShoppingListAdapter(ingredientsToBuy, this);

        rv_shoppingListToBuy = view.findViewById(R.id.fr_shopping_list_rv_shopping_list_to_buy);

        rv_shoppingListToBuy.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_shoppingListToBuy.setAdapter(shoppingListAdapter);
    }

    private void updateRecycleView() {
        shoppingListAdapter.notifyDataSetChanged();
    }

    public void onShoppingListItemToBuyClick(int position, boolean isChecked) {
        ingredientsToBuy.get(position).setBought(isChecked);
    }

    public void getShoppingList(LocalDate dateStart, LocalDate dateEnd) {
        ShoppingList newShoppingList = new ShoppingList(dateStart, dateEnd);
        for (int i = 0; i<MainActivity.myDiet.getDietPlan().size(); i++){
            LocalDate date = MainActivity.myDiet.getDietPlan().get(i).getDietPlanDate();
            if ((date.isAfter(dateStart) || date.equals(dateStart)) && (date.isBefore(dateEnd) || date.equals(dateEnd))){
                ArrayList<Meal> meals = MainActivity.myDiet.getDietPlan().get(i).getMeals();
                    for (int j=0; j<meals.size(); j++){
                        if (meals.get(j).getRecipe() != null) {
                            ArrayList<RecipeIngredient> ingredients = meals.get(j).getRecipe().getIngredients();
                            for (int k = 0; k < ingredients.size(); k++) {
                                newShoppingList.addIngredientToBuy(new ShoppingItem(ingredients.get(k), false), meals.get(j).getRecipe().getServingSize() , meals.get(j).getPortionOfRecipe());
                            }
                        }
                    }
            }
        }
        ingredientsToBuy.clear();
        ingredientsToBuy.addAll(newShoppingList.getIngredientToBuy());
        shoppingList.setDateStart(newShoppingList.getDateStart());
        shoppingList.setDateEnd(newShoppingList.getDateEnd());
        updateRecycleView();
    }

    @Override
    public void onDateClick(int position) {
        LocalDate clickedDate = selectedWeek.get(position);

        //jeżeli nie ma ustawionej daty shopping_start_date to ją ustaw
        if(shoppingStartDate == null){
            shoppingStartDate = clickedDate;
        }
        //w przeciwnym wypadku, sprawdź czy nowa data jest po dacie shopping_start_date
        //jeżeli jest po dacie shopping_start_date to ustaw shopping_end_date
        //jeżeli jest przed, to zamień miejscami daty
        else if (shoppingEndDate == null) {
            if (clickedDate.isAfter(shoppingStartDate)) {
                shoppingEndDate = clickedDate;
            }
            else {
                shoppingEndDate = shoppingStartDate;
                shoppingStartDate = clickedDate;
            }
        }
        //jeżeli obie daty są ustawione, to znaczy, że zaczęto generować nową listę zakupów
        else {
            shoppingStartDate = clickedDate;
            shoppingEndDate = null;
        }

        //zaktualizuj widok wyboru dat
        shoppingPeriodAdapter.notifyDataSetChanged();
        setShoppingPeriodTextView();
    }

    private void initNewIngredientDialog() {

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_new_ingredient, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext())
                .setTitle(R.string.dialog_add_shopping_item_title_text)
                .setCancelable(false)
                .setView(dialogView);

        // Inicjalizacja elementów widoku
        EditText ingredientNameEditText = dialogView.findViewById(R.id.dia_new_ingredient_et_ingredient_name);
        EditText ingredientAmountEditText = dialogView.findViewById(R.id.dia_new_ingredient_et_ingredient_amount);
        Spinner ingredientUnitSpinner = dialogView.findViewById(R.id.dia_new_ingredient_spin_ingredient_unit);

        // Utwórzenie adaptera przechowującego jednostki miary
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ingredientUnitSpinner.setAdapter(adapter);

        // Dodanie przycisków do dialogu
        alertDialogBuilder.setNegativeButton(R.string.dialog_negative_button_abort_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialogBuilder.setPositiveButton(R.string.dialog_positive_button_save_text,null);

        // Wyświetlenie dialogu
        AlertDialog materialDialog = alertDialogBuilder.create();
        materialDialog.show();
        materialDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isValid = true;
                String newIngredientName =  ingredientNameEditText.getText().toString();
                String newIngredientAmount = ingredientAmountEditText.getText().toString();
                String newIngredientUnit = ingredientUnitSpinner.getSelectedItem().toString();

                if(ingredientNameEditText.getText().toString().isEmpty()){
                    ingredientNameEditText.setError(getString(R.string.dialog_add_shopping_item_error_name_text));
                    isValid = false;
                }
                if(ingredientAmountEditText.getText().toString().isEmpty()){
                    ingredientAmountEditText.setError(getString(R.string.dialog_add_shopping_item_error_amount_text));
                    isValid = false;
                }

                if (isValid) {
                    shoppingList.addIngredientToBuy(new ShoppingItem(new RecipeIngredient(newIngredientName, Double.parseDouble(newIngredientAmount), newIngredientUnit), false), 1, 1);
                    materialDialog.dismiss();
                }
            }
        });

    }


    @Override
    public void onShoppingItemCheckboxClicked(int position, boolean isChecked) {
        // Obsługuje zmianę stanu checkboxa
        onShoppingListItemToBuyClick(position, isChecked);
        saveShoppingList();
    }

    @Override
    public void onShoppingItemClick(int position) {
        boolean isChecked = ingredientsToBuy.get(position).isBought();
        onShoppingItemCheckboxClicked(position, !isChecked);
        shoppingListAdapter.notifyItemChanged(position);
    }

    @Override
    public void onShoppingItemLongClick(int position, View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        popup.getMenuInflater().inflate(R.menu.menu_shopping_list_item, popup.getMenu());
        popup.setGravity(Gravity.END);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_shopping_list_item_check_all) {
                    for (int i = 0; i < ingredientsToBuy.size(); i++) {
                        ingredientsToBuy.get(i).setBought(true);
                    }
                    updateRecycleView();
                }
                if (item.getItemId() == R.id.menu_shopping_list_item_delete_checked) {
                    if(shoppingList != null){
                        shoppingList.deleteBoughtItems();
                        updateRecycleView();
                    }
                }
                return true;
            }
        });
        popup.show();//showing popup menu
    }
}