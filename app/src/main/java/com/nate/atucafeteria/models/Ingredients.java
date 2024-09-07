package com.nate.atucafeteria.models;

import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class Ingredients {

    private Map<String, List<Ingredient>> ingredientsMap = new HashMap<>();

    public Ingredients() {
        // Initialize with specific ingredients and prices for different foods
        ingredientsMap.put("Fufu", createFufuIngredients());
        ingredientsMap.put("Banku", createBankuIngredients());
        ingredientsMap.put("Yam", createYamIngredients());
        ingredientsMap.put("Rice", createRiceIngredients());
        ingredientsMap.put("Fried_Rice", createFriedRiceIngredients());
        ingredientsMap.put("Waakye", createWaakyeIngredients());
        ingredientsMap.put("Jollof", createJollofIngredients());
    }
//(Grilled Chicken, Fried Chicken, Goat meat, Okro soup)
    private List<Ingredient> createFufuIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("Grilled Chicken", 20.00));
        ingredients.add(new Ingredient("Fried Chicken", 20.00));
        ingredients.add(new Ingredient("Goat meat", 25.00));
        return ingredients;
    }

    private List<Ingredient> createBankuIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("Grilled Chicken", 20.00));
        ingredients.add(new Ingredient("Okro Soup", 5.00));
        ingredients.add(new Ingredient("Fried Chicken", 20.00));
        ingredients.add(new Ingredient("Goat meat", 25.00));
        return ingredients;
    }


    private List<Ingredient> createYamIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("Grilled Chicken", 20.00));
        ingredients.add(new Ingredient("Fried Chicken", 20.00));
        ingredients.add(new Ingredient("Goat meat", 25.00));
        return ingredients;
    }

    private List<Ingredient> createRiceIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("Grilled Chicken", 20.00));
        ingredients.add(new Ingredient("Fried Chicken", 20.00));
        ingredients.add(new Ingredient("Goat meat", 25.00));
        ingredients.add(new Ingredient("Salad", 5.00));
        return ingredients;
    }

    private List<Ingredient> createFriedRiceIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("Grilled Chicken", 20.00));
        ingredients.add(new Ingredient("Salad", 5.00));
        ingredients.add(new Ingredient("Fried Chicken", 20.00));
        ingredients.add(new Ingredient("Goat meat", 25.00));

        return ingredients;
    }


    private List<Ingredient> createWaakyeIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("Grilled Chicken", 20.00));
        ingredients.add(new Ingredient("Salad Soup", 5.00));
        ingredients.add(new Ingredient("Noodles", 2.00));
        ingredients.add(new Ingredient("Fried Chicken", 20.00));
        ingredients.add(new Ingredient("Goat meat", 25.00));
        return ingredients;
    }

    private List<Ingredient> createJollofIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("Grilled Chicken", 20.00));
        ingredients.add(new Ingredient("Salad", 5.00));
        ingredients.add(new Ingredient("Fried Chicken", 20.00));
        ingredients.add(new Ingredient("Goat meat", 25.00));
        return ingredients;
    }

    public List<Ingredient> getIngredientsForFood(String foodName) {
        return ingredientsMap.getOrDefault(foodName, new ArrayList<>());
    }
}



//public class Ingredients {
//
//    private Map<String, List<Ingredient>> ingredientsMap = new HashMap<>();
//
//    public Ingredients() {
//        // Initialize with sample ingredients for different foods
//        ingredientsMap.put("Fufu", createFufuIngredients());
//        ingredientsMap.put("Boiled Yam", createBoiledYamIngredients());
//        ingredientsMap.put("Boiled Rice", createBoiledRiceIngredients());
//        ingredientsMap.put("Fried Rice", createFriedRiceIngredients());
//        ingredientsMap.put("Waakye", createWaakyeIngredients());
//        ingredientsMap.put("Jollof Rice", createJollofRiceIngredients());
//    }
//
//    public List<Ingredient> createFufuIngredients() {
//        List<Ingredient> ingredients = new ArrayList<>();
//        ingredients.add(new Ingredient("Cassava", 3.00));
//        ingredients.add(new Ingredient("Plantain", 2.50));
//        return ingredients;
//    }
//
//    private List<Ingredient> createBoiledYamIngredients() {
//        List<Ingredient> ingredients = new ArrayList<>();
//        ingredients.add(new Ingredient("Yam", 4.00));
//        return ingredients;
//    }
//
//    private List<Ingredient> createBoiledRiceIngredients() {
//        List<Ingredient> ingredients = new ArrayList<>();
//        ingredients.add(new Ingredient("Rice", 2.00));
//        return ingredients;
//    }
//
//    private List<Ingredient> createFriedRiceIngredients() {
//        List<Ingredient> ingredients = new ArrayList<>();
//        ingredients.add(new Ingredient("Rice", 2.00));
//        ingredients.add(new Ingredient("Vegetables", 1.50));
//        ingredients.add(new Ingredient("Chicken", 3.00));
//        return ingredients;
//    }
//
//    private List<Ingredient> createWaakyeIngredients() {
//        List<Ingredient> ingredients = new ArrayList<>();
//        ingredients.add(new Ingredient("Rice", 2.00));
//        ingredients.add(new Ingredient("Beans", 2.50));
//        return ingredients;
//    }
//
//    private List<Ingredient> createJollofRiceIngredients() {
//        List<Ingredient> ingredients = new ArrayList<>();
//        ingredients.add(new Ingredient("Rice", 2.00));
//        ingredients.add(new Ingredient("Tomato Sauce", 1.50));
//        ingredients.add(new Ingredient("Chicken", 3.00));
//        return ingredients;
//    }
//
//    public  List<Ingredient> getIngredientsForFood(String foodName) {
//        return ingredientsMap.getOrDefault(foodName, new ArrayList<>());
//    }
//}
