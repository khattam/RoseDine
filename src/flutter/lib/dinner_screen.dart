import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'MenuForDate.dart';
import 'menu_repository.dart';

class DinnerScreen extends StatelessWidget {
  final DateTime selectedDate;

  const DinnerScreen({Key? key, required this.selectedDate}) : super(key: key);

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
          final dinnerItems = menuForDate.getMealsForType('Dinner');

          return ListView.builder(
            itemCount: dinnerItems.length,
            itemBuilder: (context, index) => ListTile(title: Text(dinnerItems[index])),
          );
        } else {
          return const Center(child: Text('No dinner data available.'));
        }
      },
    );
  }
}
