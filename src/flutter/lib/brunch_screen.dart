import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'MenuForDate.dart';
import 'menu_repository.dart';

class BrunchScreen extends StatelessWidget {
  final DateTime selectedDate;

  const BrunchScreen({Key? key, required this.selectedDate}) : super(key: key);

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
          final brunchItems = menuForDate.getMealsForType('Brunch');

          return ListView.builder(
            itemCount: brunchItems.length,
            itemBuilder: (context, index) => ListTile(title: Text(brunchItems[index])),
          );
        } else {
          return const Center(child: Text('No brunch data available.'));
        }
      },
    );
  }
}
