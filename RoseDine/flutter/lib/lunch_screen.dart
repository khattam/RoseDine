import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'MenuForDate.dart';
import 'menu_repository.dart';

class LunchScreen extends StatelessWidget {
  final DateTime selectedDate;

  const LunchScreen({Key? key, required this.selectedDate}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final dateString = DateFormat('yyyy-MM-dd').format(selectedDate);

    return FutureBuilder<Map<String, dynamic>>(
      future: MenuRepository().loadMenuData(),
      builder: (context, snapshot) {
        if (snapshot.connectionState == ConnectionState.waiting) {
          return const Center(child: CircularProgressIndicator());
        } else if (snapshot.hasError) {
          return Center(child: Text('Error: ${snapshot.error}'));
        } else if (snapshot.hasData) {
          final menuForDate = MenuForDate.fromJson(snapshot.data![dateString] ?? {});
          final lunchItems = menuForDate.getMealsForType('Lunch');

          return ListView.builder(
            itemCount: lunchItems.length,
            itemBuilder: (context, index) => ListTile(title: Text(lunchItems[index])),
          );
        } else {
          return const Center(child: Text('No lunch data available.'));
        }
      },
    );
  }
}
