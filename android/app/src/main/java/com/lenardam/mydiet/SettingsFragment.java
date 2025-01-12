package com.lenardam.mydiet;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;

import com.lenardam.mydiet.adapters.IngredientAdapter;
import com.lenardam.mydiet.adapters.RecipeTagAdapter;
import com.lenardam.mydiet.model.DietPlan;
import com.lenardam.mydiet.model.Meal;

import java.time.LocalDate;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment implements RecipeTagAdapter.OnRecipeTagClickListener {

    private Button add_tag_button;
    private Spinner number_of_meals_spinner;
    private String[] number_of_meals_options = {"1","2","3","4","5","6"};
    private RecyclerView allTagsRecyclerView;
    private RecipeTagAdapter recipe_tag_adapter;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initAllTagsRecycleView(view);
    }

    private void initViews(View view) {
        number_of_meals_spinner = (Spinner) view.findViewById(R.id.numberOfMealsSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, number_of_meals_options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        number_of_meals_spinner.setAdapter(adapter);
        number_of_meals_spinner.setSelection(adapter.getPosition(String.valueOf(MainActivity.myDiet.getDietSettings().getNumber_of_meals_for_diet())));

        number_of_meals_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                numberOfMealsChanged(adapterView, view, position, id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        add_tag_button = (Button) view.findViewById(R.id.addTagButton);
        add_tag_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initNewTagDialog();
            }
        });
    }

    private void numberOfMealsChanged(AdapterView<?> adapterView, View view, int position, long id) {
        int old_number_of_meals = MainActivity.myDiet.getDietSettings().getNumber_of_meals_for_diet();

        if (String.valueOf(adapterView.getItemAtPosition(position)) != null) {
            String selected_number_of_meals_text = String.valueOf(adapterView.getItemAtPosition(position));
            int selected_number_of_meals = Integer.valueOf(selected_number_of_meals_text);
            MainActivity.myDiet.getDietSettings().setNumber_of_meals_for_diet(selected_number_of_meals);

            //jeżeli nowa liczba posiłków jest większa niż poprzednia to dodajemy nowe posiłki do diety
            if (selected_number_of_meals > old_number_of_meals) {
                for (int i = 0; i < MainActivity.myDiet.getDiet_plan().size(); i++) {
                    DietPlan current_diet_plan = MainActivity.myDiet.getDiet_plan().get(i);
                    if (current_diet_plan.getDiet_plan_date().isAfter(LocalDate.now()) && current_diet_plan.getNumber_of_meals() < selected_number_of_meals) {
                        int number_of_new_meals = selected_number_of_meals - current_diet_plan.getMeals().size();
                        for (int j = 0; j < number_of_new_meals; j++) {
                            current_diet_plan.getMeals().add(new Meal());
                        }
                        current_diet_plan.setNumber_of_meals(selected_number_of_meals);
                    }

                }
            }
            //jeżeli jest mniejsza, to usuwamy posiłki z diety o ile nie są już zaplanowane posiłki
            else if (selected_number_of_meals < old_number_of_meals) {
                for (int i = 0; i < MainActivity.myDiet.getDiet_plan().size(); i++) {
                    DietPlan current_diet_plan = MainActivity.myDiet.getDiet_plan().get(i);
                    if (current_diet_plan.getDiet_plan_date().isAfter(LocalDate.now()) && current_diet_plan.getNumber_of_meals() > selected_number_of_meals) {
                        int number_of_removed_meals = current_diet_plan.getMeals().size() - selected_number_of_meals;
                        int removed_meals = 0;
                        for (int j = current_diet_plan.getMeals().size() - 1; j >= 0; j--) {
                            Meal current_meal = current_diet_plan.getMeals().get(j);
                            if (current_meal.getRecipe() == null) {
                                current_diet_plan.getMeals().remove(j);
                                removed_meals++;
                            }
                            // Zatrzymaj pętlę, jeśli osiągnięto limit usuniętych posiłków
                            if (removed_meals >= number_of_removed_meals) {
                                break;
                            }
                        }
                        current_diet_plan.setNumber_of_meals(selected_number_of_meals);
                    }
                }
            }
        }
    }

    private void initAllTagsRecycleView(View view) {
        allTagsRecyclerView = view.findViewById(R.id.allTagsRecyclerView);
        recipe_tag_adapter = new RecipeTagAdapter(MainActivity.myDiet.getAll_tags(), this);
        //TO DO: zamienić na flexbox, żeby zawijał się do nowej linii
        allTagsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        allTagsRecyclerView.setAdapter(recipe_tag_adapter);
    }

    private void initNewTagDialog() {
        // Inflate widok z XML
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.new_tag_dialog, null);

        // Stwórz dialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext())
                .setTitle("Dodaj nową kategorię przepisu")
                .setView(dialogView);

        // Inicjalizacja elementów widoku
        EditText new_tag_edit_text = dialogView.findViewById(R.id.new_tag_edit_text);

        // Dodanie przycisków do dialogu
        alertDialogBuilder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialogBuilder.setPositiveButton("Zapisz", null);

        // Wyświetlenie dialogu
        AlertDialog materialDialog = alertDialogBuilder.create();
        materialDialog.show();
        materialDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean is_valid = true;
                String new_tag_name = new_tag_edit_text.getText().toString();

                if (new_tag_name.isEmpty()) {
                    new_tag_edit_text.setError("Nazwa kategorii nie została podana!");
                    is_valid = false;
                }

                if (MainActivity.myDiet.getAll_tags().contains(new_tag_name)) {
                    new_tag_edit_text.setError("Podana kategoria już istnieje!");
                    is_valid = false;
                }

                if (is_valid) {
                    MainActivity.myDiet.getAll_tags().add(new_tag_name);
                    materialDialog.dismiss();
                    recipe_tag_adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.settings_fragment, container, false);
    }

    @Override
    public void onRecipeTagClick(int position, View view) {

    }

    @Override
    public void onRecipeTagLongClick(int position, View view) {
        PopupMenu popup = new PopupMenu(getContext(), view);
        popup.getMenuInflater().inflate(R.menu.pop_up_delete, popup.getMenu());
        popup.setGravity(Gravity.END);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.pop_up_delete){
                    MainActivity.myDiet.getAll_tags().remove(position);
                    recipe_tag_adapter.notifyDataSetChanged();
                }
                return true;
            }
        });
        popup.show();
    }
}

