import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'meal_provider.dart';

class MenuItemScreen extends ConsumerWidget {
  final String mealType;

  const MenuItemScreen({Key? key, required this.mealType}) : super(key: key);

  Future<List<dynamic>> fetchMenuItems(DateTime selectedDate, String mealType) async {
    final dateString = selectedDate.toIso8601String().split('T')[0];
    final url = 'http://localhost:8081/api/menu-items?date=$dateString&type=$mealType';

    final response = await http.get(Uri.parse(url));

    if (response.statusCode == 200) {
      return json.decode(response.body);
    } else {
      throw Exception('Failed to load menu items');
    }
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final selectedDate = ref.watch(selectedDateProvider);

    return Scaffold(
      appBar: AppBar(
        title: Text(mealType),
      ),
      body: FutureBuilder<List<dynamic>>(
        future: fetchMenuItems(selectedDate, mealType),
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError) {
            return Center(child: Text('Error: ${snapshot.error}'));
          } else if (snapshot.hasData) {
            final menuItems = snapshot.data!;

            return ListView.builder(
              itemCount: menuItems.length,
              itemBuilder: (context, index) {
                final item = menuItems[index];
                return ListTile(
                  title: Text(item['name']),
                  subtitle: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text('Stars: ${item['overallStars']}'),
                      Text('Fats: ${item['fats']}g'),
                      Text('Protein: ${item['protein']}g'),
                      Text('Calories: ${item['calories']}'),
                      Text('Gluten-Free: ${item['glutenFree']}'),
                      Text('Vegetarian: ${item['vegetarian']}'),
                      Text('Vegan: ${item['vegan']}'),
                      Text('Carbs: ${item['carbs']}g'),
                    ],
                  ),
                );
              },
            );
          } else {
            return const Center(child: Text('No menu items available.'));
          }
        },
      ),
    );
  }
}