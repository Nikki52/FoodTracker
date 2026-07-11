package com.example.data

object PrepopulatedData {
    val foods = listOf(
        // Indian Foods
        Food(name = "Idli", servingSize = "2 pieces (100g)", calories = 117, protein = 3.2f, carbs = 24.2f, fat = 0.4f, fiber = 1.0f, category = "Indian Foods"),
        Food(name = "Dosa", servingSize = "1 piece (100g)", calories = 168, protein = 3.9f, carbs = 29.0f, fat = 3.7f, fiber = 0.9f, category = "Indian Foods"),
        Food(name = "Chapati", servingSize = "1 piece (40g)", calories = 120, protein = 3.5f, carbs = 20.0f, fat = 3.0f, fiber = 2.0f, category = "Indian Foods"),
        Food(name = "Boiled Rice", servingSize = "1 cup (158g)", calories = 205, protein = 4.3f, carbs = 44.5f, fat = 0.4f, fiber = 0.6f, category = "Indian Foods"),
        Food(name = "Dal (Cooked)", servingSize = "1 cup (200g)", calories = 227, protein = 12.0f, carbs = 40.0f, fat = 2.0f, fiber = 15.0f, category = "Indian Foods"),
        Food(name = "Butter Chicken", servingSize = "1 bowl (250g)", calories = 438, protein = 25.0f, carbs = 10.0f, fat = 33.0f, fiber = 1.0f, category = "Indian Foods"),
        Food(name = "Paneer Tikka", servingSize = "1 plate (150g)", calories = 250, protein = 18.0f, carbs = 5.0f, fat = 18.0f, fiber = 0.5f, category = "Indian Foods"),
        Food(name = "Biryani (Chicken)", servingSize = "1 plate (300g)", calories = 480, protein = 18.0f, carbs = 58.0f, fat = 16.0f, fiber = 2.0f, category = "Indian Foods"),
        Food(name = "Pongal", servingSize = "1 cup (200g)", calories = 212, protein = 6.0f, carbs = 32.0f, fat = 7.0f, fiber = 1.5f, category = "Indian Foods"),
        Food(name = "Poha", servingSize = "1 plate (150g)", calories = 250, protein = 4.5f, carbs = 45.0f, fat = 6.0f, fiber = 2.0f, category = "Indian Foods"),
        Food(name = "Sambar", servingSize = "1 bowl (150g)", calories = 110, protein = 4.0f, carbs = 18.0f, fat = 2.5f, fiber = 3.5f, category = "Indian Foods"),
        Food(name = "Vada", servingSize = "1 piece (50g)", calories = 145, protein = 3.0f, carbs = 14.0f, fat = 8.0f, fiber = 1.0f, category = "Indian Foods"),

        // Fruits
        Food(name = "Apple", servingSize = "1 medium (182g)", calories = 95, protein = 0.5f, carbs = 25.0f, fat = 0.3f, fiber = 4.4f, category = "Fruits"),
        Food(name = "Banana", servingSize = "1 medium (118g)", calories = 105, protein = 1.3f, carbs = 27.0f, fat = 0.4f, fiber = 3.1f, category = "Fruits"),
        Food(name = "Orange", servingSize = "1 medium (131g)", calories = 62, protein = 1.2f, carbs = 15.0f, fat = 0.2f, fiber = 3.1f, category = "Fruits"),
        Food(name = "Watermelon", servingSize = "1 cup (152g)", calories = 46, protein = 0.9f, carbs = 11.5f, fat = 0.2f, fiber = 0.6f, category = "Fruits"),
        Food(name = "Papaya", servingSize = "1 cup (145g)", calories = 62, protein = 0.7f, carbs = 16.0f, fat = 0.4f, fiber = 2.5f, category = "Fruits"),
        Food(name = "Mango", servingSize = "1 cup (165g)", calories = 99, protein = 1.4f, carbs = 25.0f, fat = 0.6f, fiber = 2.6f, category = "Fruits"),

        // Vegetables
        Food(name = "Carrot", servingSize = "1 medium (61g)", calories = 25, protein = 0.6f, carbs = 6.0f, fat = 0.1f, fiber = 1.7f, category = "Vegetables"),
        Food(name = "Potato (Boiled)", servingSize = "1 medium (136g)", calories = 118, protein = 2.5f, carbs = 27.0f, fat = 0.1f, fiber = 2.4f, category = "Vegetables"),
        Food(name = "Broccoli", servingSize = "1 cup (91g)", calories = 31, protein = 2.6f, carbs = 6.0f, fat = 0.3f, fiber = 2.4f, category = "Vegetables"),
        Food(name = "Spinach", servingSize = "1 cup (30g raw)", calories = 7, protein = 0.9f, carbs = 1.1f, fat = 0.1f, fiber = 0.7f, category = "Vegetables"),
        
        // Dairy
        Food(name = "Milk", servingSize = "1 glass (250ml)", calories = 150, protein = 8.0f, carbs = 12.0f, fat = 8.0f, fiber = 0.0f, category = "Dairy"),
        Food(name = "Curd", servingSize = "1 bowl (100g)", calories = 98, protein = 3.1f, carbs = 3.4f, fat = 4.3f, fiber = 0.0f, category = "Dairy"),
        Food(name = "Cheese", servingSize = "1 slice (28g)", calories = 113, protein = 7.0f, carbs = 0.4f, fat = 9.0f, fiber = 0.0f, category = "Dairy"),
        
        // Meat / Eggs
        Food(name = "Boiled Egg", servingSize = "1 large (50g)", calories = 78, protein = 6.3f, carbs = 0.6f, fat = 5.3f, fiber = 0.0f, category = "Meat/Eggs"),
        Food(name = "Egg White", servingSize = "1 large (33g)", calories = 17, protein = 3.6f, carbs = 0.2f, fat = 0.1f, fiber = 0.0f, category = "Meat/Eggs"),
        Food(name = "Chicken Breast", servingSize = "100g (Cooked)", calories = 165, protein = 31.0f, carbs = 0.0f, fat = 3.6f, fiber = 0.0f, category = "Meat/Eggs"),
        Food(name = "Fish Curry", servingSize = "1 bowl (200g)", calories = 280, protein = 20.0f, carbs = 8.0f, fat = 18.0f, fiber = 1.0f, category = "Meat/Eggs"),
        
        // Fast Food
        Food(name = "Pizza (Margherita)", servingSize = "1 slice (100g)", calories = 266, protein = 11.0f, carbs = 33.0f, fat = 10.0f, fiber = 2.0f, category = "Fast Food"),
        Food(name = "Burger", servingSize = "1 medium (150g)", calories = 350, protein = 15.0f, carbs = 30.0f, fat = 18.0f, fiber = 2.0f, category = "Fast Food"),
        Food(name = "Fries", servingSize = "1 medium serving (117g)", calories = 365, protein = 4.0f, carbs = 48.0f, fat = 17.0f, fiber = 4.0f, category = "Fast Food"),
        
        // Drinks / Snacks
        Food(name = "Oats", servingSize = "1 cup (234g cooked)", calories = 158, protein = 6.0f, carbs = 27.0f, fat = 3.2f, fiber = 4.0f, category = "Snacks"),
        Food(name = "Protein Shake", servingSize = "1 scoop (30g) + water", calories = 120, protein = 24.0f, carbs = 3.0f, fat = 1.5f, fiber = 0.0f, category = "Drinks"),
        Food(name = "Coffee (Black)", servingSize = "1 cup (240ml)", calories = 2, protein = 0.3f, carbs = 0.0f, fat = 0.0f, fiber = 0.0f, category = "Drinks"),
        Food(name = "Tea (with milk/sugar)", servingSize = "1 cup (150ml)", calories = 60, protein = 1.5f, carbs = 10.0f, fat = 1.5f, fiber = 0.0f, category = "Drinks")
    )
}

