import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:http/http.dart' as http;
import 'package:rosedine/widgets/menu_item_widget.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'dart:convert';
import 'meal_provider.dart';
import 'recommendation_screen.dart';

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

   Future<void> sendReview(int userId, int menuItemId, int rating) async {
    final url = 'http://localhost:8081/api/reviews/$menuItemId?userId=$userId&stars=$rating';
    print('Sending review: userId=$userId, menuItemId=$menuItemId, rating=$rating');

    final response = await http.post(Uri.parse(url));

    if (response.statusCode != 200) {
      print('Failed to send review. Status code: ${response.statusCode}');
      throw Exception('Failed to send review');
    } else {
      print('Review sent successfully');
    }
  }

  Future<int> getUserRating(int userId, int menuItemId) async {
    final url = 'http://localhost:8081/api/reviews/$menuItemId/user-rating?userId=$userId';

    final response = await http.get(Uri.parse(url));

    if (response.statusCode == 200) {
      final rating = json.decode(response.body);
      return rating != null ? rating as int : 0;
    } else {
      throw Exception('Failed to get user rating');
    }
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
                  MaterialPageRoute(builder: (context) => RecommendationScreen(mealType: mealType, selectedDate: selectedDate))

              );
            },
          ),
        ],
      ),
      body: FutureBuilder<List<dynamic>>(
        future: fetchMenuItems(selectedDate, mealType).then((items) {
          // Sort items alphabetically by name
          items.sort((a, b) => a['name'].compareTo(b['name']));
          return items;
        }),
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError) {
            return Center(child: Text('Error: ${snapshot.error}'));
          } else if (snapshot.hasData) {
            final menuItems = snapshot.data!;

            return ListView.separated(
              itemCount: menuItems.length,
              separatorBuilder: (context, index) => const SizedBox(height: 16),
              itemBuilder: (context, index) {
                final item = menuItems[index];
                final menuItemId = item['id'];

                return Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 8.0),
                  child: MenuItemWidget(
                    menuItem: item,
                    onRatingUpdate: (userId, rating) {
                      if (menuItemId != null) {
                        sendReview(userId, menuItemId, rating);
                      } else {
                        print('Menu item ID is null');
                      }
                    },
                    getUserRating: (userId, menuItemId) => getUserRating(userId, menuItemId),
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