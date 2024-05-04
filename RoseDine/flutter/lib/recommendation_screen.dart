import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class RecommendationScreen extends StatefulWidget {
  final String mealType;

  const RecommendationScreen({required this.mealType});

  @override
  _RecommendationScreenState createState() => _RecommendationScreenState();
}

class _RecommendationScreenState extends State<RecommendationScreen> {
  Map<String, dynamic> _preferences = {};

  @override
  void initState() {
    super.initState();
    _fetchPreferences();
  }

  Future<void> _fetchPreferences() async {
    final prefs = await SharedPreferences.getInstance();
    final userId = prefs.getString('userId');

    if (userId != null) {
      final url = 'http://localhost:8081/api/user-preferences/get-all-preferences?userId=$userId&mealType=${widget.mealType}';
      final response = await http.get(Uri.parse(url));

      if (response.statusCode == 200) {
        setState(() {
          _preferences = json.decode(response.body);
        });
      } else {
        // Handle error
        print('Failed to fetch preferences');
      }
    }
  }


  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Recommendations'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: _preferences.isEmpty
            ? Center(child: CircularProgressIndicator())
            : Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text('Dietary Restrictions:'),
            Text('Vegan: ${_preferences['Is_Vegan']}'),
            Text('Vegetarian: ${_preferences['Is_Vegetarian']}'),
            Text('Gluten-Free: ${_preferences['Is_Gluten_Free']}'),
            SizedBox(height: 16),
            Text('Macros (${widget.mealType}):'),
            Text('Protein: ${_preferences['Protein'] ?? 'N/A'}'),
            Text('Carbohydrates: ${_preferences['Carbs'] ?? 'N/A'}'),
            Text('Fat: ${_preferences['Fats'] ?? 'N/A'}'),
            Text('Calories: ${_preferences['Calories'] ?? 'N/A'}'),
          ],
        ),
      ),
    );
  }
}