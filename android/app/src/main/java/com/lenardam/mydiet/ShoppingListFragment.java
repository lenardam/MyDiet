package com.lenardam.mydiet;

import static com.lenardam.mydiet.utils.CalendarUtils.daysInWeekArray;
import static com.lenardam.mydiet.utils.CalendarUtils.monthYearFromDate;
import static com.lenardam.mydiet.utils.Utils.doubleToStringFormat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.lenardam.mydiet.adapters.ShoppingListAdapter;
import com.lenardam.mydiet.adapters.ShoppingPeriodAdapter;
import com.lenardam.mydiet.database.model.MealFullData;
import com.lenardam.mydiet.database.model.RecipeFullData;
import com.lenardam.mydiet.database.model.ShoppingItem;
import com.lenardam.mydiet.database.model.ShoppingList;
import com.lenardam.mydiet.database.viewModel.MealsViewModel;
import com.lenardam.mydiet.database.viewModel.ShoppingListViewModel;
import com.lenardam.mydiet.utils.CalendarUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShoppingListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingListFragment extends Fragment{

    private List<ShoppingList> shoppingList = new ArrayList<>();
    private List<MealFullData> allMeals = new ArrayList<>();
//    public static LocalDate shoppingStartDate;
//    public static LocalDate shoppingEndDate;
    private LocalDate selectedDate = LocalDate.now();
    private ArrayList<LocalDate> selectedWeek;
    private boolean allItemChecked = false;
    private boolean isEditing = false;

    private TextView shoppingMonthYearTextView;
    private ImageButton shoppingButtonPreviousWeek;
    private ImageButton shoppingButtonNextWeek;
    private TextView shoppingPeriodTextView;
    private Button generateShoppingListButton;
    private ShoppingListAdapter shoppingListAdapter;
    private RecyclerView shoppingPeriodRecyclerView;
    private ShoppingPeriodAdapter shoppingPeriodAdapter;
    private RecyclerView rv_shoppingListToBuy;
    private MaterialButton checkAllButton;
    private MaterialButton deleteCheckedButton;

    private ShoppingListViewModel shoppingListViewModel;
    private MealsViewModel mealsViewModel;

    private ItemTouchHelper shoppingListItemTouchHelper;
    private ItemTouchHelper dateItemTouchHelper;


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
        checkAllButton = (MaterialButton) view.findViewById(R.id.fr_shopping_list_btn_check_all);
        deleteCheckedButton = (MaterialButton) view.findViewById(R.id.fr_shopping_list_btn_delete_all);

        //Ukrywanie przycisków zaznacz wszystko i usuń zaznaczone
        if(shoppingList.isEmpty()) {
            checkAllButton.setVisibility(View.INVISIBLE);
            deleteCheckedButton.setVisibility(View.INVISIBLE);
        }
        else {
            checkAllButton.setVisibility(View.VISIBLE);
            deleteCheckedButton.setVisibility(View.VISIBLE);
        }

        shoppingButtonNextWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPreviousWeek();
            }
        });

        shoppingButtonPreviousWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNextWeek();
            }
        });

        generateShoppingListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LocalDate shoppingStartDate = shoppingPeriodAdapter.getShoppingStartDate();
                LocalDate shoppingEndDate = shoppingPeriodAdapter.getShoppingEndDate();

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

        checkAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allItemChecked = !allItemChecked;

                for (int i = 0; i < shoppingList.size(); i++) {
                    shoppingList.get(i).setBought(allItemChecked);
                }
                shoppingListViewModel.updateAll(shoppingList);
                updateRecycleView();
            }
        });

        deleteCheckedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shoppingList != null){
                    for (int i = 0; i < shoppingList.size(); i++) {
                        if (shoppingList.get(i).isBought()) {
                            shoppingListViewModel.delete(shoppingList.get(i));
                        }
                    }
                }
            }
        });

        mealsViewModel = new ViewModelProvider(this).get(MealsViewModel.class);
        mealsViewModel.getMealsFullData().observe(getViewLifecycleOwner(), new Observer<List<MealFullData>>() {
            @Override
            public void onChanged(List<MealFullData> list) {
                allMeals = list;
            }
        });

    }

    private void setShoppingPeriodTextView() {
        LocalDate shoppingStartDate = shoppingPeriodAdapter.getShoppingStartDate();
        LocalDate shoppingEndDate = shoppingPeriodAdapter.getShoppingEndDate();

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
        shoppingPeriodAdapter = new ShoppingPeriodAdapter();
        shoppingPeriodAdapter.setWeekDays(selectedWeek);
        shoppingPeriodAdapter.setListener(new ShoppingPeriodAdapter.OnDateClickListener() {
            @Override
            public void onDateClick(int position) {
                LocalDate clickedDate = selectedWeek.get(position);
                LocalDate shoppingStartDate = shoppingPeriodAdapter.getShoppingStartDate();
                LocalDate shoppingEndDate = shoppingPeriodAdapter.getShoppingEndDate();

                //jeżeli nie ma ustawionej daty shopping_start_date to ją ustaw
                if(shoppingStartDate == null){
                    shoppingPeriodAdapter.setShoppingStartDate(clickedDate);
                }
                //w przeciwnym wypadku, sprawdź czy nowa data jest po dacie shopping_start_date
                //jeżeli jest po dacie shopping_start_date to ustaw shopping_end_date
                //jeżeli jest przed, to zamień miejscami daty
                else if (shoppingEndDate == null) {
                    if (clickedDate.isAfter(shoppingStartDate)) {
                        shoppingPeriodAdapter.setShoppingEndDate(clickedDate);
                    }
                    else {
                        shoppingPeriodAdapter.setShoppingEndDate(shoppingStartDate);
                        shoppingPeriodAdapter.setShoppingStartDate(clickedDate);
                    }
                }
                //jeżeli obie daty są ustawione, to znaczy, że zaczęto generować nową listę zakupów
                else {
                    shoppingPeriodAdapter.setShoppingStartDate(clickedDate);
                    shoppingPeriodAdapter.setShoppingEndDate(null);
                }

                //zaktualizuj widok wyboru dat
                shoppingPeriodAdapter.notifyDataSetChanged();
                setShoppingPeriodTextView();
            }
        });



        shoppingPeriodRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));
        shoppingPeriodRecyclerView.setAdapter(shoppingPeriodAdapter);

        dateItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // nie obsługujemy drag & drop
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT) {
                    setNextWeek();
                } else if (direction == ItemTouchHelper.RIGHT) {
                    setPreviousWeek();
                }
            }

            @Override
            public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
                return 0.3f;
            }

            @Override
            public float getSwipeEscapeVelocity(float defaultValue) {
                return defaultValue * 1.5f;
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
                // Nie przesuwaj elementu - tylko reaguj na gest
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    // Ustaw przesunięcie na 0, żeby item się nie ruszał
                    super.onChildDraw(c, recyclerView, viewHolder, 0, 0, actionState, isCurrentlyActive);
                } else {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }
        });

        dateItemTouchHelper.attachToRecyclerView(shoppingPeriodRecyclerView);



    }

    private void setNextWeek() {
        selectedDate = selectedDate.minusWeeks(1);
        shoppingMonthYearTextView.setText(monthYearFromDate(selectedDate));
        selectedWeek.clear();
        selectedWeek.addAll(daysInWeekArray(selectedDate));
        shoppingPeriodAdapter.setWeekDays(selectedWeek);

        shoppingPeriodRecyclerView.setLayoutAnimation(
                AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_slide_in_right)
        );
        shoppingPeriodRecyclerView.scheduleLayoutAnimation();

    }

    private void setPreviousWeek() {
        selectedDate = selectedDate.plusWeeks(1);
        shoppingMonthYearTextView.setText(monthYearFromDate(selectedDate));
        selectedWeek.clear();
        selectedWeek.addAll(daysInWeekArray(selectedDate));
        shoppingPeriodAdapter.setWeekDays(selectedWeek);

        shoppingPeriodRecyclerView.setLayoutAnimation(
                AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_slide_in_left)
        );
        shoppingPeriodRecyclerView.scheduleLayoutAnimation();


    }

    @SuppressLint("ClickableViewAccessibility")
    private void initRecycleView(View view) {
        shoppingListAdapter = new ShoppingListAdapter();
        shoppingListAdapter.setOnShoppingListItemClickListener(new ShoppingListAdapter.OnShoppingListItemClickListener() {
            @Override
            public void onShoppingItemCheckboxClicked(int position, ShoppingList shoppingList, boolean isChecked) {
                // Obsługuje zmianę stanu checkboxa
                onShoppingListItemToBuyClick(position, shoppingList, isChecked);
            }

            @Override
            public void onShoppingItemTextChanged(int position, ShoppingList shoppingList) {
                shoppingListViewModel.update(shoppingList);
            }

            @Override
            public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                shoppingListItemTouchHelper.startDrag(viewHolder);
            }

            @Override
            public void onShoppingItemAddButtonClick() {
                if(shoppingList != null) {
                    ShoppingList newShoppingList = new ShoppingList("", shoppingList.size()+1, false);
                    shoppingListViewModel.insert(newShoppingList);
                }
            }

            @Override
            public void onShoppingRemoveItemButtonClick(int position, ShoppingList shoppingListItem) {
                shoppingListViewModel.delete(shoppingListItem);

                for (int i = 0; i < shoppingList.size(); i++) {
                    shoppingList.get(i).setItemPosition(i);
                }

                shoppingListViewModel.updateAll(shoppingList);

            }

            @Override
            public void onEditingStateChanged(boolean isEditingState) {
                isEditing = isEditingState;
            }

        });
        rv_shoppingListToBuy = view.findViewById(R.id.fr_shopping_list_rv_shopping_list_to_buy);

        rv_shoppingListToBuy.setOnTouchListener((v, event) -> {
            View currentFocus = ((Activity) v.getContext()).getCurrentFocus();
            if (currentFocus instanceof EditText) {
                currentFocus.clearFocus();

                InputMethodManager imm = (InputMethodManager)
                        v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            }
            return false; // false -> żeby klik dalej działał dla RecyclerView (scroll, etc.)
        });

        shoppingListViewModel = new ViewModelProvider(this).get(ShoppingListViewModel.class);
        shoppingListViewModel.getAllShoppingList().observe(getViewLifecycleOwner(), new Observer<List<ShoppingList>>() {
            @Override
            public void onChanged(List<ShoppingList> list) {
                shoppingList = list;
                if (!isEditing) {
                    shoppingListAdapter.setAllShoppingList(list);
                }

                if(shoppingList.isEmpty()) {
                    checkAllButton.setVisibility(View.GONE);
                    deleteCheckedButton.setVisibility(View.GONE);
                }
                else {
                    checkAllButton.setVisibility(View.VISIBLE);
                    deleteCheckedButton.setVisibility(View.VISIBLE);
                }
            }
        });

        rv_shoppingListToBuy.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_shoppingListToBuy.setAdapter(shoppingListAdapter);

        shoppingListItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                shoppingListAdapter.onItemMove(viewHolder.getBindingAdapterPosition(),target.getBindingAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);

                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG && viewHolder != null) {
                    // Zmiana tła na lekko szary, gdy zaczynasz przesuwać
                    View layout = viewHolder.itemView.findViewById(R.id.it_shopping_list_layout_shopping_item);
                    layout.setBackgroundColor(ContextCompat.getColor(layout.getContext(), R.color.lightGrey));
                }
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);

                // Przywrócenie oryginalnego koloru po zakończeniu przesuwania wykonywane jest po updacie danych w adapterze
                View layout = viewHolder.itemView.findViewById(R.id.it_shopping_list_layout_shopping_item);
                layout.setBackgroundColor(ContextCompat.getColor(layout.getContext(), R.color.white));

                // Pobierz aktualną listę z adaptera
                List<ShoppingList> items = shoppingListAdapter.getCurrentItems();

                // Ustaw itemPosition = indeks w liście
                for (int i = 0; i < items.size(); i++) {
                    items.get(i).setItemPosition(i);
                }

                // Wywołaj ViewModel (zapis w repozytorium na background thread)
                shoppingListViewModel.updateAll(items);
            }

        });

        shoppingListItemTouchHelper.attachToRecyclerView(rv_shoppingListToBuy);

    }

    private void updateRecycleView() {
        shoppingListAdapter.notifyDataSetChanged();
    }

    public void onShoppingListItemToBuyClick(int position, ShoppingList shoppingList, boolean isChecked) {
        shoppingList.setBought(isChecked);
        shoppingListViewModel.update(shoppingList);
    }

    public void getShoppingList(LocalDate dateStart, LocalDate dateEnd, boolean clearShoppingList) {

        List<ShoppingItem> newShoppingList = new ArrayList<>();
        List<ShoppingList> shoppingListToBuy = new ArrayList<>();

        for (int i = 0; i<allMeals.size(); i++){

            LocalDate date = allMeals.get(i).dietPlan.getDate();

            if ((date.isAfter(dateStart) || date.equals(dateStart)) && (date.isBefore(dateEnd) || date.equals(dateEnd))){
                RecipeFullData recipe = allMeals.get(i).recipe;

                if (recipe != null) {

                    double portionOfRecipe = allMeals.get(i).meal.getPortionOfRecipe() / allMeals.get(i).recipe.recipe.getServingSize();

                    for (int j = 0; j < recipe.ingredients.size(); j++) {
                        boolean found = false;
                        double ingredientAmount = recipe.ingredients.get(j).recipeIngredient.getAmount() * portionOfRecipe;

                        for (int k = 0; k < newShoppingList.size(); k++) {
                            if (newShoppingList.get(k).getItemName().equalsIgnoreCase(recipe.ingredients.get(j).recipeIngredient.getName())) {
                                if (newShoppingList.get(k).getUnitName().equals(recipe.ingredients.get(j).unit.getName())) {
                                    double newAmount = newShoppingList.get(k).getAmount() + ingredientAmount;
                                    newShoppingList.get(k).setAmount(newAmount);

                                    found = true;
                                    break;
                                }
                            }
                        }

                        if (!found) {
                            ShoppingItem newShoppingListItem = new ShoppingItem(recipe.ingredients.get(j).recipeIngredient.getName(), ingredientAmount, recipe.ingredients.get(j).unit.getName());
                            newShoppingList.add(newShoppingListItem);
                        }

                    }
                }

            }
        }

        if (clearShoppingList == true) {
            for (int i = 0; i < shoppingList.size(); i++) {
                shoppingListViewModel.delete(shoppingList.get(i));
            }
        }
        else {
            shoppingListToBuy = shoppingList;
        }

        for(int i=0; i<newShoppingList.size(); i++){
            String name = newShoppingList.get(i).getItemName();
            String unitName = newShoppingList.get(i).getUnitName();
            String amount = doubleToStringFormat(newShoppingList.get(i).getAmount());

            String newShoppingItemName = name + " - " +  amount + " " + unitName;

            ShoppingList newShoppingListItem = new ShoppingList(newShoppingItemName, shoppingListToBuy.size()+1, false);
            shoppingListToBuy.add(newShoppingListItem);
        }

        for(int i=0; i<shoppingListToBuy.size(); i++){

            shoppingListToBuy.get(i).setItemPosition(i);

            if(shoppingListToBuy.get(i).getShoppingListId() != null) {
                shoppingListViewModel.update(shoppingListToBuy.get(i));
            }
            else {
                shoppingListViewModel.insert(shoppingListToBuy.get(i));
            }

        }
    }
}