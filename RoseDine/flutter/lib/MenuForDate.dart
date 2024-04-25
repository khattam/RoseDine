class MenuForDate {
  final Map<String, List<String>> meals;

  MenuForDate({required this.meals});

  factory MenuForDate.fromJson(Map<String, dynamic> json) {
    Map<String, List<String>> mealsMap = json.map(
      (mealType, mealItems) => MapEntry(mealType, List<String>.from(mealItems)),
    );
    return MenuForDate(meals: mealsMap);
  }

  List<String> getMealsForType(String type) {
    return meals[type] ?? [];
  }
}
