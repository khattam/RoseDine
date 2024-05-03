import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:http/http.dart' as http;
import 'package:rosedine/widgets/menu_item_widget.dart';
import 'dart:convert';
import 'meal_provider.dart';
import 'recommendation_screen.dart';
import 'widgets/menu_item_widget.dart';

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

  Future<List<dynamic>> fetchMenuHard() async {
    final url = 'http://localhost:8081/api/menu-items/hardcoded';

    final response = await http.get(Uri.parse(url));

    if (response.statusCode == 200) {
      return json.decode(response.body);
    } else {
      throw Exception('Failed to load hardcoded menu items');
    }
  }

  Future<List<dynamic>> fetchCombinedMenu(DateTime selectedDate, String mealType) async {
    // Fetch both regular and hardcoded menu items
    final regularItems = await fetchMenuItems(selectedDate, mealType);
    final hardcodedItems = await fetchMenuHard();
    // Combine the two lists
    return [...regularItems, ...hardcodedItems];
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final selectedDate = ref.watch(selectedDateProvider);

    return Scaffold(
      appBar: AppBar(
        title: Text('Menu Items'),
        actions: [
          IconButton(
            icon: Icon(Icons.recommend),
            onPressed: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => RecommendationScreen(mealType: mealType)),
              );
            },
          ),
        ],
      ),
      body: FutureBuilder<List<dynamic>>(
        future: fetchCombinedMenu(selectedDate, mealType),
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError) {
            return Center(child: Text('Error: ${snapshot.error}'));
          } else if (snapshot.hasData) {
            final menuItems = snapshot.data!;

            return ListView.separated(
              itemCount: (menuItems.length / 2).ceil(),
              separatorBuilder: (context, index) => const SizedBox(height: 16),
              itemBuilder: (context, index) {
                final startIndex = index * 2;
                final endIndex = startIndex + 2 < menuItems.length ? startIndex + 2 : menuItems.length;
                final rowItems = menuItems.sublist(startIndex, endIndex);

                return Row(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: rowItems.map((item) {
                    return Expanded(
                      child: Padding(
                        padding: const EdgeInsets.symmetric(horizontal: 8.0),
                        child: MenuItemWidget(menuItem: item),
                      ),
                    );
                  }).toList(),
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
