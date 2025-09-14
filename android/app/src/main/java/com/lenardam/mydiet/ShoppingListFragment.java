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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lenardam.mydiet.adapters.ShoppingListAdapter;
import com.lenardam.mydiet.adapters.ShoppingPeriodAdapter;
import com.lenardam.mydiet.adapters.UnitsAdapter;
import com.lenardam.mydiet.database.model.MealFullData;
import com.lenardam.mydiet.database.model.Meals;
import com.lenardam.mydiet.database.model.RecipeFullData;
import com.lenardam.mydiet.database.model.ShoppingList;
import com.lenardam.mydiet.database.model.Units;
import com.lenardam.mydiet.database.viewModel.MealsViewModel;
import com.lenardam.mydiet.database.viewModel.ShoppingListViewModel;
import com.lenardam.mydiet.database.viewModel.UnitsViewModel;
import com.lenardam.mydiet.utils.CalendarUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShoppingListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingListFragment extends Fragment implements ShoppingPeriodAdapter.OnDateClickListener {

    public static final String SHOPPING_LIST_DIET_PLAN_TAG = "SHOPPING_LIST_DIET_PLAN_TAG";
    public static final String SHOPPING_LIST_TAG = "SHOPPING_LIST_TAG";
    public static final String SHOPPING_LIST_SELECTED_TAG = "SHOPPING_LIST_SELECTED_TAG";
    private List<ShoppingList> shoppingList = new ArrayList<>();
    private List<MealFullData> allMeals = new ArrayList<>();
    public static LocalDate shoppingStartDate;
    public static LocalDate shoppingEndDate;
    private LocalDate selectedDate = LocalDate.now();
    private ArrayList<LocalDate> selectedWeek;

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

    private ShoppingListViewModel shoppingListViewModel;
    private MealsViewModel mealsViewModel;
    private UnitsViewModel unitsViewModel;

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

                if (!shoppingList.isEmpty()) {
                    new android.app.AlertDialog.Builder(view.getContext())
                            .setTitle(R.string.shopping_list_new_list_dialog_title)
                            .setMessage(view.getContext().getString(R.string.alert_dialog_new_list_question))
                            .setPositiveButton(R.string.dialog_positive_button_yes_text, (dialog, which) -> {
                                if (shoppingStartDate != null && shoppingEndDate != null) {
                                    //                              Nowa lista zakupów z usunięciem niekupionych produktów
                                    getShoppingList(shoppingStartDate, shoppingEndDate, true);
                                }
                            })
                            .setNegativeButton(R.string.dialog_negative_button_no_text, (dialog, which) -> {
                                if (shoppingStartDate != null && shoppingEndDate != null) {
                                    //                              Nowa lista zakupów z zachowaniem niekupionych produktów
                                    getShoppingList(shoppingStartDate, shoppingEndDate, false);
                                }
                            })
                            .show();


                } else {
                    if (shoppingStartDate != null && shoppingEndDate != null) {
                        //                              Nowa lista zakupów z zachowaniem niekupionych produktów
                        getShoppingList(shoppingStartDate, shoppingEndDate, true);
                    }
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

        shoppingStartDate = null;
        shoppingEndDate = null;

        setShoppingPeriodTextView();

        mealsViewModel = new ViewModelProvider(this).get(MealsViewModel.class);
        mealsViewModel.getMealsFullData().observe(getViewLifecycleOwner(), new Observer<List<MealFullData>>() {
            @Override
            public void onChanged(List<MealFullData> list) {
                allMeals = list;
            }
        });

        unitsViewModel = new ViewModelProvider(this).get(UnitsViewModel.class);
        unitsViewModel.getAllUnits().observe(getViewLifecycleOwner(), new Observer<List<Units>>() {
            @Override
            public void onChanged(List<Units> units) {
                shoppingListAdapter.setUnits(units);
            }
        });

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

    private void initRecycleView(View view) {
        shoppingListAdapter = new ShoppingListAdapter();
        shoppingListAdapter.setOnShoppingListItemClickListener(new ShoppingListAdapter.OnShoppingListItemClickListener() {
            @Override
            public void onShoppingItemCheckboxClicked(int position, ShoppingList shoppingList, boolean isChecked) {
                // Obsługuje zmianę stanu checkboxa
                onShoppingListItemToBuyClick(position, shoppingList, isChecked);
            }

            @Override
            public void onShoppingItemClick(int position, ShoppingList shoppingList) {
                ShoppingList updatedshoppingList = new ShoppingList(shoppingList.getItemName(), shoppingList.getAmount(), shoppingList.getUnitId(), !shoppingList.isBought());
                updatedshoppingList.setShoppingListId(shoppingList.getShoppingListId());
                shoppingListViewModel.update(updatedshoppingList);
            }

            @Override
            public void onShoppingItemLongClick(int position, ShoppingList selectedItem, View v) {
                PopupMenu popup = new PopupMenu(getContext(), v);
                popup.getMenuInflater().inflate(R.menu.menu_shopping_list_item, popup.getMenu());
                popup.setGravity(Gravity.END);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.menu_shopping_list_item_check_all) {
                            for (int i = 0; i < shoppingList.size(); i++) {
                                ShoppingList updatedshoppingList = new ShoppingList(shoppingList.get(i).getItemName(), shoppingList.get(i).getAmount(), shoppingList.get(i).getUnitId(), true);
                                updatedshoppingList.setShoppingListId(shoppingList.get(i).getShoppingListId());
                                shoppingListViewModel.update(updatedshoppingList);
                            }
                            updateRecycleView();
                        }
                        if (item.getItemId() == R.id.menu_shopping_list_item_delete_checked) {
                            if(shoppingList != null){
                                shoppingListViewModel.delete(selectedItem);
                            }
                        }
                        return true;
                    }
                });
                popup.show();//showing popup menu
            }
        });
        rv_shoppingListToBuy = view.findViewById(R.id.fr_shopping_list_rv_shopping_list_to_buy);

        shoppingListViewModel = new ViewModelProvider(this).get(ShoppingListViewModel.class);
        shoppingListViewModel.getAllShoppingList().observe(getViewLifecycleOwner(), new Observer<List<ShoppingList>>() {
            @Override
            public void onChanged(List<ShoppingList> list) {
                shoppingList = list;
                shoppingListAdapter.setAllShoppingList(list);
            }
        });

        rv_shoppingListToBuy.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_shoppingListToBuy.setAdapter(shoppingListAdapter);
    }

    private void updateRecycleView() {
        shoppingListAdapter.notifyDataSetChanged();
    }

    public void onShoppingListItemToBuyClick(int position, ShoppingList shoppingList, boolean isChecked) {
        ShoppingList updatedshoppingList = new ShoppingList(shoppingList.getItemName(), shoppingList.getAmount(), shoppingList.getUnitId(), isChecked);
        updatedshoppingList.setShoppingListId(shoppingList.getShoppingListId());
        shoppingListViewModel.update(updatedshoppingList);
    }

    public void getShoppingList(LocalDate dateStart, LocalDate dateEnd, boolean clearShoppingList) {

        List<ShoppingList> newShoppingList = new ArrayList<>();

        if (clearShoppingList == true) {
            for (int i = 0; i < shoppingList.size(); i++) {
                shoppingListViewModel.delete(shoppingList.get(i));
            }
        }
        else {
            newShoppingList = shoppingList;
        }

        for (int i = 0; i<allMeals.size(); i++){

            LocalDate date = allMeals.get(i).dietPlan.getDate();

            if ((date.isAfter(dateStart) || date.equals(dateStart)) && (date.isBefore(dateEnd) || date.equals(dateEnd))){
                RecipeFullData recipe = allMeals.get(i).recipe;
                if (recipe != null) {

                    for (int j = 0; j < recipe.ingredients.size(); j++) {
                        boolean found = false;
                        for (int k = 0; k < newShoppingList.size(); k++) {
                            if (newShoppingList.get(k).getItemName().toLowerCase().equals(recipe.ingredients.get(j).getName().toLowerCase())) {
                                if (newShoppingList.get(k).getUnitId().equals(recipe.ingredients.get(j).getUnitId())) {
                                    Double newAmount = newShoppingList.get(k).getAmount() + recipe.ingredients.get(j).getAmount();

                                    ShoppingList updatedShoppingListItem = new ShoppingList(newShoppingList.get(k).getItemName(), newAmount, newShoppingList.get(k).getUnitId(), false);
                                    updatedShoppingListItem.setShoppingListId(newShoppingList.get(k).getShoppingListId());
                                    newShoppingList.set(k, updatedShoppingListItem);

                                    found = true;
                                    break;
                                }
                            }
                        }

                        if (!found) {
                            ShoppingList newShoppingListItem = new ShoppingList(recipe.ingredients.get(j).getName(), recipe.ingredients.get(j).getAmount(), recipe.ingredients.get(j).getUnitId(), false);
                            newShoppingList.add(newShoppingListItem);
                        }

                    }
                }

            }
        }

        for(int i=0; i<newShoppingList.size(); i++){
            if(newShoppingList.get(i).getShoppingListId() != null) {
                shoppingListViewModel.update(newShoppingList.get(i));
            }
            else {
                shoppingListViewModel.insert(newShoppingList.get(i));
            }

        }
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
        UnitsAdapter unitsAdapter = new UnitsAdapter(requireContext(), new ArrayList<>());
        ingredientUnitSpinner.setAdapter(unitsAdapter);

        UnitsViewModel unitsViewModel = new ViewModelProvider(this).get(UnitsViewModel.class);
        unitsViewModel.getAllUnits().observe(this, new Observer<List<Units>>() {
            @Override
            public void onChanged(List<Units> units) {
                unitsAdapter.clear();
                unitsAdapter.addAll(units);
                unitsAdapter.notifyDataSetChanged();
            }
        });

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
                Units newIngredientUnitObject = (Units) ingredientUnitSpinner.getSelectedItem();
                String newIngredientUnit = newIngredientUnitObject.getName();

                if(ingredientNameEditText.getText().toString().isEmpty()){
                    ingredientNameEditText.setError(getString(R.string.dialog_add_shopping_item_error_name_text));
                    isValid = false;
                }
                if(ingredientAmountEditText.getText().toString().isEmpty()){
                    ingredientAmountEditText.setError(getString(R.string.dialog_add_shopping_item_error_amount_text));
                    isValid = false;
                }

                if (isValid) {
                    ShoppingList newShoppingList = new ShoppingList(newIngredientName, Double.parseDouble(newIngredientAmount), newIngredientUnitObject.getUnitId(), false);
                    materialDialog.dismiss();
                }
            }
        });

    }



}