import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:intl/intl.dart';
import 'package:shared_preferences/shared_preferences.dart';

class MealRepository {
  final http.Client httpClient;

  MealRepository({required this.httpClient});

  final int ipSwitcher = 1;

  String get baseUrl {
    switch (ipSwitcher) {
      case 0:
        return 'http://localhost:8081';
      case 1:
        return 'http://137.112.225.85:8081'; //replace with laptop ipv4 address
                                              //Also change auth.dart
                                                //Also change onboarding_screen.dart, ipconfig to get wifi ipv4 address
      default:
        return 'http://localhost:8081';
    }
  }

  Future<List<dynamic>> fetchMenuItems(DateTime selectedDate, String mealType) async {
    final dateString = selectedDate.toIso8601String().split('T')[0];
    final url = '$baseUrl/api/menu-items?date=$dateString&type=$mealType';
    final response = await httpClient.get(Uri.parse(url));
    if (response.statusCode == 200) {
      return json.decode(response.body);
    } else {
      throw Exception('Failed to load menu items');
    }
  }

  Future<int> getUserRating(int userId, int menuItemId) async {
    final url = '$baseUrl/api/reviews/$menuItemId/user-rating?userId=$userId';
    final response = await httpClient.get(Uri.parse(url));
    if (response.statusCode == 200) {
      final rating = json.decode(response.body);
      return rating != null ? rating as int : 0;
    } else {
      throw Exception('Failed to get user rating');
    }
  }

  Future<void> sendReview(int userId, int menuItemId, int rating) async {
    final url = '$baseUrl/api/reviews/$menuItemId?userId=$userId&stars=$rating';
    final response = await httpClient.post(Uri.parse(url));
    if (response.statusCode != 200) {
      throw Exception('Failed to send review');
    }
  }

  Future<Map<String, dynamic>> fetchRecommendations(DateTime selectedDate, String mealType) async {
    final prefs = await SharedPreferences.getInstance();
    final userId = prefs.getString('userId');
    final formattedDate = DateFormat('yyyy-MM-dd').format(selectedDate);

    if (userId != null) {
      final url = '$baseUrl/api/recommendations?userId=$userId&mealType=$mealType&date=$formattedDate';
      final response = await httpClient.get(Uri.parse(url));
      if (response.statusCode == 200) {
        List<dynamic> rawData = json.decode(response.body);
        if (rawData.isNotEmpty) {
          List<Map<String, dynamic>> cleanedData = rawData.map<Map<String, dynamic>>((item) {
            return {
              "item": item['item'],
            };
          }).toList();

          Map<String, dynamic> totals = {
            'totalProtein': rawData[0]['totalProtein'] ?? 0,
            'totalCarbs': rawData[0]['totalCarbs'] ?? 0,
            'totalFats': rawData[0]['totalFats'] ?? 0,
            'totalCalories': rawData[0]['totalCalories'] ?? 0,
          };

          return {
            'items': cleanedData,
            'totals': totals
          };
        }
      }
    }
    print('No data fetched or error occurred');
    return {};
  }

  Future<List<dynamic>> getNotificationFoods(int userId) async {
    final url = '$baseUrl/api/get-notifications?userId=$userId';
    final response = await httpClient.get(Uri.parse(url));
    if (response.statusCode == 200) {
      return json.decode(response.body);
    } else {
      throw Exception('Failed to load notification foods');
    }
  }
}