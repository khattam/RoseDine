import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:intl/intl.dart';

class RecommendationScreen extends StatefulWidget {
  final String mealType;
  final DateTime selectedDate;

  const RecommendationScreen({required this.mealType, required this.selectedDate});

  @override
  _RecommendationScreenState createState() => _RecommendationScreenState();
}

class _RecommendationScreenState extends State<RecommendationScreen> {
  List<dynamic> _recommendations = [];
  int _totalProtein = 0;
  int _totalCarbs = 0;
  int _totalFats = 0;
  int _totalCalories = 0;

  @override
  void initState() {
    super.initState();
    _fetchRecommendations();
  }

  Future<void> _fetchRecommendations() async {
    final prefs = await SharedPreferences.getInstance();
    final userId = prefs.getString('userId');
    final formattedDate = DateFormat('yyyy-MM-dd').format(widget.selectedDate); // Use this

    if (userId != null) {
      final url = 'http://localhost:8081/api/recommendations?userId=$userId&mealType=${widget.mealType}&date=$formattedDate';
      final response = await http.get(Uri.parse(url));

      if (response.statusCode == 200) {
        final data = json.decode(response.body);
        if (data.isEmpty) {
          print('No recommendations found for mealType: ${widget.mealType}');
          setState(() {
            _recommendations = [];
          });
        } else {
          setState(() {
            _recommendations = data;
            _totalProtein = data[0]['totalProtein'] ?? 0;
            _totalCarbs = data[0]['totalCarbs'] ?? 0;
            _totalFats = data[0]['totalFats'] ?? 0;
            _totalCalories = data[0]['totalCalories'] ?? 0;
          });
        }
      } else {
        print('Failed to fetch recommendations');
      }
    }
  }

  Widget _buildRecommendationCard(Map<String, dynamic> recommendation) {
    final item = recommendation['item'];
    final proteinMatch = recommendation['proteinMatch'];
    final carbsMatch = recommendation['carbsMatch'];
    final fatsMatch = recommendation['fatsMatch'];
    final caloriesMatch = recommendation['caloriesMatch'];

    return Card(
      child: ListTile(
        title: Text(item['name']),
        subtitle: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text('Protein: ${item['protein']} g ($proteinMatch%)'),
            Text('Carbohydrates: ${item['carbs']} g ($carbsMatch%)'),
            Text('Fats: ${item['fats']} g ($fatsMatch%)'),
            Text('Calories: ${item['calories']} kcal ($caloriesMatch%)'),
          ],
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Recommendations')),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text('Total Protein: $_totalProtein g'),
            Text('Total Carbs: $_totalCarbs g'),
            Text('Total Fats: $_totalFats g'),
            Text('Total Calories: $_totalCalories kcal'),
            const SizedBox(height: 16),
            Expanded(
              child: _recommendations.isEmpty
                  ? const Center(child: CircularProgressIndicator())
                  : ListView(
                children: _recommendations
                    .map((item) => _buildRecommendationCard(item))
                    .toList(),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
