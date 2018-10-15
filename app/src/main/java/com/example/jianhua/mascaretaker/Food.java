package com.example.jianhua.mascaretaker;

public class Food {
    public String query;
    public int calories;
    public double fat;
    public double sat_fat;
    public double cholesterol;
    public double sodium;
    public double carbs;
    public double fiber;
    public double sugar;
    public double protein;
    public double potassium;

    public Food() {}

    public Food(String query, int calories){
        this.query = query;
        this.calories = calories;
    }

    public Food(String query, int calories, double fat, double sat_fat, double cholesterol,
                double sodium, double carbs, double fiber, double sugar, double protein,
                double potassium) {
        this.query = query;
        this.calories = calories;
        this.fat = fat;
        this.sat_fat = sat_fat;
        this.cholesterol = cholesterol;
        this.sodium = sodium;
        this.carbs = carbs;
        this.fiber = fiber;
        this.sugar = sugar;
        this.protein = protein;
        this.potassium = potassium;
    }

}
